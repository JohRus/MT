package gui;

import infrastructure.Computation;
import infrastructure.DynamicCell;
import infrastructure.Measurement;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;

import testdata.Geom;

public class SystemPanel extends JPanel {
	// x-axis coord constants
	public static final int X_AXIS_FIRST_X_COORD = 50;
	public static final int X_AXIS_SECOND_X_COORD = 600;
	public static final int X_AXIS_Y_COORD = 600;

	// y-axis coord constants
	public static final int Y_AXIS_FIRST_Y_COORD = 50;
	public static final int Y_AXIS_SECOND_Y_COORD = 600;
	public static final int Y_AXIS_X_COORD = 50;

	//arrows of axis are represented with "hipotenuse" of 
	//triangle
	// now we are define length of cathetas of that triangle
	public static final int FIRST_LENGHT = 10;
	public static final int SECOND_LENGHT = 5;

	// size of start coordinate lenght
	public static final int ORIGIN_COORDINATE_LENGHT = 6;

	// distance of coordinate strings from axis
	public static final int AXIS_STRING_DISTANCE = 20;

//	private List<DynamicCell> cellList = new ArrayList<DynamicCell>();
//	private Map<DynamicCell, List<Line2D.Double>> heuristicVectorMap = new HashMap<DynamicCell,List<Line2D.Double>>();
//	private Map<DynamicCell, List<Point2D.Double>> heuristicCellTowerMap = new HashMap<DynamicCell,List<Point2D.Double>>();
//	private Map<DynamicCell, List<DynamicCell>> heuristicDynamicCellMap = new HashMap<DynamicCell,List<DynamicCell>>();
	
	private Map<DynamicCell, List<Computation>> data = new HashMap<DynamicCell, List<Computation>>();
	
	public void addData(DynamicCell dc, List<Computation> computationList) {
		data.put(dc, computationList);
	}
	
	public void clearData() {
		data.clear();
	}
	
//	public void addDynamicCell(DynamicCell dc) {
//		cellList.add(dc);
//	}
//
//	public void clearCells() {
//		cellList.clear();
//	}
//
//	public void addHeuristicVectors(DynamicCell dc, List<Line2D.Double> heuristicVectors) {
//		heuristicVectorMap.put(dc, heuristicVectors);
//	}
//
//	public void clearHeuristicVectors() {
//		heuristicVectorMap.clear();
//	}
//
//	public void addHeuristicCellTowers(DynamicCell dc, List<Point2D.Double> heuristicCellTowers) {
//		heuristicCellTowerMap.put(dc, heuristicCellTowers);
//	}
//
//	public void clearHeuristicCellTowers() {
//		heuristicCellTowerMap.clear();
//	}
//	
//	public void addHeuristicDynamicCells(DynamicCell dc, List<DynamicCell> heuristicDynamicCells) {
//		heuristicDynamicCellMap.put(dc, heuristicDynamicCells);
//	}
//	
//	public void clearHeuristicDynamicCells() {
//		heuristicDynamicCellMap.clear();
//	}

	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// x-axis
		g2d.drawLine(X_AXIS_FIRST_X_COORD, X_AXIS_Y_COORD,
				X_AXIS_SECOND_X_COORD, X_AXIS_Y_COORD);
		// y-axis
		g2d.drawLine(Y_AXIS_X_COORD, Y_AXIS_FIRST_Y_COORD,
				Y_AXIS_X_COORD, Y_AXIS_SECOND_Y_COORD);

		// x-axis arrow
		g2d.drawLine(X_AXIS_SECOND_X_COORD - FIRST_LENGHT,
				X_AXIS_Y_COORD - SECOND_LENGHT,
				X_AXIS_SECOND_X_COORD, X_AXIS_Y_COORD);
		g2d.drawLine(X_AXIS_SECOND_X_COORD - FIRST_LENGHT,
				X_AXIS_Y_COORD + SECOND_LENGHT,
				X_AXIS_SECOND_X_COORD, X_AXIS_Y_COORD);

