package logic;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


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
			double deadZoneRadius) {
		
		return Generate.dynamicCellWithMeasurements(
				cellTowerCoords, 
				vectorAngle, 
				sectorAngle,
				maxDist, 
				minDist, 
				measurements,
				hasSignal,
				deadZoneRadius);		
	}
	
	public Computation generateComputation(DynamicCell originalCell, int n, double d) {
		return Generate.computation(originalCell, n, d);
	}
	
	
	
	public void errors(
			String[] sectorAngleArray,
			double maxDist,
			double minDist,
			int M,
			boolean hasSignal,
			String[] deadzoneRadiusArray,
			String[] nArray,
			String[] dArray,
			int testSubjects,
			boolean writeToFile) {
		
		System.out.println("Angle\tM\tn\td\tdzRadius\tavTime\tError");
		
		for(int j = 0; j < sectorAngleArray.length; j++) {
			double sectorAngle = Double.parseDouble(sectorAngleArray[j]);
			
			for(int i = 0; i < nArray.length; i++) {
				int n = Integer.parseInt(nArray[i]);
				
				for(int k = 0; k < dArray.length; k++) {
					double d = Double.parseDouble(dArray[k]);
					
					for(int l = 0; l < deadzoneRadiusArray.length; l++) {
						double deadzoneRadius = Double.parseDouble(deadzoneRadiusArray[l]);
						
						double error = 0.0;
						Stopwatch sw = new Stopwatch();
						for(int o = 0; o < testSubjects; o++) {
							
							DynamicCell testCell = generateDynamicCell(
									new Point2D.Double(200.0, 200.0), 
									190.0, 
									sectorAngle, 
									maxDist, 
									minDist, 
									M, 
									hasSignal, 
									deadzoneRadius);
							
							sw.start();
							Computation computation = generateComputation(
									testCell, 
									n, 
									d);
							sw.stop();
							
							error += Generate.error(testCell, computation);
						}
						
						error = error/testSubjects;
						double avTime = sw.averageTime();
						
						System.out.printf("%.1f\t%d\t%d\t%.1f\t%.1f\t\t%.2f\t%.2f\n", sectorAngle, M, n, d, deadzoneRadius, avTime, error);
						
					
					}
				}
			}
		}
	}
}
