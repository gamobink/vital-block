package com.royrodriguez.vb.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class Background implements Disposable {
	
	private static Background background;
	
	private Mesh mesh;
	
	private ShaderProgram shader;
	
	private FrameBuffer frameBuffer;
	
	private Background() {
		shader = ShaderLoader.loadShader("shaders/vertex.glsl", "shaders/fragment.glsl");
		float[] vertices = {
				1, 1,
				-1, 1,
				-1, -1,
				-1, -1,
				1, -1,
				1, 1
			};
		mesh = new Mesh(true, 6, 0, new VertexAttribute(Usage.Position, 2, "a_position"));
		mesh.setVertices(vertices);
		
		frameBuffer = new FrameBuffer(Format.RGBA8888, 100, 100, false);
	}
	
	public static Background getInstance() {
		if(background == null)
			background = new Background();
		
		return background;
	}
	
	public Texture getBack(Color bottomColor, Color topColor) {
		frameBuffer.begin();

		shader.begin();
		shader.setUniformf("u_resolution", new Vector2(100, 100));
		shader.setUniformf("u_bottomColor", new Vector3(bottomColor.r, bottomColor.g, bottomColor.b));
		shader.setUniformf("u_topColor", new Vector3(topColor.r, topColor.g, topColor.b));
		mesh.render(shader, Gdx.gl.GL_TRIANGLES);
		shader.end();

		frameBuffer.end();
		
		return frameBuffer.getColorBufferTexture();
	}
	
	@Override
	public void dispose() {
		mesh.dispose();
		shader.dispose();
		frameBuffer.dispose();
	}

}
