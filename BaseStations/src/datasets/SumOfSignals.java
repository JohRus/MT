package datasets;

import io.IOClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import algorithms.Algorithms;

public class SumOfSignals {

	public static void main(String[] args) throws ClassNotFoundException {
		String directory = "/Volumes/My Passport/measurements_260_raw/";
		String country = "poland_"+directory.split("_")[1];
		String dbName = "sum_of_signals";
		String storage = "/Users/Johan/Workspace/MT/tmp/";
		Class.forName("org.sqlite.JDBC");
		try(Connection connection = DriverManager.getConnection("jdbc:sqlite:"+storage+dbName+".db");
			PrintWriter pw = new PrintWriter(new FileWriter(new File(storage+dbName+"."+country+"-insertion.log")));) {
			Statement statement = connection.createStatement();
			statement.executeUpdate("DROP TABLE IF EXISTS "+country);
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+country+" (mcc INT, net INT, area INT, cell INT, measurements INT, invalidmeasurements INT, sumofsignals INT)");
			int entriesInserted = 0;
			for(int i = 1; i <= 21; i++) {
				long startTime = System.currentTimeMillis();
				String fileName = "measurements_260_"+i+".csv";
				File file = new File(directory+fileName);
//				Iterator<MeasurementDto> it = IOClient.csvMeasurementsParserIterator(new FileReader(file));
				Iterator<MeasurementDto> it = IOClient.readMeasurementsFromCSVFileAndReturnIterator(directory+fileName);
				// Map<CellID, Triplet<measurements, invalidmeasurements, sumofsignals>>
				Map<CellID, Triplet<Integer, Integer, Integer>> sumOfSignalsMap = new HashMap<CellID, Triplet<Integer, Integer, Integer>>();
				pw.write("Reading from: "+fileName+"..\n");
				int measurementCounter = 0;
				int cellCounter = 0;
				while(it.hasNext()) {
					MeasurementDto measurement = it.next();
					measurementCounter++;
					int currSignal = measurement.getSignal();
//					if(currSignal == 99) {
//						continue;
//					}
					CellID cellID = new CellID(measurement.getMcc(), measurement.getNet(), measurement.getArea(), measurement.getCell());
					if(sumOfSignalsMap.containsKey(cellID)) {
						Triplet<Integer, Integer, Integer> values = sumOfSignalsMap.get(cellID);
						int measurements = values.getValue0();
						int invalidMeasurements = values.getValue1();
						int sumOfSignals = values.getValue2();
						if(currSignal == 99) {
							invalidMeasurements++;
						}
						else {
							measurements++;
							if(currSignal >= 0) {
								sumOfSignals += Algorithms.inDbm(currSignal);
							}
							else {
								sumOfSignals += currSignal;
							}
						}
						Triplet<Integer, Integer, Integer> newValues = Triplet.with(measurements, invalidMeasurements, sumOfSignals);
						sumOfSignalsMap.put(cellID, newValues);
					}
					else {
						cellCounter++;
						int measurements = 0;
						int invalidMeasurements = 0;
						int sumOfSignals = 0;
						if(currSignal == 99) {
							invalidMeasurements++;
						}
						else {
							measurements++;
							if(currSignal >= 0) {
								sumOfSignals += Algorithms.inDbm(currSignal);
							}
							else {
								sumOfSignals += currSignal;
							}
						}
						Triplet<Integer, Integer, Integer> newValues = Triplet.with(measurements, invalidMeasurements, sumOfSignals);
						sumOfSignalsMap.put(cellID, newValues);
					}
				}
				IOClient.closeIteratorReader();
				pw.write("Unique measurements read: "+measurementCounter+"\n");
				pw.write("Unique cells read: "+cellCounter+"\n");
				pw.write("Size of CellID map:"+sumOfSignalsMap.size()+"\n");
				pw.write("Writing to database..\n");
//				String prepStmt = "UPDATE "+country+" SET sumofsignals=? WHERE mcc=? AND net=? AND area=? AND cell=?";
				String prepStmt = "INSERT OR REPLACE INTO "+country+" (mcc, net, area, cell, measurements, invalidmeasurements, sumofsignals) VALUES (?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement preparedStatement = connection.prepareStatement(prepStmt);
				int entryCounter = 0;
				
//				ArrayList<Integer> returnCount = new ArrayList<Integer>();
				for(Entry<CellID, Triplet<Integer, Integer, Integer>> entry : sumOfSignalsMap.entrySet()) {
					preparedStatement.setInt(1, entry.getKey().getMcc());
					preparedStatement.setInt(2, entry.getKey().getNet());
					preparedStatement.setInt(3, entry.getKey().getArea());
					preparedStatement.setLong(4, entry.getKey().getCell());
					preparedStatement.setInt(5, entry.getValue().getValue0());
					preparedStatement.setInt(6, entry.getValue().getValue1());
					preparedStatement.setInt(7, entry.getValue().getValue2());

					preparedStatement.addBatch();
					entryCounter++;
					
					if(entryCounter % 1000 == 0) {
						connection.setAutoCommit(false);
						int[] updateCounts = preparedStatement.executeBatch();
						connection.setAutoCommit(true);
						for(int j = 0; j < updateCounts.length; j++) {
							if(updateCounts[j] != 1) {
								System.out.println("ERROR ON INSERT");
								System.out.println("File: "+fileName);
								System.out.println(entry.toString());
							}
						}
					}
				}
				connection.setAutoCommit(false);
				int[] updateCounts = preparedStatement.executeBatch();
				connection.setAutoCommit(true);
				for(int j = 0; j < updateCounts.length; j++) {
					if(updateCounts[j] != 1) {
						System.out.println("ERROR ON INSERT");
						System.out.println("File: "+fileName);
//						System.out.println(entry.toString());
					}
				}
				pw.write("Entries/cells inserted into database: "+entryCounter+"\n");
				entriesInserted += entryCounter;
				long timeElapsed = (System.currentTimeMillis()-startTime)/1000;
				pw.write("Time elapsed for processing the current file: "+timeElapsed+" seconds\n\n");
				pw.flush();
			}
			pw.write("Total number of entries/cells inserted: "+entriesInserted+"\n\n");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("END OF PROGRAM");
		return;
	}

}
