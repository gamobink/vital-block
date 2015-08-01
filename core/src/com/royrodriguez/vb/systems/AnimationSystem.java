package com.royrodriguez.vb.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.royrodriguez.vb.components.AnimationComponent;
import com.royrodriguez.vb.components.StateComponent;
import com.royrodriguez.vb.components.TextureComponent;

public class AnimationSystem extends IteratingSystem {
	
	private ComponentMapper<TextureComponent> textureMapper;
	private ComponentMapper<StateComponent> stateMapper;
	private ComponentMapper<AnimationComponent> animationMapper;
	
	public AnimationSystem(int priority) {
		super(Family.all(AnimationComponent.class, StateComponent.class).get(), priority);
		
		textureMapper = ComponentMapper.getFor(TextureComponent.class);
		stateMapper = ComponentMapper.getFor(StateComponent.class);
		animationMapper = ComponentMapper.getFor(AnimationComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		AnimationComponent animationComp = animationMapper.get(entity);
		TextureComponent textureComp = textureMapper.get(entity);
		StateComponent stateComp = stateMapper.get(entity);
		
		Animation animation = animationComp.animations.get(stateComp.currentState);
		
		stateComp.time += deltaTime;

		textureComp.region = animation.getKeyFrame(stateComp.time, true);
	}
	
}
