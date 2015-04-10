package opencellid;

import infrastructure.DefaultCell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class CellTowersReader {

	public static void main(String[] args) {
		String fileName = "/Users/Johan/Documents/CellTowers/cell_towers_GSM_exact_polish.csv";
		try(BufferedReader br = new BufferedReader(new FileReader(fileName));){

			//			List<OpenCellIdCell> cells = new ArrayList<OpenCellIdCell>();

			String line = "";
			int totalCount = 0;
			int count = 0;

			long startTime = System.currentTimeMillis();
			System.out.println("Starting to read from file..");
			while((line = br.readLine()) != null) {
				totalCount++;
				if(totalCount%30000==0)
					System.out.println("Read "+totalCount+" lines..");

				String[] fields = line.split(",");

				int samples = Integer.parseInt(fields[9]);
				if(samples > 94 && samples < 101) {

					Request request = new Request(fields[1], fields[2], fields[3], fields[4]);
					DefaultCell defaultCell = request.getData();
					//					if(defaultCell.getMeasurements().size() != samples) {
					count++;
					//					}
					//							System.out.println("MCC:"+fields[1]+", MNC:"+fields[2]+", LAC:"+fields[3]+", ID:"+fields[4]);

				}
			}
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
