package me.ethanprimmer.iungo.events;

import me.despical.commons.util.UpdateChecker;
import me.ethanprimmer.iungo.ConfigPreferences;
import me.ethanprimmer.iungo.Main;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;

public class Events extends ListenerAdapter {
	private Main plugin;
	public Events(Main plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoinCheckVersion(PlayerJoinEvent event) {
		if (!preferences.getOption(ConfigPreferences.Option.UPDATE_NOTIFIER_ENABLED) || !event.getPlayer().hasPermission("paymenter.admin.updatenotify")) {
			return;
		}

		plugin.getServer().getScheduler().runTaskLater(plugin, () -> UpdateChecker.init(plugin, 112330).requestUpdateCheck().whenComplete((result, exception) -> {

			final Player player = event.getPlayer();

			UpdateChecker.UpdateReason reason = result.getReason();
			switch (reason) {
				case NEW_UPDATE:
					player.sendMessage(chatManager.coloredRawMessage("&a[Paymenter] &bFound an update: v" + result.getNewestVersion() + " Download:"));
					player.sendMessage(chatManager.coloredRawMessage("&a» &6https://www.spigotmc.org/resources/112330/"));
					break;
				case UNSUPPORTED_VERSION_SCHEME:
					player.sendMessage(chatManager.coloredRawMessage("&a[Paymenter] &cUnsupported version scheme."));
					player.sendMessage(chatManager.coloredRawMessage("&a» &6https://www.spigotmc.org/resources/112330/"));
					break;
				case UNRELEASED_VERSION:
					player.sendMessage(chatManager.coloredRawMessage("&a[Paymenter] &cYou are using an unreleased version."));
					player.sendMessage(chatManager.coloredRawMessage("&a» &6https://www.spigotmc.org/resources/112330/"));
					break;
				case UP_TO_DATE:
					break;
				case COULD_NOT_CONNECT:
					player.sendMessage(chatManager.coloredRawMessage("&a[Paymenter] &cCould not connect to update server."));
					player.sendMessage(chatManager.coloredRawMessage("&a» &6https://www.spigotmc.org/resources/112330/"));
					break;
				case INVALID_JSON:
					player.sendMessage(chatManager.coloredRawMessage("&a[Paymenter] &cInvalid JSON response from update server."));
					player.sendMessage(chatManager.coloredRawMessage("&a» &6https://www.spigotmc.org/resources/112330/"));
					break;
				case UNAUTHORIZED_QUERY:
					player.sendMessage(chatManager.coloredRawMessage("&a[Paymenter] &cUnauthorized query to update server."));
					player.sendMessage(chatManager.coloredRawMessage("&a» &6https://www.spigotmc.org/resources/112330/"));
					break;
				case UNKNOWN_ERROR:
					player.sendMessage(chatManager.coloredRawMessage("&a[Paymenter] &cUnknown error while checking for updates."));
					player.sendMessage(chatManager.coloredRawMessage("&a» &6https://www.spigotmc.org/resources/112330/"));
					break;
			}

		}), 25);
	}
}