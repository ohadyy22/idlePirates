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

public class GameWorld {

    public final MainShip mainShip;
    public final List<Wreck> wrecks = new ArrayList<>();

    public final EnumMap<ResourceType, Integer> resourceBank =
        new EnumMap<>(ResourceType.class);
    public final EnumMap<CraftedItem, Integer> craftedInventory =
        new EnumMap<>(CraftedItem.class);

    public final BoatSystem boatSystem;
    public final WorkshopSystem workshopSystem;
    public final UpgradeSystem upgradeSystem;

    public GameWorld(MainShip mainShip) {
        this.mainShip = mainShip;

        initWrecks();

        this.boatSystem = new BoatSystem(mainShip, wrecks);
        this.workshopSystem = new WorkshopSystem(mainShip);
        this.upgradeSystem = new UpgradeSystem(resourceBank, craftedInventory);
    }

    private void initWrecks() {
        List<WreckDefinition> defs = WreckCatalog.createAllDefinitions();

        if (defs == null || defs.isEmpty()) {
            throw new IllegalStateException("No wreck definitions in catalog");
        }

        // מיקומים לדוגמה – אפשר להזיז אחר כך
        wrecks.add(new Wreck(defs.get(0),
            new Vector2(mainShip.position.x + 260f, mainShip.position.y - 120f)));

        wrecks.add(new Wreck(defs.get(1),
            new Vector2(mainShip.position.x - 300f, mainShip.position.y + 100f)));

        wrecks.add(new Wreck(defs.get(2),
            new Vector2(mainShip.position.x + 120f, mainShip.position.y - 280f)));

        wrecks.add(new Wreck(defs.get(3),
            new Vector2(mainShip.position.x - 350f, mainShip.position.y - 160f)));
    }

    public void update(float delta) {
        for (Wreck wreck : wrecks) {
            wreck.update(delta);
        }
        boatSystem.update(delta);
        workshopSystem.update(delta);
        upgradeSystem.update(delta);
    }
}


