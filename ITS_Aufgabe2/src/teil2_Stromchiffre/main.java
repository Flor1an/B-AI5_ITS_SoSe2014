package teil2_Stromchiffre;

import java.io.File;

public class main {

	private static final long x0 = 111;
	private static final File message = new File(
			"src/teil2_Stromchiffre/Files/Message.txt");
	private static final File chiffre = new File(
			"src/teil2_Stromchiffre/Files/Chiffre.txt");

	public static void main(String[] args) {
		HC1 prog = new HC1(x0, message, chiffre);

		prog.encryt();
		//prog.decrypt();

	}

}