		// y-axis arrow
		g2d.drawLine(Y_AXIS_X_COORD - SECOND_LENGHT,
				Y_AXIS_FIRST_Y_COORD + FIRST_LENGHT,
				Y_AXIS_X_COORD, Y_AXIS_FIRST_Y_COORD);
		g2d.drawLine(Y_AXIS_X_COORD + SECOND_LENGHT, 
				Y_AXIS_FIRST_Y_COORD + FIRST_LENGHT,
				Y_AXIS_X_COORD, Y_AXIS_FIRST_Y_COORD);

		// draw origin Point
		g2d.fillOval(
				X_AXIS_FIRST_X_COORD - (ORIGIN_COORDINATE_LENGHT / 2), 
				Y_AXIS_SECOND_Y_COORD - (ORIGIN_COORDINATE_LENGHT / 2),
				ORIGIN_COORDINATE_LENGHT, ORIGIN_COORDINATE_LENGHT);

		// draw text "X" and draw text "Y"
		g2d.drawString("X", X_AXIS_SECOND_X_COORD - AXIS_STRING_DISTANCE / 2,
				X_AXIS_Y_COORD + AXIS_STRING_DISTANCE);
		g2d.drawString("Y", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
				Y_AXIS_FIRST_Y_COORD + AXIS_STRING_DISTANCE / 2);
		g2d.drawString("(0, 0)", X_AXIS_FIRST_X_COORD - AXIS_STRING_DISTANCE,
				Y_AXIS_SECOND_Y_COORD + AXIS_STRING_DISTANCE);

		// numerate axis
		int xCoordNumbers = 10;
		int yCoordNumbers = 10;
		int xLength = (X_AXIS_SECOND_X_COORD - X_AXIS_FIRST_X_COORD)
				/ xCoordNumbers;
		int yLength = (Y_AXIS_SECOND_Y_COORD - Y_AXIS_FIRST_Y_COORD)
				/ yCoordNumbers;

		// draw x-axis numbers
		for(int i = 1; i < xCoordNumbers; i++) {
			g2d.drawLine(X_AXIS_FIRST_X_COORD + (i * xLength),
					X_AXIS_Y_COORD - SECOND_LENGHT,
					X_AXIS_FIRST_X_COORD + (i * xLength),
					X_AXIS_Y_COORD + SECOND_LENGHT);
			g2d.drawString(Integer.toString(i), 
					X_AXIS_FIRST_X_COORD + (i * xLength) - 3,
					X_AXIS_Y_COORD + AXIS_STRING_DISTANCE);
		}

		//draw y-axis numbers
		for(int i = 1; i < yCoordNumbers; i++) {
			g2d.drawLine(Y_AXIS_X_COORD - SECOND_LENGHT,
					Y_AXIS_SECOND_Y_COORD - (i * yLength), 
					Y_AXIS_X_COORD + SECOND_LENGHT,
					Y_AXIS_SECOND_Y_COORD - (i * yLength));
			g2d.drawString(Integer.toString(i), 
					Y_AXIS_X_COORD - AXIS_STRING_DISTANCE, 
					Y_AXIS_SECOND_Y_COORD - (i * yLength));
		}
		
		for(Entry<DynamicCell, List<Computation>> entry : data.entrySet()) {
			drawEntry(entry, g2d);
		}

//		for(DynamicCell dc : cellList) {
//			drawCell(dc, g2d);
//		}
//
//		for(Entry<DynamicCell, List<Line2D.Double>> entry : heuristicVectorMap.entrySet()) {
//			drawHeuristicVectors(entry.getKey(), entry.getValue(), g2d);
//		}
//		
//		for(Entry<DynamicCell, List<Point2D.Double>> entry : heuristicCellTowerMap.entrySet()) {
//			drawHeuristicCellTowers(entry.getKey(), entry.getValue(), g2d);
//		}
//		
//		for(Entry<DynamicCell, List<DynamicCell>> entry : heuristicDynamicCellMap.entrySet()) {
//			
//			for(DynamicCell heuristicDC : entry.getValue()) {
//				int ctX = doubleToInt(calcX(entry.getKey().getCellTowerCoordinates().getX(), heuristicDC.getCellTowerCoordinates().getX()));
//				int ctY = doubleToInt(calcY(entry.getKey().getCellTowerCoordinates().getY(), heuristicDC.getCellTowerCoordinates().getY()));
//				drawSectorVectors(ctX, ctY, heuristicDC, g2d, Color.pink);
//			}
//		}

