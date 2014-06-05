package main;

import logic.GameLogic;
import logic.Direction;
import logic.GameOver;

/**
 * @author Peter Brantsch
 * 
 */
public abstract class TwoThousandFortyEight {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameLogic theGameLogic = new GameLogic();
		System.out.println("Welcome to 2048.");
		for (int i=0;i<10;++i) {
			try {
				theGameLogic.place2();
				theGameLogic.printGrid();
				System.out.println("DOWN");
				theGameLogic.doMove(Direction.DOWN);
				theGameLogic.printGrid();
				System.out.println();

				theGameLogic.place2();
				theGameLogic.printGrid();
				System.out.println("LEFT");
				theGameLogic.doMove(Direction.LEFT);
				theGameLogic.printGrid();
				System.out.println();

				theGameLogic.place2();
				theGameLogic.printGrid();
				System.out.println("RIGHT");
				theGameLogic.doMove(Direction.RIGHT);
				theGameLogic.printGrid();
				System.out.println();

				theGameLogic.place2();
				theGameLogic.printGrid();
				System.out.println("UP");
				theGameLogic.doMove(Direction.UP);
				theGameLogic.printGrid();
				System.out.println();
			} catch (GameOver g) {
				System.err.println("GAME OVER");
				break;
			}
		}
	}

}
