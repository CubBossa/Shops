package de.bossascrew.shops.menu;

import de.bossascrew.shops.Customer;
import de.bossascrew.shops.ShopPlugin;
import de.bossascrew.shops.handler.DiscountHandler;
import de.bossascrew.shops.shop.ShopInteractionResult;
import de.bossascrew.shops.shop.VillagerShop;
import de.bossascrew.shops.shop.entry.ShopEntry;
import de.bossascrew.shops.shop.entry.TradeModule;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class VillagerShopMenu extends VillagerMenu implements ShopMenu {

	private final static ClickType[] IGNORED = {ClickType.UNKNOWN, ClickType.WINDOW_BORDER_LEFT, ClickType.WINDOW_BORDER_RIGHT};

	private final VillagerShop villagerShop;
	private final Map<ShopEntry, MerchantRecipe> recipeMap;
	private final Map<Integer, ShopEntry> entryMap;

	public VillagerShopMenu(VillagerShop villagerShop) {
		super(villagerShop.getName(), null);
		this.villagerShop = villagerShop;
		this.recipeMap = new HashMap<>();
		this.entryMap = new HashMap<>();

		setTradeHandler(targetContext -> {
			if (Arrays.stream(IGNORED).anyMatch(clickType -> targetContext.getAction().equals(clickType))) {
				return;
			}
			ShopEntry entry = entryMap.get(targetContext.getTarget());
			if(entry.getModule() != null) {
				ShopPlugin.getInstance().getLogDatabase().logToDatabase(entry.getModule().createLogEntry(Customer.wrap(targetContext.getPlayer()), ShopInteractionResult.SUCCESS));
			}
		});
	}

	private void prepareInventory() {
		int i = 0;
		for (Map.Entry<Integer, ShopEntry> entry : villagerShop.getSlotEntryMap().entrySet()) {

			ShopEntry e = entry.getValue();
			//Only works with Currency<ItemStack> for now
			if (e.getModule() != null && e.getModule() instanceof TradeModule tm) {

				ItemStack price = (ItemStack) tm.getPriceObject();
				price.setAmount(Integer.min((int) tm.getPriceAmount(), 64));

				MerchantRecipe recipe = new MerchantRecipe(tm.getArticle(), Integer.MAX_VALUE);
				recipeMap.put(e, recipe);
				entryMap.put(i++, e);
				recipe.addIngredient(price);
				updateEntry(e);

				setMerchantOffer(entry.getKey(), recipe);
			}
		}
	}

	@Override
	public InventoryView openInventorySync(@NotNull Player player, @Nullable Consumer<Inventory> inventoryPreparer) {
		prepareInventory();
		return super.openInventorySync(player, inventoryPreparer);
	}

	@Override
	public boolean closeInventory(Player player) {
		DiscountHandler.getInstance().unsubscribeToDisplayUpdates(this);
		return super.closeInventory(player);
	}

	@Override
	public void updateEntry(ShopEntry entry) {
		MerchantRecipe recipe = recipeMap.get(entry);
		if (recipe != null) {
			//Limits
			recipe.setMaxUses(Integer.MAX_VALUE); //TODO limits

			//Discounts
			double discount = DiscountHandler.getInstance().combineDiscounts(entry, entry.getShop());
			recipe.setPriceMultiplier((float) discount);
		}
	}
}