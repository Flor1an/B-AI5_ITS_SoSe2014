package teil1_Pseudozufallszahlengenerierung;

/**
 * 1.�Pseudozufallszahlengenerierung
 * 
 * Implementieren�Sie�in�JAVA�einen�Pseudozufallszahlengenerator�nach�der
 * Linearen�Kongruenzmethode!�Stellen�Sie�eine�Klasse�LCG�mit�einer�
 * Methode�nextValue()�zur�Verf�gung.�Diese�Methode�soll nach�der�
 * linearen�Kongruenzmethode�einen��Zufallswert��liefern.�Der�Startwert�
 * des�Pseudozufallzahlengenerators�(X0)�soll�dem�Konstruktor�der�
 * LCG���Klasse�als�Parameter��bergeben�werden. Verwenden�Sie�eine�
 * Parameterkombination�f�r�a,�b�und�N�aus�der�
 * Datei��LinearerKongruenzgenerator-Infos.pdf�! Achten�Sie�grunds�tzlich�
 * auf�die�Verwendung eines�geeigneten�Datentyps�(z.B.��long�),�um�
 * m�gliche��berl�ufe�zu�vermeiden!�
 * 
 */

public class main {

	public static void main(String[] args) {
		LCG lcg = new LCG(111);
		for (int i = 0; i < 10; i++) {
			System.err.println(lcg.nextValue());
		}
	}

}
