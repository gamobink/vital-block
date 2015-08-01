package com.royrodriguez.vb.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.royrodriguez.vb.App;
import com.royrodriguez.vb.components.BoundsComponent;
import com.royrodriguez.vb.components.CoinComponent;
import com.royrodriguez.vb.components.PlayerComponent;
import com.royrodriguez.vb.components.TargetComponent;
import com.royrodriguez.vb.components.TransformComponent;
import com.royrodriguez.vb.utils.RectPool;

public class CollisionSystem extends EntitySystem {
	
	private ImmutableArray<Entity> players;
	private ImmutableArray<Entity> targets;
	private ImmutableArray<Entity> coins;
	
	private ComponentMapper<TransformComponent> transformMapper;
	private ComponentMapper<BoundsComponent> boundsMapper;
	
	private App app;
	
	private Engine engine;
	
	public CollisionSystem(int priority, App app) {
		super(priority);

		this.app = app;
		
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
		boundsMapper = ComponentMapper.getFor(BoundsComponent.class);
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		
		players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
		targets = engine.getEntitiesFor(Family.all(TargetComponent.class).get());
		coins = engine.getEntitiesFor(Family.all(CoinComponent.class).get());
		
		this.engine = engine;
	}
	
	@Override
	public void update(float deltaTime) {

		for(Entity player : players) {
			Rectangle playerRect = getRect(player);
			
			// PLayer - target
			for(Entity target : targets) {
				Rectangle targetRect = getRect(target);
				
				if(playerRect.overlaps(targetRect)) {
					app.setScreen(app.levelScreen);
				}

				RectPool.getInstance().free(targetRect);
			}
			
			// Player - coin
			for(Entity coin : coins) {
				Rectangle coinRect = getRect(coin);

				if(playerRect.overlaps(coinRect)) {
					engine.getSystem(PlayerSystem.class).getCoin();
					engine.removeEntity(coin);
				}

				RectPool.getInstance().free(coinRect);
				
			}
			
			
			RectPool.getInstance().free(playerRect);
		}
	}
	
	private Rectangle getRect(Entity entity) {
		Rectangle rect = RectPool.getInstance().obtain();

		BoundsComponent bounds = boundsMapper.get(entity);
		TransformComponent transform  = transformMapper.get(entity);
		
		rect.set(transform.pos.x, transform.pos.y, bounds.width, bounds.height);
		
		return rect;
	}
	
}
