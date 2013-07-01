package hr.fer.nenr.fuzzy;

import hr.fer.nenr.util.Uzorak;

public class Sugeno {
	
	private Parametri par;
	private Tip tip;
	
	public enum Tip {JEDNOSTAVAN, KOMPLEKSAN};
	
	public Sugeno(Tip tip){
		this.tip = tip;
	}
	
	public double apply(double x, double y){
		double andValue;
		int index = 0;
		double brojnik = 0, nazivnik = 0;
		
		if (tip == Tip.JEDNOSTAVAN){
			x = 0;
			y = 0;
		}
		
		double[] p = par.getP();
		double[] q = par.getQ();
		double[] r = par.getR();
		NeizrazitiSkup[] skupovi = par.getSkupovi();
		
		for(int i = 0; i < 3; i++)
			for (int j = 3; j < 6; j++){
				andValue = einsteinAnd(skupovi[i], skupovi[j], x , y);
				brojnik += andValue * (p[index]*x + q[index]*y + r[index]);
				nazivnik += andValue;
				index++;
			}
		
		if(nazivnik == 0)
			return 0;
		
		return brojnik/nazivnik;
	}
	
	public double izracunaj(Parametri p, Uzorak u){
		par = p;
		return apply(u.getX(), u.getY());
	}

	public double einsteinAnd(NeizrazitiSkup a, NeizrazitiSkup b, double ulazX, double ulazY){
		double miA = a.getPripadnost(ulazX);
		double miB = b.getPripadnost(ulazY);
		
		double and = (miA * miB)/(2 - (miA + miB - miA * miB));
		return and;
	}
	
}