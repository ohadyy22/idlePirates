package com.game7d.idlepirates.entities;

public class CrewAssignment {

    public enum State {
        TO_WRECK,
        RETURNING,
        UNLOADING
    }

    public final Wreck wreck;
    public SmallBoat boat;
    public State state = State.TO_WRECK;

    public CrewAssignment(Wreck wreck) {
        this.wreck = wreck;
    }
}


