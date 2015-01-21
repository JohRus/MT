package testdata;

import infrastructure.Cell;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Cell[] cells = chooseInput();
		for(int i = 0; i < cells.length; i++) {
			System.out.println(cells[0].toString());
		}
		

	}

	private static Cell[] chooseInput() {
		Scanner s = new Scanner(System.in);
		System.out.println("Press 1 to use input from file");
		System.out.println("Press 2 to use generated input");
		int input = s.nextInt();
		if(input == 1) {
			 return chooseInputFromFile();
		}
		else {
			return chooseGeneratedInput();
		}
	}

	private static Cell[] chooseGeneratedInput() {
		return null;
		
	}

	private static Cell[] chooseInputFromFile() {
		String fileName = "OneSector_1.txt";
		return IO.readFromFile(fileName);
	}

}
