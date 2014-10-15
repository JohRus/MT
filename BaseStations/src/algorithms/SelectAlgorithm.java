package algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import datasets.CellID;
import datasets.CellTowerDto;
import datasets.MeasurementDto;

public class SelectAlgorithm {

	public static List<CellTowerDto> start(Map<CellID, List<MeasurementDto>> dataMap) {
		List<CellTowerDto> cellTowers = new ArrayList<CellTowerDto>();
		Scanner scanner = new Scanner(System.in);
		System.out.println("To run centroid on a dataset, press 1");
		System.out.println("To run weighted centroid on a dataset, press 2");
		System.out.println("To run Strongest RSS on a dataset, press 3");
		System.out.println("To go back one step, press 9");
		int command = scanner.nextInt();
		if(command == 1) {
			//				Algorithms.centroid(dataMap);
		}
		else if(command == 2) {
			System.out.println("Using weighted centroid on dataset");
			cellTowers = new ArrayList<CellTowerDto>();
			for(Entry<CellID, List<MeasurementDto>> entry : dataMap.entrySet()) {
//				CellTowerDto cellTower = Algorithms.weightedCentroid(entry.getValue());
//				cellTowers.add(cellTower);
			}
		}
		else if(command == 3) {
			//				Algorithms.strongestRSS(dataMap);
		}
		else {
			
		}
		scanner.close();
		return cellTowers;
	}


}
