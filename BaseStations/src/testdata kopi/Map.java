package testdata;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

class Surface extends JPanel {
	
	Statistics statistics;
	
	public Surface() {
		this.statistics = new Statistics(40);
	}
	
	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
//		AffineTransform transform = AffineTransform.getTranslateInstance(0, 700);
//		transform.scale(700, -700);
//		g2d.setTransform(transform);
		
		g2d.setColor(Color.black);
		
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(rh);
		
		Dimension size = getSize();
		Insets insets = getInsets();
		int w = size.width - insets.left - insets.right;
		int h = size.height - insets.top - insets.bottom;
		
		// Cell Tower real coords
		Point2D.Double cellTower = this.statistics.getCell().getCellTowerCoordinates();
		int x = doubleToInt(enlarge(cellTower.getX()));
		int y = invertLatitude(h, enlarge(cellTower.getY()));
		g2d.fillRect(x, y, 5, 5);
		
		// Measurements coords
		List<Measurement> measurements = this.statistics.getCell().getMeasurements();
		for(Measurement measurement : measurements) {
			x = doubleToInt(enlarge(measurement.getCoordinates().getX()));
			y = invertLatitude(h, enlarge(measurement.getCoordinates().getY()));
			g2d.fillOval(x, y, 5, 5);
		}
		
		// Cell Tower Centroid coords
		g2d.setColor(Color.blue);
		Point2D.Double centroidCoordinates = this.statistics.getCentroidCoordinates();
		x = doubleToInt(enlarge(centroidCoordinates.getX()));
		y = invertLatitude(h, enlarge(centroidCoordinates.getY()));
		g2d.fillRect(x, y, 5, 5);
		
		// Cell Tower Weighted Centroid coords
		g2d.setColor(Color.green);
		Point2D.Double weightedCentroidCoordinates = this.statistics.getWeightedCentroidCoordinates();
		x = doubleToInt(enlarge(weightedCentroidCoordinates.getX()));
		y = invertLatitude(h, enlarge(weightedCentroidCoordinates.getY()));
		g2d.fillRect(x, y, 5, 5);
		
		// Cell Tower Strongest RSS coords
		g2d.setColor(Color.red);
		Point2D.Double strongestRSSCoordinates = this.statistics.getStrongestRSSCoordinates();
		x = doubleToInt(enlarge(strongestRSSCoordinates.getX()));
		y = invertLatitude(h, enlarge(strongestRSSCoordinates.getY()));
		g2d.fillRect(x, y, 5, 5);
		
		// Cell Tower Centroid with threshold coords
		g2d.setColor(Color.cyan);
		Point2D.Double centroidWithThresholdCoordinates = this.statistics.getCentroidWithThresholdCoordinates();
		x = doubleToInt(enlarge(centroidWithThresholdCoordinates.getX()));
		y = invertLatitude(h, enlarge(centroidWithThresholdCoordinates.getY()));
		g2d.fillRect(x, y, 5, 5);
		
		// Cell Tower Weighted Centroid with threshold coords
		g2d.setColor(Color.orange);
		Point2D.Double weightedCentroidWithThresholdCoordinates = this.statistics.getWeightedCentroidWithThresholdCoordinates();
		x = doubleToInt(enlarge(weightedCentroidWithThresholdCoordinates.getX()));
		y = invertLatitude(h, enlarge(weightedCentroidWithThresholdCoordinates.getY()));
		g2d.fillRect(x, y, 5, 5);
		
		// Cell Tower Strongest RSS with threshold coords
		g2d.setColor(Color.pink);
		Point2D.Double strongestRSSWithThresholdCoordinates = this.statistics.getStrongestRSSWithThresholdCoordinates();
		x = doubleToInt(enlarge(strongestRSSWithThresholdCoordinates.getX()));
		y = invertLatitude(h, enlarge(strongestRSSWithThresholdCoordinates.getY()));
		g2d.fillRect(x, y, 5, 5);
	}
	
	protected static int invertLatitude(int height, double latitude) {
		double newY = ((double)height - latitude);
		return doubleToInt(newY);
	}
	
	protected static int doubleToInt(double d) {
		int toInt = (int)d;
		if(d-(double)toInt >= 0.5) {
			return toInt+1;
		}
		else
			return toInt;
	}
	
	private double enlarge(double d) {
		return d*2;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}
}

public class Map extends JFrame {
	
	final static int WIDTH = 700;
	final static int HEIGHT = 700;
	
	public Map() {
		initUI();
	}

	private void initUI() {
		setTitle("Map");
		
		add(new Surface());
		
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Map map = new Map();
				map.setVisible(true);
			}
		});
	}
}