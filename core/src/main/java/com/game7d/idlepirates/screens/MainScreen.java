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
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game7d.idlepirates.core.GameWorld;
import com.game7d.idlepirates.data.CraftedItem;
import com.game7d.idlepirates.data.ResourceType;
import com.game7d.idlepirates.data.WreckCatalog;
import com.game7d.idlepirates.data.WreckDefinition;
import com.game7d.idlepirates.entities.MainShip;
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
    private static final float WORLD_WIDTH  = 4096f;
    private static final float WORLD_HEIGHT = 4096f;
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
    private Sprite mainShipSprite;
    private Vector2 mainShipPos;

    // Wreck – מאוזנת (רחבה)
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

    //world
    private GameWorld gameWorld;
    private MainShip mainShip;
    private ObjectMap<Wreck,Label> wreckCostLabels;
    private GestureDetector gestureDetector;
    private static final float MIN_ZOOM = 0.6f;  // זום פנימה מקסימלי
    private static final float MAX_ZOOM = 1.8f;  // זום החוצה מקסימלי
    private static final float CLOUD_OFFSET_X = 80f;
    private static final float CLOUD_OFFSET_Y = 80f;








    @Override
    public void show() {

        // =========================
        // BATCH + CAMERA + VIEWPORT
        // =========================
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        viewport.apply();

        camera.position.set(WORLD_SIZE / 2f, WORLD_SIZE / 2f, 0f);
        camera.update();

        // =========================
        // MAIN SHIP (LOGIC FIRST)
        // =========================
        mainShipPos = new Vector2(WORLD_SIZE / 2f, WORLD_SIZE / 2f);
        mainShip = new MainShip(mainShipPos);

        // =========================
        // GAME WORLD (LOGIC ONLY)
        // =========================
        gameWorld = new GameWorld(mainShip);

        // =========================
        // BACKGROUND
        // =========================
        Texture bgTex = new Texture(Gdx.files.internal("background.png"));
        bgTex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bgTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        background = new Sprite(bgTex);
        background.setSize(WORLD_SIZE, WORLD_SIZE);
        background.setPosition(0, 0);

        // =========================
        // WATER EFFECTS
        // =========================
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
        strokesSprite.setColor(1f, 1f, 1f, 0.5f);

        // =========================
        // MAIN SHIP SPRITE (VISUAL)
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

        mainShipSprite = new Sprite(mainTex);
        mainShipSprite.setSize(180f, 260f);
        mainShipSprite.setOrigin(
            mainShipSprite.getWidth() / 2f,
            mainShipSprite.getHeight() * 0.35f
        );

        // =========================
        // WRECK SPRITE (SHARED)
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
        wreckSprite.setSize(96f, 60f);
        wreckSprite.setOriginCenter();

        // =========================
        // CLOUD / FOG OF WAR
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

        // =========================
        // UI STAGE + SKIN
        // =========================
        uiStage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // =========================
        // ECONOMY SYSTEMS
        // =========================
        resourceBank = new EnumMap<>(ResourceType.class);
        craftedInventory = new EnumMap<>(CraftedItem.class);
        resourceDiscovery = new ResourceDiscovery();

        resourceDiscovery.discover(ResourceType.WOOD);

        marketSystem = new MarketSystem(
            resourceBank,
            craftedInventory,
            resourceDiscovery
        );

        // =========================
        // TOP BAR
        // =========================
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

        // =========================
        // MARKET PANEL
        // =========================
        marketPanel = new MarketPanel(marketSystem, skin);
        marketPanel.setVisible(false);
        marketPanel.setSize(860, 1200);
        marketPanel.setPosition(110, 300);
        uiStage.addActor(marketPanel);

        // =========================
        // WRECK COST LABELS (UI)
        // =========================
        wreckCostLabels = new ObjectMap<>();

        Label.LabelStyle labelStyle =
            new Label.LabelStyle(skin.getFont("default"), Color.WHITE);

        for (Wreck wreck : gameWorld.wrecks) {
            Label label = new Label("", labelStyle);
            label.setAlignment(Align.center);
            label.setVisible(false);
            uiStage.addActor(label);
            wreckCostLabels.put(wreck, label);
        }

        // =========================
        // INPUT
        // =========================


        gestureDetector = new GestureDetector(
            new GestureDetector.GestureAdapter() {

                private float startZoom;
                private final Vector3 pinchWorldPos = new Vector3();

                @Override
                public boolean touchDown(float x, float y, int pointer, int button) {
                    startZoom = camera.zoom;
                    return false;
                }

                // =========================
                // PINCH TO ZOOM
                // =========================
                @Override
                public boolean pinch(
                    Vector2 initialPointer1,
                    Vector2 initialPointer2,
                    Vector2 pointer1,
                    Vector2 pointer2) {

                    float initialDistance = initialPointer1.dst(initialPointer2);
                    float currentDistance = pointer1.dst(pointer2);

                    if (initialDistance == 0) return false;

                    float ratio = initialDistance / currentDistance;
                    float newZoom = MathUtils.clamp(
                        startZoom * ratio,
                        MIN_ZOOM,
                        MAX_ZOOM
                    );

                    // =========================
                    // 2️⃣ נקודת הצביטה במסך (אמצע שתי האצבעות)
                    // =========================
                    float midX = (pointer1.x + pointer2.x) * 0.5f;
                    float midY = (pointer1.y + pointer2.y) * 0.5f;

                    // =========================
                    // 3️⃣ Screen → World (עם viewport!)
                    // =========================
                    pinchWorldPos.set(midX, midY, 0);
                    viewport.unproject(pinchWorldPos);

                    // =========================
                    // 4️⃣ פיצוי מיקום המצלמה
                    // =========================
                    float prevZoom = camera.zoom;
                    camera.zoom = newZoom;

                    float zoomFactor = newZoom / prevZoom;

                    camera.position.x +=
                        (pinchWorldPos.x - camera.position.x) * (1 - zoomFactor);
                    camera.position.y +=
                        (pinchWorldPos.y - camera.position.y) * (1 - zoomFactor);

                    clampCamera();   // אם יש לך גבולות עולם
                    camera.update();

                    return true;

                }

                // =========================
                // PAN (DRAG)
                // =========================
                @Override
                public boolean pan(float x, float y, float deltaX, float deltaY) {

                    // deltaX / deltaY הם במסך – מתאימים לזום
                    float panSpeed = camera.zoom;

                    camera.position.x -= deltaX * panSpeed;
                    camera.position.y += deltaY * panSpeed;

                    clampCamera();
                    camera.update();
                    return true;
                }
            }
        );



        InputMultiplexer mux = new InputMultiplexer();
        mux.addProcessor(uiStage);
        mux.addProcessor(gestureDetector);
        mux.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 worldPos = new Vector3(screenX, screenY, 0);
                viewport.unproject(worldPos);
                return handleWreckClick(worldPos.x, worldPos.y);
            }
            @Override
            public boolean scrolled(float amountX, float amountY) {
                camera.zoom += amountY * 0.1f;
                camera.zoom = MathUtils.clamp(camera.zoom, MIN_ZOOM, MAX_ZOOM);
                camera.update();
                return true;
            }
        });

        Gdx.input.setInputProcessor(mux);



    }

    private void clampCamera() {

        float halfViewWidth =
            (viewport.getWorldWidth() * camera.zoom) / 2f;
        float halfViewHeight =
            (viewport.getWorldHeight() * camera.zoom) / 2f;

        camera.position.x = MathUtils.clamp(
            camera.position.x,
            halfViewWidth,
            WORLD_WIDTH - halfViewWidth
        );

        camera.position.y = MathUtils.clamp(
            camera.position.y,
            halfViewHeight,
            WORLD_HEIGHT - halfViewHeight
        );
    }





    private boolean handleWreckClick(float worldX, float worldY) {

        for (Wreck wreck : gameWorld.wrecks) {

            // לא ניתן לקנות שוב
            if (wreck.hasCrew)
                continue;

            // לא נראה => לא לחיץ
            if (visionRange < wreck.definition.requiredVision)
                continue;

            // בדיקת פגיעה פשוטה (Bounding Box)
            float halfW = wreckSprite.getWidth() / 2f;
            float halfH = wreckSprite.getHeight() / 2f;

            if (worldX >= wreck.position.x - halfW &&
                worldX <= wreck.position.x + halfW &&
                worldY >= wreck.position.y - halfH &&
                worldY <= wreck.position.y + halfH) {

                attemptPurchase(wreck);
                return true;
            }
        }

        return false;
    }

    private void attemptPurchase(Wreck wreck) {

        int cost = wreck.definition.crewCost;

        // בדיקה שיש כסף
        if (!marketSystem.hasGold(cost)) {
            // כאן אפשר בעתיד להציג "Not enough gold"
            Gdx.app.log("PURCHASE", "Not enough gold");
            return;
        }

        // תשלום
        marketSystem.spendGold(cost);

        // הרכישה עצמה – עולם משנה state
        gameWorld.assignCrew(wreck);
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

        // =========================
        // UPDATE
        // =========================
        time += delta;

        // לוגיקה של העולם
        gameWorld.update(delta);

        // תזוזת מים
        strokesOffsetX += delta * 12.0f;
        strokesOffsetY += delta * 4.0f;

        for (WaterRipple ripple : ripples) {
            ripple.update(delta);
        }

        // =========================
        // CLEAR
        // =========================
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply(false);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // =========================
        // DRAW WORLD
        // =========================
        batch.begin();

        // --- Background ---
        background.draw(batch);

        // --- Water strokes overlay ---
        strokesSprite.setPosition(
            -(strokesOffsetX % strokesTex.getWidth()),
            -(strokesOffsetY % strokesTex.getHeight())
        );
        strokesSprite.draw(batch);

        // --- Ripples סביב הספינה הראשית ---
        for (WaterRipple ripple : ripples) {
            ripple.draw(batch,
                mainShip.position.x,
                mainShip.position.y - 50f
            );
        }

        // --- Reflection + Main ship ---
        drawShipReflection();
        drawMainShip();

        // =========================
        // DRAW WRECKS
        // =========================
        for (Wreck wreck : gameWorld.wrecks) {

            // Vision = סינון תצוגה בלבד
            if (visionRange < wreck.definition.requiredVision)
                continue;

            // צבע לפי האם נשלח צוות
            if (wreck.hasCrew) {
                wreckSprite.setColor(1f, 1f, 1f, 1f);
            } else {
                wreckSprite.setColor(0.5f, 0.5f, 0.5f, 1f);
            }

            float bob =
                MathUtils.sin(time * 0.8f + wreck.position.x * 0.01f) * 1.3f;
            float rot =
                MathUtils.sin(time * 0.4f + wreck.position.y * 0.01f) * 0.9f;

            wreckSprite.setCenter(
                wreck.position.x,
                wreck.position.y + bob
            );
            wreckSprite.setRotation(rot);

            wreckSprite.draw(batch);
            wreckSprite.setRotation(0f);
        }

        // --- Cloud תמיד אחרון בעולם ---
        drawCloud();

        batch.end();

        // =========================
        // UI / TEXT
        // =========================

        // Gold
        goldLabel.setText("Gold: " + marketSystem.getGold());

        // טקסט מחיר מעל ספינות טרופות
        for (Wreck wreck : gameWorld.wrecks) {

            Label label = wreckCostLabels.get(wreck);

            if (label == null) continue;

            if (visionRange < wreck.definition.requiredVision || wreck.hasCrew) {
                label.setVisible(false);
                continue;
            }

            int cost = wreck.definition.crewCost;

            if (cost == 0) {
                label.setText("FREE");
                label.setColor(Color.GOLD);
            } else {
                label.setText("Crew: " + cost);
                label.setColor(Color.WHITE);
            }

            Vector3 screenPos = camera.project(
                new Vector3(
                    wreck.position.x,
                    wreck.position.y + 60f,
                    0
                )
            );

            label.setPosition(
                screenPos.x - label.getWidth() / 2f,
                screenPos.y
            );

            label.setVisible(true);
        }

        // Stage
        uiStage.act(delta);
        uiStage.draw();
    }




    private void drawMainShip() {
        float bob = MathUtils.sin(time * 1.0f) * 2.8f;
        float rotation = MathUtils.sin(time * 0.6f) * 2.0f;
        float pulse = 1f + MathUtils.sin(time * 0.8f) * 0.015f;

        float x = mainShipPos.x - mainShipSprite.getWidth() / 2f;
        float y = mainShipPos.y - mainShipSprite.getHeight() / 2f;

        mainShipSprite.setPosition(x, y + bob);
        mainShipSprite.setRotation(rotation);
        mainShipSprite.setScale(pulse);

        mainShipSprite.draw(batch);

        mainShipSprite.setRotation(0f);
        mainShipSprite.setScale(1f);
        mainShipSprite.setPosition(x, y);
    }

    private void drawShipReflection() {

        float bob = MathUtils.sin(time * 1.0f) * 2.8f;

        // מיקום הספינה
        float x = mainShipSprite.getX() + mainShipSprite.getOriginX();
        float y = mainShipSprite.getY() + mainShipSprite.getOriginY()+bob;

        // כמה נמוך ההשתקפות
        float waterLineOffset = 12f;

        // שמירת מצב
        float originalScaleY = mainShipSprite.getScaleY();

        // הופכים אנכית
        mainShipSprite.setScale(
            mainShipSprite.getScaleX(),
            -mainShipSprite.getScaleY() * 0.9f   // סקווש קטן
        );

        // Alpha נמוך מאוד
        mainShipSprite.setColor(1f, 1f, 1f, 0.25f);

        // מציירים מתחת לספינה
        mainShipSprite.setCenter(x, y - waterLineOffset);
        mainShipSprite.draw(batch);

        // מחזירים מצב
        mainShipSprite.setScale(
            mainShipSprite.getScaleX(),
            originalScaleY
        );
        mainShipSprite.setColor(1f, 1f, 1f, 1f);
    }


    private void drawCloud() {

        float baseScale = visionRange / BASE_VISION;

        // אנימציות עדינות
        float pulse = MathUtils.sin(time * 0.35f) * 0.015f;
        float rotation = MathUtils.sin(time * 0.10f) * 0.4f;

        // אליפסה אנכית (מותאם למובייל)
        float scaleX = baseScale * 0.9f * (1f + pulse);
        float scaleY = baseScale * 1.3f * (1f + pulse);

        cloud.setOriginCenter();
        cloud.setScale(scaleX, scaleY);
        cloud.setRotation(rotation);

        cloud.setCenter(
            mainShipSprite.getX() + CLOUD_OFFSET_X,
            mainShipSprite.getY()+ CLOUD_OFFSET_Y
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

        if (mainShipSprite != null && mainShipSprite.getTexture() != null)
            mainShipSprite.getTexture().dispose();

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


