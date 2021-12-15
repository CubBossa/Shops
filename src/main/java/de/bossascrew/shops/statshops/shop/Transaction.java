package de.bossascrew.shops.statshops.shop;

import de.bossascrew.shops.general.Customer;
import de.bossascrew.shops.general.entry.ShopEntry;
import de.bossascrew.shops.general.util.EntryInteractionType;
import de.bossascrew.shops.statshops.shop.currency.Currency;
import de.bossascrew.shops.statshops.shop.currency.Price;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Transaction {

	private final Customer customer;
	private final ShopEntry shopEntry;
	private final EntryInteractionType interactionType;
	private final Price<?> payPrice;
	private final Price<?> gainPrice;
	private final LocalDateTime localDateTime;
	private final double discount;
	private final List<Discount> accountedDiscounts;
}
