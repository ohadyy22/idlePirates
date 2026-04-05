package com.game7d.idlepirates.core;

import com.badlogic.gdx.math.Vector2;
import com.game7d.idlepirates.data.WreckDefinition;
import com.game7d.idlepirates.entities.*;
import com.game7d.idlepirates.systems.*;
import com.game7d.idlepirates.upgrades.UpgradeSystem;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {

    public MainShip mainShip;

    public List<Wreck> wrecks = new ArrayList<>();
    public List<CrewAssignment> crews = new ArrayList<>();

    public BoatSystem boatSystem;
    public WorkshopSystem workshopSystem;
    public UpgradeSystem upgradeSystem;
    public MapSystem mapSystem;

    public List<WreckDefinition> wreckDefinitions = new ArrayList<>();

    public GameWorld(MainShip mainShip) {
        this.mainShip = mainShip;

        this.boatSystem = new BoatSystem(mainShip, crews);
        this.mapSystem = new MapSystem(mainShip, wreckDefinitions, wrecks);

        initWreckDefinitions();
    }

    private void initWreckDefinitions() {
        wreckDefinitions.add(new WreckDefinition(
            new Vector2(120, 80),
            180f
        ));

        wreckDefinitions.add(new WreckDefinition(
            new Vector2(-200, 150),
            260f
        ));

        wreckDefinitions.add(new WreckDefinition(
            new Vector2(350, -120),
            420f
        ));
    }

    public void update(float delta) {
        mapSystem.update();

        for (Wreck w : wrecks) {
            w.update(delta);
        }

        boatSystem.update(delta);
    }
}



