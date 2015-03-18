package logic;

import infrastructure.Computation;
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
	
	public static Computation computation(DynamicCell originalCell, int n, double d) {
		Line2D.Double longestVector = Geom.longestVector(originalCell.getMeasurements(), n);
//		Line2D.Double longestVector = Geom.longestVectorWithSignalStrength(originalCell.getMeasurements(), n);
		DynamicCell heuristicDC1 = Geom.findSector(longestVector, originalCell, d);
		DynamicCell heuristicDC2 = Geom.findSector(
				new Line2D.Double(longestVector.getP2(), longestVector.getP1()), originalCell, d);
		
		
		Computation comp = new Computation();
		comp.setLongestVector(longestVector);
		comp.setHeuristicDynamicCell1(heuristicDC1);
		comp.setHeuristicDynamicCell2(heuristicDC2);
		
		Geom.chooseHeuristicDynamicCell(originalCell, comp, n);
		
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

			double dist1 = testCell.getCellTowerCoordinates().distance(computation.getHeuristicDynamicCell1().getCellTowerCoordinates());
			double dist2 = testCell.getCellTowerCoordinates().distance(computation.getHeuristicDynamicCell2().getCellTowerCoordinates());

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
	
//	public static void buildings(int buildings, double buildingsMaxDist, double buildingsMinDist, DynamicCell dc) {
//		double obstructionLength = 20.0;
//		Random angleRandom = new Random();
//		for(int i = 0; i < buildings; i++) {
//			double angle = randomDouble(dc.getVectorAngle()+dc.getSectorAngle(), dc.getVectorAngle(), angleRandom);
////			System.out.printf("%.2f\n",angle);
//			double distance = randomDouble(buildingsMaxDist, buildingsMinDist, null);
//			Line2D.Double pointVector = Geom.toCartesian(angle, distance, dc.getCellTowerCoordinates());
////			System.out.printf("Angle point: %.2f\n",Math.toDegrees(Geom.angle(pointVector)));
//			Line2D.Double infinitePointVector = Geom.changeVectorLengthByP2(pointVector, dc.getMaxDistance()+10.0);
//			boolean newPoint = false;
//			for(Line2D.Double obs : dc.getObstructions()) {
//				if(infinitePointVector.intersectsLine(obs)) {
//					newPoint = true;
//					break;
//				}
//			}
//			if(newPoint) {
//				i--;
//				continue;
//			}
//			
//			pointVector = new Line2D.Double(pointVector.getP2(), pointVector.getP1());
//			
//			Line2D.Double rotated1 = Geom.rotateVector(pointVector, -90.0);
//			Line2D.Double rotated2 = Geom.rotateVector(pointVector, 90.0);
////			System.out.printf("Length rotated1: %.2f\n",rotated1.getP1().distance(rotated1.getP2()));
////			System.out.printf("Length rotated2: %.2f\n",rotated2.getP1().distance(rotated2.getP2()));
//			
//			Line2D.Double obstruction = new Line2D.Double(rotated1.getP2(), rotated2.getP2());
////			System.out.printf("Length initial obstruction: %.2f\n",obstruction.getP1().distance(obstruction.getP2()));
//			
//			double newLength = rotated1.getP1().distance(rotated1.getP2())+(obstructionLength/2);
//			obstruction = Geom.changeVectorLengthByP2(obstruction, newLength);
////			System.out.printf("Length obstruction after first cut: %.2f\n",obstruction.getP1().distance(obstruction.getP2()));
//
//			
//			obstruction = Geom.changeVectorLengthByP1(obstruction, obstructionLength);
////			System.out.printf("Length obstruction: %.2f\n",obstruction.getP1().distance(obstruction.getP2()));
////			System.out.printf("Angle obstruction p1: %.2f\n",Math.toDegrees(Geom.angle(new Line2D.Double(dc.getCellTowerCoordinates(), obstruction.getP1()))));
////			System.out.printf("Angle obstruction p2: %.2f\n",Math.toDegrees(Geom.angle(new Line2D.Double(dc.getCellTowerCoordinates(), obstruction.getP2()))));
//			dc.addObstruction(obstruction);
//			
////			System.out.println("--------");
//			
////			Line2D.Double basisVector = Geom.rotateVector(dc.getVector1(), dc.getSectorAngle()/2);
////			
////			double angleBetween = Geom.angle(pointVector, basisVector);
////			
////			double newLength = pointVector.getP1().distance(pointVector.getP2())*
////					Math.cos(Math.toRadians(angleBetween));
////			
////			basisVector = Geom.changeVectorLengthByP2(basisVector, newLength);
//			
////			Line2D.Double perpendicular = new Line2D.Double(basisVector.getP2(), pointVector.getP2());
////			
////			perpendicular = Geom.changeVectorLengthByP1(perpendicular, obstructionLength/2);
////			
////			perpendicular = Geom.changeVectorLengthByP2(perpendicular, obstructionLength);
//			
////			dc.addObstruction(perpendicular);		
//		}
//	}
	
//	public static boolean[] deadZone(double radius, Point2D.Double origo, List<Measurement> measurements) {
//		boolean[] inDeadZone = new boolean[measurements.size()];
//		for(int i = 0; i < measurements.size(); i++) {
//			double dist = origo.distance(measurements.get(i).getCoordinates());
//			if(dist <= radius) {
//				inDeadZone[i] = true;
//			}
//		}
//		return inDeadZone;
//		
//	}
	
	
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
