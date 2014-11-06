package testdata;

import java.awt.geom.Point2D;
import java.util.Random;

public class Measurement {
//	int cellId;
//	int longitude;
//	int latitude;
	
	Point2D.Double coordinates;
	
	// Signal Strength = sqrt((x2-x1)^2 + (y2-y1)^2) multiplied by -1 to achieve realistic dBm
	int signalStrength;
	
	// Weight = 113 - (-1 * Signal Strength)
	int weight;
	
	public Measurement(double longitude, double latitude) {
//		this.cellId = cellId;
//		this.longitude = longitude;
//		this.latitude = latitude;
		this.coordinates = new Point2D.Double(longitude, latitude);
		this.signalStrength = 99;
		this.weight = -99;
	}

	public Point2D.Double getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Point2D.Double coordinates) {
		this.coordinates = coordinates;
	}

	public int getSignalStrength() {
		return signalStrength;
	}

	public void setSignalStrength(int signalStrength) {
		this.signalStrength = signalStrength;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		String s = String.format("Measurement coordinates: [%.1f,%.1f] - Signal Strength: %d dBm - Weight: %d", 
				this.coordinates.x, this.coordinates.y, this.signalStrength, this.weight);
		return s;
	}

	public static Measurement generateRandomMeasurement(int maxX, int maxY) {
//		Random rand = new Random();
		double x = (double) new Random().nextInt(maxX+1);
		double y = (double) new Random().nextInt(maxY+1);
		
		return new Measurement(x, y);
	}
	
}
