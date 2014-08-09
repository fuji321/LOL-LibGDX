package edu.bjfu.lol;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import edu.bjfu.lol.utils.常量类;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "LOL";
		cfg.useGL20 = false;
		cfg.width = 常量类.屏幕宽;
		cfg.height = 常量类.屏幕高;
		
		new LwjglApplication(new 主类(), cfg);
	}
}
