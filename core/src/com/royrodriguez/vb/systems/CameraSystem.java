package com.royrodriguez.vb.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.royrodriguez.vb.components.CameraComponent;
import com.royrodriguez.vb.components.TransformComponent;

public class CameraSystem extends IteratingSystem {

	private ComponentMapper<CameraComponent> cameraMapper;
	private ComponentMapper<TransformComponent> transformMapper;
	
	private Engine engine;

	public CameraSystem(int priority) {
		super(Family.all(CameraComponent.class).get(), priority);
		
		cameraMapper = ComponentMapper.getFor(CameraComponent.class);
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		this.engine = engine;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		CameraComponent camera = cameraMapper.get(entity);
		
		if(camera.target == null)
			return;
		
		TransformComponent target = transformMapper.get(camera.target);
		
		if(target == null)
			return;
		
		// Pos.x == NaN
		int mapWidth = engine.getSystem(MapCollisionSystem.class).getMapWidth();
		if(target.pos.x > camera.camera.viewportWidth / 2.0 && 
				target.pos.x < mapWidth - camera.camera.viewportWidth / 2.0f)
			camera.camera.position.x = target.pos.x;

		camera.camera.update();
	}
	
	public void resetCamera(Entity camera) {
		CameraComponent cameraComp = cameraMapper.get(camera);

		cameraComp.camera.position.x = cameraComp.camera.viewportWidth / 2.0f;
		cameraComp.camera.update();
	}

}
