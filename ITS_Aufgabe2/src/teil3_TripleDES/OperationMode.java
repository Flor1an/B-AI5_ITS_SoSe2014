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

public enum OperationMode {
	ENCRYPT,
	DECRYPT
}
