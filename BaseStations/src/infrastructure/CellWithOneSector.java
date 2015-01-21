package infrastructure;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.List;

public class CellWithOneSector extends DefaultCell {

	public CellWithOneSector(Double cellTowerCoordinates, List<Measurement> measurements) {
		super(cellTowerCoordinates, measurements);
	}

	@Override
	public boolean measurementIsWithin(Double point) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
