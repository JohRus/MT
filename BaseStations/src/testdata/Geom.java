package testdata;

import infrastructure.DefaultMeasurement;
import infrastructure.DynamicCell;
import infrastructure.Measurement;

import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Geom {
	
	public static Line2D.Double rotateVector(Line2D.Double toBeRotated, double degrees) {
		Point2D.Double a = (Point2D.Double) toBeRotated.getP1();
		double radians = Math.toRadians(degrees);
		double x = (toBeRotated.getX2()*Math.cos(radians))-(toBeRotated.getY2()*Math.sin(radians));
		double y = (toBeRotated.getX2()*Math.sin(radians))+(toBeRotated.getY2()*Math.cos(radians));
		return new Line2D.Double(a, new Point2D.Double(x, y));
	}
	
	public static double angle(Line2D.Double vector) {
		if(vector.getX2() == 0) {
			if(vector.getY2() > 0) return 0.5*Math.PI;
			else return 1.5*Math.PI;
		}
		double tan = vector.getY2()/vector.getX2();
		double a = Math.atan(tan);
		if(vector.getX2() > 0 && vector.getY2() >= 0) return a; // 0-90
		else if(vector.getX2() > 0 && vector.getY2() < 0) return a+2.0*Math.PI; // 270-360
		else return a + Math.PI;
	}
	
	public static double angle(Line2D.Double vector1, Line2D.Double vector2) {
		double a1 = angle(vector1);
		double a2 = angle(vector2);
		if(a2 >= a1) {
			return a2-a1;
		}
		else {
			return Math.PI+(a2-a1);
		}
	}
	
	/**
	 * 
	 * @param origo The center of the coordinate system
	 * @param angle1 Angle from x-axis to first vector
	 * @param angle2 Angle from x-axis to second vector
	 * @param m The point
	 * @return
	 */
	public static boolean pointIsWithinBoundries(Point2D.Double origo, double angle1, double angle2, 
			double maxDistanceFromOrigo, double minDistanceFromOrigo, Measurement m) {
		Line2D.Double vectorToM = new Line2D.Double(origo, m.getCoordinates());
		if(Math.toDegrees(angle(vectorToM)) >= angle1 && Math.toDegrees(angle(vectorToM)) <= angle2) {
			if(origo.distance(m.getCoordinates()) <= maxDistanceFromOrigo && origo.distance(m.getCoordinates()) >= minDistanceFromOrigo) {
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	public static Line2D.Double linearRegressionVector(List<Measurement> measurements, int threshold) {
//		Collections.sort(measurements);
		
		List<Measurement> measurementSelection = measurements.subList(0, threshold);
//		System.out.println("List size = "+measurementSelection.size());
		double smallestX = 113.0;
		double largestX = -113.0;
		
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
			
			if(m.getCoordinates().getX() > largestX) largestX = m.getCoordinates().getX();
			if(m.getCoordinates().getX() < smallestX) smallestX = m.getCoordinates().getX();
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
		
		Point2D.Double pointA = new Point2D.Double(smallestX, (m*smallestX)+b);
		
//		Point2D.Double pointB = new Point2D.Double(measurements.get(measurements.size()-1).getCoordinates().getX(),
//				(m*measurements.get(measurements.size()-1).getCoordinates().getX())+b);
		
		Point2D.Double pointB = new Point2D.Double(largestX, (m*largestX)+b);
		
		return new Line2D.Double(pointA, pointB);
	}
	
	public static Line2D.Double longestVector(List<Measurement> measurements, int threshold) {
//		boolean[] used = new boolean[measurements.size()];
//		int nUsed = 0;
		
		Measurement m1 = measurements.get(0);
		Measurement m2 = measurements.get(1);
		double d = m1.getCoordinates().distance(m2.getCoordinates());
		
		int i = 2;
		
		while(threshold-i > 1) {
			Measurement currM1 = measurements.get(i++);
			Measurement currM2 = measurements.get(i++);
			double tempD = currM1.getCoordinates().distance(currM2.getCoordinates());
			if(tempD > d) {
				m1 = currM1;
				m2 = currM2;
				d = tempD;
			}
		}
		return new Line2D.Double(m1.getCoordinates(), m2.getCoordinates());
	}
	
	public static Point2D.Double endPointOfVectorClosestToOrigo(Line2D.Double vector, List<Measurement> measurements, int threshold) {
		double sumP1 = 0.0;
		int countP1 = 0;
		double sumP2 = 0.0;
		int countP2 = 0;
		
		int testMeasurementsCount = 0;
		int i = 0;
		while(testMeasurementsCount < threshold) {
			Measurement currMeasurement = measurements.get(i++);
			
			// TODO trenger bedre objektreferanser for å sjekke for likhet
			if(vector.getP1().equals(currMeasurement.getCoordinates()) || vector.getP2().equals(currMeasurement.getCoordinates())) {
				continue;
			}
			double distToP1 = vector.getP1().distance(currMeasurement.getCoordinates());
			double distToP2 = vector.getP2().distance(currMeasurement.getCoordinates());
			if(distToP1 < distToP2) {
				sumP1 += vector.ptLineDist(currMeasurement.getCoordinates());
				countP1++;
			}
			else if(distToP1 > distToP2) {
				sumP2 += vector.ptLineDist(currMeasurement.getCoordinates());
				countP2++;
			}
			else {
				// TODO very unlikely
			}
			testMeasurementsCount++;
		}
//		int sumCount = countP1+countP2;
//		System.out.println("Threshold="+threshold+" -- countP1+countP2="+sumCount);
		double meanP1 = sumP1/countP1;
		double meanP2 = sumP2/countP2;
		
		if(meanP1 < meanP2) {
			return new Point2D.Double(vector.getP1().getX(), vector.getP1().getY());
		}
		else if(meanP1 > meanP2) {
			return new Point2D.Double(vector.getP2().getX(), vector.getP2().getY());
		}
		else {
			// TODO Ved liten threshold kan vi få at alle punkter er nærmere det ene endepunktet enn det andre
			System.out.println("meanP1="+meanP1);
			System.out.println("meanP2="+meanP2);
			return new Point2D.Double(0.0, 0.0);
		}
	}
	
	public static DynamicCell dynamicCell(Line2D.Double heuristicVector, List<Measurement> measurements, boolean p1IsClosestToOrigo) {
		return null;
	}
	
	
	
	public static void main(String[] args) {
//		double angle = Geom.angle(new Line2D.Double(new Point2D.Double(0,0), new Point2D.Double(0, 10)));
//		System.out.println(angle);
		List<Measurement> measurements = new ArrayList<Measurement>();
		measurements.add(new DefaultMeasurement(new Point2D.Double(1, 2)));
		measurements.add(new DefaultMeasurement(new Point2D.Double(2, 1)));
		measurements.add(new DefaultMeasurement(new Point2D.Double(4, 3)));
		Line2D.Double line = linearRegressionVector(measurements, 3);
		String s = String.format("(%.2f,%.2f) (%.2f,%.2f)", line.getX1(), line.getY1(), line.getX2(), line.getY2());
		System.out.println(s);
	}
	
}
