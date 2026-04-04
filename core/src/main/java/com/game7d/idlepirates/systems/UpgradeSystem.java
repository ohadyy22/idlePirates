package com.game7d.idlepirates.systems;

import com.game7d.idlepirates.data.UpgradeDefinition;
import com.game7d.idlepirates.data.ResourceType;
import com.game7d.idlepirates.entities.MainShip;

import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class UpgradeSystem {

    private final MainShip mainShip;
    private final Set<String> purchasedUpgrades = new HashSet<>();

    public UpgradeSystem(MainShip mainShip) {
        this.mainShip = mainShip;
    }

    /**
     * בדיקה האם ניתן לרכוש שדרוג
     */
    public boolean canPurchase(UpgradeDefinition def) {

        // בדיקה אם כבר נרכש
        if (purchasedUpgrades.contains(def.id)) {
            return false;
        }

        // בדיקת משאבים
        for (Map.Entry<ResourceType, Integer> entry : def.costResources.entrySet()) {
            ResourceType type = entry.getKey();
            int required = entry.getValue();

            int available = mainShip.storage.getOrDefault(type, 0);
            if (available < required) {
                return false;
            }
        }

        return true;
    }

    /**
     * רכישת שדרוג בפועל
     */
    public void purchase(UpgradeDefinition def) {
        if (!canPurchase(def)) return;

        // צריכת משאבים
        for (Map.Entry<ResourceType, Integer> entry : def.costResources.entrySet()) {
            ResourceType type = entry.getKey();
            int cost = entry.getValue();

            int current = mainShip.storage.get(type);
            mainShip.storage.put(type, current - cost);
        }

        // רישום כשדרוג קבוע
        purchasedUpgrades.add(def.id);

        // XP מתקבל אך ורק כאן
        mainShip.addXP(def.xpReward);

        // TODO בעתיד:
        // applyPassiveEffects(def);
    }

    /**
     * האם שדרוג כבר נרכש
     */
    public boolean isPurchased(String upgradeId) {
        return purchasedUpgrades.contains(upgradeId);
    }

    private void applyPassiveEffects(UpgradeDefinition def) {
        switch (def.id) {

            case "STRONG_SAIL":
                // כל הסירות מהירות יותר
                // לדוגמה: +0.1
                // זה יוחל ע"י חישוב מחדש של סטטים
                break;

            case "SPYGLASS":
                mainShip.visionRange += 50f;
                break;
        }
    }



}


