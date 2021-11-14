package de.bossascrew.shops.shop;

import de.bossascrew.shops.Customer;
import de.bossascrew.shops.shop.entry.ShopEntry;
import de.bossascrew.shops.util.Editable;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface Shop extends Taggable, Comparable<Shop>, Editable<Player> {

	/**
	 * @return The unique id for this shop. It also servers as a tag for the Taggable interface and allows to apply limits and discounts to all shopentries of this shop
	 */
	UUID getUUID();

	/**
	 * @return The name format in minimessage style
	 */
	String getNameFormat();

	/**
	 * Allows to change the name format. The default implementation of this interface likewise sets the name component and the plain text string.
	 *
	 * @param nameFormat The name format in minimessage style.
	 */
	void setNameFormat(String nameFormat);

	/**
	 * @return The name as styled kyori component
	 */
	Component getName();

	/**
	 * @return The bare text name without formats and colors
	 */
	String getNamePlain();

	/**
	 * @return The permission node that allows customers to use this shop or null if no permission is set
	 */
	@Nullable String getPermission();

	/**
	 * @return The amount of pages of this shop. It may be calculated from the highest slot index.
	 */
	int getPageCount();

	ShopEntry getEntry(ShopMode mode, int slot);

	/**
	 * @return true, if customers open the shop at the same page they have closed it
	 */
	boolean isPageRemembered();

	/**
	 * @param rememberPage If set to true, customers open this shop at the page they have closed it
	 */
	void setPageRemembered(boolean rememberPage);

	/**
	 * @return The page to open the shop at for a certain customer
	 */
	int getPreferredOpenPage(Customer customer);

	/**
	 * @return true, if customers open the shop at the same shop mode they have closed it
	 */
	boolean isModeRemembered();

	/**
	 * @param rememberMode If set to true, customers open this shop at the shop mode they have closed it
	 */
	void setModeRemembered(boolean rememberMode);

	ShopMode getDefaultShopMode();

	void setDefaultShopMode(ShopMode shopMode);

	int getDefaultShopPage();

	void setDefaultShopPage(int page);

	ShopMode getPreferredShopMode(Customer customer);

	/**
	 * @return true if the shop is currently enabled. Customers cannot use disabled shops
	 */
	boolean isEnabled();

	/**
	 * If set to false, all active customers will be kicked from the shop. Customers cannot use disabled shops.
	 */
	void setEnabled(boolean enabled);

	/**
	 * @return all customers that currently use this shop and have an open shop interface.
	 */
	List<Customer> getActiveCustomers();

	/**
	 * @param customer the customer to open this shop for.
	 * @return true if the shop was opened successfully, false if errors occured
	 */
	boolean open(Customer customer);

	/**
	 * @param customer the customer to open this shop for.
	 * @param page     the page to open this shop at.
	 * @return true if the shop was opened successfully, false if errors occured
	 */
	boolean open(Customer customer, int page);

	/**
	 * @param customer the customer to open this shop for.
	 * @param shopMode the mode to open this shop at.
	 * @return true if the shop was opened successfully, false if errors occured
	 */
	boolean open(Customer customer, ShopMode shopMode);

	/**
	 * @param customer the customer to open this shop for.
	 * @param page     the page to open this shop at.
	 * @param shopMode the mode to open this shop at.
	 * @return true if the shop was opened successfully, false if errors occured
	 */
	boolean open(Customer customer, int page, ShopMode shopMode);

	/**
	 * @param customer the customer to close this shop if he currently uses it.
	 * @return true, if the shop was successfully closed. false, if the customer was not using this shop.
	 */
	boolean close(Customer customer);

	/**
	 * closes the shop for all active customers, for example when setting enabled to false.
	 */
	void closeAll();

	//TODO nah das muss noch anders
	ShopInteractionResult interact(Customer customer, ShopMode shopMode, int slot);
}
