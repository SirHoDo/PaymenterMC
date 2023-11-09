package me.ethanprimmer.iungo;

import me.ethanprimmer.iungo.events.ListenerAdapter;
import me.ethanprimmer.iungo.handlers.AuthorizationFilter;
import me.ethanprimmer.iungo.handlers.ChatManager;
import me.ethanprimmer.iungo.handlers.language.LanguageManager;
import me.ethanprimmer.iungo.webhooks.PaymentWebhook;
import org.bstats.bukkit.Metrics;
import me.despical.commons.util.Collections;
import me.despical.commons.util.UpdateChecker;
import me.ethanprimmer.iungo.commands.CommandHandler;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.io.File;

import spark.Spark;

public class Main extends JavaPlugin {
	private ConfigPreferences configPreferences;
	private CommandHandler commandHandler;
	private ChatManager chatManager;
	private LanguageManager languageManager;

	@Override
	public void onEnable() {
		setupFiles();
		initializeClasses();
		checkUpdate();

		registerWebhookEndpoints();

		getLogger().info("Initialization finished." + getConfig().getInt("port", 3000));
	}
	private void initializeClasses() {
		configPreferences = new ConfigPreferences(this);
		chatManager = new ChatManager(this);
		languageManager = new LanguageManager(this);
		commandHandler = new CommandHandler(this);
		ListenerAdapter.registerEvents(this);
		startPluginMetrics();
	}

	private void registerWebhookEndpoints() {
		Spark.port(getConfig().getInt("port", 3000));

		Spark.before(new AuthorizationFilter(this));

		Spark.post("/webhooks/complete", (request, response) -> {

			PaymentWebhook paymentWebhook = new PaymentWebhook(this);

			return paymentWebhook.handle(request, response);
		});
	}

	private void checkUpdate() {
		if (!configPreferences.getOption(ConfigPreferences.Option.UPDATE_NOTIFIER_ENABLED)) return;
		UpdateChecker.init(this, 98943).requestUpdateCheck().whenComplete((result, exception) -> {
			if (result.requiresUpdate()) {
				getLogger().info("Found a new version available: v" + result.getNewestVersion());
				getLogger().info("Download it SpigotMC:");
				getLogger().info("https://iungo.info/downloads/MinecraftDonationStore");
			}
		});
	}

	private void startPluginMetrics() {
		Metrics metrics = new Metrics(this, 20238);

		metrics.addCustomChart(new SimplePie("locale_used", () -> languageManager.getPluginLocale().prefix));
		metrics.addCustomChart(new SimplePie("update_notifier", () -> String.valueOf(getConfig().getBoolean("getUpdateCheck", false))));
	}

	private void setupFiles() {
		Collections.streamOf("messages").filter(name -> !new File(getDataFolder(), name + ".yml").exists()).forEach(name -> saveResource(name + ".yml", false));
	}
	@NotNull
	public ConfigPreferences getConfigPreferences() {
		return configPreferences;
	}
	@NotNull
	public ChatManager getChatManager() {
		return chatManager;
	}
	@NotNull
	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

}
