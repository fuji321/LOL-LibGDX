package edu.bjfu.lol.screen;

import java.util.Arrays;

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
import edu.bjfu.lol.玩家信息类;
import edu.bjfu.lol.scenes.scene2d.自适应舞台类;
import edu.bjfu.lol.utils.图像计算类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.静态.英雄类;

public class 布阵屏幕类 extends 屏幕类
{
	private static final String 布阵屏幕目录 = 常量类.图片目录 + "EmbattleScreen/";
	private static final String 图片打包文件名 = 布阵屏幕目录 + "Pack.txt";
	private static final int 英雄图片最大高度 = 170;
	private static final int 英雄图片最大宽度 = 150;
	private static final int[] X坐标数组 = new int[]{165, 360, 60, 465, 265};
	private static final int[] Y坐标数组 = new int[]{550, 550, 434, 434, 328};
	private 自适应舞台类 舞台;
	private Skin 皮肤资源;
	private 布阵演员类 布阵演员;
	private 英雄类[] 英雄阵型数组;
	private 战斗准备屏幕类 战斗准备屏幕;
	private class 布阵演员类
	{
		private Image[] 英雄图片数组 = new Image[5];
		private 布阵演员类()
		{
			英雄类[] 英雄数组 = 玩家信息类.get阵容().get英雄数组();
			for (int i = 0; i < 英雄图片数组.length; i++)
			{
				if (英雄数组[i] != null)
				{
					final Image 图片 = new Image(英雄类.get英雄图像(英雄数组[i].get英雄皮肤文件名()));
					图像计算类.调整演员至不超过指定最大宽(图片, 英雄图片最大宽度);
					图像计算类.调整演员至不超过指定最大高(图片, 英雄图片最大高度);
					图片.setPosition(X坐标数组[i], Y坐标数组[i]);
					舞台.addActor(图片);
					英雄图片数组[i] = 图片;
				}
			}
		}
		private int 按下英雄下标(float x, float y)
		{
			for (int i = 0; i < 英雄图片数组.length; i++)
			{
				if (英雄图片数组[i] != null && x >= X坐标数组[i] && x <= X坐标数组[i] + 英雄图片数组[i].getWidth() && y >= Y坐标数组[i] && y <= Y坐标数组[i] + 英雄图片数组[i].getHeight())
				{
					return i;
				}
			}
			return -1;
		}
		private void 交换图片位置(int 按下英雄下标, int 释放英雄下标)
		{
			Image 交换变量 = 英雄图片数组[按下英雄下标]; 
			英雄图片数组[按下英雄下标] = 英雄图片数组[释放英雄下标];
			英雄图片数组[释放英雄下标] = 交换变量;
			交换变量.setPosition(X坐标数组[释放英雄下标], Y坐标数组[释放英雄下标]);
			if (英雄图片数组[按下英雄下标] != null)
			{
				英雄图片数组[按下英雄下标].setPosition(X坐标数组[按下英雄下标], Y坐标数组[按下英雄下标]);
			}
			for (int i = 0; i < 英雄图片数组.length; i++)
			{
				if (英雄图片数组[i] != null)
				{
					英雄图片数组[i].remove();
					舞台.addActor(英雄图片数组[i]);
				}
			}
		}
		private void 释放英雄(int 按下英雄下标, float x, float y)
		{
			int 释放英雄下标 = -1;
			for (int i = 0; i < 英雄图片数组.length; i++)
			{
				if (x >= X坐标数组[i] && x <= X坐标数组[i] + 英雄图片最大宽度 && y >= Y坐标数组[i] && y <= Y坐标数组[i] + 英雄图片最大高度)
				{
					释放英雄下标 = i;
					break;
				}
			}
			if (释放英雄下标 >= 0)
			{
				交换图片位置(按下英雄下标, 释放英雄下标);
				// 交换阵型数组里的变量
				英雄类 交换变量 = 英雄阵型数组[释放英雄下标];
				英雄阵型数组[释放英雄下标] = 英雄阵型数组[按下英雄下标];
				英雄阵型数组[按下英雄下标] = 交换变量;
			}
			else
			{
				英雄图片数组[按下英雄下标].setPosition(X坐标数组[按下英雄下标], Y坐标数组[按下英雄下标]);
			}
		}
	}
	public 布阵屏幕类(战斗准备屏幕类 战斗准备屏幕)
	{
		this.战斗准备屏幕 = 战斗准备屏幕;
		英雄阵型数组 = Arrays.copyOf(玩家信息类.get阵容().get英雄数组(), 5);
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
		if (舞台 == null)
		{
			舞台 = new 自适应舞台类();
			舞台.addListener(new InputListener()
			{
				private int 按下英雄下标;
				private Image 被拖动图片;
				private float 按下X偏移;
				private float 按下Y偏移;
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
				{
					if (y >= 205 && y <= 737)
					{
						按下英雄下标 = 布阵演员.按下英雄下标(x, y);
						if (按下英雄下标 >= 0)
						{
							被拖动图片 = 布阵演员.英雄图片数组[按下英雄下标];
							按下X偏移 = x - 布阵演员.英雄图片数组[按下英雄下标].getX();
							按下Y偏移 = y - 布阵演员.英雄图片数组[按下英雄下标].getY();
							return true;
						}
					}
					return false;
				}
				@Override
				public void touchDragged(InputEvent event, float x, float y, int pointer)
				{
					if (被拖动图片 != null)
					{
						被拖动图片.setPosition(x - 按下X偏移, y - 按下Y偏移);
					}
				}
				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button)
				{
					if (被拖动图片 != null)
					{
						布阵演员.释放英雄(按下英雄下标, x, y);
						被拖动图片 = null;
					}
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
			// 保存按钮
			Button 保存按钮 = new Button(皮肤资源.getDrawable("SaveButtonUp"), 皮肤资源.getDrawable("SaveButtonDown"));
			保存按钮.setPosition((常量类.屏幕宽 - 保存按钮.getWidth()) / 2, 120);
			保存按钮.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					玩家信息类.get阵容().set英雄数组(英雄阵型数组);
					战斗准备屏幕.重置阵容信息();
					玩家信息类.保存阵容();
					主类.实例.播放按钮声音();
					主类.实例.退出屏幕();
				}
			});
			舞台.addActor(保存按钮);
			// 返回按钮
			Button 返回按钮 = new Button(主类.实例.getUI皮肤资源().getDrawable("BackButtonUp"), 主类.实例.getUI皮肤资源().getDrawable("BackButtonDown"));
			返回按钮.setPosition(21, 770);
			返回按钮.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					主类.实例.播放按钮声音();
					主类.实例.退出屏幕();
				}
			});
			舞台.addActor(返回按钮);
			// 布阵演员
			布阵演员 = new 布阵演员类();
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
