package opencellid;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.List;

import infrastructure.DefaultCell;
import infrastructure.Measurement;

import org.jfree.data.category.DefaultCategoryDataset;

public class OpenCellIdCell extends DefaultCell  {

	String radio;
	
	int mcc;
	int net;
	int area;
	long cell;
	
	int range;
	int samples;
	int changeable;

	int averageSignal;
	
	public OpenCellIdCell(Point2D.Double cellTowerCoordinates, String radio, int mcc, 
			int net, int area, long cell, int range, int samples, int changeable, int averageSignal) {
		super(cellTowerCoordinates, 120.0);
		this.radio = radio;
		this.mcc = mcc;
		this.net = net;
		this.area = area;
		this.cell = cell;
		this.range = range;
		this.samples = samples;
		this.changeable = changeable;
		this.averageSignal = averageSignal;
	}
	
	public String getRadio() {
		return radio;
	}

	public void setRadio(String radio) {
		this.radio = radio;
	}

	public int getMcc() {
		return mcc;
	}

	public void setMcc(int mcc) {
		this.mcc = mcc;
	}

	public int getNet() {
		return net;
	}

	public void setNet(int net) {
		this.net = net;
	}

	public int getArea() {
		return area;
	}
	
	public void setArea(int area) {
		this.area = area;
	}

	public long getCell() {
		return cell;
	}

	public void setCell(long cell) {
		this.cell = cell;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getSamples() {
		return samples;
	}

	public void setSamples(int samples) {
		this.samples = samples;
	}

	public int getChangeable() {
		return changeable;
	}

	public void setChangeable(int changeable) {
		this.changeable = changeable;
	}

	public int getAverageSignal() {
		return averageSignal;
	}

	public void setAverageSignal(int averageSignal) {
		this.averageSignal = averageSignal;
	}

	@Override
	public String toString() {
		String s = "mcc="+mcc+"|net="+net+"|area="+area+"|cell="+cell;//+"|range="+range+"|samples="+samples+"|changeable="+changeable;
		return s;
	}
	
	
}
