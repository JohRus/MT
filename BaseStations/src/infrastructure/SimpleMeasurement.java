package infrastructure;

import java.awt.geom.Point2D;

public class SimpleMeasurement extends DefaultMeasurement {
		
	// Signal Strength = sqrt((x2-x1)^2 + (y2-y1)^2) multiplied by -1 to achieve realistic dBm
	private int signalStrength;
	
	
	public SimpleMeasurement(Point2D.Double coordinates) {
		super(coordinates);
		this.signalStrength = 99;
	}
	
	public int getSignalStrength() {
		return this.signalStrength;
	}
	
	public void setSignalStrength(int signalStrength) {
		this.signalStrength = signalStrength;
	}

//	public int getWeight() {
//		return weight;
//	}

//	public void setWeight(int weight) {
//		this.weight = weight;
//	}

	@Override
	public String toString() {
		String s = String.format("Coordinates: [%.1f,%.1f] - Signal Strength: %d dBm", 
				super.getCoordinates().getX(), super.getCoordinates().getY(), this.signalStrength);
		return s;
	}

//	public static Measurement generateRandomMeasurement(int maxX, int maxY) {
//		double x = (double) new Random().nextInt(maxX+1);
//		double y = (double) new Random().nextInt(maxY+1);
//		
//		return new Measurement(x, y);
//	}
}