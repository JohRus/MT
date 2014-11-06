package testdata;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

class Surface extends JPanel {
	
	Statistics statistics;
	
	public Surface() {
		this.statistics = new Statistics(10);
	}
	
	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
//		AffineTransform transform = AffineTransform.getTranslateInstance(0, 700);
//		transform.scale(700, -700);
//		g2d.setTransform(transform);
		
//		System.out.println("Bestemmer hva som skal tegnes");
		g2d.setColor(Color.black);
		
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(rh);
		
		Dimension size = getSize();
		Insets insets = getInsets();
		int w = size.width - insets.left - insets.right;
		int h = size.height - insets.top - insets.bottom;
//		System.out.printf("w=%d, h=%d\n", w, h);
		
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
//			System.out.printf("X as int = %d - Y as int = %d\n", x,y);
			g2d.fillOval(x, y, 5, 5);
		}
		
		// Cell Tower Centroid coords
		g2d.setColor(Color.blue);
		Point2D.Double centroidCoordinates = this.statistics.getCentroidCoordinates();
		x = doubleToInt(enlarge(centroidCoordinates.getX()));
		y = invertLatitude(h, enlarge(centroidCoordinates.getY()));
//		System.out.printf("X as int = %d - Y as int = %d\n", x,y);
		g2d.fillRect(x, y, 5, 5);
		
		// Cell Tower Weighted Centroid coors
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
		
	}
	
	private int invertLatitude(int height, double latitude) {
		double newY = ((double)height - latitude);
//		System.out.printf("New y = %.1f\n", newY);
		return doubleToInt(newY);
	}
	
	private int doubleToInt(double d) {
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
//		System.out.println("Kjører super.paintComponent");
		super.paintComponent(g);
//		System.out.println("kjører doDrawing");
		doDrawing(g);
	}
}

public class Map extends JFrame {
	
	final static int WIDTH = 700;
	final static int HEIGHT = 700;
	
	public Map() {
		
//		System.out.println("Kaller initUI");
		initUI();
		
	}

	private void initUI() {
//		System.out.println("Kjører initUI");
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
//				System.out.println("Setter visible=true");
				map.setVisible(true);
				
			}
		});

	}

}
