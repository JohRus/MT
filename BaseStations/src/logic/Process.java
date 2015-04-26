package logic;

import infrastructure.Computation;
import infrastructure.DefaultCell;
import infrastructure.DynamicCell;
import infrastructure.Measurement;
import infrastructure.SimpleMeasurement;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.googlecode.jcsv.annotations.MapToColumn.Default;

import opencellid.OpenCellIdCell;
import opencellid.OpenCellIdMeasurement;

public class Process {

	public static Line2D.Double longestVector(List<Measurement> measurements, int threshold, boolean useRSS) {

		HashSet<Integer> items = Generate.randomInts(threshold, measurements.size(), null);


		Measurement m1 = null;
		Measurement m2 = null;
		double diff = 0.0;

		for(int i : items) {
			Measurement item1 = measurements.get(i);

			Random r2 = new Random();

			for(int j = 0; j < threshold; j++) {
				int r = r2.nextInt(measurements.size());
				if(r == i || measurements.get(r).getCoordinates().equals(measurements.get(i).getCoordinates())) {
					while(true) {
						r = r2.nextInt(measurements.size());
						if(r != i && !measurements.get(r).getCoordinates().equals(measurements.get(i).getCoordinates()))
							break;
					}
				}
//				if(r == i) {
//					j--;
//					continue;
//				}
				Measurement item2 = measurements.get(r);
				
				if(useRSS) {
					double currDiff = Math.abs(item1.getSignalStrength()-item2.getSignalStrength());
					if(currDiff > diff) {
						m1 = item1;
						m2 = item2;
						diff = currDiff;
					}
				}
				else {
					double currDiff = item1.getCoordinates().distance(item2.getCoordinates());
					if(currDiff > diff) {
						m1 = item1;
						m2 = item2;
						diff = currDiff;
					}
				}
			}	
		}
		
		if(m1 == null || m2 == null) {
//			System.out.println("m1 or m2 is null");
			return longestVector(measurements, threshold, useRSS);
		}
		
//		if(m1.getCoordinates().equals(m2.getCoordinates())) {
//			System.out.println("Koordinatene er like");
//			if(m1 == m2)
//				System.out.println("m1 og m2 er samme measurement");
//			else {
//				System.out.println("m1 RSS: "+m1.getSignalStrength());
//				System.out.println("m2 RSS: "+m2.getSignalStrength());
//			}
//		}

		return new Line2D.Double(m1.getCoordinates(), m2.getCoordinates());
	}

	//	public static Line2D.Double longestVector(List<Measurement> measurements, int threshold) {
	//		
	//		HashSet<Integer> items = Generate.randomInts(threshold, measurements.size(), null);
	//		
	//
	//		Measurement m1 = null;
	//		Measurement m2 = null;
	//		double diff = 0.0;
	//
	//		for(int i : items) {
	//			Measurement item1 = measurements.get(i);
	//
	//			Random r2 = new Random();
	//
	//			for(int j = 0; j < threshold; j++) {
	//				int r = r2.nextInt(measurements.size());
	//				if(r == i) {
	//					j--;
	//					continue;
	//				}
	//				Measurement item2 = measurements.get(r);
	//				
	//				if(item1 instanceof OpenCellIdMeasurement && item2 instanceof OpenCellIdMeasurement) {
	//					double currDiff = Math.abs(item1.getSignalStrength()-item2.getSignalStrength());
	//					if(currDiff > diff) {
	//						m1 = item1;
	//						m2 = item2;
	//						diff = currDiff;
	//					}
	//				}
	//
	//				else if(item1 instanceof SimpleMeasurement && item2 instanceof SimpleMeasurement) {
	//					double currDiff = Math.abs(item1.getSignalStrength()-item2.getSignalStrength());
	//					if(currDiff > diff) {
	//						m1 = item1;
	//						m2 = item2;
	//						diff = currDiff;
	//					}
	//				}
	//				
	//				else {
	//					double currDiff = item1.getCoordinates().distance(item2.getCoordinates());
	//					if(currDiff > diff) {
	//						m1 = item1;
	//						m2 = item2;
	//						diff = currDiff;
	//					}
	//				}
	//			}	
	//		}
	////		
	//		if(m1 == null || m2 == null) {
	//			System.out.println("m1 or m2 is null");
	//			return longestVector(measurements, threshold);
	//		}
	//		
	//		return new Line2D.Double(m1.getCoordinates(), m2.getCoordinates());
	//	}

