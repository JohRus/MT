package infrastructure;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Computation {
	
	private Line2D.Double longestVector;
	private DefaultCell heuristicCell1;
	private DefaultCell heuristicCell2;
	
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

	public DefaultCell getHeuristicCell1() {
		return heuristicCell1;
	}

	public void setHeuristicCell1(DefaultCell heuristicCell1) {
		this.heuristicCell1 = heuristicCell1;
	}

	public DefaultCell getHeuristicCell2() {
		return heuristicCell2;
	}

	public void setHeuristicCell2(DefaultCell heuristicCell2) {
		this.heuristicCell2 = heuristicCell2;
	}


}
