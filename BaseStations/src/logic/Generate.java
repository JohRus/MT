package logic;

import infrastructure.Computation;
import infrastructure.DefaultCell;
import infrastructure.DefaultMeasurement;
import infrastructure.DynamicCell;
import infrastructure.Measurement;
import infrastructure.SimpleMeasurement;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Generate {

	public static Measurement defaultMeasurement(DynamicCell dc, boolean hasSignal) {
		//		double randomAngle = dc.getVectorAngle() + (dc.getSectorAngle()*new Random().nextDouble());
		double randomAngle = randomDouble(dc.getVectorAngle()+dc.getSectorAngle(), dc.getVectorAngle(), null);
		//		double randomDistance = dc.getMinDistance() + ((dc.getMaxDistance()-dc.getMinDistance())*new Random().nextDouble());
		double randomDistance = randomDouble(dc.getMaxDistance(), dc.getMinDistance(), null);
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

	private static double randomDouble(double max, double min, Random r) {
		if(r == null)
			return min + ((max-min)*new Random().nextDouble());
		else
			return min + ((max-min)*r.nextDouble());
	}

	public static List<Measurement> defaultMeasurements(int measurements, DynamicCell dc, boolean hasSignal) {
		List<Measurement> list = new ArrayList<Measurement>();
		for(int i = 0; i < measurements; i++) {
			list.add(defaultMeasurement(dc, hasSignal));
		}
		return list;
	}

	public static DynamicCell dynamicCellWithMeasurements(
			Point2D.Double cellTowerCoordinates, 
			double vectorAngle, 
			double sectorAngle,
			double measurementMaxDistanceFromCellTower,
			double measurementMinDistanceFromCellTower,
			int measurements,
			boolean hasSignal,
			double deadZoneRadius) {

		DynamicCell dynamicCell = new DynamicCell(cellTowerCoordinates, vectorAngle, sectorAngle, 
				measurementMaxDistanceFromCellTower, measurementMinDistanceFromCellTower);

		dynamicCell.setVectors();

		dynamicCell.setMeasurements(defaultMeasurements(measurements, dynamicCell, hasSignal));

		if(deadZoneRadius > 0.0) {
			double angle = randomDouble(dynamicCell.getVectorAngle()+dynamicCell.getSectorAngle(), dynamicCell.getVectorAngle(), null);
			double radius = randomDouble(dynamicCell.getMaxDistance(), dynamicCell.getMinDistance(), null);
			Line2D.Double v = Geom.toCartesian(angle, radius, dynamicCell.getCellTowerCoordinates());

			dynamicCell.applyDeadZone((Point2D.Double) v.getP2(), deadZoneRadius);
		}

		return dynamicCell;
	}

	public static Computation computation(DefaultCell originalCell, int n, double d, boolean useRSS) {
		Line2D.Double longestVector = Process.longestVector(originalCell.getMeasurements(), n, useRSS);
//		System.out.println("Done with LV");

		DefaultCell heuristicCell1 = Process.findSector(longestVector, originalCell, d);
//		System.out.println("Done with HC1");

		DefaultCell heuristicCell2 = Process.findSector(
				new Line2D.Double(longestVector.getP2(), longestVector.getP1()), originalCell, d);
//		System.out.println("Done with HC2");


		Computation comp = new Computation();
		comp.setLongestVector(longestVector);
		comp.setHeuristicCell1(heuristicCell1);
		comp.setHeuristicCell2(heuristicCell2);

		Process.chooseHeuristicDynamicCell(originalCell.getMeasurements(), comp, n, useRSS);
//		System.out.println("Chosed a HC");

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

	public static double error(DynamicCell testCell, Computation computation) {

		double dist1 = testCell.getCellTowerCoordinates().distance(computation.getHeuristicCell1().getCellTowerCoordinates());
		double dist2 = testCell.getCellTowerCoordinates().distance(computation.getHeuristicCell2().getCellTowerCoordinates());

		if(dist1 <= dist2)
			return dist1;
		else {
			System.out.println("Generate.error returnerte error for heuristicCell2");
			return dist2;
		}
	}
	
	public static double sphericalError(DefaultCell originalCell, DefaultCell chosenHeuristicCell) {
		
		double lon1 = originalCell.getCellTowerCoordinates().getX();
		double lat1 = originalCell.getCellTowerCoordinates().getY();
		
		double lon2 = chosenHeuristicCell.getCellTowerCoordinates().getX();
		double lat2 = chosenHeuristicCell.getCellTowerCoordinates().getY();
		
		double dist = Geom.sphericalDistance(lon1, lat1, lon2, lat2);
			
		return dist;
	}

	public static HashSet<Integer> randomInts(int size, int range, int ... notThese) {
		Random r = new Random();
		HashSet<Integer> hs = new HashSet<Integer>(size);
		while(hs.size() < size) {
			//			while(hs.add(r.nextInt(range)) != true)
			//				;
			while(true) {
				int x = r.nextInt(range);
				boolean cont = false;
				if(notThese != null) {
					for(int i = 0; i < notThese.length; i++) {
						if(x == notThese[i]) {
							cont = true;
							break;
						}
					}
				}
				if(cont) continue;
				//				if(notThese >= 0 && x == notThese)
				//					continue;
				if(hs.add(x) == true)
					break;
			}
		}
		return hs;
	}
}
