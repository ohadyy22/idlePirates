package com.game7d.idlepirates.data;


import com.badlogic.gdx.math.Vector2;
import java.util.EnumMap;


/**
 * הגדרה לוגית של ספינה טרופה.
 * תואם ב-100% למבנה המקורי, עם הרחבות לא שוברים.
 */
public class WreckDefinition {


    // ===== מה שהיה אצלך =====
    public final Vector2 position;
    public final float requiredVision;


    public boolean revealed = false;


    public WreckDefinition(Vector2 position, float requiredVision) {
        this.position = position;
        this.requiredVision = requiredVision;
        this.productionMix = new EnumMap<>(ResourceType.class);
    }


    // ===== הרחבות חדשות, עם ערכי ברירת מחדל =====


    /** מחיר גיוס צוות (ברירת מחדל: חינם) */
    private int crewCost = 0;


    /** האם כבר נשלח צוות */
    private boolean hasCrew = false;


    /** חלוקת משאבים (באחוזים) */
    public final EnumMap<ResourceType, Integer> productionMix;


    // ===== API =====


    public void setCrewCost(int crewCost) {
        this.crewCost = crewCost;
    }


    public int getCrewCost() {
        return crewCost;
    }


    public boolean hasCrew() {
        return hasCrew;
    }


    public void assignCrew() {
        this.hasCrew = true;
    }


    public void addProduction(ResourceType type, int percent) {
        productionMix.put(type, percent);
    }
}



