package com.game7d.idlepirates.entities;

public class CrewAssignment {

    public enum State {
        TO_WRECK,
        RETURNING,
        UNLOADING
    }

    public final Wreck wreck;
    public final SmallBoat boat;

    public State state = State.TO_WRECK;

    public CrewAssignment(Wreck wreck, float boatSpeed, int cargoCapacity) {
        this.wreck = wreck;
        this.boat = new SmallBoat(boatSpeed, cargoCapacity);
    }
}


