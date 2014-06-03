/**
 * The package containing the actual game logic.
 */
package logic;

import logic.Tuple;
import logic.GameOver;

import java.util.Random;
import java.util.ArrayList;

/**
 * @author Peter Brantsch
 * 
 */
public class GameLogic {
	private static final int dimen = 4;
	private long grid[][];

	public GameLogic() {
		this.grid = new long[dimen][dimen];
		for (int i = 0; i < dimen; ++i) {
			for (int j = 0; j < dimen; ++j) {
				this.grid[i][j] = 0;
			}
		}
	}

	/**
	 * Perform a move in the given direction.
	 * 
	 * @return True, if further moves are possible.
	 * @param d
	 *            The direction in which a move would be attempted.
	 */
	public void doMove(Direction d) throws GameOver {
		int dx, dy;
		boolean already_has_fold[] = new boolean[dimen];
		for (int i = 0; i < dimen; ++i) {
			already_has_fold[i] = false;
		}
		boolean do_fold = false;
		switch (d) {
		case UP:
			dx = 0;
			dy = 1;
			break;
		case DOWN:
			dx = 0;
			dy = -1;
			break;
		case LEFT:
			dx = 1;
			dy = 0;
			break;
		case RIGHT:
			dx = -1;
			dy = 0;
			break;
		default:
			throw new RuntimeException("This must not ever happen!");
		}
		int fold_index = 0;
		for (int y = 0; y < dimen; ++y) {
			for (int x = 0; x < dimen; ++x) {
				if (x + dx >= 0 && x + dx < dimen && y + dy >= 0
						&& y + dy < dimen) {
					do_fold = (grid[y][x] == grid[y + dy][x + dx]);
					if (grid[y][x] == 0 || do_fold) {
						grid[y][x] += grid[y + dy][x + dx];
						grid[y + dy][x + dx] = 0;
					}
				}
			}
		}
	}

	public ArrayList<Integer> crush(ArrayList<Integer> ints, boolean forward) {
		int offset = 0;
		int length = ints.size();
		int delta = 1;
		boolean folded_yet = false;
		ArrayList<Integer> crushedList = new ArrayList<Integer>(length);
		if (!forward) {
			delta = -1;
			offset = length - 1;
		}
		for (int i = offset; i >= 0 && i < length; i += delta) {
			if(i-delta>=0){
				if((!folded_yet && ints.get(i) == ints.get(i-delta)) || ints.get(i) == 0){
					
				}
			}
		}
		return crushedList;
	}

	/**
	 * Place a '2' on the playing field.
	 * 
	 * @throws Exception
	 *             In case a situation arises which should be impossible in
	 *             which there is no free cell after a move.
	 */
	public void place2() {
		Random r = new Random();
		ArrayList<Tuple<Integer>> freeCells = new ArrayList<Tuple<Integer>>();
		for (int x = 0; x < dimen; ++x) {
			for (int y = 0; y < dimen; ++y) {
				if (grid[x][y] == 0) {
					freeCells.add(new Tuple<Integer>(x, y));
				}
			}
		}
		if (freeCells.isEmpty()) {
			throw new RuntimeException(
					"This should never happen! After a move, there must be at least one free cell on the grid.");
		}
		Tuple<Integer> t = freeCells.get(r.nextInt(freeCells.size()));
		grid[t.getX()][t.getY()] = 2;
	}

	public void printGrid() {
		String format = "";
		for (int i = 0; i < dimen; ++i) {
			for (int j = 0; j < dimen; ++j) {
				System.out.printf("%d ", grid[i][j]);
			}
			System.out.print('\n');
			System.out.flush();
		}
	}
}
