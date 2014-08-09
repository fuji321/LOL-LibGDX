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
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 猎手怒吼技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 12;
	private static final float 攻击速度增加百分比 = 40f / 100;
	private static final int 增速持续回合数 = 5;
	private static final String 技能名 = "猎手怒吼";
	private static final String 技能描述 = String.format("%s  沃里克发出刺耳怒吼,增加自己%.0f%%的攻击速度,同时对全体友\n军带来一半的加速效果,持续%d回合.",
			技能名
			,攻击速度增加百分比 * 100, 增速持续回合数);
	private static class 增速状态类 extends 状态类
	{
		private boolean is自身增速;
		public 增速状态类(boolean is自身增速)
		{
			super(增速持续回合数);
			this.is自身增速 = is自身增速;
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			受状态作用英雄.添加攻击速度百分比变化(is自身增速 ? 攻击速度增加百分比 : 攻击速度增加百分比 / 2);
		}
		@Override
		public String get状态描述()
		{
			return "猎手怒吼，增加攻击速度";
		}
	}
	public 猎手怒吼技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private String 英雄皮肤文件名;
		private LinkedList<Vector2> 位置链表 = new LinkedList<Vector2>();
		public 释放命令类(LinkedList<对战中英雄类> 加攻速英雄链表, String 英雄皮肤文件名)
		{
			this.英雄皮肤文件名 = 英雄皮肤文件名;
			for (对战中英雄类 英雄 : 加攻速英雄链表)
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
			return new 释放动作类(位置链表, 英雄皮肤文件名);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(1);
		private Music 音效;
		private 粒子演员类[] 粒子数组;
		private 释放动作类(LinkedList<Vector2> 位置链表, String 英雄皮肤文件名)
		{
			粒子数组 = new 粒子演员类[位置链表.size()];
			for (int i = 0; i < 粒子数组.length; i++)
			{
				ParticleEffect 粒子效果 = new ParticleEffect();
				粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "WarwickSkillW.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
				粒子数组[i] = new 粒子演员类(粒子效果);
				Vector2 位置 = 位置链表.get(i);
				粒子数组[i].setPosition(位置.x, 位置.y);
			}
			粒子数组[0].addAction(延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "WarwickSkillW.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
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
			音效.play();
			for (粒子演员类 粒子 : 粒子数组)
			{
				对战屏幕类.实例.添加演员(粒子);
			}
		}
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		并行命令类 并行命令 = new 并行命令类();
		LinkedList<对战中英雄类> 加攻速英雄链表 = new LinkedList<对战中英雄类>();
		for (对战中英雄类 英雄 : 释放此技能的英雄.get所在阵容().get对战中英雄数组())
		{
			if (英雄 != null && !英雄.is阵亡())
			{
				加攻速英雄链表.add(英雄);
				并行命令.添加命令(new 状态文本显示命令类(英雄.is我方(), 英雄.get英雄所在位置(), "攻速↑", true));
				if (英雄 == 释放此技能的英雄)
				{
					释放此技能的英雄.添加攻击速度增加状态(new 增速状态类(true));
				}
				else
				{
					释放此技能的英雄.添加攻击速度增加状态(new 增速状态类(false));
				}
			}
		}
		并行命令.添加命令(new 释放命令类(加攻速英雄链表, 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
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
		return "C";
	}
}
