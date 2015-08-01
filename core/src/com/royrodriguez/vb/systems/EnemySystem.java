package com.royrodriguez.vb.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.royrodriguez.vb.components.EnemyComponent;
import com.royrodriguez.vb.components.MovementComponent;
import com.royrodriguez.vb.components.TransformComponent;

public class EnemySystem extends IteratingSystem {

	private ComponentMapper<MovementComponent> movementMapper;
	private ComponentMapper<TransformComponent> transformMapper;
	
	private Engine engine;
	
	public EnemySystem(int priorty) {
		super(Family.all(EnemyComponent.class).get(), priorty);
		
		movementMapper = ComponentMapper.getFor(MovementComponent.class);
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		this.engine = engine;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		TransformComponent transformComp = transformMapper.get(entity);
		MovementComponent movementComp = movementMapper.get(entity);
		
		movementComp.vel.x = -3;
		
		if(transformComp.pos.x < 0) engine.removeEntity(entity);
	}

}
