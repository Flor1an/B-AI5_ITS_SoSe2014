package teil2_Stromchiffre;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import teil1_Pseudozufallszahlengenerierung.LCG;

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
 * 
 */


public class HC1 {
	private static long x0;
	private static File messagePath;
	private static File chiffrePath;

	public HC1(long x0, File message, File chiffre) {
		HC1.x0 = x0;
		HC1.messagePath = message;
		HC1.chiffrePath = chiffre;
	}

	public void encryt() {
		System.out.println("Starting encryption...");
		
		crypt(x0, messagePath, chiffrePath);
		
		System.out.println("encrypted.");

	}

	public void decrypt() {
		System.out.println("Starting decryption...");
		
		crypt(x0, chiffrePath, messagePath);
		
		System.out.println("decrypted.");
	}

	private void crypt(long x0, File inputPath, File outputPath) {
		try {

			LCG lcg = new LCG(x0);

			FileInputStream input = new FileInputStream(inputPath);

			FileOutputStream output = new FileOutputStream(outputPath);

			long m;
			while (input.available() > 0) {
				m = input.read();
				m = m ^ lcg.nextValue(); //m XOR k
				output.write((int) m);
			}
			
			input.close();
			output.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
