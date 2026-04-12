package com.game7d.idlepirates.entities;

import com.badlogic.gdx.math.Vector2;
import com.game7d.idlepirates.data.ResourceType;
import com.game7d.idlepirates.data.WreckDefinition;
import com.game7d.idlepirates.workshop.FractionalAccumulator;

import java.util.EnumMap;

/**
 * מופע חי של ספינה טרופה בעולם.
 * - יודע איפה הוא
 * - יודע אם יש צוות
 * - מנהל ייצור ומשאבים
 */
public class Wreck {

    public final WreckDefinition definition;
    public final Vector2 position;

    /** האם נשלח צוות */
    public boolean hasCrew = false;

    /** משאבים שנאגרו */
    public final EnumMap<ResourceType, Integer> stored =
        new EnumMap<>(ResourceType.class);

    private final EnumMap<ResourceType, FractionalAccumulator> accumulators =
        new EnumMap<>(ResourceType.class);

    private float productionTimer = 0f;

    public Wreck(WreckDefinition definition, Vector2 position) {
        this.definition = definition;
        this.position = position;

        for (ResourceType type : definition.productionMix.keySet()) {
            stored.put(type, 0);
            accumulators.put(type, new FractionalAccumulator());
        }
    }

    public void update(float delta) {
        if (!hasCrew) return;

        productionTimer += delta;

        if (productionTimer >= definition.baseProductionTime) {
            productionTimer = 0f;
            produce();
        }
    }

    private void produce() {
        for (ResourceType type : definition.productionMix.keySet()) {
            int amount = definition.baseCargo *
                definition.productionMix.get(type) / 100;
            stored.put(type, stored.get(type) + amount);
        }
    }
}


