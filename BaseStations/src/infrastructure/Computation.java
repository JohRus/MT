package infrastructure;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Computation {
	
	private Line2D.Double longestVector;
//	private Line2D.Double leastSquareVector;
	private Point2D.Double heuristicCellTowerPoint;
	private DynamicCell heuristicDynamicCell1;
	private DynamicCell heuristicDynamicCell2;
	
//	public Computation(Line2D.Double heuristicDirectionVector,
//			Point2D.Double heuristicCellTowerPoint,
//			DynamicCell heuristicDynamicCell) {
//
//		this.longestVector = heuristicDirectionVector;
//		this.heuristicCellTowerPoint = heuristicCellTowerPoint;
//		this.heuristicDynamicCell1 = heuristicDynamicCell;
//	}

	public Line2D.Double getLongestVector() {
		return longestVector;
	}

	public void setLongestVector(Line2D.Double longestVector) {
		this.longestVector = longestVector;
	}

//	public Line2D.Double getLeastSquareVector() {
//		return leastSquareVector;
//	}
//
//	public void setLeastSquareVector(Line2D.Double leastSquareVector) {
//		this.leastSquareVector = leastSquareVector;
//	}

	public Point2D.Double getHeuristicCellTowerPoint() {
		return heuristicCellTowerPoint;
	}

	public void setHeuristicCellTowerPoint(Point2D.Double heuristicCellTowerPoint) {
		this.heuristicCellTowerPoint = heuristicCellTowerPoint;
	}

	public DynamicCell getHeuristicDynamicCell1() {
		return heuristicDynamicCell1;
	}

	public void setHeuristicDynamicCell1(DynamicCell heuristicDynamicCell1) {
		this.heuristicDynamicCell1 = heuristicDynamicCell1;
	}

	public DynamicCell getHeuristicDynamicCell2() {
		return heuristicDynamicCell2;
	}

	public void setHeuristicDynamicCell2(DynamicCell heuristicDynamicCell2) {
		this.heuristicDynamicCell2 = heuristicDynamicCell2;
	}


}
