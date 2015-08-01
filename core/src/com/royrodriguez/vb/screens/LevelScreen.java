package com.royrodriguez.vb.screens;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.omg.CORBA.OctetSeqHolder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.royrodriguez.vb.App;
import com.royrodriguez.vb.Assets;
import com.royrodriguez.vb.systems.RenderingSystem;
import com.royrodriguez.vb.utils.LevelStartListener;

import javafx.scene.Scene;
import javafx.scene.control.Tab;

public class LevelScreen extends ScreenAdapter {
	
	private App app;
	
	private HashMap<Integer, TiledMap> maps;
	
	private Stage stage;

	private Table table;
	
	private OrthographicCamera camera;
	private Viewport viewport;
	
	public LevelScreen(App app) {
		this.app = app;
		
		maps = new HashMap<Integer, TiledMap>();
		
		camera = new OrthographicCamera();
		int s = 5;
		camera.setToOrtho(false, 16 * s, 9 * s);

		viewport = new FitViewport(camera.viewportWidth, camera.viewportHeight);
		
		stage = new Stage(viewport);

		table = new Table();
		table.setFillParent(true);
		
		stage.addActor(table);
		
		createButtons();
	}
	
	@Override
	public void render(float delta) {
		if(Gdx.input.isKeyJustPressed(Keys.A))
			launchLevel(0);

		if(Gdx.input.isKeyJustPressed(Keys.B))
			launchLevel(1);

		stage.act(delta);
		
		stage.draw();
	}
	
	public void launchLevel(int level) {
		app.setScreen(app.gameScreen);

		if(!maps.containsKey(level)) {
			switch (level) {
			case 0:
				maps.put(level, new TmxMapLoader().load("map/level1_1.tmx"));
				break;
			case 1:
				maps.put(level, new TmxMapLoader().load("map/level1_2.tmx"));
				break;

			default:
				break;
			}
		}
		
		app.gameScreen.setMap(maps.get(level));
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	
	@Override
	public void dispose() {
		Collection<TiledMap> m = maps.values();

		Iterator<TiledMap> i = m.iterator();
		while(i.hasNext()) {
			i.next().dispose();
		}

	}
	
	private void createButtons() {
		ImageButtonStyle style = new ImageButtonStyle();
		style.up = new TextureRegionDrawable(Assets.buttonUp);
		style.down = new TextureRegionDrawable(Assets.buttonDown);

		for(int i = 0; i < 2; i++) {
            Button button = new ImageButton(style);
            button.addListener(new LevelStartListener(this, i));

            table.add(button).space(1);
		}

		
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

}
