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
 * ✔ בודק דרישות
 * ✔ צורך משאבים
 * ✔ מונע רכישה כפולה
 * ✔ מחזיק XP
 *
 * ❌ לא מצייר UI
 * ❌ לא מיישם אפקטים ישירות
 */
public class UpgradeSystem {

    // =========================
    // External state
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

    /** האם שדרוג כבר נרכש */
    public boolean isPurchased(String upgradeId) {
        return purchasedUpgrades.contains(upgradeId);
    }

    /** בדיקה האם אפשר לרכוש שדרוג */
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

    /** רכישת שדרוג בפועל */
    public boolean purchase(UpgradeDefinition upgrade) {

        if (!canPurchase(upgrade)) {
            return false;
        }

        // צריכת משאבים גולמיים
        for (Map.Entry<ResourceType, Integer> entry :
            upgrade.requiredResources.entrySet()) {

            resourceBank.put(
                entry.getKey(),
                resourceBank.get(entry.getKey()) - entry.getValue()
            );
        }

        // צריכת פריטים מעובדים
        for (Map.Entry<CraftedItem, Integer> entry :
            upgrade.requiredCraftedItems.entrySet()) {

            craftedInventory.put(
                entry.getKey(),
                craftedInventory.get(entry.getKey()) - entry.getValue()
            );
        }

        purchasedUpgrades.add(upgrade.id);
        playerXP += upgrade.xpReward;

        return true;
    }

    // =========================
    // Progress / XP
    // =========================

    public int getPlayerXP() {
        return playerXP;
    }

    /** כרגע אין לוגיקת זמן – נשאר כאן להרחבות עתידיות */
    public void update(float delta) {
    }
}


