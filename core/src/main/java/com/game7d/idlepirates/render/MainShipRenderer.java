package com.game7d.idlepirates.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

public class MainShipRenderer {

    // ✅ הגדלים הלוגיים של הספינה בעולם
    public static final float WORLD_WIDTH  = 180f;
    public static final float WORLD_HEIGHT = 180f;

    private final Sprite sprite;

    public MainShipRenderer(String texturePath) {

        Texture texture = new Texture(
            Gdx.files.internal(texturePath),
            Pixmap.Format.RGBA8888,
            true // ✅ mipmaps
        );

        texture.setFilter(
            Texture.TextureFilter.MipMapLinearLinear,
            Texture.TextureFilter.Linear
        );

        sprite = new Sprite(texture);
        sprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        sprite.setOriginCenter();
    }

    public void setPosition(Vector2 worldPosition) {
        sprite.setPosition(
            worldPosition.x - WORLD_WIDTH / 2f,
            worldPosition.y - WORLD_HEIGHT / 2f
        );
    }

    public void render(SpriteBatch batch, float time) {
        // ✅ אנימציית idle פרוגרמטית (בלי sprite-sheet)
        float bob = (float) Math.sin(time * 1.2f) * 1.5f;
        sprite.setY(sprite.getY() + bob);

        float rotation = (float) Math.sin(time * 0.6f) * 1.2f;
        sprite.setRotation(rotation);

        sprite.draw(batch);

        // להחזיר מצבים
        sprite.setRotation(0);
        sprite.setY(sprite.getY() - bob);
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}



