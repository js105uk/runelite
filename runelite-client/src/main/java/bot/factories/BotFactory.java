package bot.factories;

import bot.model.bot.*;
import net.runelite.api.Client;

public class BotFactory {
	public static Bot create(Client client) {
		Bot result          = new Bot();
		Input input         = new Input();
		Mouse mouse         = new Mouse(client);
		Keyboard keyboard   = new Keyboard(client);
		input.setMouse(mouse);
		input.setKeyboard(keyboard);
		result.setInput(input);
		Walker walker = new Walker(client);
		PathFinder pathFinder = new PathFinder(client);
		walker.setPathFinder(pathFinder);
		result.setWalker(walker);
		return result;
	}
}