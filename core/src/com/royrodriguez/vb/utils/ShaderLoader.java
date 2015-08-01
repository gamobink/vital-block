package com.royrodriguez.vb.utils;

import java.io.BufferedReader;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderLoader {

	public static ShaderProgram loadShader(String vertexPath, String fragementPath) {
		ShaderProgram shader = new ShaderProgram(toFormat(vertexPath), toFormat(fragementPath));
		
		if(shader.getLog().length() > 0) System.out.println(shader.getLog());
		
		return shader;
	}

	private static String toFormat(String internalPath) {
		FileHandle file = Gdx.files.internal(internalPath);

		BufferedReader reader = new BufferedReader(file.reader());

		StringBuilder src = new StringBuilder();

		String line;
		
		try {
			while((line = reader.readLine()) != null)
				src.append(line).append("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return src.toString();
	}

}
