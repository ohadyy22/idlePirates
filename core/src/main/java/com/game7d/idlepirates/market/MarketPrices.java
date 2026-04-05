package com.game7d.idlepirates.market;

import com.game7d.idlepirates.data.ResourceType;
import com.game7d.idlepirates.data.CraftedItem;

public class MarketPrices {

    // ===== RAW RESOURCES =====
    public static int getRawPrice(ResourceType type) {
        switch (type) {
            case WOOD:  return 1;
            case IRON:  return 2;
            case ROPE:  return 4;
            case SAND:  return 1;
            default:	return 0;
        }
    }

    // ===== CRAFTED ITEMS =====
    public static int getCraftedPrice(CraftedItem item) {
        switch (item) {
            case WOOD_PLANK: 	return 1200; // 1000 WOOD → 1200$
            case REFINED_ROPE:   return 800;
            case IRON_PLATE: 	return 600;

            // שילובים
            case STRONG_SAIL:	return 2500;
            case STORAGE_NET:	return 1800;

            default:         	return 0;
        }
    }
}


