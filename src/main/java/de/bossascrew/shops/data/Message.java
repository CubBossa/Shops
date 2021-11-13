package de.bossascrew.shops.data;

import de.bossascrew.shops.ShopPlugin;
import de.bossascrew.shops.handler.TranslationHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

import java.util.ArrayList;
import java.util.List;

public enum Message {

	GENERAL_NO_PERMISSION("general.no_permission"),
	GENERAL_GUI_BACK_NAME("general.gui.back.name"),
	GENERAL_GUI_BACK_LORE("general.gui.back.lore"),
	GENERAL_GUI_ERROR_NAME("general.gui.error.name"),
	GENERAL_GUI_ERROR_LORE("general.gui.error.lore"),
	GENERAL_GUI_DELETE_NAME("general.gui.delete.name"),
	GENERAL_GUI_DELETE_LORE("general.gui.delete.lore"),
	GENERAL_GUI_NEXT_PAGE_NAME("general.gui.next_page.name"),
	GENERAL_GUI_NEXT_PAGE_LORE("general.gui.next_page.lore"),
	GENERAL_GUI_PREV_PAGE_NAME("general.gui.prev_page.name"),
	GENERAL_GUI_PREV_PAGE_LORE("general.gui.prev_page.lore"),
	GENERAL_GUI_ACCEPT_NAME("general.gui.accept.name"),
	GENERAL_GUI_ACCEPT_LORE("general.gui.accept.lore"),
	GENERAL_GUI_DECLINE_NAME("general.gui.decline.name"),
	GENERAL_GUI_DECLINE_LORE("general.gui.decline.lore"),

	SHOP_NO_PERMISSION("shop.no_permission"),
	SHOP_NOT_ENABLED("shop.not_enabled"),

	SHOP_GUI_TITLE("shop.gui.title"),
	SHOP_ITEM_LORE_PRICE("shop.gui.item.lore.price"),
	SHOP_ITEM_LORE_DISCOUNT("shop.gui.item.lore.discount"),
	SHOP_ITEM_LORE_LIMIT("shop.gui.item.lore.limit"),
	SHOP_MODE_BUY_NAME("shop.modes.buy.name"),
	SHOP_MODE_BUY_LORE("shop.modes.buy.lore"),
	SHOP_MODE_SELL_NAME("shop.modes.sell.name"),
	SHOP_MODE_SELL_LORE("shop.modes.sell.lore"),

	MANAGER_GUI_MAIN_TITLE("manager.gui.main.title"),
	MANAGER_GUI_MAIN_SHOPS_NAME("manager.gui.main.shops.name"),
	MANAGER_GUI_MAIN_SHOPS_LORE("manager.gui.main.shops.lore"),
	MANAGER_GUI_MAIN_DISCOUNTS_NAME("manager.gui.main.discounts.name"),
	MANAGER_GUI_MAIN_DISCOUNTS_LORE("manager.gui.main.discounts.lore"),
	MANAGER_GUI_MAIN_LIMITS_NAME("manager.gui.main.limits.name"),
	MANAGER_GUI_MAIN_LIMITS_LORE("manager.gui.main.limits.lore"),
	MANAGER_GUI_MAIN_LANGUAGE_NAME("manager.gui.main.language.name"),
	MANAGER_GUI_MAIN_LANGUAGE_LORE("manager.gui.main.language.lore"),
	MANAGER_GUI_MAIN_WEBINTERFACE_NAME("manager.gui.main.webinterface.name"),
	MANAGER_GUI_MAIN_WEBINTERFACE_LORE("manager.gui.main.webinterface.lore"),

	MANAGER_GUI_SHOPS_TITLE("manager.gui.shops.title"),
	MANAGER_GUI_SHOPS_NEW_NAME("manager.gui.shops.new_shop.name"),
	MANAGER_GUI_SHOPS_NEW_LORE("manager.gui.shops.new_shop.lore"),
	MANAGER_GUI_SHOPS_NAME("manager.gui.shops.entry.name"),
	MANAGER_GUI_SHOPS_LORE("manager.gui.shops.entry.lore"),

