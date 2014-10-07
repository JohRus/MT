package datasets;

import java.util.Date;
import java.util.UUID;

public class MeasurementDto implements Comparable<MeasurementDto> {
	
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
	this.radio = "";
	this.ta = -1;
	this.rnc = -1;
	this.cid = -1;
	this.psc = -1;
	this.tac = -1;
	this.pci = -1;
	this.sid = -1;
	this.nid = -1;
	this.bid = -1;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + area;
		result = prime * result + (int) (cell ^ (cell >>> 32));
		result = prime * result + mcc;
		result = prime * result + net;
		return result;
	}

	@Override
	public String toString() {
		return "MeasurementDto [mcc=" + mcc + ", net=" + net + ", area=" + area
				+ ", cell=" + cell + ", lon=" + lon + ", lat=" + lat
				+ ", signal=" + signal + ", measured=" + measured
				+ ", created=" + created + ", rating=" + rating + ", speed="
				+ speed + ", direction=" + direction + ", radio=" + radio
				+ ", ta=" + ta + ", rnc=" + rnc + ", cid=" + cid + ", psc="
				+ psc + ", tac=" + tac + ", pci=" + pci + ", sid=" + sid
				+ ", nid=" + nid + ", bid=" + bid + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeasurementDto other = (MeasurementDto) obj;
		if (area != other.area)
			return false;
		if (cell != other.cell)
			return false;
		if (mcc != other.mcc)
			return false;
		if (net != other.net)
			return false;
		return true;
	}

	@Override
	public int compareTo(MeasurementDto o) {
		if(this.getMcc() < o.getMcc()) {return -1;}
		else if(this.getMcc() > o.getMcc()) {return 1;}
		else {
			if(this.getNet() < o.getNet()) {return -1;}
			else if(this.getNet() > o.getNet()) {return 1;}
			else {
				if(this.getArea() < o.getArea()) {return -1;}
				else if(this.getArea() > o.getArea()) {return 1;}
				else {
					if(this.getCell() < o.getCell()) {return -1;}
					else if(this.getCell() > o.getCell()) {return 1;}
					else {return 0;}
				}
			}
		}
	}

}
