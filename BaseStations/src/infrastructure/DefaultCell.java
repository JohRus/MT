package infrastructure;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public abstract class DefaultCell implements Cell {
	
	private Point2D.Double cellTowerCoordinates;
	
	private List<Measurement> measurements;
	
	public DefaultCell(Point2D.Double cellTowerCoordinates, List<Measurement> measurements) {
		this.cellTowerCoordinates = cellTowerCoordinates;
		this.measurements = measurements;
	}
	
	public DefaultCell(Point2D.Double cellTowerCoordinates) {
		this.cellTowerCoordinates = cellTowerCoordinates;
		this.measurements = new ArrayList<Measurement>();
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
	
	
}
