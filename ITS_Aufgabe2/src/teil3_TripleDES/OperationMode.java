package teil3_TripleDES;

/**
 * 3.�TripleDES�als�Blockchiffre�
 * 
 * a.�Implementieren�Sie�in�JAVA�ein�Programm�TripleDES�mit�der�Aufgabe,�
 * alle�Bytes�einer�beliebigen�Datei�mit�dem�TripleDES-Verfahren�zu�
 * verschl�sseln�oder�zu�entschl�sseln.�Ihr�Programm�soll�mit�drei�
 * verschiedenen�DES-Schl�sseln�arbeiten�und�in�der�Reihenfolge�
 * EDE�(encrypt-decrypt-encrypt) das�DES-Verfahren�dreimal�hintereinander
 * durchlaufen.�Implementieren�Sie�als�Blockchiffre-Betriebsart1 den�CFB-Modus.��
 * 
 * Kommandozeilen-Parameter:�
 * 	1. Dateiname�einer�zu�verschl�sselnden/entschl�sselnden�Datei�
 * 	2. Dateiname�einer�Schl�ssel-Datei�mit�folgendem�Inhalt:�
 * 		Byte�1-24:�24�Schl�sselbytes�(3�DES-Schl�ssel��8�Byte,�wobei�von�jedem��Byte�jeweils�7�Bit�verwendet�werden)�
 * 		Byte�25-32:�8�Bytes�f�r�den�Initialisierungsvektor�zum�Betrieb�im CFB�-�Modus�
 * 	3. Dateiname�der�Ausgabedatei�
 * 	4. Status-String�zur�Angabe�der�gew�nschten�Operation:��
 * 		encrypt���Verschl�sselung�der�Datei�
 * 		decrypt���Entschl�sselung�der�Datei�
 *
 *
 * b.�Testen�Sie�Ihre�Implementierung,�indem�Sie�die�Datei��3DESTest.enc��
 * entschl�sseln�(Tipp:�Ergebnis�ist�eine�PDF-Datei).�Schl�ssel�und�
 * Initialisierungsvektor�finden�Sie�in�der�Datei��3DESTest.key��.�
 * 
 *  # Benutzen�Sie�als�DES-Implementierung�die�mitgelieferte�Datei�DES.java !�
 *  
 *  # Beispielcode�zum�blockweisen�Kopieren�einer�Datei:�
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
