package com.royrodriguez.vb.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.royrodriguez.vb.Assets;
import com.royrodriguez.vb.components.BoundsComponent;
import com.royrodriguez.vb.components.TextureComponent;
import com.royrodriguez.vb.components.TransformComponent;
import com.royrodriguez.vb.world.World;
import com.sun.glass.ui.View;

public class RenderingSystem extends IteratingSystem {
	
	public static float PIXELS_TO_WORD = 1.0f / 16.0f;
	
	private Array<Entity> renderQueue;

	private SpriteBatch batch;
	
	private ComponentMapper<TextureComponent> textureMapper;		
	private ComponentMapper<TransformComponent> transformMapper;		
	private ComponentMapper<BoundsComponent> boundsMapper;

	private OrthographicCamera camera;
	private Viewport viewport;
	
	public RenderingSystem(SpriteBatch batch, int priority) {
		super(Family.all(TextureComponent.class, TransformComponent.class).get(), priority);
		
		textureMapper = ComponentMapper.getFor(TextureComponent.class);
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
		boundsMapper = ComponentMapper.getFor(BoundsComponent.class);

		renderQueue = new Array<Entity>();
		this.batch = batch;
		

		camera = new OrthographicCamera();
		float  s = 2f;
		camera.setToOrtho(false, 16 * s, 9 * s);
		viewport = new FitViewport(camera.viewportWidth, camera.viewportHeight, camera);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		batch.begin();
		for(Entity e : renderQueue) {
			TextureComponent textureComp = textureMapper.get(e);
			TransformComponent transformComp = transformMapper.get(e);
			
			if(!textureComp.flipY) {

				if(!textureComp.flipX) {
					batch.draw(textureComp.region,
                        transformComp.pos.x, 
                        transformComp.pos.y,
                        0,
                        0,
                        textureComp.region.getRegionWidth() * PIXELS_TO_WORD,
                        textureComp.region.getRegionHeight() * PIXELS_TO_WORD,
                        transformComp.scale.x,
                        transformComp.scale.y,
                        transformComp.angle
					);
					
				} else {
				    batch.draw(textureComp.region,
                        transformComp.pos.x + textureComp.region.getRegionWidth() * PIXELS_TO_WORD, 
                        transformComp.pos.y,
                        0,
                        0,
                        -textureComp.region.getRegionWidth() * PIXELS_TO_WORD,
                        textureComp.region.getRegionHeight() * PIXELS_TO_WORD,
                        transformComp.scale.x,
                        transformComp.scale.y,
                        transformComp.angle
	                );
				}
            
			} else {
				if(!textureComp.flipX) {
					batch.draw(textureComp.region,
		                    transformComp.pos.x, 
		                    transformComp.pos.y + boundsMapper.get(e).height,
		                    0,
		                    0,
		                    textureComp.region.getRegionWidth() * PIXELS_TO_WORD,
		                    -textureComp.region.getRegionHeight() * PIXELS_TO_WORD,
		                    transformComp.scale.x,
		                    transformComp.scale.y,
		                    transformComp.angle
					);
					
				} else {
					batch.draw(textureComp.region,
		                    transformComp.pos.x + boundsMapper.get(e).width,
		                    transformComp.pos.y + boundsMapper.get(e).height,
		                    0,
		                    0,
		                    -textureComp.region.getRegionWidth() * PIXELS_TO_WORD,
		                    -textureComp.region.getRegionHeight() * PIXELS_TO_WORD,
		                    transformComp.scale.x,
		                    transformComp.scale.y,
		                    transformComp.angle
					);
					
				}
				
			}

		}

		batch.end();
		
		renderQueue.clear();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		renderQueue.add(entity);
	}
	
	public void updateViewport(int width, int height) {
		viewport.update(width, height);
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}
	
	public Viewport getViewport() {
		return viewport;
	}
	
}
