package edu.bjfu.lol.scenes.scene2d;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

import edu.bjfu.lol.utils.常量类;

/**
 * 自适应屏幕分辨率的Stage
 */
public class 自适应舞台类 extends Stage
{
	public 自适应舞台类() {
		super(常量类.屏幕宽, 常量类.屏幕高, true);
		// 设置摄像头
		setCamera(new OrthographicCamera(常量类.屏幕宽, 常量类.屏幕高));
		getCamera().position.set(常量类.屏幕宽/2, 常量类.屏幕高/2, 0);
	}
}
