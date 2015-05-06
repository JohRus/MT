package opencellid;


import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.javatuples.Pair;
import org.javatuples.Quartet;

import logic.Generate;
import logic.Geom;
import logic.Process;
import logic.Stopwatch;
import gui.Charts4J;
import infrastructure.Computation;
import infrastructure.DefaultCell;
import infrastructure.Measurement;

public class Controller {

	public void errorsScalingD(
			String fullFileName, 
			int maxMeasurements, 
			int minMeasurements, 
			int[] nArray, 
			double[] dArray,
			double r) {
		
		List<OpenCellIdCell> cells = parseCells(fullFileName, maxMeasurements, minMeasurements, r);
		
		HashMap<double[], String> chartMap = new HashMap<double[], String>();
		
		System.out.printf("Cells\tMaxMeas\tMinMeas\tr\tn\td\terror_dist\ttime_dist\terror_RSS\ttime_RSS\terror_average\taverage_fit\n");
		
		Pair<Double, Integer> averagedError = errorByAverage(cells);
		double errorAverage = averagedError.getValue0();
		int averageFit = averagedError.getValue1();
		
		
		for(int i = 0; i < nArray.length; i++) {
			int n = nArray[i];
			
			double[] distanceDataPoints = new double[dArray.length];
			double[] rssDataPoints = new double[dArray.length];

			for(int j = 0; j < dArray.length; j++) {
				double d = dArray[j];
				
				Quartet<Double, Double, Double, Double> data = errors(cells, n, d);
				
				double errorDistance = data.getValue0();
				double timeDistance = data.getValue1();
				double errorRSS = data.getValue2();
				double timeRSS = data.getValue3();
				

				System.out.printf("%d\t%d\t%d\t%.1f\t%d\t%.5f\t%.2f \t%.5f\t\t%.2f \t%.5f\t\t%.2f\t\t%d\n", cells.size(), maxMeasurements, minMeasurements, r, n, d, errorDistance, timeDistance, errorRSS, timeRSS, errorAverage, averageFit);
			
				distanceDataPoints[j] = errorDistance;
				rssDataPoints[j] = errorRSS;
			}
			chartMap.put(distanceDataPoints, new String("LV=Distance, n="+n));
			chartMap.put(rssDataPoints, new String("LV=RSS, n="+n));
		}
		
		String chartURL = Charts4J.errorChartScalingD(chartMap);
		System.out.println("\n"+chartURL);
	}
	
	public void errorsScalingR(
			String fullFileName, 
			int maxMeasurements, 
			int minMeasurements,
			int[] nArray, 
			double d,
			double[] rArray) {
		
		
		double[][] dataPointsDistanceError = new double[rArray.length][nArray.length];
		double[][] dataPointsRSSError = new double[rArray.length][nArray.length];
		
		double[][] dataPointsDistanceTime = new double[rArray.length][nArray.length];
		double[][] dataPointsRSSTime = new double[rArray.length][nArray.length];
		
		double[] dataPointsAveragedError = new double[rArray.length];
		int[] dataPointsAveragedFit = new int[rArray.length];
		
		int[] cellsSizes = new int[rArray.length];
		

		for(int i = 0; i < rArray.length; i++) {
			double currR = rArray[i];
			
			List<OpenCellIdCell> cells = parseCells(
					fullFileName,
					maxMeasurements,
					minMeasurements,
					currR);
			
			cellsSizes[i] = cells.size();
			
			for(int j = 0; j < nArray.length; j++) {
				int n = nArray[j];
				
				Quartet<Double, Double, Double, Double> data = errors(cells, n, d);
				
				double errorDistance = data.getValue0();
				double timeDistance = data.getValue1();
				double errorRSS = data.getValue2();
				double timeRSS = data.getValue3();
				
				dataPointsDistanceError[i][j] = errorDistance;
				dataPointsRSSError[i][j] = errorRSS;
				
				dataPointsDistanceTime[i][j] = timeDistance;
				dataPointsRSSTime[i][j] = timeRSS;
			}
			
			Pair<Double, Integer> averagedError = errorByAverage(cells);
			
			double errorAverage = averagedError.getValue0();
			int averageFit = averagedError.getValue1();
			
			dataPointsAveragedError[i] = errorAverage;
			dataPointsAveragedFit[i] = averageFit;
		}
		
		System.out.printf("Cells\tMaxMeas\tMinMeas\td\tn\tr\terror_dist\ttime_dist\terror_RSS\ttime_RSS\terror_average\taverage_fit\n");
		
		HashMap<double[], String> dataPointsMap = new HashMap<double[], String>();
		
		for(int j = 0; j < nArray.length; j++) {
			int n = nArray[j];
			
			double[] dataPointsErrorDistance = new double[rArray.length];
			double[] dataPointsErrorRSS = new double[rArray.length];
			
			for(int i = 0; i < rArray.length; i++) {
				
				int cellSize = cellsSizes[i];
				double r = rArray[i];
				double errorDistance = dataPointsDistanceError[i][j];
				double timeDistance = dataPointsDistanceTime[i][j];
				double errorRSS = dataPointsRSSError[i][j];
				double timeRSS = dataPointsRSSTime[i][j];
				double errorAverage = dataPointsAveragedError[i];
				int averageFit = dataPointsAveragedFit[i];
				
				System.out.printf("%d\t%d\t%d\t%.4f\t%d\t%.1f\t%.2f \t%.5f\t\t%.2f \t%.5f\t\t%.2f\t\t%d\n", cellSize, maxMeasurements, minMeasurements, d, n, r, errorDistance, timeDistance, errorRSS, timeRSS, errorAverage, averageFit);

				dataPointsErrorDistance[i] = errorDistance;
				dataPointsErrorRSS[i] = errorRSS;
			}
			
			dataPointsMap.put(dataPointsErrorDistance, "LV=Distance, n="+n);
			dataPointsMap.put(dataPointsErrorRSS, "LV=RSS, n="+n);
			
		}
		
		dataPointsMap.put(dataPointsAveragedError, "Averaged");
		
		String chartURL = Charts4J.errorChartScalingR(dataPointsMap, cellsSizes);
		System.out.println("\n"+chartURL);
		
	}
	
