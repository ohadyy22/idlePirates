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

import com.game7d.idlepirates.render.MainShipRenderer;

public class MainScreen implements Screen {

    // =========================
    // WORLD / VIEW CONFIG
    // =========================
    private static final float WORLD_SIZE = 4096f;
    private static final float VIEWPORT_WIDTH  = 1080f;
    private static final float VIEWPORT_HEIGHT = 1920f;

    // Vision baseline (לסקייל ענן)
    private static final float BASE_VISION = 200f;

    // =========================
    // RENDERING
    // =========================
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    // =========================
    // WORLD SPRITES
    // =========================
    private Sprite background;
    private Sprite cloud;

    private MainShipRenderer mainShip;
    private Vector2 mainShipPosition;

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

        // מרכז מצלמה לעולם
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
        // MAIN SHIP
        // =========================
        mainShipPosition = new Vector2(WORLD_SIZE / 2f, WORLD_SIZE / 2f);
        mainShip = new MainShipRenderer("main_ship.png");

        // =========================
        // CLOUD (Fog of War)
        // =========================
        Texture cloudTex = new Texture(
            Gdx.files.internal("cloud.png"),
            Pixmap.Format.RGBA8888,
            true // mipmaps חשובים מאוד
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

        // ניקוי מסך
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Camera
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // =========================
        // DRAW WORLD
        // =========================
        background.draw(batch);

        // =========================
        // DRAW MAIN SHIP
        // =========================
        mainShip.setPosition(mainShipPosition);
        mainShip.render(batch, time);

        // =========================
        // DRAW CLOUD (LAST)
        // =========================
        drawCloud();

        batch.end();
    }

    private void drawCloud() {

        // בסיס סקייל לפי Vision
        float baseScale = visionRange / BASE_VISION;

        // Pulse עדין (נשימה)
        float pulse = MathUtils.sin(time * 0.35f) * 0.01f;

        // Drift קטן (ערפל זז)
        float driftX = MathUtils.sin(time * 0.15f) * 4f;
        float driftY = MathUtils.cos(time * 0.12f) * 3f;

        float finalScale = baseScale * (1f + pulse);

        cloud.setScale(finalScale);

        cloud.setPosition(
            mainShipPosition.x - cloud.getWidth() / 2f + driftX,
            mainShipPosition.y - cloud.getHeight() / 2f + driftY
        );

        cloud.draw(batch);
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
        cloud.getTexture().dispose();
        mainShip.dispose();
    }
}


