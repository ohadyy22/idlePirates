package com.game7d.idlepirates.workshop;

import java.util.ArrayDeque;
import java.util.Queue;

public class Workshop {

    public int workers = 1;
    public int parallelSlots = 1;

    public final Queue<Job> jobs = new ArrayDeque<>();
}


