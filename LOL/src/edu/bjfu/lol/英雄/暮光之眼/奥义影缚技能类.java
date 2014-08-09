package edu.bjfu.lol.英雄.暮光之眼;

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

class 奥义影缚技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 8;
	private static final int 技能基础伤害 = 50;
	private static final float 技能附加伤害所占法术强度比例 = 0.5f;
	private static final float 命中率 = 70f / 100;
	private static final float 伤害减免百分比 = 50f / 100;
	private static final int 效果持续回合数 = 1;
	private static final String 技能名 = "奥义影缚";
	private static final String 技能描述 = String.format("%s  慎向目标区域猛冲,对敌方小范围造成%d(+%.1f法术强度)魔法\n伤害,并对敌人进行嘲讽,命中率%.0f%%,自身被集火,慎获得%.0f%%伤害减免,\n效果持续%d回合.", 
			技能名
			,技能基础伤害, 技能附加伤害所占法术强度比例, 命中率 * 100, 伤害减免百分比 * 100, 效果持续回合数);
	private static class 伤害减免效果状态类 extends 状态类
	{
		public 伤害减免效果状态类()
		{
			super(效果持续回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			受状态作用英雄.添加伤害百分比减免(伤害减免百分比);
		}
		@Override
		public String get状态描述()
		{
			return "奥义影缚，伤害减免";
		}
	}
	public 奥义影缚技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private float 发出者x;
		private float 发出者y;
		private boolean is我方释放;
		private String 英雄皮肤文件名;
		public 释放命令类(float 发出者x, float 发出者y, boolean is我方释放, String 英雄皮肤文件名)
		{
			this.发出者x = 发出者x;
			this.发出者y = 发出者y;
			this.is我方释放 = is我方释放;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(发出者x, 发出者y, is我方释放, 英雄皮肤文件名);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 绝对移动Action 移动Action;
		private 粒子演员类 粒子;
		private Music 音效;
		public 释放动作类(float 发出者X, float 发出者Y, boolean is我方释放, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			float 接收者X = 常量类.屏幕宽 / 2;
			float 接收者Y = 0;
			if (is我方释放)
			{
				接收者Y = 常量类.屏幕高;
			}
			if (接收者X > 发出者X)
			{
				粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "ShenSkillER.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			}
			else
			{
				粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "ShenSkillEL.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			}
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者X, 发出者Y);
			移动Action = new 绝对移动Action(接收者X, 接收者Y, 0.6f);
			粒子.addAction(移动Action);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "ShenSkillE.mp3"));
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
		释放此技能的英雄.添加伤害减免状态(new 伤害减免效果状态类());
		并行命令类 并行命令 = new 并行命令类();
		并行命令.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "伤害减免", true));
		int 技能伤害 = (int) (技能基础伤害 + 技能附加伤害所占法术强度比例 * 释放此技能的英雄.get对战中法术强度());
		int 造成伤害 = 0;
		boolean is命中 = false;
		int 数值穿透 = 释放此技能的英雄.get对战中数值法术穿透();
		float 百分比穿透 = 释放此技能的英雄.get对战中百分比法术穿透();
		for (对战中英雄类 敌方英雄 : 敌方阵容.get默认被攻击小范围英雄数组(释放此技能的英雄))
		{
			if (敌方英雄 != null && !敌方英雄.is阵亡())
			{
				if (计算类.根据_概率_计算是否发生(命中率))
				{
					敌方英雄.被嘲讽(效果持续回合数,  并行命令);
					造成伤害 += 敌方英雄.被造成魔法伤害(释放此技能的英雄, 技能伤害, 数值穿透, 百分比穿透, 并行命令);
					is命中 = true;
				}
			}
		}
		if (is命中)
		{
			释放此技能的英雄.被集火(效果持续回合数, 并行命令);
		}
		if (释放此技能的英雄.is我方())
		{
			并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置()-1], true, 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		else
		{
			并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置()-1], false, 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		命令组装器类.实例.添加命令(并行命令);
		if (is命中)
		{
			释放此技能的英雄.法术吸血(造成伤害, true);
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
		return "D";
	}
}
