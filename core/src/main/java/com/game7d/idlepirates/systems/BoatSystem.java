package com.game7d.idlepirates.systems;

import com.badlogic.gdx.math.Vector2;
import com.game7d.idlepirates.entities.*;
import com.game7d.idlepirates.data.ResourceType;

import java.util.List;

public class BoatSystem {

    private final MainShip mainShip;
    private final List<CrewAssignment> crews;

    public BoatSystem(MainShip mainShip, List<CrewAssignment> crews) {
        this.mainShip = mainShip;
        this.crews = crews;
    }

    public void update(float delta) {
        for (CrewAssignment crew : crews) {
            updateCrew(crew, delta);
        }
    }

    private void updateCrew(CrewAssignment crew, float delta) {
        SmallBoat boat = crew.boat;

        switch (crew.state) {
            case TO_WRECK:
                if (boat.updateMovement(delta)) {
                    collect(boat, crew.wreck);
                    startReturn(crew);
                }
                break;

            case RETURNING:
                if (boat.updateMovement(delta)) {
                    crew.state = CrewAssignment.State.UNLOADING;
                }
                break;

            case UNLOADING:
                unload(boat);
                startToWreck(crew);
                break;
        }
    }

    private void collect(SmallBoat boat, Wreck wreck) {
        int free = boat.cargoCapacity - boat.getCargoTotal();
        if (free <= 0) return;

        for (ResourceType type : wreck.stored.keySet()) {
            if (free <= 0) break;
            int take = Math.min(wreck.stored.get(type), free);
            if (take > 0) {
                boat.cargo.put(type,
                    boat.cargo.getOrDefault(type, 0) + take);
                wreck.stored.put(type, wreck.stored.get(type) - take);
                free -= take;
            }
        }
    }

    private void unload(SmallBoat boat) {
        for (ResourceType type : boat.cargo.keySet()) {
            mainShip.addResource(type, boat.cargo.get(type));
        }
        boat.cargo.clear();
    }

    private void startToWreck(CrewAssignment crew) {
        float distance = Vector2.dst(
            mainShip.position.x, mainShip.position.y,
            crew.wreck.position.x, crew.wreck.position.y
        );
        crew.boat.startJourney(distance);
        crew.state = CrewAssignment.State.TO_WRECK;
    }

    private void startReturn(CrewAssignment crew) {
        float distance = Vector2.dst(
            crew.wreck.position.x, crew.wreck.position.y,
            mainShip.position.x, mainShip.position.y
        );
        crew.boat.startJourney(distance);
        crew.state = CrewAssignment.State.RETURNING;
    }
}


