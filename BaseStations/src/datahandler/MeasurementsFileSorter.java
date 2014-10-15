package datahandler;

import io.IOClient;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import datasets.MeasurementDto;

public class MeasurementsFileSorter {
	
//	private Logger logger;
	
	private File file;
	private String newFileNameLocation;
//	private String logFile;
	
//	private long startTime;
	
	public MeasurementsFileSorter(File file, String newSortedFileLocation, String logFile) {
		this.file = file;
		this.newFileNameLocation = newSortedFileLocation;
//		this.logFile = logFile;
	}
	
	public void run() {
//		startTime = System.currentTimeMillis();
		List<MeasurementDto> measurements = IOClient.readMeasurementsFromCSVFile(this.file);
		Collections.sort(measurements);
		IOClient.writeMeasurementsToCSVFile(measurements, new File(newFileNameLocation));
//		logIt();
		return;
	}
	
//	private void logIt() {
//		try (PrintWriter pw = new PrintWriter(new FileWriter(this.logFile, true))) {
//			pw.println("Parsed and sorted \""+file+"\"");
//			pw.printf("\tSize: "+this.measurements.size()+"\n");
//			long time = (System.currentTimeMillis()-this.startTime)/1000;
//			pw.printf("\tTime elapsed: "+time+" seconds\n");
//			pw.printf("\tWrote to \""+newSortedFile+"\"\n");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return;
//	}

//	private void writeToNewFile() {
////		Writer out = null;
//		try (Writer out = new FileWriter(this.newFile);){
////			out = new FileWriter(this.newFile);
//			CSVWriter<MeasurementDto> csvMeasurementsWriter = new CSVWriterBuilder<MeasurementDto>(out)
//					.strategy(CSVStrategy.UK_DEFAULT)
//					.entryConverter(new MeasurementEntryConverter())
//					.build();
//			csvMeasurementsWriter.writeAll(this.measurements);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return;
//	}
//
//	private void readAndParseFile() {
////		Reader fileReader = null;
//		try (Reader fileReader = new FileReader(this.fileToBeSorted);){
////			fileReader = new FileReader(this.fileToBeSorted);
//			CSVReader<MeasurementDto> csvMeasurementsParser = new CSVReaderBuilder<MeasurementDto>(fileReader)
//					.strategy(CSVStrategy.UK_DEFAULT)
//					.entryParser(new MeasurementEntryParser())
//					.build();
//			this.measurements = csvMeasurementsParser.readAll();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//		return;
//	}

	public static void main(String[] args) {
		for(int i = 1; i <= 21; i++) {
			System.out.println("Sorting nr. "+i);
			MeasurementsFileSorter smf = new MeasurementsFileSorter(
					"/Volumes/My Passport/measurements_260_raw/measurements_260_"+i+".csv",
					"/Volumes/My Passport/measurements_260_sorted/measurements_260_"+i+".csv",
					"/Volumes/My Passport/measurements_260_sorted/log.log");
			smf.run();
			System.out.println("Sorting nr. "+i+" complete");
		}
		System.out.println("END END END");
		return;
	}

}
