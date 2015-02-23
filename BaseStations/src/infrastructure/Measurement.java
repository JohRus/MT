package infrastructure;

import java.awt.geom.Point2D;


public interface Measurement extends Comparable<Measurement> {
	
	public Point2D.Double getCoordinates();

	public void setCoordinates(Point2D.Double coordinates);
	
	@Override
	public String toString();
	
	@Override
	public int compareTo(Measurement o);
	
	public int getSignalStrength();
	
	public void setSignalStrength(int signalStrength);
	
}
