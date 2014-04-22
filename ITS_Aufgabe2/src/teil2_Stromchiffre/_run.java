package teil2_Stromchiffre;

/**
 * 2.�Stromchiffre� �
 * 
 * a.�Schreiben�Sie�unter�Verwendung�der�LCG�-�Klasse�aus�Teil1�
 * ein�JAVA-Programm�"HC1"�("HAW- Chiffre�1"),��welches�als�Eingabeparameter�von
 * der�Standardeingabe�einen�numerischen�Schl�ssel�(Startwert)�sowie�den�Pfad�
 * f�r�eine�zu�verschl�sselnde�/�entschl�sselnde�Datei�erh�lt.�Ihr�Programm�
 * soll�jedes�Byte�der�Datei�mit�einem�-�ausgehend�vom��bergebenen�Startwert�
 * -�"zuf�llig"�erzeugten�Schl�sselbyte�mittels�XOR�verkn�pfen�und�das�Ergebnis�
 * in�eine�neue�Chiffredatei�ausgeben.��
 * 
 * b.�Testen�Sie�Ihre�Stromchiffre�"HC1",�indem�Sie�eine�Klartextdatei�
 * verschl�sseln�und�die�erzeugte�Chiffredatei�anschlie�end�durch�einen�
 * nochmaligen�Aufruf�von�HC1�wieder�entschl�sseln.�Verifizieren�Sie�
 * (z.B.�mittels�"diff"),�dass�beide�Dateien�identische�Inhalte�besitzen.� �
 * Arbeiten Sie�mit�Input/Outputstreams�und�vermeiden�Sie�die�Verwendung�von�
 * "Buffered Reader"� oder�"Buffered�Writer"�-�Klassen!�
 */

public class _run {

	public static void main(String[] args) {

		long x0 = 111;
        String message = "src/teil2_Stromchiffre/Files/Message.txt",
               chiffre = "src/teil2_Stromchiffre/Files/Chiffre.txt";

		HC1 prog = new HC1(x0);

		//prog.crypt(message, chiffre);
		prog.crypt(chiffre, message);
	}
}
