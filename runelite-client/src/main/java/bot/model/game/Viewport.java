package bot.model.game;

import net.runelite.api.Client;
import net.runelite.api.NPC;

import java.awt.*;

public class Viewport {
	private final Client client;
	private final Rectangle bounds;

	public Viewport(Client client) {
		this.client = client;
		bounds = new Rectangle(
			client.getViewportXOffset(),
			client.getViewportYOffset(),
			client.getViewportWidth(),
			client.getViewportHeight());
	}

	public boolean contains(NPC npc) {
		try {
			if(npc == null)	return false;
			Shape shape = npc.getConvexHull();
			if(shape == null) return false;
			return bounds.contains(shape.getBounds2D());
		} catch (Exception e) {
			return false;
		}
	}
}