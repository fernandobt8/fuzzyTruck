package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Driver extends Thread {

	Quadro quadro;

	final int nAngulos = 7;
	final int nX = 5;
	final int nSaidas = 7;
	int sockPort;

	private Truck t;

	private boolean running;

	private PrintWriter out;

	private BufferedReader in;

	private Socket clientSocket;

	private ServerSocket server;

	public Driver(Quadro quadro, int x, int y, double stepSize, double angulo, int sockPort) {
		this.quadro = quadro;
		this.t = new Truck(x, y, stepSize, angulo);
		this.sockPort = sockPort;
	}

	public void setTruckPosition(int x, int y, double angulo) {
		this.t.setRotation(angulo);
		this.t.setPos(x, y);
	}

	@SuppressWarnings("deprecation")
	public void stopSock() {
		this.stop();
		try {
			if (this.out != null) {
				this.out.close();
			}
			if (this.in != null) {
				this.in.close();
			}
			if (this.clientSocket != null && !this.clientSocket.isClosed()) {
				this.clientSocket.close();
			}
			if (this.server != null && !this.server.isClosed()) {
				this.server.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		this.running = true;
		this.quadro.clear();
		this.quadro.addTruckImage(this.t);
		int stepsUsed = 10000;

		this.server = null;
		try {
			this.server = new ServerSocket(this.sockPort);
		} catch (IOException e) {
			System.out.println("Could not listen on port " + this.sockPort);
			System.exit(-1);
		}

		System.out.println("Listening on port " + this.sockPort);
		this.clientSocket = null;
		try {
			this.clientSocket = this.server.accept();
		} catch (IOException e) {
			System.err.println("Accept failed on " + this.sockPort);
			System.exit(1);
		}

		System.out.println("Client connected on port " + this.sockPort);

		try {

			this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
			String inputLine;
			stepsUsed = 0;

			while (this.running && this.clientSocket.isConnected() && (inputLine = this.in.readLine()) != null) {
				if (inputLine.contains("r")) {
					this.out.println((double) this.t.getPos().x / (double) this.quadro.getWidth() + "\t" + (double) this.t.getPos().y / (double) this.quadro.getHeight() + "\t"
							+ this.t.getRotation());
				} else {
					double steer = Double.valueOf(inputLine);
					if (steer > 1) {
						this.t.stepManeuver(1);
					} else if (steer < -1) {
						this.t.stepManeuver(-1);
					} else {
						this.t.stepManeuver(steer);
					}

					this.quadro.addTruckImage(this.t);
					stepsUsed++;
				}

				if (this.t.getPos().x > this.quadro.getWidth() + 200 || this.t.getPos().y > this.quadro.getHeight() || this.t.getPos().x < -200 || this.t.getPos().y < -200
						|| stepsUsed > 2000) {
					break;
				}
			}
			this.out.close();
			this.in.close();
			this.clientSocket.close();
			this.server.close();

		} catch (Exception e) {
			System.err.println("Something else went wrong on " + this.sockPort);
			System.exit(1);
		}

		double score = 10000 - stepsUsed * this.t.stepSize - Math.abs(this.t.getPos().x - this.quadro.getWidth() / 2) - Math.abs(90 - this.t.getRotation())
				- (this.quadro.getHeight() - this.t.getPos().y);
		System.out.println("Client disconnected on " + this.sockPort);
		System.out.println(this.sockPort + " Final x: " + this.t.getPos().x + " final y: " + this.t.getPos().y + " final angle: " + this.t.getRotation() + " steps used: "
				+ stepsUsed);
		System.out.println("Score of " + this.sockPort + " is: " + score);

		this.quadro.setScore(score);

	}

}
