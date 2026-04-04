package com.game7d.idlepirates.data;

import com.badlogic.gdx.math.Vector2;

public class WreckDefinition {

    public final Vector2 position;
    public final float requiredVision;

    public boolean revealed = false;

    public WreckDefinition(Vector2 position, float requiredVision) {
        this.position = position;
        this.requiredVision = requiredVision;
    }
}



