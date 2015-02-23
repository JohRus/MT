package testdata;

import infrastructure.Computation;
import infrastructure.DefaultMeasurement;
import infrastructure.DynamicCell;
import infrastructure.Measurement;
import infrastructure.SimpleMeasurement;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generate {
	
	/**
	 * 
	 * @param angle1 The angle to the first vector from the x-axis
	 * @param angle2 The angle to the second vector from the x-axis
	 * @param maxDistanceFromCT Max distance from the cell tower
	 * @param minDistanceFromCT Min distance from the cell tower
	 * @return An instance of DefaultMeasurement
	 */
	
//	public static Measurement defaultMeasurement(DynamicCell dc) {
//		double randomAngle = dc.getVectorAngle() + (dc.getSectorAngle()*new Random().nextDouble());
//		double randomDistance = dc.getMinDistance() + ((dc.getMaxDistance()-dc.getMinDistance())*new Random().nextDouble());
//		double x = randomDistance*Math.cos(Math.toRadians(randomAngle));
//		double y = randomDistance*Math.sin(Math.toRadians(randomAngle));
//		return new DefaultMeasurement(new Point2D.Double(dc.getCellTowerCoordinates().getX()+x, dc.getCellTowerCoordinates().getY()+y));
//	}
	
	public static Measurement defaultMeasurement(DynamicCell dc, boolean hasSignal) {
		double randomAngle = dc.getVectorAngle() + (dc.getSectorAngle()*new Random().nextDouble());
		double randomDistance = dc.getMinDistance() + ((dc.getMaxDistance()-dc.getMinDistance())*new Random().nextDouble());
		double x = randomDistance*Math.cos(Math.toRadians(randomAngle));
		double y = randomDistance*Math.sin(Math.toRadians(randomAngle));
		
		Point2D.Double coords = new Point2D.Double(dc.getCellTowerCoordinates().getX()+x, dc.getCellTowerCoordinates().getY()+y);
		
		if(hasSignal) {
			int signal = doubleToInt(dc.getCellTowerCoordinates().distance(coords))*-1;
			return new SimpleMeasurement(coords, signal);
		}
		else
			return new DefaultMeasurement(coords);
	}
	
	public static List<Measurement> defaultMeasurements(int measurements, DynamicCell dc, boolean hasSignal, boolean someMeasurementsAreDefect) {
		List<Measurement> list = new ArrayList<Measurement>();
		for(int i = 0; i < measurements; i++) {
			list.add(defaultMeasurement(dc, hasSignal));
		}
		if(someMeasurementsAreDefect)
			someMeasurementsAreDefect(list, 80, dc.getMaxDistance());
		return list;
	}
	
	public static DynamicCell dynamicCellWithDefaultMeasurements(
			Point2D.Double cellTowerCoordinates, 
			double vectorAngle, 
			double sectorAngle,
			double measurementMaxDistanceFromCellTower,
			double measurementMinDistanceFromCellTower,
			int measurements,
			boolean hasSignal,
			boolean someMeasurementsAreDefect) {
		
		DynamicCell dynamicCell = new DynamicCell(cellTowerCoordinates, vectorAngle, sectorAngle, 
				measurementMaxDistanceFromCellTower, measurementMinDistanceFromCellTower);
		dynamicCell.setVectors();
		
		dynamicCell.setMeasurements(defaultMeasurements(measurements, dynamicCell, hasSignal, someMeasurementsAreDefect));

		return dynamicCell;
	}
	
	public static Computation computation(DynamicCell originalCell, int n, double d) {
		Line2D.Double longestVector = Geom.longestVector(originalCell.getMeasurements(), n);
		DynamicCell heuristicDC1 = Geom.findSector(longestVector, originalCell, d);
		DynamicCell heuristicDC2 = Geom.findSector(
				new Line2D.Double(longestVector.getP2(), longestVector.getP1()), originalCell, d);
		
		
		Computation comp = new Computation();
		comp.setLongestVector(longestVector);
		comp.setHeuristicDynamicCell1(heuristicDC1);
		comp.setHeuristicDynamicCell2(heuristicDC2);
		
		return comp;
	}
	
	private static int doubleToInt(double d) {
		int toInt = (int)d;
		if(d-(double)toInt >= 0.5) {
			return toInt+1;
		}
		else
			return toInt;
	}
	
	private static void someMeasurementsAreDefect(List<Measurement> list, int percent, double maxDist) {
		boolean[] alreadyChanged = new boolean[list.size()];
		int defectMeasurements = doubleToInt((list.size()/100.0)*percent);
		Random r = new Random();
		for(int i = 0; i < defectMeasurements; i++) {
			int item = r.nextInt(list.size());
			if(alreadyChanged[item]) {
				i--;
				continue;
			}
			int currSignalStrength = list.get(item).getSignalStrength();
			int newSignalStrength = currSignalStrength-100;
			list.get(item).setSignalStrength(newSignalStrength);
			alreadyChanged[item] = true;
		}
	}
	
//	public static void main(String[] args) {
//		DynamicCell dc = dynamicCellWithDefaultMeasurements(new Point2D.Double(0.0, 0.0), 0.0, 120.0, 113.0, 0.0, 10, true);
//		for(Measurement m : dc.getMeasurements()) {
//			System.out.println(m.getSignalStrength());
//		}
//		System.out.println("--------");
//		someMeasurementsAreDefect(dc.getMeasurements(), 50, dc.getMaxDistance());
////		System.out.println("--------");
//		for(Measurement m : dc.getMeasurements()) {
//			System.out.println(m.getSignalStrength());
//		}
//	}

	

}
