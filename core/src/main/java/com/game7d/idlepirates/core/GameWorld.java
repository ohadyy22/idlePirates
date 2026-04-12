package com.game7d.idlepirates.core;

import com.badlogic.gdx.math.Vector2;
import com.game7d.idlepirates.data.WreckCatalog;
import com.game7d.idlepirates.data.WreckDefinition;
import com.game7d.idlepirates.entities.MainShip;
import com.game7d.idlepirates.entities.Wreck;
import com.game7d.idlepirates.systems.BoatSystem;
import com.game7d.idlepirates.systems.WorkshopSystem;
import com.game7d.idlepirates.upgrades.UpgradeSystem;
import com.game7d.idlepirates.data.ResourceType;
import com.game7d.idlepirates.data.CraftedItem;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * GameWorld
 *
 * מחזיק את כל מצב המשחק הלוגי:
 * - ישויות (Wreck, MainShip)
 * - מערכות (BoatSystem, WorkshopSystem, UpgradeSystem)
 *
 * אין כאן:
 * - UI
 * - Rendering
 * - Input
 */
public class GameWorld {

    public final MainShip mainShip;

    /** מופעי ספינות טרופות בעולם */
    public final List<Wreck> wrecks = new ArrayList<>();

    /** בנקים משותפים */
    public final EnumMap<ResourceType, Integer> resourceBank =
        new EnumMap<>(ResourceType.class);

    public final EnumMap<CraftedItem, Integer> craftedInventory =
        new EnumMap<>(CraftedItem.class);

    /** מערכות */
    public final BoatSystem boatSystem;
    public final WorkshopSystem workshopSystem;
    public final UpgradeSystem upgradeSystem;

    public GameWorld(MainShip mainShip) {
        this.mainShip = mainShip;

        // יצירת מופעי Wreck מהקטלוג
        initWrecksFromCatalog();

        // מערכות
        this.boatSystem = new BoatSystem(mainShip, wrecks);
        this.workshopSystem = new WorkshopSystem(mainShip);
        this.upgradeSystem = new UpgradeSystem(resourceBank, craftedInventory);
    }

    /** יצירת כל ה-Wrecks בעולם מהקטלוג */
    private void initWrecksFromCatalog() {

        List<WreckDefinition> defs =
            WreckCatalog.createAllDefinitions();

        // מיקומים דוגמה (בהמשך יבואו מ-Save / MapGen)
        wrecks.add(new Wreck(defs.get(0),
            new Vector2(120, 80)));

        wrecks.add(new Wreck(defs.get(1),
            new Vector2(-200, 150)));

        wrecks.add(new Wreck(defs.get(2),
            new Vector2(350, -120)));

        wrecks.add(new Wreck(defs.get(3),
            new Vector2(-420, -260)));
    }

    /** עדכון לוגי של העולם */
    public void update(float delta) {

        for (Wreck wreck : wrecks) {
            wreck.update(delta);
        }

        boatSystem.update(delta);
        workshopSystem.update(delta);
        upgradeSystem.update(delta);
    }
}


