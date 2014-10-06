package datasets;

public class CellTowerDto {
	
	private int size = 3;
	
	private long cell;
	private double lon;
	private double lat;
	
	public CellTowerDto(long cell, double lon, double lat) {
		this.cell = cell;
		this.lon = lon;
		this.lat = lat;
	}
	
	public int size() {
		return this.size;
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

	@Override
	public String toString() {
		return "CellTowerDto [cellId=" + cell + ", longitude=" + lon
				+ ", latitude=" + lat + "]";
	}
	
	
	
	
}
