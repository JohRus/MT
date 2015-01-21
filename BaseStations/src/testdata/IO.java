package testdata;

import infrastructure.Cell;
import infrastructure.Measurement;
import infrastructure.OneSector;
import infrastructure.SimpleMeasurement;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class IO {
	
	public static Cell[] readFromFile(String fileName) {
		Cell[] cellArr = null;
		try(BufferedReader br = new BufferedReader(new FileReader(fileName));) {
			int cells = Integer.parseInt(br.readLine());
			cellArr = new Cell[cells];
			for(int i = 0; i < cells; i++) {
				String[] cellCoords = br.readLine().split(",");
				double ctX = java.lang.Double.parseDouble(cellCoords[0]);
				double ctY = java.lang.Double.parseDouble(cellCoords[1]);
				double vpX = java.lang.Double.parseDouble(cellCoords[2]);
				double vpY = java.lang.Double.parseDouble(cellCoords[3]);
				Cell cell = new OneSector(new Point2D.Double(ctX, ctY), new Point2D.Double(vpX, vpY),
						new ArrayList<Measurement>());
				int measurements = Integer.parseInt(br.readLine());
				for(int j = 0; j < measurements; j++) {
					String[] measData = br.readLine().split(",");
					double mX = java.lang.Double.parseDouble(measData[0]);
					double mY = java.lang.Double.parseDouble(measData[1]);
					int ss = Integer.parseInt(measData[2]);
					Measurement meas = new SimpleMeasurement(new Point2D.Double(mX, mY));
					meas.setSignalStrength(ss);
					cell.addMeasurement(meas);
				}
				cellArr[i] = cell;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cellArr;
	}
}
