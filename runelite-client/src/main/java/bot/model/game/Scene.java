package bot.model.game;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Scene {
	private final Client client;

	public Scene(Client client) {
		this.client = client;
	}

	public List<NPC> getNPCs(Predicate<NPC> filter) {
		List<NPC> result = client.getLocalPlayer().getWorldView().npcs().stream().filter(filter).collect(Collectors.toList());
		WorldPoint playerWorldPoint = client.getLocalPlayer().getWorldLocation();
		Collections.sort(result, (o1, o2) -> {
			int distance1 = o1.getWorldLocation().distanceTo(playerWorldPoint);
			int distance2 = o2.getWorldLocation().distanceTo(playerWorldPoint);
			if(distance1 < distance2) {
				return -1;
			} else if (distance1 == distance2) {
				return 0;
			} else {
				return 1;
			}
		});
		return result;
	}

	public NPC getNPC(Predicate<NPC> filter) {
		List<NPC> npcs = getNPCs(filter);
		if(!npcs.isEmpty()) {
			return npcs.get(0);
		}
		return null;
	}
}