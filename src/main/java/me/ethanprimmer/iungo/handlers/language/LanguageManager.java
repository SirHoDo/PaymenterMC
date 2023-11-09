package me.ethanprimmer.iungo.handlers.language;

import me.despical.commons.file.FileUtils;
import me.despical.commons.util.Collections;
import me.ethanprimmer.iungo.Main;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class LanguageManager {

	private final Main plugin;
	private Locale pluginLocale;

	public LanguageManager(Main plugin) {
		this.plugin = plugin;

		registerLocales();
		setupLocale();
		init();
	}

	private void init() {
		if (Collections.contains(plugin.getChatManager().message("language"), pluginLocale.aliases)) return;

		try {
			FileUtils.copyURLToFile(new URL("https://iungo.info/locals/minecraft/MinecraftDonationStore" + pluginLocale.prefix + ".yml"), new File(plugin.getDataFolder(), "messages.yml"));
		} catch (IOException e) {
			plugin.getLogger().warning("Error while connecting to Language server!");
		}
	}

	private void registerLocales() {
		Collections.listOf(
				new Locale("English", "en_GB", "Ethan Primmer", "default", "english", "en"))
			.forEach(LocaleRegistry::registerLocale);
	}

	private void setupLocale() {
		String localeName = plugin.getConfig().getString("locale", "default").toLowerCase();

		for (Locale locale : LocaleRegistry.getRegisteredLocales()) {
			if (locale.prefix.equalsIgnoreCase(localeName)) {
				pluginLocale = locale;
				break;
			}

			for (String alias : locale.aliases) {
				if (alias.equals(localeName)) {
					pluginLocale = locale;
					break;
				}
			}
		}

		if (pluginLocale == null) {
			pluginLocale = LocaleRegistry.getByName("English");
			plugin.getLogger().warning("Selected locale is invalid! Using default locale.");
			return;
		}

		plugin.getLogger().info("Loaded locale " + pluginLocale.name + " (ID: " + pluginLocale.prefix + " by " + pluginLocale.author + ")");
	}

	public Locale getPluginLocale() {
		return pluginLocale;
	}

	public static class Locale {

		public final String name, prefix, author, aliases[];

		Locale(String name, String prefix, String author, String... aliases) {
			this.prefix = prefix;
			this.name = name;
			this.author = author;
			this.aliases = aliases;
		}
	}
}