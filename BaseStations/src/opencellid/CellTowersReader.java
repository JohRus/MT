package opencellid;

import infrastructure.DefaultCell;
import infrastructure.Measurement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class CellTowersReader {

	public static void main(String[] args) {
//		String fileName = "/Users/Johan/Documents/CellTowers/cells_exact_samples67-80.json";
//		try(JsonParser jp = new MappingJsonFactory().createJsonParser(new File(fileName))){
//
//			int cellCounter = 0;
//			long allMeasurements = 0;
//			int leastMeasurements = 1001;
//			int measBelow81 = 0;
//			int measCorrect = 0;
//			int measAbove120 = 0;
//			int measAboveXXX = 0;
//
//			int signalStrengthIsZeroCounter = 0;
//			int signalStrengthAboveZeroCounter = 0;
//			int invalidSignalStrengthDBMValues = 0;
//			int signalStrengthNotKnown = 0;
//			int invalidSignalStrengthASUValues = 0;
//
//			int cellsWithOnlyBadMeasurements = 0;
//
//			HashSet<String> set = new HashSet<String>();
//
//			JsonNode cellsNode = jp.readValueAsTree().get("cells");
//			if(cellsNode.isArray()) {
//				for(JsonNode cellNode : cellsNode) {
//					cellCounter++;
//					String mcc = cellNode.get("mcc").asText();
//					String net = cellNode.get("net").asText();
//					String area = cellNode.get("area").asText();
//					String cell = cellNode.get("cell").asText();
//					String cellString = mcc+"-"+net+"-"+area+"-"+cell;
//					JsonNode measurementsNode = cellNode.get("measurements");
//					if(measurementsNode.isArray()) {
//						int measCounter = 0;
//						int badMeasCounter = 0;
//						for(JsonNode measurementNode : measurementsNode) {
//							measCounter++;
//							int signalStrength = measurementNode.get("signalStrength").asInt();
//
//							if(signalStrength == 0) {
//								signalStrengthIsZeroCounter++;
//								//								set.add(cellString);
//								badMeasCounter++;
//							}
//							else if(signalStrength > 0 && signalStrength <= 31)
//								signalStrengthAboveZeroCounter++;
//							else if(signalStrength >= -50 && signalStrength <= -1)
//								invalidSignalStrengthDBMValues++;
//							else if(signalStrength == 99) {
//								signalStrengthNotKnown++;
//								set.add(cellString);
//								badMeasCounter++;
//							}
//							else if(signalStrength >= 32 && signalStrength != 99) {
//								invalidSignalStrengthASUValues++;
//								set.add(cellString);
//								badMeasCounter++;
//							}
//						}
//						if(measCounter < 81)
//							measBelow81++;
//						else if(measCounter > 120)
//							measAbove120++;
//						else
//							measCorrect++;
//
//						if(measCounter == 1000)
//							measAboveXXX++;
//
//						if(measCounter == badMeasCounter)
//							cellsWithOnlyBadMeasurements++;
//
//						if(measCounter < leastMeasurements)
//							leastMeasurements = measCounter;
//
//						allMeasurements += measCounter;
//					}
//					else
//						System.out.println("measurementsNode was not array");
//				}
//			}
//			else 
//				System.out.println("cellsNode was not array");
//
//			System.out.println("Cells: "+cellCounter);
//			System.out.println("Total number of measurements: "+allMeasurements);
//			System.out.println("Least number of measurements for a cell: "+leastMeasurements);
//			System.out.println("Cells with less than 81 measurements: "+measBelow81);
//			System.out.println("Cells with more than 120 measurements: "+measAbove120);
//			System.out.println("Cells with the correct amount of measurements: "+measCorrect);
//			System.out.println("Cells with more than XXX measurements: "+measAboveXXX);
//			System.out.println();
//			System.out.println("Measurements where signal strength is 0: "+signalStrengthIsZeroCounter);
//			System.out.println("Measurements where signal strength is above 0 and less than 32: "+signalStrengthAboveZeroCounter);
//			System.out.println("Measurements where signal strength is between -1 and -50: "+invalidSignalStrengthDBMValues);
//			System.out.println("Measurements where signal strength is 99: "+signalStrengthNotKnown);
//			System.out.println("Measurements where signal strength is above 31 but not 99: "+invalidSignalStrengthASUValues);
//			System.out.println();
//			System.out.println("Cells with one or more bad measurement: "+set.size());
//			System.out.println("Cells where all measurements are bad: "+cellsWithOnlyBadMeasurements);
//
//
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
	private static void cleanCells() {
		String fullFileName = "/Users/Johan/Documents/CellTowers/cells_exact_samples67-120_2036.json";
		String newFileName = "/Users/Johan/Documents/CellTowers/cells_exact_samples67-120_cleaned.json";
		
		JSONFile jsonFile = new JSONFile(newFileName);
		jsonFile.iWannaStartAnArray("cells");

		try(JsonParser jp = new MappingJsonFactory().createJsonParser(new File(fullFileName))){

			JsonNode cellsNode = jp.readValueAsTree().get("cells");

			if(cellsNode.isArray()) {
				for(JsonNode cellNode : cellsNode) {

					OpenCellIdCell cell = Controller.createCell(cellNode);

					JsonNode measurementsNode = cellNode.get("measurements");
					if(measurementsNode.isArray()) {

						List<Measurement> measurements = new ArrayList<Measurement>(cell.getSamples());

						for(JsonNode measurementNode : measurementsNode) {
							Measurement measurement = Controller.createMeasurement(measurementNode);
							measurements.add(measurement);
						}
						cell.setMeasurements(measurements);

						jsonFile.writeThisOpenCellIdCell(cell);
					}
					else
						System.out.println("measurementsNode was not array");
				}
			}
			else 
				System.out.println("cellsNode was not array");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jsonFile.theArrayIsDone();
		jsonFile.iAmDoneWriting();
		
		System.out.println("Done");
		
		
	}

	private static void mergeTwoJSONFiles() {
		String fullFileName1 = "/Users/Johan/Documents/CellTowers/cells_exact_samples81-120.json";
		String fullFileName2 = "/Users/Johan/Documents/CellTowers/cells_exact_samples67-80.json";
		String newFileName = "/Users/Johan/Documents/CellTowers/cells_exact_samples67-120.json";

		JSONFile jsonFile = new JSONFile(newFileName);
		jsonFile.iWannaStartAnArray("cells");

		try(JsonParser jp = new MappingJsonFactory().createJsonParser(new File(fullFileName2))){

			JsonNode cellsNode = jp.readValueAsTree().get("cells");

			if(cellsNode.isArray()) {
				for(JsonNode cellNode : cellsNode) {

					OpenCellIdCell cell = Controller.createCell(cellNode);

					JsonNode measurementsNode = cellNode.get("measurements");
					if(measurementsNode.isArray()) {

						List<Measurement> measurements = new ArrayList<Measurement>(cell.getSamples());

						for(JsonNode measurementNode : measurementsNode) {
							Measurement measurement = Controller.createMeasurement(measurementNode);
							measurements.add(measurement);
						}
						cell.setMeasurements(measurements);

						jsonFile.writeThisOpenCellIdCell(cell);
					}
					else
						System.out.println("measurementsNode was not array");
				}
			}
			else 
				System.out.println("cellsNode was not array");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Done with first");
		
		try(JsonParser jp = new MappingJsonFactory().createJsonParser(new File(fullFileName1))){

			JsonNode cellsNode = jp.readValueAsTree().get("cells");

			if(cellsNode.isArray()) {
				for(JsonNode cellNode : cellsNode) {

					OpenCellIdCell cell = Controller.createCell(cellNode);

					JsonNode measurementsNode = cellNode.get("measurements");
					if(measurementsNode.isArray()) {

						List<Measurement> measurements = new ArrayList<Measurement>(cell.getSamples());

						for(JsonNode measurementNode : measurementsNode) {
							Measurement measurement = Controller.createMeasurement(measurementNode);
							measurements.add(measurement);
						}
						cell.setMeasurements(measurements);

						jsonFile.writeThisOpenCellIdCell(cell);
					}
					else
						System.out.println("measurementsNode was not array");
				}
			}
			else 
				System.out.println("cellsNode was not array");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		jsonFile.theArrayIsDone();
		jsonFile.iAmDoneWriting();
		
		System.out.println("Done");
	}

	private static void requestAndParseAllMeasurementsBelongingToExactGSMCells() {
		String fileName = "/Users/Johan/Documents/CellTowers/cell_towers_GSM_exact.csv";
		try(BufferedReader br = new BufferedReader(new FileReader(fileName));){

			String line = "";
			int totalCount = 0;
			int count = 0;

			JSONFile jsonFile = new JSONFile(JSONFile.filePathCellTowers+"cells_exact_samples67-80"+JSONFile.fileFormat);
			jsonFile.iWannaStartAnArray("cells");

			long startTime = System.currentTimeMillis();
			System.out.println("Starting to read from file..");
			while((line = br.readLine()) != null) {
				totalCount++;
				if(totalCount%30000==0)
					System.out.println("Read "+totalCount+" lines..");

				String[] fields = line.split(",");

				int samples = Integer.parseInt(fields[9]);
				if(samples >= 67 && samples <=80) {

					Request request = new Request(fields[1], fields[2], fields[3], fields[4]);
					OpenCellIdCell openCellIdCell = request.getData();

					jsonFile.writeThisOpenCellIdCell(openCellIdCell);

					count++;
					if(count%100==0)
						System.out.println("Parsed "+count+" cells..");
				}
			}
			jsonFile.theArrayIsDone();
			jsonFile.iAmDoneWriting();
			System.out.println("Done reading from file..\n");
			double timeElapsed = (System.currentTimeMillis()-startTime)/(double)1000;
			System.out.println("Read "+totalCount+" lines..\n");

			System.out.println("Count is "+count);

			System.out.println("Time elapsed: "+timeElapsed+" seconds");




		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	//	private static void findExactPolishCells() {
	//		try(BufferedReader br = new BufferedReader(new FileReader("/Users/Johan/Documents/CellTowers/cell_towers_GSM.csv"));
	//				BufferedWriter bw_GSM_exact_polish = new BufferedWriter(new FileWriter("/Users/Johan/Documents/CellTowers/cell_towers_GSM_exact_polish.csv", true));) {
	//
	//			String line = "";
	//			int totalCount = 0;
	//			int count = 0;
	//
	//			System.out.println("Starting to read from file..");
	//			while((line = br.readLine()) != null) {
	//				totalCount++;
	//				if(totalCount%20000==0)
	//					System.out.println("Read "+totalCount+" lines..");
	//
	//				String[] fields = line.split(",");
	//				if(Integer.parseInt(fields[1]) == 260 && Integer.parseInt(fields[10]) == 0) {
	//					bw_GSM_exact_polish.write(line+"\n");
	//					count++;
	//				}
	//			}
	//
	//			System.out.println("Done reading from file..\n");
	//			System.out.println("Read "+totalCount+" in total.");
	//			System.out.println("Read "+count+" exact polish cells.");
	//
	//
	//		} catch (FileNotFoundException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//	}

	private static void findExactGSMCells() {
		FileReader fr = null;
		BufferedReader br = null;
		try (BufferedWriter bw_cellTowers_GSM_exact = new BufferedWriter(new FileWriter("/Users/Johan/Documents/CellTowers/cell_towers_GSM_exact.csv", true));) {
			String fileName = "/Users/Johan/Documents/CellTowers/cell_towers_GSM.csv";
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);

			HashSet<Integer> countries = new HashSet<Integer>(); 

			String line = "";
			int totalCount = 0;
			int writeCounter = 0;

			System.out.println("Starting to read from file..");
			while((line = br.readLine()) != null) {
				totalCount++;
				if(totalCount%500000==0)
					System.out.println("Read "+totalCount+" lines..");

				String[] fields = line.split(",");

				if(Integer.parseInt(fields[10]) == 0) {
					countries.add(Integer.parseInt(fields[1]));
					bw_cellTowers_GSM_exact.write(line+"\n");
					writeCounter++;
				}
			}
			System.out.println("Done reading from file..\n");
			System.out.println("Read "+totalCount+" lines in total..");
			System.out.println("Wrote "+writeCounter+" lines to cell_towers_GSM_exact.csv..\n");
			System.out.println("Countries with exact cells:");
			int[] countriesArray = new int[countries.size()];
			int i = 0;
			for(Integer mcc : countries) {
				System.out.println(mcc);
				countriesArray[i++] = mcc;
			}
			System.out.println();

			fr = new FileReader(fileName);
			br = new BufferedReader(fr);

			int[] exactCellsCounters = new int[countriesArray.length];

			line = "";
			totalCount = 0;

			System.out.println("Starting to read from file..");
			while((line = br.readLine()) != null) {
				totalCount++;
				if(totalCount%500000==0)
					System.out.println("Read "+totalCount+" lines..");

				String[] fields = line.split(",");

				if(Integer.parseInt(fields[10]) == 0) {
					for(i = 0; i < countriesArray.length; i++) {
						if(Integer.parseInt(fields[1]) == countriesArray[i]) {
							exactCellsCounters[i] = exactCellsCounters[i]+1;
							break;
						}
					}
				}
			}
			System.out.println("Done reading from file..\n");
			System.out.println("Read "+totalCount+" lines in total..\n");

			for(i = 0; i < countriesArray.length; i++) {
				System.out.println("Exact cells in "+countriesArray[i]+": "+exactCellsCounters[i]);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void sortCellsByRadio() {
		String fileName = "/Users/Johan/Downloads/cell_towers.csv";
		try(BufferedReader br = new BufferedReader(new FileReader(fileName));
				BufferedWriter bw_UMTS = new BufferedWriter(new FileWriter("/Users/Johan/Documents/CellTowers/cell_towers_UMTS.csv", true));
				BufferedWriter bw_GSM = new BufferedWriter(new FileWriter("/Users/Johan/Documents/CellTowers/cell_towers_GSM.csv", true));
				BufferedWriter bw_LTE = new BufferedWriter(new FileWriter("/Users/Johan/Documents/CellTowers/cell_towers_LTE.csv", true));
				BufferedWriter bw_CDMA = new BufferedWriter(new FileWriter("/Users/Johan/Documents/CellTowers/cell_towers_CDMA.csv", true));
				) {

			String line = "";
			int totalCount = 0;
			int lines_UMTS = 0;
			int lines_GSM = 0;
			int lines_LTE = 0;
			int lines_CDMA = 0;

			System.out.println("Starting to read from file..");
			while((line = br.readLine()) != null) {
				totalCount++;
				if(totalCount%500000==0)
					System.out.println("Read "+totalCount+" lines..");

				String[] fields = line.split(",");

				if(fields[0].equals("UMTS")) {
					bw_UMTS.write(line+"\n");
					lines_UMTS++;
				}
				else if(fields[0].equals("GSM")) {
					bw_GSM.write(line+"\n");
					lines_GSM++;
				}
				else if(fields[0].equals("LTE")) {
					bw_LTE.write(line+"\n");
					lines_LTE++;
				}
				else if(fields[0].equals("CDMA")) {
					bw_CDMA.write(line+"\n");
					lines_CDMA++;
				}
				else {
					System.out.println("Do not fit: "+line);
				}
			}
			System.out.println("Done reading from file..\n");
			System.out.println("Read "+totalCount+" lines in total..\n");

			System.out.println("Wrote "+lines_UMTS+" to cell_towers_UMTS.csv");
			System.out.println("Wrote "+lines_GSM+" to cell_towers_GSM.csv");
			System.out.println("Wrote "+lines_LTE+" to cell_towers_LTE.csv");
			System.out.println("Wrote "+lines_CDMA+" to cell_towers_CDMA.csv");
			int sum = lines_UMTS+lines_GSM+lines_LTE+lines_CDMA;
			System.out.println("Wrote "+sum+" lines in total..");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
