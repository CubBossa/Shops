package de.bossascrew.shops.data;

import de.bossascrew.shops.Customer;
import de.bossascrew.shops.handler.CurrencyHandler;
import de.bossascrew.shops.handler.ShopHandler;
import de.bossascrew.shops.shop.*;
import de.bossascrew.shops.shop.entry.BaseEntry;
import de.bossascrew.shops.shop.entry.ShopEntry;
import de.bossascrew.shops.shop.entry.TradeBaseModule;
import de.bossascrew.shops.util.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestDatabase implements Database, LogDatabase {

	@Override
	public Customer loadCustomer(UUID uuid) {
		return new Customer(Bukkit.getPlayer(uuid), new HashMap<>(), new HashMap<>());
	}

	@Override
	public void saveCustomer(Customer customer) {

	}

	@Override
	public Shop createShop(String nameFormat, UUID uuid) {
		ChestMenuShop shop = new ChestMenuShop(nameFormat, uuid);
		shop.setDefaultShopMode(ShopHandler.getInstance().getShopModes().get(0));
		return shop;
	}

	@Override
	public Map<UUID, Shop> loadShops() {
		Map<UUID, Shop> map = new HashMap<>();
		Shop s1 = createShop("<rainbow>ExampleShop</rainbow>", UUID.randomUUID());
		s1.addTag("swords");
		s1.addTag("rainbow");
		s1.addTag("i am a tag");
		s1.addTag("ululu");
		for (int i = 0; i < 14; i++) {
			s1.addTag("tag" + i);
		}
		Shop s2 = createShop("<white>Boring Shop", UUID.randomUUID());

		Shop s3 = new VillagerShop("<dark_purple>Villager Shop", UUID.randomUUID());
		ShopEntry entry = createEntry(UUID.randomUUID(), s3, new ItemStack(Material.MINECART), ShopHandler.getInstance().getShopModes().get(0), 0);
		entry.setModule(new TradeBaseModule<>(CurrencyHandler.CURRENCY_ITEM, 3, new ItemStack(Material.EMERALD), new ItemStack(Material.MINECART)));
		s3.newEntry(0, entry);

		map.put(s1.getUUID(), s1);
		map.put(s2.getUUID(), s2);
		map.put(s3.getUUID(), s3);
		return map;
	}

	@Override
	public void saveShop(Shop shop) {

	}

	@Override
	public void deleteShop(Shop shop) {

	}

	@Override
	public ShopEntry createEntry(UUID uuid, Shop shop, ItemStack displayItem, ShopMode shopMode, int slot) {
		return new BaseEntry(uuid, shop, displayItem, null, slot, shopMode);
	}

	@Override
	public Map<UUID, Shop> loadEntries(Shop shop) {
		return new HashMap<>();
	}

	@Override
	public void saveEntry(ShopEntry shopEntry) {

	}

	@Override
	public void deleteEntry(ShopEntry shopEntry) {

	}

	@Override
	public Discount createDiscount(String nameFormat, LocalDateTime start, Duration duration, double percent, String... tags) {
		return new Discount(UUID.randomUUID(), nameFormat, start, duration, percent, null, tags);
	}

	@Override
	public Map<UUID, Discount> loadDiscounts() {
		Map<UUID, Discount> map = new HashMap<>();
		Discount d1 = new Discount(UUID.randomUUID(), "<red>XMas Discount", LocalDateTime.now(), Duration.of(3, ChronoUnit.DAYS), 10, null);


		map.put(d1.getUuid(), d1);
		return map;
	}

	@Override
	public void saveDiscount(Discount discount) {

	}

	@Override
	public void deleteDiscount(Discount discount) {

	}

	@Override
	public Limit createLimit(String name) {
		return new Limit(name, Duration.of(3, ChronoUnit.DAYS), customer -> true, 32);
	}

	@Override
	public Map<UUID, Limit> loadLimits() {
		Map<UUID, Limit> map = new HashMap<>();
		Limit limit = createLimit("Example Limit");
		map.put(limit.getUuid(), limit);
		return map;
	}

	@Override
	public void saveLimit(Limit limit) {

	}

	@Override
	public void deleteLimit(Limit limit) {

	}

	@Override
	public EntryTemplate createTemplate(String name) {
		return new EntryTemplate(UUID.randomUUID(), "<aqua>new-template");
	}

	@Override
	public Map<UUID, EntryTemplate> loadTemplates() {
		EntryTemplate template = new EntryTemplate(UUID.randomUUID(), "<gradient:dark_green:green:dark_green>Default Template");
		for (int i = 0; i < 9; i++) {
			int finalI = i;
			template.put(rows -> (rows - 1) * 9 + finalI, new BaseEntry(UUID.randomUUID(), null, ItemStackUtils.createItemStack(Material.DIAMOND, "lol", ""),
					null, i, ShopHandler.getInstance().getShopModes().get(0)));
		}
		return Map.of(template.getUuid(), template);
	}

	@Override
	public void saveTemplate(EntryTemplate template) {

	}

	@Override
	public void deleteTemplate(EntryTemplate template) {

	}

	@Override
	public Map<Location, UUID> loadShopBlockMapping() {
		return null;
	}

	@Override
	public void mapShopToBlock(Shop shop, Location location) {

	}

	@Override
	public void logToDatabase(LogEntry entry) {

	}
}
