package datahandler;

import java.io.File;

@Deprecated
public class Main {
	
	//private String connectionPoint = "localhost";

	public static void main(String[] args) {
		//String CSVFileString = "/Users/Johan/Workspace/BaseStations/measurements_18.csv";
		String CSVFileString = "/Volumes/My Passport/measurements_21.csv";
		File CSVFile = new File(CSVFileString);
		
		
		MeasurementsClient measurementsClient = new MeasurementsClient(CSVFile, 21);
		measurementsClient.run();
		
		System.out.println("End of program");
		return;
	}

}
