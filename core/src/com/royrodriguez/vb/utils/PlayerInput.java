package com.royrodriguez.vb.utils;

import com.badlogic.gdx.Input.Keys;

import javax.swing.JEditorPane;

import com.badlogic.gdx.InputAdapter;
import com.royrodriguez.vb.screens.GameScreen;
import com.royrodriguez.vb.systems.PlayerSystem;
import com.royrodriguez.vb.world.World;

public class PlayerInput extends InputAdapter {
	
	private PlayerSystem playerSystem;
	private World world;
	
	public PlayerInput(PlayerSystem playerSystem, World world) {
		this.playerSystem = playerSystem;
		this.world = world;
		
		reset();
	}
	
	private void reset() {
		playerSystem.setTurnRight(false);
		playerSystem.setTurnLeft(false);
		playerSystem.setJump(false);
		playerSystem.setDubVel(false);
		playerSystem.setFlip(false);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {

		case Keys.RIGHT:
			playerSystem.setTurnRight(true);
			break;
			
		case Keys.LEFT:
			playerSystem.setTurnLeft(true);
			break;

		case Keys.Z:
			playerSystem.setJump(true);
			break;
			
		case Keys.X:
			playerSystem.setDubVel(true);
			playerSystem.setFlip(true);
			break;

		default:
			break;
		}

		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {

		case Keys.RIGHT:
			playerSystem.setTurnRight(false);
		break;
		
		case Keys.LEFT:
			playerSystem.setTurnLeft(false);
		break;

		case Keys.Z:
			playerSystem.setJump(false);
		break;

		case Keys.X:
			playerSystem.setDubVel(false);
			playerSystem.setFlip(false);
		break;

		default:
			break;
		}

		return false;
	}

}
