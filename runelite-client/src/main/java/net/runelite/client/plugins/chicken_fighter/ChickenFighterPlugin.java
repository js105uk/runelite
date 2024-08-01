package net.runelite.client.plugins.chicken_fighter;

import bot.model.ScriptBase;
import net.runelite.api.Client;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

@PluginDescriptor(name="Chicken Fighter")
public class ChickenFighterPlugin extends Plugin {
	private ChickenFighterScript script;
	@Inject
	private Client client;
	@Inject
	private KeyManager keyManager;
	@Inject
	private OverlayManager overlayManager;

	@Override
	protected void startUp() throws Exception {
		script = new ChickenFighterScript(client, overlayManager, keyManager);
		script.startUp();
	}

	@Override
	protected void shutDown() throws Exception {
		script.shutDown();
	}
}
