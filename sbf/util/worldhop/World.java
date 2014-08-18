package scripts.sbf.util.worldhop;

import java.awt.Rectangle;

import org.tribot.api.input.Mouse;

public class World {

	private int worldNumber;
	private final int initialPosX = 205;
	private final int initialPosY = 61;
	private final int ROWS = 4;
	private final int COLS = 17;
	private final int X_MULT = 93;
	private final int Y_MULT = 24;
	private final int WIDTH = 84;
	private final int HEIGHT = 18;
	private final int UNO = 1;

	public World(int worldNumber) {
		this.worldNumber = worldNumber;
	}

	public Rectangle getBounds() {
		int x = ((int) ((Math.floor(worldNumber) / COLS) % ROWS) * X_MULT)
				+ initialPosX;
		int y = ((int) (Math.ceil(worldNumber) % COLS) * Y_MULT) + initialPosY;
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public void click() {
		Rectangle rect = getBounds();
		int x = (int) rect.getX();
		int y = (int) rect.getY();
		int maxX = (int) rect.getWidth() + x;
		int maxY = (int) rect.getHeight() + y;
		Mouse.clickBox(x, y, maxX, maxY, UNO);
	}

	public int getWorldIndex() {
		return this.worldNumber;
	}

}
