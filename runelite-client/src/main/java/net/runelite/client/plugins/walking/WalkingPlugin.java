package net.runelite.client.plugins.walking;

import net.runelite.api.Client;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

@PluginDescriptor(name="Walking")
public class WalkingPlugin extends Plugin {
	private WalkingScript script;
	@Inject
	private Client client;
	@Inject
	private KeyManager keyManager;
	@Inject
	private OverlayManager overlayManager;

	@Override
	protected void startUp() throws Exception {
		script = new WalkingScript(client, overlayManager, keyManager);
		script.startUp();
	}

	@Override
	protected void shutDown() throws Exception {
		script.shutDown();
	}
}
