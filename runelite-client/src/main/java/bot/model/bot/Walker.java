package bot.model.bot;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;

import java.util.List;

@Setter
@Getter
public class Walker {
	private final Client client;
	private PathFinder pathFinder;

	public Walker(Client client) {
		this.client = client;
	}

	public void walk(WorldPoint destination) {
		List<WorldPoint> path = pathFinder.find(destination);
	}
}
