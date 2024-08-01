package bot.model.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Game {
	private Scene scene;
	private Viewport viewport;
	private Minimap minimap;
}
