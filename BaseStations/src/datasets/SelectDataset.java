package datasets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;

public class SelectDataset {

	private static String pathToExternalHardrive = "/Volumes/My Passport";

	public static Map<CellID, List<MeasurementDto>> start() {
		List<MeasurementDto> measurements = new ArrayList<MeasurementDto>();
		Scanner scanner = new Scanner(System.in);
		System.out.println("To select all measurements, press 1");
		System.out.println("To select all polish measurements, press 2");
		System.out.println("To go back one step, press 9");
		int command = scanner.nextInt();
		if(command == 1) {

		}
		else if(command == 2) {
			String pathToDirectory = pathToExternalHardrive+"/measurements_poland/";
			Reader fileReader;
			for(int i = 1; i <= 21; i++) {
				System.out.println("Parsing Poland file number "+i);
				String pathToMeasurementsFile = pathToDirectory+"measurements_260_"+i+".csv";
				try {
					fileReader = new FileReader(pathToMeasurementsFile);
					CSVReader<MeasurementDto> csvMeasurementsParser = new CSVReaderBuilder<MeasurementDto>(fileReader)
							.strategy(CSVStrategy.UK_DEFAULT)
							.entryParser(new MeasurementsEntryParser())
							.build();
					measurements = csvMeasurementsParser.readAll();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(command == 3) {

		}
		else {

		}
		scanner.close();
		System.out.println("Done parsing files");
		return sortMeasurements(measurements);
	}
	
	private static Map<CellID, List<MeasurementDto>> sortMeasurements(List<MeasurementDto> measurements) {
		Map<CellID, List<MeasurementDto>> dataMap = new HashMap<CellID, List<MeasurementDto>>();
		Iterator<MeasurementDto> iterator = measurements.iterator();
		while(iterator.hasNext()) {
			MeasurementDto measurement = iterator.next();
			CellID cellID = new CellID(measurement.getMcc(), measurement.getNet(), measurement.getArea(), measurement.getCell());
			if(dataMap.containsKey(cellID)) {
				dataMap.get(cellID).add(measurement);
			}
			else {
				List<MeasurementDto> measurementList = new ArrayList<MeasurementDto>();
				measurementList.add(measurement);
				dataMap.put(cellID, measurementList);
			}
		}
		return dataMap;
	}
}
