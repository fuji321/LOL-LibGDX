package edu.bjfu.lol.英雄.九尾妖狐;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
import edu.bjfu.lol.scenes.scene2d.actions.绝对移动Action;
import edu.bjfu.lol.scenes.scene2d.ui.粒子演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.英雄.九尾妖狐.摄魂夺魄技能类.摄魂夺魄状态类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 欺诈宝珠技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 4;
	private static final int 基础伤害 = 40;
	private static final float 附加伤害所占法术强度比例 = 0.33f;
	private static final String 技能名 = "欺诈宝珠";
	private static final String 技能描述 = String.format("%s  阿狸放出宝珠,对敌方竖排造成%d(+%.2f法术强度)魔法伤害,\n随后将其收回,造成%d(+%.2f法术强度)真实伤害.", 
			技能名
			,基础伤害, 附加伤害所占法术强度比例, 基础伤害, 附加伤害所占法术强度比例);
	private class 欺诈宝珠命令类 extends 命令类
	{
		private float 发出者x;
		private float 发出者y;
		private float 接收者x;
		private float 接收者y;
		private String 英雄皮肤文件名;
		private boolean is技能放出;
		private boolean is技能收回;
		public 欺诈宝珠命令类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名, boolean is技能放出, boolean is技能收回)
		{
			this.发出者x = 发出者x;
			this.发出者y = 发出者y;
			this.接收者x = 接收者x;
			this.接收者y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
			this.is技能放出 = is技能放出;
			this.is技能收回 = is技能收回;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 欺诈宝珠动作类(发出者x, 发出者y, 接收者x, 接收者y, 英雄皮肤文件名, is技能放出, is技能收回);
		}
	}
	private class 欺诈宝珠动作类 extends 动作类
	{
		private 粒子演员类 粒子;
		private 绝对移动Action 移动Action;
		private SequenceAction 序列Action;
		private Music 音效;
		private boolean is技能收回;
		private 欺诈宝珠动作类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名, boolean is技能放出, boolean is技能收回)
		{
			this.is技能收回 = is技能收回;
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "AhriSkillQ.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者x, 发出者y);
			移动Action = new 绝对移动Action(接收者x, 接收者y, .5f);
			if (is技能放出)
			{
				音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "AhriSkillQ.mp3"));
				音效.setOnCompletionListener(new 音乐播放完自动销毁类());
			}
			if (is技能收回)
			{
				延迟Action 延迟 = new 延迟Action(0.2f);
				序列Action = Actions.sequence(移动Action, 延迟);
			}
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
			if (is技能收回)
			{
				粒子.addAction(序列Action);
			}
			else
			{
				粒子.addAction(移动Action);
			}
			对战屏幕类.实例.添加演员(粒子);
			if (音效 != null)
			{
				音效.play();
			}
		}
	}
	public 欺诈宝珠技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		int 技能伤害 = (int) (基础伤害 + 附加伤害所占法术强度比例 * 释放此技能的英雄.get对战中法术强度());
		int 总体魔法伤害 = 0;
		int 数值穿透 = 释放此技能的英雄.get对战中数值法术穿透();
		float 百分比穿透 = 释放此技能的英雄.get对战中百分比法术穿透();
		对战中英雄类[] 竖排被攻击英雄数组 = 敌方阵容.get竖排被攻击英雄数组(释放此技能的英雄);
		LinkedList<对战中英雄类> 被攻击敌人链表 = new LinkedList<对战中英雄类>();
		for (int i = 0; i < 竖排被攻击英雄数组.length; i++)
		{
			对战中英雄类 敌人 = 竖排被攻击英雄数组[i];
			if (敌人 != null && !敌人.is阵亡())
			{
				被攻击敌人链表.add(敌人);
			}
		}
		float 发出位置X = 0;
		float 发出位置Y = 0;
		float 一倍接收位置X = 0;
		float 一倍接收位置Y = 0;
		if (释放此技能的英雄.is我方())
		{
			发出位置X = 对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1];
			发出位置Y = 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1];
			if (被攻击敌人链表.isEmpty())
			{
				一倍接收位置X = 常量类.屏幕宽 / 2;
				一倍接收位置Y = 常量类.屏幕高 * 2f / 3;
			}
			else
			{
				for (对战中英雄类 英雄 : 被攻击敌人链表)
				{
					一倍接收位置X += 对战屏幕类.敌方X坐标数组[英雄.get英雄所在位置() - 1];
					一倍接收位置Y += 对战屏幕类.敌方Y坐标数组[英雄.get英雄所在位置() - 1];
				}
				一倍接收位置X /= 被攻击敌人链表.size();
				一倍接收位置Y /= 被攻击敌人链表.size();
			}
		}
		else
		{
			发出位置X = 对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1];
			发出位置Y = 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1];
			if (被攻击敌人链表.isEmpty())
			{
				一倍接收位置X = 常量类.屏幕宽 / 2;
				一倍接收位置Y = 常量类.屏幕高 * 1f / 3;
			}
			else
			{
				for (对战中英雄类 英雄 : 被攻击敌人链表)
				{
					一倍接收位置X += 对战屏幕类.我方X坐标数组[英雄.get英雄所在位置() - 1];
					一倍接收位置Y += 对战屏幕类.我方Y坐标数组[英雄.get英雄所在位置() - 1];
				}
				一倍接收位置X /= 被攻击敌人链表.size();
				一倍接收位置Y /= 被攻击敌人链表.size();
			}
		}
		float 二倍接收位置X = (一倍接收位置X - 发出位置X) * 2 + 发出位置X;
		float 二倍接收位置Y = (一倍接收位置Y - 发出位置Y) * 2 + 发出位置Y;
		命令组装器类.实例.添加命令(new 欺诈宝珠命令类(发出位置X, 发出位置Y, 一倍接收位置X, 一倍接收位置Y, 释放此技能的英雄.get英雄().get英雄皮肤文件名(), true, false));
		并行命令类 并行命令 = new 并行命令类();
		并行命令.添加命令(new 欺诈宝珠命令类(一倍接收位置X, 一倍接收位置Y, 二倍接收位置X, 二倍接收位置Y, 释放此技能的英雄.get英雄().get英雄皮肤文件名(), false, false));
		for (int i = 0; i < 竖排被攻击英雄数组.length; i++)
		{
			对战中英雄类 敌人 = 竖排被攻击英雄数组[i];
			if (敌人 != null && !敌人.is阵亡())
			{
				总体魔法伤害 += 敌人.被造成魔法伤害(释放此技能的英雄, 技能伤害, 数值穿透, 百分比穿透, 并行命令);
			}
		}
		命令组装器类.实例.添加命令(并行命令);
		摄魂夺魄状态类.法术吸血(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), 释放此技能的英雄, 1, 总体魔法伤害, true);
		命令组装器类.实例.添加命令(new 欺诈宝珠命令类(二倍接收位置X, 二倍接收位置Y, 一倍接收位置X, 一倍接收位置Y, 释放此技能的英雄.get英雄().get英雄皮肤文件名(), false, false));
		并行命令 = new 并行命令类();
		并行命令.添加命令(new 欺诈宝珠命令类(一倍接收位置X, 一倍接收位置Y, 发出位置X, 发出位置Y, 释放此技能的英雄.get英雄().get英雄皮肤文件名(), false, true));
		for (int i = 竖排被攻击英雄数组.length - 1; i >= 0; i--)
		{
			对战中英雄类 敌人 = 竖排被攻击英雄数组[i];
			if (敌人 != null && !敌人.is阵亡())
			{
				敌人.被造成真实伤害(释放此技能的英雄, 技能伤害, 并行命令);
			}
		}
		命令组装器类.实例.添加命令(并行命令);
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
		return "B";
	}
}
