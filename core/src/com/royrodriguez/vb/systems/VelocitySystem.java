package com.royrodriguez.vb.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.royrodriguez.vb.components.MovementComponent;

public class VelocitySystem extends IteratingSystem {
	
	private ComponentMapper<MovementComponent> movementMapper;

	public VelocitySystem(int priority) {
		super(Family.all(MovementComponent.class).get(), priority);
		
		movementMapper = ComponentMapper.getFor(MovementComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		MovementComponent movementComp = movementMapper.get(entity);

		movementComp.vel.add(movementComp.accel.x * deltaTime, movementComp.accel.y * deltaTime);
	}

}
