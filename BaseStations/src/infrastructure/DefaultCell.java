package infrastructure;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class DefaultCell implements Cell {
	
	private Point2D.Double cellTowerCoordinates;
	
	private List<Measurement> measurements;
	
	// The angle from the x-axis to the virtual vector that forms the first part of the cell sector
	private double vectorAngle;
	
	// The angle from te first vector (vectorAngle) to the second vector
	private double sectorAngle;
	
	public DefaultCell(Point2D.Double cellTowerCoordinates, List<Measurement> measurements, double vectorAngle, double sectorAngle) {
		this.cellTowerCoordinates = cellTowerCoordinates;
		this.measurements = measurements;
		this.vectorAngle = vectorAngle;
		this.sectorAngle = sectorAngle;
	}
	
	public DefaultCell(Point2D.Double cellTowerCoordinates, double vectorAngle, double sectorAngle) {
		this.cellTowerCoordinates = cellTowerCoordinates;
		this.measurements = new ArrayList<Measurement>();
		this.vectorAngle = vectorAngle;
		this.sectorAngle = sectorAngle;
	}
	
	public DefaultCell(Point2D.Double cellTowerCoordinates, double sectorAngle) {
		this.cellTowerCoordinates = cellTowerCoordinates;
		this.measurements = new ArrayList<Measurement>();
		this.sectorAngle = sectorAngle;
		this.vectorAngle = 0.0;
	}
	
	@Override
	public Point2D.Double getCellTowerCoordinates() {
		return this.cellTowerCoordinates;
	}

	@Override
	public void setCellTowerCoordinates(Point2D.Double cellTowerCoordinates) {
		this.cellTowerCoordinates = cellTowerCoordinates;
	}

	@Override
	public List<Measurement> getMeasurements() {
		return this.measurements;
	}

	@Override
	public void setMeasurements(List<Measurement> measurements) {
		this.measurements = measurements;
	}

	@Override
	public void addMeasurement(Measurement measurement) {
		this.measurements.add(measurement);
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
	
}
