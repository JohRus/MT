package testdata;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import infrastructure.Computation;
import infrastructure.DynamicCell;

public class Controller {

	public DynamicCell generateDynamicCell(
			Point2D.Double cellTowerCoords, 
			double vectorAngle, 
			double sectorAngle, 
			double maxDist, 
			double minDist, 
			int measurements) {
		
		return Generate.dynamicCellWithDefaultMeasurements(
				cellTowerCoords, 
				vectorAngle, 
				sectorAngle,
				maxDist, 
				minDist, 
				measurements);		
	}
	
	public Computation generateComputation(DynamicCell originalCell, int n) {
		return Generate.computation(originalCell, n);
	}
	
	public double averageError(int testSubjects, int M, int n, double sectorAngle, double maxDist, double minDist, boolean writeToFile) {
		
		double error = 0.0;
		
		for(int i = 0; i < testSubjects; i++) {
			DynamicCell testCell = generateDynamicCell(
					new Point2D.Double(200.0, 200.0), 
					190.0, 
					sectorAngle, 
					maxDist, 
					minDist, 
					M);
			
			Computation comp = generateComputation(testCell, n);
			
			double dist1 = testCell.getCellTowerCoordinates().distance(comp.getHeuristicDynamicCell1().getCellTowerCoordinates());
			double dist2 = testCell.getCellTowerCoordinates().distance(comp.getHeuristicDynamicCell2().getCellTowerCoordinates());
			
			if(dist1 <= dist2)
				error += dist1;
			else
				error += dist2;
		}
		
		error = error/testSubjects;
		
		System.out.println(String.format("%.1f\t%d\t%d\t%.2f\n", sectorAngle, M, n, error));
		
		if(writeToFile) {
			String path = "/Users/Johan/Desktop/";
			String fileName = String.format("%.1f-%d-%d.txt", sectorAngle, M, n);
			try(BufferedWriter bw = new BufferedWriter(new FileWriter(path+fileName));){
				bw.write("Test subjects\tAngle\tM\tn\tMax dist\tMin dist\tError\n");
				bw.write(String.format("%d\t\t%.1f\t%d\t%d\t%.1f\t\t%.1f\t\t%.2f\n", testSubjects, sectorAngle, M, n, maxDist, minDist, error));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return error;
	}
	
	public void averageErrors(int testSubjects, int M, String[] ns, String[] sectorAngles, double maxDist, double minDist, boolean writeToFile) {
		for(int i = 0; i < ns.length; i++) {
			int n = Integer.parseInt(ns[i]);
			for(int j = 0; j < sectorAngles.length; j++) {
				double sectorAngle = Double.parseDouble(sectorAngles[j]);
				double error = averageError(testSubjects, M, n, sectorAngle, maxDist, minDist, writeToFile);
			}
		}
	}
}
