package teil1_Pseudozufallszahlengenerierung;

/**
 * 1. Pseudozufallszahlengenerierung
 * 
 * Implementieren Sie in JAVA einen Pseudozufallszahlengenerator nach der
 * Linearen Kongruenzmethode! Stellen Sie eine Klasse LCG mit einer 
 * Methode nextValue() zur Verfügung. Diese Methode soll nach der 
 * linearen Kongruenzmethode einen „Zufallswert“ liefern. Der Startwert 
 * des Pseudozufallzahlengenerators (X0) soll dem Konstruktor der 
 * LCG – Klasse als Parameter übergeben werden. Verwenden Sie eine 
 * Parameterkombination für a, b und N aus der 
 * Datei „LinearerKongruenzgenerator-Infos.pdf“! Achten Sie grundsätzlich 
 * auf die Verwendung eines geeigneten Datentyps (z.B. „long“), um 
 * mögliche Überläufe zu vermeiden! 
 * 
 */

public class LCG {

	// Referenc BASIC
	private static final long a = 214013;
	private static final long b = 13523655;
	private static final long N = (long) Math.pow(2.0, 24);

	private long x;

	public LCG(long x0) {
		this.x = x0 % N;
	}

	public long nextValue() {
		return x = (a * x + b) % N;
	}


}
