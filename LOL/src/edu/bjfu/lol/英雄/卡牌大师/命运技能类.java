package edu.bjfu.lol.英雄.卡牌大师;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

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
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 命运技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 3;
	private static final int 释放间隔回合数 = 15;
	private static final int 攻击后排状态持续回合数 = 2;
	private static final int 被集火持续回合数 = 1;
	private static final String 技能名 = "命运";
	private static final String 技能描述 = String.format("%s  显示敌方所有英雄的位置,持续%d回合,并自身获得攻击敌方后排的\n能力,持续%d回合,但会被集火%d回合.",
			技能名
			,攻击后排状态持续回合数, 攻击后排状态持续回合数, 被集火持续回合数);
	private static class 显隐状态类 extends 对战中英雄类.显隐状态类
	{
		public 显隐状态类()
		{
			super(攻击后排状态持续回合数);
		}
		@Override
		public String get状态描述()
		{
			return "命运，显隐";
		}
	}
	public 命运技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private float 接收者X;
		private float 接收者Y;
		private String 英雄皮肤文件名;
		private boolean is有音效;
		public 释放命令类(float 接收者x, float 接收者y, String 英雄皮肤文件名, boolean is有音效)
		{
			接收者X = 接收者x;
			接收者Y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
			this.is有音效 = is有音效;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(接收者X, 接收者Y, 英雄皮肤文件名, is有音效);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(0.85f);
		private 粒子演员类 粒子;
		private Music 音效;
		private 释放动作类(float 接收者X, float 接收者Y, String 英雄皮肤文件名, boolean is有音效)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "TwistedFateSkillR.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(接收者X, 接收者Y);
			粒子.addAction(延迟);
			if (is有音效)
			{
				音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "TwistedFateSkillR.mp3"));
				音效.setOnCompletionListener(new 音乐播放完自动销毁类());
			}
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
			if (音效 != null)
			{
				音效.play();
			}
		}
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		并行命令类 并行命令 = new 并行命令类();
		boolean is有音效 = true;
		for (对战中英雄类 敌方英雄 : 敌方阵容.get对战中英雄数组())
		{
			if (敌方英雄 != null && !敌方英雄.is阵亡())
			{
				并行命令.添加命令(new 状态文本显示命令类(敌方英雄.is我方(), 敌方英雄.get英雄所在位置(), "显隐", false));
				if (敌方英雄.is我方())
				{
					并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[敌方英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[敌方英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名(), is有音效));
				}
				else
				{
					并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[敌方英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[敌方英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名(), is有音效));
				}
				敌方英雄.添加显隐状态(new 显隐状态类(), 并行命令);
				is有音效 = false;
			}
		}
		并行命令.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "获得攻击后排能力", true));
		释放此技能的英雄.获得攻击后排能力(攻击后排状态持续回合数);
		释放此技能的英雄.被集火(被集火持续回合数, 并行命令);
		命令组装器类.实例.添加命令(并行命令);
		命令组装器类.实例.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "被集火", false));
		return true;
	}
	@Override
	public String get技能描述()
	{
		return 技能描述;
	}
	@Override
	public String get技能名()
	{
		return "命运";
	}
	@Override
	public String get技能文件后缀()
	{
		return "D";
	}
	@Override
	public boolean is大招()
	{
		return true;
	}
}
