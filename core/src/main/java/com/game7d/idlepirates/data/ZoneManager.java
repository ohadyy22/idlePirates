package com.game7d.idlepirates.data;

public class ZoneManager {

    public static int getZoneForVision(float visionRange) {
        if (visionRange < 200) return 1;
        if (visionRange < 350) return 2;
        if (visionRange < 500) return 3;
        if (visionRange < 650) return 4;
        if (visionRange < 800) return 5;
        if (visionRange < 1000) return 6;
        return 7;
    }
}
