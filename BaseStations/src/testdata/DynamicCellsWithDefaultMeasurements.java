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
		
		int cells = 10;
		
		for(int i = 0; i < cells; i++) {
			
			DynamicCell dc = Generate.dynamicCellWithDefaultMeasurements(
					new Point2D.Double(0.0, 0.0), 
					0.0, 
					10.0, 
					113.0, 
					0.0, 
					20);
			
//			System.out.println(dc.toString());
			
			Line2D.Double heuristicVector = Geom.longestVector(dc.getMeasurements(), 10);
			Point2D.Double heuristicCellTower = Geom.endPointOfVectorClosestToOrigo(heuristicVector, dc.getMeasurements(), 10);
			double distanceP1ToCT = dc.getCellTowerCoordinates().distance(heuristicVector.getP1());
			double distanceP2ToCT = dc.getCellTowerCoordinates().distance(heuristicVector.getP2());
			double distanceHeuristicCTToCT = dc.getCellTowerCoordinates().distance(heuristicCellTower);
			System.out.printf("Cell %d\t%.2f\t%.2f\t%.2f",i,distanceP1ToCT, distanceP2ToCT, distanceHeuristicCTToCT);
			
			
			heuristicVector = Geom.linearRegressionVector(dc.getMeasurements(), 10);
			heuristicCellTower = Geom.endPointOfVectorClosestToOrigo(heuristicVector, dc.getMeasurements(), 10);
			distanceP1ToCT = dc.getCellTowerCoordinates().distance(heuristicVector.getP1());
			distanceP2ToCT = dc.getCellTowerCoordinates().distance(heuristicVector.getP2());
			distanceHeuristicCTToCT = dc.getCellTowerCoordinates().distance(heuristicCellTower);
			System.out.printf("\t%.2f\t%.2f\t%.2f\n",distanceP1ToCT, distanceP2ToCT, distanceHeuristicCTToCT);
			
			
			
//			System.out.printf("Heuristic Linear Graph: P1=(%.2f , %.2f) P2=(%.2f , %.2f)\n", heuristicLinearGraph.getP1().getX(), heuristicLinearGraph.getP1().getY(), heuristicLinearGraph.getP2().getX(), heuristicLinearGraph.getP2().getY());
//			double p1DistanceFromCT = dc.getCellTowerCoordinates().distance(heuristicLinearGraph.getP1());
//			double p2DistanceFromCT = dc.getCellTowerCoordinates().distance(heuristicLinearGraph.getP2());
//			System.out.printf("Distance CT to P1 = %.2f\nDistance CT to P2 = %.2f\n", p1DistanceFromCT, p2DistanceFromCT);
			
//			Point2D.Double ctHeuristically = Geom.endPointOfVectorClosestToOrigo(heuristicLinearGraph, dc.getMeasurements(), 10);
//			System.out.printf("\nHeuristic CT Point=(%.2f , %.2f)\n", ctHeuristically.getX(), ctHeuristically.getY());
//			double heuristicCTDistanceFromCT = dc.getCellTowerCoordinates().distance(ctHeuristically);
//			System.out.printf("Distance CT to Heuristic CT = %.2f\n", heuristicCTDistanceFromCT);
		}
	}

}
