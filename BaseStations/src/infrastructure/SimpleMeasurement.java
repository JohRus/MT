package infrastructure;

import java.awt.geom.Point2D;

public class SimpleMeasurement extends DefaultMeasurement {
		
	// Signal Strength = sqrt((x2-x1)^2 + (y2-y1)^2) multiplied by -1 to achieve realistic dBm
	private int signalStrength;
	
	
	public SimpleMeasurement(Point2D.Double coordinates, int signalStrength) {
		super(coordinates);
		this.signalStrength = signalStrength;
	}
	
	public int getSignalStrength() {
		return this.signalStrength;
	}
	
	public void setSignalStrength(int signalStrength) {
		this.signalStrength = signalStrength;
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += String.format(", Signalstrength: %d", getSignalStrength());
		return s;
	}
	
//	public static void main(String[] args) {
//		Measurement sm = new SimpleMeasurement(new Point2D.Double(0.0, 0.0));
//		
//		Measurement dm = new DefaultMeasurement(new Point2D.Double(0.0, 0.0));
//		
//		if(sm instanceof SimpleMeasurement) {
//			System.out.println("true");
//			SimpleMeasurement smnew = (SimpleMeasurement) sm;
//			smnew.setSignalStrength(80);
//			System.out.println(smnew.getSignalStrength());
//		}
//		
//	}
}