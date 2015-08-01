package com.royrodriguez.vb.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.royrodriguez.vb.components.MovementComponent;
import com.royrodriguez.vb.components.TransformComponent;

public class MovementSystem extends IteratingSystem {
	
	private ComponentMapper<TransformComponent> transformMapper;
	private ComponentMapper<MovementComponent> movementMapper;
	
	public MovementSystem(int priority) {
		super(Family.all(MovementComponent.class).get(), priority);
		
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
		movementMapper = ComponentMapper.getFor(MovementComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		TransformComponent transformComp = transformMapper.get(entity);
		MovementComponent movementComp = movementMapper.get(entity);
		
		transformComp.pos.add(movementComp.vel.x * deltaTime, movementComp.vel.y * deltaTime);
	}

}
