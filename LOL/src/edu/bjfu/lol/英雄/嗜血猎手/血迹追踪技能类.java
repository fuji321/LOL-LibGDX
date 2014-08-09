package edu.bjfu.lol.英雄.嗜血猎手;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
import edu.bjfu.lol.scenes.scene2d.ui.粒子演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.screen.对战屏幕类.英雄造型类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.敌方阵容状态触发本英雄状态类;
import edu.bjfu.lol.动态.对战中英雄类.显隐状态类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 血迹追踪技能类 extends 技能类
{
	private static final float 生命值低于百分比 = 50f / 100;
	private static final float 先手值增加百分比 = 20f / 100;
	private static final int 状态持续回合数 = 2;
	private static final String 技能名 = "血迹追踪";
	private static final String 技能描述 = String.format("%s  沃里克能够感知敌方生命值低于%.0f%%的敌方英雄的行踪.发现\n虚弱英雄后,沃里克增加%.0f%%的先手值,持续%d回合.",
			技能名, 生命值低于百分比 * 100, 先手值增加百分比 * 100, 状态持续回合数);
	private static class 血迹追踪状态类 extends 敌方阵容状态触发本英雄状态类
	{
		private static class 先手值提升状态类 extends 状态类
		{
			public 先手值提升状态类()
			{
				super(状态持续回合数);
			}
			@Override
			public void 作用(对战中英雄类 受状态作用英雄)
			{
				受状态作用英雄.添加先手值百分比变化(先手值增加百分比);
			}
			@Override
			public String get状态描述()
			{
				return "敌方英雄生命值低，沃里克先手值提升";
			}
		}
		private static class 虚弱英雄显隐状态类 extends 显隐状态类
		{
			public 虚弱英雄显隐状态类()
			{
				super(状态持续回合数);
			}
			@Override
			public String get状态描述()
			{
				return "虚弱并被沃里克发现";
			}
		}
		public 血迹追踪状态类()
		{
			super(Integer.MAX_VALUE);
		}
		@Override
		public String get状态描述()
		{
			return 技能描述;
		}
		private class 被发现命令类 extends 命令类
		{
			private String 英雄皮肤文件名;
			private LinkedList<Vector2> 位置链表 = new LinkedList<Vector2>();
			public 被发现命令类(String 英雄皮肤文件名, LinkedList<对战中英雄类> 被发现英雄链表)
			{
				this.英雄皮肤文件名 = 英雄皮肤文件名;
				for (对战中英雄类 英雄 : 被发现英雄链表)
				{
					if (英雄.is我方())
					{
						位置链表.add(new Vector2(对战屏幕类.我方X坐标数组[英雄.get英雄所在位置()-1], 对战屏幕类.我方Y坐标数组[英雄.get英雄所在位置()-1]));
					}
					else
					{
						位置链表.add(new Vector2(对战屏幕类.敌方X坐标数组[英雄.get英雄所在位置()-1], 对战屏幕类.敌方Y坐标数组[英雄.get英雄所在位置()-1]));
					}
				}
			}
			@Override
			protected 动作类 生成动作()
			{
				return new 被发现动作类(位置链表, 英雄皮肤文件名);
			}
		}
		private class 被发现动作类 extends 动作类
		{
			private 延迟Action 延迟 = new 延迟Action(1);
			private 粒子演员类[] 粒子数组;
			private 被发现动作类(LinkedList<Vector2> 位置链表, String 英雄皮肤文件名)
			{
				粒子数组 = new 粒子演员类[位置链表.size()];
				ParticleEffect 粒子效果 = new ParticleEffect();
				粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "WarwickSkillE.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
				for (int i = 0; i < 粒子数组.length; i++)
				{
					粒子数组[i] = new 粒子演员类(粒子效果);
					Vector2 位置 = 位置链表.get(i);
					粒子数组[i].setPosition(位置.x, 位置.y);
				}
				粒子数组[0].addAction(延迟);
			}
			@Override
			public boolean is完成()
			{
				if (延迟.isAction已结束())
				{
					for (粒子演员类 粒子 : 粒子数组)
					{
						粒子.remove();
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
			}
		}
		private class 释放命令类 extends 命令类
		{
			private boolean is我方;
			private int 英雄所在位置;
			public 释放命令类(boolean is我方, int 英雄所在位置)
			{
				this.is我方 = is我方;
				this.英雄所在位置 = 英雄所在位置;
			}
			@Override
			protected 动作类 生成动作()
			{
				return new 释放动作类(is我方, 英雄所在位置);
			}
		}
		private class 释放动作类 extends 动作类
		{
			private 延迟Action 延迟 = new 延迟Action(1);
			private 英雄造型类 英雄造型;
			private Music 音效;
			private 释放动作类(boolean is我方, int 英雄所在位置)
			{
				this.英雄造型 = 对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置);
				音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "WarwickSkillE.mp3"));
				音效.setOnCompletionListener(new 音乐播放完自动销毁类());
			}
			@Override
			public boolean is完成()
			{
				if (延迟.isAction已结束())
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
				英雄造型.addAction(延迟);
				音效.play();
			}
		}
		@Override
		public void 被通知敌方阵容状态(对战中英雄类 被通知英雄, 对战中阵容类 敌方阵容)
		{
			boolean 有新的敌人虚弱 = false;
			boolean 存在敌人虚弱 = false;
			并行命令类 并行命令 = new 并行命令类();
			LinkedList<对战中英雄类> 被发现英雄链表 = new LinkedList<对战中英雄类>();
			for (对战中英雄类 敌方英雄 : 敌方阵容.get对战中英雄数组())
			{
				if (敌方英雄 != null && !敌方英雄.is阵亡())
				{
					if (敌方英雄.get对战中生命值() * 1.0f / 敌方英雄.get对战中最大生命值() < 生命值低于百分比)
					{
						存在敌人虚弱 = true;
						状态类 显隐状态 = 敌方英雄.get显隐状态(虚弱英雄显隐状态类.class);
						if (显隐状态 == null)
						{
							敌方英雄.添加显隐状态(new 虚弱英雄显隐状态类(), 并行命令);	
							被发现英雄链表.add(敌方英雄);
							有新的敌人虚弱 = true;
						}
						else
						{
							显隐状态.重置状态回合数();
						}
						状态类 先手值提升状态 = 被通知英雄.get先手值增加状态(先手值提升状态类.class);
						if (先手值提升状态 == null)
						{
							被通知英雄.添加先手值增加状态(new 先手值提升状态类());
						}
						else
						{
							先手值提升状态.重置状态回合数();
						}
					}
					else
					{
						敌方英雄.移除显隐状态(虚弱英雄显隐状态类.class, 并行命令);
					}
				}
			}
			if (!存在敌人虚弱)
			{
				被通知英雄.移除先手值增加状态(先手值提升状态类.class);
			}
			if (有新的敌人虚弱)
			{
				并行命令.添加命令(new 被发现命令类(被通知英雄.get英雄().get英雄皮肤文件名(), 被发现英雄链表));
				并行命令.添加命令(new 释放命令类(被通知英雄.is我方(), 被通知英雄.get英雄所在位置()));
				并行命令.添加命令(new 状态文本显示命令类(被通知英雄.is我方(), 被通知英雄.get英雄所在位置(), "先手值提升", true));
				命令组装器类.实例.添加命令(并行命令);
			}
		}
	}
	public 血迹追踪技能类()
	{
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		对战中英雄.添加敌方阵容状态触发本英雄状态(new 血迹追踪状态类());
	}
	@Override
	public String get技能描述()
	{
		return 技能描述;
	}
	@Override
	public String get技能名()
	{
		return 技能名;
	}
	@Override
	public String get技能文件后缀()
	{
		return "D";
	}
}
