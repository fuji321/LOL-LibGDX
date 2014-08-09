package edu.bjfu.lol.英雄.寒冰射手;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import edu.bjfu.lol.scenes.scene2d.actions.绝对移动Action;
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
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 魔法水晶箭技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 2;
	private static final int 释放间隔回合数 = 10;
	private static final int 技能基础伤害 = 250;
	private static final float 命中率 = 70f / 100;
	private static final float 技能附加伤害所占法术强度比例 = 1.0f;
	private static final int 眩晕回合数 = 2;
	private static final float 减少先手值百分比 = 50f / 100;
	private static final int 先手值减少持续回合数 = 1;
	private static final String 技能名 = "魔法水晶箭";
	private static final String 技能描述 = String.format("%s  艾希向前方射出魔法水晶箭,命中率%.0f%%,击中敌方英雄时造\n成%d(+%.1f法术强度)魔法伤害,并眩晕此目标,眩晕%d回合,同时对该敌人\n周围其他敌人造成一半伤害,并减少先手值%.0f%%,持续%d回合.",
			技能名
			,命中率 * 100, 技能基础伤害, 技能附加伤害所占法术强度比例, 眩晕回合数, 减少先手值百分比 * 100, 先手值减少持续回合数);
	private static class 先手值降低状态类 extends 状态类
	{
		public 先手值降低状态类()
		{
			super(先手值减少持续回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			受状态作用英雄.添加先手值百分比变化(-减少先手值百分比);
		}
		@Override
		public String get状态描述()
		{
			return "受魔法水晶箭溅射效果先手值降低";
		}
	}
	public 魔法水晶箭技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private float 发出者x;
		private float 发出者y;
		private float 接收者x;
		private float 接收者y;
		private String 英雄皮肤文件名;
		private boolean is命中;
		public 释放命令类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名, boolean is命中)
		{
			this.发出者x = 发出者x;
			this.发出者y = 发出者y;
			this.接收者x = 接收者x;
			this.接收者y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
			this.is命中 = is命中;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(发出者x, 发出者y, 接收者x, 接收者y, 英雄皮肤文件名, is命中);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 绝对移动Action 移动Action;
		private 粒子演员类 粒子;
		private Music 音效;
		public 释放动作类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名, boolean is命中)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "AsheSkillR.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			float 角度 = (float)(Math.atan((接收者y-发出者y)/(接收者x-发出者x)) / Math.PI * 180) - 90;
			if (接收者x < 发出者x)
			{
				角度 += 180;
			}
			粒子效果.findEmitter("Untitled").getRotation().setHigh(角度);
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者x, 发出者y);
			移动Action = new 绝对移动Action(接收者x, 接收者y, 0.5f);
			粒子.addAction(移动Action);
			if (is命中)
			{
				音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "AsheSkillR.mp3"));
			}
			else
			{
				音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "AsheSkillRMiss.mp3"));
			}
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		@Override
		public boolean is完成()
		{
			if (移动Action.isAction已结束())
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
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		if (计算类.根据_概率_计算是否发生(命中率))
		{
			对战中英雄类 被攻击英雄 = 敌方阵容.get默认被攻击英雄(释放此技能的英雄);
			if (被攻击英雄 != null)
			{
				if (释放此技能的英雄.is我方())
				{
					命令组装器类.实例.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.敌方X坐标数组[被攻击英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[被攻击英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名(), true));
				}
				else
				{
					命令组装器类.实例.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.我方X坐标数组[被攻击英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[被攻击英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名(), true));	
				}
				int 技能伤害值 = (int) (技能基础伤害 + 技能附加伤害所占法术强度比例 * 释放此技能的英雄.get对战中法术强度());
				并行命令类 并行命令 = new 并行命令类();
				被攻击英雄.被眩晕(眩晕回合数, 并行命令);
				int 总体造成伤害 = 0;
				int 数值穿透 = 释放此技能的英雄.get对战中数值法术穿透();
				float 百分比穿透 = 释放此技能的英雄.get对战中百分比法术穿透();
				for (对战中英雄类 被溅射英雄 : 敌方阵容.get默认被攻击小范围英雄数组(释放此技能的英雄))
				{
					if (被溅射英雄 != null && !被溅射英雄.is阵亡())
					{
						被溅射英雄.添加先手值减少状态(new 先手值降低状态类());
						并行命令.添加命令(new 状态文本显示命令类(被溅射英雄.is我方(), 被溅射英雄.get英雄所在位置(), "先手值↓", false));
						if (被溅射英雄 == 被攻击英雄)
						{
							总体造成伤害 += 被溅射英雄.被造成魔法伤害(释放此技能的英雄, 技能伤害值, 数值穿透, 百分比穿透, 并行命令);
						}
						else
						{
							总体造成伤害 += 被溅射英雄.被造成魔法伤害(释放此技能的英雄, (int) (技能伤害值 * 0.5f), 数值穿透, 百分比穿透, 并行命令);
						}
					}
				}
				命令组装器类.实例.添加命令(并行命令);
				释放此技能的英雄.法术吸血(总体造成伤害, true);
			}
			else
			{
				if (释放此技能的英雄.is我方())
				{
					命令组装器类.实例.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 常量类.屏幕宽 / 2, 常量类.屏幕高, 释放此技能的英雄.get英雄().get英雄皮肤文件名(), false));
				}
				else
				{
					命令组装器类.实例.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 常量类.屏幕宽 / 2, 0, 释放此技能的英雄.get英雄().get英雄皮肤文件名(), false));
				}
			}
		}
		else
		{
			if (释放此技能的英雄.is我方())
			{
				命令组装器类.实例.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 常量类.屏幕宽 / 2, 常量类.屏幕高, 释放此技能的英雄.get英雄().get英雄皮肤文件名(), false));
			}
			else
			{
				命令组装器类.实例.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 常量类.屏幕宽 / 2, 0, 释放此技能的英雄.get英雄().get英雄皮肤文件名(), false));
			}
		}
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
		return 技能名;
	}
	@Override
	public String get技能文件后缀()
	{
		return "E";
	}
	@Override
	public boolean is大招()
	{
		return true;
	}
}
