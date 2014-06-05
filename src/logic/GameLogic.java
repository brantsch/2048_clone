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
		checkGrid();
		for (int i = 0; i < dimen; ++i) {
			switch (d) {
			case DOWN:
				squash_column(i, 0, 1);
				break;
			case UP:
				squash_column(i, dimen - 1, -1);
				break;
			case LEFT:
				squash_row(i, dimen - 1, -1);
				break;
			case RIGHT:
				squash_row(i, 0, 1);
				break;
			}
		}
	}

	private void squash_column(int idx, int offset, int step) {
		long output[];
		long input[] = new long[dimen];
		for(int i=offset,j=0;i>=0 && i<dimen;i += step,++j){
			input[j] = grid[i][idx];
		}
		output = squash(input);
		for(int i=offset,j=0;i>=0 && i<dimen;i += step,++j){
			grid[i][idx] = output[j];
		}
	}

	private void squash_row(int idx, int offset, int step) {
		long output[];
		long input[] = new long[dimen];
		for(int i=offset,j=0;i>=0 && i<dimen;i += step,++j){
			input[j] = grid[idx][i];
		}
		output = squash(input);
		for(int i=offset,j=0;i>=0 && i<dimen;i += step,++j){
			grid[idx][i] = output[j];
		}
	}

	private void checkGrid() throws GameOver {
		boolean canMove = false;
		for (int y = 0; y < dimen; ++y) {
			for (int x = 0; x < dimen; ++x) {
				canMove |= canMove(1, 0, y, x);
				canMove |= canMove(-1, 0, y, x);
				canMove |= canMove(0, 1, y, x);
				canMove |= canMove(0, -1, y, x);
			}
		}
		if (!canMove) {
			throw new GameOver();
		}
	}

	private boolean canMove(int dy, int dx, int y, int x) {
		return (x + dx >= 0 && x + dx < dimen && y + dy >= 0 && y + dy < dimen)
				&& (grid[y + dy][x + dx] == 0 || grid[y][x] == grid[y + dy][x + dx]);
	}

	private long[] squash(long numbers[]) {
		long result[] = new long[dimen];
		for (int i = 0; i < dimen; ++i) {
			result[i] = 0;
		}
		for (int i = 0, j = 0; i < dimen - 1; ++i) {
			if (numbers[i] == numbers[i + 1]) {
				result[j] = numbers[i] * 2;
				++i;
				++j;
			} else if (numbers[i] == 0) {
			} else {
				result[j] = numbers[i];
				++j;
			}
		}
		return result;
	}

	/**
	 * Place a '2' on the playing field, if possible, do nothing otherwise.
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
				System.out.printf("%4d ", grid[i][j]);
			}
			System.out.print('\n');
			System.out.flush();
		}
	}
}
