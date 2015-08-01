package com.royrodriguez.vb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Assets {
	
	private static Texture sheet;

	public static TextureRegion floor;

	public static TextureRegion coin;

	public static TextureRegion target;

	public static TextureRegion enemy;
	public static TextureRegion bigEnemy;
	
	public static Animation playerWalkAnimation;
	public static Animation playerIdleAnimation;
	public static Animation playerJumpAnimation;

	public static TextureRegion buttonUp;
	public static TextureRegion buttonDown;
	
	public static Sound jumpSound;
	public static Sound pickupCoin;
	public static Sound flipSound;
	
	public static void load() {
		sheet = new Texture("imgs/sheet.png");

		floor = new TextureRegion(sheet, 0, 10, 9 * 16, 2);
		
		enemy = new TextureRegion(sheet, 0, 12, 13, 16);
		bigEnemy = new TextureRegion(sheet, 13, 12, 13, 30);
		
		coin = new TextureRegion(sheet, 0, 41, 12, 13);

		target = new TextureRegion(sheet, 21, 73, 14, 16);
		
		playerWalkAnimation = new Animation(1.0f / 8.0f,
			new TextureRegion(sheet, 62, 57, 32, 11),
			new TextureRegion(sheet, 94, 56, 29, 12),
			new TextureRegion(sheet, 123, 55, 26, 13),
			new TextureRegion(sheet, 94, 56, 29, 12)
		);
		
		playerIdleAnimation = new Animation(1.0f / 8.0f,
			new TextureRegion(sheet, 62, 57, 32, 11)
		);
		
		playerJumpAnimation = new Animation(1.0f /8.0f,
			new TextureRegion(sheet, 94, 56, 29, 12)
		);
		
		buttonUp = new TextureRegion(sheet, 0, 0, 16, 16);
		buttonDown = new TextureRegion(sheet, 16, 0, 16, 16);
		
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.wav"));
		pickupCoin = Gdx.audio.newSound(Gdx.files.internal("sounds/pickupCoin.wav"));
		flipSound = Gdx.audio.newSound(Gdx.files.internal("sounds/flip.wav"));
	}
	
	public static void dispose() {
		sheet.dispose();

		jumpSound.dispose();
		pickupCoin.dispose();
		flipSound.dispose();
	}
}