	public static Line2D.Double longestVectorWithSignalStrength(List<Measurement> measurements, int threshold) {
		//		Measurement strongest = new SimpleMeasurement(new Point2D.Double(0.0,0.0), -500);
		int strongestPos = -1;
		HashSet<Integer> items = Generate.randomInts(threshold, measurements.size(), null);

		for(int i : items) {
			if(strongestPos < 0) {
				strongestPos = i;
				continue;
			}
			if(measurements.get(i).getSignalStrength() > measurements.get(strongestPos).getSignalStrength()) {
				strongestPos = i;
			}
		}
		System.out.println("funnet strongest");

		items = Generate.randomInts(threshold, measurements.size(), strongestPos);
		System.out.printf("strongestPos=%d, RSS=%d\n", strongestPos, measurements.get(strongestPos).getSignalStrength());
		for(int i : items) {
			System.out.println(i);
		}
		LinkedList<Integer> weakest = new LinkedList<Integer>();
		for(Integer i : items) {
			if(weakest.size() < 5) {
				weakest.add(i);
				continue;
			}
			for(int j = 0; j < weakest.size(); j++) {
				if(measurements.get(i).getSignalStrength() < measurements.get(weakest.get(j)).getSignalStrength()) {
					weakest.remove(j);
					weakest.add(j, i);
					break;
				}
			}
		}
		System.out.println("funnet de svakeste");
		double angleSum = 0.0;
		for(Integer i : weakest) {
			angleSum += Math.toDegrees(Geom.angle(new Line2D.Double(
					measurements.get(strongestPos).getCoordinates(), 
					measurements.get(i).getCoordinates())));
		}
		double longestVectorAngle = angleSum / weakest.size();
		double longestVectorLength = measurements.get(strongestPos).getCoordinates().distance(
				measurements.get(weakest.peekFirst()).getCoordinates());
		System.out.println("funnet lv");

		return Geom.toCartesian(longestVectorAngle, longestVectorLength, measurements.get(strongestPos).getCoordinates());
	}

	public static DefaultCell findSector(Line2D.Double heuristicVector, DefaultCell originalCell, double d) {
		DefaultCell heuristicSector = computeSector(heuristicVector, originalCell);

		List<Measurement> subset = originalCell.getMeasurements();
		Line2D.Double newHeuristicVector = heuristicVector;
//		System.out.println("Entering while loop");
//		int c = 0;
		while(!Geom.pointsFitInsideSectorAngle(subset, heuristicSector.getCellTowerCoordinates(),
				heuristicSector.getVectorAngle(), heuristicSector.getVectorAngle()+heuristicSector.getSectorAngle())) {
//			c++;
//			if(c >= 500) {
//				System.out.println(heuristicVector.getP1()+" |||||| "+heuristicVector.getP2());
//				System.out.println(newHeuristicVector.getP1()+" |||||| "+newHeuristicVector.getP2());
//				System.out.println("Original cell: "+originalCell.getCellTowerCoordinates().getX()+"-"+originalCell.getCellTowerCoordinates().getY());
//				System.out.println("Heuristic cell: "+heuristicSector.getCellTowerCoordinates().getX()+"-"+heuristicSector.getCellTowerCoordinates().getY());
//				System.out.println("angle1: "+heuristicSector.getVectorAngle()+" ===== angle2: "+heuristicSector.getVectorAngle()+heuristicSector.getSectorAngle());
//				System.exit(0);
//			}
			newHeuristicVector = Geom.changeVectorLengthByP1(
					newHeuristicVector, 
					newHeuristicVector.getP1().distance(newHeuristicVector.getP2())+d);
			heuristicSector = computeSector(newHeuristicVector, originalCell);
		}
//		System.out.println("done with while loop");
		return heuristicSector;
	}

	//	public static DynamicCell findSector(Line2D.Double heuristicVector, DynamicCell originalCell, double d) {
	//		DynamicCell heuristicSector = computeSector(heuristicVector, originalCell);
	//		List<Measurement> subset = originalCell.getMeasurements();
	//		Line2D.Double newHeuristicVector = heuristicVector;
	//		//			double distToAdd = 10.0;
	//		while(!Geom.pointsFitInsideSectorAngle(subset, heuristicSector)) {
	//			// modifisere subset slik at jeg utelukker de målingene jeg vet passer
	//
	//			newHeuristicVector = Geom.changeVectorLengthByP1(
	//					newHeuristicVector, 
	//					newHeuristicVector.getP1().distance(newHeuristicVector.getP2())+d);
	//			heuristicSector = computeSector(newHeuristicVector, originalCell);
	//		}
	//		return heuristicSector;
	//	}

