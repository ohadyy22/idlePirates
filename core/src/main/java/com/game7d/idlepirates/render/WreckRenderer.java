package com.game7d.idlepirates.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class WreckRenderer {

    // ✅ קטן משמעותית מהספינה הראשית
    public static final float WORLD_WIDTH  = 96f;
    public static final float WORLD_HEIGHT = 60f;

    private final Sprite sprite;
    private float baseX;
    private float baseY;

    public WreckRenderer(String texturePath) {
        Texture texture = new Texture(
            Gdx.files.internal(texturePath),
            Pixmap.Format.RGBA8888,
            true
        );

        texture.setFilter(
            Texture.TextureFilter.MipMapLinearLinear,
            Texture.TextureFilter.Linear
        );

        sprite = new Sprite(texture);
        sprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        sprite.setOrigin(WORLD_WIDTH / 2f, WORLD_HEIGHT * 0.4f);
    }

    public void setPosition(Vector2 worldPos) {
        baseX = worldPos.x - WORLD_WIDTH / 2f;
        baseY = worldPos.y - WORLD_HEIGHT / 2f;
        sprite.setPosition(baseX, baseY);
    }

    public void render(SpriteBatch batch, float time) {

        // ✅ אנימציה עדינה יותר מהספינה הראשית
        float bob = MathUtils.sin(time * 0.8f) * 1.2f;
        float rotation = MathUtils.sin(time * 0.4f) * 0.8f;

        sprite.setPosition(baseX, baseY + bob);
        sprite.setRotation(rotation);

        sprite.draw(batch);

        // ניקוי
        sprite.setRotation(0f);
        sprite.setPosition(baseX, baseY);
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}


