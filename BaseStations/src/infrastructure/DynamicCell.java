package infrastructure;

import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import logic.Geom;

public class DynamicCell extends DefaultCell {
	
//	// The angle from the x-axis to the virtual vector that forms the first part of the cell sector
//	private double vectorAngle;
	
//	// The angle from the first vector (vectorAngle) to the second vector
//	private double sectorAngle;
	
	// The two vectors forming the edges of the cell sector
	private Line2D.Double vector1, vector2;
	
	// The max and min distance from the cell tower for the measurements
	private double maxDistance, minDistance;

	public DynamicCell(Point2D.Double cellTowerCoordinates, double vectorAngle, double sectorAngle, 
			double maxDistance, double minDistance) {
		super(cellTowerCoordinates, vectorAngle, sectorAngle);
		this.maxDistance = maxDistance;
		this.minDistance = minDistance;
	}

//	public double getVectorAngle() {
//		return vectorAngle;
//	}
//
//	public void setVectorAngle(double vectorAngle) {
//		this.vectorAngle = vectorAngle;
//	}

//	public double getSectorAngle() {
//		return sectorAngle;
//	}
//
//	public void setSectorAngle(double sectorAngle) {
//		this.sectorAngle = sectorAngle;
//	}
	
	public Line2D.Double getVector1() {
		return vector1;
	}

	public void setVector1(Line2D.Double vector1) {
		this.vector1 = vector1;
	}

	public Line2D.Double getVector2() {
		return vector2;
	}

	public void setVector2(Line2D.Double vector2) {
		this.vector2 = vector2;
	}
	
	public void setVectors() {
		this.vector1 = Geom.toCartesian(getVectorAngle(), getMaxDistance(), getCellTowerCoordinates());
		this.vector2 = Geom.toCartesian(getVectorAngle()+getSectorAngle(), getMaxDistance(), getCellTowerCoordinates());
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}

	public double getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(double minDistance) {
		this.minDistance = minDistance;
	}
	
	public void applyDeadZone(Point2D.Double origo, double radius) {
		LinkedList<Measurement> tempList = new LinkedList<>(getMeasurements());
		for(int i = 0; i < tempList.size(); i++) {
			double dist = origo.distance(tempList.get(i).getCoordinates());
			if(dist <= radius) {
				tempList.remove(i);
				i--;
			}
		}
		setMeasurements(new ArrayList<Measurement>(tempList));
//		if(getMeasurements().size() < 40)
//			System.out.println("less then 40 meas left");
	}

	@Override
	public String toString() {
		String s = String.format("Cell Tower Coordinates = (%.1f , %.1f)\n", super.getCellTowerCoordinates().getX(), super.getCellTowerCoordinates().getY());
		s += String.format("Vector Angle = %.1f degrees\nSector Angle = %.1f degrees\n", super.getVectorAngle(), super.getSectorAngle());
		s += String.format("Measurements: %d\n", super.getMeasurements().size());
		if(super.getMeasurements().size() <= 20) {
			for(int i = 0; i < super.getMeasurements().size(); i++) {
				s += String.format("\t");
				s += super.getMeasurements().get(i).toString();
				double angle = Math.toDegrees(Geom.angle(new Line2D.Double(super.getCellTowerCoordinates(), super.getMeasurements().get(i).getCoordinates())));
				s += String.format(" -- Angle = %.1f\n", angle);
			}
		}
		return s;
	}

}
