package de.bossascrew.shops.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import de.bossascrew.shops.menu.ShopManagementMenu;
import org.bukkit.entity.Player;

@CommandAlias("shop")
public class ShopCommand extends BaseCommand {

	@Default
	public void onDefault(Player player) {
		new ShopManagementMenu().openBaseMenu(player);
	}
}
