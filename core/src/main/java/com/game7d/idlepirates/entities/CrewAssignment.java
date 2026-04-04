package com.game7d.idlepirates.entities;

public class CrewAssignment {

    public enum State {
        TO_WRECK,
        RETURNING,
        UNLOADING
    }

    public final SmallBoat boat;
    public final Wreck wreck;

    public State state = State.TO_WRECK;

    public CrewAssignment(SmallBoat boat, Wreck wreck) {
        this.boat = boat;
        this.wreck = wreck;
    }
}


