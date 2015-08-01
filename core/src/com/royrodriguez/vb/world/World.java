package com.royrodriguez.vb.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.royrodriguez.vb.Assets;
import com.royrodriguez.vb.components.AnimationComponent;
import com.royrodriguez.vb.components.BoundsComponent;
import com.royrodriguez.vb.components.CameraComponent;
import com.royrodriguez.vb.components.CoinComponent;
import com.royrodriguez.vb.components.GravityComponent;
import com.royrodriguez.vb.components.MapCollisionComponent;
import com.royrodriguez.vb.components.MovementComponent;
import com.royrodriguez.vb.components.PlayerComponent;
import com.royrodriguez.vb.components.StateComponent;
import com.royrodriguez.vb.components.TargetComponent;
import com.royrodriguez.vb.components.TextureComponent;
import com.royrodriguez.vb.components.TransformComponent;
import com.royrodriguez.vb.systems.CameraSystem;
import com.royrodriguez.vb.systems.GravitySystem;
import com.royrodriguez.vb.systems.MapCollisionSystem;
import com.royrodriguez.vb.systems.RenderingSystem;
import com.royrodriguez.vb.utils.Background;

public class World implements Disposable {
	
	private Engine engine;
	
	private OrthogonalTiledMapRenderer mapRenderer;

	private TiledMap map;
	
	private BitmapFont font;
	
	private long maxTime = 1000 * 16;
	private long startTime = 0;
	
	private OrthographicCamera camera;
	
	private Texture bg;
	
	private Entity cameraEntity;
	private Entity playerEntity;
	
	public World(Engine engine, SpriteBatch batch) {
		this.engine = engine;
		//map = new TmxMapLoader().load("map/level1_1.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1.0f / 16.0f);

		//createEntitys();
		
		// kjklasj
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/slkscr.ttf"));
		
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();

		parameter.size = 45;
		parameter.magFilter = TextureFilter.Nearest;
		parameter.minFilter = TextureFilter.Nearest;
		
		font = generator.generateFont(parameter);
		generator.dispose();
		
		startTime = TimeUtils.millis();
		
		camera = new OrthographicCamera();
		int s = 40;
		camera.setToOrtho(false, 9 * s, 16 * s);
		
		bg = Background.getInstance().getBack(Color.BLUE, Color.PINK);
	}
	
	public void createEntitys() {
		engine.removeAllEntities();

		playerEntity = createPlayer();

		cameraEntity = createCamera();
		
		MapLayer objectsLayer = map.getLayers().get("objects");

		// Coins
		if(objectsLayer == null) return;
		for(MapObject o : objectsLayer.getObjects()) {
			float x = Float.parseFloat(o.getProperties().get("x").toString()) * RenderingSystem.PIXELS_TO_WORD ;//* RenderingSystem.PIXELS_TO_WORD;
			float y = (Float.parseFloat(o.getProperties().get("y").toString())) * RenderingSystem.PIXELS_TO_WORD;

			if(o.getName().equals("coin"))
				createCoin(x, y);

			if(o.getName().equals("target"))
				createTarget(x, y);
		}
		
	}
	
