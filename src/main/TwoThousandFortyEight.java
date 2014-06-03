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
		for(;;){
			theGameLogic.place2();
			theGameLogic.printGrid();
			try{
				theGameLogic.doMove(Direction.DOWN);
			}catch(GameOver g){
				System.err.println("GAME OVER");
				break;
			}
			theGameLogic.printGrid();
			System.out.println();
		}
	}

}
