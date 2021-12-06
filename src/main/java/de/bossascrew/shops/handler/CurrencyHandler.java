package de.bossascrew.shops.handler;

import de.bossascrew.shops.Customer;
import de.bossascrew.shops.shop.Currency;
import de.bossascrew.shops.util.ItemStackUtils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CurrencyHandler {

	@Getter
	private static CurrencyHandler instance;

	public static final Currency<ItemStack> CURRENCY_ITEM = new Currency<>("<amount>x <currency>", (integer, itemStack) -> {
		return Component.translatable("item.minecraft." + itemStack.getType().toString().toLowerCase());
	}) {
		@Override
		public double getAmount(Customer customer, ItemStack object) {
			int count = 0;
			for (ItemStack i : customer.getPlayer().getInventory()) {
				if (i.isSimilar(object)) {
					count += i.getAmount();
				}
			}
			return count;
		}

		@Override
		public boolean addAmount(Customer customer, double amount, ItemStack object) {
			object = object.clone();
			object.setAmount((int) amount);
			ItemStackUtils.giveOrDrop(customer.getPlayer(), object);
			return true;
		}

		@Override
		public boolean removeAmount(Customer customer, double amount, ItemStack object) {
			Map<Integer, ItemStack> removableStacks = new HashMap<>();
			for (int slot = 0; slot < customer.getPlayer().getInventory().getSize(); slot++) {
				ItemStack i = customer.getPlayer().getInventory().getItem(slot);
				if (i == null) {
					continue;
				}
				if (i.isSimilar(object)) {
					removableStacks.put(slot, i);
				}
			}
			int removed = 0;
			for (Map.Entry<Integer, ItemStack> entry : removableStacks.entrySet()) {
				int a = entry.getValue().getAmount();
				if (removed + a > amount) {
					customer.getPlayer().getInventory().getItem(entry.getKey()).setAmount((int) (removed + a - amount));
					break;
				}
				customer.getPlayer().getInventory().setItem(entry.getKey(), null);
			}
			return true;
		}
	};

	Map<String, Currency<?>> currencies;

	public CurrencyHandler() {
		instance = this;
		this.currencies = new HashMap<>();

		registerCurrency("item", CURRENCY_ITEM);
	}

	public <T> void registerCurrency(String key, Currency<T> currency) {
		this.currencies.put(key, currency);
	}

	public void unregisterCurrency(String key) {
		this.currencies.remove(key);
	}
}