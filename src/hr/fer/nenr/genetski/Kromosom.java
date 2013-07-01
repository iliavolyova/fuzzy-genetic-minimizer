package hr.fer.nenr.genetski;

import java.util.ArrayList;
import java.util.Random;

import hr.fer.nenr.fuzzy.Parametri;

public class Kromosom implements Comparable<Kromosom> {

	double fitnes;
	Parametri varijable;
	
	public Kromosom() {
		this.fitnes = 0;
		this.varijable = new Parametri();
	}
	
	public Kromosom(Random gen){
		this.fitnes = 0;
		this.varijable = new Parametri(gen);
	}
	
	public Kromosom(ArrayList<Double> params){
		varijable = new Parametri(params);
	}

	@Override
	public int compareTo(Kromosom o) {
		double fitJa = Math.abs(this.fitnes);
		double fitOn = Math.abs(o.fitnes);
		
		if(fitJa < fitOn) {
			return -1;
		}
		if(fitJa > fitOn) {
			return 1;
		}
		return 0;
	}
}
