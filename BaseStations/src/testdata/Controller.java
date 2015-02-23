package testdata;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import infrastructure.Computation;
import infrastructure.DynamicCell;

public class Controller {

	public DynamicCell generateDynamicCell(
			Point2D.Double cellTowerCoords, 
			double vectorAngle, 
			double sectorAngle, 
			double maxDist, 
			double minDist, 
			int measurements,
			boolean hasSignal,
			boolean someMeasurementsAreDefect) {
		
		return Generate.dynamicCellWithDefaultMeasurements(
				cellTowerCoords, 
				vectorAngle, 
				sectorAngle,
				maxDist, 
				minDist, 
				measurements,
				hasSignal,
				someMeasurementsAreDefect);		
	}
	
	public Computation generateComputation(DynamicCell originalCell, int n, double d) {
		return Generate.computation(originalCell, n, d);
	}
	
	public double averageError(int testSubjects, int M, int n, double sectorAngle, double d, double maxDist, double minDist, boolean hasSignal, boolean someMeasurementsAreDefect, boolean writeToFile) {
		
		Stopwatch sw = new Stopwatch();
		
		double error = 0.0;
		
		for(int i = 0; i < testSubjects; i++) {
			DynamicCell testCell = generateDynamicCell(
					new Point2D.Double(200.0, 200.0), 
					190.0, 
					sectorAngle, 
					maxDist, 
					minDist, 
					M,
					hasSignal,
					someMeasurementsAreDefect);
			
			sw.start();
			Computation comp = generateComputation(testCell, n, d);
			sw.stop();
			
			double dist1 = testCell.getCellTowerCoordinates().distance(comp.getHeuristicDynamicCell1().getCellTowerCoordinates());
			double dist2 = testCell.getCellTowerCoordinates().distance(comp.getHeuristicDynamicCell2().getCellTowerCoordinates());
			
			if(dist1 <= dist2)
				error += dist1;
			else
				error += dist2;
		}
		
		error = error/testSubjects;
		
		double averageTime = sw.averageTime();
		
//		System.out.println("Angle\tM\tn\tavTime\tError");
		System.out.println(String.format("%.1f\t%d\t%d\t%.1f\t%.2f\t%.2f", sectorAngle, M, n, d, averageTime, error));
		
		
		
		if(writeToFile) {
			String path = "/Users/Johan/Desktop/";
			String fileName = String.format("Computation.txt");
			try(BufferedWriter bw = new BufferedWriter(new FileWriter(path+fileName, true));){
				bw.write("Test subjects\tAngle\tM\tn\td\tMax dist\tMin dist\tTime to compute\tError\n");
				bw.write(String.format("%d\t\t%.1f\t%d\t%d\t%.1f\t%.1f\t\t%.1f\t%.1f\t\t%.2f\n", testSubjects, sectorAngle, M, n, d, maxDist, minDist, averageTime, error));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return error;
	}
	
	public void averageErrors(int testSubjects, int M, String[] ns, String[] sectorAngles, String[] ds, double maxDist, double minDist, boolean hasSignal, boolean someMeasurementsAreDefect, boolean writeToFile) {
		for(int i = 0; i < ns.length; i++) {
			int n = Integer.parseInt(ns[i]);
			for(int j = 0; j < sectorAngles.length; j++) {
				double sectorAngle = Double.parseDouble(sectorAngles[j]);
				for(int k = 0; k < ds.length; k++) {
					double d = Double.parseDouble(ds[k]);
					double error = averageError(testSubjects, M, n, sectorAngle, d, maxDist, minDist, hasSignal, someMeasurementsAreDefect, writeToFile);
				}
			}
		}
	}
}
