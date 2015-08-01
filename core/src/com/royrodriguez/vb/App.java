package com.royrodriguez.vb;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.royrodriguez.vb.components.MapCollisionComponent.CellType;
import com.royrodriguez.vb.screens.GameScreen;
import com.royrodriguez.vb.screens.LevelScreen;
import com.royrodriguez.vb.utils.Background;

public class App extends Game {

	public SpriteBatch batch;
	
	public GameScreen gameScreen;
	public LevelScreen levelScreen;
	
	@Override
	public void create() {
		batch = new SpriteBatch();

		Assets.load();

		gameScreen = new GameScreen(this);
		levelScreen = new LevelScreen(this);

		setScreen(levelScreen);
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		Assets.dispose();
		Background.getInstance().dispose();
		gameScreen.dispose();
		levelScreen.dispose();
	}


}