	private Quartet<Double, Double, Double, Double> errors(List<OpenCellIdCell> cells, int n, double d) {
		double errorDistance = 0.0;
		double errorRSS = 0.0;
		
		double timeDistance = 0.0;
		double timeRSS = 0.0;
		
		for(OpenCellIdCell cell : cells) {
			
			double errorCellDistance = 0.0;
			double errorCellRSS = 0.0;
			
			Stopwatch stopWatchCellDistance = new Stopwatch();
			Stopwatch stopWatchCellRSS = new Stopwatch();
			
			for(int k = 0; k < 5; k++) {
				stopWatchCellDistance.start();
				Computation compDistance = Generate.computation(cell, n, d, false);
				stopWatchCellDistance.stop();
				
				stopWatchCellRSS.start();
				Computation compRSS = Generate.computation(cell, n, d, true);
				stopWatchCellRSS.stop();
				
				errorCellDistance += Generate.sphericalError(cell, compDistance.getHeuristicCell1());
				errorCellRSS += Generate.sphericalError(cell, compRSS.getHeuristicCell1());
			}
			
			errorCellDistance = errorCellDistance/5.0;
			errorCellRSS = errorCellRSS/5.0;
			
			errorDistance += errorCellDistance;
			errorRSS += errorCellRSS;
			
			timeDistance += stopWatchCellDistance.averageTime();
			timeRSS += stopWatchCellRSS.averageTime();
					
		}
		errorDistance = errorDistance/cells.size();
		errorRSS = errorRSS/cells.size();
		
		timeDistance = timeDistance/cells.size();
		timeRSS = timeRSS/cells.size();
		
		return new Quartet<Double, Double, Double, Double>(errorDistance, timeDistance, errorRSS, timeRSS);
	}
	
	private Pair<Double, Integer> errorByAverage(List<OpenCellIdCell> cells) {
		double errorAverage = 0.0;
		int averageFit = 0;
		
		for(OpenCellIdCell cell : cells) {
			DefaultCell cellByAveraging = Process.averagedCell(cell.getMeasurements());
			
			errorAverage += Generate.sphericalError(cell, cellByAveraging);
			averageFit += cellByAveraging.getMeasurements().size();
		}
		
		errorAverage = errorAverage/cells.size();
		averageFit = averageFit/cells.size();
		
		return new Pair<Double, Integer>(errorAverage, averageFit);
	}