	private Entity createPlayer() {
		Entity entity = new Entity();
		
		PlayerComponent playerComp = new PlayerComponent();
		playerComp.top = true;

		TextureComponent textureComp = new TextureComponent();
		textureComp.region = Assets.playerWalkAnimation.getKeyFrame(0);

		TransformComponent transformComp = new TransformComponent();
		transformComp.pos.set(1, 10);
		
		MovementComponent movementComp = new MovementComponent();
		
		GravityComponent gravityComp = new GravityComponent();
		
		BoundsComponent boundsComp = new BoundsComponent();
		boundsComp.width = textureComp.region.getRegionWidth() * RenderingSystem.PIXELS_TO_WORD;
		boundsComp.height = textureComp.region.getRegionHeight() * RenderingSystem.PIXELS_TO_WORD;
		
		MapCollisionComponent mapCollisionComp = new MapCollisionComponent();
		
		AnimationComponent animationComp = new AnimationComponent();
		animationComp.animations.put(PlayerComponent.IDLE_STATE, Assets.playerIdleAnimation);
		animationComp.animations.put(PlayerComponent.WALK_STATE, Assets.playerWalkAnimation);
		animationComp.animations.put(PlayerComponent.JUMP_STATE, Assets.playerJumpAnimation);
		
		StateComponent stateComp = new StateComponent();
		stateComp.currentState = PlayerComponent.IDLE_STATE;
		
		entity.add(playerComp);
		entity.add(textureComp);
		entity.add(transformComp);
		entity.add(movementComp);
		entity.add(gravityComp);
		entity.add(boundsComp);
		entity.add(mapCollisionComp);
		entity.add(animationComp);
		entity.add(stateComp);
		
		engine.addEntity(entity);

		return entity;
	}
	
	private Entity createCamera() {
		Entity entity = new Entity();
		
		CameraComponent cameraComp = new CameraComponent();
		cameraComp.camera = engine.getSystem(RenderingSystem.class).getCamera();
		cameraComp.target = getPlayer();
		
		entity.add(cameraComp);
		
		engine.addEntity(entity);;
		
		return entity;
	}
	
	private void createCoin(float x, float y) {
		Entity entity = new Entity();
		
		CoinComponent coinComp = new CoinComponent();
		
		BoundsComponent bounds = new BoundsComponent();
		bounds.width = Assets.coin.getRegionWidth() * RenderingSystem.PIXELS_TO_WORD;
		bounds.height = Assets.coin.getRegionHeight() * RenderingSystem.PIXELS_TO_WORD;
		
		TransformComponent transformComp = new TransformComponent();
		transformComp.pos.set(x - bounds.width / 2.0f, y - bounds.height / 2.0f);
		
		TextureComponent textureComp = new TextureComponent();
		textureComp.region = Assets.coin;
		
		
		entity.add(coinComp);
		entity.add(bounds);
		entity.add(transformComp);
		entity.add(textureComp);

		engine.addEntity(entity);
	}
	
	public void render(SpriteBatch batch) {
		if(map != null) {
			mapRenderer.setView(engine.getSystem(RenderingSystem.class).getCamera());
			mapRenderer.render();
		}
	}
	
	public void reset() {
		if(map == null)
			return;

		createEntitys();
		engine.getSystem(GravitySystem.class).setNormal();
		engine.getSystem(CameraSystem.class).resetCamera(cameraEntity);
	}
	
	public void createTarget(float x, float y) {
		Entity entity = new Entity();

		TargetComponent targetComp = new TargetComponent();
		
		BoundsComponent boundsComp = new BoundsComponent();
		boundsComp.width = Assets.target.getRegionWidth() * RenderingSystem.PIXELS_TO_WORD;
		boundsComp.height = Assets.target.getRegionHeight() * RenderingSystem.PIXELS_TO_WORD;
		
		TransformComponent transformComp = new TransformComponent();
		transformComp.pos.set(x - boundsComp.width / 2.0f, y - boundsComp.height / 2.0f);
		
		TextureComponent textureComp = new TextureComponent();
		textureComp.region = Assets.target;
		
		entity.add(targetComp);
		entity.add(boundsComp);
		entity.add(transformComp);
		entity.add(textureComp);
		
		engine.addEntity(entity);
	}
	
	
	public void setMap(TiledMap map) {
		this.map = map;
		mapRenderer.setMap(map);
		engine.getSystem(MapCollisionSystem.class).setMap(map);
		
		reset();
	}
	
	public Entity getPlayer() {
		return playerEntity;
	}

	@Override
	public void dispose() {
		mapRenderer.dispose();
		bg.dispose();
	}
	
}
