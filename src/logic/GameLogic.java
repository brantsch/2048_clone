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
		int dx, dy, offset;
		switch (d) {
		case UP:
			dy = -1;
			dx = 0;
			offset = dimen - 1;
			break;
		case DOWN:
			dy = 1;
			dx = 0;
			offset = 0;
			break;
		case LEFT:
			dy = 0;
			dx = -1;
			offset = dimen - 1;
			break;
		case RIGHT:
			dy = 0;
			dx = 1;
			offset = 0;
			break;
		default:
			throw new RuntimeException("This must not ever happen!");
		}
		for (int i = 0; i < dimen; ++i) {
			if (d == Direction.UP || d == Direction.DOWN) {
				squash(dx, dy, i, offset);
			} else {
				squash(dx, dy, offset, i);
			}
		}
	}

	private void squash(int dx, int dy, int x, int y) {
		boolean already_squashed = false;
		for (; x >= 0 && x < dimen && y >= 0 && y < dimen; x += dx, y += dy) {
			if (x + dx >= 0 && x + dx < dimen && y + dy >= 0 && y + dy < dimen) {
				if (grid[y + dy][x + dx] == 0) {
					grid[y + dy][x + dx] = grid[y][x];
					grid[y][x] = 0;
				} else if (!already_squashed && grid[y + dy][x + dx] == grid[y][x]) {
					grid[y + dy][x + dx] += grid[y][x];
					grid[y][x] = 0;
					already_squashed = true;
				}
			}
		}
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
