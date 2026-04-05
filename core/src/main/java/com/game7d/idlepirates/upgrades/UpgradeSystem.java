package com.game7d.idlepirates.upgrades;

import com.game7d.idlepirates.data.ResourceType;
import com.game7d.idlepirates.data.CraftedItem;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * אחראי על רכישת שדרוגים.
 *
 * תפקידים:
 * - בדיקת דרישות (Raw + Crafted)
 * - צריכת משאבים
 * - מניעת רכישה כפולה
 * - הענקת XP להתקדמות
 *
 * לא מצייר UI.
 * לא קובע מחירים.
 * לא קשור ל-Market או Workshop ישירות.
 */
public class UpgradeSystem {

    // =========================
    // External dependencies
    // =========================
    private final EnumMap<ResourceType, Integer> resourceBank;
    private final EnumMap<CraftedItem, Integer> craftedInventory;

    // =========================
    // Internal state
    // =========================
    private final Set<String> purchasedUpgrades = new HashSet<>();

    private int playerXP = 0;

    public UpgradeSystem(
        EnumMap<ResourceType, Integer> resourceBank,
        EnumMap<CraftedItem, Integer> craftedInventory
    ) {
        this.resourceBank = resourceBank;
        this.craftedInventory = craftedInventory;
    }

    // =========================
    // Public API
    // =========================

    /**
     * בדיקה האם שדרוג כבר נרכש
     */
    public boolean isPurchased(String upgradeId) {
        return purchasedUpgrades.contains(upgradeId);
    }

    /**
     * בדיקה האם ניתן לרכוש שדרוג כרגע
     */
    public boolean canPurchase(UpgradeDefinition upgrade) {

        if (isPurchased(upgrade.id)) {
            return false;
        }

        // בדיקת משאבים גולמיים
        for (Map.Entry<ResourceType, Integer> entry :
            upgrade.requiredResources.entrySet()) {

            int available = resourceBank.getOrDefault(entry.getKey(), 0);
            if (available < entry.getValue()) {
                return false;
            }
        }

        // בדיקת פריטים מעובדים
        for (Map.Entry<CraftedItem, Integer> entry :
            upgrade.requiredCraftedItems.entrySet()) {

            int available = craftedInventory.getOrDefault(entry.getKey(), 0);
            if (available < entry.getValue()) {
                return false;
            }
        }

        return true;
    }

    /**
     * רכישת שדרוג בפועל
     */
    public boolean purchase(UpgradeDefinition upgrade) {

        if (!canPurchase(upgrade)) {
            return false;
        }

        // צריכת משאבים גולמיים
        for (Map.Entry<ResourceType, Integer> entry :
            upgrade.requiredResources.entrySet()) {

            ResourceType type = entry.getKey();
            int cost = entry.getValue();

            resourceBank.put(
                type,
                resourceBank.get(type) - cost
            );
        }

        // צריכת פריטים מעובדים
        for (Map.Entry<CraftedItem, Integer> entry :
            upgrade.requiredCraftedItems.entrySet()) {

            CraftedItem item = entry.getKey();
            int cost = entry.getValue();

            craftedInventory.put(
                item,
                craftedInventory.get(item) - cost
            );
        }

        // סימון כשדרוג שנרכש
        purchasedUpgrades.add(upgrade.id);

        // הענקת XP (רק כאן!)
        playerXP += upgrade.xpReward;

        // אפקטים בפועל יוחלו דרך מערכת אחרת (Stats / Modifiers)
        return true;
    }

    // =========================
    // Progress / XP
    // =========================

    public int getPlayerXP() {
        return playerXP;
    }
}


