package de.bossascrew.shops.statshops.handler;

import de.bossascrew.shops.general.Taggable;
import de.bossascrew.shops.general.entry.ShopEntry;
import de.bossascrew.shops.general.menu.ListManagementMenuElementHolder;
import de.bossascrew.shops.general.menu.ShopMenu;
import de.bossascrew.shops.general.util.ItemStackUtils;
import de.bossascrew.shops.general.util.Pair;
import de.bossascrew.shops.statshops.StatShops;
import de.bossascrew.shops.statshops.shop.Limit;
import de.bossascrew.shops.web.WebAccessable;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class LimitsHandler implements
		WebAccessable<Limit>,
		ListManagementMenuElementHolder<Limit> {

	@Getter
	private static LimitsHandler instance;

	private final Map<UUID, Limit> limitMap;
	private final Map<String, Collection<Limit>> tagMap;

	private final Map<Limit, List<Pair<ShopMenu, ShopEntry>>> subscribers;

	private final TreeMap<Long, Collection<EntryInteraction>> recoveries;

	public LimitsHandler() {
		instance = this;

		subscribers = new HashMap<>();
		tagMap = new HashMap<>();
		limitMap = StatShops.getInstance().getDatabase().loadLimits();
		recoveries = loadExpiringLimits();
	}

	public TreeMap<Long, Collection<EntryInteraction>> loadExpiringLimits() {
		return new TreeMap<>(Collections.reverseOrder());
		//TODO load all transactions as EntryInteraction for longest limit duration. In case a longer limit duration occures afterwards cache live
	}

	public List<Limit> getLimits() {
		return new ArrayList<>(limitMap.values());
	}

	public boolean handleLimitInteraction(ShopEntry entry, Player player, ShopMenu shopMenu) {

		Pair<Limit, Limit> limits = getMinimalLimitsWithMatchingTags(player, entry, entry.getShop());
		Limit a = limits.getLeft();
		Limit b = limits.getRight();

		long duration;
		int limit;
		if (a != null && b != null) {
			duration = Long.max(a.getRecover().toMillis(), b.getRecover().toMillis());
			limit = Integer.min(a.getTransactionLimit(), b.getTransactionLimit());
		} else if (a != null) {
			duration = a.getRecover().toMillis();
			limit = a.getTransactionLimit();
		} else if (b != null) {
			duration = b.getRecover().toMillis();
			limit = b.getTransactionLimit();
		} else {
			//People that dont have a limit also increase the global limit
			//TODO config option?
			/*insertRecovery(player.getUniqueId(), entry, shopMenu, null);
			updateSubscribersGlobalLimit(entry);
			*/
			return true;
		}
		int count = getLimitUserCount(player.getUniqueId(), entry.getUUID(), System.currentTimeMillis() - duration, b != null);

		if (count >= limit) {
			return false;
		}
		insertRecovery(player.getUniqueId(), entry, shopMenu, duration);
		updateSubscribersGlobalLimit(entry);
		shopMenu.updateEntry(entry);
		return true;
	}

	private void insertRecovery(UUID playerUuid, ShopEntry entry, ShopMenu menu, @Nullable Long recoveryDuration) {
		long now = System.currentTimeMillis();
		Collection<EntryInteraction> innerRecoveries = recoveries.getOrDefault(now, new HashSet<>());
		innerRecoveries.add(new EntryInteraction(entry.getUUID(), playerUuid, now, recoveryDuration == null ? 0 : recoveryDuration));
		recoveries.put(now, innerRecoveries);

		if (recoveryDuration != null) {
			menu.handleLimitRecoverInit(entry, recoveryDuration);
		}
	}

	public Collection<EntryInteraction> getExpiringInteractions(UUID entryUuid) {
		long now = System.currentTimeMillis();
		Collection<EntryInteraction> result = new HashSet<>();
		recoveries.forEach((expire, entryInteractions) -> result.addAll(entryInteractions.stream()
				.filter(entryInteraction -> entryInteraction.timeStamp + entryInteraction.duration > now)
				.filter(entryInteraction -> entryInteraction.entryUuid.equals(entryUuid))
				.collect(Collectors.toList())));
		return result;
	}

	public int getLimitUserCount(UUID playerId, UUID entryId, long since, boolean global) {
		int count = 0;
		for (Map.Entry<Long, Collection<EntryInteraction>> entry : recoveries.entrySet()) {
			if (entry.getKey() < since) {
				break;
			}
			for (EntryInteraction i : entry.getValue()) {
				if (!i.entryUuid.equals(entryId)) {
					continue;
				}
				if (global || i.playerUuid.equals(playerId)) {
					count++;
				}
			}
		}
		return count;
	}

	public void handleLimitTagAdded(Limit limit, String tag) {
		Collection<Limit> limits = tagMap.getOrDefault(tag, new ArrayList<>());
		if (!limits.contains(limit)) {
			limits.add(limit);
			tagMap.put(tag, limits);
		}
		updateAllSubscribers(limit);
	}

	public void handleLimitTagRemoved(Limit limit, String tag) {
		System.out.println("before: " + tagMap.get(tag).size());
		System.out.println(tagMap.get(tag).stream().map(Limit::getUuid).map(UUID::toString).collect(Collectors.joining(", ")));
		tagMap.get(tag).remove(limit);
		System.out.println("after: " + tagMap.get(tag).size());
		System.out.println(tagMap.get(tag).stream().map(Limit::getUuid).map(UUID::toString).collect(Collectors.joining(", ")));
		updateAllSubscribers(limit);
	}

	public void subscribeToDisplayUpdates(ShopMenu menu, Player player, ShopEntry shopEntry) {
		List<Limit> limits = getLimitsWithMatchingTags(player, shopEntry, shopEntry.getShop());
		for (Limit limit : limits) {
			List<Pair<ShopMenu, ShopEntry>> innerSubscribers = subscribers.getOrDefault(limit, new ArrayList<>());
			innerSubscribers.add(new Pair<>(menu, shopEntry));
			subscribers.put(limit, innerSubscribers);
		}
	}

	public void unsubscribeToDisplayUpdates(ShopMenu menu) {
		subscribers.replaceAll((k, v) -> v.stream().filter(p -> !p.getLeft().equals(menu)).collect(Collectors.toList()));
	}

	/**
	 * Act when the global sum changed for one entry
	 */
	public void updateSubscribersGlobalLimit(ShopEntry shopEntry) {
		getLimitsWithMatchingTags(null, shopEntry, shopEntry.getShop()).stream()
				.filter(Limit::isGlobal)
				.map(limit -> subscribers.getOrDefault(limit, new ArrayList<>()))
				.forEach(pairs -> pairs.stream()
						.filter(pair -> pair.getRight().getUUID().equals(shopEntry.getUUID()))
						.forEach(pair -> pair.getLeft().updateEntry(pair.getRight())));
	}

	/**
	 * Act when a limit is removed / modified
	 */
	public void updateAllSubscribers(Limit limit) {
		for (Pair<ShopMenu, ShopEntry> pair : subscribers.getOrDefault(limit, new ArrayList<>())) {
			pair.getLeft().updateEntry(pair.getRight());
		}
	}

	public void addLimitsLore(ShopEntry shopEntry, List<Component> existingLore, Player player) {
		Pair<Limit, Limit> pair = getMinimalLimitsWithMatchingTags(player, shopEntry, shopEntry.getShop());
		long duration;
		if (pair.getLeft() != null && pair.getRight() != null) {
			duration = Long.max(pair.getLeft().getRecover().toMillis(), pair.getRight().getRecover().toMillis());
		} else if (pair.getLeft() != null) {
			duration = pair.getLeft().getRecover().toMillis();
		} else if (pair.getRight() != null) {
			duration = pair.getRight().getRecover().toMillis();
		} else {
			return;
		}
		ItemStackUtils.addLoreLimits(existingLore, pair.getLeft(), pair.getRight(),
				getLimitUserCount(player.getUniqueId(), shopEntry.getUUID(), System.currentTimeMillis() - duration, pair.getRight() != null));
	}

	public Pair<Limit, Limit> getMinimalLimitsWithMatchingTags(@Nullable Player player, Taggable... taggables) {
		List<Limit> limits = getLimitsWithMatchingTags(player, taggables);
		Limit smallLocal = null;
		Limit smallGlobal = null;
		for (Limit limit : limits) {
			if (limit.isGlobal()) {
				if (smallGlobal == null || limit.getTransactionLimit() < smallGlobal.getTransactionLimit()) {
					smallGlobal = limit;
				}
			} else {
				if (smallLocal == null || limit.getTransactionLimit() < smallLocal.getTransactionLimit()) {
					smallLocal = limit;
				}
			}
		}
		return new Pair<>(smallLocal, smallGlobal);
	}

	public List<Limit> getLimitsWithMatchingTags(@Nullable Player player, Taggable... taggables) {
		List<Limit> limits = new ArrayList<>();
		for (Taggable taggable : taggables) {
			for (String tag : taggable.getTags()) {
				if (tagMap.containsKey(tag)) {
					limits.addAll(tagMap.get(tag).stream().filter(limit -> player == null || limit.getPermission() == null || player.hasPermission(limit.getPermission())).collect(Collectors.toList()));
				}
			}
		}
		return limits;
	}

	@Override
	public List<Limit> getWebData() {
		return getLimits();
	}

	@Override
	public void storeWebData(List<Limit> values) {
		//TODO
	}

	@Override
	public List<Limit> getValues() {
		return getLimits();
	}

	@Override
	public Limit createNew(String input) {
		Limit limit = StatShops.getInstance().getDatabase().createLimit(input);
		limitMap.put(limit.getUuid(), limit);
		return limit;
	}

	@Override
	public Limit createDuplicate(Limit element) {
		Limit limit = createNew(element.getTransactionLimit() + "");
		limit.setRecover(element.getRecover());
		limit.setAppliesToCustomer(element.getAppliesToCustomer());
		StatShops.getInstance().getDatabase().saveLimit(limit);
		return limit;
	}

	@Override
	public boolean delete(Limit limit) {
		// Automatically updates all guis. Doesn't matter because limit is deleted anyways
		new ArrayList<>(limit.getTags()).forEach(limit::removeTag);

		StatShops.getInstance().getDatabase().deleteLimit(limit);
		subscribers.remove(limit);
		limitMap.remove(limit.getUuid());
		return true;
	}

	public record EntryInteraction(UUID entryUuid, UUID playerUuid, long timeStamp, long duration) {

	}
}
