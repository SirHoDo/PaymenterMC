package me.ethanprimmer.iungo.handlers;

import me.despical.commons.configuration.ConfigUtils;
import me.despical.commons.util.Strings;
import me.ethanprimmer.iungo.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class ChatManager {

	private FileConfiguration config;
	private final Main plugin;
	private final String prefix;

	public ChatManager(Main plugin) {
		this.plugin = plugin;
		this.config = ConfigUtils.getConfig(plugin, "messages");
		this.prefix = message("in_game.plugin_prefix");
	}
	public String coloredRawMessage(String message) {
		return Strings.format(message);
	}

	public String message(String path) {
		path = me.despical.commons.string.StringUtils.capitalize(path.replace('_', '-'), '-', '.');
		return coloredRawMessage(config.getString(path));
	}

	public String prefixedMessage(String path) {
		return prefix + message(path);
	}

	public void reloadConfig() {
		config = ConfigUtils.getConfig(plugin, "messages");
	}

}