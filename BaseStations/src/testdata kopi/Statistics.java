package testdata;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Statistics {
	
	private Cell cell;
	
	private Point2D.Double centroidCoordinates;
	private Point2D.Double weightedCentroidCoordinates;
	private Point2D.Double strongestRSSCoordinates;
	
	private Point2D.Double centroidWithThresholdCoordinates;
	private Point2D.Double weightedCentroidWithThresholdCoordinates;
	private Point2D.Double strongestRSSWithThresholdCoordinates;
	
	
	public Statistics(int measurements) {
		this.cell = Cell.generateCellWithRandomMeasurements(measurements);
		this.centroidCoordinates = centroid(this.cell);
		this.weightedCentroidCoordinates = weightedCentroid(this.cell);
		this.strongestRSSCoordinates = strongestRSS(this.cell);
		
		int threshold = 50;
		
		this.centroidWithThresholdCoordinates = centroid(this.cell.applyWeightThreshold(threshold).useMeasurements(Cell.MEASUREMENTS_THRESHOLD));
		this.weightedCentroidWithThresholdCoordinates = weightedCentroid(this.cell);
		this.strongestRSSWithThresholdCoordinates = strongestRSS(this.cell);	
		
		System.out.println(this.toString());
	}

	public static void main(String[] args) {
		
		Statistics statistics = new Statistics(5);

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
	
	
	public Point2D.Double getCentroidWithThresholdCoordinates() {
		return centroidWithThresholdCoordinates;
	}

	public void setCentroidWithThresholdCoordinates(
			Point2D.Double centroidWithThresholdCoordinates) {
		this.centroidWithThresholdCoordinates = centroidWithThresholdCoordinates;
	}

	public Point2D.Double getWeightedCentroidWithThresholdCoordinates() {
		return weightedCentroidWithThresholdCoordinates;
	}

	public void setWeightedCentroidWithThresholdCoordinates(
			Point2D.Double weightedCentroidWithThresholdCoordinates) {
		this.weightedCentroidWithThresholdCoordinates = weightedCentroidWithThresholdCoordinates;
	}

	public Point2D.Double getStrongestRSSWithThresholdCoordinates() {
		return strongestRSSWithThresholdCoordinates;
	}

	public void setStrongestRSSWithThresholdCoordinates(
			Point2D.Double strongestRSSWithThresholdCoordinates) {
		this.strongestRSSWithThresholdCoordinates = strongestRSSWithThresholdCoordinates;
	}

	@Override
	public String toString() {
		String s = "";
		if(this.cell.useMeasurements(Cell.MEASUREMENTS_ALL).getMeasurements().size() <= 5) {
			s += this.cell.useMeasurements(Cell.MEASUREMENTS_ALL).toString();
			s += "\n";
		}
		
		s += "\t\t\tCell Tower Longitude\tCell Tower Latitude\tError\n";
		s += String.format("Real\t\t\t%.1f\t\t\t%.1f\t\t\t%.1f\n", this.cell.getCellTowerCoordinates().getX(), this.cell.getCellTowerCoordinates().getY(), this.cell.getCellTowerCoordinates().distance(this.cell.getCellTowerCoordinates()));
		s += String.format("Centroid\t\t%.1f\t\t\t%.1f\t\t\t%.1f\n", this.centroidCoordinates.getX(), this.centroidCoordinates.getY(), this.cell.getCellTowerCoordinates().distance(this.getCentroidCoordinates()));
		s += String.format("Centroid >= 50\t\t%.1f\t\t\t%.1f\t\t\t%.1f\n",this.centroidWithThresholdCoordinates.getX(), this.centroidWithThresholdCoordinates.getY(), this.cell.getCellTowerCoordinates().distance(this.getCentroidWithThresholdCoordinates()));
		s += String.format("Weighted Centroid\t%.1f\t\t\t%.1f\t\t\t%.1f\n", this.weightedCentroidCoordinates.getX(), this.weightedCentroidCoordinates.getY(), this.cell.getCellTowerCoordinates().distance(this.getWeightedCentroidCoordinates()));
		s += String.format("Weighted Centroid >= 50\t%.1f\t\t\t%.1f\t\t\t%.1f\n",this.weightedCentroidWithThresholdCoordinates.getX(), this.weightedCentroidWithThresholdCoordinates.getY(), this.cell.getCellTowerCoordinates().distance(this.getWeightedCentroidWithThresholdCoordinates()));
		s += String.format("Strongest RSS\t\t%.1f\t\t\t%.1f\t\t\t%.1f\n", strongestRSSCoordinates.getX(), strongestRSSCoordinates.getY(), this.cell.getCellTowerCoordinates().distance(this.getStrongestRSSCoordinates()));
		s += String.format("Strongest RSS >= 50\t%.1f\t\t\t%.1f\t\t\t%.1f\n",this.strongestRSSWithThresholdCoordinates.getX(), this.strongestRSSWithThresholdCoordinates.getY(), this.cell.getCellTowerCoordinates().distance(this.getStrongestRSSWithThresholdCoordinates()));

		return s;
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
	
	private static Point2D.Double centroid(Cell cell) {
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
	
	private static Point2D.Double strongestRSS(Cell cell) {
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
			sumOfLongitudes += measurement.getCoordinates().getX();
			sumOfLatitudes += measurement.getCoordinates().getY();
		}
		double cellTowerLongitude = sumOfLongitudes/strongestWeightMeasurements.size();
		double cellTowerLatitude = sumOfLatitudes/strongestWeightMeasurements.size();
		return new Point2D.Double(cellTowerLongitude, cellTowerLatitude);
	}
}