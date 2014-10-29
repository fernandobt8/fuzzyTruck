package server;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Quadro extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	LinkedList<ArrayList<Point>> ps;
	int graphs;
	private JLabel scoreLabel;

	public Quadro() {
		this.ps = new LinkedList<ArrayList<Point>>();
		this.ps.add(new ArrayList<Point>());
		this.graphs = 0;
		this.scoreLabel = new JLabel("Score:");
		this.scoreLabel.setFont(this.scoreLabel.getFont().deriveFont(28.0f));
		this.add(this.scoreLabel, BorderLayout.NORTH);
		this.setBorder(BorderFactory.createLineBorder(this.getForeground(), 2));
	}

	public void setScore(double score) {
		this.scoreLabel.setText("Score: " + String.valueOf(score));
		this.repaint();
	}

	public void addTruckImage(Truck truck) {
		for (int i = 0; i < Truck.nPontos; i++) {
			this.ps.get(this.graphs).add(new Point(truck.getPoint(i).x, truck.getPoint(i).y));
		}
		this.ps.add(new ArrayList<Point>());
		this.graphs++;
		this.repaint();
	}

	public void clear() {
		this.ps.clear();
		this.ps.add(new ArrayList<Point>());
		this.ps.add(new ArrayList<Point>());
		this.ps.add(new ArrayList<Point>());

		this.ps.get(0).add(new Point(this.getWidth() / 2 - 20, this.getHeight() - 20));
		this.ps.get(0).add(new Point(this.getWidth() / 2 - 20, this.getHeight()));

		this.ps.get(1).add(new Point(this.getWidth() / 2 + 20, this.getHeight() - 20));
		this.ps.get(1).add(new Point(this.getWidth() / 2 + 20, this.getHeight()));

		this.graphs = 2;
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Point p, p2;
		for (int j = 0; j < this.graphs + 1; j++) {
			for (int i = 0; i < this.ps.get(j).size() - 1; i++) {
				p = this.ps.get(j).get(i);
				p2 = this.ps.get(j).get(i + 1);
				g.drawLine(p.x, p.y, p2.x, p2.y);
			}
		}

	}

}
