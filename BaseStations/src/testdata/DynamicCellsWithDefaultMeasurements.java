package testdata;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D.Double;
import java.util.ArrayList;
import java.util.List;

import infrastructure.Cell;
import infrastructure.DefaultMeasurement;
import infrastructure.DynamicCell;
import infrastructure.Measurement;

public class DynamicCellsWithDefaultMeasurements {

	public static void main(String[] args) {
		DynamicCellsWithDefaultMeasurements gogogo = new DynamicCellsWithDefaultMeasurements();
	}



	public DynamicCellsWithDefaultMeasurements() {
		int cells = 10;

		for(int i = 0; i < cells; i++) {

			DynamicCell dc = Generate.dynamicCellWithDefaultMeasurements(
					new Point2D.Double(0.0, 0.0), 
					0.0, 
					10.0, 
					113.0, 
					0.0, 
					20);

			System.out.println(dc.toString());

			Line2D.Double heuristicVector = Geom.longestVector(dc.getMeasurements(), 10);
			Point2D.Double heuristicCellTower = Geom.distanceFromSurroundingPointsToVector(heuristicVector, dc.getMeasurements(), 10);
			double distanceP1ToCT = dc.getCellTowerCoordinates().distance(heuristicVector.getP1());
			double distanceP2ToCT = dc.getCellTowerCoordinates().distance(heuristicVector.getP2());
			double distanceHeuristicCTToCT = dc.getCellTowerCoordinates().distance(heuristicCellTower);
			System.out.printf("Cell %d\t%.2f\t%.2f\t%.2f",i,distanceP1ToCT, distanceP2ToCT, distanceHeuristicCTToCT);


			heuristicVector = Geom.linearRegressionVector(dc.getMeasurements(), 10);
			heuristicCellTower = Geom.distanceFromSurroundingPointsToVector(heuristicVector, dc.getMeasurements(), 10);
			distanceP1ToCT = dc.getCellTowerCoordinates().distance(heuristicVector.getP1());
			distanceP2ToCT = dc.getCellTowerCoordinates().distance(heuristicVector.getP2());
			distanceHeuristicCTToCT = dc.getCellTowerCoordinates().distance(heuristicCellTower);
			System.out.printf("\t%.2f\t%.2f\t%.2f\n",distanceP1ToCT, distanceP2ToCT, distanceHeuristicCTToCT);
		}
		Draw.draw();
		
	}

//	public DynamicCell getDynamicCell() {
//
//	}

}
