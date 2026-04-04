package com.game7d.idlepirates.data;


import java.util.EnumMap;

public class UpgradeDefinition {

    public final String id;
    public final int xpReward;

    public final EnumMap<ResourceType, Integer> costResources;
    public final EnumMap<CraftedItem, Integer> costItems;

    public UpgradeDefinition(String id, int xpReward) {
        this.id = id;
        this.xpReward = xpReward;
        this.costResources = new EnumMap<>(ResourceType.class);
        this.costItems = new EnumMap<>(CraftedItem.class);
    }
}
