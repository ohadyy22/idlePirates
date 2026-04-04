package com.game7d.idlepirates.workshop;

import com.game7d.idlepirates.data.ResourceType;

import java.util.EnumMap;

public class Job {

    public final EnumMap<ResourceType, Integer> input =
        new EnumMap<>(ResourceType.class);

    public final EnumMap<ResourceType, Integer> output =
        new EnumMap<>(ResourceType.class);

    public float workRequired;
    private float workDone;

    private final FractionalAccumulator accumulator =
        new FractionalAccumulator();

    public Job(float workRequired) {
        this.workRequired = workRequired;
    }

    public boolean update(float productivity, float delta) {
        int progress = accumulator.add(productivity, delta);
        workDone += progress;
        return workDone >= workRequired;
    }
}


