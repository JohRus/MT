package datahandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import datasets.MeasurementDto;

@Deprecated
public class MeasurementsClient {
	
	private CassandraClient cassandraClient;
	private File CSVFile;
	private int measurementsFileNumber;
	
	public MeasurementsClient(File CSVFile, int measurementsFileNumber) {
		this.cassandraClient = new CassandraClient();
		this.cassandraClient.connect();
		this.cassandraClient.prepareToInsertMeasurements();
		this.CSVFile = CSVFile;
		this.measurementsFileNumber = measurementsFileNumber;
		
	}
	
	public void run() {
		this.copyFromCSVToCassandra();
		this.cassandraClient.close();
		System.out.println("Returning from MeasurementsClient");
		return;
	}

	private void copyFromCSVToCassandra() {
		try(BufferedReader bufferedReader = new BufferedReader(new FileReader(CSVFile))) {
			int lineCounter = 0;
			String line = "";
			while((line = bufferedReader.readLine()) != null) {
				lineCounter++;
				String[] lineSplit = line.split(",");
				MeasurementDto measurement = new MeasurementDto();
				measurement.setMcc(Integer.parseInt(lineSplit[0]));
				measurement.setNet(Integer.parseInt(lineSplit[1]));
				measurement.setArea(Integer.parseInt(lineSplit[2]));
				measurement.setCell(Long.parseLong(lineSplit[3]));
				measurement.setLon(Double.parseDouble(lineSplit[4]));
				measurement.setLat(Double.parseDouble(lineSplit[5]));
//				System.out.println("Lon: "+measurement.getLon()+", Lat: "+measurement.getLat());
				measurement.setSignal(lineSplit[6].equals("") ? 99 : Integer.parseInt(lineSplit[6]));
				measurement.setMeasured(new Date(Long.parseLong(lineSplit[7])));
//				measurement.setFromMeasurementsFile(measurementsFileNumber);
				cassandraClient.insertMeasurement(measurement);
				if(lineCounter % 10000 == 0) {
					System.out.printf("Parsed %d lines from CSV file and inserted into Cassandra \n", lineCounter);
				}
			}
			
			System.out.println("End of file..");
			System.out.printf("Parsed %d lines from CSV file and inserted into Cassandra \n", lineCounter);
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
		
	}
}
