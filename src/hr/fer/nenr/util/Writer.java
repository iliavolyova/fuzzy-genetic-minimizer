package hr.fer.nenr.util;

import hr.fer.nenr.fuzzy.NeizrazitiSkup;
import hr.fer.nenr.fuzzy.Parametri;
import hr.fer.nenr.fuzzy.Sugeno;
import hr.fer.nenr.fuzzy.Sugeno.Tip;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Writer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			FileWriter fw = new FileWriter(new File("izlaz.txt"));
			Scanner s1 = new Scanner(new File("ulaz.txt"));
			Scanner s2 = new Scanner(new File("ulaz2.txt"));
			Scanner s3 = new Scanner(new File("ulaz1.txt"));
			
			double[] p = new double[9];
			double[] q = new double[9];
			double[] r = new double[9];
			
			Sugeno s = new Sugeno(Tip.KOMPLEKSAN);
			NeizrazitiSkup[] skupovi = new NeizrazitiSkup[6];
			
			int j = 0;
			while(s3.hasNextLine()){
				String[] line = s3.nextLine().split("\t");
				double w = Double.parseDouble(line[0]);
				double c = Double.parseDouble(line[1]);
				skupovi[j] = new NeizrazitiSkup(w, c);
				j++;
			}
			
			int i = 0;
			while(s2.hasNextLine()){
				
				
				String[] line = s2.nextLine().split("\t");
				p[i] = Double.parseDouble(line[0]);
				q[i] = Double.parseDouble(line[1]);
				r[i] = Double.parseDouble(line[2]);
				i++;
			}
			
			Parametri par = new Parametri(skupovi, p,q,r);
			
			while (s1.hasNextLine()){
				
				String[] line = s1.nextLine().split("\t");
				if(line.length == 1){
					fw.write("\n");
					continue;
				}
				
				double x = Double.parseDouble(line[0]);
				double y = Double.parseDouble(line[1]);
				
				double rez = s.izracunaj(par, new Uzorak(x, y, 0));
				
				fw.write(x + "\t" + y + "\t" + rez + "\n");
				
			}
			fw.close();
			s1.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

	}

}
