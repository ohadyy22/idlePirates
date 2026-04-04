package com.game7d.idlepirates.data;

import java.util.HashMap;
import java.util.Map;

public class UpgradeCatalog {

    public static final Map<String, UpgradeDefinition> UPGRADES = new HashMap<>();

    static {
        registerStrongSail();
        registerSpyglass();
    }

    private static void registerStrongSail() {
        UpgradeDefinition def =
            new UpgradeDefinition("STRONG_SAIL", 3);

        def.costResources.put(ResourceType.WOOD_REFINED, 2);
        def.costResources.put(ResourceType.ROPE_REFINED, 1);

        UPGRADES.put(def.id, def);
    }

    private static void registerSpyglass() {
        UpgradeDefinition def =
            new UpgradeDefinition("SPYGLASS", 5);

        def.costResources.put(ResourceType.WOOD_REFINED, 1);
        def.costResources.put(ResourceType.ROPE_REFINED, 1);
        def.costResources.put(ResourceType.METAL_REFINED, 1);

        UPGRADES.put(def.id, def);
    }
}


