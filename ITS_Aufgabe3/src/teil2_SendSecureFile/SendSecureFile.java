package teil2_SendSecureFile;

/**
 * 2. Erzeugen, Signieren und Verschlüsseln eines geheimen Sitzungsschlüssels und Verschlüsseln einer Dokumentendatei (Sender) 
 * Schreiben Sie ein JAVA-Programm SSF („SendSecureFile“) mit folgender Funktionalität:  
 * a) Einlesen eines privaten RSA-Schlüssels (.prv) aus einer Datei gemäß Aufgabenteil 1. 
 * b) Einlesen eines öffentlichen RSA-Schlüssels (.pub) aus einer Datei gemäß Aufgabenteil 1.  
 * c) Erzeugen eines geheimen Schlüssels für den AES-Algorithmus mit der Schlüssellänge 128 Bit 
 * d) Erzeugung einer Signatur für den geheimen Schlüssel aus c) mit dem privaten RSA-Schlüssel  (Algorithmus: „SHA1withRSA“) 
 * e) Verschlüsselung des geheimen Schlüssel aus c) mit dem öffentlichen RSA-Schlüssel (Algorithmus:  „RSA“) 
 * f) Einlesen einer Dokumentendatei und Verschlüsseln der Datei mit dem symmetrischen AES-Algorithmus (geheimer Schlüssel aus c). 
 * g) Erzeugung einer Ausgabedatei mit folgender Struktur:  
 * 1. Länge des verschlüsselten geheimen Schlüssels (integer)  
 * 2. Verschlüsselter geheimer Schlüssel (Bytefolge) 
 * 3. Länge der Signatur des geheimen Schlüssels (integer) 
 * 4. Signatur des geheimen Schlüssels (Bytefolge)  
 * 5. Verschlüsselte Dateidaten (Ergebnis von f) (Bytefolge) 
 *  
 * Die Dateinamen sollen als Argument in der Kommandozeile übergeben werden. Als privater Schlüssel ist derjenige des Senders 
 * zu verwenden, als öffentlicher Schlüssel derjenige des Empfängers. 
 *  
 * Beispiel (K. Müller sendet an F. Meier): java SSF KMueller.prv FMeier.pub Brief.pdf Brief.ssf
 */

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SendSecureFile {
	String privateFilePath = "";
	String publicFilePath = "";
	String documentFilePath = "";
	String encryptedFilePath = "";

	PublicKey pubRSAKey = null;
	PrivateKey prvRSAKey = null;
	SecretKey secretAESKey = null;
	byte[] signatureBlock = null;
	byte[] encryptedsecretAESKey = null;
	byte[] encryptedDocument = null;

	private enum RSAKeyType {
		PRIVATE, PUBLIC;
	}

	public static void main(String[] args) {
		SendSecureFile ssf = new SendSecureFile(
				"src/Files/Florian.pub", 
				"src/Files/Florian.prv", 
				"src/Files/ITSAufgabe3.pdf",
				"src/Files/Florian.ssf");
		ssf.start();

		// SendSecureFile ssf = new SendSecureFile(
		// "src/Files/MHuebner.pub",
		// "src/Files/MHuebner.prv",
		// "src/Files/RSFTest.class",
		// "src/Files/MHuebner.ssf");
		// ssf.start();
	}

	public SendSecureFile(String publicFilePath, String privateFilePath, String documentFilePath, String encryptedFilePath) {
		this.publicFilePath = publicFilePath;
		this.privateFilePath = privateFilePath;
		this.documentFilePath = documentFilePath;
		this.encryptedFilePath = encryptedFilePath;
	}

	public void start() {
		// a)
		// Einlesen eines privaten RSA-Schlüssels (.prv) aus einer Datei gemäß Aufgabenteil 1
		readRSAKeyFile(privateFilePath, RSAKeyType.PRIVATE);
		// b)
		// Einlesen eines öffentlichen RSA-Schlüssels (.pub) aus einer Datei gemäß Aufgabenteil 1.
		readRSAKeyFile(publicFilePath, RSAKeyType.PUBLIC);
		// c)
		// Erzeugen eines geheimen Schlüssels für den AES-Algorithmus mit der Schlüssellänge 128 Bit 
		createAESKey(128);
		// d)
		// Erzeugung einer Signatur für den geheimen Schlüssel aus c) mit dem privaten RSA-Schlüssel 
		// (Algorithmus: „SHA1withRSA“) 
		createSignature();
		// e)
		// Verschlüsselung des geheimen Schlüssel aus c) mit dem öffentlichen RSA-Schlüssel 
		// (Algorithmus: „RSA“)
		encryptSecretKey();
		// f)
		// Einlesen einer Dokumentendatei und Verschlüsseln der Datei mit dem symmetrischen
		// AES-Algorithmus (geheimer Schlüssel aus c).
		readDocumentAndEncrypt();
		// g) Erzeugung einer Ausgabedatei mit folgender Struktur: 
		// 1. Länge des verschlüsselten geheimen Schlüssels (integer) 
		// 2. Verschlüsselter geheimer Schlüssel (Bytefolge) 
		// 3. Länge der Signatur des geheimen Schlüssels (integer) 
		// 4. Signatur des geheimen Schlüssels (Bytefolge) 
		// 5. Verschlüsselte Dateidaten (Ergebnis von f) (Bytefolge) 
		writeEncryptedFile();
	}

	private void readRSAKeyFile(String filePath, RSAKeyType keyType) {
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(new File(filePath)));

			// Länge des Inhaber-Namens (integer)
			int nameLength = dis.readInt();
			// Inhaber-Name (Bytefolge)
			byte[] name = new byte[nameLength];
			dis.read(name);
			// Länge des privaten Schlüssels (integer)
			int keyLenght = dis.readInt();
			byte[] key = new byte[keyLenght];
			dis.read(key);

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			if (keyType == RSAKeyType.PRIVATE) {
				try {
					PKCS8EncodedKeySpec prvKeySpec = new PKCS8EncodedKeySpec(key);
					prvRSAKey = keyFactory.generatePrivate(prvKeySpec);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			} else if (keyType == RSAKeyType.PUBLIC) {
				try {
					X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(key);
					pubRSAKey = keyFactory.generatePublic(pubKeySpec);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}

			dis.close();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void createAESKey(int keyLength) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");

			kgen.init(keyLength);
			secretAESKey = kgen.generateKey();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void createSignature() {
		try {
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(prvRSAKey);
			signature.update(secretAESKey.getEncoded());
			signatureBlock = signature.sign();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void encryptSecretKey() {

		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pubRSAKey);
			byte[] plain = secretAESKey.getEncoded();
			encryptedsecretAESKey = cipher.doFinal(plain);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void readDocumentAndEncrypt() {
		DataInputStream dis = null;
		ByteArrayOutputStream baos = null;
		try {
			dis = new DataInputStream(new FileInputStream(new File(documentFilePath)));

			baos = new ByteArrayOutputStream();

			int temp = 0;
			while ((temp = dis.read()) >= 0) {
				baos.write(temp);
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());

		}

		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretAESKey);
			encryptedDocument = cipher.doFinal(baos.toByteArray());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	private void writeEncryptedFile() {
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(encryptedFilePath)));

			dos.writeInt(encryptedsecretAESKey.length);
			dos.write(encryptedsecretAESKey);
			dos.writeInt(signatureBlock.length);
			dos.write(signatureBlock);
			dos.write(encryptedDocument);
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
