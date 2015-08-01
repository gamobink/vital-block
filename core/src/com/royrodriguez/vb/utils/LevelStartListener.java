package com.royrodriguez.vb.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.royrodriguez.vb.screens.LevelScreen;

public class LevelStartListener extends ChangeListener {
	
	private LevelScreen levelScreen;
	private int level;
	
	public LevelStartListener(LevelScreen levelScreen, int level) {
		this.levelScreen = levelScreen;
		this.level = level;
	}

	@Override
	public void changed(ChangeEvent event, Actor actor) {
		levelScreen.launchLevel(level);
	}

}
