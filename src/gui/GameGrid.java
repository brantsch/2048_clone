package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import logic.Direction;
import logic.GameLogic;
import logic.GameState;

public class GameGrid extends JPanel implements KeyListener {
	/* constants */
	private static final int KEY_UP = KeyEvent.VK_UP;
	private static final int KEY_DOWN = KeyEvent.VK_DOWN;
	private static final int KEY_RIGHT = KeyEvent.VK_RIGHT;
	private static final int KEY_LEFT = KeyEvent.VK_LEFT;
	private static final int KEY_RESET = KeyEvent.VK_ESCAPE;
	private static final int layoutGap = 5;
	private final int dimen;

	/* attributes */
	private GameLogic gameLogic;
	private JLabel[][] numbers;

	/**
	 * Generate a GameGrid for an instance of GameLogic.
	 * 
	 * @param gl
	 *            The instance of GameLogic for which a GameGrid shall be
	 *            generated.
	 */
	public GameGrid(GameLogic gl) {
		if (gl == null) {
			throw new NullPointerException();
		}
		this.gameLogic = gl;
		this.dimen = gl.getDimen();
		this.addKeyListener(this);
		this.setFocusable(true);
		this.setLayout(new GridLayout(dimen, dimen, layoutGap, layoutGap));
		this.numbers = new JLabel[dimen][dimen];
		for (int y = 0; y < dimen; ++y) {
			for (int x = 0; x < dimen; ++x) {
				JLabel l = new JLabel();
				numbers[y][x] = l;
				l.setHorizontalAlignment(SwingConstants.CENTER);
				l.setVerticalAlignment(SwingConstants.CENTER);
				l.setBorder(new LineBorder(Color.GRAY, 2, true));
				l.setMinimumSize(new Dimension(60, 60));
				l.setPreferredSize(l.getMinimumSize());
				l.setOpaque(true);
				this.add(l);
			}
		}
		this.update();
	}

	/**
	 * Update contents of the GameGrid.
	 */
	private void update() {
		long[][] grid = this.gameLogic.getGrid();
		float h;
		String text;
		Color bgcolor;
		for (int y = 0; y < dimen; ++y) {
			for (int x = 0; x < dimen; ++x) {
				if (grid[y][x] == 0) {
					text = "";
					bgcolor = Color.gray;
				} else {
					text = "" + grid[y][x];
					/* log2(2048) = 11, therefore the 12th bit must be set */
					h = ((float) (Long.SIZE - Long.numberOfLeadingZeros(grid[y][x]))) / 12f;
					bgcolor = Color.getHSBColor(h, 0.6f, 1.0f);
				}
				numbers[y][x].setText(text);
				numbers[y][x].setBackground(bgcolor);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		switch (gameLogic.getState()) {
		case LOST:
			if(ke.getKeyCode() == KEY_RESET){
				gameLogic.reset();
			}
			break;
		case IDLE: /* press any key to start/reset */
			gameLogic.reset();
			update();
			break;
		case RUNNING:
			switch (ke.getKeyCode()) {
			case KEY_UP:
				gameLogic.doMove(Direction.UP);
				System.err.println("up key pressed");
				break;
			case KEY_DOWN:
				gameLogic.doMove(Direction.DOWN);
				System.err.println("down key pressed");
				break;
			case KEY_LEFT:
				gameLogic.doMove(Direction.LEFT);
				System.err.println("left key pressed");
				break;
			case KEY_RIGHT:
				gameLogic.doMove(Direction.RIGHT);
				System.err.println("right key pressed");
				break;
			case KEY_RESET:
				gameLogic.reset();
			default:
				System.err.println("unhandled key pressed");
			}
			update();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// this type of event is currently ignored
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// we currently ignore typed keys
	}
}
