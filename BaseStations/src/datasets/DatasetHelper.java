package datasets;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DatasetHelper {
	
	public static List<MeasurementDto> merge(LinkedList<MeasurementDto> fileOne, LinkedList<MeasurementDto> fileTwo) {
		List<MeasurementDto> merged = new ArrayList<MeasurementDto>();
		while(!fileOne.isEmpty() || !fileTwo.isEmpty()) {
			if(fileOne.isEmpty()) {
				merged.addAll(fileTwo);
				break;
			}
			else if(fileTwo.isEmpty()) {
				merged.addAll(fileOne);
				break;
			}
			if(fileOne.peek().compareTo(fileTwo.peek()) == -1 || fileOne.peek().compareTo(fileTwo.peek()) == 0) {
				merged.add(fileOne.poll());
			}
			else {
				merged.add(fileTwo.poll());
			}
		}
		return merged;
	}
	
}
