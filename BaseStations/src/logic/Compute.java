package logic;

import infrastructure.Cell;
import infrastructure.Measurement;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Compute {
	
	public static Point2D.Double weightedCentroid(Cell cell) {
		
		HashMap<Measurement, Integer> weights = calculateWeights(cell);
		
		int sumOfWeights = 0;
		for(int weight : weights.values()) {
			sumOfWeights += weight;
		}
				
		double ctX = 0.0;
		double ctY = 0.0;
		for(Entry<Measurement, Integer> entry : weights.entrySet()) {
			ctX += entry.getKey().getCoordinates().getX()*entry.getValue();
			ctY += entry.getKey().getCoordinates().getY()*entry.getValue();
		}
		ctX = ctX/sumOfWeights;
		ctY = ctY/sumOfWeights;
		
		return new Point2D.Double(ctX, ctY);
	}
	
	public static Point2D.Double centroid(Cell cell) {
		double ctX = 0.0;
		double ctY = 0.0;
		for(Measurement measurement : cell.getMeasurements()) {
			ctX += measurement.getCoordinates().getX();
			ctY += measurement.getCoordinates().getY();
		}
		ctX = ctX/cell.getMeasurements().size();
		ctY = ctY/cell.getMeasurements().size();
		
		return new Point2D.Double(ctX, ctY);
	}
	
	public static Point2D.Double strongestRSS(Cell cell) {
		int currentStrongestRSS = -114;
		List<Measurement> strongestRSSMeasurements = new ArrayList<Measurement>();
		for(Measurement measurement : cell.getMeasurements()) {
			if(measurement.getSignalStrength() > currentStrongestRSS) {
				strongestRSSMeasurements.clear();
				strongestRSSMeasurements.add(measurement);
				currentStrongestRSS = measurement.getSignalStrength();
			}
			else if(measurement.getSignalStrength() == currentStrongestRSS) {
				strongestRSSMeasurements.add(measurement);
			}
		}
//		Cell tempCell = new OneSector(cell.getCellTowerCoordinates(), cell.getVectors()[0].getP2(), strongestRSSMeasurements);
		double ctX = 0.0;
		double ctY = 0.0;
		for(Measurement measurement : strongestRSSMeasurements) {
			ctX += measurement.getCoordinates().getX();
			ctY += measurement.getCoordinates().getY();
		}
		ctX = ctX/strongestRSSMeasurements.size();
		ctY = ctY/strongestRSSMeasurements.size();
		
		return new Point2D.Double(ctX, ctY);
	}
	
	private static HashMap<Measurement, Integer> calculateWeights(Cell cell) {
		HashMap<Measurement, Integer> weights = new HashMap<Measurement, Integer>();
		for(Measurement measurement : cell.getMeasurements()) {
			int weight = toWeight(measurement.getSignalStrength());
			weights.put(measurement, weight);
		}
		return weights;
	}
	
	// Weight = 113 - (-1 * Signal Strength)
	private static int toWeight(int signalStrength) {
		return 113 - (-1 * signalStrength);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}