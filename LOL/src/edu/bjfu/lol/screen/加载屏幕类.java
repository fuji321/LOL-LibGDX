package edu.bjfu.lol.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import edu.bjfu.lol.主类;
import edu.bjfu.lol.scenes.scene2d.自适应舞台类;
import edu.bjfu.lol.scenes.scene2d.ui.进度条演员类;
import edu.bjfu.lol.utils.常量类;

public class 加载屏幕类 extends 屏幕类
{
	private static final String 提示单击屏幕继续字符串 = "点击屏幕继续";
	private static final String 加载屏幕目录 = 常量类.图片目录 + "LoadingScreen/";
	private boolean is进度条已移除;
	private 自适应舞台类 舞台;
	private Skin 皮肤资源;
	private 进度条演员类 进度条演员;
	private 主屏幕类 主屏幕;
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		舞台.act();
		舞台.draw();
		进度条演员.set进度(主类.实例.get初始化进度());
		if (进度条演员.is完成())
		{
			if (!is进度条已移除)
			{
				主屏幕 = new 主屏幕类();
				进度条演员.remove();
				/*TextBounds 字体宽高范围 = 位图字体.getBounds(提示单击屏幕继续字符串);
				*/
				Label 点击继续标签 = new Label(提示单击屏幕继续字符串, 皮肤资源);
				点击继续标签.setPosition((常量类.屏幕宽 - 点击继续标签.getWidth()) / 2, 常量类.屏幕高 / 4);
				SequenceAction 顺序动作 = new SequenceAction();
				顺序动作.addAction(Actions.fadeIn(1));
				顺序动作.addAction(Actions.fadeOut(1));
				RepeatAction 重复动作 = Actions.forever(顺序动作);		
				点击继续标签.addAction(重复动作);
				舞台.addActor(点击继续标签);
				is进度条已移除 = true;
			}
		}
		else
		{
			主类.实例.初始化游戏资源();
		}
	}
	@Override
	public void show()
	{
		if (舞台 == null)
		{
			舞台 = new 自适应舞台类()
			{
				@Override
				public boolean touchDown(int screenX, int screenY, int pointer, int button)
				{
					if (is进度条已移除)
					{
						主类.实例.进入屏幕(主屏幕);
						主类.实例.播放背景音乐();
						dispose();
						return true;
					}
					else
					{
						return false;
					}
				}
			};
			// 加载纹理图集
			TextureAtlas 纹理图集 = new TextureAtlas(Gdx.files.internal(加载屏幕目录 + "Pack.txt"));
			// 加载皮肤资源
			皮肤资源 = new Skin(Gdx.files.internal(加载屏幕目录 + "Skin.json"), 纹理图集);
			// 设置背景图片
			Image 背景图片 = new Image(皮肤资源.getRegion("Background"));
			舞台.addActor(背景图片);
			TextureRegion 后 = 皮肤资源.getRegion("ProgressBar_Back");
			TextureRegion 前 = 皮肤资源.getRegion("ProgressBar_Fore");
			进度条演员 = new 进度条演员类(前, 后, 常量类.屏幕宽 / 2, 后.getRegionHeight());
			进度条演员.setPosition(常量类.屏幕宽 / 4, 常量类.屏幕高 / 10);
			舞台.addActor(进度条演员);
		}
		// 接收用户输入
		Gdx.input.setInputProcessor(舞台);
	}
	@Override
	public void dispose()
	{
		if (舞台 != null)
		{
			舞台.dispose();
		}
		if (皮肤资源 != null)
		{
			皮肤资源.dispose();
		}
	}
}
