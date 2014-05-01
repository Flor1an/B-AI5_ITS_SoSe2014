package teil3_ReceiveSecureFile;

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
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 3.�Entschl�sseln�und�Verifizieren�eines�geheimen�Sitzungsschl�ssels�und�
 * Entschl�sseln�eines�Dokuments�(Empf�nger)�
 * Schreiben�Sie�ein�JAVA-Programm�RSF
 * �(�ReceiveSecureFile�)�mit�folgender�Funktionalit�t:� a)
 * Einlesen�eines��ffentlichen
 * �RSA-Schl�ssels�aus�einer�Datei�gem�ߠAufgabenteil�1.� b)
 * Einlesen�eines�privaten�RSA-Schl�ssels�aus�einer�Datei�gem�ߠAufgabenteil�1.�
 * c) Einlesen�einer�.ssf-Datei�gem�ߠAufgabenteil�2,�Entschl�sselung�des�
 * geheimen�Schl�ssels�mit� dem�privaten�
 * RSA-Schl�ssel,�Entschl�sselung�der�Dateidaten
 * �mit�dem�geheimen�Schl�ssel�(AES)�sowie�Erzeugung�einer�
 * Klartext-Ausgabedatei.�� d)
 * �berpr�fung�der�Signatur�f�r�den�geheimen�Schl�ssel
 * �aus�c)�mit�dem��ffentlichen�RSA-Schl�ssel�(Algorithmus:��SHA1withRSA�)� �
 * 
 * Die�Dateinamen�sollen�als�Argument�in�der�Kommandozeile��bergeben�werden.�
 * Als��ffentlicher�Schl�ssel�ist�der�Schl�ssel�des�Senders�zu�verwenden,�als�
 * privater�Schl�ssel�derjenige� des�Empf�ngers.� �
 * Beispiel�(F.�Meier�empf�ngt�von�K.�M�ller): java RSF FMeier.prv KMueller.pub
 * Brief.ssf Brief.pdf
 */

public class ReceiveSecureFile {
	String privateFilePath = "";
	String publicFilePath = "";
	String encryptedFilePath = "";
	String documentFilePath = "";

	PublicKey pubRSAKey = null;
	PrivateKey prvRSAKey = null;
	SecretKey decryptedAESKey = null;
	byte[] encryptedsecretAESKey = null;
	byte[] signatureBlock = null;
	byte[] encryptedDocument = null;
	byte[] decryptedDocument = null;

	private enum RSAKeyType {
		PRIVATE, PUBLIC;
	}

	public static void main(String[] args) {
		ReceiveSecureFile rsf = new ReceiveSecureFile("src/Files/Florian.prv", "src/Files/Florian.pub", "src/Files/Florian.ssf",
				"src/Files/decryptedITSAufgabe3.pdf");
		rsf.start();
		// ReceiveSecureFile rsf = new ReceiveSecureFile(
		// "src/Files/MHuebner.prv",
		// "src/Files/MHuebner.pub",
		// "src/Files/MHuebner.ssf",
		// "src/Files/decryptedRSFTest.class");
		// rsf.start();
	}

	ReceiveSecureFile(String privateFilePath, String publicFilePath, String encryptedFilePath, String documentFilePath) {
		this.privateFilePath = privateFilePath;
		this.publicFilePath = publicFilePath;
		this.encryptedFilePath = encryptedFilePath;
		this.documentFilePath = documentFilePath;

	}

	public void start() {
		// a)
		// Einlesen�eines��ffentlichen�RSA-Schl�ssels�aus�einer�Datei�gem�ߠAufgabenteil�1.�
		readRSAKeyFile(publicFilePath, RSAKeyType.PUBLIC);
		// b)
		// Einlesen�eines�privaten�RSA-Schl�ssels�aus�einer�Datei�gem�ߠAufgabenteil�1.�
		readRSAKeyFile(privateFilePath, RSAKeyType.PRIVATE);
		// c)
		// Einlesen�einer�.ssf-Datei�gem�ߠAufgabenteil�2,�
		readSSF();
		// Entschl�sselung�des�geheimen�Schl�ssels�mit�dem�privaten�RSA-Schl�ssel,�
		decryptSecretKey();
		// Entschl�sselung�der�Dateidaten�mit�dem�geheimen�Schl�ssel�(AES)�
		decryptDocument();
		// sowie�Erzeugung�einer�Klartext-Ausgabedatei.��
		writePlaintext();
		// d)
		// �berpr�fung�der�Signatur�f�r�den�geheimen�Schl�ssel�aus�c)�mit�dem��ffentlichen�RSA-Schl�ssel�
		// (Algorithmus:��SHA1withRSA�)
		verifySignature();
	}

	private void readRSAKeyFile(String filePath, RSAKeyType keyType) {
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(new File(filePath)));

			// L�nge des Inhaber-Namens (integer)
			int nameLength = dis.readInt();
			// Inhaber-Name (Bytefolge)
			byte[] name = new byte[nameLength];
			dis.read(name);
			// L�nge des privaten Schl�ssels (integer)
			int keyLenght = dis.readInt();
			byte[] key = new byte[keyLenght];
			dis.read(key);

			if (keyType == RSAKeyType.PRIVATE) {
				try {
					PKCS8EncodedKeySpec prvKeySpec = new PKCS8EncodedKeySpec(key);
					KeyFactory keyFactory = KeyFactory.getInstance("RSA");
					prvRSAKey = keyFactory.generatePrivate(prvKeySpec);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			} else if (keyType == RSAKeyType.PUBLIC) {
				try {
					X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(key);
					KeyFactory keyFactory;
					keyFactory = KeyFactory.getInstance("RSA");
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

	private void readSSF() {
		try {
			DataInputStream dis = null;
			dis = new DataInputStream(new FileInputStream(new File(encryptedFilePath)));

			int length = dis.readInt();
			encryptedsecretAESKey = new byte[length];
			dis.read(encryptedsecretAESKey);

			length = dis.readInt();
			signatureBlock = new byte[length];
			dis.read(signatureBlock);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			int temp = 0;
			while ((temp = dis.read()) >= 0) {
				baos.write(temp);
			}

			encryptedDocument = baos.toByteArray();

			dis.close();

		} catch (Exception e) {
			System.err.println(e.getMessage());

		}
	}

	private void decryptSecretKey() {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, prvRSAKey);
			byte[] decSkeyByte = cipher.doFinal(encryptedsecretAESKey);
			decryptedAESKey = new SecretKeySpec(decSkeyByte, "AES");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void decryptDocument() {

		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, decryptedAESKey);
			decryptedDocument = cipher.doFinal(encryptedDocument);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void writePlaintext() {
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(documentFilePath)));
			dos.write(decryptedDocument);
			dos.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void verifySignature() {

		try {
			Signature signer = Signature.getInstance("SHA1withRSA");
			signer.initVerify(pubRSAKey);
			signer.update(decryptedAESKey.getEncoded());
			if (!signer.verify(signatureBlock)) {
				System.err.println("ILLEGAL SIGNATURE");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());

		}
	}
}
