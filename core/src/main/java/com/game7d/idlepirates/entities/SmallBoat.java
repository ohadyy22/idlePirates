package com.game7d.idlepirates.entities;

import com.game7d.idlepirates.data.ResourceType;
import com.game7d.idlepirates.workshop.FractionalAccumulator;

import java.util.EnumMap;

public class SmallBoat {

    public float speed;
    public int cargoCapacity;

    public final EnumMap<ResourceType, Integer> cargo =
        new EnumMap<>(ResourceType.class);

    private float distanceRemaining;
    private final FractionalAccumulator movementAccumulator =
        new FractionalAccumulator();

    public SmallBoat(float speed, int cargoCapacity) {
        this.speed = speed;
        this.cargoCapacity = cargoCapacity;
    }

    public void startJourney(float distance) {
        this.distanceRemaining = distance;
    }

    public boolean updateMovement(float delta) {
        int progressed = movementAccumulator.add(speed, delta);
        distanceRemaining -= progressed;
        return distanceRemaining <= 0f;
    }

    public int getCargoTotal() {
        int total = 0;
        for (int v : cargo.values()) total += v;
        return total;
    }
}


