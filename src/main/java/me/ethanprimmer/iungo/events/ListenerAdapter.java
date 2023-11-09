package me.ethanprimmer.iungo.events;

import me.ethanprimmer.iungo.ConfigPreferences;
import me.ethanprimmer.iungo.Main;
import me.ethanprimmer.iungo.handlers.ChatManager;
import org.bukkit.event.Listener;

public abstract class ListenerAdapter implements Listener {

	protected final Main plugin;
	protected final ChatManager chatManager;
	protected final ConfigPreferences preferences;

	public ListenerAdapter(Main plugin) {
		this.plugin = plugin;
		this.chatManager = plugin.getChatManager();
		this.preferences = plugin.getConfigPreferences();
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public static void registerEvents(Main plugin) {
		final Class<?>[] listenerAdapters = {Events.class};

		try {
			for (final Class<?> listenerAdapter : listenerAdapters) {
				listenerAdapter.getConstructor(Main.class).newInstance(plugin);
			}
		} catch (Exception ignored) {
			plugin.getLogger().warning("Exception occurred on event registering.");
		}
	}
}