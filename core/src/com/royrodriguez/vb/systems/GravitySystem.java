package com.royrodriguez.vb.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.royrodriguez.vb.components.GravityComponent;
import com.royrodriguez.vb.components.MovementComponent;

public class GravitySystem extends IteratingSystem {

	private ComponentMapper<MovementComponent> movementMapper;
	
	private float accel;
	
	public GravitySystem(int priority) {
		super(Family.all(GravityComponent.class).get());
		
		movementMapper = ComponentMapper.getFor(MovementComponent.class);
		accel = -24f;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		MovementComponent movementComp = movementMapper.get(entity);
		
		movementComp.accel.y = accel;
	}
	
	public void invert() {
		accel *= -1;
	}
	
	public void setNormal() {
		accel = -24f;
	}

}
