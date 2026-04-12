package com.game7d.idlepirates.systems;

import com.game7d.idlepirates.data.ResourceType;
import com.game7d.idlepirates.entities.MainShip;
import com.game7d.idlepirates.workshop.Job;
import com.game7d.idlepirates.workshop.Workshop;

import java.util.Iterator;
import java.util.Map;

/**
 * WorkshopSystem
 *
 * אחראי על:
 * - ניהול עבודות (Jobs)
 * - חלוקת כוח אדם
 * - יצירת תוצרים לאורך זמן
 *
 * לא מצייר ולא תלוי ב-UI.
 */
public class WorkshopSystem {

    private final Workshop workshop;
    private final MainShip mainShip;

    /** יוצר מערכת Workshop עצמאית */
    public WorkshopSystem(MainShip mainShip) {
        this.mainShip = mainShip;
        this.workshop = new Workshop();
    }

    public Workshop getWorkshop() {
        return workshop;
    }

    /** עדכון לוגי */
    public void update(float delta) {

        if (workshop.jobs.isEmpty())
            return;

        int active =
            Math.min(workshop.parallelSlots, workshop.jobs.size());

        if (active <= 0)
            return;

        float productivityPerJob =
            workshop.workers / (float) active;

        Iterator<Job> it = workshop.jobs.iterator();
        int processed = 0;

        while (it.hasNext() && processed < active) {

            Job job = it.next();
            processed++;

            if (job.update(productivityPerJob, delta)) {

                // העברת התוצרים לספינה הראשית
                for (Map.Entry<ResourceType, Integer> entry
                    : job.output.entrySet()) {

                    mainShip.addResource(
                        entry.getKey(),
                        entry.getValue()
                    );
                }

                it.remove();
            }
        }
    }
}


