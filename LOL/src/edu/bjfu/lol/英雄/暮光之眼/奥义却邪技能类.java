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
import edu.bjfu.lol.动态.对战中英雄类.被普通攻击触发效果状态类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 奥义却邪技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 9;
	private static final int 技能基础伤害 = 60;
	private static final float 技能附加伤害所占法术强度比例 = 0.6f;
	private static final int 却邪印持续回合数 = 2;
	private static final int 普通攻击基础回复生命值 = 25;
	private static final float 普通攻击附加回复生命值所占最大生命值百分比 = 1.5f / 100;
	private static final int 敌人死亡回复生命值 = 6;
	private static final String 技能名 = "奥义却邪";
	private static final String 技能描述 = String.format("%s  对目标单位造成%d(+%.1f法术强度)魔法伤害,并对目标施加一\n个持续%d回合的却邪印,友军普通攻击该目标会使友军回复%d(+%.1f%%最大\n生命值)生命值,如果敌人死于该技能,会使慎直接回复%d生命值.", 
			技能名
			,技能基础伤害, 技能附加伤害所占法术强度比例, 却邪印持续回合数, 普通攻击基础回复生命值, 普通攻击附加回复生命值所占最大生命值百分比, 敌人死亡回复生命值);
	private static class 被普通攻击回复攻击者生命值状态类 extends 被普通攻击触发效果状态类
	{
		private int 增加生命值;
		public 被普通攻击回复攻击者生命值状态类(int 增加生命值)
		{
			super(却邪印持续回合数);
			this.增加生命值 = 增加生命值;
		}
		@Override
		public void 被普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者)
		{
			并行命令类 并行命令 = new 并行命令类();
			并行命令.添加命令(new 状态文本显示命令类(攻击者.is我方(), 攻击者.get英雄所在位置(), "奥义却邪回复", true));
			攻击者.增加生命值(增加生命值, 并行命令);
			命令组装器类.实例.添加命令(并行命令);
		}
		@Override
		public String get状态描述()
		{
			return "奥义却邪，被普通攻击会回复攻击者生命值";
		}
	}
	public 奥义却邪技能类()
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
		public 释放命令类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			this.发出者x = 发出者x;
			this.发出者y = 发出者y;
			this.接收者x = 接收者x;
			this.接收者y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(发出者x, 发出者y, 接收者x, 接收者y, 英雄皮肤文件名);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 绝对移动Action 移动Action;
		private 粒子演员类 粒子;
		private Music 音效;
		public 释放动作类(float 发出者X, float 发出者Y, float 接收者X, float 接收者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "ShenSkillQ.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者X, 发出者Y);
			移动Action = new 绝对移动Action(接收者X, 接收者Y, 0.5f);
			粒子.addAction(移动Action);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "ShenSkillQ.mp3"));
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
		对战中英雄类 被攻击英雄 = 敌方阵容.get默认被攻击英雄(释放此技能的英雄);
		if (被攻击英雄 != null)
		{
			并行命令类 并行命令 = new 并行命令类();
			if (释放此技能的英雄.is我方())
			{
				并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.敌方X坐标数组[被攻击英雄.get英雄所在位置()-1], 对战屏幕类.敌方Y坐标数组[被攻击英雄.get英雄所在位置()-1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			else
			{
				并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.我方X坐标数组[被攻击英雄.get英雄所在位置()-1], 对战屏幕类.我方Y坐标数组[被攻击英雄.get英雄所在位置()-1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			被攻击英雄.添加被普通攻击触发效果状态(new 被普通攻击回复攻击者生命值状态类(普通攻击基础回复生命值 + (int) (普通攻击附加回复生命值所占最大生命值百分比 * 释放此技能的英雄.get对战中最大生命值())));
			int 造成伤害 = 被攻击英雄.被造成魔法伤害(释放此技能的英雄, (int) (技能基础伤害 + 技能附加伤害所占法术强度比例 * 释放此技能的英雄.get对战中法术强度()), 释放此技能的英雄.get对战中数值法术穿透(), 释放此技能的英雄.get对战中百分比法术穿透(), 并行命令);
			释放此技能的英雄.法术吸血(造成伤害, false);
			if (被攻击英雄.is阵亡())
			{
				Gdx.app.debug("奥义却邪技能类.释放", "敌人死于奥义却邪，回复慎生命值");
				释放此技能的英雄.增加生命值(敌人死亡回复生命值, 并行命令);
			}
			命令组装器类.实例.添加命令(并行命令);
			return true;
		}
		else
		{
			return false;
		}
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
		return "B";
	}
}
