package edu.bjfu.lol.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import edu.bjfu.lol.主类;
import edu.bjfu.lol.玩家信息类;
import edu.bjfu.lol.scenes.scene2d.自适应舞台类;
import edu.bjfu.lol.scenes.scene2d.ui.椭圆转盘演员类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.静态.英雄类;

public class 战斗准备屏幕类 extends 屏幕类
{
	private static final String 战斗准备屏幕目录 = 常量类.图片目录 + "BattlePrepareScreen/";
	private static final String 图片打包文件名 = 战斗准备屏幕目录 + "Pack.txt";
	private LabelStyle 名字标签样式;
	private LabelStyle 属性标签样式;
	private Label 名字标签;
	private Label 生命标签;
	private Label 回复标签;
	private Label 先手标签;
	private Label 攻击标签;
	private Label 攻速标签;
	private Label 护甲标签;
	private Label 魔抗标签;
	private 头像列表类 头像列表;
	private 自适应舞台类 舞台;
	private Skin 皮肤资源;
	private 椭圆转盘演员类 转盘演员;
	private 主屏幕类 主屏幕;
	private class 头像类 extends Group
	{
		public 头像类(英雄类 英雄, final int 下标)
		{
			Image 头像 = new Image(英雄 == null ? 皮肤资源.getRegion("Add") : 英雄.get英雄头像());
			addActor(头像);
			if (英雄 == null)
			{
				SequenceAction 顺序动作 = new SequenceAction();
				顺序动作.addAction(Actions.fadeIn(1));
				顺序动作.addAction(Actions.fadeOut(1));
				RepeatAction 重复动作 = Actions.forever(顺序动作);
				addAction(重复动作);
				头像.setPosition(47, 0);
			}
			else
			{
				头像.setSize(80, 80);
			}
			addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					主类.实例.播放按钮声音();
					头像列表.set选中下标(下标);
					转盘演员.转至目标位置(下标);
				}
			});
		}
	}
	private class 头像列表类
	{
		private Image 选中框;
		private 头像类[] 头像数组 = new 头像类[5];
		public 头像列表类()
		{
			选中框 = new Image(皮肤资源.getRegion("Highlight"));
			英雄类[] 英雄数组 = 玩家信息类.get阵容().get英雄数组();
			for (int i = 0; i < 头像数组.length; i++)
			{
				头像数组[i] = new 头像类(英雄数组[i], i);
				头像数组[i].setPosition(i * 125 + 30, 英雄数组[i] == null ? 671 : 674);
				头像数组[i].setSize(80, 80);
				舞台.addActor(头像数组[i]);
			}
			舞台.addActor(选中框);
			set选中下标(转盘演员.get屏幕正对英雄下标());
		}
		private void 重置头像(int 下标)
		{
			头像数组[下标].remove();
			英雄类[] 英雄数组 = 玩家信息类.get阵容().get英雄数组();
			头像数组[下标] = new 头像类(英雄数组[下标], 下标);
			头像数组[下标].setPosition(下标 * 125 + 30, 英雄数组[下标] == null ? 671 : 674);
			头像数组[下标].setSize(80, 80);
			舞台.addActor(头像数组[下标]);
		}
		private void 重置头像()
		{
			for (int i = 0; i < 头像数组.length; i++)
			{
				头像数组[i].remove();
				英雄类[] 英雄数组 = 玩家信息类.get阵容().get英雄数组();
				头像数组[i] = new 头像类(英雄数组[i], i);
				头像数组[i].setPosition(i * 125 + 30, 英雄数组[i] == null ? 671 : 674);
				头像数组[i].setSize(80, 80);
				舞台.addActor(头像数组[i]);
			}
		}
		private void set选中下标(int 选中下标)
		{
			选中框.setPosition(选中下标 * 125 + 22, 666);
		}
	}
	public 战斗准备屏幕类(椭圆转盘演员类 转盘演员, 主屏幕类 主屏幕)
	{
		this.转盘演员 = 转盘演员;
		this.主屏幕 = 主屏幕;
		名字标签样式 = new LabelStyle(主类.实例.getUI皮肤资源().getFont("default"), new Color(239f / 255, 227f / 255, 8f / 255, 1f));
		属性标签样式 = new LabelStyle(主类.实例.getUI皮肤资源().getFont("default"), new Color(255f / 255, 198f / 255, 8f / 255, 1f));
	}
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		舞台.act();
		if (转盘演员.is回弹结束())
		{
			重置标签();
			头像列表.set选中下标(转盘演员.get屏幕正对英雄下标());
		}
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
				private static final float 转盘接收事件纵坐标开始 = 180;
				private static final float 转盘接收事件纵坐标结束 = 664;
				private float 上一次按下横坐标;
				private boolean is转盘被拖动;
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
						is转盘被拖动 = true;
					}
				}
				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button)
				{
					转盘演员.被通知触屏已松开();
					if (is转盘被拖动)
					{
						is转盘被拖动 = false;
					}
					else if (x >= 椭圆转盘演员类.转盘中心横坐标 - 常量类.屏幕宽 / 3 && x <= 椭圆转盘演员类.转盘中心横坐标 + 常量类.屏幕宽 / 3 && 转盘演员.is回弹结束())
					{
						主类.实例.进入屏幕(new 选择英雄屏幕类(转盘演员.get屏幕正对英雄下标(), 战斗准备屏幕类.this));
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
			// 返回按钮
			Button 返回按钮 = new Button(主类.实例.getUI皮肤资源().getDrawable("BackButtonUp"), 主类.实例.getUI皮肤资源().getDrawable("BackButtonDown"));
			返回按钮.setPosition(21, 770);
			返回按钮.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					主屏幕.恢复转盘();
					主类.实例.播放按钮声音();
					主类.实例.退出屏幕();
				}
			});
			舞台.addActor(返回按钮);
			// 布阵按钮
			Button 布阵按钮 = new Button(皮肤资源.getDrawable("EmbattleButtonUp"), 皮肤资源.getDrawable("EmbattleButtonDown"));
			布阵按钮.setPosition(521, 770);
			布阵按钮.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					主类.实例.播放按钮声音();
					主类.实例.进入屏幕(new 布阵屏幕类(战斗准备屏幕类.this));
				}
			});
			舞台.addActor(布阵按钮);
			// 转盘
			舞台.addActor(转盘演员);
			// 头像列表类
			头像列表 = new 头像列表类();
			重置标签();
		}
		// 接收用户输入
		Gdx.input.setInputProcessor(舞台);
	}
	public void 重置阵容信息()
	{
		头像列表.重置头像();
		转盘演员.重置英雄图像();
	}
	public void 重置阵容信息(int 英雄下标)
	{
		头像列表.重置头像(英雄下标);
		转盘演员.重置英雄图像(英雄下标);
		重置标签();
	}
	private void 重置标签()
	{
		英雄类 英雄 = 玩家信息类.get阵容().get英雄数组()[转盘演员.get屏幕正对英雄下标()];
		// 第一次添加标签
		if (名字标签 == null)
		{
			float 字体缩放 = 0.75f;
			// 名字标签
			名字标签 = new Label("???", 名字标签样式);
			名字标签.setPosition(30, 130);
			名字标签.setFontScale(字体缩放);
			舞台.addActor(名字标签);
			// 生命标签
			生命标签 = new Label("生命???", 属性标签样式);
			生命标签.setPosition(200, 130);
			生命标签.setFontScale(字体缩放);
			舞台.addActor(生命标签);
			// 回复标签
			回复标签 = new Label("回复???", 属性标签样式);
			回复标签.setPosition(200 + 160, 130);
			回复标签.setFontScale(字体缩放);
			舞台.addActor(回复标签);
			// 先手标签
			先手标签 = new Label("先手???", 属性标签样式);
			先手标签.setPosition(200 + 150 + 150, 130);
			先手标签.setFontScale(字体缩放);
			舞台.addActor(先手标签);
			// 攻击标签
			攻击标签 = new Label("攻击???", 属性标签样式);
			攻击标签.setPosition(30, 90);
			攻击标签.setFontScale(字体缩放);
			舞台.addActor(攻击标签);
			// 攻速标签
			攻速标签 = new Label("攻速???", 属性标签样式);
			攻速标签.setPosition(200, 90);
			攻速标签.setFontScale(字体缩放);
			舞台.addActor(攻速标签);
			// 护甲标签
			护甲标签 = new Label("护甲???", 属性标签样式);
			护甲标签.setPosition(200 + 160, 90);
			护甲标签.setFontScale(字体缩放);
			舞台.addActor(护甲标签);
			// 魔抗标签
			魔抗标签 = new Label("魔抗???", 属性标签样式);
			魔抗标签.setPosition(200 + 150 + 150, 90);
			魔抗标签.setFontScale(字体缩放);
			舞台.addActor(魔抗标签);
		}
		名字标签.setText(英雄 == null ? "???" : 英雄.get英雄名字());
		生命标签.setText(String.format("生命%s", 英雄 == null ? "???" : String.valueOf(英雄.get英雄属性().get最大生命值())));
		回复标签.setText(String.format("回复%s", 英雄 == null ? "???" : String.valueOf(英雄.get英雄属性().get生命回复())));
		先手标签.setText(String.format("先手%s", 英雄 == null ? "???" : String.valueOf(英雄.get英雄属性().get先手值())));
		攻击标签.setText(String.format("攻击%s", 英雄 == null ? "???" : String.valueOf(英雄.get英雄属性().get攻击力())));
		攻速标签.setText(String.format("攻速%s", 英雄 == null ? "???" : String.format("%.2f", 英雄.get英雄属性().get攻击速度())));
		护甲标签.setText(String.format("护甲%s", 英雄 == null ? "???" : String.valueOf(英雄.get英雄属性().get护甲值())));
		魔抗标签.setText(String.format("魔抗%s", 英雄 == null ? "???" : String.valueOf(英雄.get英雄属性().get魔法抗性())));
	}
	@Override
	public void dispose()
	{
		if (舞台 != null)
		{
			舞台.dispose();
			舞台 = null;
		}
		if (皮肤资源 != null)
		{
			皮肤资源.dispose();
			皮肤资源 = null;
		}
	}
}
