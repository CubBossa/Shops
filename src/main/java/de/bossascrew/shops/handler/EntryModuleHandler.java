package de.bossascrew.shops.handler;

import de.bossascrew.shops.data.Message;
import de.bossascrew.shops.hook.VaultHook;
import de.bossascrew.shops.menu.ListMenuElement;
import de.bossascrew.shops.menu.ListMenuElementHolder;
import de.bossascrew.shops.shop.PaginatedShop;
import de.bossascrew.shops.shop.entry.*;
import de.bossascrew.shops.util.ItemStackUtils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
public class EntryModuleHandler implements ListMenuElementHolder<EntryModuleHandler.EntryModuleProvider> {

	public static PageModule openExactPage(ShopEntry shopEntry, int page) {
		PageBaseModule pageModule = new PageBaseModule(Message.MANAGER_GUI_ENTRY_FUNCTION_EXACT_PAGE_NAME,
				Message.MANAGER_GUI_ENTRY_FUNCTION_EXACT_PAGE_LORE, shopEntry, (customer, se, integer) -> {
			if (se.getShop() instanceof PaginatedShop ps) {
				ps.open(customer, integer);
			}
		});
		pageModule.setNewPage(page);
		return pageModule;
	}

	public static PageModule openNextPage(ShopEntry shopEntry, int page) {
		PageBaseModule pageModule = new PageBaseModule(Message.MANAGER_GUI_ENTRY_FUNCTION_NEXT_PAGE_NAME,
				Message.MANAGER_GUI_ENTRY_FUNCTION_NEXT_PAGE_LORE, shopEntry, (customer, se, integer) -> {
			System.out.println(se);
			System.out.println(se.getShop());
			if (se.getShop() instanceof PaginatedShop ps) {
				ps.open(customer, integer + page);
			}
		});
		pageModule.setNewPageRelative(page);
		return pageModule;
	}

	public static PageModule openPrevPage(ShopEntry shopEntry, int page) {
		PageBaseModule pageModule = new PageBaseModule(Message.MANAGER_GUI_ENTRY_FUNCTION_PREV_PAGE_NAME,
				Message.MANAGER_GUI_ENTRY_FUNCTION_PREV_PAGE_LORE, shopEntry, (customer, se, integer) -> {
			if (se.getShop() instanceof PaginatedShop ps) {
				ps.open(customer, integer - page);
			}
		});
		pageModule.setNewPageRelative(-1 * page);
		return pageModule;
	}

	public static TradeModule<ItemStack> tradeItem(ItemStack article) {
		return new TradeBaseModule<>(CurrencyHandler.CURRENCY_ITEM, 1, new ItemStack(Material.EMERALD), article);
	}

	public static TradeModule<Void> tradeMoney(ItemStack article) {
		return new TradeBaseModule<>(VaultHook.CURRENCY_VAULT, 10, null, article);
	}

	//TODO andere defaults

	@Getter
	private static EntryModuleHandler instance;

	private final Map<String, EntryModuleProvider> entryModules;

	public EntryModuleHandler() {
		instance = this;
		entryModules = new LinkedHashMap<>();
	}

	public void registerEntryModule(String key, ItemStack itemStack, Message name, Message lore, Function<ShopEntry, EntryModule> moduleSupplier) {
		entryModules.put(key, new EntryModuleProvider(key, itemStack, name, lore, moduleSupplier));
	}

	/**
	 * Removes the EntryModule from the EntryEditor Menu. Does not remove the module from each entry.
	 *
	 * @param key the EntryModule to remove from the EntryModule selection menu.
	 */
	public void unregisterEntryModule(String key) {
		entryModules.remove(key);
	}

	public void registerDefaults() {
		registerEntryModule("static", new ItemStack(Material.BLACK_STAINED_GLASS), Message.MANAGER_GUI_ENTRY_FUNCTION_STATIC_NAME,
				Message.MANAGER_GUI_ENTRY_FUNCTION_STATIC_LORE, shopEntry -> null);
		registerEntryModule("exact_page", new ItemStack(Material.BOOK), Message.MANAGER_GUI_ENTRY_FUNCTION_EXACT_PAGE_NAME,
				Message.MANAGER_GUI_ENTRY_FUNCTION_EXACT_PAGE_LORE, shopEntry -> openExactPage(shopEntry, 1));
		registerEntryModule("next_page", new ItemStack(Material.BOOK), Message.MANAGER_GUI_ENTRY_FUNCTION_NEXT_PAGE_NAME,
				Message.MANAGER_GUI_ENTRY_FUNCTION_NEXT_PAGE_LORE, shopEntry -> openExactPage(shopEntry, 1));
		registerEntryModule("prev_page", new ItemStack(Material.BOOK), Message.MANAGER_GUI_ENTRY_FUNCTION_PREV_PAGE_NAME,
				Message.MANAGER_GUI_ENTRY_FUNCTION_PREV_PAGE_LORE, shopEntry -> openExactPage(shopEntry, 1));
	}

	@Override
	public List<EntryModuleProvider> getValues() {
		return entryModules.values().stream().toList();
	}

	public static class EntryModuleProvider implements ListMenuElement {
		@Getter
		private final String key;
		private final ItemStack itemStack;
		private final Message name;
		private final Message lore;
		private final Function<ShopEntry, EntryModule> moduleSupplier;

		public EntryModuleProvider(String key, ItemStack itemStack, Message name, Message lore, Function<ShopEntry, EntryModule> moduleSupplier) {
			this.key = key;
			this.itemStack = itemStack;
			this.name = name;
			this.lore = lore;
			this.moduleSupplier = moduleSupplier;
		}

		public EntryModule getModule(ShopEntry shopEntry) {
			return moduleSupplier.apply(shopEntry);
		}

		@Override
		public Component getName() {
			return name.getTranslation();
		}

		@Override
		public ItemStack getListDisplayItem() {
			return ItemStackUtils.createItemStack(itemStack, name, lore);
		}
	}
}
