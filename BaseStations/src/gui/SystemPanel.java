package gui;

import infrastructure.Computation;
import infrastructure.DynamicCell;
import infrastructure.Measurement;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;

public class SystemPanel extends JPanel {
	// x-axis coord constants
	public static final int X_AXIS_FIRST_X_COORD = 0;
	public static final int X_AXIS_SECOND_X_COORD = 600;
	public static final int X_AXIS_Y_COORD = 600;

	// y-axis coord constants
	public static final int Y_AXIS_FIRST_Y_COORD = 50;
	public static final int Y_AXIS_SECOND_Y_COORD = 700;
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

	private Map<DynamicCell, Computation> data;// = new HashMap<DynamicCell, Computation>();
	
	private boolean showhideMeasurements;
	private boolean showHideLongestVectors;
	private boolean showHideHeuristicDCs;
	private boolean showHideChosenHeuristicDC;
	
	public SystemPanel() {
		data = new HashMap<DynamicCell, Computation>();
		showhideMeasurements = false;
		showHideLongestVectors = false;
		showHideHeuristicDCs = false;
		showHideChosenHeuristicDC = false;
	}

	public void addData(DynamicCell dc, Computation computation) {
		data.put(dc, computation);
	}

	public void clearData() {
		data.clear();
		hideAll();
	}

	public Map<DynamicCell, Computation> getData() {
		return data;
	}
	
	public void showHideMeasurements() {
		showhideMeasurements = !showhideMeasurements;
	}
	
	public void showHideLongestVectors() {
		showHideLongestVectors = !showHideLongestVectors;
	}
	

	public void showHideHeuristicDCs() {
		showHideHeuristicDCs = !showHideHeuristicDCs;
	}
	
	public void showHideChosenHeuristicDC() {
		showHideChosenHeuristicDC = !showHideChosenHeuristicDC;
	}
	
	public void hideAll() {
		showhideMeasurements = false;
		showHideLongestVectors = false;
		showHideHeuristicDCs = false;
		showHideChosenHeuristicDC = false;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		for(Entry<DynamicCell, Computation> entry : data.entrySet()) {
			drawEntry(entry, g2d);
		}

	}

	private void drawEntry(Entry<DynamicCell, Computation> entry, Graphics2D g2d) {
		
		drawCell(entry.getKey(), 6, g2d, Color.black);
		
		if(showhideMeasurements) {
			for(Measurement m : entry.getKey().getMeasurements()) {

				int mX = doubleToInt(X_AXIS_FIRST_X_COORD+m.getCoordinates().getX()-(4/2));
				int mY = doubleToInt(Y_AXIS_SECOND_Y_COORD-m.getCoordinates().getY()-(4/2));

				g2d.fillOval(mX, mY, 4, 4);
			}
			
//			for(Line2D.Double obs : entry.getKey().getObstructions()) {
//				drawVector(obs, g2d, Color.black);
//			}
		}

		if(showHideLongestVectors) {			
			drawVector(entry.getValue().getLongestVector(), g2d, Color.magenta);
		}

		if(showHideHeuristicDCs) {
			drawCell(entry.getValue().getHeuristicDynamicCell1(), 4, g2d, Color.pink);

			drawCell(entry.getValue().getHeuristicDynamicCell2(), 4, g2d, Color.cyan);
		}
		
		if(showHideChosenHeuristicDC) {
			drawCell(entry.getValue().getHeuristicDynamicCell1(), 4, g2d, Color.pink);
		}
	}
	
	private void drawCell(DynamicCell dc, int ctSize, Graphics2D g2d, Color color) {
		g2d.setColor(color);
		
		drawVector(dc.getVector1(), g2d, color);
		
		drawVector(dc.getVector2(), g2d, color);
		
		int rectX = doubleToInt(X_AXIS_FIRST_X_COORD+dc.getCellTowerCoordinates().getX()-(ctSize/2));
		int rectY = doubleToInt(Y_AXIS_SECOND_Y_COORD-dc.getCellTowerCoordinates().getY()-(ctSize/2));
		g2d.drawRect(rectX, rectY, ctSize, ctSize);
	}
	
	private void drawVector(Line2D.Double vector, Graphics2D g2d, Color color) {
		g2d.setColor(color);
		g2d.drawLine(
				doubleToInt(X_AXIS_FIRST_X_COORD+vector.getX1()), 
				doubleToInt(Y_AXIS_SECOND_Y_COORD-vector.getY1()), 
				doubleToInt(X_AXIS_FIRST_X_COORD+vector.getX2()), 
				doubleToInt(Y_AXIS_SECOND_Y_COORD-vector.getY2()));
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
