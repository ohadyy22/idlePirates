package com.game7d.idlepirates.ui;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.game7d.idlepirates.market.MarketSystem;
import com.game7d.idlepirates.market.MarketPrices;
import com.game7d.idlepirates.data.ResourceType;
import com.game7d.idlepirates.data.CraftedItem;

public abstract class MarketRow extends Table {

    protected final MarketSystem market;
    protected final Skin skin;

    protected MarketRow(MarketSystem market, Skin skin) {
        this.market = market;
        this.skin = skin;

        pad(6);
        defaults().pad(4);
        setBackground(skin.newDrawable("white", 0.15f, 0.15f, 0.15f, 1f));
    }

    // =========================
    // RAW RESOURCE ROW
    // =========================
    public static class Raw extends MarketRow {

        public Raw(ResourceType type, MarketSystem market, Skin skin) {
            super(market, skin);

            int price = MarketPrices.getRawPrice(type);

            Label name = new Label(type.name(), skin);
            Label priceLabel = new Label(price + " G", skin);

            TextButton sell10 = new TextButton("Sell 10", skin);
            TextButton sellAll = new TextButton("Sell All", skin);

            sell10.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    market.sellRaw(type, 10);
                }
            });

            sellAll.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    market.sellAllRaw(type);
                }
            });

            add(name).left().expandX();
            add(priceLabel).width(80);
            add(sell10);
            add(sellAll);
        }
    }

    // =========================
    // CRAFTED ITEM ROW
    // =========================
    public static class Crafted extends MarketRow {

        public Crafted(CraftedItem item, MarketSystem market, Skin skin) {
            super(market, skin);

            int price = MarketPrices.getCraftedPrice(item);

            Label name = new Label(item.name(), skin);
            Label priceLabel = new Label(price + " G", skin);

            TextButton sellOne = new TextButton("Sell", skin);
            TextButton sellAll = new TextButton("Sell All", skin);

            sellOne.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    market.sellCrafted(item, 1);
                }
            });

            sellAll.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    market.sellAllCrafted(item);
                }
            });

            add(name).left().expandX();
            add(priceLabel).width(100);
            add(sellOne);
            add(sellAll);
        }
    }
}


