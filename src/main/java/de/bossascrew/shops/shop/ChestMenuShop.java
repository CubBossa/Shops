package de.bossascrew.shops.shop;

import de.bossascrew.shops.Customer;
import de.bossascrew.shops.ShopPlugin;
import de.bossascrew.shops.data.Message;
import de.bossascrew.shops.menu.ShopMenuView;
import de.bossascrew.shops.shop.entry.ShopEntry;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
@Setter
public class ChestMenuShop implements Shop {

	private final UUID uuid;
	private String nameFormat;
	private Component name;
	private @Nullable String permission = null;

	private int rows;
	private boolean enabled;

	private boolean isPagingCyclic;
	private boolean isPageRemembered;
	private boolean isModeRemembered;
	private int defaultPage;
	private ShopMode defaultMode;

	private final Map<ShopMode, Map<Integer, ShopEntry>> modeEntryMap;
	private final List<Customer> activeCustomers;
	private final Map<Customer, ShopMenuView> menuMap;
	private final List<String> tags;

	public ChestMenuShop(String nameFormat) {
		this(nameFormat, UUID.randomUUID());
	}

	public ChestMenuShop(String nameFormat, UUID uuid) {
		setNameFormat(nameFormat);
		this.uuid = uuid;

		this.modeEntryMap = new HashMap<>();
		this.activeCustomers = new ArrayList<>();
		this.menuMap = new HashMap<>();
		this.tags = new ArrayList<>();
	}

	public void setNameFormat(String nameFormat) {
		this.nameFormat = nameFormat;
		this.name = ShopPlugin.getInstance().getMiniMessage().parse(nameFormat);
	}

	public @Nullable
	ShopEntry getEntry(ShopMode shopEntry, int slot) {
		return modeEntryMap.getOrDefault(shopEntry, new HashMap<>()).getOrDefault(slot, null);
	}

	@Override
	public void setRememberPage(boolean rememberPage) {
		this.isPageRemembered = rememberPage;
	}

	public int getPreferredOpenPage(Customer customer) {
		return isPageRemembered ? customer.getPage(this, defaultPage) : defaultPage;
	}

	@Override
	public void setRememberMode(boolean rememberMode) {
		this.isModeRemembered = rememberMode;
	}

	@Override
	public ShopMode setDefaultShopMode(ShopMode shopMode) {
		return null;
	}

	@Override
	public ShopMode getDefaultShopMode() {
		return null;
	}

	public @Nullable
	ShopMode getPreferredShopMode(Customer customer) {
		ShopMode mode = isModeRemembered ? customer.getShopMode(this, defaultMode) : defaultMode;
		if (!modeEntryMap.containsKey(mode)) {
			return null;
		}
		return mode;
	}

	public boolean open(Customer customer) {
		return open(customer, getPreferredOpenPage(customer), getPreferredShopMode(customer));
	}

	public boolean open(Customer customer, int page) {
		return open(customer, page, getPreferredShopMode(customer));
	}

	public boolean open(Customer customer, ShopMode mode) {
		return open(customer, getPreferredOpenPage(customer), mode);
	}

	public boolean open(Customer customer, int page, ShopMode mode) {
		if (!enabled) {
			customer.sendMessage(Message.SHOP_NOT_ENABLED);
			return false;
		}
		if (permission != null && !customer.getPlayer().hasPermission(permission)) {
			customer.sendMessage(Message.SHOP_NO_PERMISSION);
			return false;
		}
		ShopMenuView menu = new ShopMenuView(this);
		menu.openShop(customer);
		menuMap.put(customer, menu);
		activeCustomers.add(customer);
		return true;
	}

	public boolean close(Customer customer) {
		ShopMenuView menu = menuMap.get(customer);
		if (menu != null) {
			if (customer.getPlayer().getOpenInventory().equals(menu.getInventoryView())) {
				customer.getPlayer().closeInventory();
			}
			menuMap.remove(customer);
		}
		return activeCustomers.remove(customer);
	}

	public void closeAll() {
		for (Customer customer : activeCustomers) {
			close(customer);
		}
	}

	public ShopInteractionResult interact(Customer customer, ShopMode shopMode, int slot) {
		if (!enabled) {
			return ShopInteractionResult.FAIL_SHOP_DISABLED;
		}
		ShopEntry entry = getEntry(shopMode, slot);
		if (entry == null) {
			return ShopInteractionResult.FAIL_NO_ENTRY;
		}
		if (!entry.hasPermission(customer)) {
			return ShopInteractionResult.FAIL_NO_PERMISSION;
		}
		return entry.buy(customer);
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public List<String> getTags() {
		List<String> list = new ArrayList<>(tags);
		list.add(uuid.toString());
		return list;
	}

	@Override
	public boolean addTag(String tag) {
		return tags.add(tag);
	}

	@Override
	public boolean removeTag(String tag) {
		return tags.remove(tag);
	}

	@Override
	public boolean hasTag(String tag) {
		return getTags().contains(tag);
	}
}
