package datasets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.googlecode.jcsv.writer.CSVWriter;
import com.googlecode.jcsv.writer.internal.CSVWriterBuilder;

public class SortMeasurementsFile {
	
	private File fileToBeSorted;
	private String newFile;
	private String logFile;
	private List<MeasurementDto> measurements;
	
	private long startTime;
	
	public SortMeasurementsFile(String fileToBeSorted, String newFile, String logFile) {
		this.fileToBeSorted = new File(fileToBeSorted);
		this.newFile = newFile;
		this.logFile = logFile;
		this.measurements = new ArrayList<MeasurementDto>();
	}
	
	public void run() {
		startTime = System.currentTimeMillis();
		readAndParseFile();
		Collections.sort(measurements);
		writeToNewFile();
		logIt();
		return;
	}
	
	private void logIt() {
		try (PrintWriter pw = new PrintWriter(new FileWriter(this.logFile, true))) {
			pw.println("Parsed and sorted \""+fileToBeSorted+"\"");
			pw.printf("\tSize: "+this.measurements.size()+"\n");
			long time = (System.currentTimeMillis()-this.startTime)/1000;
			pw.printf("\tTime elapsed: "+time+" seconds\n");
			pw.printf("\tWrote to \""+newFile+"\"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	private void writeToNewFile() {
//		Writer out = null;
		try (Writer out = new FileWriter(this.newFile);){
//			out = new FileWriter(this.newFile);
			CSVWriter<MeasurementDto> csvMeasurementsWriter = new CSVWriterBuilder<MeasurementDto>(out)
					.strategy(CSVStrategy.UK_DEFAULT)
					.entryConverter(new MeasurementEntryConverter())
					.build();
			csvMeasurementsWriter.writeAll(this.measurements);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	public void readAndParseFile() {
//		Reader fileReader = null;
		try (Reader fileReader = new FileReader(this.fileToBeSorted);){
//			fileReader = new FileReader(this.fileToBeSorted);
			CSVReader<MeasurementDto> csvMeasurementsParser = new CSVReaderBuilder<MeasurementDto>(fileReader)
					.strategy(CSVStrategy.UK_DEFAULT)
					.entryParser(new MeasurementEntryParser())
					.build();
			this.measurements = csvMeasurementsParser.readAll();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return;
	}

	public static void main(String[] args) {
		for(int i = 1; i <= 21; i++) {
			System.out.println("Sorting nr. "+i);
			SortMeasurementsFile smf = new SortMeasurementsFile(
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
