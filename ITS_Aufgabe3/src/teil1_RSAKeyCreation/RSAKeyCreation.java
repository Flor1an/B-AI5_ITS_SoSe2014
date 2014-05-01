package teil1_RSAKeyCreation;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 1. Erzeugen eines Schlüsselpaares 
 * Schreiben Sie ein JAVA-Programm RSAKeyCreation
 * , welches ein RSA-Schlüsselpaar erzeugt 
 * (Schlüssellänge: 2048 Bit) und beide 
 * Schlüssel jeweils in einer Datei speichert
 * . Der Name des Inhabers soll als Argument
 *  in der Kommandozeile übergeben werden.   
 * Der öffentliche Schlüssel soll einer
 *  einer Datei <Inhabername>.pub gespeichert
 *  werden, deren Struktur wie folgt aussieht:  1.
 * Länge des Inhaber-Namens (integer)  2. Inhaber-Name (Bytefolge)  3.
 * Länge des öffentlichen Schlüssels (integer)  4.
 * Öffentlicher Schlüssel (Bytefolge) [X.509-Format]   
 * Der private Schlüssel soll
 *  einer einer Datei <Inhabername>.prv gespeichert werden, deren Struktur 
 * wie folgt aussieht:  1. Länge des Inhaber-Namens (integer)  2.
 * Inhaber-Name (Bytefolge)  3. Länge des privaten Schlüssels (integer)  4.
 * Privater Schlüssel (Bytefolge) [PKCS8-Format]    Beispiel: java
 * RSAKeyCreation KMueller 
 * erzeugt die Ausgabedateien KMueller.pub  und  KMueller.prv
 * 
 */

public class RSAKeyCreation {
	static String LOCATION = "src/Files/";
	static String name;
	static int KEYLENGTH = 2048;

	private enum KeyKind {
		PUBLIC, PRIVATE
	}

	public static void main(String[] args) {
		String name = "";
		if (args.length < 1) {
			name = "Florian";
		} else {
			name = args[0];
		}

		RSAKeyCreation rsa = new RSAKeyCreation(name);
		rsa.generateRSA();
	}

	public RSAKeyCreation(String name) {
		RSAKeyCreation.name = name;
	}

	public void generateRSA() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(KEYLENGTH);

			KeyPair kp = kpg.genKeyPair();

			Key publicKey = kp.getPublic();
			Key privateKey = kp.getPrivate();

			writeKeyFile(publicKey, KeyKind.PUBLIC);
			writeKeyFile(privateKey, KeyKind.PRIVATE);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private void writeKeyFile(Key keyToWrite, KeyKind keyKind) {
		try {
			File f = null;
			if (keyKind == KeyKind.PRIVATE) {
				f = new File(LOCATION + name + ".prv");
			} else if (keyKind == KeyKind.PUBLIC) {
				f = new File(LOCATION + name + ".pub");
			}

			DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
			// 1. Länge des Inhaber-Namens (integer)
			dos.writeInt(name.length());
			// 2. Inhaber-Name (Bytefolge)
			dos.write(name.getBytes());
			// 3. Länge des öffentlichen Schlüssels (integer)
			dos.writeInt(keyToWrite.getEncoded().length);

			if (keyKind == KeyKind.PRIVATE) {
				// 4. Privater Schlüssel (Bytefolge) [PKCS8-Format]
				PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(keyToWrite.getEncoded());
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				PrivateKey prvKey = keyFactory.generatePrivate(pkcs8);
				dos.write(prvKey.getEncoded());
			} else if (keyKind == KeyKind.PUBLIC) {
				// 4. Öffentlicher Schlüssel (Bytefolge) [X.509-Format]
				X509EncodedKeySpec x509 = new X509EncodedKeySpec(keyToWrite.getEncoded());
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				PublicKey pubKey = keyFactory.generatePublic(x509);
				dos.write(pubKey.getEncoded());
			}

			dos.close();
		} catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
