package datasets;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.writer.CSVWriter;
import com.googlecode.jcsv.writer.internal.CSVWriterBuilder;

public class IOClient {
	
	public static void writeCellTowersToCSVFile(String fileName, boolean append, List<CellTowerDto> cellTowers) {
		System.out.println("Writing Cell Towers to file "+fileName);

		Writer out;
		try {
			out = new FileWriter(fileName, append);
			CSVWriter<CellTowerDto> csvWriter = new CSVWriterBuilder<CellTowerDto>(out).strategy(CSVStrategy.UK_DEFAULT).entryConverter(new CellTowerEntryConverter()).build();
			csvWriter.writeAll(cellTowers);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done writing Cell Towers to file "+fileName);
	}
	
//	public static void writeMeasurementsToCSVFile(String fileName, boolean append, List<CellTowerDto> measurements) {
//		System.out.println("Writing Measurements to file "+fileName);
//
//		Writer out;
//		try {
//			out = new FileWriter(fileName, append);
//			CSVWriter<MeasurementDto> csvWriter = new CSVWriterBuilder<MeasurementDto>(out).strategy(CSVStrategy.UK_DEFAULT).entryConverter(new MeasurementsEntryParser()).build();
//			csvWriter.writeAll(measurements);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("Done writing Measurements to file "+fileName);
//	}
}
