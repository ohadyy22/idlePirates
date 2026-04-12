package com.game7d.idlepirates.screens;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game7d.idlepirates.data.CraftedItem;
import com.game7d.idlepirates.data.ResourceType;
import com.game7d.idlepirates.data.WreckCatalog;
import com.game7d.idlepirates.data.WreckDefinition;
import com.game7d.idlepirates.entities.Wreck;
import com.game7d.idlepirates.market.MarketSystem;
import com.game7d.idlepirates.progress.ResourceDiscovery;
import com.game7d.idlepirates.ui.MarketPanel;
import com.game7d.idlepirates.world.WaterRipple;

import java.util.EnumMap;
import java.util.List;

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
    private Array<Wreck> wrecks;
    private Sprite wreckSprite;


    // =========================
    // STATE
    // =========================
    private float time = 0f;

    // זמני בדיקה – בהמשך יבוא מ‑MainShip
    private float visionRange = 200f;

    // --- UI ---
    private Stage uiStage;
    private Skin skin;

    // --- Market ---
    private MarketSystem marketSystem;
    private MarketPanel marketPanel;
    private Label goldLabel;

    // ===== ECONOMY =====
    private EnumMap<ResourceType, Integer> resourceBank;
    private EnumMap<CraftedItem, Integer> craftedInventory;
    private ResourceDiscovery resourceDiscovery;

    //water move
    private Array<WaterRipple> ripples;
    private Texture rippleTexture;
    private Texture strokesTex;
    private Sprite strokesSprite;
    private float strokesOffsetX;
    private float strokesOffsetY;





    @Override
    public void show() {
        batch = new SpriteBatch();

        // Camera + Viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        viewport.apply();

        camera.position.set(WORLD_SIZE / 2f, WORLD_SIZE / 2f, 0f);
        camera.update();

        wrecks = new Array<>();

        List<WreckDefinition> defs = WreckCatalog.createAllDefinitions();

        mainShipPos = new Vector2(WORLD_SIZE / 2f, WORLD_SIZE / 2f);


// מיקומים לדוגמה (4 ספינות)
        wrecks.add(new Wreck(defs.get(0), new Vector2(mainShipPos.x + 260f, mainShipPos.y - 120f)));
        wrecks.add(new Wreck(defs.get(1), new Vector2(mainShipPos.x - 300f, mainShipPos.y + 100f)));
        wrecks.add(new Wreck(defs.get(2), new Vector2(mainShipPos.x + 120f, mainShipPos.y - 280f)));
        wrecks.add(new Wreck(defs.get(3), new Vector2(mainShipPos.x - 350f, mainShipPos.y - 160f)));



        // =========================
        // WORLD (Background)
        // =========================
        // BACKGROUND
        // =========================
        Texture bgTex = new Texture(Gdx.files.internal("background.png"));
        bgTex.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        bgTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        background = new Sprite(bgTex);
        background.setSize(WORLD_SIZE, WORLD_SIZE);
        background.setPosition(0, 0);


        rippleTexture = new Texture(Gdx.files.internal("ripple.png"));
        rippleTexture.setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        );

        ripples = new Array<>();
        ripples.add(new WaterRipple(rippleTexture, 0.3f, 0.25f));
        ripples.add(new WaterRipple(rippleTexture, 0.5f, 0.20f));
        ripples.add(new WaterRipple(rippleTexture, 0.7f, 0.15f));

        strokesTex = new Texture(Gdx.files.internal("water_strokes.png"));
        strokesTex.setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        );

        strokesSprite = new Sprite(strokesTex);
        strokesSprite.setSize(WORLD_SIZE, WORLD_SIZE);
        strokesSprite.setPosition(0,0);
        strokesSprite.setColor(1f, 1f, 1f, 0.5f); // חשוב: עדין!




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

        wreckSprite = new Sprite(wreckTex);
        wreckSprite.setSize(69f,69f);
        wreckSprite.setOriginCenter();

        // ✅ קטן וברור מה-Hero
        final float WRECK_W = 96f;
        final float WRECK_H = 60f;

        //wreck.setSize(WRECK_W, WRECK_H);
        //wreck.setOrigin(WRECK_W / 2f, WRECK_H * 0.4f);



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


        // ========= UI STAGE =========
        uiStage = new Stage(new FitViewport(1080, 1920));
        FileHandle fh = Gdx.files.internal("ui\\uiskin.json");
        Gdx.app.log("ASSET", "uiskin.json exists = " + fh.exists());




        skin = new Skin(Gdx.files.internal("ui\\uiskin.json"));

        // ========= ECONOMY BASE =========
        resourceBank = new EnumMap<>(ResourceType.class);
        craftedInventory = new EnumMap<>(CraftedItem.class);
        resourceDiscovery = new ResourceDiscovery();



// ========= ECONOMY =========
        resourceDiscovery.discover(ResourceType.WOOD); // הספינה החינמית
        marketSystem = new MarketSystem(
            resourceBank,
            craftedInventory,
            resourceDiscovery
        );

// ========= TOP BAR =========
        Table topBar = new Table();
        topBar.setFillParent(true);
        topBar.top().pad(10);

        TextButton marketButton = new TextButton("Market", skin);
        goldLabel = new Label("Gold: 0", skin);

        marketButton.addListener(e -> {
            if (marketButton.isPressed()) {
                marketPanel.refresh();
                marketPanel.setVisible(!marketPanel.isVisible());
            }
            return true;
        });

        topBar.add(marketButton).left().padRight(10);
        topBar.add(goldLabel).left();

        uiStage.addActor(topBar);

