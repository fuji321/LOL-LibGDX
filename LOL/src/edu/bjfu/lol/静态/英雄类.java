package edu.bjfu.lol.静态;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import edu.bjfu.lol.主类;
import edu.bjfu.lol.scenes.scene2d.ui.动画演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.动作.攻击前摇动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.screen.英雄详细屏幕类;
import edu.bjfu.lol.screen.英雄详细屏幕类.技能描述类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;

public abstract class 英雄类 {
	private static final String 头像文件名 = "Portrait";
	private static final String 英雄图像文件名 = "Hero";
	public abstract 英雄属性类 get英雄属性();
	public abstract 技能类[] get技能数组();
	public final void 添加被动状态(对战中英雄类 对战中英雄)
	{
		for (技能类 技能 : get技能数组())
		{
			技能.添加被动状态(对战中英雄);
		}
	}
	public abstract String get英雄名字();
	public abstract String get英雄皮肤文件名();
	public final TextureRegion get英雄头像()
	{
		return 主类.实例.get英雄皮肤资源(get英雄皮肤文件名()).getRegion(头像文件名);
	}
	public static TextureRegion get英雄图像(String 英雄皮肤文件名)
	{
		return 主类.实例.get英雄皮肤资源(英雄皮肤文件名).getRegion(英雄图像文件名);
	}
	public final Music get英雄声音()
	{
		Music 声音 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + get英雄皮肤文件名() + ".mp3"));
		声音.setOnCompletionListener(new 音乐播放完自动销毁类());
		return 声音;
	}
	public static Skin get皮肤资源(String 英雄皮肤文件名)
	{
		return 主类.实例.get英雄皮肤资源(英雄皮肤文件名);
	}
	public static TextureRegion get技能图标(String 英雄皮肤文件名, String 技能文件后缀)
	{
		return 主类.实例.get英雄皮肤资源(英雄皮肤文件名).getRegion("SkillPortrait" + 技能文件后缀);
	}
	public final 技能描述类[] get英雄描述数组(英雄详细屏幕类 英雄详细屏幕)
	{
		技能类[] 技能数组 = get技能数组();
		技能描述类[] 技能描述数组 = new 技能描述类[技能数组.length];
		for (int i = 0 ; i < 技能数组.length; i++)
		{
			int 第一次释放回合数 = 技能数组[i].get第一次释放回合数();
			if (第一次释放回合数 == Integer.MAX_VALUE)
			{
				第一次释放回合数 = 1;
			}
			int 释放间隔回合数 = 技能数组[i].get释放间隔回合数();
			if (释放间隔回合数 == Integer.MAX_VALUE)
			{
				释放间隔回合数 = 1;
			}
			技能描述数组[i] = 英雄详细屏幕.new 技能描述类(技能数组[i].get技能描述(), get技能图标(get英雄皮肤文件名(), 技能数组[i].get技能文件后缀()), 第一次释放回合数, 释放间隔回合数);
		}
		return 技能描述数组;
	}
	private class 近战普通攻击动作类 extends 动作类
	{
		private static final float 普通攻击动画比例 = 0.7f;
		private static final float 总体动画时间 = 攻击前摇动作类.近战总体摆动时间;
		private 动画演员类 发起者动画;
		private 动画演员类 接收者动画;
		private Music 音效;
		/**
		 * @param 音效文件名 不包括声音目录
		 */
		private 近战普通攻击动作类(int 发出者X, int 发出者Y, int 接收者X, int 接收者Y, boolean is奇数次攻击, String 音效文件名)
		{
			TextureRegion[] 发出者图片数组 = new TextureRegion[4];
			for (int i = 0; i < 发出者图片数组.length; i++)
			{
				发出者图片数组[i] = 对战屏幕类.实例.get皮肤资源().getRegion("AttackGiver" + (is奇数次攻击 ? "Odd" : "Even") + (char)(i + 'A'));
			}
			发起者动画 = new 动画演员类(总体动画时间 / 发出者图片数组.length, 发出者图片数组, Animation.NORMAL);
			发起者动画.setWidth(对战屏幕类.英雄图片最大宽度 * 普通攻击动画比例);
			发起者动画.setHeight(对战屏幕类.英雄图片最大高度 * 普通攻击动画比例);
			发起者动画.setPosition(发出者X - 发起者动画.getWidth() / 2, 发出者Y - 发起者动画.getHeight() / 2);
			TextureRegion[] 接收者图片数组 = new TextureRegion[3];
			for (int i = 0; i < 接收者图片数组.length; i++)
			{
				接收者图片数组[i] = 对战屏幕类.实例.get皮肤资源().getRegion("AttackReceiver" + (char)(i + 'A'));
			}
			接收者动画 = new 动画演员类(总体动画时间 / 接收者图片数组.length, 接收者图片数组, Animation.NORMAL);
			接收者动画.setWidth(对战屏幕类.英雄图片最大宽度 * 普通攻击动画比例);
			接收者动画.setHeight(对战屏幕类.英雄图片最大高度 * 普通攻击动画比例);
			接收者动画.setPosition(接收者X - 接收者动画.getWidth() / 2, 接收者Y - 接收者动画.getHeight() / 2);
			this.音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + 音效文件名));
			this.音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		@Override
		public boolean is完成()
		{
			if (发起者动画.is已经结束())
			{
				发起者动画.remove();
				接收者动画.remove();
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
			对战屏幕类.实例.添加演员(发起者动画);
			对战屏幕类.实例.添加演员(接收者动画);
			音效.play();
		}
	}
	private class 普通攻击命令类 extends 命令类
	{
		private int 发出者X;
		private int 发出者Y;
		private int 接收者X;
		private int 接收者Y;
		private boolean is奇数次攻击;
		private String 英雄普攻声音文件名;
		public 普通攻击命令类(int 发出者x, int 发出者y, int 接收者x, int 接收者y, boolean is奇数次攻击, String 英雄普攻声音文件名)
		{
			发出者X = 发出者x;
			发出者Y = 发出者y;
			接收者X = 接收者x;
			接收者Y = 接收者y;
			this.is奇数次攻击 = is奇数次攻击;
			this.英雄普攻声音文件名 = 英雄普攻声音文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 近战普通攻击动作类(发出者X, 发出者Y, 接收者X, 接收者Y, is奇数次攻击, 英雄普攻声音文件名);
		}
	}
	public 命令类 get普通攻击命令(int 发出者X, int 发出者Y, int 接收者X, int 接收者Y, boolean is奇数次攻击, String 英雄皮肤文件名, 对战中英雄类 对战中英雄)
	{
		return new 普通攻击命令类(发出者X, 发出者Y, 接收者X, 接收者Y, is奇数次攻击, get英雄普攻声音文件名(对战中英雄));
	}
	public String get英雄普攻声音文件名(对战中英雄类 对战中英雄){return null;}
	public abstract boolean is近战(对战中英雄类 对战中英雄);
	public String get阵亡音效文件名()
	{
		return get英雄皮肤文件名() + "Dying.mp3";
	}
}