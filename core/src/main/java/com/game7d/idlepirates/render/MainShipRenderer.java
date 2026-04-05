package com.game7d.idlepirates.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MainShipRenderer {

    // ======== גודל לוגי בעולם ========
    // כרגע משתמשים בספינה טרופה, לכן נשאיר זה סביר
    public static final float WORLD_WIDTH  = 130f;
    public static final float WORLD_HEIGHT = 180f;

    private final Sprite sprite;

    // אנימציה
    private float baseX;
    private float baseY;

    public MainShipRenderer(String texturePath) {
        Texture texture = new Texture(
            Gdx.files.internal(texturePath),
            Pixmap.Format.RGBA8888,
            true // mipmaps
        );

        texture.setFilter(
            Texture.TextureFilter.MipMapLinearLinear,
            Texture.TextureFilter.Linear
        );

        sprite = new Sprite(texture);
        sprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);

        // Origin קצת מתחת למרכז – מרגיש “יושב במים”
        sprite.setOrigin(WORLD_WIDTH / 2f, WORLD_HEIGHT * 0.35f);
    }

    public void setPosition(Vector2 worldPosition) {
        baseX = worldPosition.x - WORLD_WIDTH / 2f;
        baseY = worldPosition.y - WORLD_HEIGHT / 2f;

        sprite.setPosition(baseX, baseY);
    }

    public void render(SpriteBatch batch, float time) {

        // ======== אנימציית ים (Idle) ========

        // נדנוד למעלה/למטה
        float bob = MathUtils.sin(time * 1.1f) * 2.5f;

        // סיבוב עדין שמאלה/ימינה
        float rotation = MathUtils.sin(time * 0.6f) * 2f;

        // נשימה קלה
        float scalePulse = 1f + MathUtils.sin(time * 0.8f) * 0.015f;

        sprite.setPosition(baseX, baseY + bob);
        sprite.setRotation(rotation);
        sprite.setScale(scalePulse);

        sprite.draw(batch);

        // ניקוי אחרי ציור
        sprite.setRotation(0f);
        sprite.setScale(1f);
        sprite.setPosition(baseX, baseY);
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}


