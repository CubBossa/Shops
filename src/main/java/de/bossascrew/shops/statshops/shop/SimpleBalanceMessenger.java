package de.bossascrew.shops.statshops.shop;

import de.bossascrew.shops.general.Customer;
import de.bossascrew.shops.general.TransactionBalanceMessenger;
import de.bossascrew.shops.general.entry.TradeModule;
import de.bossascrew.shops.general.util.TradeMessageType;
import de.bossascrew.shops.statshops.StatShops;
import de.bossascrew.shops.statshops.data.Message;
import de.bossascrew.shops.statshops.shop.currency.Price;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class SimpleBalanceMessenger implements TransactionBalanceMessenger {

	@Getter
	@Setter
	private TradeMessageType tradeMessageType;
	private final Map<UUID, List<Price<?>>> tradeCache;

	public SimpleBalanceMessenger(TradeMessageType tradeMessageType) {
		this.tradeMessageType = tradeMessageType;
		this.tradeCache = new HashMap<>();
	}


	@Override
	public void handleTransaction(Transaction transaction) {
		if (tradeMessageType.equals(TradeMessageType.NONE)) {
			return;
		}

		Customer customer = transaction.getCustomer();
		TradeModule<?, ?> tm = (TradeModule<?, ?>) transaction.getShopEntry().getModule();
		Price<?> gain = tm.getGainPrice().duplicate();
		Price<?> pay = tm.getPayPrice().duplicate();
		gain.setAmount(gain.getAmount() * (transaction.getInteractionType().isBuy() ? 1 : -1));
		pay.setAmount(pay.getAmount() * (transaction.getInteractionType().isBuy() ? -1 : 1));

		List<Price<?>> prices = tradeCache.getOrDefault(customer.getUuid(), new ArrayList<>());

		Price<?> cachedGain = prices.stream().filter(price -> price.equals(gain)).findAny().orElse(null);
		if (cachedGain == null) {
			prices.add(gain);
		} else {
			cachedGain.setAmount(cachedGain.getAmount() + gain.getAmount());
		}
		Price<?> cachedPay = prices.stream().filter(price -> price.equals(pay)).findAny().orElse(null);
		if (cachedPay == null) {
			prices.add(pay);
		} else {
			cachedPay.setAmount(cachedPay.getAmount() + pay.getAmount());
		}

		TradeMessageType feedback = StatShops.getInstance().getShopsConfig().getTradeMessageFeedback();
		if (feedback.equals(TradeMessageType.PROMPT)) {
			printCachedBalanceAndClear(customer, false);
		}
		tradeCache.put(customer.getUuid(), prices);
	}

	@Override
	public void handlePageClose(Player player) {
		if (tradeMessageType.equals(TradeMessageType.CUMULATIVE_PAGE)) {
			printCachedBalanceAndClear(Customer.wrap(player));
		}
	}

	@Override
	public void handleShopClose(Player player) {
		if (tradeMessageType.equals(TradeMessageType.CUMULATIVE_SHOP)) {
			printCachedBalanceAndClear(Customer.wrap(player));
		}
	}

	private void printCachedBalanceAndClear(Customer customer) {
		List<Price<?>> cache = tradeCache.get(customer.getUuid());
		printCachedBalanceAndClear(customer, cache != null && cache.size() > 0 && cache.stream()
				.anyMatch(price -> price.getAmount() != 0));
	}

	private void printCachedBalanceAndClear(Customer customer, boolean header) {
		if (header) {
			customer.sendMessage(Message.SHOP_TRADE_FEEDBACK_CUMUL_TITLE, 0);
		}
		List<Price<?>> cache = tradeCache.get(customer.getUuid());
		if (cache == null) {
			return;
		}
		cache = cache.stream().sorted().collect(Collectors.toList());
		for (Price<?> price : cache) {
			customer.sendMessage("", getTransactionFeedback(price.getAmount(), price.getObjectComponent(), price.getCurrency().isCastToInt()), 0);
		}
		tradeCache.put(customer.getUuid(), new ArrayList<>());
	}

	private Component getTransactionFeedback(double amount, Component tradeObjectComponent, boolean toInt) { //TODO toint aus currency holen
		Template[] templates = {
				Template.of("indicator", amount >= 0 ? Message.SHOP_TRADE_FEEDBACK_GAIN.getTranslation() : Message.SHOP_TRADE_FEEDBACK_PAY.getTranslation()),
				Template.of("amount", (toInt ? "" + ((int) Math.abs(amount)) : "" + Math.abs(amount))),
				Template.of("subject", tradeObjectComponent)
		};
		if (tradeMessageType.equals(TradeMessageType.PROMPT)) {
			return Message.SHOP_TRADE_FEEDBACK_PROMPT_FORMAT.getTranslation(templates);
		} else {
			return Message.SHOP_TRADE_FEEDBACK_CUMUL_FORMAT.getTranslation(templates);
		}
	}
}
