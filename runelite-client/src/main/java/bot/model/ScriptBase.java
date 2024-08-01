package bot.model;

import bot.factories.BotFactory;
import bot.factories.GameFactory;
import bot.model.bot.Bot;
import bot.model.bot.ScriptOverlay;
import bot.model.game.Game;
import net.runelite.api.Client;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.ui.overlay.OverlayManager;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class ScriptBase implements KeyListener {
	public      final   Client          client;
	private     final   OverlayManager  overlayManager;
	private     final   KeyManager      keyManager;
	private     final   ScriptOverlay   overlay;
	private             boolean         isRunning;
	protected           Bot             bot;
	protected           Game            game;

	public ScriptBase(Client client, OverlayManager overlayManager, KeyManager keyManager) {
		this.client = client;
		this.overlayManager = overlayManager;
		this.keyManager = keyManager;
		this.overlay = new ScriptOverlay(this);
	}

	public void startUp() {
		overlayManager.add(overlay);
		keyManager.registerKeyListener(this);
	}

	public void shutDown() {
		overlayManager.remove(overlay);
		keyManager.unregisterKeyListener(this);
	}

	public abstract void render(Graphics2D graphics);
	public abstract void onGameLoop();

	@Override public void keyTyped(KeyEvent e) { }
	@Override public void keyPressed(KeyEvent e) { }

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.isControlDown()) {
			if(e.getKeyCode() == KeyEvent.VK_S) {
				if(isRunning) {
					stopGameLoop();
				} else {
					startGameLoop();
				}
			}
		}
	}

	private void startGameLoop() {
		isRunning = true;
		bot = BotFactory.create(client);
		game = GameFactory.create(client);
		Runnable gameLoop = () -> {
			while(isRunning) {
				onGameLoop();
			}
		};
		Thread thread = new Thread(gameLoop);
		thread.start();
	}

	private void stopGameLoop() {
		isRunning = false;
		bot = null;
		game = null;
	}
}