	public List<OpenCellIdCell> parseCells(String fullFileName, int maxMeasurements, int minMeasurements, double r) {
		
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
							if(measurementIsValid(measurement, cell, r))
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

			System.out.println("Parsed "+cells.size()+" cells including measurements with r_include = "+r);
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
	
	public void writeRandomCellsToFileForVisualizing(List<OpenCellIdCell> cells, int cellsToWrite, boolean useRSS) {
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
	
	public void writeCellWithComputationsToFilesForVisualization(
			String fullFileName,
			int maxMeas, 
			int minMeas,
			int mcc,
			int net,
			int area,
			long cellid,
			int n,
			double d,
			double r) {
		
		List<OpenCellIdCell> cells = parseCells(fullFileName, maxMeas, minMeas, r);
		for(OpenCellIdCell cell :  cells) {
			if(cell.getMcc() == mcc && cell.getNet() == net && cell.getArea() == area && cell.getCell() == cellid) {
				
				System.out.println("Measurements: "+cell.getMeasurements().size());
				
				Computation compDist = Generate.computation(cell, n, d, false);
				Computation compRSS = Generate.computation(cell, n, d, true);
				Point2D.Double averagedCellTowerPos = Process.averagedCellTowerPosition(cell.getMeasurements());
				DefaultCell averagedCell = new DefaultCell(averagedCellTowerPos, 120.0);
				
				double errorDist = Generate.sphericalError(cell, compDist.getHeuristicCell1());
				double errorRSS = Generate.sphericalError(cell, compRSS.getHeuristicCell1());
				double errorAver = Generate.sphericalError(cell, averagedCell);
				
				String newFileNameFirst = JSONFile.filePathDesktop;
				newFileNameFirst += cell.getMcc()+"-"+cell.getNet()+"-"+cell.getArea()+"-"+cell.getCell();
				
				String newFileNameDist = newFileNameFirst + "_distance"+JSONFile.fileFormat;
				String newFileNameRSS = newFileNameFirst + "_RSS"+JSONFile.fileFormat;
				String newFileNameAver = newFileNameFirst + "_averaged"+JSONFile.fileFormat;
				
				JSONFile jsonFile = new JSONFile(newFileNameDist);
				jsonFile.writeResultForMap(cell, compDist.getHeuristicCell1(), errorDist);
				
				jsonFile = new JSONFile(newFileNameRSS);
				jsonFile.writeResultForMap(cell, compRSS.getHeuristicCell1(), errorRSS);
				
				jsonFile = new JSONFile(newFileNameAver);
				jsonFile.writeResultForMap(cell, averagedCell, errorAver);
				
				
				System.out.println("Done writin cells to files");
				return;
			}
		}
	}
	
	public void cellError() {
		List<OpenCellIdCell> cells = parseCells(
				"/Users/Johan/Documents/CellTowers/cells_exact_samples67-120_2036.json", 
				150, 
				100,
				50000);
		for(OpenCellIdCell cell : cells) {
			if(cell.getMcc() == 262 && cell.getNet() == 7 && cell.getArea() == 30605 && cell.getCell() == 342) {
				System.out.println("Measurements: "+cell.getMeasurements().size());
				
				Computation compDist = Generate.computation(cell, 40, 0.0001, false);
				Computation compRSS = Generate.computation(cell, 40, 0.0001, true);
				
				double errorDist = Generate.sphericalError(cell, compDist.getHeuristicCell1());
				double errorRSS = Generate.sphericalError(cell, compRSS.getHeuristicCell1());
				
				System.out.println("LV=Dist: "+compDist.getHeuristicCell1().getCellTowerCoordinates()+", error: "+errorDist);
				System.out.println("LV=RSS: "+compRSS.getHeuristicCell1().getCellTowerCoordinates()+", error: "+errorRSS);
				
				break;
			}
		}
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
	
	private boolean measurementIsValid(Measurement measurement, OpenCellIdCell cell, double r) {
		
		double distanceToCellTower = Geom.sphericalDistance(
				cell.getCellTowerCoordinates().getX(), 
				cell.getCellTowerCoordinates().getY(), 
				measurement.getCoordinates().getX(), 
				measurement.getCoordinates().getY());
		
		if(distanceToCellTower > r) {
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
	
	private boolean cellIsValid(OpenCellIdCell cell, int maxMeasurements, int minMeasurements) {
		
		HashSet<Integer> signalValues = new HashSet<Integer>();
		for(Measurement m : cell.getMeasurements()) {
			signalValues.add(m.getSignalStrength());
		}
		if(signalValues.size() <= 3)
			return false;
		
		if(cell.getMeasurements().size() > maxMeasurements)
			return false;
		else if(cell.getMeasurements().size() < minMeasurements)
			return false;
		
		return true;
	}



	public static void main(String[] args) {
		String fullFileName = "/Users/Johan/Documents/CellTowers/cells_exact_samples67-120_2036.json";
		
		int maxMeasurements = 300;
		int minMeasurements = 100;
		double r = 100000.0;
	
		Controller controller = new Controller();
//		controller.errorsScalingD(cells, maxMeasurements, minMeasurements, new int[]{10}, new double[]{0.0001}, r);
				
//		controller.errorsScalingR(fullFileName, maxMeasurements, minMeasurements, new int[]{10,20,40,80,160,320,640}, 0.0001, new double[]{35000.0,25000.0,15000.0,10000.0,5000.0,2000.0});
		
//		List<OpenCellIdCell> cells = controller.parseCells(fullFileName, maxMeasurements, minMeasurements, r);
//		controller.writeRandomCellsToFileForVisualizing(cells, 10, true);
		
		controller.writeCellWithComputationsToFilesForVisualization(
				fullFileName, 
				maxMeasurements, 
				minMeasurements, 
				260, 
				1, 
				29001, 
				22095, 
				80, 
				0.0001, 
				r);
		
		
//		controller.cellError();
		
	}

}
