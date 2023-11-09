package me.ethanprimmer.iungo.handlers.language;

import me.ethanprimmer.iungo.handlers.language.LanguageManager.Locale;
import java.util.HashSet;
import java.util.Set;

public class LocaleRegistry {
	private static final Set<Locale> registeredLocales = new HashSet<>();
	public static void registerLocale(Locale locale) {
		registeredLocales.removeIf(l -> l.prefix.equals(locale.prefix));
		registeredLocales.add(locale);
	}

	public static Set<Locale> getRegisteredLocales() {
		return registeredLocales;
	}

	public static Locale getByName(String name) {
		for (Locale locale : registeredLocales) {
			if (locale.name.equals(name)) {
				return locale;
			}
		}

		return null;
	}
}