package infrastructure;

import java.awt.geom.Line2D;

public interface Sector {
	
	public Line2D.Double[] getVectors();
	
	public void setVectors(Line2D.Double[] vectors);
	
	public double[] vectorAngles();
}
