package net.runelite.client.plugins.chicken_fighter;

import bot.Common;
import bot.model.ScriptBase;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.input.KeyManager;
import net.runelite.client.ui.overlay.OverlayManager;

import java.awt.event.MouseEvent;
import java.util.List;

import java.awt.*;

public class ChickenFighterScript extends ScriptBase {
	private NPC chicken;
	private List<WorldPoint> path;

	public ChickenFighterScript(Client client, OverlayManager overlayManager, KeyManager keyManager) {
		super(client, overlayManager, keyManager);
	}

	@Override
	public void render(Graphics2D graphics) {
		if(chicken != null) {
			Shape shape = chicken.getConvexHull();
			if(shape != null) {
				graphics.setColor(new Color(255, 0, 0, 200));
				graphics.fill(shape);
				graphics.setColor(Color.WHITE);
				graphics.draw(shape);
			}
		}
		if(path != null) {
			for(WorldPoint worldPoint : path) {
				LocalPoint localPoint = LocalPoint.fromWorld(client.getLocalPlayer().getWorldView(), worldPoint);
				if(localPoint != null) {
					Polygon polygon = Perspective.getCanvasTilePoly(client, localPoint);
					if(polygon != null) {
						graphics.setColor(new Color(0, 255, 0, 50));
						graphics.fill(polygon);
						graphics.setColor(Color.GREEN);
						graphics.draw(polygon);
					}
				}
			}
		}
	}

	@Override
	public void onGameLoop() {
		if(!client.getLocalPlayer().isInteracting()) {
			path = null;
			chicken = game.getScene().getNPC(this::npcFilter);
			if(chicken != null) {
				path = bot.getWalker().getPathFinder().find(chicken.getWorldLocation());
				if(game.getViewport().contains(chicken)) {
					if(bot.interact(chicken)) {
						Common.sleepUntil(client.getLocalPlayer()::isInteracting, 100, 7500);
					}
				} else {
					WorldPoint chickenWorldPoint = chicken.getWorldLocation();
					Point miniMapPoint = game.getMinimap().getPoint(chicken);
					bot.getInput().getMouse().click(miniMapPoint.x, miniMapPoint.y, MouseEvent.BUTTON1);
					Common.sleepUntil(() -> client.getLocalPlayer().getWorldLocation().distanceTo2D(chickenWorldPoint) <= 3, 600, 7500);
				}
			}
		}
		Common.sleep(600);
	}

	private boolean npcFilter(NPC npc) {
		return
			npc != null
			&& npc.getName() != null
			&& npc.getName().equals("Chicken")
			&& !npc.isInteracting()
			&& !npc.isDead()
			&& bot.getWalker().getPathFinder().find(npc.getWorldLocation()) != null;
	}
}