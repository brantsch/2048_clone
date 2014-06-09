package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import logic.Direction;
import logic.GameLogic;

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
	private JLabel messageLabel;
	private JPanel innerPanel;

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
		gameLogic = gl;
		dimen = gl.getDimen();
		addKeyListener(this);
		setFocusable(true);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		messageLabel = new JLabel("", JLabel.CENTER);
		messageLabel.setVerticalAlignment(SwingConstants.CENTER);
		c.gridy = 0;
		c.gridx = 0;
		c.ipady = 50;
		c.gridwidth = 1;//dimen;
		c.fill = GridBagConstraints.BOTH;
		add(messageLabel,c);

		numbers = new JLabel[dimen][dimen];
		innerPanel = new JPanel(new GridLayout(dimen,dimen,layoutGap,layoutGap));
		for (int y = 0; y < dimen; ++y) {
			for (int x = 0; x < dimen; ++x) {
				JLabel l = new JLabel();
				numbers[y][x] = l;
				l.setHorizontalAlignment(SwingConstants.CENTER);
				l.setVerticalAlignment(SwingConstants.CENTER);
				l.setBorder(new LineBorder(Color.GRAY, 2, true));
				l.setMinimumSize(new Dimension(60, 60));
				l.setPreferredSize(l.getMinimumSize());
				l.setMaximumSize(l.getMinimumSize());
				l.setOpaque(true);
				innerPanel.add(l);
			}
		}
		c.ipady = 0;
		c.gridwidth = 1;//dimen;
		c.gridy++;
		add(innerPanel,c);
		update();
	}

	/**
	 * Update contents of the GameGrid.
	 */
	private void update() {
		long[][] grid = gameLogic.getGrid();
		String message;
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
		switch (gameLogic.getState()) {
		case IDLE:
			message = "Press any key to start";
			break;
		case LOST:
			message = "Game Over. Press ESC to restart.";
			break;
		default:
			message = "";
		}
		messageLabel.setText(message);
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
				break;
			case KEY_DOWN:
				gameLogic.doMove(Direction.DOWN);
				break;
			case KEY_LEFT:
				gameLogic.doMove(Direction.LEFT);
				break;
			case KEY_RIGHT:
				gameLogic.doMove(Direction.RIGHT);
				break;
			case KEY_RESET:
				gameLogic.reset();
			default:
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
