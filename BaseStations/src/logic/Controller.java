package logic;

import java.awt.geom.Point2D;
import java.util.HashMap;

import gui.Charts4J;
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

	public Computation generateComputation(DynamicCell originalCell, int n, double d, boolean useRSS) {
		Computation comp = Generate.computation(originalCell, n, d, useRSS);

		DynamicCell heuristicDynamicCell1 = new DynamicCell(
				comp.getHeuristicCell1().getCellTowerCoordinates(), 
				comp.getHeuristicCell1().getVectorAngle(), 
				comp.getHeuristicCell1().getSectorAngle(), 
				originalCell.getMaxDistance(), 
				originalCell.getMinDistance());

		heuristicDynamicCell1.setVectors();

		DynamicCell heuristicDynamicCell2 = new DynamicCell(
				comp.getHeuristicCell2().getCellTowerCoordinates(), 
				comp.getHeuristicCell2().getVectorAngle(), 
				comp.getHeuristicCell2().getSectorAngle(), 
				originalCell.getMaxDistance(), 
				originalCell.getMinDistance());

		heuristicDynamicCell2.setVectors();

		comp.setHeuristicCell1(heuristicDynamicCell1);
		comp.setHeuristicCell2(heuristicDynamicCell2);

		return comp;
		//		return Generate.computation(originalCell, n, d);
	}



	public void errors(
			String[] sectorAngleArray,
			String[] maxDistArray,
			double minDist,
			String[] MArray,
			boolean hasSignal,
			String[] deadzoneRadiusArray,
			String[] nArray,
			String[] dArray,
			int testSubjects,
			boolean writeToFile) {

		System.out.println("Angle\tM\tn\tr_incl\td\tdzRadius\tErrorD\tavTimeD\tErrorRSS\tavTimeRSS");

		HashMap<double[],String> dataPoints = new HashMap<double[], String>();

		for(int p = 0; p < MArray.length; p++) {
			int M = Integer.parseInt(MArray[p]);
			
//			for(int i = 0; i < nArray.length; i++) {
//				int n = Integer.parseInt(nArray[i]);
			
			int n = Integer.parseInt(nArray[p]);
//			for(int i = 0; i < 2; i++) {
//				int n  = Integer.parseInt(nArray[(p*2)+i]);

				double[] errorArrayD = new double[deadzoneRadiusArray.length];
				double[] errorArrayRSS = new double[deadzoneRadiusArray.length];

				for(int j = 0; j < sectorAngleArray.length; j++) {
					double sectorAngle = Double.parseDouble(sectorAngleArray[j]);

					for(int q = 0; q < maxDistArray.length; q++) {
						double maxDist = Double.parseDouble(maxDistArray[q]); 

						for(int k = 0; k < dArray.length; k++) {
							double d = Double.parseDouble(dArray[k]);

							for(int l = 0; l < deadzoneRadiusArray.length; l++) {
								double deadzoneRadius = Double.parseDouble(deadzoneRadiusArray[l]);
								
								double errorD = 0.0;
								double errorRSS = 0.0;
								
								Stopwatch swD = new Stopwatch();
								Stopwatch swRSS = new Stopwatch();
								
								for(int o = 0; o < testSubjects; o++) {

									DynamicCell testCellD = generateDynamicCell(
											new Point2D.Double(400.0, 400.0), 
											120.0, 
											sectorAngle, 
											maxDist, 
											minDist, 
											M, 
											false, 
											deadzoneRadius);
									
									
									DynamicCell testCellRSS = generateDynamicCell(
											new Point2D.Double(400.0, 400.0), 
											120.0, 
											sectorAngle, 
											maxDist, 
											minDist, 
											M, 
											true, 
											deadzoneRadius);

									swD.start();
									Computation computationD = generateComputation(
											testCellD, 
											n, 
											d,
											false);
									swD.stop();
									
									swRSS.start();
									Computation computationRSS = generateComputation(
											testCellRSS, 
											n, 
											d,
											true);
									swRSS.stop();
									

									errorD += Generate.error(testCellD, computationD);
									errorRSS += Generate.error(testCellRSS, computationRSS);
								}

								errorD = errorD/testSubjects;
								errorRSS = errorRSS/testSubjects;
								
								double avTimeD = swD.averageTime();
								double avTimeRSS = swRSS.averageTime();

								errorArrayD[l] = errorD;
								errorArrayRSS[l] = errorRSS;

								System.out.printf("%.1f\t%d\t%d\t%.1f\t%.1f\t%.1f\t\t%.2f\t%.5f\t%.2f\t\t%.5f\n", sectorAngle, M, n, maxDist, d, deadzoneRadius, errorD, avTimeD, errorRSS, avTimeRSS);

							}
						}
					}
				}
				String valueD = "D-CTL, M="+M+", n="+n;
				String valueRSS = "RSS-CTL, M="+M+", n="+n;
				dataPoints.put(errorArrayD, valueD);
				dataPoints.put(errorArrayRSS, valueRSS);
				for(double e : errorArrayD) {
					System.out.printf("%.2f|",e);
				}
				System.out.println();
				for(double e : errorArrayRSS) {
					System.out.printf("%.2f|",e);
				}
				System.out.println();
//			}
		}
		String url = Charts4J.errorChartScalingDeadzonesTheoretically(dataPoints);
		System.out.println("\n"+url);
		
		System.out.println("\nEnd of computations");
	}
}
