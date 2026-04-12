package com.game7d.idlepirates.data;

import java.util.ArrayList;
import java.util.List;

/**
 * קטלוג של כל סוגי הספינות במשחק.
 * - מקור אמת יחיד לתוכן
 * - לא יודע כלום על מיקום או world
 */
public class WreckCatalog {

    public static List<WreckDefinition> createAllDefinitions() {

        List<WreckDefinition> defs = new ArrayList<>();

        // ===== ספינה 1 – חינמית =====
        WreckDefinition wood = new WreckDefinition(
            "WOOD_WRECK",
            0,      	// crewCost
            0f,     	// requiredVision
            5f,     	// production time
            5       	// cargo
        );
        wood.addProduction(ResourceType.WOOD, 100);
        defs.add(wood);

        // ===== ספינה 2 =====
        WreckDefinition rope = new WreckDefinition(
            "ROPE_WRECK",
            300,
            80f,
            6f,
            6
        );
        rope.addProduction(ResourceType.WOOD, 60);
        rope.addProduction(ResourceType.ROPE, 40);
        defs.add(rope);

        // ===== ספינה 3 =====
        WreckDefinition iron = new WreckDefinition(
            "IRON_WRECK",
            600,
            120f,
            7f,
            8
        );
        iron.addProduction(ResourceType.IRON, 100);
        defs.add(iron);

        // ===== ספינה 4 =====
        WreckDefinition mixed = new WreckDefinition(
            "MIXED_WRECK",
            1000,
            200f,
            8f,
            10
        );
        mixed.addProduction(ResourceType.WOOD, 40);
        mixed.addProduction(ResourceType.ROPE, 40);
        mixed.addProduction(ResourceType.IRON, 20);
        defs.add(mixed);

        return defs;
    }
}


