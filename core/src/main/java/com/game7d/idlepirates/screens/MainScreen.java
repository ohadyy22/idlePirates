package com.game7d.idlepirates.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScreen implements Screen {

    // =========================
    // WORLD / VIEW
    // =========================
    private static final float WORLD_SIZE = 4096f;
    private static final float VIEWPORT_WIDTH  = 1080f;
    private static final float VIEWPORT_HEIGHT = 1920f;

    // Vision baseline for cloud scaling
    private static final float BASE_VISION = 200f;

    // =========================
    // CAMERA / BATCH
    // =========================
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    // =========================
    // SPRITES
    // =========================
    private Sprite background;
    private Sprite cloud;

    // Main ship (Hero) – אנכית
    private Sprite mainShip;
    private Vector2 mainShipPos;

    // Wreck – מאוזנת (רחבה)
    private Sprite wreck;
    private Vector2 wreckPos;

    // =========================
    // STATE
    // =========================
    private float time = 0f;

    // זמני בדיקה – בהמשך יבוא מ‑MainShip
    private float visionRange = 200f;

    @Override
    public void show() {
        batch = new SpriteBatch();

        // Camera + Viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        viewport.apply();

        camera.position.set(WORLD_SIZE / 2f, WORLD_SIZE / 2f, 0f);
        camera.update();

        // =========================
        // BACKGROUND
        // =========================
        Texture bgTex = new Texture(Gdx.files.internal("background.png"));
        bgTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        background = new Sprite(bgTex);
        background.setSize(WORLD_SIZE, WORLD_SIZE);
        background.setPosition(0, 0);

        // =========================
        // MAIN SHIP (Hero) – אנכית
        // =========================
        Texture mainTex = new Texture(
            Gdx.files.internal("main_ship.png"),
            Pixmap.Format.RGBA8888,
            true
        );
        mainTex.setFilter(
            Texture.TextureFilter.MipMapLinearLinear,
            Texture.TextureFilter.Linear
        );

        mainShip = new Sprite(mainTex);

        // ✅ Hero גדול ובולט – אנכי יותר
        final float MAIN_W = 180f;
        final float MAIN_H = 260f;

        mainShip.setSize(MAIN_W, MAIN_H);
        mainShip.setOrigin(MAIN_W / 2f, MAIN_H * 0.35f);

        mainShipPos = new Vector2(WORLD_SIZE / 2f, WORLD_SIZE / 2f);

        // =========================
        // WRECK – מאוזנת (רחבה)
        // =========================
        Texture wreckTex = new Texture(
            Gdx.files.internal("wreck.png"),
            Pixmap.Format.RGBA8888,
            true
        );
        wreckTex.setFilter(
            Texture.TextureFilter.MipMapLinearLinear,
            Texture.TextureFilter.Linear
        );

        wreck = new Sprite(wreckTex);

        // ✅ קטן וברור מה-Hero
        final float WRECK_W = 96f;
        final float WRECK_H = 60f;

        wreck.setSize(WRECK_W, WRECK_H);
        wreck.setOrigin(WRECK_W / 2f, WRECK_H * 0.4f);

        // ליד הספינה הראשית – מעט ימינה ולמטה
        wreckPos = new Vector2(
            mainShipPos.x + 260f,
            mainShipPos.y - 120f
        );

        // =========================
        // CLOUD (Fog of War)
        // =========================
        Texture cloudTex = new Texture(
            Gdx.files.internal("cloud.png"),
            Pixmap.Format.RGBA8888,
            true
        );

        cloudTex.setFilter(
            Texture.TextureFilter.MipMapLinearLinear,
            Texture.TextureFilter.Linear
        );

        cloud = new Sprite(cloudTex);
        cloud.setOriginCenter();
    }

    @Override
    public void render(float delta) {
        time += delta;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // =========================
        // WORLD
        // =========================
        background.draw(batch);

        // =========================
        // MAIN SHIP (Hero)
        // =========================
        drawMainShip();

        // =========================
        // WRECK
        // =========================
        drawWreck();

        // =========================
        // CLOUD – תמיד אחרון
        // =========================
        drawCloud();

        batch.end();
    }

    private void drawMainShip() {
        float bob = MathUtils.sin(time * 1.0f) * 2.8f;
        float rotation = MathUtils.sin(time * 0.6f) * 2.0f;
        float pulse = 1f + MathUtils.sin(time * 0.8f) * 0.015f;

        float x = mainShipPos.x - mainShip.getWidth() / 2f;
        float y = mainShipPos.y - mainShip.getHeight() / 2f;

        mainShip.setPosition(x, y + bob);
        mainShip.setRotation(rotation);
        mainShip.setScale(pulse);

        mainShip.draw(batch);

        mainShip.setRotation(0f);
        mainShip.setScale(1f);
        mainShip.setPosition(x, y);
    }

    private void drawWreck() {
        float bob = MathUtils.sin(time * 0.8f) * 1.3f;
        float rotation = MathUtils.sin(time * 0.4f) * 0.9f;

        float x = wreckPos.x - wreck.getWidth() / 2f;
        float y = wreckPos.y - wreck.getHeight() / 2f - 10f;

        wreck.setPosition(x, y + bob);
        wreck.setRotation(rotation);

        wreck.draw(batch);

        wreck.setRotation(0f);
        wreck.setPosition(x, y);
    }

    private void drawCloud() {

        // מרכז המסך בעולם (ולא הספינה ישירות)
        float focusX = camera.position.x;
        float focusY = camera.position.y;

        float baseScale = visionRange / BASE_VISION;

        // אנימציות עדינות
        float pulse = MathUtils.sin(time * 0.35f) * 0.015f;
        float driftX = MathUtils.sin(time * 0.18f) * 4f;
        float driftY = MathUtils.cos(time * 0.14f) * 3f;
        float rotation = MathUtils.sin(time * 0.10f) * 0.4f;

        // אליפסה אנכית (מותאם למובייל)
        float scaleX = baseScale * 0.9f * (1f + pulse);
        float scaleY = baseScale * 1.3f * (1f + pulse);

        cloud.setOriginCenter();
        cloud.setScale(scaleX, scaleY);
        cloud.setRotation(rotation);

        cloud.setPosition(
            focusX - cloud.getWidth() / 2f + driftX,
            focusY - cloud.getHeight() / 2f + driftY
        );

        cloud.draw(batch);

        cloud.setRotation(0f);
    }



    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        background.getTexture().dispose();
        mainShip.getTexture().dispose();
        wreck.getTexture().dispose();
        cloud.getTexture().dispose();
    }
}


