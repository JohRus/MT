package application;

import io.CellTowerEntryConverter;
import io.MeasurementEntryParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.googlecode.jcsv.writer.CSVWriter;
import com.googlecode.jcsv.writer.internal.CSVWriterBuilder;

import algorithms.Algorithms;
import algorithms.SelectAlgorithm;
import datahandler.CassandraClient;
import datahandler.SelectDataset;
import datasets.CellID;
import datasets.CellTowerDto;
import datasets.MeasurementDto;

public class Controller {

	private Scanner scanner;

	private TaskBuilder task;

	public Controller() {
		scanner = new Scanner(System.in);
		task = new TaskBuilder();
	}

	public void startOver() {
		scanner = new Scanner(System.in);
		this.task = new TaskBuilder();
		this.start();
	}

	public void start() {
		System.out.println("Welcome to Johan's master thesis project.");
		selectTask();
		scanner.close();
		System.out.println("Goodbye!");
		//		CassandraClient cassandraClient = new CassandraClient();
		//		cassandraClient.connect();
		//		
		//		List<MeasurementDto> measurements = cassandraClient.queryForMeasurements(310, 4, 8, 667);
		//		
		//		System.out.printf("There are %d measurements for the cell.\n", measurements.size());
		//		for(MeasurementDto measurement : measurements) {
		//			System.out.println(measurement);
		//		}

		//		CellTowerDto cellTower = Algorithms.centroid(measurements);
		//		System.out.println(cellTower);

		//		cassandraClient.close();
		return;
	}

	private void selectTask() {
		System.out.println("To run an algorithm on a dataset, press 1.");
		System.out.println("To create a new dataset, press 2.");
		System.out.println("To abort, press something else.");
		int command = scanner.nextInt();
		if(command == 1) {
			selectDataSet();
			selectAlgorithm();
			executeTask();
			startOver();
		}
		else if(command == 2) {

		}
		else {
		}
	}

	private void executeTask() {
		if(task.getAlgorithm().equals(TaskBuilder.CENTROID)) Algorithms.centroid(task);
		else if(task.getAlgorithm().equals(TaskBuilder.WEIGHTED_CENTROID)) Algorithms.weightedCentroid(task);
		else if(task.getAlgorithm().equals(TaskBuilder.STRONGEST_RSS)) Algorithms.strongestRSS(task);
	}

	private void selectDataSet() {
		System.out.println("To select the dataset measurements_all, press 1");
		System.out.println("To select the dataset measurements_260, press 2");
		System.out.println("To abort, press something else");
		int command = scanner.nextInt();
		if(command == 1) {
			this.task.setDataSet(TaskBuilder.MEASUREMENTS_ALL);
		}
		else if(command == 2) {
			this.task.setDataSet(TaskBuilder.MEASUREMENTS_260);
		}
		else {

		}		
	}

	private void selectAlgorithm() {
		System.out.println("To select the algorithm Centroid, press 1");
		System.out.println("To select the algorithm Weighted Centroid, press 2");
		System.out.println("To select the algorithm Strongest RSS, press 3");
		System.out.println("To abort, press something else");
		int command = scanner.nextInt();
		if(command == 1) {
			//				Algorithms.centroid(dataMap);
		}
		else if(command == 2) {
			this.task.setAlgorithm(TaskBuilder.WEIGHTED_CENTROID);
		}
		else if(command == 3) {
			//				Algorithms.strongestRSS(dataMap);
		}
		else {

		}
	}

	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.start();
	}
}
