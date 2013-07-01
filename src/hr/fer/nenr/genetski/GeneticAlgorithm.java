package hr.fer.nenr.genetski;

import hr.fer.nenr.fuzzy.Parametri;
import hr.fer.nenr.fuzzy.Sugeno;
import hr.fer.nenr.fuzzy.Sugeno.Tip;
import hr.fer.nenr.util.InputReader;
import hr.fer.nenr.util.Uzorak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class GeneticAlgorithm {

	// Definiranje osnovnih parametara algoritma
	private int VEL_POP = 50;
	private double VJER_KRIZ = 1;
	private double VJER_MUT = 0.09;

	private Sugeno fuzzy;
	private ArrayList<Uzorak> uzorci;
	private static Random rand;
	private Kromosom najbolji, srednji;

	public GeneticAlgorithm(){
		fuzzy = new Sugeno(Tip.KOMPLEKSAN);
	}

	public void run() throws IOException {
		
		// Generator sluèajnih brojeva
		rand = new Random();
		najbolji = new Kromosom(rand);

		InputReader reader = new InputReader("ulaz.txt");
		uzorci = reader.readInputs();

		Kromosom[] populacija = stvoriPopulaciju(VEL_POP);
		Kromosom[] novaGeneracija = new Kromosom[populacija.length];
		for (int i = 0; i < populacija.length; i++)
			novaGeneracija[i] = new Kromosom();

		// Definiranje funkcije koju optimiramo
		IFunkcija funkcija = new IFunkcija(){
			@Override
			public double izracunaj(Parametri p) {

				double greska = 0.0;
				for (Uzorak sample : uzorci) {
					double izracunata = fuzzy.izracunaj(p, sample);
					double prava = sample.getZ();

					greska += (izracunata - prava)*(izracunata-prava);
				}
				return greska;
			}
		};

		// Poèetna evaluacija populacije
		evaluirajPopulaciju(populacija, funkcija);
		System.out.println("Pocetno rjesenje: "+funkcija.izracunaj(populacija[0].varijable));
		
		ArrayList<Kromosom> moguciOdabrani;
		for(int generacija = 0; generacija < 30000; generacija++) {

			Arrays.sort(populacija);
			srednji = racunajSrednji(populacija);
			moguciOdabrani = new ArrayList<Kromosom>();
			
			for(int i=0; i < VEL_POP/2; i++) {
				Kromosom roditelj1 = odaberiTurnirski(3, populacija, rand);
				Kromosom roditelj2 = odaberiRoditelja( populacija, rand);
				moguciOdabrani.add(roditelj1);
				moguciOdabrani.add(roditelj2);
				
				Kromosom[] djeca =  krizajUniformno(VJER_KRIZ, roditelj1, roditelj2, rand);
				moguciOdabrani.add(djeca[0]);
				moguciOdabrani.add(djeca[1]);
			}
				
			Collections.sort(moguciOdabrani);
			for (int j = 0;  j < novaGeneracija.length;){
				novaGeneracija[j++] = mutiraj(VJER_MUT, moguciOdabrani.get(j), rand);
			}
				
			// Zamijeni staru i novu populaciju
			Kromosom[] pomocni = populacija;
			populacija = novaGeneracija;
			novaGeneracija = pomocni;

			evaluirajPopulaciju(populacija, funkcija);

			for(int i = 0; i < populacija.length; i++) {
				if(i==0 || najbolji.fitnes>populacija[i].fitnes) {
					najbolji = populacija[i];
				}
			}
			if (generacija % 50 == 0)
				System.out.println(generacija + "\ttrenutno rjesenje: "+funkcija.izracunaj(najbolji.varijable));
			if (generacija % 2000 == 0)
				VJER_MUT += 0.001;
		}
		System.out.println("najbolje:" + funkcija.izracunaj(najbolji.varijable));
		System.out.println(najbolji.varijable.toString());
	}

	private Kromosom racunajSrednji(Kromosom[] populacija) {
		ArrayList<Double> paramsSvi = populacija[0].varijable.getParametri();
		ArrayList<Double> params;
		for (int j = 1; j < populacija.length; j++){
			 params = populacija[j].varijable.getParametri();
			 for (int i = 0; i < params.size(); i++){	
				 paramsSvi.set(i, paramsSvi.get(i) + params.get(i));
			 }
		}
		
		for(int i = 0; i < paramsSvi.size(); i++){
			 paramsSvi.set(i, paramsSvi.get(i)/populacija.length);
		}
		
		return new Kromosom(paramsSvi);

	}

	@SuppressWarnings("unused")
	private Kromosom kopiraj(Kromosom original) {
		ArrayList<Double> parOriginal = original.varijable.getParametri();
		ArrayList<Double> parKopija = new ArrayList<Double>();

		for (int i = 0; i < parOriginal.size(); i++)
			parKopija.add(parOriginal.get(i));

		Kromosom kopija = new Kromosom(parKopija);
		kopija.fitnes = original.fitnes;
		return kopija;
	}


	public static Kromosom[] stvoriPopulaciju(int brojJedinki) {
		Kromosom[] populacija = new Kromosom[brojJedinki];
		for(int i = 0; i < populacija.length; i++) {
			populacija[i] = new Kromosom(rand);
		}
		return populacija;
	}


	private static void evaluirajPopulaciju(Kromosom[] populacija, IFunkcija funkcija) {
		for(int i = 0; i < populacija.length; i++) {
			evaluirajJedinku(populacija[i], funkcija);
		}
	}

	private static void evaluirajJedinku(Kromosom kromosom, IFunkcija funkcija) {
		kromosom.fitnes = funkcija.izracunaj(kromosom.varijable);
	}

	private static Kromosom odaberiRoditelja(Kromosom[] populacija, Random rand) {
		double sumaDobrota = 0;
		double najvecaVrijednost = 0;
		for(int i = 0; i < populacija.length; i++) {
			sumaDobrota += populacija[i].fitnes;
			if(i==0 || najvecaVrijednost<populacija[i].fitnes) {
				najvecaVrijednost = populacija[i].fitnes;
			}
		}
		sumaDobrota = populacija.length * najvecaVrijednost - sumaDobrota; 
		double slucajniBroj = rand.nextDouble() * sumaDobrota;
		double akumuliranaSuma = 0;
		for(int i = 0; i < populacija.length; i++) {
			akumuliranaSuma += najvecaVrijednost - populacija[i].fitnes;
			if(slucajniBroj<akumuliranaSuma) return populacija[i];
		}
		return populacija[populacija.length-1];
	}
	
	private static Kromosom odaberiTurnirski(int k, Kromosom[] populacija, Random rand){
		int j;
		ArrayList<Kromosom> pobjednici = new ArrayList<Kromosom>();
		for (int i = 0; i < k; i++){
			j = rand.nextInt(populacija.length);
			pobjednici.add(populacija[j]);
		}
		Collections.sort(pobjednici);
		return pobjednici.get(0);
	}


	private Kromosom[] krizajUniformno(double vjerKriz, Kromosom roditelj1, 
			Kromosom roditelj2, Random rand) {
		Kromosom[] djeca = new Kromosom[2];

		if(rand.nextFloat() <= vjerKriz) {

			ArrayList<Double> parRoditelj1 = roditelj1.varijable.getParametri();
			ArrayList<Double> parRoditelj2 = roditelj2.varijable.getParametri();
			ArrayList<Double> parDijete1 = new ArrayList<Double>();
			ArrayList<Double> parDijete2 = new ArrayList<Double>();
			char[] uniformBinary = createBinaryString(parRoditelj1.size());

			//rekombinacija
			if (rand.nextFloat() <= 0.5){

				for (int i = 0; i < uniformBinary.length; i++){
					if(uniformBinary[i] == '1'){
						parDijete1.add(parRoditelj1.get(i));
						parDijete2.add(parRoditelj2.get(i));
					}else {
						parDijete1.add(parRoditelj2.get(i));
						parDijete2.add(parRoditelj1.get(i));
					}
				}
				//usrednjavanje parametara roditelja
			} else {
				for (int i = 0; i < uniformBinary.length; i++){
					if (uniformBinary[i] == '1'){
						parDijete1.add(0.4*parRoditelj1.get(i) + 0.6*parRoditelj2.get(i));
						parDijete2.add(0.6*parRoditelj1.get(i) + 0.4*parRoditelj2.get(i));
					}else {
						parDijete1.add(parRoditelj1.get(i));
						parDijete2.add(parRoditelj2.get(i));
					}
				}
			}


			djeca[0] = new Kromosom(parDijete1);
			djeca[1] = new Kromosom(parDijete2);
			return djeca;
		}
		djeca[0] = roditelj1;
		djeca[1] = roditelj2;
		return djeca;
	}


	private Kromosom mutiraj(double vjerMut, Kromosom dijete, Random rand) {
		ArrayList<Double> params = dijete.varijable.getParametri();
		double  trenutna;

		if (rand.nextFloat() < vjerMut) {
			double vj = rand.nextFloat();
			
			if (vj < 0.35){
				int i = rand.nextInt(params.size());
				trenutna = params.get(i);
				params.set(i, trenutna + (2*rand.nextFloat() - 1) * trenutna);
			}else if (vj < 0.7){
				int i = rand.nextInt(params.size());
				trenutna = params.get(i);
				params.set(i, trenutna + ((rand.nextFloat() - 0.5) * trenutna));
			}else {
				char[] bitovi = createBinaryString(params.size());
				for (int i = 0; i < bitovi.length; i++){
					if (bitovi[i] == '1'){
						trenutna = params.get(i);
						params.set(i, trenutna + ((rand.nextFloat() - 0.5) * trenutna));
					}
				}
			}
		}
		return new Kromosom(params);
	}
	
	private Kromosom mutirajGaussian(double vjerMut, Kromosom dijete, Random rand){
		ArrayList<Double> params = dijete.varijable.getParametri();
		double trenutna, vrijednost;
		if (rand.nextFloat() < vjerMut) {
			double vj = rand.nextFloat();
			double sigma;
			
			if (vj < 0.4){
				int i = rand.nextInt(params.size());
				trenutna = params.get(i);
				if (i < 12){
					sigma = 2;
					vrijednost =  ((rand.nextFloat()-0.5)*sigma) + srednji.varijable.getParametri().get(i);
				}else {
					sigma = 20;
					vrijednost =   ((rand.nextFloat()-0.5)*sigma) + srednji.varijable.getParametri().get(i);
				}
				params.set(i, vrijednost);
			}else {
				char[] bitovi = createBinaryString(params.size());
				for (int i = 0; i < bitovi.length; i++){
					if (bitovi[i] == '1'){
						trenutna = params.get(i);
						params.set(i, trenutna + (0.5*(rand.nextFloat() - 0.5) * trenutna));
					}
				}
			}
		}
		return new Kromosom(params);
	}

	private static char[] createBinaryString(int len){
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < len; i++)
			sb.append(rand.nextBoolean() ? 1:0);

		return sb.toString().toCharArray();
	}

	public static void main(String[] args){
		try {
			new GeneticAlgorithm().run();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}