	public static DefaultCell computeSector(Line2D.Double heuristicVector, DefaultCell originalCell) {		

		double degreesToRotate = originalCell.getSectorAngle()/2;
		Line2D.Double heuristicSectorVector1 = Geom.rotateVector(heuristicVector, degreesToRotate*-1);

		return new DefaultCell(
				(Point2D.Double) heuristicVector.getP1(), 
				Math.toDegrees(Geom.angle(heuristicSectorVector1)), 
				originalCell.getSectorAngle());
	}

	//	public static DefaultCell computeSector(Line2D.Double heuristicVector, DefaultCell originalCell) {		
	////		System.out.println(heuristicVector.getP1());
	////		System.out.println(heuristicVector.getP2());
	//		double degreesToRotate = originalCell.getSectorAngle()/2;
	//		Line2D.Double heuristicSectorVector1 = Geom.rotateVector(heuristicVector, degreesToRotate*-1);
	//		Line2D.Double heuristicSectorVector2 = Geom.rotateVector(heuristicVector, degreesToRotate);
	//		if(originalCell instanceof DynamicCell) {
	//			DynamicCell dynamicOriginalCell = (DynamicCell) originalCell;
	//			heuristicSectorVector1 = Geom.changeVectorLengthByP2(heuristicSectorVector1, dynamicOriginalCell.getMaxDistance());
	//			heuristicSectorVector2 = Geom.changeVectorLengthByP2(heuristicSectorVector2, dynamicOriginalCell.getMaxDistance());
	//
	//			DynamicCell heuristicCell = new DynamicCell(
	//					(Point2D.Double) heuristicVector.getP1(), 
	//					Math.toDegrees(Geom.angle(heuristicSectorVector1)), 
	//					dynamicOriginalCell.getSectorAngle(), 
	//					dynamicOriginalCell.getMaxDistance(), 
	//					dynamicOriginalCell.getMinDistance());
	//			heuristicCell.setVector1(heuristicSectorVector1);
	//			heuristicCell.setVector2(heuristicSectorVector2);
	//			
	//			return heuristicCell;
	//		}
	//		else {
	//			OpenCellIdCell openCellIdOriginalCell = (OpenCellIdCell) originalCell;
	//			return new OpenCellIdCell(
	//					(Point2D.Double) heuristicVector.getP1(), 
	//					openCellIdOriginalCell.getRadio(), 
	//					openCellIdOriginalCell.getMcc(), 
	//					openCellIdOriginalCell.getNet(), 
	//					openCellIdOriginalCell.getArea(), 
	//					openCellIdOriginalCell.getCell(), 
	//					openCellIdOriginalCell.getRange(), 
	//					openCellIdOriginalCell.getSamples(), 
	//					openCellIdOriginalCell.getChangeable(), 
	//					openCellIdOriginalCell.getAverageSignal());
	//		}
	//	}

	//	public static DynamicCell computeSector(Line2D.Double heuristicVector, DynamicCell originalCell) {		
	//
	//		double degreesToRotate = originalCell.getSectorAngle()/2;
	//		Line2D.Double heuristicSectorVector1 = Geom.rotateVector(heuristicVector, degreesToRotate*-1);
	//		heuristicSectorVector1 = Geom.changeVectorLengthByP2(heuristicSectorVector1, originalCell.getMaxDistance());
	//		Line2D.Double heuristicSectorVector2 = Geom.rotateVector(heuristicVector, degreesToRotate);
	//		heuristicSectorVector2 = Geom.changeVectorLengthByP2(heuristicSectorVector2, originalCell.getMaxDistance());
	//		DynamicCell heuristicCell = new DynamicCell(
	//				(Point2D.Double) heuristicVector.getP1(), 
	//				Math.toDegrees(Geom.angle(heuristicSectorVector1)), 
	//				originalCell.getSectorAngle(), 
	//				originalCell.getMaxDistance(), 
	//				originalCell.getMinDistance());
	//		heuristicCell.setVector1(heuristicSectorVector1);
	//		heuristicCell.setVector2(heuristicSectorVector2);
	//		return heuristicCell;
	//	}

