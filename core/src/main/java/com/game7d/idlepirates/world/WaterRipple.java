package com.game7d.idlepirates.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WaterRipple {


    private final Sprite sprite;
    private float scale;
    private float alpha;
    private final float speed;


    public WaterRipple(Texture texture, float startScale, float speed) {
        this.sprite = new Sprite(texture);
        this.sprite.setOriginCenter();
        this.scale = startScale;
        this.alpha = 1f;
        this.speed = speed;
    }


    public void update(float delta) {
        scale += speed * delta * 0.5f;
        alpha -= speed * 0.35f * delta;


        if (alpha <= 0f) {
            reset();
        }


        sprite.setScale(scale);
        sprite.setAlpha(alpha);
    }


    public void draw(SpriteBatch batch, float x, float y) {
        sprite.setCenter(x, y);
        sprite.draw(batch);
    }


    private void reset() {
        scale = 0.25f + (float)Math.random() * 0.2f;
        alpha = 1f;
    }
}