		g2d.drawRect(10, 10, 10, 10);

	}
	
	private void drawEntry(Entry<DynamicCell, List<Computation>> entry, Graphics2D g2d) {
		DynamicCell actuallCell = entry.getKey();
		double xBasis = xBasis(actuallCell.getCellTowerCoordinates().getX());
		double yBasis = yBasis(actuallCell.getCellTowerCoordinates().getY());
		
		g2d.setColor(Color.black);
//		Point2D.Double v1 = new Point2D.Double(xBasis+actuallCell.getVector1().getX2(), yBasis-actuallCell.getVector1().getY2());
//		Point2D.Double v2 = new Point2D.Double(xBasis+actuallCell.getVector2().getX2(), yBasis-actuallCell.getVector2().getY2());
//		Point2D.Double v1 = new Point2D.Double(X_AXIS_FIRST_X_COORD+actuallCell.getVector1().getX2(), Y_AXIS_SECOND_Y_COORD-actuallCell.getVector1().getY2());
//		Point2D.Double v2 = new Point2D.Double(X_AXIS_FIRST_X_COORD+actuallCell.getVector2().getX2(), Y_AXIS_SECOND_Y_COORD-actuallCell.getVector2().getY2());
		
//		g2d.drawLine(doubleToInt(xBasis), doubleToInt(yBasis), doubleToInt(v1.getX()), doubleToInt(v1.getY()));
//		g2d.drawLine(doubleToInt(xBasis), doubleToInt(yBasis), doubleToInt(v2.getX()), doubleToInt(v2.getY()));
		g2d.drawLine(
				doubleToInt(X_AXIS_FIRST_X_COORD+actuallCell.getCellTowerCoordinates().getX()),
				doubleToInt(Y_AXIS_SECOND_Y_COORD-actuallCell.getCellTowerCoordinates().getY()), 
				doubleToInt(X_AXIS_FIRST_X_COORD+actuallCell.getVector1().getX2()), 
				doubleToInt(Y_AXIS_SECOND_Y_COORD-actuallCell.getVector1().getY2()));
		g2d.drawLine(
				doubleToInt(X_AXIS_FIRST_X_COORD+actuallCell.getCellTowerCoordinates().getX()), 
				doubleToInt(Y_AXIS_SECOND_Y_COORD-actuallCell.getCellTowerCoordinates().getY()), 
				doubleToInt(X_AXIS_FIRST_X_COORD+actuallCell.getVector2().getX2()), 
				doubleToInt(Y_AXIS_SECOND_Y_COORD-actuallCell.getVector2().getY2()));
//		int rectX = doubleToInt(xBasis)-(6/2);
//		int rectY = doubleToInt(yBasis)-(6/2);
		int rectX = doubleToInt(X_AXIS_FIRST_X_COORD+actuallCell.getCellTowerCoordinates().getX()-(6/2));
		int rectY = doubleToInt(Y_AXIS_SECOND_Y_COORD-actuallCell.getCellTowerCoordinates().getY()-(6/2));
		g2d.drawRect(rectX, rectY, 6, 6);
		for(Measurement m : actuallCell.getMeasurements()) {
//			int mX = doubleToInt(xBasis+m.getCoordinates().getX());
//			int mY = doubleToInt(yBasis-m.getCoordinates().getY());
			int mX = doubleToInt(X_AXIS_FIRST_X_COORD+m.getCoordinates().getX()-(4/2));
			int mY = doubleToInt(Y_AXIS_SECOND_Y_COORD-m.getCoordinates().getY()-(4/2));
//			mX = mX-(4/2);
//			mY = mY-(4/2);
			g2d.fillOval(mX, mY, 4, 4);
		}
		
//		System.out.printf("GUI: Sector Vector 1 Angle: %.2f\n", Math.toDegrees(Geom.angle(actuallCell.getVector1())));
//		System.out.printf("GUI: Sector Vector 2 Angle: %.2f\n\n", Math.toDegrees(Geom.angle(actuallCell.getVector2())));
		
		
		for(Computation c : entry.getValue()) {
			
			Line2D.Double v = c.getHeuristicDirectionVector();
//			double angle = Math.toDegrees(Geom.angle(v));
			g2d.setColor(Color.blue);
			g2d.drawLine(
					doubleToInt(X_AXIS_FIRST_X_COORD+v.getX1()), 
					doubleToInt(Y_AXIS_SECOND_Y_COORD-v.getY1()), 
					doubleToInt(X_AXIS_FIRST_X_COORD+v.getX2()), 
					doubleToInt(Y_AXIS_SECOND_Y_COORD-v.getY2()));
			
			DynamicCell dc = c.getHeuristicDynamicCell();
			g2d.setColor(Color.pink);
//			Point2D.Double v1 = new Point2D.Double(xBasis+dc.getVector1().getX2(), yBasis-dc.getVector1().getY2());
//			Point2D.Double v2 = new Point2D.Double(xBasis+dc.getVector2().getX2(), yBasis-dc.getVector2().getY2());
//			g2d.drawLine(doubleToInt(xBasis+dc.getCellTowerCoordinates().getX()), 
//					doubleToInt(yBasis-dc.getCellTowerCoordinates().getY()), 
//					doubleToInt(v1.getX()), doubleToInt(v1.getY()));
//			g2d.drawLine(doubleToInt(xBasis+dc.getCellTowerCoordinates().getX()),
//					doubleToInt(yBasis-dc.getCellTowerCoordinates().getY()),
//					doubleToInt(v2.getX()), doubleToInt(v2.getY()));
			g2d.drawLine(
					doubleToInt(X_AXIS_FIRST_X_COORD+dc.getCellTowerCoordinates().getX()),
					doubleToInt(Y_AXIS_SECOND_Y_COORD-dc.getCellTowerCoordinates().getY()), 
					doubleToInt(X_AXIS_FIRST_X_COORD+dc.getVector1().getX2()), 
					doubleToInt(Y_AXIS_SECOND_Y_COORD-dc.getVector1().getY2()));
			g2d.drawLine(
					doubleToInt(X_AXIS_FIRST_X_COORD+dc.getCellTowerCoordinates().getX()), 
					doubleToInt(Y_AXIS_SECOND_Y_COORD-dc.getCellTowerCoordinates().getY()), 
					doubleToInt(X_AXIS_FIRST_X_COORD+dc.getVector2().getX2()), 
					doubleToInt(Y_AXIS_SECOND_Y_COORD-dc.getVector2().getY2()));
//			rectX = doubleToInt(xBasis+dc.getCellTowerCoordinates().getX())-(4/2);
//			rectY = doubleToInt(yBasis-dc.getCellTowerCoordinates().getY())-(4/2);
			rectX = doubleToInt(X_AXIS_FIRST_X_COORD+dc.getCellTowerCoordinates().getX()-(4/2));
			rectY = doubleToInt(Y_AXIS_SECOND_Y_COORD-dc.getCellTowerCoordinates().getY()-(4/2));
			g2d.drawRect(rectX, rectY, 4, 4);
			
//			System.out.printf("GUI: Heuristic Vector Angle: %.2f\n", Math.toDegrees(Geom.angle(v)));
//			System.out.printf("GUI: Heuristic Sector Vector 1 Angle: %.2f\n", Math.toDegrees(Geom.angle(dc.getVector1())));
//			System.out.printf("GUI: Heuristic Sector Vector 2 Angle: %.2f\n\n", Math.toDegrees(Geom.angle(dc.getVector2())));
			
		}
	}


	private double xBasis(double realCTX) {
//		double sum = (double) X_AXIS_FIRST_X_COORD;
//		for(int i = 0; i < parameters.length; i++) {
//			sum += parameters[i];
//		}
//		return sum;
		return (double) X_AXIS_FIRST_X_COORD + realCTX;
	}

	private double yBasis(double realCTY) {
//		double sum = (double) Y_AXIS_SECOND_Y_COORD;
//		for(int i = 0; i < parameters.length; i++) {
//			sum -= parameters[i];
//		}
//		return sum;
		return (double) Y_AXIS_SECOND_Y_COORD - realCTY;
	}

	private int doubleToInt(double d) {
		int toInt = (int)d;
		if(d-(double)toInt >= 0.5) {
			return toInt+1;
		}
		else
			return toInt;
	}
}
