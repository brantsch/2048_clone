package logic;

import java.util.Random;
import java.util.ArrayList;

/**
 * Implement the logic for a game of 2048.
 * 
 * @author Peter Brantsch
 * 
 */
public class GameLogic {
	private long grid[][];
	private final int dimen = 4;
	private final long won_threshold = 2048;
	private GameState state;

	public int getDimen() {
		return dimen;
	}

	public GameLogic() {
		grid = new long[dimen][dimen];
		state = GameState.IDLE;
	}

	/**
	 * Reset the GameLogic to its initial state.
	 * 
	 * Fills the grid with empty cells (zeroes), then places two numbers.
	 */
	public void reset() {
		this.state = GameState.RUNNING;
		for (int i = 0; i < dimen; ++i) {
			for (int j = 0; j < dimen; ++j) {
				this.grid[i][j] = 0;
			}
		}
		placeNumber();
		placeNumber();
	}

	/**
	 * Perform a move in the given direction.
	 * 
	 * @param d
	 *            The direction in which a move would be attempted.
	 */
	public void doMove(Direction d) {
		boolean did_squash = false;
		for (int i = 0; i < dimen; ++i) {
			switch (d) {
			case DOWN:
				did_squash |= squashColumn(i, true);
				break;
			case UP:
				did_squash |= squashColumn(i, false);
				break;
			case RIGHT:
				did_squash |= squashRow(i, true);
				break;
			case LEFT:
				did_squash |= squashRow(i, false);
				break;
			}
		}
		if (did_squash) {
			placeNumber();
		}
		state = checkGrid();
	}

	/**
	 * @param idx
	 *            Index of the column to be squashed.
	 * @param forward
	 *            If this parameter is true, the elements of the column are
	 *            squashed from their lowest index to their highest. If false,
	 *            they are squashed in the opposite order.
	 * @return True, if the column could be squashed, false otherwise.
	 */
	private boolean squashColumn(int idx, boolean forward) {
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

	/**
	 * @param idx
	 *            Index of the row to be squashed.
	 * @param forward
	 *            If this parameter is true, the elements of the row are
	 *            squashed from their lowest index to their highest. If false,
	 *            they are squashed in the opposite order.
	 * @return True, if the column could be squashed, false otherwise.
	 */
	private boolean squashRow(int idx, boolean forward) {
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

	/**
	 * @param numbers
	 *            The array of numbers to be squashed.
	 * @return This function returns the squashed numbers in a new array if the
	 *         squashing was successful. Otherwise it returns null.
	 */
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
				squashed = (j != i);
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

	/**
	 * Test the grid for the possibility of movements for any cell in any
	 * direction, and for any cell value large enough for the player to have
	 * won.
	 * 
	 * @return Depending on the values of the cells on the grid and the previous
	 *         GameState, determine the next GameState.
	 */
	private GameState checkGrid() {
		boolean canMove = false;
		boolean hasWon = false;
		for (int y = 0; y < dimen; ++y) {
			for (int x = 0; x < dimen; ++x) {
				canMove |= canMove(1, 0, y, x);
				canMove |= canMove(-1, 0, y, x);
				canMove |= canMove(0, 1, y, x);
				canMove |= canMove(0, -1, y, x);
				if (grid[y][x] >= won_threshold) {
					hasWon = true;
				}
			}
		}
		GameState nextState = state;
		if (hasWon && state != GameState.WON) {
			nextState = GameState.WON;
		} else if (!canMove) {
			nextState = GameState.LOST;
		}
		return nextState;
	}

	/**
	 * For a cell on the grid, test if movement in a direction is possible.
	 * 
	 * @param dy
	 *            Movement step along the Y axis.
	 * @param dx
	 *            Movement step along the x axis.
	 * @param y
	 *            Y coordinate of the cell.
	 * @param x
	 *            X coordinate of the cell.
	 * @return
	 */
	private boolean canMove(int dy, int dx, int y, int x) {
		return (x + dx >= 0 && x + dx < dimen && y + dy >= 0 && y + dy < dimen)
				&& (grid[y + dy][x + dx] == 0 || grid[y][x] == grid[y + dy][x + dx]);
	}

	/**
	 * Place a number on the playing field, if possible, do nothing otherwise.
	 * The number is randomly chosen to be either a 2 or a 4, with a bias
	 * towards 2s.
	 */
	private void placeNumber() {
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
		return grid;
	}

	public GameState getState() {
		return state;
	}
}
