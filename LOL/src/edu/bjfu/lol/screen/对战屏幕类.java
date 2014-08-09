package edu.bjfu.lol.screen;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import edu.bjfu.lol.主类;
import edu.bjfu.lol.对手信息类;
import edu.bjfu.lol.玩家信息类;
import edu.bjfu.lol.scenes.scene2d.自适应舞台类;
import edu.bjfu.lol.scenes.scene2d.actions.保持中心X大小绝对变化Action;
import edu.bjfu.lol.scenes.scene2d.actions.围绕原点转圈移动Action;
import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
import edu.bjfu.lol.scenes.scene2d.actions.相对移动Action;
import edu.bjfu.lol.scenes.scene2d.actions.绝对移动Action;
import edu.bjfu.lol.scenes.scene2d.ui.动画演员类;
import edu.bjfu.lol.scenes.scene2d.ui.粒子演员类;
import edu.bjfu.lol.scenes.scene2d.ui.进度条演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.动作.并行动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.utils.图像计算类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.英雄类;

public class 对战屏幕类 extends 屏幕类
{
	private static final String 对战屏幕目录 = 常量类.图片目录 + "BattleScreen/";
	private static final String 图片打包文件名 = 对战屏幕目录 + "Pack.txt";
	public static final int 英雄图片最大高度 = 198;
	public static final int 英雄图片最大宽度 = 153;
	private static final float 放技能前变大倍数 = 1.2f;
	private static final int[] 我方其他位与0号位X坐标偏差数组 = new int[]{0, 144, -120, 192, -15};
	private static final int[] 我方其他位与0号位Y坐标偏差数组 = new int[]{0, -21, -70, -148, -160};
	private static final int 回合数标签X坐标 = 常量类.屏幕宽 / 2;
	private static final int 回合数标签Y坐标 = 845;
	public static final int[] 我方X坐标数组 = new int[]{191,0,0,0,0};
	public static final int[] 我方Y坐标数组 = new int[]{336,0,0,0,0};
	public static final int[] 敌方X坐标数组 = new int[]{330,0,0,0,0};
	public static final int[] 敌方Y坐标数组 = new int[]{550,0,0,0,0};
	// 各代码可与此进行交互,以减少通信的开销
	public static 对战屏幕类 实例;
	static
	{
		for (int i = 1; i < 5; i++)
		{
			我方X坐标数组[i] = 我方X坐标数组[0] + 我方其他位与0号位X坐标偏差数组[i];
			我方Y坐标数组[i] = 我方Y坐标数组[0] + 我方其他位与0号位Y坐标偏差数组[i];
			if (i == 4)
			{
				敌方X坐标数组[i] = 敌方X坐标数组[0] - 我方其他位与0号位X坐标偏差数组[i];
			}
			else
			{
				敌方X坐标数组[i] = 敌方X坐标数组[0] + 我方其他位与0号位X坐标偏差数组[i];
			}
			敌方Y坐标数组[i] = 敌方Y坐标数组[0] - 我方其他位与0号位Y坐标偏差数组[i];
		}
	}
	public static class 英雄造型类 extends Group
	{
		private static final int 血条高度 = 10;
		private static final float 血条宽度所占英雄宽度比例 = 0.6f;
		// 英雄造型静止时宽高
		private float 正常宽;
		private float 正常高;
		private static final float 沉默图标缩放比例 = 0.25f;
		private boolean is正在被沉默;
		private Image 沉默图标;
		private boolean is正在被冰冻;
		private Image 冰冻图标;
		private boolean is正在被眩晕;
		private Image 眩晕图标;
		private boolean is正在被压制;
		private Image 压制图标;
		private boolean is正在被嘲讽;
		private Image 嘲讽图标;
		private boolean is正在被击飞;
		private boolean is正在被致盲;
		private Image 致盲图标;
		private boolean is正在隐身;
		private 动画演员类 击飞效果;
		private 粒子演员类 击飞叶子效果;
		private 粒子演员类 集火效果;
		private boolean is正在被集火;
		private Image 英雄;
		private 进度条演员类 血条;
		private float 英雄X;
		private int 正常Z序号;
		public 英雄造型类(float 英雄X, Image 英雄图像, TextureRegion 血条前, TextureRegion 血条后)
		{
			this.英雄X = 英雄X;
			this.英雄 = 英雄图像;
			血条 = new 进度条演员类(血条前, 血条后, 英雄图片最大宽度 * 血条宽度所占英雄宽度比例, 血条高度);
			血条.set进度(1);
			addActor(英雄);
			addActor(血条);
			血条.setWidth(血条宽度所占英雄宽度比例 * 英雄图片最大宽度);
			血条.setX((1 - 血条宽度所占英雄宽度比例) * 英雄图片最大宽度 / 2);
			英雄.setY(血条.getHeight());
			setSize(英雄.getWidth(), 英雄.getHeight() + 血条.getHeight());
			正常宽 = getWidth();
			正常高 = getHeight();
			// 沉默图标
			沉默图标 = new Image(对战屏幕类.实例.皮肤资源.getRegion("Silence"));
			沉默图标.setSize(沉默图标.getWidth() * 沉默图标缩放比例, 沉默图标.getHeight() * 沉默图标缩放比例);
			沉默图标.setPosition((英雄.getWidth() - 沉默图标.getWidth()) / 2, 英雄.getY() + 英雄.getHeight());
			// 冰冻图标
			冰冻图标 = new Image(对战屏幕类.实例.皮肤资源.getRegion("Frozen"));
			图像计算类.调整演员至不超过指定最大宽(冰冻图标, 英雄.getWidth() * 1.1f);
			冰冻图标.setPosition((英雄.getWidth() - 冰冻图标.getWidth()) / 2, 英雄.getY() + (英雄.getHeight() - 冰冻图标.getHeight()) / 2);
			// 眩晕图标
			眩晕图标 = new Image(对战屏幕类.实例.皮肤资源.getRegion("Stun"));
			图像计算类.调整演员至不超过指定最大宽(眩晕图标, 英雄.getWidth());
			眩晕图标.setOrigin(眩晕图标.getWidth() / 2, 眩晕图标.getHeight() / 2);
			眩晕图标.addAction(Actions.forever(Actions.rotateBy(-100, 0.3f)));
			眩晕图标.setPosition((英雄.getWidth() - 眩晕图标.getWidth()) / 2, 英雄.getY() + (英雄.getHeight() - 眩晕图标.getHeight()) / 2);
			// 压制图标
			压制图标 = new Image(对战屏幕类.实例.皮肤资源.getRegion("Bound"));
			图像计算类.调整演员至不超过指定最大宽(压制图标, 英雄.getWidth());
			压制图标.setPosition((英雄.getWidth() - 压制图标.getWidth()) / 2, 英雄.getY() + (英雄.getHeight() - 压制图标.getHeight()) / 2);
			// 嘲讽图标
			嘲讽图标 = new Image(对战屏幕类.实例.皮肤资源.getRegion("Horror"));
			图像计算类.调整演员至不超过指定最大宽(嘲讽图标, 英雄.getWidth() * 0.5f);
			float 闪烁周期 = 0.2f;
			嘲讽图标.addAction(Actions.forever(Actions.sequence(Actions.alpha(0, 闪烁周期 / 2), Actions.alpha(1, 闪烁周期 / 2))));
			嘲讽图标.setPosition((英雄.getWidth() - 嘲讽图标.getWidth()) / 2, 英雄.getY() + (英雄.getHeight() - 嘲讽图标.getHeight()) / 2);
			// 击飞效果
			TextureRegion[] 击飞图标数组 = new TextureRegion[2];
			击飞图标数组[0] = 对战屏幕类.实例.皮肤资源.getRegion("AeroA");
			击飞图标数组[1] = 对战屏幕类.实例.皮肤资源.getRegion("AeroB");
			击飞效果 = new 动画演员类(0.1f, 击飞图标数组, Animation.LOOP);
			击飞效果.setColor(new Color(1, 1, 1, 0.2f));
			图像计算类.调整演员至不超过指定最大宽(击飞效果, 英雄.getWidth() * 0.8f);
			击飞效果.setPosition((英雄.getWidth() - 击飞效果.getWidth()) / 2, 英雄.getY() + (英雄.getHeight() - 击飞效果.getHeight()) / 2);
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "AeroLeaf.p"), 对战屏幕类.实例.皮肤资源.getAtlas());
			击飞叶子效果 = new 粒子演员类(粒子效果);
			击飞叶子效果.addAction(Actions.forever(new 围绕原点转圈移动Action(英雄.getWidth() / 2, 英雄.getY() + 英雄.getHeight() / 2, 击飞效果.getWidth() / 2 * 0.7f, 1f)));
			// 致盲图标
			致盲图标 = new Image(对战屏幕类.实例.皮肤资源.getRegion("Blind"));
			图像计算类.调整演员至不超过指定最大宽(致盲图标, 英雄.getWidth() * 0.4f);
			致盲图标.setPosition((英雄.getWidth() - 致盲图标.getWidth()) / 2, 英雄.getY() + (英雄.getHeight() - 致盲图标.getHeight()) / 2);
			// 集火效果
			粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "Fired.p"), 对战屏幕类.实例.皮肤资源.getAtlas());
			集火效果 = new 粒子演员类(粒子效果);
			集火效果.setPosition(英雄.getWidth() / 2, 英雄.getY() + 英雄.getHeight() / 2);
		}
		public void 保存正常Z序号()
		{
			正常Z序号 = getZIndex();
		}
		public int get正常Z序号()
		{
			return 正常Z序号;
		}
		public void 设置英雄阵亡图片(Drawable 死亡图)
		{
			removeActor(血条);
			血条 = null;
			英雄.setDrawable(死亡图);
			英雄.setSize(100, 100);
			英雄.setPosition(0, 0);
			setX(英雄X - 英雄.getWidth() / 2);
			setSize(英雄.getWidth(), 英雄.getHeight());
		}
		public void 设置血量百分比(float 百分比)
		{
			if (血条 != null)
			{
				血条.set进度(百分比);
			}
		}
		/**
		 * 得到对应英雄图像中心X对应的英雄造型的Position.x
		 */
		private float get目标X对应位置X(float 目标X)
		{
			return 目标X - 英雄.getWidth() / 2;
		}
		/**
		 * 得到对应英雄图像中心Y对应的英雄造型的Position.y
		 */
		private float get目标Y对应位置Y(float 目标Y)
		{
			if (血条 == null)
			{
				return 目标Y - 英雄.getHeight() / 2;
			}
			else
			{
				return 目标Y - 英雄.getHeight() / 2 - 血条.getHeight();
			}
		}
		// 只有英雄旋转，血条不能旋转
		@Override
		public void rotate(float amountInDegrees)
		{
			英雄.rotate(amountInDegrees);
		}
		@Override
		public void setSize(float width, float height)
		{
			if (getWidth() > 0 && 血条 != null)
			{
				float 缩放比例 = width / getWidth();
				英雄.setSize(英雄.getWidth() * 缩放比例, 英雄.getHeight() * 缩放比例);
				血条.setSize(缩放比例 * 血条.getWidth(), 缩放比例 * 血条.getHeight());
				血条.setX((1 - 血条宽度所占英雄宽度比例) * 英雄图片最大宽度 / 2);
				英雄.setY(血条.getHeight());
				if (is正在被沉默)
				{
					沉默图标.setPosition((英雄.getWidth() - 沉默图标.getWidth()) / 2, 英雄.getY() + 英雄.getHeight());
				}
				if (is正在被冰冻)
				{
					冰冻图标.setPosition((英雄.getWidth() - 冰冻图标.getWidth()) / 2, 英雄.getY() + (英雄.getHeight() - 冰冻图标.getHeight()) / 2);
				}
				if (is正在被眩晕)
				{
					眩晕图标.setPosition((英雄.getWidth() - 眩晕图标.getWidth()) / 2, 英雄.getY() + (英雄.getHeight() - 眩晕图标.getHeight()) / 2);
				}
				if (is正在被压制)
				{
					压制图标.setPosition((英雄.getWidth() - 压制图标.getWidth()) / 2, 英雄.getY() + (英雄.getHeight() - 压制图标.getHeight()) / 2);
				}
				if (is正在被嘲讽)
				{
					嘲讽图标.setPosition((英雄.getWidth() - 嘲讽图标.getWidth()) / 2, 英雄.getY() + (英雄.getHeight() - 嘲讽图标.getHeight()) / 2);
				}
				if (is正在被击飞)
				{
					击飞效果.setPosition((英雄.getWidth() - 击飞效果.getWidth()) / 2, 英雄.getY() + (英雄.getHeight() - 击飞效果.getHeight()) / 2);
				}
				if (is正在被致盲)
				{
					致盲图标.setPosition((英雄.getWidth() - 致盲图标.getWidth()) / 2, 英雄.getY() + (英雄.getHeight() - 致盲图标.getHeight()) / 2);
				}
				if (is正在被集火)
				{
					集火效果.setPosition(英雄.getWidth() / 2, 英雄.getY() + 英雄.getHeight() / 2);
				}
			}
			super.setSize(width, height);
		}
		public void set隐身(boolean is隐身)
		{
			if (is隐身 != is正在隐身)
			{
				is正在隐身 = is隐身;
				英雄.setColor(1, 1, 1, is隐身 ? 0.2f : 1);
			}
		}
		public void set沉默(boolean is沉默)
		{
			if (is正在被沉默 != is沉默)
			{
				is正在被沉默 = is沉默;
				if (is沉默)
				{
					addActor(沉默图标);
				}
				else
				{
					沉默图标.remove();
				}
			}
		}
		public void set击飞(boolean is击飞)
		{
			if (is正在被击飞 != is击飞)
			{
				is正在被击飞 = is击飞;
				if (is击飞)
				{
					addActor(击飞叶子效果);
					addActor(击飞效果);
				}
				else
				{
					击飞叶子效果.remove();
					击飞效果.remove();
				}
			}
		}
		public void set嘲讽(boolean is嘲讽)
		{
			if (is正在被嘲讽 != is嘲讽)
			{
				is正在被嘲讽 = is嘲讽;
				if (is嘲讽)
				{
					addActor(嘲讽图标);
				}
				else
				{
					嘲讽图标.remove();
				}
			}
		}
		public void set眩晕(boolean is眩晕)
		{
			if (is正在被眩晕 != is眩晕)
			{
				is正在被眩晕 = is眩晕;
				if (is眩晕)
				{
					addActor(眩晕图标);
				}
				else
				{
					眩晕图标.remove();
				}
			}
		}
		public void set压制(boolean is压制)
		{
			if (is正在被压制 != is压制)
			{
				is正在被压制 = is压制;
				if (is压制)
				{
					addActor(压制图标);
				}
				else
				{
					压制图标.remove();
				}
			}
		}
		public void set冰冻(boolean is冰冻)
		{
			if (is正在被冰冻 != is冰冻)
			{
				is正在被冰冻 = is冰冻;
				if (is冰冻)
				{
					addActor(冰冻图标);
				}
				else
				{
					冰冻图标.remove();
				}
			}
		}
		public void set致盲(boolean is致盲)
		{
			if (is正在被致盲 != is致盲)
			{
				is正在被致盲 = is致盲;
				if (is致盲)
				{
					addActor(致盲图标);
				}
				else
				{
					致盲图标.remove();
				}
			}
		}
		public void set集火(boolean is集火)
		{
			if (is正在被集火 != is集火)
			{
				is正在被集火 = is集火;
				if (is集火)
				{
					addActor(集火效果);
				}
				else
				{
					集火效果.remove();
				}
			}
		}
	}
	private 自适应舞台类 舞台;
	private Skin 皮肤资源;
	private Label 回合数标签;
	// 存储对战的动作队列
	private Queue<动作类> 动作队列 = new LinkedList<动作类>();
	private Queue<动作类> 延迟添加动作队列 = new LinkedList<动作类>();
	private 英雄造型类[] 我方阵容造型数组 = new 英雄造型类[5];
	private 英雄造型类[] 敌方阵容造型数组 = new 英雄造型类[5];
	private boolean is动作已经开始;
	private Image 战场背景;
	private Music 对战背景音乐;
	private Queue<命令类> 命令队列;
	/**
	 * @param 命令队列 用于生成动作队列的命令队列
	 */
	public 对战屏幕类(Queue<命令类> 命令队列)
	{
		this.命令队列 = 命令队列;
	}
	/**
	 * 将服务器传来的命令转为动作
	 */
	private void 命令转换为动作()
	{
		生成双方阵容上场动作();
		while (!命令队列.isEmpty())
		{
			命令队列.poll().生成并添加动作(this);
		}
	}
	public Skin get皮肤资源()
	{
		return 皮肤资源;
	}
	/**
	 * 该方法将更换阵亡英雄的图像
	 * @param 位置 0-4
	 */
	public void 被通知英雄阵亡(boolean is我方, int 位置)
	{
		Drawable 死亡图 = 皮肤资源.getDrawable("Dead");
		if (is我方)
		{
			我方阵容造型数组[位置].设置英雄阵亡图片(死亡图);
		}
		else
		{
			敌方阵容造型数组[位置].设置英雄阵亡图片(死亡图);
			
		}
	}
	/**
	 * @param 英雄所在位置 1-5
	 */
	public 英雄造型类 get英雄造型(boolean is我方, int 英雄所在位置)
	{
		return is我方 ? 我方阵容造型数组[英雄所在位置-1] : 敌方阵容造型数组[英雄所在位置-1];
	}
	/**
	 * 将动作添加到延迟添加队列中，该动作将会在下一个添加动作被调用时添加到那个动作的后面
	 */
	public void 延迟添加动作(动作类 动作)
	{
		延迟添加动作队列.add(动作);
	}
	public void 添加动作(动作类 动作)
	{
		动作队列.add(动作);
		if (!延迟添加动作队列.isEmpty())
		{
			动作队列.addAll(延迟添加动作队列);
			延迟添加动作队列.clear();
		}
	}
	public void 添加演员(Actor 演员)
	{
		舞台.addActor(演员);
	}
	public static class 英雄释放技能后摇命令类 extends 命令类
	{
		private boolean is我方;
		private int 英雄所在位置;
		public 英雄释放技能后摇命令类(boolean is我方, int 英雄所在位置)
		{
			this.is我方 = is我方;
			this.英雄所在位置 = 英雄所在位置;
		}
		@Override
		protected 动作类 生成动作()
		{
			英雄造型类 英雄造型 = 对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置);
			return new 英雄释放技能后摇动作类(英雄造型, 英雄造型.get正常Z序号());
		}
	}
	private static class 英雄释放技能后摇动作类 extends 动作类
	{
		private 保持中心X大小绝对变化Action 变小Action;
		private 英雄造型类 英雄造型;
		private int Z序号;
		private 英雄释放技能后摇动作类(英雄造型类 英雄造型, int Z序号)
		{
			this.英雄造型 = 英雄造型;
			this.Z序号 = Z序号;
			变小Action = new 保持中心X大小绝对变化Action(英雄造型.正常宽, 英雄造型.正常高, 0.2f);
		}
		@Override
		public boolean is完成()
		{
			if (变小Action.isAction已结束())
			{
				英雄造型.setZIndex(Z序号);
				return true;
			}
			else
			{
				return false;
			}
		}
		@Override
		public void 开始()
		{
			英雄造型.addAction(变小Action);
		}
	}
	public static class 英雄释放技能前摇命令类 extends 命令类
	{
		private boolean is我方;
		private int 英雄所在位置;
		private String 英雄皮肤文件名;
		private String 技能文件后缀;
		private String 技能名;
		public 英雄释放技能前摇命令类(boolean is我方, int 英雄所在位置, String 英雄皮肤文件名, String 技能文件后缀, String 技能名)
		{
			this.is我方 = is我方;
			this.英雄所在位置 = 英雄所在位置;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
			this.技能文件后缀 = 技能文件后缀;
			this.技能名 = 技能名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 英雄释放技能前摇动作类(对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置), 英雄类.get技能图标(英雄皮肤文件名, 技能文件后缀), 技能名);
		}
	}
	private static class 英雄释放技能前摇动作类 extends 动作类
	{
		private Group 技能提示 = new Group();
		private Image 背景;
		private Image 图标;
		private Label 标签;
		private 延迟Action 技能提示延迟Action = new 延迟Action(1);
		private 英雄造型类 英雄造型;
		private 保持中心X大小绝对变化Action 变大Action;
		private boolean is已经变大;
		private Music 音效;
		private 英雄释放技能前摇动作类(英雄造型类 英雄造型, TextureRegion 技能图标, String 技能名)
		{
			this.英雄造型 = 英雄造型;
			背景 = new Image(对战屏幕类.实例.皮肤资源.getRegion("SmallSkillBackground"));
			图像计算类.调整演员至不超过指定最大宽(背景, 60 * 6 + 20);
			图标 = new Image(技能图标);
			图标.setSize(60, 60);
			图标.setPosition(40, 0);
			标签 = new Label(技能名, 主类.实例.getUI皮肤资源());
			float 字体缩放比例 = 0.9f;
			标签.setFontScale(字体缩放比例);
			标签.setSize(标签.getWidth() * 字体缩放比例, 标签.getHeight() * 字体缩放比例);
			标签.setPosition(图标.getX() + 图标.getWidth() + 10, 20);
			技能提示.addActor(背景);
			技能提示.addActor(图标);
			技能提示.addActor(标签);
			变大Action = new 保持中心X大小绝对变化Action(英雄造型.getWidth() * 放技能前变大倍数, 英雄造型.getHeight() * 放技能前变大倍数, 0.3f);
			相对移动Action 技能提示上升Action = new 相对移动Action(0, 背景.getHeight(), 0.2f);
			SequenceAction 序列Action = Actions.sequence(技能提示上升Action, 技能提示延迟Action);
			技能提示.addAction(序列Action);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "SkillBefore.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		@Override
		public boolean is完成()
		{
			if (!is已经变大 && 变大Action.isAction已结束())
			{
				对战屏幕类.实例.添加演员(技能提示);
				技能提示.setPosition(英雄造型.getX() + 英雄造型.getWidth() / 2 - 背景.getWidth() / 2, 英雄造型.getHeight() + 英雄造型.getY() - 背景.getHeight());
				is已经变大 = true;
				return false;
			}
			else if (技能提示延迟Action.isAction已结束())
			{
				技能提示.remove();
				return true;
			}
			else
			{
				return false;
			}
		}
		@Override
		public void 开始()
		{
			英雄造型.setZIndex(Integer.MAX_VALUE);
			英雄造型.addAction(变大Action);
			音效.play();
		}
	}
	public static class 英雄释放大招前摇命令类 extends 命令类
	{
		private boolean is我方;
		private int 英雄所在位置;
		private String 英雄皮肤文件名;
		private String 技能名;
		public 英雄释放大招前摇命令类(boolean is我方, int 英雄所在位置, String 英雄皮肤文件名, String 技能名)
		{
			this.is我方 = is我方;
			this.英雄所在位置 = 英雄所在位置;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
			this.技能名 = 技能名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 英雄释放大招前摇动作类(对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置), 英雄类.get英雄图像(英雄皮肤文件名), 技能名);
		}
	}
	private static class 英雄释放大招前摇动作类 extends 动作类
	{
		private 英雄造型类 英雄造型;
		private 保持中心X大小绝对变化Action 变大Action;
		private boolean is已经变大;
		private Image 英雄图像;
		private Image 大招背景;
		private Label 标签;
		private 延迟Action 延迟;
		private Music 音效;
		private 英雄释放大招前摇动作类(英雄造型类 英雄造型, TextureRegion 图像, String 大招名字)
		{
			this.英雄造型 = 英雄造型;
			英雄图像 = new Image(图像);
			图像计算类.调整演员至不超过指定最大高(英雄图像, 英雄图片最大高度 * 1.2f);
			英雄图像.setPosition(-英雄图像.getWidth(), (常量类.屏幕高 - 英雄图像.getHeight()) / 2);
			相对移动Action 英雄出现Action = new 相对移动Action(英雄图像.getWidth(), 0, 0.5f);
			延迟 = new 延迟Action(1);
			SequenceAction 顺序Action = Actions.sequence(英雄出现Action, 延迟);
			英雄图像.addAction(顺序Action);
			大招背景 = new Image(对战屏幕类.实例.皮肤资源.getRegion("BigSkillBackground"));
			图像计算类.调整演员至不超过指定最大宽(大招背景, 常量类.屏幕宽);
			大招背景.setY((常量类.屏幕高 - 大招背景.getHeight()) / 2);
			标签 = new Label(大招名字, 主类.实例.getUI皮肤资源());
			float 字体缩放比例 = 1.6f;
			标签.setFontScale(字体缩放比例);
			标签.setSize(标签.getWidth() * 字体缩放比例, 标签.getHeight() * 字体缩放比例);
			标签.setPosition((常量类.屏幕宽 - 英雄图像.getWidth() - 标签.getWidth()) / 2 + 英雄图像.getWidth(), (常量类.屏幕高 - 标签.getHeight()) / 2);
			变大Action = new 保持中心X大小绝对变化Action(英雄造型.getWidth() * 放技能前变大倍数, 英雄造型.getHeight() * 放技能前变大倍数, 0.3f);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "SkillBefore.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		@Override
		public boolean is完成()
		{
			if (!is已经变大 && 变大Action.isAction已结束())
			{
				对战屏幕类.实例.战场背景.setColor(Color.GRAY);
				对战屏幕类.实例.添加演员(大招背景);
				对战屏幕类.实例.添加演员(标签);
				对战屏幕类.实例.添加演员(英雄图像);
				is已经变大 = true;
				return false;
			}
			else if (延迟.isAction已结束())
			{
				对战屏幕类.实例.战场背景.setColor(Color.WHITE);
				大招背景.remove();
				标签.remove();
				英雄图像.remove();
				return true;
			}
			else
			{
				return false;
			}
		}
		@Override
		public void 开始()
		{
			英雄造型.setZIndex(Integer.MAX_VALUE);
			英雄造型.addAction(变大Action);
			音效.play();
		}
	}
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		if (!动作队列.isEmpty())
		{
			动作类 动作 = 动作队列.peek();
			if (!is动作已经开始)
			{
				动作.开始();
				is动作已经开始 = true;
			}
			else if (动作.is完成())
			{
				动作队列.poll();
				is动作已经开始 = false;
			}
		}
		else if (!延迟添加动作队列.isEmpty())
		{
			动作队列.addAll(延迟添加动作队列);
			延迟添加动作队列.clear();
		}
		舞台.act();
		舞台.draw();
	}
	@Override
	public void show()
	{
		if (舞台 == null)
		{
			实例 = this;
			舞台 = new 自适应舞台类();
			舞台.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					if (动作队列.isEmpty())
					{
						对战背景音乐.stop();
						对战背景音乐.dispose();
						主类.实例.播放背景音乐();
						主类.实例.退出屏幕();
					}
				}
			});
			// 加载纹理图集
			TextureAtlas 纹理图集 = new TextureAtlas(Gdx.files.internal(图片打包文件名));
			// 加载皮肤资源
			皮肤资源 = new Skin(纹理图集);
			// 背景
			战场背景 = new Image(皮肤资源.getRegion("Background"));
			战场背景.setSize(常量类.屏幕宽, 常量类.屏幕高);
			舞台.addActor(战场背景);
			// 回合数标签
			回合数标签 = new Label(null, new LabelStyle(主类.实例.getUI皮肤资源().getFont("default"), Color.WHITE));
			舞台.addActor(回合数标签);
			// 敌方阵容图片数组
			英雄类[] 敌方英雄数组 = 对手信息类.get阵容().get英雄数组();
			// 我方阵容图片数组
			英雄类[] 我方英雄数组 = 玩家信息类.get阵容().get英雄数组();
			for (int i = 0; i < 我方英雄数组.length; i++)
			{
				if (我方英雄数组[i] != null)
				{
					Image 英雄图像 = new Image(英雄类.get英雄图像(我方英雄数组[i].get英雄皮肤文件名()));
					图像计算类.调整演员至不超过指定最大高(英雄图像, 英雄图片最大高度);
					图像计算类.调整演员至不超过指定最大宽(英雄图像, 英雄图片最大宽度);
					英雄图像.setOrigin(英雄图像.getWidth() / 2, 英雄图像.getHeight() / 2);
					我方阵容造型数组[i] = new 英雄造型类(我方X坐标数组[i], 英雄图像, 皮肤资源.getRegion("LifeBarFore"), 皮肤资源.getRegion("LifeBarBack"));
				}
				if (敌方英雄数组[i] != null)
				{
					Image 英雄图像 = new Image(英雄类.get英雄图像(敌方英雄数组[i].get英雄皮肤文件名()));
					图像计算类.调整演员至不超过指定最大高(英雄图像, 英雄图片最大高度);
					图像计算类.调整演员至不超过指定最大宽(英雄图像, 英雄图片最大宽度);
					英雄图像.setOrigin(英雄图像.getWidth() / 2, 英雄图像.getHeight() / 2);
					敌方阵容造型数组[i] = new 英雄造型类(敌方X坐标数组[i], 英雄图像, 皮肤资源.getRegion("LifeBarFore"), 皮肤资源.getRegion("LifeBarBack"));
				}
			}
			命令转换为动作();
			对战背景音乐 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "BattleBackground.mp3"));
			对战背景音乐.setLooping(true);
			对战背景音乐.setVolume(0.15f);
			主类.实例.停止播放背景音乐();
			对战背景音乐.play();
		}
		// 接收用户输入
		Gdx.input.setInputProcessor(舞台);
	}
	public void 设置回合数(int 回合数)
	{
		String 回合文本 = String.valueOf(回合数);
		回合数标签.setText(回合文本);
		TextBounds 文本矩形框 = 主类.实例.getUI皮肤资源().getFont("default").getBounds(回合文本);
		回合数标签.setWidth(文本矩形框.width);
		回合数标签.setHeight(文本矩形框.height);
		回合数标签.setPosition(回合数标签X坐标 - 回合数标签.getWidth() / 2, 回合数标签Y坐标 - 回合数标签.getHeight() / 2);
	}
	private class 我方上场动作类 extends 动作类
	{
		private static final float 移动时间 = 0.5f;
		private static final int 跳动次数 = 10;
		private static final int 上下跳动位移 = 10;
		private ParallelAction 移动加跳动Action;
		private 绝对移动Action 移动Action;
		private 英雄造型类 英雄造型;
		private 我方上场动作类(英雄造型类 英雄造型, float 目标X, float 目标Y)
		{
			this.英雄造型 = 英雄造型;
			MoveByAction 上跳动作 = Actions.moveBy(0, 上下跳动位移, 移动时间 / 跳动次数 / 2);
			MoveByAction 下跳动作 = Actions.moveBy(0, -上下跳动位移, 移动时间 / 跳动次数 / 2);
			SequenceAction 跳动动作 = Actions.sequence(上跳动作, 下跳动作);
			RepeatAction 多次跳动动作 = Actions.repeat(跳动次数, 跳动动作);
			移动Action = new 绝对移动Action(目标X, 目标Y, 移动时间);
			移动加跳动Action = Actions.parallel(移动Action, 多次跳动动作);
		}
		@Override
		public boolean is完成()
		{
			if (移动Action.isAction已结束())
			{
				英雄造型.removeAction(移动加跳动Action);
				return true;
			}
			else
			{
				return false;
			}
		}
		@Override
		public void 开始()
		{
			英雄造型.addAction(移动加跳动Action);
		}
	}
	private class 敌方上场动作类 extends 动作类
	{
		private 绝对移动Action 移动Action;
		private 英雄造型类 英雄图片;
		private 敌方上场动作类(英雄造型类 英雄图片, float 目标X, float 目标Y)
		{
			this.英雄图片 = 英雄图片;
			移动Action = new 绝对移动Action(目标X, 目标Y, 0.3f);
		}
		@Override
		public boolean is完成()
		{
			if (移动Action.isAction已结束())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		@Override
		public void 开始()
		{
			英雄图片.addAction(移动Action);
		}
	}
	private void 生成双方阵容上场动作()
	{
		for (int i = 4; i >= 0; i--)
		{
			if (敌方阵容造型数组[i] != null)
			{
				敌方阵容造型数组[i].setPosition(计算类.随机整数值(常量类.屏幕宽), 常量类.屏幕高);
				动作队列.add(new 敌方上场动作类(敌方阵容造型数组[i], 敌方阵容造型数组[i].get目标X对应位置X(敌方X坐标数组[i]), 敌方阵容造型数组[i].get目标Y对应位置Y(敌方Y坐标数组[i])));
				舞台.addActor(敌方阵容造型数组[i]);
				敌方阵容造型数组[i].保存正常Z序号();
			}
		}
		并行动作类 我方并行上场动作 = new 并行动作类();
		for (int i = 0; i < 5; i++)
		{
			if (我方阵容造型数组[i] != null)
			{
				我方阵容造型数组[i].setPosition(我方阵容造型数组[i].get目标X对应位置X(我方X坐标数组[i]), -我方阵容造型数组[i].getHeight());
				我方并行上场动作.添加动作(new 我方上场动作类(我方阵容造型数组[i], 我方阵容造型数组[i].get目标X对应位置X(我方X坐标数组[i]), 我方阵容造型数组[i].get目标Y对应位置Y(我方Y坐标数组[i])));
				舞台.addActor(我方阵容造型数组[i]);
				我方阵容造型数组[i].保存正常Z序号();
			}
		}
		动作队列.add(我方并行上场动作);
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
