package com.game7d.idlepirates.upgrades;

import com.game7d.idlepirates.data.ResourceType;
import com.game7d.idlepirates.data.CraftedItem;

import java.util.EnumMap;
import java.util.Map;

/**
 * מגדיר שדרוג יחיד במשחק.
 *
 * שדרוג יכול לדרוש:
 * - משאבים גולמיים (ResourceType)
 * - פריטים מעובדים (CraftedItem)
 *
 * השדרוג עצמו הוא פסיבי ונקנה פעם אחת.
 */
public class UpgradeDefinition {

    public final String id;
    public final String displayName;
    public final String description;

    /** דרישות משאבים גולמיים */
    public final EnumMap<ResourceType, Integer> requiredResources;

    /** דרישות פריטים מעובדים */
    public final EnumMap<CraftedItem, Integer> requiredCraftedItems;

    /** כמה XP השדרוג מעניק לשדרוג הספינה הראשית / ההתקדמות */
    public final int xpReward;

    public UpgradeDefinition(
        String id,
        String displayName,
        String description,
        int xpReward
    ) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.xpReward = xpReward;

        this.requiredResources = new EnumMap<>(ResourceType.class);
        this.requiredCraftedItems = new EnumMap<>(CraftedItem.class);
    }

    // --- Helpers ---

    public void addResourceCost(ResourceType type, int amount) {
        requiredResources.put(type, amount);
    }

    public void addCraftedItemCost(CraftedItem item, int amount) {
        requiredCraftedItems.put(item, amount);
    }
}


