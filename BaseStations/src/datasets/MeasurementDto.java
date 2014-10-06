package datasets;

import java.util.Date;
import java.util.UUID;

public class MeasurementDto {
	
	//private UUID id;
	private int mcc;
	private int net;
	private int area;
	private long cell;
	private double lon;
	private double lat;
	private int signal;
	private Date measured;
	private Date created;
	private double rating;
	private double speed;
	private double direction;
	private String radio;
	private int ta;
	private int rnc;
	private int cid;
	private int psc;
	private int tac;
	private int pci;
	private int sid;
	private int nid;
	private int bid;
//	private int fromMeasurementsFile;
	
	public MeasurementDto() {
		
	}

	public MeasurementDto(int mcc, int net, int area, long cell, double lon,
		double lat, int signal, Date measured, Date created, double rating,
		double speed, double direction) {
	this.mcc = mcc;
	this.net = net;
	this.area = area;
	this.cell = cell;
	this.lon = lon;
	this.lat = lat;
	this.signal = signal;
	this.measured = measured;
	this.created = created;
	this.rating = rating;
	this.speed = speed;
	this.direction = direction;
}



	public MeasurementDto(int mcc, int net, int area, long cell, double lon,
			double lat, int signal, Date measured, Date created, double rating,
			double speed, double direction, String radio, int ta, int rnc,
			int cid, int psc, int tac, int pci, int sid, int nid, int bid) {
		this.mcc = mcc;
		this.net = net;
		this.area = area;
		this.cell = cell;
		this.lon = lon;
		this.lat = lat;
		this.signal = signal;
		this.measured = measured;
		this.created = created;
		this.rating = rating;
		this.speed = speed;
		this.direction = direction;
		this.radio = radio;
		this.ta = ta;
		this.rnc = rnc;
		this.cid = cid;
		this.psc = psc;
		this.tac = tac;
		this.pci = pci;
		this.sid = sid;
		this.nid = nid;
		this.bid = bid;
//		this.fromMeasurementsFile = fromMeasurementsFile;
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

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public int getSignal() {
		return signal;
	}

	public void setSignal(int signal) {
		this.signal = signal;
	}

	public Date getMeasured() {
		return measured;
	}

	public void setMeasured(Date measured) {
		this.measured = measured;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

	public String getRadio() {
		return radio;
	}

	public void setRadio(String radio) {
		this.radio = radio;
	}

	public int getTa() {
		return ta;
	}

	public void setTa(int ta) {
		this.ta = ta;
	}

	public int getRnc() {
		return rnc;
	}

	public void setRnc(int rnc) {
		this.rnc = rnc;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getPsc() {
		return psc;
	}

	public void setPsc(int psc) {
		this.psc = psc;
	}

	public int getTac() {
		return tac;
	}

	public void setTac(int tac) {
		this.tac = tac;
	}

	public int getPci() {
		return pci;
	}

	public void setPci(int pci) {
		this.pci = pci;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public int getNid() {
		return nid;
	}

	public void setNid(int nid) {
		this.nid = nid;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

//	public int getFromMeasurementsFile() {
//		return fromMeasurementsFile;
//	}
//
//	public void setFromMeasurementsFile(int fromMeasurementsFile) {
//		this.fromMeasurementsFile = fromMeasurementsFile;
//	}

	@Override
	public String toString() {
		return "MeasurementDto [mcc=" + mcc + ", net=" + net + ", area=" + area
				+ ", cell=" + cell + ", lon=" + lon + ", lat=" + lat
				+ ", signal=" + signal + "]";
	}

}
