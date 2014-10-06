package datasets;

import java.util.Date;

public class Parsers {
	public static MeasurementDto stringArrayToMeasurementDto(String[] stringArray) {
		int mcc = Integer.parseInt(stringArray[0]);
		int net = Integer.parseInt(stringArray[1]);
		int area = Integer.parseInt(stringArray[2]);
		long cell = Integer.parseInt(stringArray[3]);
		double lon = Double.parseDouble(stringArray[4]);
		double lat = Double.parseDouble(stringArray[5]);
		int signal = stringArray[6].equals("") ? 99 : Integer.parseInt(stringArray[6]);
		Date measured = new Date(Long.parseLong(stringArray[7]));
		Date created = new Date(Long.parseLong(stringArray[8]));
		double rating = stringArray[9].equals("") ? -1 : Double.parseDouble(stringArray[9]);
		double speed = stringArray[10].equals("") ? -1 : Double.parseDouble(stringArray[10]);
		double direction = stringArray[11].equals("") ? -1 : Double.parseDouble(stringArray[11]);
		if(stringArray.length == 12) {
			return new MeasurementDto(mcc, net, area, cell, lon, lat, signal, measured, created, rating, speed, direction);
		}
		String radio = stringArray[12].equals("") ? "" : stringArray[12];
		int ta = stringArray[13].equals("") ? -1 : Integer.parseInt(stringArray[13]);
		int rnc = stringArray[14].equals("") ? -1 : Integer.parseInt(stringArray[14]);
		int cid = stringArray[15].equals("") ? -1 : Integer.parseInt(stringArray[15]);
		int psc = stringArray[16].equals("") ? -1 : Integer.parseInt(stringArray[16]);
		int tac = stringArray[17].equals("") ? -1 : Integer.parseInt(stringArray[17]);
		int pci = stringArray[18].equals("") ? -1 : Integer.parseInt(stringArray[18]);
		int sid = stringArray[19].equals("") ? -1 : Integer.parseInt(stringArray[19]);
		int nid = stringArray[20].equals("") ? -1 : Integer.parseInt(stringArray[20]);
		int bid = stringArray[21].equals("") ? -1 : Integer.parseInt(stringArray[21]);
		//	    	int fromMeasurementsFile = fromMeasurementFile;
		return new MeasurementDto(mcc, net, area, cell, lon, lat, signal, measured, created, 
				rating, speed, direction, radio, ta, rnc, cid, 
				psc, tac, pci, sid, nid, bid);
	}
	
	public static String[] cellTowerDtoToStringArray(CellTowerDto cellTower) {
		String[] data = new String[cellTower.size()];
		data[0] = String.valueOf(cellTower.getCell());
		data[1] = String.valueOf(cellTower.getLon());
		data[2] = String.valueOf(cellTower.getLat());
		return data;
	}

	public static String[] measurementDtoToStringArray(MeasurementDto measurement) {
		String[]
		return null;
	}
}
