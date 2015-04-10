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

	public static Computation computation(DefaultCell originalCell, int n, double d) {
		//		System.out.println("Attempting to create Computation in Generate");
		Line2D.Double longestVector = Process.longestVector(originalCell.getMeasurements(), n);
		//		System.out.println(longestVector.getP1());
		//		System.out.println(longestVector.getP2());
		//		Line2D.Double longestVector = Geom.longestVectorWithSignalStrength(originalCell.getMeasurements(), n);
		DefaultCell heuristicCell1 = Process.findSector(longestVector, originalCell, d);
		//		System.out.println(heuristicDC1.getCellTowerCoordinates());
		//		System.out.println(heuristicDC1.getVectorAngle());
		//		System.out.println(heuristicDC1.getSectorAngle());
		DefaultCell heuristicCell2 = Process.findSector(
				new Line2D.Double(longestVector.getP2(), longestVector.getP1()), originalCell, d);


		Computation comp = new Computation();
		comp.setLongestVector(longestVector);
		comp.setHeuristicCell1(heuristicCell1);
		comp.setHeuristicCell2(heuristicCell2);

		Process.chooseHeuristicDynamicCell(originalCell.getMeasurements(), comp, n);
		//		System.out.println("Picked a heuristic cell");

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
		else
			return dist2;


		//		if(writeToFile) {
		//			String path = "/Users/Johan/Desktop/";
		//			String fileName = String.format("Computation.txt");
		//			try(BufferedWriter bw = new BufferedWriter(new FileWriter(path+fileName, true));){
		//				bw.write("Test subjects\tAngle\tM\tn\td\tMax dist\tMin dist\tTime to compute\tError\n");
		//				bw.write(String.format("%d\t\t%.1f\t%d\t%d\t%.1f\t%.1f\t\t%.1f\t%.1f\t\t%.2f\n", testSubjects, sectorAngle, M, n, d, maxDist, minDist, averageTime, error));
		//			} catch (IOException e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//		}
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
