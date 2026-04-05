package com.game7d.idlepirates.upgrades;

import com.game7d.idlepirates.data.ResourceType;
import com.game7d.idlepirates.data.CraftedItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * מאגר מרכזי של כל השדרוגים במשחק.
 * כאן מגדירים שדרוגים פעם אחת.
 */
public class UpgradeCatalog {

    private static final Map<String, UpgradeDefinition> UPGRADES =
        new HashMap<>();

    static {
        registerEarlyUpgrades();
        // בעתיד:
        // registerMidGameUpgrades();
        // registerLateGameUpgrades();
    }

    // =========================
    // Early Game
    // =========================
    private static void registerEarlyUpgrades() {

        // --- Strong Sail ---
        UpgradeDefinition strongSail = new UpgradeDefinition(
            "STRONG_SAIL",
            "Strong Sail",
            "Improves travel speed of all crews.",
            10
        );

        // נדרש עיבוד – לא משאבים גולמיים
        strongSail.addCraftedItemCost(CraftedItem.WOOD_PLANK, 5);
        strongSail.addCraftedItemCost(CraftedItem.REFINED_ROPE, 2);

        UPGRADES.put(strongSail.id, strongSail);

        // --- Storage Nets ---
        UpgradeDefinition storageNets = new UpgradeDefinition(
            "STORAGE_NETS",
            "Storage Nets",
            "Crews can carry more resources per trip.",
            8
        );

        storageNets.addCraftedItemCost(CraftedItem.REFINED_ROPE, 3);
        storageNets.addCraftedItemCost(CraftedItem.IRON_PLATE, 1);

        UPGRADES.put(storageNets.id, storageNets);

        // --- Faster Production ---
        UpgradeDefinition fasterProduction = new UpgradeDefinition(
            "FASTER_PRODUCTION",
            "Efficient Crews",
            "Crews collect resources faster.",
            6
        );

        // דוגמה לשדרוג שדורש גם Raw וגם Crafted
        fasterProduction.addResourceCost(ResourceType.WOOD, 500);
        fasterProduction.addCraftedItemCost(CraftedItem.WOOD_PLANK, 1);

        UPGRADES.put(fasterProduction.id, fasterProduction);
    }

    // =========================
    // Public API
    // =========================

    public static UpgradeDefinition get(String id) {
        return UPGRADES.get(id);
    }

    public static Collection<UpgradeDefinition> getAll() {
        return UPGRADES.values();
    }
}


