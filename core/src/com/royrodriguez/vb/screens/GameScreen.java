package com.royrodriguez.vb.screens;

import com.apple.eawt.event.GestureListener;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.FPSLogger;
import com.royrodriguez.vb.App;
import com.royrodriguez.vb.Assets;
import com.royrodriguez.vb.components.GravityComponent;
import com.royrodriguez.vb.components.MapCollisionComponent;
import com.royrodriguez.vb.components.MovementComponent;
import com.royrodriguez.vb.components.PlayerComponent;
import com.royrodriguez.vb.components.TextureComponent;
import com.royrodriguez.vb.components.TransformComponent;
import com.royrodriguez.vb.systems.AnimationSystem;
import com.royrodriguez.vb.systems.CameraSystem;
import com.royrodriguez.vb.systems.CollisionSystem;
import com.royrodriguez.vb.systems.EnemySystem;
import com.royrodriguez.vb.systems.GravitySystem;
import com.royrodriguez.vb.systems.MapCollisionSystem;
import com.royrodriguez.vb.systems.MovementSystem;
import com.royrodriguez.vb.systems.PlayerSystem;
import com.royrodriguez.vb.systems.RenderingSystem;
import com.royrodriguez.vb.systems.VelocitySystem;
import com.royrodriguez.vb.utils.PlayerInput;
import com.royrodriguez.vb.world.World;

public class GameScreen extends ScreenAdapter {
	
	private App app;

	private enum State {PLAY};
	private State currentState;
	
	private Engine engine;
	
	private World worldTest;
	
	private FPSLogger fpsLogger;
	
	public GameScreen(App app) {
		this.app = app;

		engine = new Engine();

		currentState = State.PLAY;

		createSystems();
		
		worldTest = new World(engine, app.batch);

		fpsLogger = new FPSLogger();
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(new PlayerInput(engine.getSystem(PlayerSystem.class), worldTest));
	}
	
	private void createSystems() {
		int i = 0;
		
		i++;
		engine.addSystem(new PlayerSystem(i));

		i++;
		engine.addSystem(new GravitySystem(i));

		i++;
		engine.addSystem(new VelocitySystem(i));
		
		i++;
		engine.addSystem(new CollisionSystem(i, app));

		i++;
		engine.addSystem(new MapCollisionSystem(i));

		i++;
		engine.addSystem(new MovementSystem(i));

		i++;
		engine.addSystem(new CameraSystem(i));

		i++;
		engine.addSystem(new AnimationSystem(i));

		i++;
		engine.addSystem(new RenderingSystem(app.batch, i));
	}
	
	@Override
	public void render(float delta) {
		switch (currentState) {
		case PLAY:
			updatePlay(delta);
			break;

		default:
			break;
		}
	}
	
	private void updatePlay(float delta) {
		if(Gdx.input.isKeyJustPressed(Keys.R)) {
			worldTest.reset();
		}

		app.batch.setProjectionMatrix(engine.getSystem(RenderingSystem.class).getCamera().combined);

		worldTest.render(app.batch);
		engine.update(delta);
		
		fpsLogger.log();
	}
	
	@Override
	public void resize(int width, int height) {
		engine.getSystem(RenderingSystem.class).updateViewport(width, height);
	}
	
	@Override
	public void dispose() {
		worldTest.dispose();
	}
	
	public void setMap(TiledMap map) {
		worldTest.setMap(map);
	}

}
