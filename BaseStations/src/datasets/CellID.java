package datasets;

public class CellID {
	int mcc;
	int net;
	int area;
	long cell;
	
	public CellID(int mcc, int net, int area, long cell) {
		this.mcc = mcc;
		this.net = net;
		this.area = area;
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
	
	
	
	
}
