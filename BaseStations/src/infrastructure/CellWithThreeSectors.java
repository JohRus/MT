package infrastructure;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import testdata.Geom;

public class CellWithThreeSectors extends DefaultCell implements Sector {
	
	private Line2D.Double[] vectors;
	
	public CellWithThreeSectors(Point2D.Double cellTowerCoordinates, Line2D.Double aVector, List<Measurement> measurements) {
		super(cellTowerCoordinates, measurements);
		this.vectors = new Line2D.Double[3];
		this.vectors[0] = aVector;
		this.vectors[1] = Geom.rotateVector(this.vectors[0], 120.0);
		this.vectors[2] = Geom.rotateVector(this.vectors[1], 120.0);
	}

//	public void addRandomMeasurements(int measurements, int maxX, int maxY) {
//		
//		for(int i = 0; i < measurements; i++) {
//
//			SimpleMeasurement measurement;
//			double d;
//			do {
//				measurement = SimpleMeasurement.generateRandomMeasurement(maxX, maxY);
//				d = this.cellTowerCoordinates.distance(measurement.getCoordinates());
//				
//			} while(d > 113.0);
//			
//			int dBm = Surface.doubleToInt(d)*-1;
//			measurement.setSignalStrength(dBm);
//			
//			int weight = 113 - (-1*measurement.getSignalStrength());
//			measurement.setWeight(weight);
//		
//			this.measurementsAll.add(measurement);
//			
//			this.useMeasurements(MEASUREMENTS_ALL);
//		}
//		return;
//	}
	
//	protected CellWithThreeSectors applyWeightThreshold(int minimumWeight) {
//		this.measurementsWithThreshold.clear();
//		for(SimpleMeasurement measurement : this.measurementsAll) {
//			if(measurement.getWeight() >= minimumWeight) {
//				this.measurementsWithThreshold.add(measurement);
//			}
//		}
//		return this;
//	}
	
//	protected CellWithThreeSectors useMeasurements(String measurementsList) {
//		if(measurementsList.equals(MEASUREMENTS_ALL)) {
//			this.measurements = this.measurementsAll;
//		}
//		else if(measurementsList.equals(MEASUREMENTS_THRESHOLD)) {
//			this.measurements = this.measurementsWithThreshold;
//		}
//		else {
//			throw new IllegalArgumentException("There is no such list");
//		}
//		return this;
//	}
	
	@Override
	public String toString() {
		double[] vectorAngles = vectorAngles();
		String s = String.format("Cell Tower Coordinates: [%.1f,%.1f] - Vector Angles: (%.1f,%.1f,%.1f)\n\tMeasurements: %d", 
				super.getCellTowerCoordinates().getX(), super.getCellTowerCoordinates().getY(),
				vectorAngles[0], vectorAngles[1], vectorAngles[2], super.getMeasurements().size());
		for(Measurement measurement : super.getMeasurements()) {
			s += "\n\t"+measurement.toString();
		}
		s += "\n";
		return s;
	}

	@Override
	public double[] vectorAngles() {
		double[] angles = new double[this.vectors.length];
		for(int i = 0; i < this.vectors.length; i++) {
			angles[i] = Geom.angle(this.vectors[i]);
		}
		return angles;
	}

	@Override
	public Line2D.Double[] getVectors() {
		return this.vectors;
	}

	@Override
	public void setVectors(Line2D.Double[] vectors) {
		this.vectors = vectors;
	}

	@Override
	public boolean measurementIsWithin(Double point) {
		// TODO Auto-generated method stub
		return false;
	}

//	public static CellWithThreeSectors generateCellWithRandomMeasurements(int measurements) {
//		
//		CellWithThreeSectors cell = new CellWithThreeSectors();
//		
//		if(measurements > 0) {
//			cell.addRandomMeasurements(measurements, MEASUREMENT_X_Y_LIMIT, MEASUREMENT_X_Y_LIMIT);
//		}
//				
//		return cell;
//	}
}