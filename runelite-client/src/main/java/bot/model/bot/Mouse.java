package bot.model.bot;

import bot.Common;
import net.runelite.api.Client;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Mouse {
	private Client client;
	public static double GRAVITY        = 9;
	public static double WIND           = 3;
	public static double MAGNITUDE      = 15;
	public static double DISTANCE       = 12;
	private static final double sqrt3   = Math.sqrt(3);
	private static final double sqrt5   = Math.sqrt(5);

	public Mouse(Client client) {
		this.client = client;
	}

	public void move(double destX, double destY) {
		double startX = client.getMouseCanvasPosition().getX();
		double startY = client.getMouseCanvasPosition().getY();
		double currentX = startX;
		double currentY = startY;
		double v_x = 0, v_y = 0, W_x = 0, W_y = 0;
		Random random = new Random();
		double distance;
		double magnitude = MAGNITUDE;

		while ((distance = Math.hypot(destX - startX, destY - startY)) >= 1) {
			double W_mag = Math.min(WIND, distance);

			if (distance >= DISTANCE) {
				W_x = W_x / sqrt3 + (2 * random.nextDouble() - 1) * W_mag / sqrt5;
				W_y = W_y / sqrt3 + (2 * random.nextDouble() - 1) * W_mag / sqrt5;
			} else {
				W_x /= sqrt3;
				W_y /= sqrt3;
			}

			if (magnitude < 3) {
				magnitude = random.nextDouble() * 3 + 3;
			} else {
				magnitude /= sqrt5;
			}

			v_x += W_x + GRAVITY * (destX - startX) / distance;
			v_y += W_y + GRAVITY * (destY - startY) / distance;
			double v_mag = Math.hypot(v_x, v_y);

			if (v_mag > magnitude) {
				double v_clip = magnitude / 2 + random.nextDouble() * magnitude / 2;
				v_x = (v_x / v_mag) * v_clip;
				v_y = (v_y / v_mag) * v_clip;
			}

			startX += v_x;
			startY += v_y;
			int moveX = (int) Math.round(startX);
			int moveY = (int) Math.round(startY);

			if (currentX != moveX || currentY != moveY) {
				// Move the mouse to moveX and moveY.
				Canvas canvas = client.getCanvas();
				MouseEvent mouseEvent = new MouseEvent(
					canvas,
					MouseEvent.MOUSE_MOVED,
					System.currentTimeMillis(),
					0,
					moveX,
					moveY,
					0,
					false
				);
				canvas.dispatchEvent(mouseEvent);
				Common.sleep(1);

				currentX = moveX;
				currentY = moveY;
			}
		}
	}

	public void click(int button) {
		Canvas canvas = client.getCanvas();
		int x = client.getMouseCanvasPosition().getX();
		int y = client.getMouseCanvasPosition().getY();
		MouseEvent mouseEvent = new MouseEvent(
			canvas,
			MouseEvent.MOUSE_PRESSED,
			System.currentTimeMillis(),
			0,
			x,
			y,
			0,
			false,
			button
		);
		canvas.dispatchEvent(mouseEvent);
		mouseEvent = new MouseEvent(
			canvas,
			MouseEvent.MOUSE_RELEASED,
			System.currentTimeMillis(),
			0,
			x,
			y,
			0,
			false,
			button
		);
		canvas.dispatchEvent(mouseEvent);
	}

	public void click(double x, double y, int button) {
		// Get current mouse position
		int currentX = client.getMouseCanvasPosition().getX();
		int currentY = client.getMouseCanvasPosition().getY();

		// Check if current mouse position is not equal to the given coordinates
		if(currentX != x || currentY != y) {
			// Move the mouse to the given coordinates
			move(x, y);
			Common.sleep(Common.generateRandomNumber(30, 90));
		}

		// Press the specified mouse button down
		Canvas canvas = client.getCanvas();
		MouseEvent mouseEvent = new MouseEvent(
			canvas,
			MouseEvent.MOUSE_PRESSED,
			System.currentTimeMillis(),
			0,
			currentX,
			currentY,
			0,
			false,
			button
		);
		canvas.dispatchEvent(mouseEvent);

		// Release the specified mouse button
		mouseEvent = new MouseEvent(
			canvas,
			MouseEvent.MOUSE_RELEASED,
			System.currentTimeMillis(),
			0,
			currentX,
			currentY,
			0,
			false,
			button
		);
		canvas.dispatchEvent(mouseEvent);
	}

	public Point getPosition() {
		net.runelite.api.Point rlPoint = client.getMouseCanvasPosition();
		return new Point(rlPoint.getX(), rlPoint.getY());
	}
}