package com.game7d.idlepirates;

import com.badlogic.gdx.Game;
import com.game7d.idlepirates.screens.MainScreen;

public class IdlePirates extends Game {

    @Override
    public void create() {
        // פה מחליטים איזה מסך עולה ראשון
        setScreen(new MainScreen());
    }

    @Override
    public void render() {
        super.render(); // חשוב מאוד
    }


    @Override
    public void dispose() {
        if (getScreen() != null) {
            getScreen().dispose();
        }


    }
}


