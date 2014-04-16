package teil2_Stromchiffre;

/**
 * 2. Stromchiffre   
 * 
 * a. Schreiben Sie unter Verwendung der LCG – Klasse aus Teil1 
 * ein JAVA-Programm „HC1“ („HAW- Chiffre 1“),  welches als Eingabeparameter von
 * der Standardeingabe einen numerischen Schlüssel (Startwert) sowie den Pfad 
 * für eine zu verschlüsselnde / entschlüsselnde Datei erhält. Ihr Programm 
 * soll jedes Byte der Datei mit einem – ausgehend vom übergebenen Startwert 
 * – „zufällig“ erzeugten Schlüsselbyte mittels XOR verknüpfen und das Ergebnis 
 * in eine neue Chiffredatei ausgeben.  
 * 
 * b. Testen Sie Ihre Stromchiffre „HC1“, indem Sie eine Klartextdatei 
 * verschlüsseln und die erzeugte Chiffredatei anschließend durch einen 
 * nochmaligen Aufruf von HC1 wieder entschlüsseln. Verifizieren Sie 
 * (z.B. mittels „diff“), dass beide Dateien identische Inhalte besitzen.   
 * Arbeiten Sie mit Input/Outputstreams und vermeiden Sie die Verwendung von 
 * „Buffered Reader“  oder „Buffered Writer“ – Klassen! 
 */

import java.io.File;

public class _run {

	private static final long x0 = 111;
	private static final String message = "Message.txt";
	private static final String chiffre = "Chiffre.txt";

	public static void main(String[] args) {
		HC1 prog = new HC1(x0, message, chiffre);

		prog.encryt();
		// prog.decrypt();
	}

}
