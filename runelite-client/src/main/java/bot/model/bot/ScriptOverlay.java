package bot.model.bot;

import bot.model.ScriptBase;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import java.awt.*;

public class ScriptOverlay extends Overlay {
	private final ScriptBase script;

	public ScriptOverlay(ScriptBase script) {
		this.script = script;
		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.HIGHEST);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
	}

	@Override
	public Dimension render(Graphics2D graphics) {
		script.render(graphics);
		renderMouse(graphics);
		return null;
	}

	private void renderMouse(Graphics2D graphics) {
		Point mousePosition = script.client.getMouseCanvasPosition();
		int x = mousePosition.getX();
		int y = mousePosition.getY();
		graphics.setColor(Color.RED);
		int lineSize = 5;
		graphics.drawLine(x-lineSize, y-lineSize, x+lineSize, y+lineSize);
		graphics.drawLine(x+lineSize, y-lineSize, x-lineSize, y+lineSize);
	}
}
