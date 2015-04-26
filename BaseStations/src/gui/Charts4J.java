package gui;

import java.util.HashMap;
import java.util.Map.Entry;

import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.DataUtil;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.Line;
import com.googlecode.charts4j.LineStyle;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.Shape;
import com.googlecode.charts4j.LineChart;

public class Charts4J {

	private static final Color[] colors = new Color[]{Color.RED, Color.BLUE, Color.LIGHTGREEN, Color.YELLOW, Color.MAGENTA, Color.PINK, 
		Color.BROWN, Color.CYAN, Color.ORANGE, Color.DARKGREEN, Color.BISQUE, Color.SALMON, Color.DARKGRAY, 
		Color.STEELBLUE, Color.TURQUOISE};

	public static String errorChartScalingR(HashMap<double[], String> dataPointsMap, int[] cellsSizes) {

		double yLengthMax = 18000.0;
		double yLengthMin = 0.0;

		int i = 0;

		Line[] lines = new Line[dataPointsMap.size()];

		for(Entry<double[], String> entry : dataPointsMap.entrySet()) {
			Line line = Plots.newLine(DataUtil.scaleWithinRange(yLengthMin, yLengthMax, entry.getKey()), colors[i], 
					entry.getValue());
			line.setLineStyle(LineStyle.newLineStyle(1, 1, 0));
			line.addShapeMarkers(Shape.SQUARE, colors[i], 5);
			lines[i] = line;
			i++;
		}

		// Defining chart.
		LineChart chart = GCharts.newLineChart(lines);
		chart.setSize(550, 350);
		//        chart.setTitle("M_max=200, M_min=100, Cells=241, r_include=35000|Errors", Color.BLACK, 10);
//		        chart.setGrid(20, 6.666666666, 3, 2);
		//        chart.setGrid(25, 9.090909090, 3, 2);
//		chart.setGrid(20, 7.142857142, 3, 2);
//		        chart.setGrid(20, 10, 3, 2);
		chart.setGrid(20, 5.555555555, 3, 2);

		// Defining axis info and styles
		AxisStyle axisStyle = AxisStyle.newAxisStyle(Color.BLACK, 10, AxisTextAlignment.CENTER);
		AxisLabels xAxis = AxisLabelsFactory.newAxisLabels("35000", "25000", "15000", "10000", "5000", "2000");
		xAxis.setAxisStyle(axisStyle);
		AxisLabels xAxis2 = AxisLabelsFactory.newAxisLabels("("+cellsSizes[0]+")", "("+cellsSizes[1]+")", "("+cellsSizes[2]+")", "("+cellsSizes[3]+")", "("+cellsSizes[4]+")", "("+cellsSizes[5]+")");
		xAxis2.setAxisStyle(axisStyle);
		AxisLabels yAxis = AxisLabelsFactory.newNumericRangeAxisLabels(yLengthMin, yLengthMax);
		yAxis.setAxisStyle(axisStyle);
		AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("r_include", 50.0);
		xAxis3.setAxisStyle(axisStyle);
		AxisLabels yAxis2 = AxisLabelsFactory.newAxisLabels("Error", 50.0);
		yAxis2.setAxisStyle(axisStyle);

		// Adding axis info to chart.
		chart.addXAxisLabels(xAxis);
		chart.addXAxisLabels(xAxis2);
		chart.addXAxisLabels(xAxis3);
		chart.addYAxisLabels(yAxis);
		chart.addYAxisLabels(yAxis2);

		// Defining background and chart fills.
		chart.setBackgroundFill(Fills.newSolidFill(Color.WHITE));
		chart.setAreaFill(Fills.newSolidFill(Color.WHITE));

		return chart.toURLString();
	}

	public static String errorChartScalingD(HashMap<double[], String> dataPointsMap) {

		double yLengthMax = 15000.0;
		double yLengthMin = 0.0;

		int i = 0;

		Line[] lines = new Line[dataPointsMap.size()];

		for(Entry<double[], String> entry : dataPointsMap.entrySet()) {
			Line line = Plots.newLine(DataUtil.scaleWithinRange(yLengthMin, yLengthMax, entry.getKey()), colors[i], 
					entry.getValue());
			line.setLineStyle(LineStyle.newLineStyle(1, 1, 0));
			line.addShapeMarkers(Shape.SQUARE, colors[i], 5);
			lines[i] = line;
			i++;
		}

		// Defining chart.
		LineChart chart = GCharts.newLineChart(lines);
		chart.setSize(550, 350);
		//        chart.setTitle("M_max=200, M_min=100, Cells=241, r_include=35000|Errors", Color.BLACK, 10);
		        chart.setGrid(25, 6.666666666, 3, 2);
		//        chart.setGrid(25, 9.090909090, 3, 2);
//		chart.setGrid(25, 7.142857142, 3, 2);

		// Defining axis info and styles
		AxisStyle axisStyle = AxisStyle.newAxisStyle(Color.BLACK, 10, AxisTextAlignment.CENTER);
		AxisLabels xAxis = AxisLabelsFactory.newAxisLabels("0,1", "0,01", "0,001", "0,0001", "0,00001");
		xAxis.setAxisStyle(axisStyle);
		AxisLabels yAxis = AxisLabelsFactory.newNumericRangeAxisLabels(yLengthMin, yLengthMax);
		yAxis.setAxisStyle(axisStyle);
		AxisLabels xAxis2 = AxisLabelsFactory.newAxisLabels("d_backwards", 50.0);
		xAxis2.setAxisStyle(axisStyle);
		AxisLabels yAxis2 = AxisLabelsFactory.newAxisLabels("Error", 50.0);
		yAxis2.setAxisStyle(axisStyle);

		// Adding axis info to chart.
		chart.addXAxisLabels(xAxis);
		chart.addXAxisLabels(xAxis2);
		chart.addYAxisLabels(yAxis);
		chart.addYAxisLabels(yAxis2);

		// Defining background and chart fills.
		chart.setBackgroundFill(Fills.newSolidFill(Color.WHITE));
		chart.setAreaFill(Fills.newSolidFill(Color.WHITE));

		return chart.toURLString();
	}

	public static void main(String[] args) {
		int res = (int) 20054.69/1000;
		System.out.println(res);

	}
}
