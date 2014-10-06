package datahandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@Deprecated
public class CSVFiltering {
	
	//<mcc> <number of files> file1 file2 ...
	public static void main(String[] args) {
		int numFiles = Integer.parseInt(args[1]);
		//File[] files = new File[numFiles];
		for(int i = 2; i <= 1+numFiles; i++) {
			readAndWrite(new File(args[i]), Integer.parseInt(args[0]));
		}
		return;
	}
	
	private static void readAndWrite(File measFile, int mcc) {
		String newFileName = genNewFileName(measFile.getName(), mcc);
		try (BufferedReader in = new BufferedReader(new FileReader(measFile));
			PrintWriter out = new PrintWriter(new FileWriter(newFileName+".csv"));
			PrintWriter outLog = new PrintWriter(new FileWriter("log.log", true));) {
			
			outLog.println("Starting to read from "+measFile.getName());
			long startTime = System.currentTimeMillis();
			long currentTime = 0;
			long timeUsedInSeconds = 0;
			int linesScanned = 0;
			int relevantLinesFound = 0;
			while(in.ready()) {
				String line = in.readLine();
				String[] lineSplit = line.split(",");
				if(Integer.parseInt(lineSplit[0]) == mcc) {
					out.println(line);
					relevantLinesFound++;
				}
				linesScanned++;
				if(linesScanned % 100000 == 0) {
					currentTime = System.currentTimeMillis();
					timeUsedInSeconds = (currentTime - startTime) / 1000;
					outLog.println("Current scanned lines="+linesScanned+" | Current relevant lines found="+relevantLinesFound+" | current time used="+timeUsedInSeconds+" sec");
				}
			}
			out.println(relevantLinesFound);
			outLog.println("End of file "+measFile.getName());
			currentTime = System.currentTimeMillis();
			timeUsedInSeconds = (currentTime - startTime) / 1000;
			outLog.println("Total scanned lines="+linesScanned+" | Total relevant lines found="+relevantLinesFound+" | Total time used="+timeUsedInSeconds+" sec");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	private static String genNewFileName(String fileName, int mcc) {
		
		String name = fileName.substring(13, fileName.indexOf('.'));
		int number = Integer.parseInt(name);
		
		return "measurements_"+mcc+"_"+number;
	}

}
