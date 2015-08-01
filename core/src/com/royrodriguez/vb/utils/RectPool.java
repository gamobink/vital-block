package com.royrodriguez.vb.utils;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class RectPool {

	private static Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject () {
			return new Rectangle();
		}
	};
	
	private RectPool() {

	}
	
	public static Pool<Rectangle> getInstance() {
		return rectPool;
	}

}
