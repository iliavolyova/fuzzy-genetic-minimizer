package hr.fer.nenr.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class InputReader {

	private Scanner scan;
	
	public InputReader(String filename){
		try {
			scan = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Uzorak> readInputs(){
		
		ArrayList<Uzorak> uzorci = new ArrayList<Uzorak>();
		double rez;
		double x, y;
		
		while(scan.hasNextLine()){
			String[] line = scan.nextLine().split("\t");
			
			if (line.length == 1)
				continue;
			
			x = Double.parseDouble(line[0]);
			y = Double.parseDouble(line[1]);
			rez = Double.parseDouble(line[2]);
			
			uzorci.add(new Uzorak(x, y, rez));
		}
		return uzorci;
	}
}
