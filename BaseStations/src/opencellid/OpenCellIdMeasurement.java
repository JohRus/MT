package opencellid;

import java.awt.geom.Point2D;

import infrastructure.SimpleMeasurement;

public class OpenCellIdMeasurement extends SimpleMeasurement {
	
	int mcc;
	int net;
	int area;
	long cell;
	

	public OpenCellIdMeasurement(Point2D.Double coordinates, int signalStrength, int mcc, int net, int area, long cell) {
		super(coordinates, signalStrength);
		this.mcc = mcc;
		this.net = net;
		this.area = area;
		this.cell = cell;
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
}