// ========= MARKET PANEL =========
        marketPanel = new MarketPanel(marketSystem, skin);
        marketPanel.setVisible(false);
        marketPanel.setSize(860, 1200);
        marketPanel.setPosition(110, 300);

        uiStage.addActor(marketPanel);

// ========= INPUT =========
        InputMultiplexer mux = new InputMultiplexer();
        mux.addProcessor(uiStage);
        Gdx.input.setInputProcessor(mux);
        mux.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 worldPos = camera.unproject(
                    new Vector3(screenX, screenY, 0)
                );
                return handleWreckClick(worldPos.x, worldPos.y);
            }
        });


    }

    private boolean handleWreckClick(float x, float y) {

        for (Wreck wreck : wrecks) {

            if (wreck.hasCrew)
                continue;

            if (visionRange < wreck.definition.requiredVision)
                continue;

            float halfW = 48f;
            float halfH = 30f;

            Vector2 p = wreck.position;

            if (x >= p.x - halfW && x <= p.x + halfW &&
                y >= p.y - halfH && y <= p.y + halfH) {

                onWreckClicked(wreck);
                return true;
            }
        }





        return false;
    }

    private void onWreckClicked(Wreck wreck) {

        int cost = wreck.definition.crewCost;

        if (cost == 0 || marketSystem.spendGold(cost)) {
            wreck.hasCrew = true;

            // גילוי משאבים
            for (ResourceType type : wreck.definition.productionMix.keySet()) {
                resourceDiscovery.discover(type);
            }

            Gdx.app.log("WRECK", "Crew assigned to " + wreck.definition.id);
        }
    }



    @Override
    public void render(float delta) {
        time += delta;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        strokesOffsetX += delta * 12.0f;
        strokesOffsetY += delta * 4.0f;

        for(WaterRipple ripple : ripples){

            ripple.update(delta);
        }


        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // =========================
        // WORLD
        // =========================
        background.draw(batch);

        // =========================
        // MAIN SHIP (Hero)
        // =========================
        strokesSprite.setPosition(
            -(strokesOffsetX % strokesTex.getWidth()),
            -(strokesOffsetY % strokesTex.getHeight())
        );

        strokesSprite.draw(batch);


        for(WaterRipple ripple : ripples){

            ripple.draw(batch,mainShipPos.x ,mainShipPos.y - 50);
        }

        drawShipReflection();
        drawMainShip();



        // =========================
        // WRECK
        // =========================

        for (Wreck wreck : wrecks) {

            // Vision = תנאי תצוגה בלבד
            if (visionRange < wreck.definition.requiredVision)
                continue;

            // צבע לפי יש/אין צוות
            if (!wreck.hasCrew) {
                wreckSprite.setColor(0.5f, 0.5f, 0.5f, 1f);
            } else {
                wreckSprite.setColor(1f, 1f, 1f, 1f);
            }

            // אנימציה קטנה
            float bob = MathUtils.sin(time * 0.8f + wreck.position.x * 0.01f) * 1.3f;
            float rot = MathUtils.sin(time * 0.4f + wreck.position.y * 0.01f) * 0.9f;

            wreckSprite.setCenter(
                wreck.position.x,
                wreck.position.y + bob
            );

            wreckSprite.setRotation(rot);
            wreckSprite.draw(batch);
            wreckSprite.setRotation(0f);
        }



        // =========================
        // CLOUD –– תמיד אחרון
        // =========================
        drawCloud();
        batch.end();

        // =========================
        // UI
        // =========================
        goldLabel.setText("Gold: " + marketSystem.getGold());
        uiStage.act(delta);
        uiStage.draw();

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

    private void drawShipReflection() {

        float bob = MathUtils.sin(time * 1.0f) * 2.8f;

        // מיקום הספינה
        float x = mainShip.getX() + mainShip.getOriginX();
        float y = mainShip.getY() + mainShip.getOriginY()+bob;

        // כמה נמוך ההשתקפות
        float waterLineOffset = 12f;

        // שמירת מצב
        float originalScaleY = mainShip.getScaleY();

        // הופכים אנכית
        mainShip.setScale(
            mainShip.getScaleX(),
            -mainShip.getScaleY() * 0.9f   // סקווש קטן
        );

        // Alpha נמוך מאוד
        mainShip.setColor(1f, 1f, 1f, 0.25f);

        // מציירים מתחת לספינה
        mainShip.setCenter(x, y - waterLineOffset);
        mainShip.draw(batch);

        // מחזירים מצב
        mainShip.setScale(
            mainShip.getScaleX(),
            originalScaleY
        );
        mainShip.setColor(1f, 1f, 1f, 1f);
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
        // עדכון viewport של ה-UI
        uiStage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {


    }
    @Override public void hide() {}

    @Override
    public void dispose() {

        if (batch != null) batch.dispose();

        if (background != null && background.getTexture() != null)
            background.getTexture().dispose();

        if (mainShip != null && mainShip.getTexture() != null)
            mainShip.getTexture().dispose();

        if (cloud != null && cloud.getTexture() != null)
            cloud.getTexture().dispose();

        if (wreckSprite != null && wreckSprite.getTexture() != null)
            wreckSprite.getTexture().dispose();

        if (rippleTexture != null)
            rippleTexture.dispose();

        if (strokesTex != null)
            strokesTex.dispose();

        if (uiStage != null)
            uiStage.dispose();

        if (skin != null)
            skin.dispose();
    }

}


