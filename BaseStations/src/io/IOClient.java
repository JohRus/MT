package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.googlecode.jcsv.writer.CSVWriter;
import com.googlecode.jcsv.writer.internal.CSVWriterBuilder;

import datasets.CellTowerDto;
import datasets.MeasurementDto;

public class IOClient {
	
	public static void writeCellTowersToCSVFile(List<CellTowerDto> cellTowers, File file) {
//		Writer out;
		try (Writer out = new FileWriter(file);){
//			out = new FileWriter(fileName, append);
			CSVWriter<CellTowerDto> csvWriter = new CSVWriterBuilder<CellTowerDto>(out)
					.strategy(CSVStrategy.UK_DEFAULT)
					.entryConverter(new CellTowerEntryConverter())
					.build();
			csvWriter.writeAll(cellTowers);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public static void writeMeasurementsToCSVFile(List<MeasurementDto> measurements, File file) {
//		Writer out;
		try (Writer out = new FileWriter(file);){
//			out = new FileWriter(fileName, append);
			CSVWriter<MeasurementDto> csvWriter = new CSVWriterBuilder<MeasurementDto>(out)
					.strategy(CSVStrategy.UK_DEFAULT)
					.entryConverter(new MeasurementEntryConverter())
					.build();
			csvWriter.writeAll(measurements);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	
	/**
	 * 
	 * @param file Complete path that ends with "filename".csv
	 * @return
	 */
	public static List<MeasurementDto> readMeasurementsFromCSVFile(File file) {
		List<MeasurementDto> measurements = null;
		try (Reader fileReader = new FileReader(file);){
			CSVReader<MeasurementDto> csvMeasurementsParser = new CSVReaderBuilder<MeasurementDto>(fileReader)
					.strategy(CSVStrategy.UK_DEFAULT)
					.entryParser(new MeasurementEntryParser())
					.build();
			measurements = csvMeasurementsParser.readAll();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return measurements;
	}
}
