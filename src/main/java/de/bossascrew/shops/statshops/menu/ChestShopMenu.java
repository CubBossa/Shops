package de.bossascrew.shops.statshops.menu;

import com.google.common.base.Preconditions;
import de.bossascrew.shops.general.Customer;
import de.bossascrew.shops.general.entry.ShopEntry;
import de.bossascrew.shops.general.entry.TradeModule;
import de.bossascrew.shops.general.menu.ChestMenu;
import de.bossascrew.shops.general.menu.DefaultSpecialItem;
import de.bossascrew.shops.general.menu.ShopMenu;
import de.bossascrew.shops.general.menu.contexts.BackContext;
import de.bossascrew.shops.general.menu.contexts.ContextConsumer;
import de.bossascrew.shops.general.util.EntryInteractionType;
import de.bossascrew.shops.general.util.ItemStackUtils;
import de.bossascrew.shops.general.util.LoggingPolicy;
import de.bossascrew.shops.statshops.StatShops;
import de.bossascrew.shops.statshops.data.Message;
import de.bossascrew.shops.statshops.handler.DiscountHandler;
import de.bossascrew.shops.statshops.handler.InventoryHandler;
import de.bossascrew.shops.statshops.handler.LimitsHandler;
import de.bossascrew.shops.statshops.shop.ChestMenuShop;
import de.bossascrew.shops.statshops.shop.EntryInteractionResult;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ChestShopMenu extends ChestMenu implements ShopMenu {

	private final ChestMenuShop shop;
	private @Nullable ContextConsumer<BackContext> customBackHandler;

	/**
	 * Limits will set for targetCustomer
	 */
	private final Customer targetCustomer;
	private final int cooldown;
	private final boolean showCooldownMessage;
	private final HashMap<Player, Long> interactionCooldown;

	private final List<BukkitTask> schedulers;

	public ChestShopMenu(ChestMenuShop shop, Customer customer) {
		this(shop, customer, null);
	}

	public ChestShopMenu(ChestMenuShop shop, Customer customer, @Nullable ContextConsumer<BackContext> backHandler) {
		super(shop.getName(), shop.getRows(), 0, null, null);
		this.shop = shop;
		this.customBackHandler = backHandler;
		this.interactionCooldown = new HashMap<>();
		this.cooldown = StatShops.getInstance().getShopsConfig().getCooldown();
		this.showCooldownMessage = StatShops.getInstance().getShopsConfig().isShowCooldownMessage();
		this.targetCustomer = customer;
		this.schedulers = new ArrayList<>();

		setCloseHandler(closeContext -> {
			unsubscribeToDisplayUpdates();
			Customer.wrap(closeContext.getPlayer()).setActiveShop(null);
		});
	}

	private void setupLimitRecoveryScheduler(ShopEntry shopEntry, int durationInTicks) {
		BukkitTask task = Bukkit.getScheduler().runTaskLater(StatShops.getInstance(), () -> {
			updateEntry(shopEntry);
			cleanupRecoveryScheduler();
		}, durationInTicks + 2);
		schedulers.add(task);
	}

	private void cleanupRecoveryScheduler() {
		List<BukkitTask> remove = schedulers.stream().filter(BukkitTask::isCancelled).collect(Collectors.toList());
		schedulers.removeAll(remove);
	}

	private void cancelLimitRecoveryTasks() {
		schedulers.forEach(BukkitTask::cancel);
		schedulers.clear();
	}

	public void handleLimitRecoverInit(ShopEntry shopEntry, long recoverDuration) {
		setupLimitRecoveryScheduler(shopEntry, (int) (recoverDuration / 50));
	}

	@Override
	public InventoryView openInventory(Player player) {
		return openInventory(player, null);
	}

	public InventoryView openInventory(Player player, Consumer<Inventory> inventoryPreparer) {
		return openInventory(Customer.wrap(player));
	}

	public InventoryView openInventory(Customer customer) {
		return openInventory(customer, null);
	}

	public InventoryView openInventory(Customer customer, Consumer<Inventory> inventoryPreparer) {

		Preconditions.checkNotNull(customer, "customer");

		return StatShops.getInstance().callTaskSync(() -> openInventorySync(customer.getPlayer(), null, shop.getPreferredOpenPage(customer)));
	}

	@Override
	public void setBackSlot(int backSlot) {
		//backhandler not allowed for ShopMenu
	}

	@Override
	public void setBackHandlerAction(@NotNull ContextConsumer<BackContext> backHandler) {
		//backhandler not allowed for ShopMenu
	}

	@Override
	public InventoryView openInventorySync(@NotNull Player player, @Nullable Consumer<Inventory> inventoryPreparer) {
		return openInventorySync(player, inventoryPreparer, shop.getDefaultShopPage());
	}

	public InventoryView openInventorySync(@NotNull Player player, @Nullable Consumer<Inventory> inventoryPreparer, int page) {
		Inventory inventory = Bukkit.createInventory(null, slots.length, Message.SHOP_GUI_TITLE.getLegacyTranslation(
				Template.of("name", shop.getName()),
				Template.of("page", "" + (page + 1)),
				Template.of("pages", "" + shop.getPageCount())));
		return openInventorySync(player, inventory, inventoryPreparer, page);
	}

	@Override
	public InventoryView openInventorySync(@NotNull Player player, Inventory inventory, Consumer<Inventory> inventoryPreparer) {
		return openInventorySync(player, inventory, inventoryPreparer, shop.getDefaultShopPage());
	}

	public InventoryView openInventorySync(@NotNull Player player, Inventory inventory, Consumer<Inventory> inventoryPreparer, int page) {
		this.inventory = inventory;

		if (inventory == null) {
			throw new NullPointerException("Inventar für OpenableMenu nicht gesetzt. Nutze openInventorySync(Player, Inventory, Consumer<Inventory>) in Child-Klasse.");
		}

		if (player.isSleeping()) {
			player.wakeup(true);
		}

		if (inventoryPreparer != null) {
			try {
				inventoryPreparer.accept(inventory);
			} catch (Exception exc) {
				StatShops.getInstance().log(LoggingPolicy.ERROR, "Fehler bei openInventorySync() von Spieler " + player.getName(), exc);
			}
		}

		fillMenu(DefaultSpecialItem.EMPTY_LIGHT_RP);

		List<ShopEntry> entries = shop.getEntries(page);
		for (ShopEntry entry : entries) {
			updateEntry(entry);

			//Subscribe to limits and discounts so changes can be displayed live
			DiscountHandler.getInstance().subscribeToDisplayUpdates(this, entry);
			LimitsHandler.getInstance().subscribeToDisplayUpdates(this, player, entry);
		}

		InventoryView view = player.openInventory(inventory);
		if (view == null) {
			return null;
		}

		Customer customer = Customer.wrap(player);
		customer.setPage(shop, page);

		UUID playerId = player.getUniqueId();
		InventoryHandler.getInstance().handleMenuOpen(player, this);
		openInventories.put(playerId, inventory);

		for (ShopEntry entry : entries) {
			for (LimitsHandler.EntryInteraction interaction : LimitsHandler.getInstance().getExpiringInteractions(entry.getUUID())) {
				System.out.println("duration: " + interaction.duration());
				handleLimitRecoverInit(entry, interaction.duration());
			}
		}
		return view;
	}

	@Override
	public boolean closeInventory(Player player) {
		unsubscribeToDisplayUpdates();
		cancelLimitRecoveryTasks();
		return super.closeInventory(player);
	}

	public void unsubscribeToDisplayUpdates() {
		DiscountHandler.getInstance().unsubscribeToDisplayUpdates(this);
		LimitsHandler.getInstance().unsubscribeToDisplayUpdates(this);
	}

	public void setEntry(ShopEntry entry) {

		int slot = entry.getSlot() % LARGEST_INV_SIZE;
		setItemAndClickHandler(slot, ItemStackUtils.createEntryItemStack(entry, targetCustomer), clickContext -> {
			Player player = clickContext.getPlayer();
			Customer c = Customer.wrap(player);

			long now = System.currentTimeMillis();
			Long last = interactionCooldown.get(player);
			if (last != null) {
				long dif = now - last;
				if (dif < cooldown) {
					if (showCooldownMessage) {
						c.sendMessage(Message.SHOP_COOLDOWN);
					}
					return;
				}
			}
			EntryInteractionResult result = entry.interact(targetCustomer, this, EntryInteractionType.fromClickType(clickContext.getAction()));
			if (result == EntryInteractionResult.SUCCESS && entry.getModule() != null && entry.getModule() instanceof TradeModule tm) {
				shop.getBalanceMessenger().handleTransaction(tm.getLastTransaction(targetCustomer));
			}
			interactionCooldown.put(player, now);
		});
	}

	public void updateEntry(ShopEntry entry) {
		setEntry(entry);
		refresh(entry.getSlot() % LARGEST_INV_SIZE);
	}
}