	MANAGER_GUI_SHOP_SET_NAME_NAME("manager.gui.shops.set_name.name"),
	MANAGER_GUI_SHOP_SET_NAME_LORE("manager.gui.shops.set_name.lore"),
	MANAGER_GUI_SHOP_SET_PERMISSION_NAME("manager.gui.shops.set_permission.name"),
	MANAGER_GUI_SHOP_SET_PERMISSION_LORE("manager.gui.shops.set_permission.lore"),
	MANAGER_GUI_SHOP_SET_TAGS_NAME("manager.gui.shops.set_tags.name"),
	MANAGER_GUI_SHOP_SET_TAGS_LORE("manager.gui.shops.set_tags.lore"),
	MANAGER_GUI_SHOP_SET_LIMITS_NAME("manager.gui.shops.set_limits.name"),
	MANAGER_GUI_SHOP_SET_LIMITS_LORE("manager.gui.shops.set_limits.lore"),
	MANAGER_GUI_SHOP_SET_DISCOUNTS_NAME("manager.gui.shops.set_discounts.name"),
	MANAGER_GUI_SHOP_SET_DISCOUNTS_LORE("manager.gui.shops.set_discounts.lore"),
	MANAGER_GUI_SHOP_SET_ENABLED_NAME("manager.gui.shops.set_enabled.name"),
	MANAGER_GUI_SHOP_SET_ENABLED_LORE("manager.gui.shops.set_enabled.lore"),
	MANAGER_GUI_SHOP_SET_REMEMBER_PAGE_NAME("manager.gui.shops.set_remember_page.name"),
	MANAGER_GUI_SHOP_SET_REMEMBER_PAGE_LORE("manager.gui.shops.set_remember_page.lore"),
	MANAGER_GUI_SHOP_SET_REMEMBER_MODE_NAME("manager.gui.shops.set_remember_mode.name"),
	MANAGER_GUI_SHOP_SET_REMEMBER_MODE_LORE("manager.gui.shops.set_remember_mode.lore"),
	MANAGER_GUI_SHOP_SET_CYCLIC_NAME("manager.gui.shops.set_cyclic.name"),
	MANAGER_GUI_SHOP_SET_CYCLIC_LORE("manager.gui.shops.set_cyclic.lore"),

	MANAGER_GUI_LIMITS("manager.gui.limits.title"),
	MANAGER_GUI_LIMITS_ENTRY_NAME("manager.gui.limits.entry.name"),
	MANAGER_GUI_LIMITS_ENTRY_LORE("manager.gui.limits.entry.lore"),
	MANAGER_GUI_LIMITS_NEW_NAME("manager.gui.limits.new.name"),
	MANAGER_GUI_LIMITS_NEW_LORE("manager.gui.limits.new.lore"),

	MANAGER_GUI_DISCOUNTS("manager.gui.discounts.title"),
	MANAGER_GUI_DISCOUNTS_ENTRY_NAME("manager.gui.discounts.entry.name"),
	MANAGER_GUI_DISCOUNTS_ENTRY_LORE("manager.gui.discounts.entry.lore"),
	MANAGER_GUI_DISCOUNTS_NEW_NAME("manager.gui.discounts.new.name"),
	MANAGER_GUI_DISCOUNTS_NEW_LORE("manager.gui.discounts.new.lore"),



	;

	private final String key;

	Message(String key) {
		this.key = key;
	}

	public Component getTranslation(Template...templates) {
		String format = TranslationHandler.getInstance().getMessage(key);
		return ShopPlugin.getInstance().getMiniMessage().parse(format, templates);
	}

	public List<Component> getTranslations(Template...templates) {
		String[] toFormat = TranslationHandler.getInstance().getMessage(key).split("\n");
		List<Component> result = new ArrayList<>();
		MiniMessage miniMessage = ShopPlugin.getInstance().getMiniMessage();
		for(String string : toFormat) {
			result.add(miniMessage.parse(string));
		}
		return result;
	}
}
