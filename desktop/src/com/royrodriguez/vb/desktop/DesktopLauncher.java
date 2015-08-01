package com.royrodriguez.vb.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.royrodriguez.vb.App;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		int s = 70;
		config.width = 16 * s;
		config.height = 9 * s;
		new LwjglApplication(new App(), config);
	}
}
