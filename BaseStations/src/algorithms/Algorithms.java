package algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import application.TaskBuilder;
import datasets.CellTowerDto;
import datasets.MeasurementDto;

public class Algorithms {
	
	public static CellTowerDto centroid(TaskBuilder task) {
//		double sumOfLongitudes = 0;
//		double sumOfLatitudes = 0;
//		for(MeasurementDto measurement : measurements) {
//			sumOfLongitudes += measurement.getLon();
//			sumOfLatitudes += measurement.getLat();
//		}
//		double cellTowerLongitude = sumOfLongitudes/measurements.size();
//		double cellTowerLatitude = sumOfLatitudes/measurements.size();
//		
//		return new CellTowerDto(measurements.get(0).getCell(), cellTowerLongitude, cellTowerLatitude);
		return null;
	}
	
	public static CellTowerDto weightedCentroid(TaskBuilder task) {
//		List<Long> sumOfSignals = new ArrayList<Long>();
		
//		LinkedList<MeasurementDto> list = new LinkedList<MeasurementDto>(measurements);
		LinkedList<MeasurementDto> list = new LinkedList<MeasurementDto>();
		double cellTowerLongitude = 0.0;
		double cellTowerLatitude = 0.0;
		int sumOfSignals = 0;
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).getSignal() == 99) {
				list.remove(i);
			}
			else {
				if(list.get(i).getSignal() >= 0) {
					list.get(i).setSignal(inDbm(list.get(i).getSignal()));
				}
				sumOfSignals += list.get(i).getSignal();
			}
				
			
		}
		for(int i = 0; i < list.size(); i++) {
			cellTowerLongitude += list.get(i).getLon() * (list.get(i).getSignal()/sumOfSignals);
			cellTowerLatitude += list.get(i).getLat() * (list.get(i).getSignal()/sumOfSignals);
		}
		return new CellTowerDto(list.get(0).getCell(), cellTowerLongitude, cellTowerLatitude);
	}
	
	public static CellTowerDto strongestRSS(TaskBuilder task) {
//		int invalidMeasurements = 0;
//		int currentStrongestRSS = -1000;
//		List<MeasurementDto> strongestRSSMeasurements = new ArrayList<MeasurementDto>();
//		for(MeasurementDto measurement : measurements) {
//			if(measurement.getSignal() == 99) {
//				invalidMeasurements++;
//			}
//			else {
//				if(measurement.getSignal() > currentStrongestRSS) {
//					strongestRSSMeasurements.clear();
//					strongestRSSMeasurements.add(measurement);
//					currentStrongestRSS = measurement.getSignal();
//				}
//				else if(measurement.getSignal() == currentStrongestRSS) {
//					strongestRSSMeasurements.add(measurement);
//				}
//			}
//		}
//		System.out.println("Invalid measurements: "+invalidMeasurements);
//		double cellTowerLongitude = 0.0;
//		double cellTowerLatitude = 0.0;
//		for(MeasurementDto measurement : strongestRSSMeasurements) {
//			cellTowerLongitude += measurement.getLon();
//			cellTowerLatitude += measurement.getLat();
//		}
//		cellTowerLongitude = cellTowerLongitude/strongestRSSMeasurements.size();
//		cellTowerLatitude = cellTowerLatitude/strongestRSSMeasurements.size();
//		return new CellTowerDto(strongestRSSMeasurements.get(0).getCell(), cellTowerLongitude, cellTowerLatitude);
		return null;
	}
	
	private static int inDbm(int signalStrength) {
		if(signalStrength == 0) return -113;
		else if(signalStrength == 1) return -111;
		else if(signalStrength >= 2 && signalStrength <= 30) {
			return -109+((signalStrength - 2)*2);
		}
		//else if(signalStrength == 31) return -51;
		else return -51;
	}
	
	
		
	
	

}
