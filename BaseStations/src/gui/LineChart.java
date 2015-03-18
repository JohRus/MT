package gui;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.demo.BarChartDemo1;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

public class LineChart {// extends ApplicationFrame {

//	public LineChart(String title) {
//		super(title);
//		JFreeChart lineChart = ChartFactory.createLineChart(
//		         title,
//		         "Deadzone radius","Error",
//		         createDataset(),
//		         PlotOrientation.VERTICAL,
//		         true,true,false);
//		
//		
//	}
//
//	private CategoryDataset createDataset() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public static void main(String[] args) {
		DefaultCategoryDataset ds1 = new DefaultCategoryDataset();
		ds1.addValue(49.09, "dzr=0", "10");
		ds1.addValue(48.58, "dzr=0", "20");
		ds1.addValue(50.50, "dzr=0", "40");
		ds1.addValue(49.28, "dzr=0", "80");
		
		ds1.addValue(51.00, "dzr=20", "10");
		ds1.addValue(49.27, "dzr=20", "20");
		ds1.addValue(49.95, "dzr=20", "40");
		ds1.addValue(50.15, "dzr=20", "80");
		
		ds1.addValue(51.07, "dzr=30", "10");
		ds1.addValue(51.30, "dzr=30", "20");
		ds1.addValue(50.32, "dzr=30", "40");
		ds1.addValue(51.29, "dzr=30", "80");
		
		ds1.addValue(54.55, "dzr=40", "10");
		ds1.addValue(52.29, "dzr=40", "20");
		ds1.addValue(54.35, "dzr=40", "40");
		ds1.addValue(51.47, "dzr=40", "80");
		
		ds1.addValue(55.22, "dzr=50", "10");
		ds1.addValue(54.50, "dzr=50", "20");
		ds1.addValue(54.07, "dzr=50", "40");
		ds1.addValue(53.49, "dzr=50", "80");
		
		ds1.addValue(53.02, "dzr=60", "10");
		ds1.addValue(54.92, "dzr=60", "20");
		ds1.addValue(55.53, "dzr=60", "40");
		ds1.addValue(54.07, "dzr=60", "80");
		
		

		JFreeChart lineChart = ChartFactory.createLineChart(
				"Error growth", "n",
				"Error",
				ds1,
				PlotOrientation.VERTICAL,
				true,true,false);
		
//		XYPlot plot = (XYPlot) lineChart.getPlot();
//		ValueAxis yAxis = plot.getRangeAxis();
//		yAxis.setRange(40.0, 70.0);
		

		int width = 320; /* Width of the image */
		int height = 300; /* Height of the image */ 
		File lineChartPicture = new File("/Users/Johan/Desktop/LineChart.png");
		try {
			ChartUtilities.saveChartAsPNG(lineChartPicture, lineChart, width, height);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
