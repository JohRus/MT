package infrastructure;

import java.awt.geom.Point2D;
import java.util.List;

public interface Cell {
	
	public Point2D.Double getCellTowerCoordinates();

	public void setCellTowerCoordinates(Point2D.Double cellTowerCoordinates);

	public List<Measurement> getMeasurements();

	public void setMeasurements(List<Measurement> measurements);
	
	public void addMeasurement(Measurement measurement);
	
//	public boolean measurementIsWithin(Point2D.Double point);
		
	@Override
	public String toString();
}
