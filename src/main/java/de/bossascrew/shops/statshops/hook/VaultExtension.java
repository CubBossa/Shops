package de.bossascrew.shops.statshops.hook;

import de.bossascrew.shops.general.Customer;
import de.bossascrew.shops.general.handler.CurrencyHandler;
import de.bossascrew.shops.general.handler.SubModulesHandler;
import de.bossascrew.shops.general.util.ItemStackUtils;
import de.bossascrew.shops.statshops.StatShops;
import de.bossascrew.shops.statshops.StatShopsExtension;
import de.bossascrew.shops.statshops.data.Message;
import de.bossascrew.shops.statshops.handler.TranslationHandler;
import de.bossascrew.shops.statshops.shop.currency.Currency;
import de.bossascrew.shops.statshops.shop.currency.Price;
import de.bossascrew.shops.statshops.shop.entry.CostsSubModule;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.plugin.RegisteredServiceProvider;

@RequiredArgsConstructor
public class VaultExtension extends StatShopsExtension {

	private final StatShops shopPlugin;
	private static Economy economy = null;

	public static Currency<Void> CURRENCY_VAULT;
	public static SubModulesHandler.CostsSubModuleProvider<Void> COSTS_VAULT_PROVIDER;

	@Override
	public void registerCurrencies(CurrencyHandler currencyHandler) {
		super.registerCurrencies(currencyHandler);

		CURRENCY_VAULT = new Currency<>(
				"money", StatShops.getInstance().getShopsConfig().getCurrencyVaultFormattingDiscounted(), (integer, unused) -> Component.text(integer == 1 ? economy.currencyNameSingular() : economy.currencyNamePlural()), StatShops.getInstance().getShopsConfig().getCurrencyVaultFormatting()
		) {
			@Override
			public double applyDiscount(double amount, double discount) {
				return amount * discount;
			}

			@Override
			public double getAmount(Customer customer, Void object) {
				return economy.getBalance(customer.getPlayer());
			}

			@Override
			public boolean addAmount(Customer customer, double amount, Void object) {
				return economy.depositPlayer(customer.getPlayer(), amount).transactionSuccess();
			}

			@Override
			public boolean removeAmount(Customer customer, double amount, Void object) {
				return economy.withdrawPlayer(customer.getPlayer(), amount).transactionSuccess();
			}
		};
		currencyHandler.registerCurrency(CURRENCY_VAULT);
	}

	@Override
	public void registerMessages(TranslationHandler translationHandler) {
		super.registerMessages(translationHandler);

		//TODO vault spezifische messages hier registrieren
	}

	@Override
	public void registerTradeSubModules(SubModulesHandler subModulesHandler) {
		super.registerTradeSubModules(subModulesHandler);

		COSTS_VAULT_PROVIDER = subModulesHandler.registerCostsSubModule("vault", StatShops.PERM_COSTS_VAULT, ItemStackUtils.createItemStack(Material.GOLD_INGOT, 7122000),
				Message.GUI_ENTRY_FUNCTION_COSTS_VAULT_NAME, Message.GUI_ENTRY_FUNCTION_COSTS_VAULT_LORE, (provider, shopEntry) -> new MoneyCosts(provider));
	}

	public boolean setupEconomy() {
		if (shopPlugin.getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = shopPlugin.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		economy = rsp.getProvider();
		return economy != null;
	}

	public static class MoneyCosts extends CostsSubModule<Void> {

		public MoneyCosts(SubModulesHandler.CostsSubModuleProvider<Void> provider) {
			super(provider, new Price<>(CURRENCY_VAULT, 100, null),
					new Price<>(CURRENCY_VAULT, 50, null));
		}
	}
}