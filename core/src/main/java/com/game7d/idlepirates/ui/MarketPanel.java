package com.game7d.idlepirates.ui;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.game7d.idlepirates.market.MarketSystem;
import com.game7d.idlepirates.data.ResourceType;
import com.game7d.idlepirates.data.CraftedItem;

import java.util.List;

public class MarketPanel extends Table {

    private final MarketSystem market;
    private final Skin skin;

    private final Label goldLabel;
    private final Table contentTable;

    public MarketPanel(MarketSystem market, Skin skin) {
        this.market = market;
        this.skin = skin;

        setFillParent(true);
        setBackground(skin.newDrawable("white", 0, 0, 0, 0.85f));
        pad(20);

        // ===== Header =====
        Label title = new Label("Market", skin);
        goldLabel = new Label("", skin);

        Table header = new Table();
        header.add(title).left().expandX();
        header.add(goldLabel).right();

        add(header).growX().row();

        row();
        add(new Label("Sell resources for Gold", skin)).left().padBottom(10);
        row();

        // ===== Content =====
        contentTable = new Table();
        add(contentTable).grow().top();

        refresh();
    }

    /** רענון מלא של התוכן */
    public void refresh() {
        contentTable.clear();
        updateGold();

        // --- Raw Resources ---
        List<ResourceType> rawResources = market.getVisibleRawResources();
        if (!rawResources.isEmpty()) {
            contentTable.add(new Label("Raw Resources", skin)).left().padTop(10);
            contentTable.row();

            for (ResourceType type : rawResources) {
                contentTable.add(new MarketRow.Raw(type, market, skin)).growX().row();
            }
        }

        // --- Crafted Items ---
        List<CraftedItem> crafted = market.getVisibleCraftedItems();
        if (!crafted.isEmpty()) {
            contentTable.add(new Label("Processed Goods", skin)).left().padTop(15);
            contentTable.row();

            for (CraftedItem item : crafted) {
                contentTable.add(new MarketRow.Crafted(item, market, skin)).growX().row();
            }
        }
    }

    private void updateGold() {
        goldLabel.setText("Gold: " + market.getGold());
    }
}


