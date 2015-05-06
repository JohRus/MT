package opencellid;

import java.awt.geom.Point2D;
import java.io.FileWriter;
import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

import infrastructure.DefaultCell;
import infrastructure.Measurement;
import infrastructure.SimpleMeasurement;

public class JSONFile {

	public final static String filePathCellTowers = "/Users/Johan/Documents/CellTowers/";
	public final static String filePathDesktop = "/Users/Johan/Desktop/ComputedCells/";
	public final static String fileFormat = ".json";
	
	FileWriter fw;
	JsonGenerator jg;

	public JSONFile(String newFileName) {
		initGenerator(newFileName);
//		initGenerator(filePathCellTowers+fileName+fileFormat);
	}

	private void initGenerator(String completeFileName) {
		JsonFactory jf = new JsonFactory();
		try {
			fw = new FileWriter(completeFileName, true);
			jg = jf.createJsonGenerator(fw);
			jg.writeStartObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeResultForMap(OpenCellIdCell originalCell, DefaultCell chosenHeuristicCell, double error) {
		try {
			jg.writeObjectFieldStart("cell");
			jg.writeObjectField("lon", originalCell.getCellTowerCoordinates().getX());
			jg.writeObjectField("lat", originalCell.getCellTowerCoordinates().getY());
			jg.writeEndObject();
			
			jg.writeObjectFieldStart("calculatedCell");
			jg.writeObjectField("lon", chosenHeuristicCell.getCellTowerCoordinates().getX());
			jg.writeObjectField("lat", chosenHeuristicCell.getCellTowerCoordinates().getY());
			jg.writeObjectField("errorDist", error);
			jg.writeEndObject();
			
			jg.writeArrayFieldStart("measurements");
			for(Measurement measurement : originalCell.getMeasurements()) {
				jg.writeStartObject();
				jg.writeObjectField("lon", measurement.getCoordinates().getX());
				jg.writeObjectField("lat", measurement.getCoordinates().getY());
				jg.writeEndObject();
			}
			jg.writeEndArray();
			
			iAmDoneWriting();
			
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void iAmDoneWriting() {
		try {
			jg.writeEndObject();
			jg.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void iWannaStartAnArray(String arrayName) {
		try {
			jg.writeArrayFieldStart(arrayName);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void theArrayIsDone() {
		try {
			jg.writeEndArray();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeThisOpenCellIdCell(OpenCellIdCell openCellIdCell) {
		try {
			jg.writeStartObject();
			jg.writeObjectField("lon", openCellIdCell.getCellTowerCoordinates().getX());
			jg.writeObjectField("lat", openCellIdCell.getCellTowerCoordinates().getY());
			jg.writeObjectField("radio", openCellIdCell.getRadio());
			jg.writeObjectField("mcc", openCellIdCell.getMcc());
			jg.writeObjectField("net", openCellIdCell.getNet());
			jg.writeObjectField("area", openCellIdCell.getArea());
			jg.writeObjectField("cell", openCellIdCell.getCell());
			jg.writeObjectField("range", openCellIdCell.getRange());
			jg.writeObjectField("samples", openCellIdCell.getSamples());
			jg.writeObjectField("changeable", openCellIdCell.getChangeable());
			jg.writeObjectField("averageSignal", openCellIdCell.getAverageSignal());
			
			iWannaStartAnArray("measurements");
			for(Measurement measurement : openCellIdCell.getMeasurements()) {
				writeThisMeasurement(measurement);
			}
			theArrayIsDone();
			
			jg.writeEndObject();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void writeThisMeasurement(Measurement measurement) {
		try {
			jg.writeStartObject();
			jg.writeObjectField("lon", measurement.getCoordinates().getX());
			jg.writeObjectField("lat", measurement.getCoordinates().getY());
			jg.writeObjectField("signalStrength", measurement.getSignalStrength());
			jg.writeEndObject();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		OpenCellIdCell cell1 = new OpenCellIdCell(new Point2D.Double(200.0, 200.0), "GSM", 260, 2, 5245, 25322, 30, 100, 0, -80);
		Measurement m1 = new SimpleMeasurement(new Point2D.Double(223.0, 1234.0), -87);
		Measurement m2 = new SimpleMeasurement(new Point2D.Double(233.0, 457.0), -94);
		cell1.addMeasurement(m1);
		cell1.addMeasurement(m2);
		OpenCellIdCell cell2 = new OpenCellIdCell(new Point2D.Double(52.0, 345.0), "LTE", 262, 1, 653, 34522, 25, 1000, 0, -90);
		cell2.addMeasurement(new SimpleMeasurement(new Point2D.Double(253.0, 758.0), -50));
		
		JSONFile jsonFile = new JSONFile("test2");
		jsonFile.iWannaStartAnArray("cells");
		jsonFile.writeThisOpenCellIdCell(cell1);
		jsonFile.writeThisOpenCellIdCell(cell2);
		jsonFile.theArrayIsDone();
		jsonFile.iAmDoneWriting();
	}

}