	public static void chooseHeuristicDynamicCell(List<Measurement> measurements, Computation comp, int threshold, boolean useRSS) {
		if(useRSS) {
			HashSet<Integer> indices = Generate.randomInts(threshold, measurements.size(), null);
			int sum1 = 0;
			int sum2 = 0;
			int count1 = 0;
			int count2 = 0;
			for(int i : indices) {
				Measurement curr = measurements.get(i);
				double dist1 = comp.getLongestVector().getP1().distance(curr.getCoordinates());
				double dist2 = comp.getLongestVector().getP2().distance(curr.getCoordinates());
				if(dist1 <= dist2) {
					sum1 += curr.getSignalStrength();
					count1++;
				}
				else {
					sum2 += curr.getSignalStrength();
					count2++;
				}
			}
			double average1 = sum1/(double)count1;
			double average2 = sum2/(double)count2;

			if(average1 >= average2) {
				double dist1 = comp.getLongestVector().getP1().distance(comp.getHeuristicCell1().getCellTowerCoordinates());
				double dist2 = comp.getLongestVector().getP1().distance(comp.getHeuristicCell2().getCellTowerCoordinates());
				if(dist2 < dist1) {
					DefaultCell temp = comp.getHeuristicCell1();
					comp.setHeuristicCell1(comp.getHeuristicCell2());
					comp.setHeuristicCell2(temp);
				}
			}
			else {
				double dist1 = comp.getLongestVector().getP2().distance(comp.getHeuristicCell1().getCellTowerCoordinates());
				double dist2 = comp.getLongestVector().getP2().distance(comp.getHeuristicCell2().getCellTowerCoordinates());
				if(dist2 < dist1) {
					DefaultCell temp = comp.getHeuristicCell1();
					comp.setHeuristicCell1(comp.getHeuristicCell2());
					comp.setHeuristicCell2(temp);
				}
			}
		}
		else {
			distanceFromSurroundingPointsToVector(comp, measurements, threshold);
		}
	}

	public static void distanceFromSurroundingPointsToVector(Computation comp, List<Measurement> measurements, int threshold) {
		double sum1 = 0.0;
		int count1 = 0;
		double sum2 = 0.0;
		int count2 = 0;

		HashSet<Integer> indices = Generate.randomInts(threshold, measurements.size(), null);

		for(int i :  indices) {
			Measurement curr = measurements.get(i);

			double dist1 = comp.getLongestVector().getP1().distance(curr.getCoordinates());
			double dist2 = comp.getLongestVector().getP2().distance(curr.getCoordinates());
			if(dist1 <= dist2) {
				sum1 += comp.getLongestVector().ptLineDist(curr.getCoordinates());
				count1++;
			}
			else {
				sum2 += comp.getLongestVector().ptLineDist(curr.getCoordinates());
				count2++;
			}

		}

		double average1 = sum1/count1;
		double average2 = sum2/count2;

		if(average1 <= average2) {
			double dist1 = comp.getLongestVector().getP1().distance(comp.getHeuristicCell1().getCellTowerCoordinates());
			double dist2 = comp.getLongestVector().getP1().distance(comp.getHeuristicCell2().getCellTowerCoordinates());
			if(dist2 < dist1) {
				DefaultCell temp = comp.getHeuristicCell1();
				comp.setHeuristicCell1(comp.getHeuristicCell2());
				comp.setHeuristicCell2(temp);
			}
		}
		else {
			double dist1 = comp.getLongestVector().getP2().distance(comp.getHeuristicCell1().getCellTowerCoordinates());
			double dist2 = comp.getLongestVector().getP2().distance(comp.getHeuristicCell2().getCellTowerCoordinates());
			if(dist2 < dist1) {
				DefaultCell temp = comp.getHeuristicCell1();
				comp.setHeuristicCell1(comp.getHeuristicCell2());
				comp.setHeuristicCell2(temp);
			}
		}
	}







