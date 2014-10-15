package datahandler;

import io.IOClient;
import io.MeasurementEntryConverter;
import io.MeasurementEntryParser;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.io.FileUtils;
import org.jboss.netty.handler.codec.http.multipart.FileUpload;

import com.google.common.base.Splitter;
import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.googlecode.jcsv.writer.CSVWriter;
import com.googlecode.jcsv.writer.internal.CSVWriterBuilder;

import datasets.MeasurementDto;

public class SortedMeasurementsFilesMerger {
	
	private List<MeasurementDto> listOne;
	private List<MeasurementDto> listTwo;
	
	@Deprecated
	public SortedMeasurementsFilesMerger(List<MeasurementDto> listOne, List<MeasurementDto> listTwo) {
		this.listOne = listOne;
		this.listTwo = listTwo;
	}

	public List<MeasurementDto> run() {
		LinkedList<MeasurementDto> linkedListOne = new LinkedList<MeasurementDto>(this.listOne);
		LinkedList<MeasurementDto> linkedListTwo = new LinkedList<MeasurementDto>(this.listTwo);
		return merge(linkedListOne, linkedListTwo);
	}
	
	private String generateMergedFileName() {
		String[] splitOne = listOne.getName().split("_");
		int lastIndexOne = splitOne.length-1;
		if(splitOne[splitOne.length-1].contains(".csv") && splitOne[splitOne.length-1].length() == 5) {
			splitOne[splitOne.length-1] = splitOne[splitOne.length-1].substring(0, 1);
			lastIndexOne = splitOne.length-1;
		}
		else if(splitOne[splitOne.length-1].equals(".csv")) {
			lastIndexOne = splitOne.length-2;
		}
		String[] splitTwo = listTwo.getName().split("_");
		int lastIndexTwo = splitTwo.length-1;
		if(splitTwo[splitTwo.length-1].contains(".csv") && splitTwo[splitTwo.length-1].length() == 5) {
			splitTwo[splitTwo.length-1] = splitTwo[splitTwo.length-1].substring(0, 1);
			lastIndexTwo = splitTwo.length-1;
		}
		else if(splitTwo[splitTwo.length-1].equals(".csv")) {
			lastIndexTwo = splitTwo.length-2;
		}
		ArrayList<Integer> ints = new ArrayList<Integer>();
		for(int i = 2; i <= lastIndexOne; i++) {
			ints.add(Integer.parseInt(splitOne[i]));
		}
		for(int i = 2; i <= lastIndexTwo; i++) {
			ints.add(Integer.parseInt(splitTwo[i]));
		}
		Collections.sort(ints);
		String name = listOne.getName().substring(0, 16);
		for(int i = 0; i < ints.size(); i++) {
			name += "_"+ints.get(i);
		}
		name += "_.csv";
		
		return name;
	}

//	private void mergeSortedFiles() {
//		Queue<String> queue = new ConcurrentLinkedQueue<String>(this.sortedFilesToBeMerged);
//		int mergingNumber = 0;
////		File tempFile = null;
//		while(queue.size() > 1) {
//			long startTime = System.currentTimeMillis();
//			String fileOne = queue.remove();
//			String fileTwo = queue.remove();
//			List<MeasurementDto> listOne = IOClient.readMeasurementsFromCSVFile(fileOne);
//			List<MeasurementDto> listTwo = IOClient.readMeasurementsFromCSVFile(fileTwo);
//			List<MeasurementDto> sorted = merge(listOne, listTwo);
//			String tempFileName = this.tempDirectory.getPath()+"/merging_"+mergingNumber+".csv";
//			mergingNumber++;
////			tempFile = new File(this.tempDirectoryName+"/"+tempFileName);
//			IOClient.writeMeasurementsToCSVFile(sorted, tempFileName);
//			queue.add(tempFileName);
//			long time = (System.currentTimeMillis() - startTime)/1000;
//			logIt(fileOne, fileTwo, tempFileName, listOne.size(), listTwo.size(), sorted.size(), time, queue.size());
//		}
////		try {
////			FileUtils.copyFile(tempFile, new File("/Users/Johan/Workspace/"+tempFile.getName()));
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
//		return;
//	}
	
//	private void logIt(String fileOne, String fileTwo, String fileMerge, int elementsOne, int elementsTwo, int elementsMerge, long time, int queueSize) {
//		try (PrintWriter pw = new PrintWriter(new FileWriter(this.logFileName, true))) {
//			pw.println("There are currently "+queueSize+" files in the queue..");
//			pw.println();
//			pw.println("Merged "+fileOne+" and "+fileTwo+" into "+fileMerge);
//			pw.printf("\tSize "+fileOne+": "+elementsOne+"\n");
//			pw.printf("\tSize "+fileTwo+": "+elementsTwo+"\n");
//			pw.printf("\tSize "+fileMerge+": "+elementsMerge+"\n");
//			if(elementsOne+elementsTwo==elementsMerge) {
//				pw.printf("\t"+elementsOne+" + "+elementsTwo+" = "+elementsMerge+" :-)\n");
//			}
//			else {
//				pw.printf("\t"+elementsOne+" + "+elementsTwo+" != "+elementsMerge+" :-(\n");
//			}
//			pw.printf("\tTime elapsed for this merge: "+time+" seconds\n\n");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return;
//	}
	

	private List<MeasurementDto> merge(LinkedList<MeasurementDto> fileOne, LinkedList<MeasurementDto> fileTwo) {
		List<MeasurementDto> merged = new ArrayList<MeasurementDto>();
		while(!fileOne.isEmpty() || !fileTwo.isEmpty()) {
			if(fileOne.isEmpty()) {
				merged.addAll(fileTwo);
				break;
			}
			else if(fileTwo.isEmpty()) {
				merged.addAll(fileOne);
				break;
			}
			if(fileOne.peek().compareTo(fileTwo.peek()) == -1 || fileOne.peek().compareTo(fileTwo.peek()) == 0) {
				merged.add(fileOne.poll());
			}
			else {
				merged.add(fileTwo.poll());
			}
		}
		return merged;
	}

//	private void deleteTempDirectory() {
//		File[] files = this.tempDirectory.listFiles();
//		for(int i = 0; i < files.length; i++) {
//			files[i].delete();
//		}
//		this.tempDirectory.delete();
//	}

//	private File createTempDirectory(String tempDirectoryName) {
//		File tempDirectory = new File(tempDirectoryName);
//		boolean createTempDir = tempDirectory.mkdir();
////		System.out.println("Temp dir was created: "+createTempDir);
//		return tempDirectory;
//	}
	
//	private List<File> createFilesFromNames(List<String> listOfNames) {
//		List<File> listOfFiles = new ArrayList<File>();
//		for(String s : listOfNames) {
//			listOfFiles.add(new File(s));
//		}
//		return listOfFiles;
//	}

//	public static void main(String[] args) {
//		String filePath = "/Volumes/My Passport/measurements_260_sorted/";
//		List<String> files = new ArrayList<String>();
//		for(int i = 1; i <= 21; i++) {
//			files.add(filePath+"measurements_260_"+i+".csv");
//		}
//		SortedMeasurementsFilesMerger sortMeasurementsFiles = new SortedMeasurementsFilesMerger(
//				files,
//				filePath+"measurements_260_sorted.csv", 
//				"/Users/Johan/Workspace/temp/sorting_all_files.log",
//				"/Users/Johan/Workspace/temp");
////				"/Volumes/My Passport/temp");
//		sortMeasurementsFiles.run();
//		return;
//	}

}
