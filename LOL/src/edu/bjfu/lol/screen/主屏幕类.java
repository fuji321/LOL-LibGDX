package edu.bjfu.lol.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import edu.bjfu.lol.主类;
import edu.bjfu.lol.scenes.scene2d.自适应舞台类;
import edu.bjfu.lol.scenes.scene2d.ui.椭圆转盘演员类;
import edu.bjfu.lol.utils.常量类;

public class 主屏幕类 extends 屏幕类
{
	private static final String 主屏幕目录 = 常量类.图片目录 + "MainScreen/";
	private static final String 图片打包文件名 = 主屏幕目录 + "Pack.txt";
	private 自适应舞台类 舞台;
	private Skin 皮肤资源;
	private 椭圆转盘演员类 转盘演员;
	public 主屏幕类()
	{
		舞台 = new 自适应舞台类();
		舞台.addListener(new InputListener()
		{
			private static final float 转盘接收事件纵坐标开始 = 200;
			private static final float 转盘接收事件纵坐标结束 = 760;
			private float 上一次按下横坐标;
			private boolean is拖动了;
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				if (y >= 转盘接收事件纵坐标开始 && y <= 转盘接收事件纵坐标结束)
				{
					上一次按下横坐标 = x;
					转盘演员.被通知触屏已按下();
					return true;
				}
				else
				{
					return false;
				}
			}
			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer)
			{
				if (Math.abs(x - 上一次按下横坐标) >= 0.1f)
				{
					转盘演员.旋转屏幕正对位置((x - 上一次按下横坐标) / 常量类.屏幕宽 * 2f);
					上一次按下横坐标 = x;
					is拖动了 = true;
				}
			}
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				转盘演员.被通知触屏已松开();
				if (!is拖动了 && x >= 椭圆转盘演员类.转盘中心横坐标 - 常量类.屏幕宽 / 3 && x <= 椭圆转盘演员类.转盘中心横坐标 + 常量类.屏幕宽 / 3 && 转盘演员.is回弹结束())
				{
					主类.实例.播放按钮声音();
					主类.实例.进入屏幕(new 战斗准备屏幕类(转盘演员, 主屏幕类.this));
				}
				is拖动了 = false;
			}
		});
		// 加载纹理图集
		TextureAtlas 纹理图集 = new TextureAtlas(Gdx.files.internal(图片打包文件名));
		// 加载皮肤资源
		皮肤资源 = new Skin(纹理图集);
		// 背景
		Image 背景图片 = new Image(皮肤资源.getRegion("Background"));
		背景图片.setSize(常量类.屏幕宽, 常量类.屏幕高);
		舞台.addActor(背景图片);
		// 查看英雄按钮
		Button 查看英雄按钮 = new Button(皮肤资源.getDrawable("ViewHeroButtonUp"), 皮肤资源.getDrawable("ViewHeroButtonDown"));
		查看英雄按钮.setPosition(21, 770);
		查看英雄按钮.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				主类.实例.播放按钮声音();
				主类.实例.进入屏幕(new 查看英雄屏幕类());
			}
		});
		舞台.addActor(查看英雄按钮);
		// 底部按钮背景
		float 按钮高度调整比例 = 常量类.屏幕宽 * 1.0f / Gdx.graphics.getWidth();
		Image 底部按钮背景图片 = new Image(主类.实例.getUI皮肤资源().getRegion("UnderButtonsBackground"));
		底部按钮背景图片.setSize(底部按钮背景图片.getWidth(), 底部按钮背景图片.getHeight() * 按钮高度调整比例);
		底部按钮背景图片.setPosition(0, 80);
		舞台.addActor(底部按钮背景图片);
		// 主屏幕按钮
		Button 主屏幕按钮 = new Button(主类.实例.getUI皮肤资源().getDrawable("MainScreenButtonUp"), 主类.实例.getUI皮肤资源().getDrawable("MainScreenButtonDown"));
		主屏幕按钮.setPosition(16, 90);
		主屏幕按钮.setSize(主屏幕按钮.getWidth(), 主屏幕按钮.getHeight() * 按钮高度调整比例);
		主屏幕按钮.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				主类.实例.播放按钮声音();
			}
		});
		舞台.addActor(主屏幕按钮);
		// 对战按钮
		Button 对战按钮 = new Button(主类.实例.getUI皮肤资源().getDrawable("BattleButtonUp"), 主类.实例.getUI皮肤资源().getDrawable("BattleButtonDown"));
		对战按钮.setPosition(117, 90);
		对战按钮.setSize(对战按钮.getWidth(), 对战按钮.getHeight() * 按钮高度调整比例);
		对战按钮.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				主类.实例.播放按钮声音();
				主类.实例.进入屏幕(new 寻找目标屏幕类());
			}
		});
		舞台.addActor(对战按钮);
		// 转盘
		转盘演员 = new 椭圆转盘演员类();
		转盘演员.setScale(280);
		舞台.addActor(转盘演员);
	}
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		舞台.act();
		舞台.draw();
	}
	@Override
	public void show()
	{
		// 接收用户输入
		Gdx.input.setInputProcessor(舞台);
	}
	public void 恢复转盘()
	{
		舞台.addActor(转盘演员);
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
