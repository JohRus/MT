package io;

import java.util.Date;

import datasets.CellTowerDto;
import datasets.MeasurementDto;

public class Parsers {
	
	public static final int MEASUREMENT_SIZE = 22;
	public static final int CELL_TOWER_SIZE = 3;
	
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
		String[] data = new String[CELL_TOWER_SIZE];
		data[0] = String.valueOf(cellTower.getCell());
		data[1] = String.valueOf(cellTower.getLon());
		data[2] = String.valueOf(cellTower.getLat());
		return data;
	}

	public static String[] measurementDtoToStringArray(MeasurementDto measurement) {
		String[] stringArray = new String[MEASUREMENT_SIZE];
		stringArray[0] = String.valueOf(measurement.getMcc());
		stringArray[1] = String.valueOf(measurement.getNet());
		stringArray[2] = String.valueOf(measurement.getArea());
		stringArray[3] = String.valueOf(measurement.getCell());
		stringArray[4] = String.valueOf(measurement.getLon());
		stringArray[5] = String.valueOf(measurement.getLat());
		stringArray[6] = String.valueOf(measurement.getSignal());
		stringArray[7] = String.valueOf(measurement.getMeasured().getTime());
		stringArray[8] = String.valueOf(measurement.getCreated().getTime());
		stringArray[9] = String.valueOf(measurement.getRating());
		stringArray[10] = String.valueOf(measurement.getSpeed());
		stringArray[11] = String.valueOf(measurement.getDirection());
		stringArray[12] = measurement.getRadio();
		stringArray[13] = String.valueOf(measurement.getTa());
		stringArray[14] = String.valueOf(measurement.getRnc());
		stringArray[15] = String.valueOf(measurement.getCid());
		stringArray[16] = String.valueOf(measurement.getPsc());
		stringArray[17] = String.valueOf(measurement.getTac());
		stringArray[18] = String.valueOf(measurement.getPci());
		stringArray[19] = String.valueOf(measurement.getSid());
		stringArray[20] = String.valueOf(measurement.getNid());
		stringArray[21] = String.valueOf(measurement.getBid());
		return stringArray;
	}
}
