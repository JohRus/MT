package opencellid;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

import infrastructure.DefaultCell;
import infrastructure.Measurement;

public class JSONFile {
	
	private final String filePath = "/Users/Johan/Desktop/";
	
	private JsonGenerator jg;
	
	public JSONFile(DefaultCell originalCell, DefaultCell calculatedCell, 
			String fileName) {
		jsonGenerator(new File(filePath+fileName));
		write(originalCell, calculatedCell);
		
	}

	private void write(DefaultCell originalCell, DefaultCell calculatedCell) {
		try {
			jg.writeStartObject();
			jg.writeObjectFieldStart("cell");
			jg.writeObjectField("lon", originalCell.getCellTowerCoordinates().getX());
			jg.writeObjectField("lat", originalCell.getCellTowerCoordinates().getY());
			jg.writeEndObject();
			jg.writeObjectFieldStart("calculatedCell");
			jg.writeObjectField("lon", calculatedCell.getCellTowerCoordinates().getX());
			jg.writeObjectField("lat", calculatedCell.getCellTowerCoordinates().getY());
			jg.writeEndObject();
			jg.writeArrayFieldStart("measurements");
			for(Measurement m : originalCell.getMeasurements()) {
				jg.writeStartObject();
				jg.writeObjectField("lon", m.getCoordinates().getX());
				jg.writeObjectField("lat", m.getCoordinates().getY());
				jg.writeEndObject();
			}
			jg.writeEndArray();
			jg.writeEndObject();
			
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		
		
	}

	private void jsonGenerator(File toFile) {
		JsonFactory jsonFactory = new JsonFactory();
		try {
			jg = jsonFactory.createJsonGenerator(toFile, JsonEncoding.UTF8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void close() {
		try {
			jg.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
