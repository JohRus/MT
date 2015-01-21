package infrastructure;

import java.awt.geom.Point2D;

public class DefaultMeasurement implements Measurement {
	
	private Point2D.Double coordinates;
	
	public DefaultMeasurement(Point2D.Double coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public Point2D.Double getCoordinates() {
		return this.coordinates;
	}

	@Override
	public void setCoordinates(Point2D.Double coordinates) {
		this.coordinates = coordinates;
	}
	
	@Override
	public String toString() {
		String s = String.format("Coordinates: (%.2f , %.2f)", 
				this.coordinates.getX(), this.coordinates.getY());
		return s;
	}

	@Override
	public int compareTo(Measurement o) {
		if(this.getCoordinates().getX() < o.getCoordinates().getX()) {return -1;}
		else if(this.getCoordinates().getX() > o.getCoordinates().getX()) {return 1;}
		else {
			if(this.getCoordinates().getY() < o.getCoordinates().getY()) {return -1;}
			else if(this.getCoordinates().getY() > o.getCoordinates().getY()) {return 1;}
			else return 0;
		}
	}
	


}
