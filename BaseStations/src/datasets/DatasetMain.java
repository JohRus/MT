package datasets;

import io.IOClient;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@Deprecated
public class DatasetMain {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String fileOne = scanner.nextLine();
		String fileTwo = scanner.nextLine();
//		File fileOne = new File(scanner.nextLine());
//		File fileTwo = new File(scanner.nextLine());
		LinkedList<MeasurementDto> listOne = new LinkedList<MeasurementDto>(IOClient.readMeasurementsFromCSVFile(fileOne));
		LinkedList<MeasurementDto> listTwo = new LinkedList<MeasurementDto>(IOClient.readMeasurementsFromCSVFile(fileTwo));
		int sizeOne = listOne.size();
		int sizeTwo = listTwo.size();
		System.out.println("Size file one: "+listOne.size());
		System.out.println("Size file two: "+listTwo.size());
		List<MeasurementDto> merged = DatasetHelper.merge(listOne, listTwo);
		System.out.println("Size file merged: "+merged.size());
		System.out.println("Size file one: "+listOne.size());
		for(MeasurementDto m : listOne) {
			System.out.println(m);
		}
		System.out.println("Size file two: "+listTwo.size());
		for(MeasurementDto m : listTwo) {
			System.out.println(m);
		}
		int diff = merged.size()-sizeOne-sizeTwo;
		System.out.println("Difference: "+diff);
		String name = generateMergedFileName(fileOne, fileTwo);
		name = "temp/level_1/"+name;
		IOClient.writeMeasurementsToCSVFile(merged, name, false);
		System.out.println("Done merging and writing");
		scanner.close();
		return;
	}
	
	
	public static String generateMergedFileName(String fileNameOne, String fileNameTwo) {
		String[] splitOne = fileNameOne.split("_");
		int lastIndexOne = splitOne.length-1;
		if(splitOne[splitOne.length-1].equals(".csv")) {
			lastIndexOne = splitOne.length-2;
		}
		else if(splitOne[splitOne.length-1].contains(".csv")) {
			if(splitOne[splitOne.length-1].length() == 5) {
				splitOne[splitOne.length-1] = splitOne[splitOne.length-1].substring(0, 1);
				lastIndexOne = splitOne.length-1;
			}
			else if(splitOne[splitOne.length-1].length() == 6) {
				splitOne[splitOne.length-1] = splitOne[splitOne.length-1].substring(0, 2);
				lastIndexOne = splitOne.length-1;
			}
		}
		
		String[] splitTwo = fileNameTwo.split("_");
		int lastIndexTwo = splitTwo.length-1;
		if(splitTwo[splitTwo.length-1].equals(".csv")) {
			lastIndexTwo = splitTwo.length-2;
		}
		else if(splitTwo[splitTwo.length-1].contains(".csv")) {
			if(splitTwo[splitTwo.length-1].length() == 5) {
				splitTwo[splitTwo.length-1] = splitTwo[splitTwo.length-1].substring(0, 1);
				lastIndexTwo = splitTwo.length-1;
			}
			else if(splitTwo[splitTwo.length-1].length() == 6) {
				splitTwo[splitTwo.length-1] = splitTwo[splitTwo.length-1].substring(0, 2);
				lastIndexTwo = splitTwo.length-1;
			}
		}
		
		ArrayList<Integer> ints = new ArrayList<Integer>();
		for(int i = 2; i <= lastIndexOne; i++) {
			ints.add(Integer.parseInt(splitOne[i]));
		}
		for(int i = 2; i <= lastIndexTwo; i++) {
			ints.add(Integer.parseInt(splitTwo[i]));
		}
		Collections.sort(ints);
		String name = fileNameOne.substring(0, 16);
		for(int i = 0; i < ints.size(); i++) {
			name += "_"+ints.get(i);
		}
		name += "_.csv";
		
		return name;
	}
}
