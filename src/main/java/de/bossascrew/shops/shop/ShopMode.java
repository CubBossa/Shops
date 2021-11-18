package de.bossascrew.shops.shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

/**
 * Buy, Sell, Trade, etc...
 */
@Getter
@Setter
public abstract class ShopMode {

	@JsonIgnore
	private ShopMode next = null;
	@JsonIgnore
	private ShopMode previous = null;

	public abstract String getKey();

	@JsonIgnore
	public abstract Component getDisplayName();

	@JsonIgnore
	public abstract ItemStack getDisplayItem();
}