	public static Line2D.Double linearRegressionVector(List<Measurement> measurements, int threshold, 
			Line2D.Double longestVector) {
		//		Collections.sort(measurements);

		List<Measurement> measurementSelection = measurements.subList(0, threshold);
		//		System.out.println("List size = "+measurementSelection.size());
		//		double smallestX = 113.0;
		//		double largestX = -113.0;

		double meanOfX = 0.0;
		double meanOfY = 0.0;
		double meanOfXY = 0.0;

		// mean(x^2)
		double meanOfXSquared = 0.0;

		// (mean(x))^2
		double meanOfXAndThenSquared = 0.0;

		for(Measurement m : measurementSelection) {
			meanOfX += m.getCoordinates().getX();
			meanOfY += m.getCoordinates().getY();
			meanOfXY += m.getCoordinates().getX()*m.getCoordinates().getY();
			meanOfXSquared += Math.pow(m.getCoordinates().getX(), 2);

			//			if(m.getCoordinates().getX() > largestX) largestX = m.getCoordinates().getX();
			//			if(m.getCoordinates().getX() < smallestX) smallestX = m.getCoordinates().getX();
		}
		meanOfX = meanOfX/measurementSelection.size();
		meanOfY = meanOfY/measurementSelection.size();
		meanOfXY = meanOfXY/measurementSelection.size();
		meanOfXSquared = meanOfXSquared/measurementSelection.size();
		meanOfXAndThenSquared = Math.pow(meanOfX, 2);

		//		System.out.printf("Mean of x = %.4f\n", meanOfX);
		//		System.out.printf("Mean of y = %.4f\n", meanOfY);
		//		System.out.printf("Mean of xy = %.4f\n", meanOfXY);
		//		System.out.printf("Mean of x^2 = %.4f\n", meanOfXSquared);
		//		System.out.printf("(Mean of x)^2 = %.4f\n", meanOfXAndThenSquared);

		double m = ((meanOfX*meanOfY)-meanOfXY)/(meanOfXAndThenSquared-meanOfXSquared);
		double b = meanOfY-(m*meanOfX);

		//		System.out.printf("m = %.4f\n", m);
		//		System.out.printf("b = %.4f\n", b);

		//		Point2D.Double pointA = new Point2D.Double(measurements.get(0).getCoordinates().getX(), 
		//				(m*measurements.get(0).getCoordinates().getX())+b);

		//		Point2D.Double pointA = new Point2D.Double(smallestX, (m*smallestX)+b);
		Point2D.Double pointA = new Point2D.Double(longestVector.getX1(), (m*longestVector.getX1())+b);

		//		Point2D.Double pointB = new Point2D.Double(measurements.get(measurements.size()-1).getCoordinates().getX(),
		//				(m*measurements.get(measurements.size()-1).getCoordinates().getX())+b);

		//		Point2D.Double pointB = new Point2D.Double(largestX, (m*largestX)+b);
		Point2D.Double pointB = new Point2D.Double(longestVector.getX2(), (m*longestVector.getX2())+b);

		return new Line2D.Double(pointA, pointB);
	}
	
	public static DefaultCell averageCellTowerPosition(List<Measurement> measurements) {
		double sumLon = 0.0;
		double sumLat = 0.0;
		
		for(Measurement m : measurements) {
			sumLon += m.getCoordinates().getX();
			sumLat += m.getCoordinates().getY();
		}
		
		double lon = sumLon/measurements.size();
		double lat = sumLat/measurements.size();
		
		DefaultCell averageCell = new DefaultCell(new Point2D.Double(lon, lat), 0.0, 120.0);
		
		double vectorAngle = 0.0;
		
		List<Measurement> maxMeasurementsThatFit = new ArrayList<Measurement>();
		
		while(vectorAngle < 360.0) {
			List<Measurement> currentMeasurementsThatFit = new ArrayList<Measurement>();
			for(Measurement m : measurements) {
				if(Geom.pointIsWithinSectorAngleBoundries(
						m.getCoordinates(), 
						averageCell.getCellTowerCoordinates(), 
						vectorAngle, 
						vectorAngle+averageCell.getSectorAngle())) {
					
					currentMeasurementsThatFit.add(m);
				}
			}
			
			if(currentMeasurementsThatFit.size() > maxMeasurementsThatFit.size()) {
				maxMeasurementsThatFit = currentMeasurementsThatFit;
				averageCell.setVectorAngle(vectorAngle);
			}
			vectorAngle += 0.1;
		}
		
		averageCell.setMeasurements(maxMeasurementsThatFit);
		
		return averageCell;
		
//		return new DefaultCell(new Point2D.Double(lon, lat), 120.0);
	}
	
	public static void main(String[] args) {
		String fullFileName = "/Users/Johan/Documents/CellTowers/cells_exact_samples67-120_2036.json";
		opencellid.Controller cont = new opencellid.Controller();
		List<OpenCellIdCell> cells = cont.parseCells(fullFileName, 135, 130, 59675);
		for(OpenCellIdCell cell : cells) {
			if(cell.getMcc() == 262 && cell.getNet() == 7 && cell.getArea() == 30605 && cell.getCell() == 342) {
				System.out.println(cell.getMeasurements().size());
				
				DefaultCell averagedCell = Process.averageCellTowerPosition(cell.getMeasurements());
				System.out.println("\n"+averagedCell.getCellTowerCoordinates());
				System.out.println(averagedCell.getMeasurements().size());
				System.out.println(averagedCell.getVectorAngle());
				
//				for(Measurement m : cell.getMeasurements()) {
//					if(Geom.sphericalDistance(cell.getCellTowerCoordinates().getX(), cell.getCellTowerCoordinates().getY(), m.getCoordinates().getX(), m.getCoordinates().getY()) > 9450) {
//						System.out.println(m.getCoordinates());
//					}
//				}
			}
		}
		
	}

}
