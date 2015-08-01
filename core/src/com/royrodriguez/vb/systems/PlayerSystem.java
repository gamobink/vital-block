package com.royrodriguez.vb.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.royrodriguez.vb.Assets;
import com.royrodriguez.vb.components.BoundsComponent;
import com.royrodriguez.vb.components.MapCollisionComponent;
import com.royrodriguez.vb.components.MapCollisionComponent.CellType;
import com.sun.security.auth.module.JndiLoginModule;
import com.royrodriguez.vb.components.MovementComponent;
import com.royrodriguez.vb.components.PlayerComponent;
import com.royrodriguez.vb.components.StateComponent;
import com.royrodriguez.vb.components.TextureComponent;
import com.royrodriguez.vb.components.TransformComponent;

public class PlayerSystem extends IteratingSystem {

	private ComponentMapper<MovementComponent> movementMapper;
	private ComponentMapper<TransformComponent> transformMapper;
	private ComponentMapper<BoundsComponent> boundsMapper;
	private ComponentMapper<PlayerComponent> playerMaper;
	private ComponentMapper<TextureComponent> textureMapper;
	private ComponentMapper<MapCollisionComponent> mapCollisionMapper;
	private ComponentMapper<StateComponent> stateMapper;
	
	private float velY;
	
	private Engine engine;
	
	private float accelX;
	
	private boolean turnLeft;
	private boolean turnRight;
	private boolean dupVel;
	private boolean jump;
	private boolean flip;

	public PlayerSystem(int priority) {
		super(Family.all(PlayerComponent.class).get(), priority);
		
		movementMapper = ComponentMapper.getFor(MovementComponent.class);
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
		boundsMapper = ComponentMapper.getFor(BoundsComponent.class);
		playerMaper = ComponentMapper.getFor(PlayerComponent.class);
		textureMapper = ComponentMapper.getFor(TextureComponent.class);
		mapCollisionMapper = ComponentMapper.getFor(MapCollisionComponent.class);
		stateMapper = ComponentMapper.getFor(StateComponent.class);
		
		velY = PlayerComponent.jumpVel;
		
		accelX = PlayerComponent.accelX;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		this.engine = engine;
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		MovementComponent movementComp = movementMapper.get(entity);
		StateComponent stateComp = stateMapper.get(entity);
		
		CellType bottomCell = mapCollisionMapper.get(entity).bottomCell;

		// Movement
		if(turnRight && !turnLeft) {
			movementMapper.get(entity).accel.x = accelX;
		} else if(turnLeft && !turnRight) {
			movementMapper.get(entity).accel.x = -accelX;
		} else {
			movementMapper.get(entity).accel.x = 0;
		}
		
		if(jump && bottomCell != CellType.NONE) {
			movementMapper.get(entity).vel.y = velY;
			jump = false;
			Assets.jumpSound.play();
		}
		
		
		if(playerMaper.get(entity).top == true && velY < 0) 
			velY = PlayerComponent.jumpVel;
		if(flip && bottomCell == CellType.FLIP) {
			playerMaper.get(entity).top = !playerMaper.get(entity).top;
			
			TransformComponent playerPos = transformMapper.get(entity);
			
			if(playerMaper.get(entity).top) {
				playerPos.pos.y += 2f; //+ textureMapper.get(player).region.getRegionHeight() * RenderingSystem.PIXELS_TO_WORD;
				textureMapper.get(entity).flipY = false;
				velY = PlayerComponent.jumpVel;
			} else {
				playerPos.pos.y -= 1.5f + boundsMapper.get(entity).height;
				textureMapper.get(entity).flipY = true;
				velY = -PlayerComponent.jumpVel;
			}
			
			engine.getSystem(GravitySystem.class).invert();

			flip = false;

			Assets.flipSound.play();
		}
		
		if(dupVel && bottomCell == CellType.VEL || (PlayerComponent.maxVel == 14 && bottomCell == CellType.NONE)) {
			PlayerComponent.maxVel = 14;
		} else {
			PlayerComponent.maxVel = 7;
		}
		
		//
		movementMapper.get(entity).accel.x *= 9;
		
		// Set states
		float absVelX = Math.abs(movementComp.vel.x);

		if(movementComp.vel.x > 0.1) {
			textureMapper.get(entity).flipX = false;
		} else if(movementComp.vel.x < -0.1) {
			textureMapper.get(entity).flipX = true;
		}

		if(absVelX > 0.5f) {
			stateComp.set(PlayerComponent.WALK_STATE);
		} else {
			stateComp.set(PlayerComponent.IDLE_STATE);
		}
		
		if(Math.abs(movementComp.vel.y) > 0f)
			stateComp.set(PlayerComponent.JUMP_STATE);

		// Limit vel
		if(absVelX > PlayerComponent.maxVel)
			movementComp.vel.x = PlayerComponent.maxVel * Math.signum(movementComp.vel.x);
	}
	
	public void setDubVel(boolean dupVel) {
		this.dupVel = dupVel;
	}
	
	public void setJump(boolean jump) {
		this.jump = jump;
	}
	
	public void setFlip(boolean flip) {
		this.flip = flip;
	}
	
	public void setTurnRight(boolean turnRight) {
		this.turnRight = turnRight;
	}

	public void setTurnLeft(boolean turnLeft) {
		this.turnLeft = turnLeft;
	}
	
	public void getCoin() {
		Assets.pickupCoin.play();
	}

}