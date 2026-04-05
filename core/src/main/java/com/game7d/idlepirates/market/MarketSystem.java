package com.game7d.idlepirates.market;

import com.game7d.idlepirates.data.ResourceType;
import com.game7d.idlepirates.data.CraftedItem;
import com.game7d.idlepirates.progress.ResourceDiscovery;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class MarketSystem {

    // ===== STORAGE =====
    private final EnumMap<ResourceType, Integer> resourceBank;
    private final EnumMap<CraftedItem, Integer> craftedInventory;

    // ===== DISCOVERY =====
    private final ResourceDiscovery resourceDiscovery;
    // CraftedDiscovery אפשרי בעתיד

    // ===== GOLD =====
    private int gold = 0;

    public MarketSystem(
        EnumMap<ResourceType, Integer> resourceBank,
        EnumMap<CraftedItem, Integer> craftedInventory,
        ResourceDiscovery resourceDiscovery
    ) {
        this.resourceBank = resourceBank;
        this.craftedInventory = craftedInventory;
        this.resourceDiscovery = resourceDiscovery;
    }

    // =========================
    // GOLD
    // =========================
    public int getGold() {
        return gold;
    }

    // =========================
    // VISIBILITY (Market UI)
    // =========================

    /** משאבים גולמיים שניתן להציג בשוק */
    public List<ResourceType> getVisibleRawResources() {
        return new ArrayList<>(resourceDiscovery.getDiscovered());
    }

    /** פריטים מעובדים עם כמות > 0 */
    public List<CraftedItem> getVisibleCraftedItems() {
        List<CraftedItem> result = new ArrayList<>();
        for (CraftedItem item : craftedInventory.keySet()) {
            if (craftedInventory.get(item) > 0) {
                result.add(item);
            }
        }
        return result;
    }

    // =========================
    // SELL RAW RESOURCES
    // =========================

    public boolean sellRaw(ResourceType type, int amount) {

        if (!resourceDiscovery.isDiscovered(type)) return false;

        int available = resourceBank.getOrDefault(type, 0);
        if (available < amount || amount <= 0) return false;

        int price = MarketPrices.getRawPrice(type);

        resourceBank.put(type, available - amount);
        gold += amount * price;

        return true;
    }

    public boolean sellAllRaw(ResourceType type) {

        if (!resourceDiscovery.isDiscovered(type)) return false;

        int amount = resourceBank.getOrDefault(type, 0);
        if (amount <= 0) return false;

        int price = MarketPrices.getRawPrice(type);

        resourceBank.put(type, 0);
        gold += amount * price;

        return true;
    }

    // =========================
    // SELL CRAFTED ITEMS
    // =========================

    public boolean sellCrafted(CraftedItem item, int amount) {

        int available = craftedInventory.getOrDefault(item, 0);
        if (available < amount || amount <= 0) return false;

        int price = MarketPrices.getCraftedPrice(item);

        craftedInventory.put(item, available - amount);
        gold += amount * price;

        return true;
    }

    public boolean sellAllCrafted(CraftedItem item) {

        int amount = craftedInventory.getOrDefault(item, 0);
        if (amount <= 0) return false;

        int price = MarketPrices.getCraftedPrice(item);

        craftedInventory.put(item, 0);
        gold += amount * price;

        return true;
    }
}


