package datasets;

import io.IOClient;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FilterOutOneCountryFromMeasurementFile {

	public static void main(String[] args) {
		String sourceFileName = "/Volumes/My Passport/measurements_all/measurements_23.csv";
		String destinationFileName = "/Volumes/My Passport/measurements_260_raw/measurements_260_23.csv";
		int mcc = 260;
		
		long startTime = System.currentTimeMillis();
		
		Iterator<MeasurementDto> iterator = IOClient.readMeasurementsFromCSVFileAndReturnIterator(sourceFileName);
		
		List<MeasurementDto> batch = new ArrayList<MeasurementDto>(1000);
		
		int counter = 0;
		while(iterator.hasNext()) {
			MeasurementDto measurement = iterator.next();
			if(measurement.getMcc() == mcc) {
				batch.add(measurement);
				counter++;
			}
			if(counter % 10000 == 0) {
				IOClient.writeMeasurementsToCSVFile(batch, destinationFileName, true);
				batch.clear();
//				long currentTime = (System.currentTimeMillis()-startTime)/1000;
//				System.out.println(currentTime+" seconds");
			}
		}
		
		IOClient.writeMeasurementsToCSVFile(batch, destinationFileName, true);
		long currentTime = (System.currentTimeMillis()-startTime)/1000;
		System.out.println(currentTime+" seconds");
		
		System.out.println("Elements filtered ut: "+counter);
		
		IOClient.closeIteratorReader();
		
		System.out.println("END OF PROGRAM");
		return;
	}

}
