package com.game7d.idlepirates.systems;

import com.badlogic.gdx.math.Vector2;
import com.game7d.idlepirates.entities.*;
import com.game7d.idlepirates.data.ResourceType;

import java.util.ArrayList;
import java.util.List;

public class BoatSystem {

    private final MainShip mainShip;
    private final List<Wreck> wrecks;

    private final List<CrewAssignment> crews = new ArrayList<>();

    // ערכי בסיס – בהמשך יבואו משדרוגים
    private static final float BASE_BOAT_SPEED = 100f;   // units/sec
    private static final int   BASE_CARGO  	= 10;

    public BoatSystem(MainShip mainShip, List<Wreck> wrecks) {
        this.mainShip = mainShip;
        this.wrecks = wrecks;
    }

    public void update(float delta) {

        // ודא שלכל Wreck עם צוות יש CrewAssignment
        for (Wreck wreck : wrecks) {
            if (wreck.hasCrew && !hasCrewAssigned(wreck)) {
                crews.add(new CrewAssignment(
                    wreck,
                    BASE_BOAT_SPEED,
                    BASE_CARGO
                ));
                startToWreck(crews.get(crews.size() - 1));
            }
        }

        for (CrewAssignment crew : crews) {
            updateCrew(crew, delta);
        }
    }

    private boolean hasCrewAssigned(Wreck wreck) {
        for (CrewAssignment crew : crews) {
            if (crew.wreck == wreck) {
                return true;
            }
        }
        return false;
    }

    private void updateCrew(CrewAssignment crew, float delta) {

        switch (crew.state) {

            case TO_WRECK:
                if (crew.boat.updateMovement(delta)) {
                    collect(crew.boat, crew.wreck);
                    startReturn(crew);
                }
                break;

            case RETURNING:
                if (crew.boat.updateMovement(delta)) {
                    crew.state = CrewAssignment.State.UNLOADING;
                }
                break;

            case UNLOADING:
                unload(crew.boat);
                startToWreck(crew);
                break;
        }
    }

    private void collect(SmallBoat boat, Wreck wreck) {

        int free = boat.cargoCapacity - boat.getCargoTotal();
        if (free <= 0) return;

        for (ResourceType type : wreck.stored.keySet()) {
            if (free <= 0) break;

            int available = wreck.stored.get(type);
            int take = Math.min(available, free);

            if (take > 0) {
                boat.cargo.put(
                    type,
                    boat.cargo.getOrDefault(type, 0) + take
                );
                wreck.stored.put(type, available - take);
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
            mainShip.position.x,
            mainShip.position.y,
            crew.wreck.position.x,
            crew.wreck.position.y
        );

        crew.boat.startJourney(distance);
        crew.state = CrewAssignment.State.TO_WRECK;
    }

    private void startReturn(CrewAssignment crew) {

        float distance = Vector2.dst(
            crew.wreck.position.x,
            crew.wreck.position.y,
            mainShip.position.x,
            mainShip.position.y
        );

        crew.boat.startJourney(distance);
        crew.state = CrewAssignment.State.RETURNING;
    }
}


