package com.game7d.idlepirates.progress;

import com.game7d.idlepirates.data.ResourceType;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * אחראי על גילוי משאבים גולמיים (ResourceType).
 *
 * משאב נחשב "מגולה" ברגע שלשחקן יש לפחות ספינה אחת
 * שמפיקה אותו.
 *
 * משאבים שלא התגלו:
 * - לא מופיעים ב-Market
 * - לא מופיעים ב-UI
 * - השחקן לא "יודע" שהם קיימים
 */
public class ResourceDiscovery {

    private final EnumSet<ResourceType> discovered =
        EnumSet.noneOf(ResourceType.class);

    /**
     * סימון משאב כ"מגולה".
     * ניתן לקרוא לפונקציה הזו מספר פעמים – זה בטוח.
     */
    public void discover(ResourceType type) {
        discovered.add(type);
    }

    /**
     * בדיקה האם משאב כבר התגלה.
     */
    public boolean isDiscovered(ResourceType type) {
        return discovered.contains(type);
    }

    /**
     * כל המשאבים שהתגלו – לקריאה בלבד.
     * מיועד ל-Market UI.
     */
    public Set<ResourceType> getDiscovered() {
        return Collections.unmodifiableSet(discovered);
    }

    /**
     * לאיפוס (רק אם תרצה בעתיד: New Game / Prestige).
     */
    public void reset() {
        discovered.clear();
    }
}


