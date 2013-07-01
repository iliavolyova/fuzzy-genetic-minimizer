package hr.fer.nenr.genetski;

import hr.fer.nenr.fuzzy.Parametri;

/**
 * Sučelje koje opisuje funkciju koja se
 * optimira.
 * 
 * @author marcupic
 */
public interface IFunkcija {

	/**
	 * Metoda na temelju varijabli računa vrijednost
	 * funkcije.
	 * 
	 * @param varijable varijable
	 * @return vrijednost funkcije
	 */
	public double izracunaj(Parametri p);
}
