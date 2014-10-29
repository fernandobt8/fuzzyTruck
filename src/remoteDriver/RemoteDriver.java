package remoteDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import net.sourceforge.jFuzzyLogic.FIS;

public class RemoteDriver {

	static int port = 4321;
	static String host = "localhost";

	public static void main(String[] args) throws IOException {

		Socket kkSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;

		try {
			kkSocket = new Socket(host, port);
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host:" + host);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + host);
			System.exit(1);
		}

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromServer;

		double x, y;
		double angle;

		// requisicao da posicao do caminhao
		out.println("r");
		while ((fromServer = in.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(fromServer);
			x = Double.valueOf(st.nextToken()).doubleValue();
			y = Double.valueOf(st.nextToken()).doubleValue();
			angle = Double.valueOf(st.nextToken()).doubleValue();
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (angle > 360) {
				angle %= 360;
			}

			System.out.println("x: " + x + " y: " + y + " angle: " + angle);

			FIS fis = FIS.load("./src/remoteDriver/fuzzyTruck.fcl", true);

			fis.setVariable("x", x);
			fis.setVariable("y", y);
			fis.setVariable("angle", angle);

			fis.evaluate();

			double teste = fis.getVariable("turn").getLatestDefuzzifiedValue();
			// double teste = Double.valueOf(stdIn.readLine());

			// /////////////////////////////////////////////////////////////////////////////// Acaba sua modificacao aqui
			// envio da acao do volante
			System.out.println("volante: " + teste);
			out.println(teste);

			// requisicao da posicao do caminhao
			out.println("r");
		}

		out.close();
		in.close();
		stdIn.close();
		kkSocket.close();
	}
}