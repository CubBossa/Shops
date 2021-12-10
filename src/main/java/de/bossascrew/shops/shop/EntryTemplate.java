package de.bossascrew.shops.shop;

import com.google.common.base.Preconditions;
import de.bossascrew.shops.ShopPlugin;
import de.bossascrew.shops.menu.ListMenuElement;
import de.bossascrew.shops.menu.RowedOpenableMenu;
import de.bossascrew.shops.shop.entry.ShopEntry;
import de.bossascrew.shops.util.ComponentUtils;
import de.bossascrew.shops.util.Duplicable;
import de.bossascrew.shops.util.ItemStackUtils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class EntryTemplate implements ListMenuElement, Duplicable<EntryTemplate> {

	@Getter
	private final UUID uuid;

	@Getter
	private String nameFormat;
	@Getter
	private String namePlain;
	@Getter
	private Component name;
	private final HashMap<Function<Integer, Integer>, ShopEntry> map;

	public EntryTemplate(UUID uuid, String nameFormat) {
		super();
		this.uuid = uuid;
		setNameFormat(nameFormat);
		this.map = new LinkedHashMap<>();
	}

	public void setNameFormat(String nameFormat) {
		this.nameFormat = nameFormat;
		this.name = ShopPlugin.getInstance().getMiniMessage().parse(nameFormat);
		this.namePlain = ComponentUtils.toPlain(this.name);
	}

	@Override
	public ItemStack getListDisplayItem() {
		return ItemStackUtils.createTemplatesItemStack(this);
	}

	public ShopEntry put(Integer key, ShopEntry value) {
		Preconditions.checkNotNull(key);
		Preconditions.checkArgument(key >= 0 && key < RowedOpenableMenu.LARGEST_INV_SIZE, "slot \"%d\" needs to be \"0 <= slot < 6 * 9\" ", key);
		return map.put(rows -> key, value);
	}

	public ShopEntry put(Function<Integer, Integer> key, ShopEntry value) {
		return map.put(key, value);
	}

	public Map<Integer, ShopEntry> getEntries(int rows) {
		Map<Integer, ShopEntry> mapped = new HashMap<>();
		for (Map.Entry<Function<Integer, Integer>, ShopEntry> mapEntry : map.entrySet()) {
			int slot = mapEntry.getKey().apply(rows);
			mapEntry.getValue().setSlot(slot);
			mapped.put(slot, mapEntry.getValue());
		}
		return mapped;
	}

	public int size() {
		return map.size();
	}

	@Override
	public EntryTemplate duplicate() {
		EntryTemplate duplicate = new EntryTemplate(UUID.randomUUID(), nameFormat);
		for (Map.Entry<Function<Integer, Integer>, ShopEntry> entry : map.entrySet()) {
			duplicate.put(entry.getKey(), entry.getValue().duplicate());
		}
		return duplicate;
	}
}
