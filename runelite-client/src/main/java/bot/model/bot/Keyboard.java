package bot.model.bot;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;

public class Keyboard {
	private Client client;

	public Keyboard(Client client) {
		this.client = client;
	}
}
