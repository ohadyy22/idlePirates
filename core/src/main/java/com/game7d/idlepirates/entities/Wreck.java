package com.game7d.idlepirates.entities;

import com.badlogic.gdx.math.Vector2;
import com.game7d.idlepirates.data.ResourceType;
import com.game7d.idlepirates.workshop.FractionalAccumulator;

import java.util.EnumMap;

public class Wreck {

    public final Vector2 position;

    public final EnumMap<ResourceType, Float> productionRates =
        new EnumMap<>(ResourceType.class);

    public final EnumMap<ResourceType, Integer> stored =
        new EnumMap<>(ResourceType.class);

    private final EnumMap<ResourceType, FractionalAccumulator> accumulators =
        new EnumMap<>(ResourceType.class);

    public Wreck(Vector2 position) {
        this.position = position;
    }

    public void addResource(ResourceType type, float ratePerSecond) {
        productionRates.put(type, ratePerSecond);
        stored.put(type, 0);
        accumulators.put(type, new FractionalAccumulator());
    }

    public void update(float delta) {
        for (ResourceType type : productionRates.keySet()) {
            int produced = accumulators
                .get(type)
                .add(productionRates.get(type), delta);
            if (produced > 0) {
                stored.put(type, stored.get(type) + produced);
            }
        }
    }
}


