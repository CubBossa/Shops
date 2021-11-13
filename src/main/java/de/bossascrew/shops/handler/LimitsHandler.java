package de.bossascrew.shops.handler;

import de.bossascrew.shops.Customer;
import de.bossascrew.shops.menu.ShopMenuView;
import de.bossascrew.shops.shop.Limit;
import de.bossascrew.shops.shop.entry.ShopEntry;
import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.util.*;

@Getter
public class LimitsHandler {

	@Getter
	private static LimitsHandler instance;

	private final Map<UUID, Limit> limitMap;
	private final Map<String, Collection<Limit>> tagMap;

	//TODO schöner strukturieren, besser durchdenken
	//TODO alle menüs aktuallisieren, wenn sich ein globales limit verändert

	public LimitsHandler() {
		instance = this;

		this.limitMap = new HashMap<>();
		this.tagMap = new HashMap<>();
	}

	public void handleLimitDisplay(ShopMenuView inventory, ShopEntry shopEntry, List<Component> existingLore) {

	}

	public void isLimited(Customer customer, String... tags) {
		for (String tag : tags) {
			for (Limit limit : tagMap.getOrDefault(tag, new HashSet<>())) {
				//TODO transactionmap des customers prüfen

			}
		}
	}
}
