package opencellid;


import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;

import logic.Generate;
import logic.Geom;
import logic.Process;
import infrastructure.Computation;
import infrastructure.DefaultCell;
import infrastructure.Measurement;

public class Controller {

	public void errors(
			List<OpenCellIdCell> cells, 
			int maxMeasurements, 
			int minMeasurements, 
			int[] nArray, 
			double[] dArray) {
		
		System.out.printf("Cells\tMaxMeas\tMinMeas\tn\td\terror_distance\terror_RSS\terror_average\n");
		
		for(int i = 0; i < nArray.length; i++) {
			int n = nArray[i];

			for(int j = 0; j < dArray.length; j++) {
				double d = dArray[j];

				double errorDistance = 0.0;
				double errorRSS = 0.0;
				double errorAverage = 0.0;
				
				for(OpenCellIdCell cell : cells) {
					Computation compDistance = Generate.computation(cell, n, d, false);
					Computation compRSS = Generate.computation(cell, n, d, true);
					DefaultCell cellByAveraging = Process.averageCellTowerPosition(cell.getMeasurements());
					
					
					errorDistance += Generate.sphericalError(cell, compDistance.getHeuristicCell1());
					errorRSS += Generate.sphericalError(cell, compRSS.getHeuristicCell1());
					errorAverage += Generate.sphericalError(cell, cellByAveraging);

				}
				errorDistance = errorDistance/cells.size();
				errorRSS = errorRSS/cells.size();
				errorAverage = errorAverage/cells.size();

				System.out.printf("%d\t%d\t%d\t%d\t%.3f\t%.2f\t%.2f\t%.2f\n", cells.size(), maxMeasurements, minMeasurements, n, d, errorDistance, errorRSS, errorAverage);
			}
		}
	}

	public List<OpenCellIdCell> parseCells(String fullFileName, int maxMeasurements, int minMeasurements) {
//		String fileName = "/Users/Johan/Documents/CellTowers/cells_exact_samples81-120.json";
		
		List<OpenCellIdCell> cells = null;
		
		try(JsonParser jp = new MappingJsonFactory().createJsonParser(new File(fullFileName))){

			JsonNode cellsNode = jp.readValueAsTree().get("cells");
			
			cells = new ArrayList<OpenCellIdCell>(cellsNode.size());
						
			if(cellsNode.isArray()) {
				for(JsonNode cellNode : cellsNode) {
					
					OpenCellIdCell cell = createCell(cellNode);
					
					JsonNode measurementsNode = cellNode.get("measurements");
					if(measurementsNode.isArray()) {
						
						List<Measurement> measurements = new ArrayList<Measurement>(cell.getSamples());
						
						for(JsonNode measurementNode : measurementsNode) {
							Measurement measurement = createMeasurement(measurementNode);
							if(measurementIsValid(measurement, cell))
								measurements.add(measurement);
						}
						cell.setMeasurements(measurements);
						
						if(cellIsValid(cell, maxMeasurements, minMeasurements))
							cells.add(cell);
					}
					else
						System.out.println("measurementsNode was not array");
				}
			}
			else 
				System.out.println("cellsNode was not array");

			System.out.println("Parsed "+cells.size()+" cells including measurements");
			System.out.println();
						

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cells;
	}
	
	public void writeToFileForVisualizing(List<OpenCellIdCell> cells, int cellsToWrite, boolean useRSS) {
		HashSet<Integer> items = Generate.randomInts(cellsToWrite, cells.size(), null);
		
		for(int i : items) {
			OpenCellIdCell cell = cells.get(i);
			
			Computation comp = Generate.computation(cell, 30, 0.001, useRSS);
			
			double error = Generate.sphericalError(cell, comp.getHeuristicCell1());
			
			String fullFileName = JSONFile.filePathDesktop;
			fullFileName += cell.getMcc()+"-"+cell.getNet()+"-"+cell.getArea()+"-"+cell.getCell();
			fullFileName += JSONFile.fileFormat;
			
			JSONFile jsonFile = new JSONFile(fullFileName);
			jsonFile.writeResultForMap(cell, comp.getHeuristicCell1(), error);
		}
		
		System.out.println("Wrote "+cellsToWrite+" computations to files");
		
	}
	
	protected static OpenCellIdCell createCell(JsonNode n) {
		double lon = n.get("lon").asDouble();
		double lat = n.get("lat").asDouble();
		Point2D.Double coords = new Point2D.Double(lon, lat);
		String radio = n.get("radio").asText();
		int mcc = n.get("mcc").asInt();
		int net = n.get("net").asInt();
		int area = n.get("area").asInt();
		long cell = n.get("cell").asLong();
		int range = n.get("range").asInt();
		int samples = n.get("samples").asInt();
		int changeable = n.get("changeable").asInt();
		int averageSignal = n.get("averageSignal").asInt();
		
		return new OpenCellIdCell(
				coords, 
				radio, 
				mcc, 
				net, 
				area, 
				cell, 
				range, 
				samples, 
				changeable, 
				averageSignal);
	}
	
	protected static Measurement createMeasurement(JsonNode n) {
		double lon = n.get("lon").asDouble();
		double lat = n.get("lat").asDouble();
		Point2D.Double coords = new Point2D.Double(lon, lat);
		int signalStrength = n.get("signalStrength").asInt();
		return new OpenCellIdMeasurement(coords, signalStrength);
	}
	
	private boolean measurementIsValid(Measurement measurement, OpenCellIdCell cell) {
		
		double distanceToCellTower = Geom.sphericalDistance(
				cell.getCellTowerCoordinates().getX(), 
				cell.getCellTowerCoordinates().getY(), 
				measurement.getCoordinates().getX(), 
				measurement.getCoordinates().getY());
		
		if(distanceToCellTower > 350000.0) {
			return false;
		}
		
		int signalStrength = measurement.getSignalStrength();
		
		if(signalStrength >= 32)
			return false;
		
		else if(signalStrength >= 0 && signalStrength <= 31) {
			int signalStrengthDBM = (2*signalStrength)-113;
			measurement.setSignalStrength(signalStrengthDBM);
		}
		return true;
	}
	
	private boolean cellIsValid(DefaultCell defaultCell, int maxMeasurements, int minMeasurements) {
		if(defaultCell.getMeasurements().size() > maxMeasurements)
			return false;
		else if(defaultCell.getMeasurements().size() < minMeasurements)
			return false;
		
		HashSet<Integer> signalValues = new HashSet<Integer>();
		for(Measurement m : defaultCell.getMeasurements()) {
			signalValues.add(m.getSignalStrength());
		}
		if(signalValues.size() <= 3)
			return false;
		
		return true;
	}



	public static void main(String[] args) {
		String fullFileName = "/Users/Johan/Documents/CellTowers/cells_exact_samples67-120_2036.json";
		
		int maxMeasurements = 300;
		int minMeasurements = 100;
		
		Controller controller = new Controller();
		List<OpenCellIdCell> cells = controller.parseCells(
				fullFileName,
				maxMeasurements,
				minMeasurements);
		controller.errors(cells, maxMeasurements, minMeasurements, new int[]{10,20,40,80}, new double[]{0.001});
		
//		Controller controller = new Controller();
//		List<OpenCellIdCell> cells = controller.parseCells(fullFileName, maxMeasurements, minMeasurements);
//		controller.writeToFileForVisualizing(cells, 10, true);
		
	}

}
