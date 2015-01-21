package testdata;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Cell {
	
	protected static final String MEASUREMENTS_ALL = "all measurements";
	protected static final String MEASUREMENTS_THRESHOLD = "measurements filtered";
	
	protected static final int CELL_X_Y_VALUE = 113;
	protected static final int MEASUREMENT_X_Y_LIMIT = 226;
	
	private Point2D.Double cellTowerCoordinates;
	
	// Pointer
	private List<Measurement> measurements;
	
	// List for storing all measurements
	private List<Measurement> measurementsAll;
	
	// List for storing measurements above a certain weight threshold
	private List<Measurement> measurementsWithThreshold;
	
	public Cell() {
		this.cellTowerCoordinates = new Point2D.Double((double) CELL_X_Y_VALUE, (double) CELL_X_Y_VALUE);
		this.measurementsAll = new ArrayList<Measurement>();
		this.measurementsWithThreshold = new ArrayList<Measurement>();
	}

	public Point2D.Double getCellTowerCoordinates() {
		return cellTowerCoordinates;
	}

	public void setCellTowerCoordinates(Point2D.Double cellTowerCoordinates) {
		this.cellTowerCoordinates = cellTowerCoordinates;
	}

	public List<Measurement> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(List<Measurement> measurements) {
		this.measurements = measurements;
	}

	public void addRandomMeasurements(int measurements, int maxX, int maxY) {
		
		for(int i = 0; i < measurements; i++) {

			Measurement measurement;
			double d;
			do {
				measurement = Measurement.generateRandomMeasurement(maxX, maxY);
				d = this.cellTowerCoordinates.distance(measurement.getCoordinates());
				
			} while(d > 113.0);
			
			int dBm = Surface.doubleToInt(d)*-1;
			measurement.setSignalStrength(dBm);
			
			int weight = 113 - (-1*measurement.getSignalStrength());
			measurement.setWeight(weight);
		
			this.measurementsAll.add(measurement);
			
			this.useMeasurements(MEASUREMENTS_ALL);
		}
		return;
	}
	
	protected Cell applyWeightThreshold(int minimumWeight) {
		this.measurementsWithThreshold.clear();
		for(Measurement measurement : this.measurementsAll) {
			if(measurement.getWeight() >= minimumWeight) {
				this.measurementsWithThreshold.add(measurement);
			}
		}
		return this;
	}
	
	protected Cell useMeasurements(String measurementsList) {
		if(measurementsList.equals(MEASUREMENTS_ALL)) {
			this.measurements = this.measurementsAll;
		}
		else if(measurementsList.equals(MEASUREMENTS_THRESHOLD)) {
			this.measurements = this.measurementsWithThreshold;
		}
		else {
			throw new IllegalArgumentException("There is no such list");
		}
		return this;
	}
	
	@Override
	public String toString() {
		String s = String.format("Cell Tower Coordinates: [%.1f,%.1f]\n\tMeasurements: %d", 
				this.cellTowerCoordinates.x, this.cellTowerCoordinates.y, this.measurements.size());
		for(Measurement measurement : this.measurements) {
			s += "\n\t"+measurement.toString();
		}
		s += "\n";
		return s;
	}

	public static Cell generateCellWithRandomMeasurements(int measurements) {
		
		Cell cell = new Cell();
		
		if(measurements > 0) {
			cell.addRandomMeasurements(measurements, MEASUREMENT_X_Y_LIMIT, MEASUREMENT_X_Y_LIMIT);
		}
				
		return cell;
	}
}