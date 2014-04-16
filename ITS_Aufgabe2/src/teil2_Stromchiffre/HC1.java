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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import teil1_Pseudozufallszahlengenerierung.LCG;

public class HC1 {
	private static final String LOCATION = "src/teil2_Stromchiffre/Files/";

	private static long x0;
	private static String messageFilename;
	private static String chiffreFilename;

	public HC1(long x0, String messageFilename, String chiffreFilename) {
		HC1.x0 = x0;
		HC1.messageFilename = messageFilename;
		HC1.chiffreFilename = chiffreFilename;
	}

	public void encryt() {
		System.out.println("Starting encryption...");
		
		crypt(x0, messageFilename, chiffreFilename);
		
		System.out.println("encrypted.");
	}

	public void decrypt() {
		System.out.println("Starting decryption...");
		
		crypt(x0, chiffreFilename, messageFilename);
		
		System.out.println("decrypted.");
	}

	private void crypt(long x0, String inputFilename, String outputFilename) {
		try {

			LCG lcg = new LCG(x0);

			FileInputStream message = new FileInputStream(LOCATION + inputFilename);

			FileOutputStream chiffre = new FileOutputStream(LOCATION + outputFilename);

			long m;
			while (message.available() > 0) {
				m = message.read();
				m = m ^ lcg.nextValue(); //m XOR k
				chiffre.write((int) m);
			}
			
			message.close();
			chiffre.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
