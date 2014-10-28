package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.googlecode.jcsv.writer.CSVWriter;
import com.googlecode.jcsv.writer.internal.CSVWriterBuilder;

import datasets.CellTowerDto;
import datasets.MeasurementDto;

public class IOClient {
	
	/**
	 * Write the complete content of a list of Cell Towers to a csv file
	 * @param cellTowers The list that are to be written to a csv file
	 * @param file The file to be written to
	 */
	public static void writeCellTowersToCSVFile(List<CellTowerDto> cellTowers, String file) {
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
	
	/**
	 * Write the complete content of a list of Measurements to a csv file
	 * @param measurements The list that are to be written to a csv file
	 * @param file The file to be written to
	 */
	public static void writeMeasurementsToCSVFile(List<MeasurementDto> measurements, String file, boolean append) {
//		Writer out;
		try (Writer out = new FileWriter(file, append);){
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
	 * Read the complete content from a Measurements file 
	 * @param file The file to be read from
	 * @return A list with the complete content of the csv file
	 */
	public static List<MeasurementDto> readMeasurementsFromCSVFile(String file) {
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
	
//	/**
//	 * Read the complete content from a Measurements file
//	 * @param fileReader
//	 * @return An iterator for the measurements from the csv file
//	 */
//	public static Iterator<MeasurementDto> csvMeasurementsParserIterator(Reader fileReader) {
////		List<MeasurementDto> measurements = null;
//		Iterator<MeasurementDto> it = null;
////			fileReader = new FileReader(file);
//					CSVReader<MeasurementDto> csvMeasurementsParser = new CSVReaderBuilder<MeasurementDto>(fileReader)
//							.strategy(CSVStrategy.UK_DEFAULT)
//							.entryParser(new MeasurementEntryParser())
//							.build();
//		//			measurements = csvMeasurementsParser.readAll();
//					it = csvMeasurementsParser.iterator();
////		return measurements;
//		return it;
//	}
	
	private static FileReader fileReader;
	
	/**
	 * Read the complete content from a Measurements file
	 * @param fileReader
	 * @return An iterator over the measurements from the csv file
	 */
	public static Iterator<MeasurementDto> readMeasurementsFromCSVFileAndReturnIterator(String fileName) {
		Iterator<MeasurementDto> it = null;
		try {
			fileReader = new FileReader(fileName);
			CSVReader<MeasurementDto> csvMeasurementsParser = new CSVReaderBuilder<MeasurementDto>(fileReader)
					.strategy(CSVStrategy.UK_DEFAULT)
					.entryParser(new MeasurementEntryParser())
					.build();
			it = csvMeasurementsParser.iterator();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return it;
	}
	
	public static void closeIteratorReader() {
		try {
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
