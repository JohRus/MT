package testdata;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


public class Statistics {
	
	Cell cell;
	Point2D.Double centroidCoordinates;
	Point2D.Double weightedCentroidCoordinates;
	Point2D.Double strongestRSSCoordinates;
	
	public Statistics(int measurements) {
		this.cell = Cell.generateCellWithRandomMeasurements(measurements);
		this.centroidCoordinates = centroid(this.cell);
		this.weightedCentroidCoordinates = weightedCentroid(this.cell);
		this.strongestRSSCoordinates = strongestRSS(this.cell);
		
		if(measurements <= 5)
			System.out.println(this.cell.toString());
		printResults(this.cell, this.centroidCoordinates, this.weightedCentroidCoordinates, 
				this.strongestRSSCoordinates);
	}

	public static void main(String[] args) {
		
		Statistics statistics = new Statistics(5);
		
//		Cell cell = Cell.generateCellWithRandomMeasurements(measurements);
//		System.out.println(cell.toString());
//		
//		Point2D.Double weightedCentroidCoordinates = weightedCentroid(cell);
//		Point2D.Double centroidCoordinates = centroid(cell);
//		Point2D.Double strongestRSSCoordinates = strongestRSS(cell);
//		
//		printResults(cell, centroidCoordinates, weightedCentroidCoordinates, strongestRSSCoordinates);
		

		System.out.println("\nEND OF PROGRAM");
	}
	
	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public Point2D.Double getCentroidCoordinates() {
		return centroidCoordinates;
	}

	public void setCentroidCoordinates(Point2D.Double centroidCoordinates) {
		this.centroidCoordinates = centroidCoordinates;
	}

	public Point2D.Double getWeightedCentroidCoordinates() {
		return weightedCentroidCoordinates;
	}

	public void setWeightedCentroidCoordinates(
			Point2D.Double weightedCentroidCoordinates) {
		this.weightedCentroidCoordinates = weightedCentroidCoordinates;
	}

	public Point2D.Double getStrongestRSSCoordinates() {
		return strongestRSSCoordinates;
	}

	public void setStrongestRSSCoordinates(Point2D.Double strongestRSSCoordinates) {
		this.strongestRSSCoordinates = strongestRSSCoordinates;
	}

	private static void printResults(Cell cell, Point2D.Double centroidCoordinates, 
			Point2D.Double weightedCentroidCoordinates, Point2D.Double strongestRSSCoordinates) {
		
		System.out.println("\t\t\tCell Tower Longitude\tCell Tower Latitude");
		System.out.printf("Real\t\t\t%.1f\t\t\t%.1f\n", cell.getCellTowerCoordinates().getX(), cell.getCellTowerCoordinates().getY());
		System.out.printf("Centroid\t\t%.1f\t\t\t%.1f\n", centroidCoordinates.getX(), centroidCoordinates.getY());
		System.out.printf("Weighted Centroid\t%.1f\t\t\t%.1f\n", weightedCentroidCoordinates.getX(), weightedCentroidCoordinates.getY());
		System.out.printf("Strongest RSS\t\t%.1f\t\t\t%.1f\n", strongestRSSCoordinates.getX(), strongestRSSCoordinates.getY());
	
		System.out.println();
	}
	
	private static Point2D.Double weightedCentroid(Cell cell) {
		
		int sumOfWeights = 0;
		for(Measurement measurement : cell.getMeasurements()) {
			sumOfWeights += measurement.getWeight();
		}
				
		double cellTowerLongitude = 0.0;
		double cellTowerLatitude = 0.0;
		for(Measurement measurement : cell.getMeasurements()) {
			double weightRatio = measurement.getWeight()/(double)sumOfWeights;

			cellTowerLongitude += (measurement.getCoordinates().getX()*weightRatio);
			cellTowerLatitude += (measurement.getCoordinates().getY()*weightRatio);
		}
		return new Point2D.Double(cellTowerLongitude, cellTowerLatitude);
	}
	
	public static Point2D.Double centroid(Cell cell) {
		double sumOfLongitudes = 0;
		double sumOfLatitudes = 0;
		for(Measurement measurement : cell.getMeasurements()) {
			sumOfLongitudes += measurement.getCoordinates().getX();
			sumOfLatitudes += measurement.getCoordinates().getY();
		}
		double cellTowerLongitude = sumOfLongitudes/cell.getMeasurements().size();
		double cellTowerLatitude = sumOfLatitudes/cell.getMeasurements().size();
		
		return new Point2D.Double(cellTowerLongitude, cellTowerLatitude);
	}
	
	public static Point2D.Double strongestRSS(Cell cell) {
		int currentStrongestWeight = -1;
		List<Measurement> strongestWeightMeasurements = new ArrayList<Measurement>();
		for(Measurement measurement : cell.getMeasurements()) {
			if(measurement.getWeight() > currentStrongestWeight) {
				strongestWeightMeasurements.clear();
				strongestWeightMeasurements.add(measurement);
				currentStrongestWeight = measurement.getWeight();
			}
			else if(measurement.getWeight() == currentStrongestWeight) {
				strongestWeightMeasurements.add(measurement);
			}
		}
		double sumOfLongitudes = 0.0;
		double sumOfLatitudes = 0.0;
		for(Measurement measurement : strongestWeightMeasurements) {
//			System.out.println(measurement.getCoordinates().getX()+","+measurement.getCoordinates().getY());
			sumOfLongitudes += measurement.getCoordinates().getX();
			sumOfLatitudes += measurement.getCoordinates().getY();
		}
		double cellTowerLongitude = sumOfLongitudes/strongestWeightMeasurements.size();
		double cellTowerLatitude = sumOfLatitudes/strongestWeightMeasurements.size();
		return new Point2D.Double(cellTowerLongitude, cellTowerLatitude);
	}

}
