package com.game7d.idlepirates.entities;

import com.game7d.idlepirates.data.ResourceType;

import java.util.EnumMap;

public class SmallBoat {

    public float speed;
    public int cargoCapacity;

    public final EnumMap<ResourceType, Integer> cargo =
        new EnumMap<>(ResourceType.class);

    // ✅ ויזואל
    public final float startX, startY, endX, endY;
    private float distanceRemaining;
    private float totalDistance;

    // מיקום נוכחי (לציור)
    public float x;
    public float y;

    public SmallBoat(float speed, int cargoCapacity,
                     float startX, float startY,
                     float endX, float endY) {

        this.speed = speed;
        this.cargoCapacity = cargoCapacity;

        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        this.x = startX;
        this.y = startY;

        float dist = (float)Math.hypot(endX - startX, endY - startY);
        startJourney(dist);
    }

    public void startJourney(float distance) {
        this.totalDistance = distance;
        this.distanceRemaining = distance;
    }

    /** @return true אם הגיעה */
    public boolean updateMovement(float delta) {

        if (distanceRemaining <= 0f)
            return true;

        distanceRemaining -= speed * delta;
        if (distanceRemaining < 0) distanceRemaining = 0;

        float t = getProgress();

        x = startX + (endX - startX) * t;
        y = startY + (endY - startY) * t;

        return distanceRemaining <= 0f;
    }

    public float getProgress() {
        if (totalDistance <= 0f) return 1f;
        return 1f - (distanceRemaining / totalDistance);
    }

    public int getCargoTotal() {
        int total = 0;
        for (int v : cargo.values()) total += v;
        return total;
    }
}


