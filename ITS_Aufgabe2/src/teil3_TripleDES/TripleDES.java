package teil3_TripleDES;

/**
 * 3. TripleDES als Blockchiffre 
 * 
 * a. Implementieren Sie in JAVA ein Programm TripleDES mit der Aufgabe, 
 * alle Bytes einer beliebigen Datei mit dem TripleDES-Verfahren zu 
 * verschlüsseln oder zu entschlüsseln. Ihr Programm soll mit drei 
 * verschiedenen DES-Schlüsseln arbeiten und in der Reihenfolge 
 * EDE (encrypt-decrypt-encrypt) das DES-Verfahren dreimal hintereinander
 * durchlaufen. Implementieren Sie als Blockchiffre-Betriebsart1 den CFB-Modus.  
 * 
 * Kommandozeilen-Parameter: 
 * 	1. Dateiname einer zu verschlüsselnden/entschlüsselnden Datei 
 * 	2. Dateiname einer Schlüssel-Datei mit folgendem Inhalt: 
 * 		Byte 1-24: 24 Schlüsselbytes (3 DES-Schlüssel à 8 Byte, wobei von jedem  Byte jeweils 7 Bit verwendet werden) 
 * 		Byte 25-32: 8 Bytes für den Initialisierungsvektor zum Betrieb im CFB - Modus 
 * 	3. Dateiname der Ausgabedatei 
 * 	4. Status-String zur Angabe der gewünschten Operation:  
 * 		encrypt – Verschlüsselung der Datei 
 * 		decrypt – Entschlüsselung der Datei 
 *
 *
 * b. Testen Sie Ihre Implementierung, indem Sie die Datei „3DESTest.enc“ 
 * entschlüsseln (Tipp: Ergebnis ist eine PDF-Datei). Schlüssel und 
 * Initialisierungsvektor finden Sie in der Datei „3DESTest.key“ . 
 * 
 *  # Benutzen Sie als DES-Implementierung die mitgelieferte Datei DES.java ! 
 *  
 *  # Beispielcode zum blockweisen Kopieren einer Datei: 
 *  	FileInputStream in; 
 *    	FileOutputStream out; 
 *     	byte[] buffer = new byte[8]; 
 *      int len; 
 *      while ((len = in.read(buffer)) > 0) { 
 *      	out.write(buffer, 0, len); 
 *      }
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class TripleDES {
	private byte[] desKey1 = new byte[8];
	private byte[] desKey2 = new byte[8];
	private byte[] desKey3 = new byte[8];
	private byte[] iv = new byte[8];

	private static final String LOCATION = "src/teil3_TripleDES/Files/";

	public TripleDES(String messageFilename, String key, String chiffreFilename, String mode) {
		try {

			FileInputStream message = new FileInputStream(LOCATION + messageFilename);
			FileOutputStream chiffre = new FileOutputStream(LOCATION + chiffreFilename);

			readKeyFile(key);

			byte[] c = ede(iv);

			byte[] m = new byte[8];

			// in 8-Byte Blöcke zerlegen
			while ((message.read(m)) > 0) {

				// Stromchiffre
				for (int j = 0; j < 8; j++) {
					c[j] = (byte) (m[j] ^ c[j]);
				}

				chiffre.write(c, 0, 8);

				if (mode.equals("Encrypt")) {
					c = ede(c);

				} else if (mode.equals("Decrypt")) {
					c = ede(m);
				}
			}

			message.close();
			chiffre.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	private void readKeyFile(String keyFile) throws Exception {
		FileInputStream fis = new FileInputStream(LOCATION + keyFile);

		fis.read(desKey1);
		fis.read(desKey2);
		fis.read(desKey3);
		fis.read(iv);

		fis.close();
	}

	private byte[] ede(byte[] source) {
		byte[] dest = new byte[8];

		DES step1 = new DES(desKey1);
		step1.encrypt(source, 0, dest, 0);

		DES step2 = new DES(desKey2);
		step2.decrypt(dest, 0, source, 0);

		DES step3 = new DES(desKey3);
		step3.encrypt(source, 0, dest, 0);

		return dest;
	}
}