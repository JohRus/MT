package logic;

import infrastructure.DynamicCell;
import infrastructure.Measurement;
import infrastructure.SimpleMeasurement;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Geom {

	public static Line2D.Double rotateVector(Line2D.Double toBeRotated, double degrees) {
		//		Point2D.Double adjustedP1 = new Point2D.Double(toBeRotated.getX1()-toBeRotated.getX1(), toBeRotated.getY1()-toBeRotated.getY1());
		Point2D.Double adjustedP2 = new Point2D.Double(toBeRotated.getX2()-toBeRotated.getX1(), toBeRotated.getY2()-toBeRotated.getY1());
		double radians = Math.toRadians(degrees);
		double x = (adjustedP2.getX()*Math.cos(radians))-(adjustedP2.getY()*Math.sin(radians));
		double y = (adjustedP2.getX()*Math.sin(radians))+(adjustedP2.getY()*Math.cos(radians));
		return new Line2D.Double(toBeRotated.getP1(), new Point2D.Double(x+toBeRotated.getX1(), y+toBeRotated.getY1()));
	}

	public static double angle(Line2D.Double vector) {
		Point2D.Double adjustedP2 = new Point2D.Double(vector.getX2()-vector.getX1(), vector.getY2()-vector.getY1());
		if(adjustedP2.getX() == 0.0) {
			if(adjustedP2.getY() > 0.0) return 0.5*Math.PI;
			else return 1.5*Math.PI;
		}
		double tan = adjustedP2.getY()/adjustedP2.getX();
		double a = Math.atan(tan);
		if(adjustedP2.getX() > 0.0 && adjustedP2.getY() >= 0.0) return a; // 0-90
		else if(adjustedP2.getX() > 0.0 && adjustedP2.getY() < 0.0) return a+2.0*Math.PI; // 270-360
		else return a + Math.PI;
	}

	public static double angle(Line2D.Double vector1, Line2D.Double vector2) {
		double a1 = angle(vector1);
		double a2 = angle(vector2);
		if(a2 >= a1) {
			return a2-a1;
		}
		else {
			// returnerer vinkelen fra a1 og helt rundt til a2
			// eks: hvis a1 = 60 grader og a2 = 10 grader blir 310 grader returnert
			//			return 2*Math.PI+a2-a1;
			return a1-a2;
		}
	}

	/**
	 * 
	 * @param t The angle in degrees
	 * @param r The radius/weight
	 * @return A vector in cartesian coordinates
	 */
	public static Line2D.Double toCartesian(double t, double r, Point2D.Double relativOrigo) {
		double x = r*Math.cos(Math.toRadians(t));
		double y = r*Math.sin(Math.toRadians(t));
		return new Line2D.Double(relativOrigo, new Point2D.Double(relativOrigo.getX()+x, relativOrigo.getY()+y));
	}

	/**
	 * 
	 * @param origo The center of the sector
	 * @param angle1 Angle from x-axis to first vector
	 * @param angle2 Angle from x-axis to second vector
	 * @param p The point
	 * @return
	 */
	public static boolean pointIsWithinBoundries(Point2D.Double p, Point2D.Double origo, double angle1, 
			double angle2, double maxDistanceFromOrigo, double minDistanceFromOrigo) {

		if(pointIsWithinSectorAngleBoundries(p, origo, angle1, angle2) && 
				pointIsWithinSectorDistanceBoundries(p, origo, maxDistanceFromOrigo, minDistanceFromOrigo)) {
			return true;
		}
		else
			return false;
	}

	public static boolean pointIsWithinSectorAngleBoundries(Point2D.Double p, Point2D.Double origo, 
			double angle1, double angle2) {
		Line2D.Double vectorToP = new Line2D.Double(origo, p);
		double vectorToPAngle = Math.toDegrees(angle(vectorToP));
		if(angle2 > 360 && vectorToPAngle >= 0.0 && vectorToPAngle <= (angle2-360)) {
			vectorToPAngle += 360;
		}
		if(vectorToPAngle >= angle1 && vectorToPAngle <= angle2) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean pointsFitInsideSectorAngle(List<Measurement> measurements, Point2D.Double origo,
			double angle1, double angle2) {
		for(Measurement m : measurements) {
			if(!pointIsWithinSectorAngleBoundries(m.getCoordinates(), origo, angle1, angle2)) {
				return false;
			}
		}
		return true;
	}
	
//	public static boolean pointsFitInsideSectorAngle(List<Measurement> measurements, DynamicCell dc) {
//		for(Measurement m : measurements) {
//			if(!pointIsWithinSectorAngleBoundries(m.getCoordinates(), dc.getCellTowerCoordinates(),
//					dc.getVectorAngle(), dc.getVectorAngle()+dc.getSectorAngle())) {
//
//				return false;
//			}
//		}
//		return true;
//	}

	public static boolean pointIsWithinSectorDistanceBoundries(Point2D.Double p, Point2D.Double origo,
			double maxDistanceFromOrigo, double minDistanceFromOrigo) {
		if(origo.distance(p) <= maxDistanceFromOrigo && origo.distance(p) >= minDistanceFromOrigo) {
			return true;
		}
		else
			return false;
	}

	public static Line2D.Double changeVectorLengthByP2(Line2D.Double vector, double newLength) {
		double oldLength = vector.getP1().distance(vector.getP2());
		if(oldLength == newLength) return vector;

		double newOldLengthRatio = newLength/oldLength;

		double newX2 = (newOldLengthRatio*(vector.getX2()-vector.getX1()))+vector.getX1();
		double newY2 = (newOldLengthRatio*(vector.getY2()-vector.getY1()))+vector.getY1();

		return new Line2D.Double(vector.getP1(), new Point2D.Double(newX2, newY2));
	}

	public static Line2D.Double changeVectorLengthByP1(Line2D.Double vector, double newLength) {
		Line2D.Double tempVector = new Line2D.Double(vector.getP2(), vector.getP1());

		tempVector = changeVectorLengthByP2(tempVector, newLength);

		return new Line2D.Double(tempVector.getP2(), tempVector.getP1());
	}

	

	public static double sphericalDistance(double lon1, double lat1, double lon2, double lat2) {

		double earthRadius = 6.371;

		//haversin lat2-lat1
		double hsLat = Math.pow(Math.sin((lat2-lat1)/2), 2);

		//haversin lon2-lon1
		double hsLon = Math.pow(Math.sin((lon2-lon1)/2), 2);

		double a = Math.sqrt(hsLat+(Math.cos(lat1)*Math.cos(lat2)*hsLon));

		double d = 2*earthRadius*Math.asin(a);

		return d;
	}
}
