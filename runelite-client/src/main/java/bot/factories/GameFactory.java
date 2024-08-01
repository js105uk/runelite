package bot.factories;

import bot.model.game.Game;
import bot.model.game.Minimap;
import bot.model.game.Scene;
import bot.model.game.Viewport;
import net.runelite.api.Client;

public class GameFactory {
	public static Game create(Client client) {
		Game result = new Game();
		Scene scene = new Scene(client);
		result.setScene(scene);
		Viewport viewport = new Viewport(client);
		result.setViewport(viewport);
		Minimap minimap = new Minimap(client);
		result.setMinimap(minimap);
		return result;
	}
}
