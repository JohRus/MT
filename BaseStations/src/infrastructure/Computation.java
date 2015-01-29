package infrastructure;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Computation {
	
	private Line2D.Double heuristicDirectionVector;
	private Point2D.Double heuristicCellTowerPoint;
	private DynamicCell heuristicDynamicCell;
	
	public Computation(Line2D.Double heuristicDirectionVector,
			Point2D.Double heuristicCellTowerPoint,
			DynamicCell heuristicDynamicCell) {

		this.heuristicDirectionVector = heuristicDirectionVector;
		this.heuristicCellTowerPoint = heuristicCellTowerPoint;
		this.heuristicDynamicCell = heuristicDynamicCell;
	}

	public Line2D.Double getHeuristicDirectionVector() {
		return heuristicDirectionVector;
	}

	public void setHeuristicDirectionVector(Line2D.Double heuristicDirectionVector) {
		this.heuristicDirectionVector = heuristicDirectionVector;
	}

	public Point2D.Double getHeuristicCellTowerPoint() {
		return heuristicCellTowerPoint;
	}

	public void setHeuristicCellTowerPoint(Point2D.Double heuristicCellTowerPoint) {
		this.heuristicCellTowerPoint = heuristicCellTowerPoint;
	}

	public DynamicCell getHeuristicDynamicCell() {
		return heuristicDynamicCell;
	}

	public void setHeuristicDynamicCell(DynamicCell heuristicDynamicCell) {
		this.heuristicDynamicCell = heuristicDynamicCell;
	}
}
