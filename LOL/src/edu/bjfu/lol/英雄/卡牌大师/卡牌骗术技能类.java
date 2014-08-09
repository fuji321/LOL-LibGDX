package edu.bjfu.lol.英雄.卡牌大师;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
import edu.bjfu.lol.scenes.scene2d.ui.粒子演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.普通攻击触发效果状态类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 卡牌骗术技能类 extends 技能类
{
	private static final int 每几次攻击 = 4;
	private static final int 技能基础伤害 = 55;
	private static final float 技能附加伤害所占法术强度比例 = 0.4f;
	private static final float 攻速提高百分比 = 10f / 100;
	private static final String 技能名 = "卡牌骗术";
	private static final String 技能描述 = String.format("%s  每%d次攻击,崔斯特造成额外的%d(+%.1f法术强度)魔法伤害.\n此外,他的攻击速度增加%.0f%%.",
			技能名, 每几次攻击, 技能基础伤害, 技能附加伤害所占法术强度比例, 攻速提高百分比 * 100);
	private static class 普通攻击状态类 extends 普通攻击触发效果状态类
	{
		private static final String 状态描述 = String.format("卡牌骗术被动，每%d次攻击，崔斯特造成额外魔法伤害。", 每几次攻击);
		public 普通攻击状态类()
		{
			super(Integer.MAX_VALUE, 每几次攻击, 0);
		}
		private class 准备好命令类 extends 命令类
		{
			private float 发出者X;
			private float 发出者Y;
			private String 英雄皮肤文件名;
			public 准备好命令类(float 发出者x, float 发出者y, String 英雄皮肤文件名)
			{
				发出者X = 发出者x;
				发出者Y = 发出者y;
				this.英雄皮肤文件名 = 英雄皮肤文件名;
			}
			@Override
			protected 动作类 生成动作()
			{
				return new 准备好动作类(发出者X, 发出者Y, 英雄皮肤文件名);
			}
		}
		private class 准备好动作类 extends 动作类
		{
			private 延迟Action 延迟 = new 延迟Action(1f);
			private 粒子演员类 粒子;
			private Music 音效;
			private 准备好动作类(float 发出者X, float 发出者Y, String 英雄皮肤文件名)
			{
				ParticleEffect 粒子效果 = new ParticleEffect();
				粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "TwistedFateSkillE.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
				粒子 = new 粒子演员类(粒子效果);
				粒子.setPosition(发出者X, 发出者Y);
				粒子.addAction(延迟);
				音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "TwistedFateSkillEReady.mp3"));
				音效.setOnCompletionListener(new 音乐播放完自动销毁类());
			}
			public boolean is完成()
			{
				if (延迟.isAction已结束())
				{
					粒子.remove();
					粒子.dispose();
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
				对战屏幕类.实例.添加演员(粒子);
				音效.play();
			}
		}
		private static class 释放命令类 extends 命令类
		{
			private float 接收者x;
			private float 接收者y;
			private String 英雄皮肤文件名;
			public 释放命令类(float 接收者x, float 接收者y, String 英雄皮肤文件名)
			{
				this.接收者x = 接收者x;
				this.接收者y = 接收者y;
				this.英雄皮肤文件名 = 英雄皮肤文件名;
			}
			@Override
			protected 动作类 生成动作()
			{
				return new 释放动作类(接收者x, 接收者y, 英雄皮肤文件名);
			}
		}
		private static class 释放动作类 extends 动作类
		{
			private static final int 效果数 = 2;
			private 延迟Action[] 延迟数组 = new 延迟Action[效果数];
			private 粒子演员类[] 粒子数组 = new 粒子演员类[效果数];
			private Music 音效;
			private 释放动作类(float 接收者X, float 接收者Y, String 英雄皮肤文件名)
			{
				for (int i = 0; i < 效果数; i++)
				{
					延迟数组[i] = new 延迟Action(0.8f);
					ParticleEffect 粒子效果 = new ParticleEffect();
					粒子效果.loadEmitters(Gdx.files.internal(常量类.粒子目录 + "TwistedFateSkillEEffect.p"));
					char 随机字母 = (char) (计算类.随机整数值(8) + 'A');
					ParticleEmitter 粒子喷射 = 粒子效果.findEmitter("Untitled");
					粒子喷射.setImagePath("SkillEEffect" + 随机字母 + ".png");
					粒子效果.loadEmitterImages(英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
					粒子数组[i] = new 粒子演员类(粒子效果);
					粒子数组[i].setPosition(接收者X, 接收者Y);
					粒子数组[i].addAction(延迟数组[i]);
				}
				音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "TwistedFateSkillE.mp3"));
				音效.setOnCompletionListener(new 音乐播放完自动销毁类());
			}
			public boolean is完成()
			{
				if (延迟数组[0].isAction已结束())
				{
					for (粒子演员类 粒子 : 粒子数组)
					{
						粒子.remove();
						粒子.dispose();
					}
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
				for (粒子演员类 粒子 : 粒子数组)
				{
					对战屏幕类.实例.添加演员(粒子);
				}
				音效.play();
			}
		}
		@Override
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器)
		{
			增加一层(false);
			if (get当前层数() == 每几次攻击 - 1)
			{
				if (攻击者.is我方())
				{
					命令组装器类.实例.添加命令(new 准备好命令类(对战屏幕类.我方X坐标数组[攻击者.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[攻击者.get英雄所在位置() - 1], 攻击者.get英雄().get英雄皮肤文件名()));
				}
				else
				{
					命令组装器类.实例.添加命令(new 准备好命令类(对战屏幕类.敌方X坐标数组[攻击者.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[攻击者.get英雄所在位置() - 1], 攻击者.get英雄().get英雄皮肤文件名()));
				}
			}
			else if (get当前层数() == 每几次攻击)
			{
				层数清零();
				Gdx.app.debug("卡牌骗术技能类.普通攻击状态类.普通攻击", "触发卡牌骗术");
				并行命令类 并行命令 = new 并行命令类();
				if (被攻击者.is我方())
				{
					并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[被攻击者.get英雄所在位置()-1], 对战屏幕类.我方Y坐标数组[被攻击者.get英雄所在位置()-1], 攻击者.get英雄().get英雄皮肤文件名()));
				}
				else
				{
					并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[被攻击者.get英雄所在位置()-1], 对战屏幕类.敌方Y坐标数组[被攻击者.get英雄所在位置()-1], 攻击者.get英雄().get英雄皮肤文件名()));
				}
				被攻击者.被造成魔法伤害(攻击者, (int) (技能基础伤害 + 技能附加伤害所占法术强度比例 * 攻击者.get对战中法术强度()), 攻击者.get对战中数值法术穿透(), 攻击者.get对战中百分比法术穿透(), 并行命令);
				命令组装器类.实例.添加命令(并行命令);
			}
		}
		@Override
		public String get状态描述()
		{
			return 状态描述;
		}
	}
	private static class 攻速提高状态类 extends 状态类
	{
		public 攻速提高状态类()
		{
			super(Integer.MAX_VALUE);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			受状态作用英雄.添加攻击速度百分比变化(攻速提高百分比);
		}
		@Override
		public String get状态描述()
		{
			return "卡牌骗术被动，他的攻击速度增加";
		}
	}
	public 卡牌骗术技能类()
	{
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	@Override
	public String get技能描述()
	{
		return 技能描述;
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		命令组装器类.实例.添加命令(new 状态文本显示命令类(对战中英雄.is我方(), 对战中英雄.get英雄所在位置(), "攻速↑", true));
		对战中英雄.添加普通攻击触发效果状态(new 普通攻击状态类());
		对战中英雄.添加攻击速度增加状态(new 攻速提高状态类());
	}
	@Override
	public String get技能名()
	{
		return 技能名;
	}
	@Override
	public String get技能文件后缀()
	{
		return "C";
	}
}
