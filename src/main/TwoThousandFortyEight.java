package main;

import java.awt.BorderLayout;

import gui.GameGrid;

import javax.swing.JFrame;

import logic.GameLogic;

/**
 * @author Peter Brantsch
 * 
 */
public abstract class TwoThousandFortyEight {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame("2048");
		GameLogic gl = new GameLogic();
		GameGrid g = new GameGrid(gl);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(new BorderLayout());
		f.add(g,BorderLayout.CENTER);
		f.pack();
		f.setVisible(true);
	}

}
