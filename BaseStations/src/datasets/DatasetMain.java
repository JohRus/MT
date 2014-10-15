package datasets;

import io.IOClient;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DatasetMain {

	public static void main(String[] args) {
		File fileOne = new File(args[0]);
		File fileTwo = new File(args[1]);
		LinkedList<MeasurementDto> listOne = new LinkedList<MeasurementDto>(IOClient.readMeasurementsFromCSVFile(fileOne));
		LinkedList<MeasurementDto> listTwo = new LinkedList<MeasurementDto>(IOClient.readMeasurementsFromCSVFile(fileTwo));
		System.out.println("Size file one: "+listOne.size());
		System.out.println("Size file two: "+listTwo.size());
		List<MeasurementDto> merged = DatasetHelper.merge(listOne, listTwo);
		System.out.println("Size file merged: "+merged.size());
		int diff = merged.size()-listOne.size()-listTwo.size();
		System.out.println("Difference: "+diff);
		IOClient.writeMeasurementsToCSVFile(merged, new File(
				generateMergedFileName(fileOne.getName(), fileTwo.getName())));
		System.out.println("Done merging and writing");
		return;
	}
	
	
	public static String generateMergedFileName(String fileNameOne, String fileNameTwo) {
		String[] splitOne = fileNameOne.split("_");
		int lastIndexOne = splitOne.length-1;
		if(splitOne[splitOne.length-1].contains(".csv") && splitOne[splitOne.length-1].length() == 5) {
			splitOne[splitOne.length-1] = splitOne[splitOne.length-1].substring(0, 1);
			lastIndexOne = splitOne.length-1;
		}
		else if(splitOne[splitOne.length-1].equals(".csv")) {
			lastIndexOne = splitOne.length-2;
		}
		String[] splitTwo = fileNameTwo.split("_");
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
		String name = fileNameOne.substring(0, 16);
		for(int i = 0; i < ints.size(); i++) {
			name += "_"+ints.get(i);
		}
		name += "_.csv";
		
		return name;
	}
}
