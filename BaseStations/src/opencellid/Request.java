package opencellid;

import infrastructure.DefaultCell;
import infrastructure.Measurement;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;


public class Request {

	JsonParser jp;
	
	DefaultCell cell;

	public Request(String httpRequestURL) {
		jsonParser(httpRequestURL);
		doRequest();
	}
	
	public Request(String mcc, String net, String area, String cell) {
		jsonParser(generateRequestURL(mcc, net, area, cell));
		doRequest();
	}
	
	private String generateRequestURL(String mcc, String net, String area, String cell) {	
		String s = "http://opencellid.org/cell/getMeasures?key=710914bd-60b1-4ad6-aa96-faeb03eb16f8";
		s += "&mcc="+mcc;
		s += "&mnc="+net;
		s += "&lac="+area;
		s += "&cellid="+cell;
		s += "&radio=GSM&format=json";
		return s;
	}
	
	public DefaultCell getData() {
		return cell;
	}

	private void doRequest() {

		try {
			
			// jumping to START_OBJECT
			if(jp.nextToken() != JsonToken.START_OBJECT) {
				System.out.println("Root should be object: quitting");
				return;
			}
			
			// jumping to fieldName "cell"
			jp.nextToken();

			
			// jumping to START_OBJECT
			jp.nextToken();
			JsonNode n = jp.readValueAsTree();
			
			cell = createCell(n);

			
			// jumping to fieldName "measurements"
			jp.nextToken();
			
			// jumping to START_ARRAY
			jp.nextToken();
			
			OpenCellIdCell openCellIdCell = (OpenCellIdCell) cell;
			List<Measurement> measurements = new ArrayList<Measurement>(openCellIdCell.getSamples());
			int measurementsCounter = 0;
			
			// jumping to either the next START_OBJECT or END_ARRAY
			while(jp.nextToken() != JsonToken.END_ARRAY) {
				n = jp.readValueAsTree();
				measurements.add(createMeasurement(n));
				measurementsCounter++;
			}
			cell.setMeasurements(measurements);
			
			if(measurementsCounter != openCellIdCell.getSamples()) {
				System.out.println("The number of measurements in file is not equal to the number of samples");
			}
						
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	private DefaultCell createCell(JsonNode n) {
		double lon = n.get("lon").asDouble();
		double lat = n.get("lat").asDouble();
		Point2D.Double coords = new Point2D.Double(lon, lat);
		String radio = n.get("radio").asText();
		int mcc = n.get("mcc").asInt();
		int net = n.get("mnc").asInt();
		int area = n.get("lac").asInt();
		long cell = n.get("cellid").asLong();
		int range = n.get("range").asInt();
		int samples = n.get("samples").asInt();
		int changeable = n.get("changeable").asInt();
		int averageSignal = n.get("averageSignalStrength").asInt();
		
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
	
	private Measurement createMeasurement(JsonNode n) {
		double lon = n.get("lon").asDouble();
		double lat = n.get("lat").asDouble();
		Point2D.Double coords = new Point2D.Double(lon, lat);
		int signalStrength = n.get("signal").asInt();
		int mcc = n.get("mcc").asInt();
		int net = n.get("mnc").asInt();
		int area = n.get("lac").asInt();
		long cell = n.get("cellid").asLong();
		
		return new OpenCellIdMeasurement(
				coords, 
				signalStrength, 
				mcc, 
				net, 
				area, 
				cell);
	}

	private void jsonParser(String inputString) {
		try {
			URL url = new URL(inputString);
			JsonFactory f = new MappingJsonFactory();
			jp = f.createJsonParser(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void close() {
		try {
			jp.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String in = "http://opencellid.org/cell/getMeasures?key=710914bd-60b1-4ad6-aa96-faeb03eb16f8&mcc=260&mnc=1&lac=29001&cellid=22094&radio=GSM&format=json";
		Request r = new Request(in);
		DefaultCell defaultCell = r.getData();
	}

}
