package hr.fer.nenr.fuzzy;

import java.util.ArrayList;
import java.util.Random;

public class Parametri {

	private Random generator;

	private NeizrazitiSkup[] skupovi;
	private double p[];
	private double q[];
	private double r[];

	private double parMin = -100;
	private double parMax = 100;
	private double skupMin = 0;
	private double skupMax = 10;

	private boolean simple = false;



	public Parametri(){
		skupovi = new NeizrazitiSkup[6];
		p = new double[9];
		q = new double[9];
		r = new double[9];

		generator = new Random();
	}

	public Parametri(Random generator){
		this();

		this.generator = generator;

		double maxMin = (skupMax - skupMin);
		double[] wc;
		for(int i = 0; i < 6; i++){
			wc = generateWC();
			skupovi[i] = new NeizrazitiSkup(wc[0], wc[1]);
		}

		maxMin = (parMax - parMin);
		for (int i = 0; i < 9; i++){

			if (!simple){
				q[i] = maxMin * generator.nextDouble() - maxMin/2;
				p[i] = maxMin * generator.nextDouble() - maxMin/2;
			}
			r[i] = maxMin * generator.nextDouble() - maxMin/2;
		}
	}

	public Parametri(ArrayList<Double> params) {
		this();

		double w,c, temp;
		int j = 0, k = 0;
		for (int i = 0; i < 6; i++){
			w = params.get(k);
			c = params.get(k+1);

			if (w < 0 || c < 0 || c > 10) {
				double[] wc = generateWC();
				skupovi[i] = new NeizrazitiSkup(wc[0] , wc[1]);
				k += 2;
			} else {

				if (w > c){
					temp = c;
					c = w;
					w = temp;
				}
				if (c-w < 0){
					temp = c-w;
					w -= temp;
				}
				if (c+w > 10){
					temp = c+w - 10;
					w -= temp;
				}

				skupovi[i] = new NeizrazitiSkup(w , c);
				k += 2;
			}
		}

		if (simple) {
			for (int i = 12; i < params.size(); i++){
				p[j] = 0;
				q[j] = 0;
				r[j] = params.get(i);
				j++;
			}
		}else {
			for (int i = 12; i < params.size(); i = i+3){
				p[j] = params.get(i);
				q[j] = params.get(i+1);
				r[j] = params.get(i+2);
				j++;
			}

		}
	}

	public Parametri(NeizrazitiSkup[] skupovi, double[] p, double[] q, double[] r){
		this.p = p;
		this.q = q;
		this.r = r;
		this.skupovi = skupovi;
	}

	public ArrayList<Double> getParametri(){
		ArrayList<Double> rez = new ArrayList<Double>();

		for (int i = 0; i < skupovi.length; i++){
			rez.add(skupovi[i].getW());
			rez.add(skupovi[i].getC());
		}
		for (int i = 0; i < p.length; i++){
			if (!simple){
				rez.add(p[i]);
				rez.add(q[i]);
			}
			rez.add(r[i]);
		}
		return rez;
	}

	private double[] generateWC() {
		double c = generator.nextDouble()*10;
		double w = generator.nextDouble()*10;
		if (c-w < skupMin || c+w> skupMax)
			return generateWC();
		else {
			double[] wc = new double[2];
			wc[0] = w;
			wc[1] = c;
			return wc;
		}
	}

	public NeizrazitiSkup[] getSkupovi() {
		return skupovi;
	}

	public void setSkupovi(NeizrazitiSkup[] skupovi) {
		this.skupovi = skupovi;
	}

	public double[] getP() {
		return p;
	}

	public void setP(double[] p) {
		this.p = p;
	}

	public double[] getQ() {
		return q;
	}

	public void setQ(double[] q) {
		this.q = q;
	}

	public double[] getR() {
		return r;
	}

	public void setR(double[] r) {
		this.r = r;
	}


	public String toString(){
		StringBuilder sb = new StringBuilder("\nParametri\n");

		for (int i = 0; i < skupovi.length; i++){
			sb.append(skupovi[i].getW() + "\t" + skupovi[i].getC() + "\n");
		}

		for (int i = 0; i < p.length; i++){
			sb.append(p[i] + "\t" + q[i] + "\t" + r[i] + "\n");
		}

		return sb.toString();
	}

}
