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
	private final int dimen = 4;

	public int getDimen() {
		return dimen;
	}

	private long grid[][];

	public GameLogic() {
		this.grid = new long[dimen][dimen];
		for (int i = 0; i < dimen; ++i) {
			for (int j = 0; j < dimen; ++j) {
				this.grid[i][j] = 0;
			}
		}
		this.placeNumber();
		this.placeNumber();
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
		boolean did_squash = false;
		for (int i = 0; i < dimen; ++i) {
			switch (d) {
			case DOWN:
				did_squash |= squash_column(i, true);
				break;
			case UP:
				did_squash |= squash_column(i, false);
				break;
			case RIGHT:
				did_squash |= squash_row(i, true);
				break;
			case LEFT:
				did_squash |= squash_row(i, false);
				break;
			}
		}
		if (did_squash) {
			System.out.println("squashed");
			this.placeNumber();
		} else {
			System.out.println("didn't squash");
		}
	}

	private boolean squash_column(int idx, boolean forward) {
		long output[];
		long input[] = new long[dimen];
		for (int i = 0; i < dimen; ++i) {
			input[i] = grid[(!forward ? i : dimen - 1 - i)][idx];
		}
		output = squash(input);
		if (null != output) {
			for (int i = 0; i < dimen; ++i) {
				grid[(!forward ? i : dimen - 1 - i)][idx] = output[i];
			}
			return true;
		} else {
			return false;
		}
	}

	private boolean squash_row(int idx, boolean forward) {
		long output[];
		long input[] = new long[dimen];
		for (int i = 0; i < dimen; ++i) {
			input[i] = grid[idx][(!forward ? i : dimen - 1 - i)];
		}
		output = squash(input);
		if (null != output) {
			for (int i = 0; i < dimen; ++i) {
				grid[idx][(!forward ? i : dimen - 1 - i)] = output[i];
			}
			return true;
		} else {
			return false;
		}
	}

	private long[] squash(long numbers[]) {
		long result[] = new long[dimen];
		boolean squashed = false;
		for (int i = 0; i < dimen; ++i) {
			result[i] = 0;
		}
		for (int i = 0, j = 0; i < dimen; ++i) {
			if (numbers[i] == 0) {
				continue;
			}
			int k;
			for (k = i + 1; k < dimen && numbers[k] == 0; ++k)
				;
			if (k >= dimen) { // nothing to do, rest of numbers is just zeroes
				result[j] = numbers[i];
				squashed = (j!=i);
				break;
			}
			if (numbers[i] == numbers[k]) {
				result[j++] = 2 * numbers[i];
				i = k;
				squashed = true;
			} else {
				result[j++] = numbers[i];
			}
		}
		if (squashed) {
			return result;
		} else {
			return null;
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

	/**
	 * Place a number on the playing field, if possible, do nothing otherwise.
	 * The number is randomly chosen to be either a 2 or a 4, with a bias towards 2s.
	 */
	public void placeNumber() {
		Random r = new Random();
		long number = (r.nextFloat() < 0.8f ? 2 : 4);
		ArrayList<Tuple<Integer>> freeCells = new ArrayList<Tuple<Integer>>();
		for (int x = 0; x < dimen; ++x) {
			for (int y = 0; y < dimen; ++y) {
				if (grid[x][y] == 0) {
					freeCells.add(new Tuple<Integer>(x, y));
				}
			}
		}
		if (!freeCells.isEmpty()) {
			Tuple<Integer> t = freeCells.get(r.nextInt(freeCells.size()));
			grid[t.getX()][t.getY()] = number;
		}
	}

	public void printGrid() {
		for (int i = 0; i < dimen; ++i) {
			for (int j = 0; j < dimen; ++j) {
				System.out.printf("%4d ", grid[i][j]);
			}
			System.out.print('\n');
			System.out.flush();
		}
	}

	public long[][] getGrid() {
		return this.grid;
	}
}
