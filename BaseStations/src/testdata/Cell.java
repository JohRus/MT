package testdata;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cell {
	
	static final int CELL_X_Y_VALUE = 113;
	static final int MEASUREMENT_X_Y_LIMIT = 226;
	
//	int cellId;
	Point2D.Double cellTowerCoordinates;
//	int cellTowerLongitude;
//	int cellTowerLatitude;
	
	List<Measurement> measurements;
	
	public Cell() {
//		this.cellId = cellId;
//		this.cellTowerLongitude = CELL_X_Y_VALUE;
//		this.cellTowerLatitude = CELL_X_Y_VALUE;
		this.cellTowerCoordinates = new Point2D.Double((double) CELL_X_Y_VALUE, (double) CELL_X_Y_VALUE);
		this.measurements = new ArrayList<Measurement>();
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
				d = this.cellTowerCoordinates.distance(measurement.coordinates);
				
			} while(d > 113.0);
			
			int dBm = -1*((int) d);
			measurement.setSignalStrength(dBm);
			
			int weight = 113 - (-1*measurement.getSignalStrength());
			measurement.setWeight(weight);
		
			this.measurements.add(measurement);
		}

		return;

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
//		Random rand = new Random();
//		int x = new Random().nextInt(maxX+1);
//		int y = new Random().nextInt(maxY+1);
		
		Cell cellTower = new Cell();
		
		if(measurements > 0) {
			cellTower.addRandomMeasurements(measurements, MEASUREMENT_X_Y_LIMIT, MEASUREMENT_X_Y_LIMIT);
		}
		
		return cellTower;
	}
}
