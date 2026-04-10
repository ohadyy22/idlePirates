package com.game7d.idlepirates.data;


import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;


/**
 * קטלוג ספינות טרופות התחלתיות.
 * משתמש אך ורק בבנאי המקורי של WreckDefinition.
 */
public class WreckCatalog {


    public static List<WreckDefinition> createInitialWrecks(float centerX, float centerY) {


        List<WreckDefinition> wrecks = new ArrayList<>();


        // ========= ספינה 1 – חינמית =========
        WreckDefinition w1 = new WreckDefinition(
            new Vector2(centerX + 220, centerY),
            50f
        );
        w1.setCrewCost(0);
        w1.addProduction(ResourceType.WOOD, 100);
        wrecks.add(w1);


        // ========= ספינה 2 – 750 =========
        WreckDefinition w2 = new WreckDefinition(
            new Vector2(centerX - 300, centerY + 160),
            90f
        );
        w2.setCrewCost(750);
        w2.addProduction(ResourceType.WOOD, 70);
        w2.addProduction(ResourceType.ROPE, 30);
        wrecks.add(w2);


        // ========= ספינה 3 – 1300 =========
        WreckDefinition w3 = new WreckDefinition(
            new Vector2(centerX, centerY - 280),
            130f
        );
        w3.setCrewCost(1300);
        w3.addProduction(ResourceType.WOOD, 40);
        w3.addProduction(ResourceType.ROPE, 40);
        w3.addProduction(ResourceType.IRON, 20);
        wrecks.add(w3);


        // ========= ספינה 4 – 2400 (מדורג והגיוני) =========
        WreckDefinition w4 = new WreckDefinition(
            new Vector2(centerX + 420, centerY - 220),
            180f
        );
        w4.setCrewCost(2400);
        w4.addProduction(ResourceType.WOOD, 25);
        w4.addProduction(ResourceType.ROPE, 45);
        w4.addProduction(ResourceType.IRON, 30);
        wrecks.add(w4);


        return wrecks;
    }
}



