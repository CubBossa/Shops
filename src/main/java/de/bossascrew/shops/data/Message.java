package de.bossascrew.shops.data;

import de.bossascrew.shops.ShopPlugin;
import de.bossascrew.shops.handler.TranslationHandler;
import de.bossascrew.shops.util.ComponentUtils;
import de.bossascrew.shops.util.Pair;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public enum Message {

	PREFIX("general.prefix"),

	GENERAL_NO_PERMISSION("general.no_permission"),
	GENERAL_CONFIG_RELOADED_IN_MS("general.config_reloaded"),
	GENERAL_CONFIG_RELOAD_ERROR("general.config_error"),
	GENERAL_LANGUAGE_RELOADED_IN_MS("general.language_reloaded"),
	GENERAL_LANGUAGE_RELOAD_ERROR("general.language_error"),
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
	GENERAL_GUI_LIST_INFO_NAME("general.gui.list_info.name"),
	GENERAL_GUI_LIST_INFO_LORE("general.gui.list_info.lore"),
	GENERAL_WEBINTERFACE_LOADING("general.webinterface.loading"),
	GENERAL_WEBINTERFACE_LINK("general.webinterface.link"),
	GENERAL_WEBINTERFACE_ERROR("general.webinterface.error"),

	CITIZENS_ASSIGNED("citizens.assign.success"),
	CITIZENS_CONFIRM_OVERRIDE("citizens.assign.confirm"),
	CITIZENS_CLICK_TO_ASSIGN("citizens.assign.info"),

	SHOP_NO_PERMISSION("shop.no_permission"),
	SHOP_NOT_ENABLED("shop.not_enabled"),
	SHOP_COOLDOWN("shop.cooldown"),

	SHOP_GUI_TITLE("shop.gui.title",
			"Defines the title of the shop menu",
			new Pair<>("name", "<gradient:dark_green:green:dark_green>Example Shop"),
			new Pair<>("page", "" + 2),
			new Pair<>("mode", "<gold>Sell</gold>"),
			new Pair<>("pages", "" + 3)),
	SHOP_ITEM_LORE_PRICE("shop.gui.item.lore.price"),
	SHOP_ITEM_LORE_DISCOUNT("shop.gui.item.lore.discount"),
	SHOP_ITEM_LORE_LIMIT("shop.gui.item.lore.limit"),
	SHOP_MODE_BUY_NAME("shop.modes.buy.name"),
	SHOP_MODE_BUY_LORE("shop.modes.buy.lore"),
	SHOP_MODE_SELL_NAME("shop.modes.sell.name"),
	SHOP_MODE_SELL_LORE("shop.modes.sell.lore"),
	SHOP_MODE_TRADE_NAME("shop.modes.trade.name"),
	SHOP_MODE_TRADE_LORE("shop.modes.trade.lore"),

	VILLAGER_SHOP_TITLE("villager_shop.gui.title"),

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
	MANAGER_GUI_SHOPS_DELETE_CONFIRM("manager.gui.shops.confirm_delete"),
	MANAGER_GUI_SHOPS_NEW_TITLE("manager.gui.shops.new_shop.title"),
	MANAGER_GUI_SHOPS_NEW_NAME("manager.gui.shops.new_shop.name"),
	MANAGER_GUI_SHOPS_NEW_LORE("manager.gui.shops.new_shop.lore"),
	MANAGER_GUI_SHOPS_NAME("manager.gui.shops.entry.name"),
	MANAGER_GUI_SHOPS_LORE("manager.gui.shops.entry.lore"),
	MANAGER_GUI_SHOPS_ALREADY_EDITED("manager.gui.shops.already_edited"),

	MANAGER_GUI_SHOP_SET_NAME_TITLE("manager.gui.shop.set_name.title"),
	MANAGER_GUI_SHOP_SET_NAME_NAME("manager.gui.shop.set_name.name"),
	MANAGER_GUI_SHOP_SET_NAME_LORE("manager.gui.shop.set_name.lore"),
	MANAGER_GUI_SHOP_SET_PERMISSION_TITLE("manager.gui.shop.set_permission.title"),
	MANAGER_GUI_SHOP_SET_PERMISSION_NAME("manager.gui.shop.set_permission.name"),
	MANAGER_GUI_SHOP_SET_PERMISSION_LORE("manager.gui.shop.set_permission.lore"),
	MANAGER_GUI_SHOP_SET_TAGS_NAME("manager.gui.shop.set_tags.name"),
	MANAGER_GUI_SHOP_SET_TAGS_LORE("manager.gui.shop.set_tags.lore"),
	MANAGER_GUI_SHOP_SET_LIMITS_NAME("manager.gui.shop.set_limits.name"),
	MANAGER_GUI_SHOP_SET_LIMITS_LORE("manager.gui.shop.set_limits.lore"),
	MANAGER_GUI_SHOP_SET_DISCOUNTS_NAME("manager.gui.shop.set_discounts.name"),
	MANAGER_GUI_SHOP_SET_DISCOUNTS_LORE("manager.gui.shop.set_discounts.lore"),
	MANAGER_GUI_SHOP_SET_TEMPLATE_NAME("manager.gui.shop.set_template.name"),
	MANAGER_GUI_SHOP_SET_TEMPLATE_LORE("manager.gui.shop.set_template.lore"),
	MANAGER_GUI_SHOP_SET_NPC_NAME("manager.gui.shop.set_citizens.name",
			"The name of the item in the shop menu, that allows you to assign this shop to an citizens npc."),
	MANAGER_GUI_SHOP_SET_NPC_LORE("manager.gui.shop.set_citizens.lore",
			"The lore of the item in the shop menu, that allows you to assign this shop to an citizens npc."),
	MANAGER_GUI_SHOP_SET_ENABLED_NAME("manager.gui.shop.set_enabled.name"),
	MANAGER_GUI_SHOP_SET_ENABLED_LORE("manager.gui.shop.set_enabled.lore"),
	MANAGER_GUI_SHOP_SET_REMEMBER_PAGE_NAME("manager.gui.shop.set_remember_page.name"),
	MANAGER_GUI_SHOP_SET_REMEMBER_PAGE_LORE("manager.gui.shop.set_remember_page.lore"),
	MANAGER_GUI_SHOP_SET_REMEMBER_MODE_NAME("manager.gui.shop.set_remember_mode.name"),
	MANAGER_GUI_SHOP_SET_REMEMBER_MODE_LORE("manager.gui.shop.set_remember_mode.lore"),
	MANAGER_GUI_SHOP_SET_CONTENT_NAME("manager.gui.shop.set_content.name"),
	MANAGER_GUI_SHOP_SET_CONTENT_LORE("manager.gui.shop.set_content.lore"),
	MANAGER_GUI_SHOP_SET_PREVIEW_NAME("manager.gui.shop.preview.name"),
	MANAGER_GUI_SHOP_SET_PREVIEW_LORE("manager.gui.shop.preview.lore"),
	MANAGER_GUI_SHOP_SET_DEFAULT_MODE_NAME("manager.gui.shop.set_default_mode.name"),
	MANAGER_GUI_SHOP_SET_DEFAULT_MODE_LORE("manager.gui.shop.set_default_mode.lore"),
	MANAGER_GUI_SHOP_SET_DEFAULT_PAGE_NAME("manager.gui.shop.set_default_page.name"),
	MANAGER_GUI_SHOP_SET_DEFAULT_PAGE_LORE("manager.gui.shop.set_default_page.lore"),
	MANAGER_GUI_SHOP_SET_ROWS_NAME("manager.gui.shop.set_rows.name"),
	MANAGER_GUI_SHOP_SET_ROWS_LORE("manager.gui.shop.set_rows.lore"),
	MANAGER_GUI_SHOP_LIMITS_TITLE("manager.gui.shop.limits.title"),
	MANAGER_GUI_SHOP_LIMITS_INFO_NAME("manager.gui.shop.limits.info.name"),
	MANAGER_GUI_SHOP_LIMITS_INFO_LORE("manager.gui.shop.limits.info.lore"),
	MANAGER_GUI_SHOP_DISCOUNTS_TITLE("manager.gui.shop.discounts.title"),
	MANAGER_GUI_SHOP_DISCOUNTS_INFO_NAME("manager.gui.shop.discounts.info.name"),
	MANAGER_GUI_SHOP_DISCOUNTS_INFO_LORE("manager.gui.shop.discounts.info.lore"),
	MANAGER_GUI_SHOP_TEMPLATE_TITLE("manager.gui.shop.template.title"),
	MANAGER_GUI_SHOP_TEMPLATE_INFO_NAME("manager.gui.shop.template.info.name"),
	MANAGER_GUI_SHOP_TEMPLATE_INFO_LORE("manager.gui.shop.template.info.lore"),

	MANAGER_GUI_TAGS_TITLE("manager.gui.tags.title"),
	MANAGER_GUI_TAGS_NEW_TAG_TITLE("manager.gui.tags.new_tag.title"),
	MANAGER_GUI_TAGS_NEW_TAG_NAME("manager.gui.tags.new_tag.name"),
	MANAGER_GUI_TAGS_NEW_TAG_LORE("manager.gui.tags.new_tag.lore"),
	GENERAL_GUI_TAGS_INFO_NAME("manager.gui.tags.info.name"),
	GENERAL_GUI_TAGS_INFO_LORE("manager.gui.tags.info.lore"),
	GENERAL_GUI_TAGS_REMOVE_TAG("manager.gui.tags.remove_tag"),

	MANAGER_GUI_SHOP_EDITOR_APPLY_TEMPLATE_NAME("manager.gui.shop_editor.template.name"),
	MANAGER_GUI_SHOP_EDITOR_APPLY_TEMPLATE_LORE("manager.gui.shop_editor.template.lore"),
	MANAGER_GUI_SHOP_EDITOR_TOGGLE_FREEZE_NAME("manager.gui.shop_editor.freeze.name"),
	MANAGER_GUI_SHOP_EDITOR_TOGGLE_FREEZE_LORE("manager.gui.shop_editor.freeze.lore"),

	MANAGER_GUI_LIMITS("manager.gui.limits.title"),
	MANAGER_GUI_LIMITS_ALREADY_EDITED("manager.gui.limits.already_edited"),
	MANAGER_GUI_LIMITS_DELETE_CONFIRM("manager.gui.limits.confirm_delete"),
	MANAGER_GUI_LIMITS_ENTRY_NAME("manager.gui.limits.entry.name"),
	MANAGER_GUI_LIMITS_ENTRY_LORE("manager.gui.limits.entry.lore"),
	MANAGER_GUI_LIMITS_NEW_TITLE("manager.gui.limits.new.title"),
	MANAGER_GUI_LIMITS_NEW_NAME("manager.gui.limits.new.name"),
	MANAGER_GUI_LIMITS_NEW_LORE("manager.gui.limits.new.lore"),

	MANAGER_GUI_LIMIT_SET_NAME_TITLE("manager.gui.limit.set_name.title"),
	MANAGER_GUI_LIMIT_SET_NAME_NAME("manager.gui.limit.set_name.name"),
	MANAGER_GUI_LIMIT_SET_NAME_LORE("manager.gui.limit.set_name.lore"),
	MANAGER_GUI_LIMIT_SET_TAGS_NAME("manager.gui.limit.set_tags.name"),
	MANAGER_GUI_LIMIT_SET_TAGS_LORE("manager.gui.limit.set_tags.lore"),
	MANAGER_GUI_LIMIT_SET_PERMISSION_TITLE("manager.gui.limit.set_permission.title"),
	MANAGER_GUI_LIMIT_SET_PERMISSION_NAME("manager.gui.limit.set_permission.name"),
	MANAGER_GUI_LIMIT_SET_PERMISSION_LORE("manager.gui.limit.set_permission.lore"),

	MANAGER_GUI_DISCOUNTS("manager.gui.discounts.title"),
	MANAGER_GUI_DISCOUNTS_DELETE_CONFIRM("manager.gui.discounts.confirm_delete"),
	MANAGER_GUI_DISCOUNTS_ALREADY_EDITED("manager.gui.discounts.already_edited"),
	MANAGER_GUI_DISCOUNTS_ENTRY_NAME("manager.gui.discounts.entry.name"),
	MANAGER_GUI_DISCOUNTS_ENTRY_LORE("manager.gui.discounts.entry.lore"),
	MANAGER_GUI_DISCOUNTS_NEW_TITLE("manager.gui.discounts.new.title"),
	MANAGER_GUI_DISCOUNTS_NEW_NAME("manager.gui.discounts.new.name"),
	MANAGER_GUI_DISCOUNTS_NEW_LORE("manager.gui.discounts.new.lore"),

	MANAGER_GUI_DISCOUNT("manager.gui.discount.title"),
	MANAGER_GUI_DISCOUNT_SET_NAME_TITLE("manager.gui.discount.set_name.title"),
	MANAGER_GUI_DISCOUNT_SET_NAME_NAME("manager.gui.discount.set_name.name"),
	MANAGER_GUI_DISCOUNT_SET_NAME_LORE("manager.gui.discount.set_name.lore"),
	MANAGER_GUI_DISCOUNT_SET_PERMISSION_TITLE("manager.gui.discount.set_permission.title"),
	MANAGER_GUI_DISCOUNT_SET_PERMISSION_NAME("manager.gui.discount.set_permission.name"),
	MANAGER_GUI_DISCOUNT_SET_PERMISSION_LORE("manager.gui.discount.set_permission.lore"),
	MANAGER_GUI_DISCOUNT_SET_TAGS_NAME("manager.gui.discount.set_tags.name"),
	MANAGER_GUI_DISCOUNT_SET_TAGS_LORE("manager.gui.discount.set_tags.lore"),
	MANAGER_GUI_DISCOUNT_SET_DURATION_NAME("manager.gui.discount.set_duration.name"),
	MANAGER_GUI_DISCOUNT_SET_DURATION_LORE("manager.gui.discount.set_duration.lore"),
	MANAGER_GUI_DISCOUNT_SET_START_NAME("manager.gui.discount.set_start.name"),
	MANAGER_GUI_DISCOUNT_SET_START_LORE("manager.gui.discount.set_start.lore"),
	MANAGER_GUI_DISCOUNT_SET_PERCENT_NAME("manager.gui.discount.set_percent.name"),
	MANAGER_GUI_DISCOUNT_SET_PERCENT_LORE("manager.gui.discount.set_percent.lore"),

	MANAGER_GUI_SHOP_ENTRY("manager.gui.shop_entry.title"),

	MANAGER_GUI_TEMPLATES("manager.gui.templates.title"),
	MANAGER_GUI_TEMPLATES_CHOOSE("manager.gui.templates.choose_title"),
	MANAGER_GUI_TEMPLATES_APPLY("manager.gui.templates.apply_title"),
	MANAGER_GUI_TEMPLATES_NEW("manager.gui.templates.new_title"),
	MANAGER_GUI_TEMPLATES_ENTRY_NAME("manager.gui.templates.entry.name",
			"Sets the display name of a template entry in the templates menu",
			new Pair<>("template", "<white>Default Shop Layout")),
	MANAGER_GUI_TEMPLATES_ENTRY_LORE("manager.gui.templates.entry.lore",
			"Sets the lore of a template entry in the templates menu",
			new Pair<>("template", "<white>Default Shop Layout"),
			new Pair<>("uuid", UUID.randomUUID().toString()),
			new Pair<>("size", "7")),

	MANAGER_GUI_ENTRY_TITLE("manager.gui.entry.title"),
	MANAGER_GUI_ENTRY_SET_LORE_NAME("manager.gui.entry.set_lore.name"),
	MANAGER_GUI_ENTRY_SET_LORE_LORE("manager.gui.entry.set_lore.lore"),
	MANAGER_GUI_ENTRY_SET_PERMISSION_TITLE("manager.gui.entry.set_permission.title"),
	MANAGER_GUI_ENTRY_SET_PERMISSION_NAME("manager.gui.entry.set_permission.name"),
	MANAGER_GUI_ENTRY_SET_PERMISSION_LORE("manager.gui.entry.set_permission.lore"),
	MANAGER_GUI_ENTRY_SET_TAGS_NAME("manager.gui.entry.set_tags.name"),
	MANAGER_GUI_ENTRY_SET_TAGS_LORE("manager.gui.entry.set_tags.lore"),
	MANAGER_GUI_ENTRY_SET_FUNCTION_TITLE("manager.gui.entry.set_function.title"),
	MANAGER_GUI_ENTRY_SET_FUNCTION_NAME("manager.gui.entry.set_function.name",
			new Pair<>("name", "<white>Static")),
	MANAGER_GUI_ENTRY_SET_FUNCTION_LORE("manager.gui.entry.set_function.lore",
			new Pair<>("function", "<white>Static")),

	MANAGER_GUI_ENTRY_FUNCTION_STATIC_NAME("manager.gui.entry.defaults.static.name"),
	MANAGER_GUI_ENTRY_FUNCTION_STATIC_LORE("manager.gui.entry.defaults.static.lore"),
	MANAGER_GUI_ENTRY_FUNCTION_PREV_PAGE_NAME("manager.gui.entry.defaults.prev_page.name"),
	MANAGER_GUI_ENTRY_FUNCTION_PREV_PAGE_LORE("manager.gui.entry.defaults.prev_page.lore"),
	MANAGER_GUI_ENTRY_FUNCTION_NEXT_PAGE_NAME("manager.gui.entry.defaults.next_page.name"),
	MANAGER_GUI_ENTRY_FUNCTION_NEXT_PAGE_LORE("manager.gui.entry.defaults.next_page.lore"),
	MANAGER_GUI_ENTRY_FUNCTION_EXACT_PAGE_NAME("manager.gui.entry.defaults.exact_page.name"),
	MANAGER_GUI_ENTRY_FUNCTION_EXACT_PAGE_LORE("manager.gui.entry.defaults.exact_page.lore"),
	MANAGER_GUI_ENTRY_FUNCTION_TRADE_NAME("manager.gui.entry.defaults.trade.name"),
	MANAGER_GUI_ENTRY_FUNCTION_TRADE_LORE("manager.gui.entry.defaults.trade.lore"),

	;

	@Getter
	private final String key;
	@Getter
	private final String comment;
	@Getter
	private final Pair<String, String>[] examplePlaceholders;

	Message(String key, Pair<String, String>... examplePlaceholders) {
		this(key, "", examplePlaceholders);
	}

	Message(String key, String comment, Pair<String, String>... examplePlaceholders) {
		this.key = key;
		this.comment = comment;
		this.examplePlaceholders = examplePlaceholders;
	}

	public Component getTranslation(Template... templates) {
		List<Template> t = new ArrayList<>(List.of(templates));
		if (!this.equals(Message.PREFIX)) {
			t.add(Template.of("prefix", Message.PREFIX.getTranslation()));
		}
		String format = TranslationHandler.getInstance().getMessage(key);
		return ShopPlugin.getInstance().getMiniMessage().parse(format, t);
	}

	public List<Component> getTranslations(Template...templates) {
		String[] toFormat = TranslationHandler.getInstance().getMessage(key).split("\n");
		List<Component> result = new ArrayList<>();
		MiniMessage miniMessage = ShopPlugin.getInstance().getMiniMessage();
		for (String string : toFormat) {
			result.add(miniMessage.parse(string, templates));
		}
		return result;
	}

	public String getLegacyTranslation(Template... templates) {
		return ComponentUtils.toLegacy(getTranslation(templates));
	}

	public List<String> getLegacyTranslations(Template... templates) {
		return getTranslations(templates).stream().map(ComponentUtils::toLegacy).collect(Collectors.toList());
	}
}
