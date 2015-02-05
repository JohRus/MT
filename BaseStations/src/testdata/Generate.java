package testdata;

import infrastructure.Cell;
import infrastructure.DefaultMeasurement;
import infrastructure.DynamicCell;
import infrastructure.Measurement;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generate {
	
	/**
	 * 
	 * @param angle1 The angle to the first vector from the x-axis
	 * @param angle2 The angle to the second vector from the x-axis
	 * @param maxDistanceFromCT Max distance from the cell tower
	 * @param minDistanceFromCT Min distance from the cell tower
	 * @return An instance of DefaultMeasurement
	 */
//	public static Measurement defaultMeasurement(double angle1, double angle2, double maxDistanceFromCT, double minDistanceFromCT) {
//		double randomAngle = angle1 + ((angle2 - angle1)*new Random().nextDouble());
//		double randomDistance = minDistanceFromCT + ((maxDistanceFromCT - minDistanceFromCT)*new Random().nextDouble());
//		double x = randomDistance*Math.cos(Math.toRadians(randomAngle));
//		double y = randomDistance*Math.sin(Math.toRadians(randomAngle));
//		return new DefaultMeasurement(new Point2D.Double(x, y));
//	}
	
	public static Measurement defaultMeasurement(DynamicCell dc) {
		double randomAngle = dc.getVectorAngle() + (dc.getSectorAngle()*new Random().nextDouble());
		double randomDistance = dc.getMinDistance() + ((dc.getMaxDistance()-dc.getMinDistance())*new Random().nextDouble());
		double x = randomDistance*Math.cos(Math.toRadians(randomAngle));
		double y = randomDistance*Math.sin(Math.toRadians(randomAngle));
		return new DefaultMeasurement(new Point2D.Double(dc.getCellTowerCoordinates().getX()+x, dc.getCellTowerCoordinates().getY()+y));
	}
	
	public static List<Measurement> defaultMeasurements(int measurements, DynamicCell dc) {
		List<Measurement> list = new ArrayList<Measurement>();
		for(int i = 0; i < measurements; i++) {
			list.add(defaultMeasurement(dc));
		}
		return list;
	}
	
	public static DynamicCell dynamicCellWithDefaultMeasurements(
			Point2D.Double cellTowerCoordinates, 
			double vectorAngle, 
			double sectorAngle,
			double measurementMaxDistanceFromCellTower,
			double measurementMinDistanceFromCellTower,
			int measurements) {
		
		DynamicCell dynamicCell = new DynamicCell(cellTowerCoordinates, vectorAngle, sectorAngle, 
				measurementMaxDistanceFromCellTower, measurementMinDistanceFromCellTower);
		dynamicCell.setVectors();
		
		dynamicCell.setMeasurements(defaultMeasurements(measurements, dynamicCell));

		return dynamicCell;
	}

	

}
