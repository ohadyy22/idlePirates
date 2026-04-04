package com.game7d.idlepirates.workshop;

public class FractionalAccumulator {

    private float buffer = 0f;

    public int add(float ratePerSecond, float delta) {
        buffer += ratePerSecond * delta;
        int whole = (int) buffer;
        buffer -= whole;
        return whole;
    }
}


