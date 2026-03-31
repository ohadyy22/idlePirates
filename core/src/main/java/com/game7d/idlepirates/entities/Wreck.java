package com.game7d.idlepirates.entities;

import com.badlogic.gdx.math.Vector2;
import com.game7d.idlepirates.data.ResurceType;
import java.util.EnumMap;

public class Wreck {
    public Vector2 position;
    public EnumMap<ResurceType,Float>   resourseRates;
    public EnumMap<ResurceType,Integer> generated;
    public EnumMap<ResurceType,Float>   fractionalBuffer;

    public Wreck(Vector2 pos, EnumMap<ResurceType,Float> rates)
    {
     this.position         = pos;
     this.resourseRates    = rates;
     this.generated        = new EnumMap<>(ResurceType.class);
     this.fractionalBuffer = new EnumMap<>(ResurceType.class);
     for(ResurceType rt : rates.keySet())
     {
        generated.put(rt,0);
        fractionalBuffer.put(rt,0f);
     }
    }

    public void update(float delta)
    {
        for(ResurceType rt : resourseRates.keySet())
        {
            float produce = resourseRates.get(rt) * delta;
            float newBuffer = fractionalBuffer.get(rt) + produce;
            int wholeUnits = (int)newBuffer;
            if(wholeUnits>0)
            {
                generated.put(rt,generated.get(rt)+wholeUnits);
            }
            fractionalBuffer.put(rt,newBuffer - wholeUnits);
        }
        return;
    }

}
