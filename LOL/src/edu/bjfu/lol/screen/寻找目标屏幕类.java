package edu.bjfu.lol.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import edu.bjfu.lol.主类;
import edu.bjfu.lol.对手信息类;
import edu.bjfu.lol.玩家信息类;
import edu.bjfu.lol.scenes.scene2d.自适应舞台类;
import edu.bjfu.lol.utils.图像计算类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.逻辑.战场逻辑类;
import edu.bjfu.lol.静态.英雄类;

public class 寻找目标屏幕类 extends 屏幕类
{
	private static final String 寻找目标屏幕目录 = 常量类.图片目录 + "FindTargetScreen/";
	private static final String 图片打包文件名 = 寻找目标屏幕目录 + "Pack.txt";
	private static final int 英雄图片最大高度 = 170;
	private static final int 英雄图片最大宽度 = 150;
	private static final int[] X坐标数组 = new int[]{212, 433, 133, 513, 常量类.屏幕宽 / 2};
	private static final int[] Y坐标数组 = new int[]{530, 530, 440, 440, 370};
	private 自适应舞台类 舞台;
	private 目标演员类 目标演员;
	private Skin 皮肤资源;
	private class 目标演员类
	{
		private Image[] 英雄图片数组 = new Image[5];
		private 目标演员类()
		{
			生成目标图片();
		}
		private void 生成目标图片()
		{
			英雄类[] 英雄数组 = 对手信息类.get阵容().get英雄数组();
			for (int i = 0; i < 英雄图片数组.length; i++)
			{
				if (英雄数组[i] != null)
				{
					final Image 图片 = new Image(英雄类.get英雄图像(英雄数组[i].get英雄皮肤文件名()));
					图像计算类.调整演员至不超过指定最大宽(图片, 英雄图片最大宽度);
					图像计算类.调整演员至不超过指定最大高(图片, 英雄图片最大高度);
					图片.setPosition(X坐标数组[i] - 图片.getWidth() / 2, Y坐标数组[i] - 图片.getHeight() / 2);
					舞台.addActor(图片);
					英雄图片数组[i] = 图片;
				}
			}
		}
		private void 重新生成目标()
		{
			for (int i = 0; i < 英雄图片数组.length; i++)
			{
				if (英雄图片数组[i] != null)
				{
					英雄图片数组[i].remove();
				}
			}
			生成目标图片();
		}
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
			// 加载纹理图集
			TextureAtlas 纹理图集 = new TextureAtlas(Gdx.files.internal(图片打包文件名));
			// 加载皮肤资源
			皮肤资源 = new Skin(纹理图集);
			// 背景
			Image 背景图片 = new Image(皮肤资源.getRegion("Background"));
			背景图片.setSize(常量类.屏幕宽, 常量类.屏幕高);
			舞台.addActor(背景图片);
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
			// 更换对手按钮
			Button 更换对手按钮 = new Button(皮肤资源.getDrawable("ChangeTargetButtonUp"), 皮肤资源.getDrawable("ChangeTargetButtonDown"));
			更换对手按钮.setPosition((常量类.屏幕宽 / 2 - 更换对手按钮.getWidth()) / 2, 110);
			更换对手按钮.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					对手信息类.生成不一样的随机阵容();
					目标演员.重新生成目标();
					主类.实例.播放按钮声音();
				}
			});
			舞台.addActor(更换对手按钮);
			// 开始对战按钮
			Button 开始对战按钮 = new Button(皮肤资源.getDrawable("StartBattleButtonUp"), 皮肤资源.getDrawable("StartBattleButtonDown"));
			开始对战按钮.setPosition((常量类.屏幕宽 / 2 - 开始对战按钮.getWidth()) / 2 + 常量类.屏幕宽 / 2, 110);
			开始对战按钮.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					主类.实例.播放按钮声音();
					主类.实例.进入屏幕(new 对战屏幕类(new 战场逻辑类().对战(玩家信息类.get阵容(), 对手信息类.get阵容())));
				}
			});
			舞台.addActor(开始对战按钮);
			// 目标演员
			目标演员 = new 目标演员类();
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
