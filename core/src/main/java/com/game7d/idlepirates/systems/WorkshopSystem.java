package com.game7d.idlepirates.systems;

import com.game7d.idlepirates.data.ResourceType;
import com.game7d.idlepirates.entities.MainShip;
import com.game7d.idlepirates.workshop.Job;
import com.game7d.idlepirates.workshop.Workshop;

import java.util.Iterator;
import java.util.Map;

public class WorkshopSystem {

    private final Workshop workshop;
    private final MainShip mainShip;

    public WorkshopSystem(Workshop workshop, MainShip mainShip) {
        this.workshop = workshop;
        this.mainShip = mainShip;
    }

    public void update(float delta) {
        if (workshop.jobs.isEmpty()) return;

        int active = Math.min(workshop.parallelSlots, workshop.jobs.size());
        float productivityPerJob = workshop.workers / (float) active;

        Iterator<Job> it = workshop.jobs.iterator();
        int processed = 0;

        while (it.hasNext() && processed < active) {
            Job job = it.next();
            processed++;

            if (job.update(productivityPerJob, delta)) {
                for (Map.Entry<ResourceType, Integer> entry : job.output.entrySet()) {
                    mainShip.addResource(entry.getKey(), entry.getValue());
                }
                it.remove();
            }
        }
    }
}


