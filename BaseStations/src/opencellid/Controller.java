package opencellid;


import logic.Generate;
import infrastructure.Computation;
import infrastructure.DefaultCell;

public class Controller {
	
	public Computation generateComputation(DefaultCell originalCell, int n, double d) {
		return Generate.computation(originalCell, n, d);
	}

	public static void main(String[] args) {
		String in = "http://opencellid.org/cell/getMeasures?key=710914bd-60b1-4ad6-aa96-faeb03eb16f8&mcc=260&mnc=1&lac=29001&cellid=22095&radio=GSM&format=json";
		Request r = new Request(in);
		DefaultCell cell = r.getData();
		
		Controller controller = new Controller();
		
		Computation comp = controller.generateComputation(cell, 50, 0.001);
		System.out.println("Done with computation");
		
		new JSONFile(cell, comp.getHeuristicCell1(), "test1.txt");
		System.out.println("Done writing to file");
		
		System.out.println(comp.getHeuristicCell1().getCellTowerCoordinates());
		
//		HashSet<Integer> signals = new HashSet<Integer>();
//		for(Measurement m : cell.getMeasurements()) {
//			OpenCellIdMeasurement openCellIdMeas = (OpenCellIdMeasurement) m;
//			int signal = openCellIdMeas.getSignalStrength();
//			signals.add(signal);
//			if(signal == -10) {
//				System.out.println(openCellIdMeas.getCoordinates());
//			}
//		}
//		
//		for(int i : signals) {
//			System.out.println(i);
//		}

	}

}
