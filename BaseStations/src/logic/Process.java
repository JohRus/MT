package logic;

import infrastructure.Computation;
import infrastructure.DefaultCell;
import infrastructure.Measurement;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import opencellid.OpenCellIdCell;

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
			return longestVector(measurements, threshold, useRSS);
		}

		return new Line2D.Double(m1.getCoordinates(), m2.getCoordinates());
	}

	public static Line2D.Double longestVectorWithSignalStrength(List<Measurement> measurements, int threshold) {
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
		while(!Geom.pointsFitInsideSectorAngle(subset, heuristicSector.getCellTowerCoordinates(),
				heuristicSector.getVectorAngle(), heuristicSector.getVectorAngle()+heuristicSector.getSectorAngle())) {

			newHeuristicVector = Geom.changeVectorLengthByP1(
					newHeuristicVector, 
					newHeuristicVector.getP1().distance(newHeuristicVector.getP2())+d);
			heuristicSector = computeSector(newHeuristicVector, originalCell);
		}
		System.out.println("found sector");
		System.out.println(heuristicSector.getVectorAngle());
		System.out.println(heuristicSector.getSectorAngle());
		return heuristicSector;
	}


	public static DefaultCell computeSector(Line2D.Double heuristicVector, DefaultCell originalCell) {
		System.out.println(Math.toDegrees(Geom.angle(heuristicVector)));

		double degreesToRotate = originalCell.getSectorAngle()/2;
		Line2D.Double heuristicSectorVector1 = Geom.rotateVector(heuristicVector, degreesToRotate*-1);
		System.out.println(Math.toDegrees(Geom.angle(heuristicSectorVector1)));
		System.out.println();

		return new DefaultCell(
				(Point2D.Double) heuristicVector.getP1(), 
				Math.toDegrees(Geom.angle(heuristicSectorVector1)), 
				originalCell.getSectorAngle());
	}


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

	
	public static DefaultCell averagedCell(List<Measurement> measurements) {
		
		Point2D.Double averagedCellTowerPos = averagedCellTowerPosition(measurements);
		
		DefaultCell averageCell = new DefaultCell(averagedCellTowerPos, 0.0, 120.0);
		
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
	
	public static Point2D.Double averagedCellTowerPosition(List<Measurement> measurements) {
		double sumLon = 0.0;
		double sumLat = 0.0;
		
		for(Measurement m : measurements) {
			sumLon += m.getCoordinates().getX();
			sumLat += m.getCoordinates().getY();
		}
		
		double lon = sumLon/measurements.size();
		double lat = sumLat/measurements.size();
		
		return new Point2D.Double(lon, lat);
	}
	
	public static void main(String[] args) {
		String fullFileName = "/Users/Johan/Documents/CellTowers/cells_exact_samples67-120_2036.json";
		opencellid.Controller cont = new opencellid.Controller();
		List<OpenCellIdCell> cells = cont.parseCells(fullFileName, 240, 0, 1000000);
		for(OpenCellIdCell cell : cells) {
			if(cell.getMcc() == 262 && cell.getNet() == 7 && cell.getArea() == 30605 && cell.getCell() == 342) {
				System.out.println(cell.getMeasurements().size());
				System.out.println(cell.getCellTowerCoordinates());
				System.out.println(cell.getAverageSignal());
				System.out.println(cell.getRange());
				System.out.println(cell.getSamples());
				System.out.println(cell.getRadio());
				
//				DefaultCell averagedCell = Process.averageCellTowerPosition(cell.getMeasurements());
//				System.out.println("\n"+averagedCell.getCellTowerCoordinates());
//				System.out.println(averagedCell.getMeasurements().size());
//				System.out.println(averagedCell.getVectorAngle());
				
//				for(Measurement m : cell.getMeasurements()) {
//					if(Geom.sphericalDistance(cell.getCellTowerCoordinates().getX(), cell.getCellTowerCoordinates().getY(), m.getCoordinates().getX(), m.getCoordinates().getY()) > 9450) {
//						System.out.println(m.getCoordinates());
//					}
//				}
			}
		}
		
	}

}
