package logic;

import infrastructure.Computation;
import infrastructure.DynamicCell;
import infrastructure.Measurement;
import infrastructure.SimpleMeasurement;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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

	public static Line2D.Double linearRegressionVector(List<Measurement> measurements, int threshold, Line2D.Double longestVector) {
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
	
	
	public static Line2D.Double longestVector(List<Measurement> measurements, int threshold) {
		
		Random r1 = new Random();
		
		Measurement m1 = null;
		Measurement m2 = null;
		double diff = 0.0;
		
		for(int i = 0; i < threshold; i++) {
//			System.out.println(measurements.size());
			int e1 = r1.nextInt(measurements.size());
			Measurement item1 = measurements.get(e1);
			
			Random r2 = new Random();
			
			for(int j = 0; j < threshold; j++) {
				int e2 = r2.nextInt(measurements.size());
				if(e2 == e1) {
					j--;
					continue;
				}
				Measurement item2 = measurements.get(e2);
				
				if(item1 instanceof SimpleMeasurement && item2 instanceof SimpleMeasurement) {
					double currDiff = Math.abs(item1.getSignalStrength()-item2.getSignalStrength());
					if(currDiff > diff) {
						m1 = item1;
						m2 = item2;
						diff = currDiff;
					}
				}
				else {
//					System.out.println("blæææ");
					double currDiff = item1.getCoordinates().distance(item2.getCoordinates());
					if(currDiff > diff) {
						m1 = item1;
						m2 = item2;
						diff = currDiff;
					}
				}
			}	
		}
		return new Line2D.Double(m1.getCoordinates(), m2.getCoordinates());
	}
	
	public static Line2D.Double longestVectorWithSignalStrength(List<Measurement> measurements, int threshold) {
//		Measurement strongest = new SimpleMeasurement(new Point2D.Double(0.0,0.0), -500);
		int strongestPos = -1;
		HashSet<Integer> items = randomInts(threshold, measurements.size(), null);
		
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
		
		items = randomInts(threshold, measurements.size(), strongestPos);
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
			angleSum += Math.toDegrees(angle(new Line2D.Double(
					measurements.get(strongestPos).getCoordinates(), 
					measurements.get(i).getCoordinates())));
		}
		double longestVectorAngle = angleSum / weakest.size();
		double longestVectorLength = measurements.get(strongestPos).getCoordinates().distance(
				measurements.get(weakest.peekFirst()).getCoordinates());
		System.out.println("funnet lv");
		
		return toCartesian(longestVectorAngle, longestVectorLength, measurements.get(strongestPos).getCoordinates());
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
	

	public static void distanceFromSurroundingPointsToVector(Computation comp, List<Measurement> measurements, int threshold) {
		double sum1 = 0.0;
		int count1 = 0;
		double sum2 = 0.0;
		int count2 = 0;
		
		HashSet<Integer> indices = randomInts(threshold, measurements.size(), null);
//		int testMeasurementsCount = 0;
//		int i = 0;
//		while(testMeasurementsCount < threshold) {
		for(int i :  indices) {
			Measurement curr = measurements.get(i++);

			// TODO trenger bedre objektreferanser for å sjekke for likhet
//			if(vector.getP1().equals(curr.getCoordinates()) || vector.getP2().equals(curr.getCoordinates())) {
//				continue;
//			}
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
//			else {
//				// TODO very unlikely
//			}
//			testMeasurementsCount++;
		}
		//		int sumCount = countP1+countP2;
		//		System.out.println("Threshold="+threshold+" -- countP1+countP2="+sumCount);
		double average1 = sum1/count1;
		double average2 = sum2/count2;

		if(average1 <= average2) {
//			return new Point2D.Double(vector.getP1().getX(), vector.getP1().getY());
			double dist1 = comp.getLongestVector().getP1().distance(comp.getHeuristicDynamicCell1().getCellTowerCoordinates());
			double dist2 = comp.getLongestVector().getP1().distance(comp.getHeuristicDynamicCell2().getCellTowerCoordinates());
			if(dist2 < dist1) {
				DynamicCell temp = comp.getHeuristicDynamicCell1();
				comp.setHeuristicDynamicCell1(comp.getHeuristicDynamicCell2());
				comp.setHeuristicDynamicCell2(temp);
			}
		}
		else {
//			return new Point2D.Double(vector.getP2().getX(), vector.getP2().getY());
			double dist1 = comp.getLongestVector().getP2().distance(comp.getHeuristicDynamicCell1().getCellTowerCoordinates());
			double dist2 = comp.getLongestVector().getP2().distance(comp.getHeuristicDynamicCell2().getCellTowerCoordinates());
			if(dist2 < dist1) {
				DynamicCell temp = comp.getHeuristicDynamicCell1();
				comp.setHeuristicDynamicCell1(comp.getHeuristicDynamicCell2());
				comp.setHeuristicDynamicCell2(temp);
			}
		}
//		else {
//			// TODO Ved liten threshold kan vi få at alle punkter er nærmere det ene endepunktet enn det andre
//			System.out.println("meanP1="+average1);
//			System.out.println("meanP2="+average2);
//			return new Point2D.Double(0.0, 0.0);
//		}
	}

		public static DynamicCell computeSector(Line2D.Double heuristicVector, DynamicCell originalCell) {		
			
			double degreesToRotate = originalCell.getSectorAngle()/2;
			Line2D.Double heuristicSectorVector1 = rotateVector(heuristicVector, degreesToRotate*-1);
			heuristicSectorVector1 = changeVectorLengthByP2(heuristicSectorVector1, originalCell.getMaxDistance());
			Line2D.Double heuristicSectorVector2 = rotateVector(heuristicVector, degreesToRotate);
			heuristicSectorVector2 = changeVectorLengthByP2(heuristicSectorVector2, originalCell.getMaxDistance());
			DynamicCell heuristicCell = new DynamicCell(
					(Point2D.Double) heuristicVector.getP1(), 
					Math.toDegrees(angle(heuristicSectorVector1)), 
					originalCell.getSectorAngle(), 
					originalCell.getMaxDistance(), 
					originalCell.getMinDistance());
			heuristicCell.setVector1(heuristicSectorVector1);
			heuristicCell.setVector2(heuristicSectorVector2);
			return heuristicCell;
		}
		
		public static DynamicCell findSector(Line2D.Double heuristicVector, DynamicCell originalCell, double d) {
			DynamicCell heuristicSector = computeSector(heuristicVector, originalCell);
			List<Measurement> subset = originalCell.getMeasurements();
			Line2D.Double newHeuristicVector = heuristicVector;
//			double distToAdd = 10.0;
			while(!pointsFitInsideSectorAngle(subset, heuristicSector)) {
				// modifisere subset slik at jeg utelukker de målingene jeg vet passer
				
				newHeuristicVector = changeVectorLengthByP1(
						newHeuristicVector, 
						newHeuristicVector.getP1().distance(newHeuristicVector.getP2())+d);
				heuristicSector = computeSector(newHeuristicVector, originalCell);
			}
			return heuristicSector;
		}
		
		public static boolean pointsFitInsideSectorAngle(List<Measurement> measurements, DynamicCell dc) {
			for(Measurement m : measurements) {
				if(!pointIsWithinSectorAngleBoundries(m.getCoordinates(), dc.getCellTowerCoordinates(),
						dc.getVectorAngle(), dc.getVectorAngle()+dc.getSectorAngle())) {

					return false;
				}
			}
			return true;
		}
		
		public static void chooseHeuristicDynamicCell(DynamicCell dc, Computation comp, int threshold) {
			if(dc.getMeasurements().get(0) instanceof SimpleMeasurement) {
				HashSet<Integer> indices = randomInts(threshold, dc.getMeasurements().size(), null);
				int sum1 = 0;
				int sum2 = 0;
				int count1 = 0;
				int count2 = 0;
				for(int i : indices) {
					Measurement curr = dc.getMeasurements().get(i);
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
					double dist1 = comp.getLongestVector().getP1().distance(comp.getHeuristicDynamicCell1().getCellTowerCoordinates());
					double dist2 = comp.getLongestVector().getP1().distance(comp.getHeuristicDynamicCell2().getCellTowerCoordinates());
					if(dist2 < dist1) {
						DynamicCell temp = comp.getHeuristicDynamicCell1();
						comp.setHeuristicDynamicCell1(comp.getHeuristicDynamicCell2());
						comp.setHeuristicDynamicCell2(temp);
					}
				}
				else {
					double dist1 = comp.getLongestVector().getP2().distance(comp.getHeuristicDynamicCell1().getCellTowerCoordinates());
					double dist2 = comp.getLongestVector().getP2().distance(comp.getHeuristicDynamicCell2().getCellTowerCoordinates());
					if(dist2 < dist1) {
						DynamicCell temp = comp.getHeuristicDynamicCell1();
						comp.setHeuristicDynamicCell1(comp.getHeuristicDynamicCell2());
						comp.setHeuristicDynamicCell2(temp);
					}
				}
			}
			else {
				distanceFromSurroundingPointsToVector(comp, dc.getMeasurements(), threshold);
			}
		}


//	public static Line2D.Double moveSectorOrigoBackwards(Line2D.Double heuristicVector, Point2D.Double p, double sectorAngle) {
//		// flytt P1 bakover til p er innenfor vinkelboundries
//		double pvDist = heuristicVector.ptLineDist(p);
//		double pvDistSquared = Math.pow(pvDist, 2);
//		double poDist = heuristicVector.getP1().distance(p);
//		double poDistSquared = Math.pow(poDist, 2);
//
//		double a = pvDist/Math.tan(sectorAngle/2);
//		double b = Math.sqrt(poDistSquared-pvDistSquared);
//
//		double angle = Math.toDegrees(angle(heuristicVector, new Line2D.Double(heuristicVector.getP1(), p)));
//
//		double lengthToMoveBackwards = 0.0;
//
//		if(pvDist == 0.0)
//			lengthToMoveBackwards = poDist;
//		else if(angle > 90)
//			lengthToMoveBackwards = a+b;
//		else if(angle < 90)
//			lengthToMoveBackwards = a-b;
//		else
//			lengthToMoveBackwards = a;
//
//		//		System.out.printf("Length to move backwards=%.2f\n", lengthToMoveBackwards);
//
//		double newLength = heuristicVector.getP1().distance(heuristicVector.getP2())+lengthToMoveBackwards;
//
//		Line2D.Double newHeuristicVector = changeVectorLengthByP1(heuristicVector, newLength);
//
//		return newHeuristicVector;
//	}


	public static void main(String[] args) {
		List<Measurement> m = new ArrayList<Measurement>();
		m.add(new SimpleMeasurement((Point2D.Double)toCartesian(10.0, 100.0, new Point2D.Double(0.0, 0.0)).getP2(), -100));
		m.add(new SimpleMeasurement((Point2D.Double)toCartesian(80.0, 100.0, new Point2D.Double(0.0, 0.0)).getP2(), -100));
		m.add(new SimpleMeasurement((Point2D.Double)toCartesian(20.0, 100.0, new Point2D.Double(0.0, 0.0)).getP2(), -100));
		m.add(new SimpleMeasurement((Point2D.Double)toCartesian(70.0, 100.0, new Point2D.Double(0.0, 0.0)).getP2(), -100));
		m.add(new SimpleMeasurement((Point2D.Double)toCartesian(30.0, 100.0, new Point2D.Double(0.0, 0.0)).getP2(), -100));
		m.add(new SimpleMeasurement((Point2D.Double)toCartesian(60.0, 100.0, new Point2D.Double(0.0, 0.0)).getP2(), -10));

		Line2D.Double v = longestVectorWithSignalStrength(m, 5);
		System.out.println(Math.toDegrees(angle(v)));
	}

}
