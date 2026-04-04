package com.game7d.idlepirates.systems;

import com.game7d.idlepirates.data.WreckDefinition;
import com.game7d.idlepirates.entities.MainShip;
import com.game7d.idlepirates.entities.Wreck;

import java.util.List;
import java.util.ArrayList;

public class MapSystem {

    private final MainShip mainShip;
    private final List<WreckDefinition> definitions;
    private final List<Wreck> activeWrecks;

    public MapSystem(MainShip mainShip, List<WreckDefinition> definitions, List<Wreck> activeWrecks) {
        this.mainShip = mainShip;
        this.definitions = definitions;
        this.activeWrecks = activeWrecks;
    }

    public void update() {
        for (WreckDefinition def : definitions) {
            if (!def.revealed && mainShip.visionRange >= def.requiredVision) {
                revealWreck(def);
            }
        }
    }

    private void revealWreck(WreckDefinition def) {
        def.revealed = true;

        Wreck wreck = new Wreck(def.position);
        activeWrecks.add(wreck);

        // כאן בהמשך תוכל:
        // - להוסיף משאבים ל wreck
        // - לשגר אנימציה
        // - להפעיל סאונד
    }
}


