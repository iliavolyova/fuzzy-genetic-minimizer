package hr.fer.nenr.fuzzy;

public class NeizrazitiSkup {
	
	private double w;
	private double c;
	
	public NeizrazitiSkup(){}
	
	public NeizrazitiSkup(double w, double c){
		this.w = w;
		this.c = c;
	}
	
	public double getPripadnost(double x){		
		if (x < c - w)
			return 0;
		else if (x > w + c)
			return 0;
		else if (x > c - w && x < c)
			return (-1.0/w) * (x-c+w);
		else 
			return (1.0/-w) * (x-c-w);
				  
	}

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}

	public double getC() {
		return c;
	}

	public void setC(double c) {
		this.c = c;
	}
}