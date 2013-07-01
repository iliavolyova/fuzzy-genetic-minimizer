package hr.fer.nenr.util;

public class Uzorak {

	private double x;
	private double y;
	private double z;
	
	public Uzorak(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
	
	public String toString(){
		return x + "\t" + y + "\t " + z;
	}
}
