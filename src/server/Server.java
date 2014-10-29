package server;

class Server {

	private Driver driver1;
	private Driver driver2;
	private Quadro quadro1;
	private Quadro quadro2;
	private int x, y;
	private double angulo;

	Server() {
		this.quadro1 = new Quadro();
		this.quadro2 = new Quadro();
		Gui gui = new Gui(this.quadro1, this.quadro2, this);
		gui.setVisible(true);
	}

	public void drive(double stepSize) {
		if (this.driver1 != null) {
			this.driver1.stopSock();
		}
		if (this.driver2 != null) {
			this.driver2.stopSock();
		}

		this.driver1 = new Driver(this.quadro1, this.x, this.y, stepSize, this.angulo, 4321);
		this.driver1.start();
		this.driver2 = new Driver(this.quadro2, this.x, this.y, stepSize, this.angulo, 4322);
		this.driver2.start();
	}

	void finalizar() {
		System.exit(0);
	}

	public static void main(String[] args) {
		new Server();
	}

	public void setTruckPosition(String x, String y, String angulo) {
		this.x = (int) (Double.parseDouble(x) * this.quadro1.getWidth());
		this.y = (int) (Double.parseDouble(y) * this.quadro1.getHeight());
		this.angulo = Double.parseDouble(angulo);
	}

}
