package edu.bjfu.lol.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import edu.bjfu.lol.主类;
import edu.bjfu.lol.scenes.scene2d.自适应舞台类;
import edu.bjfu.lol.utils.图像计算类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.静态.英雄类;

public class 英雄详细屏幕类 extends 屏幕类
{
	private static final String 英雄详细屏幕目录 = 常量类.图片目录 + "HeroDetailScreen/";
	private static final String 图片打包文件名 = 英雄详细屏幕目录 + "Pack.txt";
	private LabelStyle 名字标签样式;
	private LabelStyle 属性标签样式;
	private 英雄类 英雄;
	private 自适应舞台类 舞台;
	private Skin 皮肤资源;
	public class 技能描述类
	{
		private static final float 文本字体缩放比例 = 0.5f;
		private static final int 文本左上X坐标 = 55;
		private static final int 文本左上Y坐标 = 260;
		private Label 技能文本标签;
		private Label 技能释放回合文本标签;
		private Image 技能图像;
		private 技能列表演员类 技能列表演员;
		public 技能描述类(String 技能文本, TextureRegion 技能图片, int 第一次释放回合数, int 释放间隔回合数)
		{
			技能图像 = new Image(技能图片);
			技能图像.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					技能列表演员.切换技能显示文本(技能描述类.this);
				}
			});
			技能文本标签 = new Label(技能文本, 属性标签样式);
			技能文本标签.setFontScale(文本字体缩放比例);
			技能文本标签.setSize(技能文本标签.getWidth() * 文本字体缩放比例, 技能文本标签.getHeight() * 文本字体缩放比例);
			技能文本标签.setPosition(文本左上X坐标, 文本左上Y坐标 - 技能文本标签.getHeight());
			技能释放回合文本标签 = new Label(String.format("技能每%d回合释放一次,第%d回合释放!", 释放间隔回合数, 第一次释放回合数), 属性标签样式);
			技能释放回合文本标签.setFontScale(文本字体缩放比例);
			技能释放回合文本标签.setSize(技能释放回合文本标签.getWidth() * 文本字体缩放比例, 技能释放回合文本标签.getHeight() * 文本字体缩放比例);
			技能释放回合文本标签.setPosition(文本左上X坐标, 158);
		}
		private void set技能列表演员(技能列表演员类 技能列表演员)
		{
			this.技能列表演员 = 技能列表演员;
		}
		private void 显示技能文本(技能列表演员类 列表演员)
		{
			列表演员.addActor(技能文本标签);
			列表演员.addActor(技能释放回合文本标签);
		}
		private void 移除技能文本()
		{
			技能文本标签.remove();
			技能释放回合文本标签.remove();
		}
	}
	private class 技能列表演员类 extends Group
	{
		private 技能描述类 当前技能描述;
		private HorizontalGroup 水平布局 = new HorizontalGroup();
		public 技能列表演员类(技能描述类[] 技能描述数组)
		{
			for (技能描述类 技能描述 : 技能描述数组)
			{
				水平布局.addActor(技能描述.技能图像);
				技能描述.set技能列表演员(this);
			}
			当前技能描述 = 技能描述数组[0];
			当前技能描述.显示技能文本(this);
			ScrollPane 滚动板 = new ScrollPane(水平布局);
			滚动板.setPosition(60, 267);
			滚动板.setSize(520, 滚动板.getHeight());
			水平布局.setSpacing(30);
			addActor(滚动板);
		}
		private void 切换技能显示文本(技能描述类 新技能描述)
		{
			if (新技能描述 != 当前技能描述)
			{
				当前技能描述.移除技能文本();
				新技能描述.显示技能文本(this);
				当前技能描述 = 新技能描述;
			}
		}
	}
	public 英雄详细屏幕类(英雄类 英雄)
	{
		this.英雄 = 英雄;
		名字标签样式 = new LabelStyle(主类.实例.getUI皮肤资源().getFont("default"), new Color(239f / 255, 235f / 255, 132f / 255, 1f));
		属性标签样式 = new LabelStyle(主类.实例.getUI皮肤资源().getFont("default"), new Color(74f / 255, 40f / 255, 8f / 255, 1));
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
			// 英雄图像
			Image 英雄图像 = new Image(英雄类.get英雄图像(英雄.get英雄皮肤文件名()));
			图像计算类.调整演员至不超过指定最大宽(英雄图像, 200);
			图像计算类.调整演员至不超过指定最大高(英雄图像, 250);
			int 英雄图像中心X = 180;
			英雄图像.setPosition(英雄图像中心X - 英雄图像.getWidth() / 2, 490);
			舞台.addActor(英雄图像);
			// 英雄名字
			Label 英雄名字标签 = new Label(英雄.get英雄名字(), 名字标签样式);
			float 名字缩放比例 = 0.8f;
			英雄名字标签.setFontScale(名字缩放比例);
			英雄名字标签.setSize(英雄名字标签.getWidth() * 名字缩放比例, 英雄名字标签.getHeight() * 名字缩放比例);
			英雄名字标签.setPosition(英雄图像中心X - 英雄名字标签.getWidth() / 2, 435);
			舞台.addActor(英雄名字标签);
			int 属性X坐标 = 355;
			// 生命值
			Label 生命值标签 = new Label(String.format("生命值:%d", 英雄.get英雄属性().get最大生命值()), 属性标签样式);
			生命值标签.setPosition(属性X坐标, 670);
			舞台.addActor(生命值标签);
			// 攻击力
			Label 攻击力标签 = new Label(String.format("攻击力:%d", 英雄.get英雄属性().get攻击力()), 属性标签样式);
			攻击力标签.setPosition(属性X坐标, 629);
			舞台.addActor(攻击力标签);
			// 攻击速度
			Label 攻击速度标签 = new Label(String.format("攻击速度:%.2f", 英雄.get英雄属性().get攻击速度()), 属性标签样式);
			攻击速度标签.setPosition(属性X坐标, 588);
			舞台.addActor(攻击速度标签);
			// 先手值
			Label 先手值标签 = new Label(String.format("先手值:%d", 英雄.get英雄属性().get先手值()), 属性标签样式);
			先手值标签.setPosition(属性X坐标, 547);
			舞台.addActor(先手值标签);
			// 护甲值
			Label 护甲值标签 = new Label(String.format("护甲值:%d", 英雄.get英雄属性().get护甲值()), 属性标签样式);
			护甲值标签.setPosition(属性X坐标, 506);
			舞台.addActor(护甲值标签);
			// 魔法抗性
			Label 魔法抗性标签 = new Label(String.format("魔法抗性:%d", 英雄.get英雄属性().get魔法抗性()), 属性标签样式);
			魔法抗性标签.setPosition(属性X坐标, 465);
			舞台.addActor(魔法抗性标签);
			// 生命回复
			Label 生命回复标签 = new Label(String.format("生命回复:%d", 英雄.get英雄属性().get生命回复()), 属性标签样式);
			生命回复标签.setPosition(属性X坐标, 424);
			舞台.addActor(生命回复标签);
			// 技能列表
			舞台.addActor(new 技能列表演员类(英雄.get英雄描述数组(英雄详细屏幕类.this)));
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
			// 英雄声音
			英雄.get英雄声音().play();
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
