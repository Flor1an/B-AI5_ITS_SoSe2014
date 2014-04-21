package teil2_Stromchiffre;
/**
 * 2.�Stromchiffre� �
 * 
 * a.�Schreiben�Sie�unter�Verwendung�der�LCG���Klasse�aus�Teil1�
 * ein�JAVA-Programm��HC1��(�HAW- Chiffre�1�),��welches�als�Eingabeparameter�von
 * der�Standardeingabe�einen�numerischen�Schl�ssel�(Startwert)�sowie�den�Pfad�
 * f�r�eine�zu�verschl�sselnde�/�entschl�sselnde�Datei�erh�lt.�Ihr�Programm�
 * soll�jedes�Byte�der�Datei�mit�einem���ausgehend�vom��bergebenen�Startwert�
 * ���zuf�llig��erzeugten�Schl�sselbyte�mittels�XOR�verkn�pfen�und�das�Ergebnis�
 * in�eine�neue�Chiffredatei�ausgeben.��
 * 
 * b.�Testen�Sie�Ihre�Stromchiffre��HC1�,�indem�Sie�eine�Klartextdatei�
 * verschl�sseln�und�die�erzeugte�Chiffredatei�anschlie�end�durch�einen�
 * nochmaligen�Aufruf�von�HC1�wieder�entschl�sseln.�Verifizieren�Sie�
 * (z.B.�mittels��diff�),�dass�beide�Dateien�identische�Inhalte�besitzen.� �
 * Arbeiten Sie�mit�Input/Outputstreams�und�vermeiden�Sie�die�Verwendung�von�
 * �Buffered Reader�� oder��Buffered�Writer����Klassen!�
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;


import teil1_Pseudozufallszahlengenerierung.LCG;

public class HC1 {

	private long x0;

	public HC1(long x0) {
		this.x0 = x0;
	}


	public void crypt(String inputFile, String outputFile) {
		try {

			LCG lcg = new LCG(this.x0);
			FileInputStream input = new FileInputStream(inputFile);
			FileOutputStream output = new FileOutputStream(outputFile);

			long m;
			while (input.available() > 0) {
				m = input.read();
				m = m ^ lcg.nextValue(); //m XOR k
                output.write((int) m);
			}

            input.close();
            output.close();
            System.out.println("done");
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
