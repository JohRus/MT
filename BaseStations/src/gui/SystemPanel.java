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
		Point2D.Double tempV1 = Geom.toCartesian(actuallCell.getVectorAngle(), 113.0);
		Point2D.Double v1 = new Point2D.Double(xBasis+tempV1.getX(), yBasis-tempV1.getY());
		Point2D.Double tempV2 = Geom.toCartesian(actuallCell.getVectorAngle()+actuallCell.getSectorAngle(), 113.0);
		Point2D.Double v2 = new Point2D.Double(xBasis+tempV2.getX(), yBasis-tempV2.getY());
		g2d.drawLine(doubleToInt(xBasis), doubleToInt(yBasis), doubleToInt(v1.getX()), doubleToInt(v1.getY()));
		g2d.drawLine(doubleToInt(xBasis), doubleToInt(yBasis), doubleToInt(v2.getX()), doubleToInt(v2.getY()));
		int rectX = doubleToInt(xBasis)-(6/2);
		int rectY = doubleToInt(yBasis)-(6/2);
		g2d.drawRect(rectX, rectY, 6, 6);
		for(Measurement m : actuallCell.getMeasurements()) {
			int mX = doubleToInt(xBasis+m.getCoordinates().getX());
			int mY = doubleToInt(yBasis-m.getCoordinates().getY());
			mX = mX-(4/2);
			mY = mY-(4/2);
			g2d.fillOval(mX, mY, 4, 4);
		}
		
		for(Computation c : entry.getValue()) {
			
			Line2D.Double v = c.getHeuristicDirectionVector();
			double angle = Math.toDegrees(Geom.angle(v));
//			System.out.printf("Heuristic vector angle at gui: %.2f\n\n", angle);
			g2d.setColor(Color.blue);
			g2d.drawLine(doubleToInt(xBasis+v.getX1()), doubleToInt(yBasis-v.getY1()), 
					doubleToInt(xBasis+v.getX2()), doubleToInt(yBasis-v.getY2()));
			
			DynamicCell dc = c.getHeuristicDynamicCell();
			g2d.setColor(Color.pink);
			tempV1 = Geom.toCartesian(dc.getVectorAngle(), 113.0);
			v1 = new Point2D.Double(xBasis+dc.getCellTowerCoordinates().getX()+tempV1.getX(), yBasis-dc.getCellTowerCoordinates().getY()-tempV1.getY());
//			v1 = new Point2D.Double(xBasis+tempV1.getX(), yBasis-tempV1.getY());
			tempV2 = Geom.toCartesian(dc.getVectorAngle()+dc.getSectorAngle(), 113.0);
			v2 = new Point2D.Double(xBasis+dc.getCellTowerCoordinates().getX()+tempV2.getX(), yBasis-dc.getCellTowerCoordinates().getY()-tempV2.getY());
			g2d.drawLine(doubleToInt(xBasis+dc.getCellTowerCoordinates().getX()), 
					doubleToInt(yBasis-dc.getCellTowerCoordinates().getY()), 
					doubleToInt(v1.getX()), doubleToInt(v1.getY()));
			g2d.drawLine(doubleToInt(xBasis+dc.getCellTowerCoordinates().getX()),
					doubleToInt(yBasis-dc.getCellTowerCoordinates().getY()),
					doubleToInt(v2.getX()), doubleToInt(v2.getY()));
			rectX = doubleToInt(xBasis+dc.getCellTowerCoordinates().getX())-(4/2);
			rectY = doubleToInt(yBasis-dc.getCellTowerCoordinates().getY())-(4/2);
			g2d.drawRect(rectX, rectY, 4, 4);
		}
	}


	

//	private void drawCell(double ctX, double ctY, List<Measurement> measurements, Graphics2D g2d, Color color) {
//		g2d.setColor(color);
//
////		int ctX = doubleToInt(calcX(dc.getCellTowerCoordinates().getX()));
////		int ctY = doubleToInt(calcY(dc.getCellTowerCoordinates().getY()));
//		
////		drawSectorVectors(ctX, ctY, dc, g2d, Color.black);
//
////		ctX = ctX-(6/2);
////		ctY = ctY-(6/2);
//		g2d.drawRect(ctX, ctY, 6, 6);
//		for(Measurement m : dc.getMeasurements()) {
//			ctX = doubleToInt(calcX(dc.getCellTowerCoordinates().getX(),m.getCoordinates().getX()));
//			ctY = doubleToInt(calcY(dc.getCellTowerCoordinates().getY(),m.getCoordinates().getY()));
//			ctX = ctX-(4/2);
//			ctY = ctY-(4/2);
//			g2d.fillOval(ctX, ctY, 4, 4);
//		}
//	}

//	private void drawHeuristicVectors(DynamicCell dc, List<Line2D.Double> vList, Graphics2D g2d) {
//		g2d.setColor(Color.blue);
//		for(Line2D.Double v : vList) {
//			int x1 = doubleToInt(calcX(dc.getCellTowerCoordinates().getX(),v.getX1()));
//			int x2 = doubleToInt(calcX(dc.getCellTowerCoordinates().getX(),v.getX2()));
//			int y1 = doubleToInt(calcY(dc.getCellTowerCoordinates().getY(),v.getY1()));
//			int y2 = doubleToInt(calcY(dc.getCellTowerCoordinates().getY(),v.getY2()));
//			g2d.drawLine(x1, y1, x2, y2);
//		}
//	}
	
//	private void drawHeuristicCellTowers(DynamicCell dc, List<Point2D.Double> pList, Graphics2D g2d) {
//		g2d.setColor(Color.pink);
//		for(Point2D.Double p : pList) {
//			int x = doubleToInt(calcX(dc.getCellTowerCoordinates().getX(), p.getX()));
//			int y = doubleToInt(calcY(dc.getCellTowerCoordinates().getY(), p.getY()));
//			x = x-(4/2);
//			y = y-(4/2);
//			g2d.drawRect(x, y, 4, 4);
//		}
//	}
	
//	private void drawSectorVectors(int ctX, int ctY, DynamicCell dc, Graphics2D g2d, Color color) {
//		g2d.setColor(color);
//		
//		Point2D.Double v1 = calcCellSectorVector(dc.getVectorAngle(), dc);
//		Point2D.Double v2 = calcCellSectorVector(dc.getVectorAngle()+dc.getSectorAngle(), dc);
//
//		g2d.drawLine(ctX, ctY, doubleToInt(v1.getX()), doubleToInt(v1.getY()));
//		g2d.drawLine(ctX, ctY, doubleToInt(v2.getX()), doubleToInt(v2.getY()));
//	}
//
//	private Point2D.Double cellSectorVector(double angle, DynamicCell dc) {
//		Point2D.Double temp = Geom.toCartesian(angle, 113.0);
//		Point2D.Double p = new Point2D.Double(
//				calcX(dc.getCellTowerCoordinates().getX(), temp.getX()),
//				calcY(dc.getCellTowerCoordinates().getY(), temp.getY()));
//		return p;
//	}

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
