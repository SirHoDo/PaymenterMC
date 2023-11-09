package me.ethanprimmer.iungo.handlers.donations;
import com.google.gson.JsonObject;
import me.ethanprimmer.iungo.Main;
import org.bukkit.Bukkit;

public class DonationCommands {
	private final Main plugin;
	public DonationCommands(Main plugin) {
		this.plugin = plugin;
	}

	public String handle(JsonObject jsonContent) {
		if (jsonContent.has("commands")) {
			String commandsString = jsonContent.get("commands").getAsString();
			String username = jsonContent.getAsJsonObject("config").get("username").getAsString();

			commandsString = commandsString.replaceAll("/(\\w+)", "$1");
			commandsString = commandsString.replaceAll("%username%", username);
			String[] commandsArray = commandsString.split(", ");

			Bukkit.getScheduler().runTask(plugin, () -> {
				for (String command : commandsArray) {
					plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
				}
			});
			return "complete";
		} else {
			return "No 'commands' field found in JSON data";
		}
	}

}







