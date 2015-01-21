package testdata;

import java.awt.geom.Point2D;

public class Computation {
	public static final String CENTROID = "Centroid";
	public static final String WEIGHTED_CENTROID = "Weighted Centroid";
	public static final String STRONGEST_RSS = "Strongest RSS";
	
	private boolean centroid = false;
	private boolean weightedCentroid = false;
	private boolean strongestRSS = false;
	
	private int threshold;
	private boolean towerBasedRegrouping;
	
	private Point2D.Double cellTowerCoordinates;
	
	/**
	 * 
	 * @param algorithm The algorithm to be used
	 * @param threshold The threshold to be used. If no threshold, then input 99.
	 * @param towerBasedRegrouping True if tower-based regrouping should be applied.
	 */
	public Computation(String algorithm, int threshold, boolean towerBasedRegrouping) {
		if(algorithm.equals(CENTROID)) this.centroid = true;
		else if(algorithm.equals(WEIGHTED_CENTROID)) this.weightedCentroid = true;
		else if(algorithm.equals(STRONGEST_RSS)) this.strongestRSS = true;
		else System.err.println("No valid algorithm chosen.");
	}
}
