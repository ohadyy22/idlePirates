package com.game7d.idlepirates.data;

import com.game7d.idlepirates.data.ResourceType;
import java.util.EnumMap;

/**
 * הגדרה לוגית של סוג ספינה טרופה.
 * - אינו משתנה בזמן משחק
 * - אין כאן state (אין hasCrew, אין timers)
 */
public class WreckDefinition {

    public final String id;

    /** מחיר גיוס צוות */
    public final int crewCost;

    /** כמה Vision צריך כדי לראות */
    public final float requiredVision;

    /** חלוקת משאבים באחוזים */
    public final EnumMap<ResourceType, Integer> productionMix;

    /** זמן בסיסי למחזור ייצור (בשניות) */
    public final float baseProductionTime;

    /** כמות בסיסית של משאב לכל איסוף */
    public final int baseCargo;

    public WreckDefinition(
        String id,
        int crewCost,
        float requiredVision,
        float baseProductionTime,
        int baseCargo
    ) {
        this.id = id;
        this.crewCost = crewCost;
        this.requiredVision = requiredVision;
        this.baseProductionTime = baseProductionTime;
        this.baseCargo = baseCargo;

        this.productionMix = new EnumMap<>(ResourceType.class);
    }

    public void addProduction(ResourceType type, int percent) {
        productionMix.put(type, percent);
    }
}

