package com.game7d.idlepirates.entities;

import com.badlogic.gdx.math.Vector2;
import com.game7d.idlepirates.data.ResourceType;

import java.util.EnumMap;

public class MainShip {

    public final Vector2 position;

    public int level = 1;
    public int xp = 0;
    public float visionRange = 200f;

    public final EnumMap<ResourceType, Integer> storage =
        new EnumMap<>(ResourceType.class);

    public MainShip(Vector2 position) {
        this.position = position;
    }

    public void addResource(ResourceType type, int amount) {
        storage.put(type, storage.getOrDefault(type, 0) + amount);
    }

    public void addXP(int amount) {
        xp += amount;
        while (xp >= xpForNextLevel()) {
            xp -= xpForNextLevel();
            level++;
            visionRange += 50f;
        }
    }

    private int xpForNextLevel() {
        return level * 10;
    }
}


