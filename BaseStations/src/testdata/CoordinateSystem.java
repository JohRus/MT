package testdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CoordinateSystem {
	
	int maxLongitude;
	int maxLatitude;
	
	int cellsSize;	
	List<Cell> cells;

	public CoordinateSystem(int maxLongitude, int maxLatitude) {
		this.maxLongitude = maxLongitude;
		this.maxLatitude = maxLatitude;
		this.cellsSize = 0;
		this.cells = new ArrayList<Cell>();
	}
	
	public int getMaxLongitude() {
		return maxLongitude;
	}

	public void setMaxLongitude(int maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	public int getMaxLatitude() {
		return maxLatitude;
	}

	public void setMaxLatitude(int maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	public int getCellsSize() {
		return cellsSize;
	}

	public void setCellsSize(int cellsSize) {
		this.cellsSize = cellsSize;
	}

	public List<Cell> getCells() {
		return cells;
	}

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}

	public void generateRandomCellTowers(int cellTowers, int measurementsPerCell) {
		
		for(int i = 0; i < cellTowers; i++) {
			
			Cell cellTower = Cell.generateCellWithRandomMeasurements(measurementsPerCell);
			
			this.cells.add(cellTower);
		}
		
		return;
	}

	@Override
	public String toString() {
		String s = "";
		for(Cell cell : this.getCells()) {
			s += cell.toString();
			s += "\n\n";
		}
		return s;
	}

	public static void main(String[] args) {
		CoordinateSystem coordinateSystem = new CoordinateSystem(10, 10);
		coordinateSystem.generateRandomCellTowers(3, 4);
		
		System.out.println(coordinateSystem.toString());
		
		return;
	}

}
