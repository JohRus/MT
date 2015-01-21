package infrastructure;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D.Double;

import testdata.Geom;

public class DynamicCell extends DefaultCell {
	
	// The angle from the x-axis to the virtual vector that forms the first part of the cell sector
	private double vectorAngle;
	
	// The angle from the first vector (vectorAngle) to the second vector
	private double sectorAngle;

	public DynamicCell(Double cellTowerCoordinates, double vectorAngle, double sectorAngle) {
		super(cellTowerCoordinates);
		this.vectorAngle = vectorAngle;
		this.sectorAngle = sectorAngle;	
	}
	
	

	public double getVectorAngle() {
		return vectorAngle;
	}

	public void setVectorAngle(double vectorAngle) {
		this.vectorAngle = vectorAngle;
	}

	public double getSectorAngle() {
		return sectorAngle;
	}

	public void setSectorAngle(double sectorAngle) {
		this.sectorAngle = sectorAngle;
	}
	
	@Override
	public String toString() {
		String s = String.format("Cell Tower Coordinates = (%.1f , %.1f)\n", super.getCellTowerCoordinates().getX(), super.getCellTowerCoordinates().getY());
		s += String.format("Vector Angle = %.1f degrees\nSector Angle = %.1f degrees\n", this.vectorAngle, this.sectorAngle);
		s += String.format("Measurements: %d\n", super.getMeasurements().size());
		if(super.getMeasurements().size() <= 10) {
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
