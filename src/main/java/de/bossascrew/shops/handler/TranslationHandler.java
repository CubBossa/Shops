package de.bossascrew.shops.handler;

import de.bossascrew.shops.ShopPlugin;
import de.bossascrew.shops.data.LoadedMessage;
import de.bossascrew.shops.data.Message;
import de.bossascrew.shops.util.LoggingPolicy;
import de.bossascrew.shops.web.WebAccessable;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TranslationHandler implements WebAccessable<LoadedMessage> {

	@Getter
	private static TranslationHandler instance;

	private String activeLanguage = "none";
	private final Map<String, String> messageFormats;

	public TranslationHandler(String startlanguage) {
		instance = this;
		messageFormats = new HashMap<>();
		loadLanguage(startlanguage);
	}

	public CompletableFuture<Boolean> loadLanguage(String languageKey) {
		activeLanguage = languageKey;
		messageFormats.clear();

		return CompletableFuture.supplyAsync(() -> {

			File file = new File(ShopPlugin.getInstance().getDataFolder(), "lang/" + languageKey + ".yml");
			if (!file.exists()) {
				ShopPlugin.getInstance().saveResource("lang/" + languageKey + ".yml", false);
				file = new File(ShopPlugin.getInstance().getDataFolder(), "lang/" + languageKey + ".yml");
				if (!file.exists()) {

					ShopPlugin.getInstance().log(LoggingPolicy.ERROR, "Error while creating language.yml for " + languageKey);
					return false;
				}
			}
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			Map<String, Object> map = cfg.getValues(true);
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (entry.getValue() instanceof String s) {
					messageFormats.put(entry.getKey(), s);
				}
			}
			return true;
		});
	}

	public String getMessage(String key) {
		return messageFormats.getOrDefault(key, activeLanguage + "-missing:" + key);
	}

	@Override
	public List<LoadedMessage> getWebData() {
		List<LoadedMessage> messages = new ArrayList<>();
		for (Message message : Message.values()) {
			messages.add(new LoadedMessage(message.getKey(), getMessage(message.getKey()), message.getExamplePlaceholders()));
		}
		return messages;
	}

	@Override
	public void storeWebData(List<LoadedMessage> values) {

	}
}
