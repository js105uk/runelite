package bot.model.game;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;

import java.awt.*;

public class Minimap {
	private final Client client;

	public Minimap(Client client) {
		this.client = client;
	}

	public Point getPoint(NPC npc) {
		LocalPoint localPoint = npc.getLocalLocation();
		net.runelite.api.Point rlPoint = Perspective.localToMinimap(client, localPoint);
		return new Point(rlPoint.getX(), rlPoint.getY());
	}
}