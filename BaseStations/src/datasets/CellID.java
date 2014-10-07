package datasets;

public class CellID implements Comparable<CellID> {
	private int mcc;
	private int net;
	private int area;
	private long cell;
	
	public CellID(int mcc, int net, int area, long cell) {
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CellID other = (CellID) obj;
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
	public int compareTo(CellID o) {
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
