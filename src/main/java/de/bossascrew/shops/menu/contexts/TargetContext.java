package de.bossascrew.shops.menu.contexts;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class TargetContext<A, T> extends ActionContext<A> {

	private final T target;

	public TargetContext(Player player, ItemStack itemStack, int slot, A action, T target) {
		this(player, itemStack, slot, true, action, target);
	}

	public TargetContext(Player player, ItemStack itemStack, int slot, boolean cancelled, A action, T target) {
		super(player, itemStack, slot, action, cancelled);
		this.target = target;
	}
}