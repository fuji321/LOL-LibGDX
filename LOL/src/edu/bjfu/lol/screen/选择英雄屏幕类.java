package edu.bjfu.lol.screen;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import edu.bjfu.lol.主类;
import edu.bjfu.lol.玩家信息类;
import edu.bjfu.lol.scenes.scene2d.自适应舞台类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.静态.英雄类;

public class 选择英雄屏幕类 extends 屏幕类
{
	private static final String 选择英雄屏幕目录 = 常量类.图片目录 + "SelectHeroScreen/";
	private static final String 图片打包文件名 = 选择英雄屏幕目录 + "Pack.txt";
	private 自适应舞台类 舞台;
	private Skin 皮肤资源;
	private 英雄列表演员类 英雄列表演员;
	private int 英雄下标;
	private LabelStyle 名字标签格式;
	private 战斗准备屏幕类 战斗准备屏幕;
	private class 英雄列表演员类 extends Group
	{
		private static final int 每页显示演员数量 = 10;
		private static final int 接收触控高度 = 500;
		private static final int 接收触控起始坐标 = 250;
		private static final int 左右间距 = 20;
		private int 当前页数;
		private int 总页数;
		private VerticalGroup 垂直布局 = new VerticalGroup();
		private ScrollPane 滚动面板;
		private Label 页数标签;
		private LinkedList<Actor> 演员列表 = new LinkedList<Actor>();
		private 英雄列表演员类()
		{
			if (玩家信息类.get阵容().get英雄数组()[英雄下标] != null)
			{
				演员列表.add(new 英雄信息条演员类(null));
			}
			for (英雄类 英雄 : 玩家信息类.get英雄列表())
			{
				if (!玩家信息类.get阵容().is包含该英雄(英雄))
				{
					演员列表.add(new 英雄信息条演员类(英雄));
				}
			}
			总页数 = (演员列表.size() / 每页显示演员数量) + (演员列表.size() % 每页显示演员数量 == 0 ? 0 : 1);
			当前页数 = 1;
			for (int i = (当前页数 - 1) * 每页显示演员数量; i < 当前页数 * 每页显示演员数量 && i < 演员列表.size(); i++)
			{
				垂直布局.addActor(演员列表.get(i));
			}
			垂直布局.setAlignment(Align.left);
			滚动面板 = new ScrollPane(垂直布局);
			滚动面板.setSize(常量类.屏幕宽 - 左右间距, 接收触控高度);
			滚动面板.setPosition(左右间距, 接收触控起始坐标);
			滚动面板.setScrollingDisabled(true, false);
			页数标签 = new Label(String.format("%d/%d", 当前页数, 总页数), new LabelStyle(主类.实例.getUI皮肤资源().get(BitmapFont.class), new Color(255, 230, 0, 1f)));
			页数标签.setFontScale(0.6f);
			页数标签.setPosition(90, 208);
			addActor(页数标签);
			addActor(滚动面板);
		}
		private void 上一页()
		{
			当前页数--;
			if (当前页数 < 1)
			{
				if (总页数 == 0)
				{
					当前页数 = 1;
				}
				else
				{
					当前页数 = 总页数;
				}
			}
			重置显示页();
		}
		private void 下一页()
		{
			当前页数++;
			if (当前页数 > 总页数)
			{
				当前页数 = 1;
			}
			重置显示页();
		}
		private void 重置显示页()
		{
			垂直布局.clearChildren();
			滚动面板.setScrollY(0);
			for (int i = (当前页数 - 1) * 每页显示演员数量; i < 当前页数 * 每页显示演员数量 && i < 演员列表.size(); i++)
			{
				垂直布局.addActor(演员列表.get(i));
			}
			页数标签.setText(String.format("%d/%d", 当前页数, 总页数));
		}
	}
	private class 英雄信息条演员类 extends Group
	{
		private Image 背景图片 = new Image();
		private Drawable 背景弹起;
		private Drawable 背景按下;
		public 英雄信息条演员类(final 英雄类 英雄)
		{
			背景弹起 = 皮肤资源.getDrawable("HeroBarBackgroundUp");
			背景按下 = 皮肤资源.getDrawable("HeroBarBackgroundDown");
			背景图片.setDrawable(背景弹起);
			背景图片.setSize(背景弹起.getMinWidth(), 背景弹起.getMinHeight());
			setSize(常量类.屏幕宽 - 2 * 英雄列表演员类.左右间距, 背景弹起.getMinHeight());
			Image 头像图片 = null;
			Label 名字标签 = null;
			if (英雄 == null)
			{
				名字标签 = new Label("卸下女神", 名字标签格式);
				头像图片 = new Image(皮肤资源.getRegion("DeleteHero"));
			}
			else
			{
				头像图片 = new Image(英雄.get英雄头像());
				名字标签 = new Label(英雄.get英雄名字(), 名字标签格式);
			}
			addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					玩家信息类.get阵容().get英雄数组()[英雄下标] = 英雄;
					战斗准备屏幕.重置阵容信息(英雄下标);
					玩家信息类.保存阵容();
					主类.实例.退出屏幕();
				}
			});
			头像图片.setPosition(英雄列表演员类.左右间距, (getHeight() - 头像图片.getHeight()) / 2);
			名字标签.setPosition(头像图片.getX() + 头像图片.getWidth() + 英雄列表演员类.左右间距, (getHeight() - 名字标签.getHeight()) / 2);
			addActor(背景图片);
			addActor(头像图片);
			addActor(名字标签);
			addListener(new InputListener()
			{
				private float 上一次按下位置Y;
				boolean is拖动了;
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
				{
					if (x >= 0 && x < 背景图片.getWidth() && y >= 0 && y < 背景图片.getHeight())
					{
						上一次按下位置Y = y;
						is拖动了 = false;
						背景图片.setDrawable(背景按下);
						return true;
					}
					else
					{
						return false;
					}
				}
				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button)
				{
					// 单击了某个英雄
					if (!is拖动了)
					{
						主类.实例.播放按钮声音();
						背景图片.setDrawable(背景弹起);
					}
				}
				@Override
				public void touchDragged(InputEvent event, float x, float y, int pointer)
				{
					if (上一次按下位置Y != y)
					{
						is拖动了 = true;
						背景图片.setDrawable(背景弹起);
					}
				}
			});
		}
	}
	public 选择英雄屏幕类(int 英雄下标, 战斗准备屏幕类 战斗准备屏幕)
	{
		this.英雄下标 = 英雄下标;
		this.战斗准备屏幕 = 战斗准备屏幕;
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
			名字标签格式 = new LabelStyle(主类.实例.getUI皮肤资源().getFont("default"), new Color(103f / 255, 66f / 255, 11f / 255, 1));
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
					主类.实例.退出屏幕();
				}
			});
			舞台.addActor(返回按钮);
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
					主类.实例.回到主屏幕();
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
			英雄列表演员 = new 英雄列表演员类();
			舞台.addActor(英雄列表演员);
			// 上一页按钮
			Image 上一页按钮 = new Image(皮肤资源.getDrawable("LeftButton"));
			上一页按钮.setPosition(40, 500);
			上一页按钮.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					英雄列表演员.上一页();
				}
			});
			舞台.addActor(上一页按钮);
			// 下一页按钮
			Image 下一页按钮 = new Image(皮肤资源.getDrawable("RightButton"));
			下一页按钮.setPosition(常量类.屏幕宽 - 65, 500);
			下一页按钮.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					英雄列表演员.下一页();
				}
			});
			舞台.addActor(下一页按钮);
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
