package edu.bjfu.lol.英雄.众星之子;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;

import edu.bjfu.lol.scenes.scene2d.actions.相对移动Action;
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

class 群星坠落技能类 extends 技能类 {
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 1;
	private static final int 技能基础伤害 = 60;
	private static final float 技能附加伤害所占法术强度比例 = 0.4f;
	private static final int 每层效果减少魔法抗性值 = 8;
	private static final int 魔法抗性降低持续回合数 = 2;
	private static final int 魔法抗性降低可叠加层数 = 10;
	private static final String 技能名 = "群星坠落";
	private static final String 技能描述 = String.format("%s  索拉卡召唤流星雨,对敌人前排造成%d(+%.1f法术强度)魔法伤\n害并减少他们%d点魔法抗性,持续%d回合,此效果最多可以叠加%d层.",
			技能名
			,技能基础伤害, 技能附加伤害所占法术强度比例, 每层效果减少魔法抗性值, 魔法抗性降低持续回合数, 魔法抗性降低可叠加层数);
	private static class 魔法抗性降低状态类 extends 状态类
	{
		public 魔法抗性降低状态类() {
			super(魔法抗性降低持续回合数, 魔法抗性降低可叠加层数, 1);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄) {
			受状态作用英雄.添加魔法抗性数值变化(super.get当前层数() * -每层效果减少魔法抗性值);
		}
		@Override
		public String get状态描述() {
			return String.format("群星坠落，魔法抗性降低，层数%d", super.get当前层数());
		}
	}
	public 群星坠落技能类() {
		super(第一次释放回合数, 释放间隔回合数);
	}
	private static class 释放命令类 extends 命令类
	{
		private String 英雄皮肤文件名;
		private LinkedList<Vector2> 位置链表 = new LinkedList<Vector2>();
		public 释放命令类(String 英雄皮肤文件名, LinkedList<对战中英雄类> 被攻击英雄链表)
		{
			this.英雄皮肤文件名 = 英雄皮肤文件名;
			for (对战中英雄类 英雄 : 被攻击英雄链表)
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
	private static class 释放动作类 extends 动作类
	{
		private static final int 位移 = 300;
		private 粒子演员类[] 粒子数组;
		private 相对移动Action[] 移动Action数组;
		private Music 音效;
		private 释放动作类(LinkedList<Vector2> 位置链表, String 英雄皮肤文件名)
		{
			粒子数组 = new 粒子演员类[位置链表.size()];
			移动Action数组 = new 相对移动Action[粒子数组.length];
			for (int i = 0; i < 位置链表.size(); i++)
			{
				ParticleEffect 粒子效果 = new ParticleEffect();
				粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "SorakaSkillQ.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
				粒子数组[i] = new 粒子演员类(粒子效果);
				移动Action数组[i] = new 相对移动Action(0, -位移, 0.5f);
				粒子数组[i].addAction(移动Action数组[i]);
				Vector2 位置 = 位置链表.get(i);
				粒子数组[i].setPosition(位置.x, 位置.y + 位移);
			}
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "SorakaSkillQ.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		@Override
		public boolean is完成()
		{
			if (移动Action数组[0].isAction已结束())
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
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄) {
		int 造成总伤害 = 0;
		int 数值穿透 = 释放此技能的英雄.get对战中数值法术穿透();
		float 百分比穿透 = 释放此技能的英雄.get对战中百分比法术穿透();
		LinkedList<对战中英雄类> 被攻击英雄链表 = new LinkedList<对战中英雄类>();
		并行命令类 并行命令 = new 并行命令类();
		for (对战中英雄类 被攻击英雄 : 敌方阵容.get前排被攻击英雄数组())
		{
			if (被攻击英雄 != null && !被攻击英雄.is阵亡() && !被攻击英雄.is对战中隐身())
			{
				状态类 状态 = 被攻击英雄.get魔法抗性减少状态(魔法抗性降低状态类.class);
				并行命令.添加命令(new 状态文本显示命令类(被攻击英雄.is我方(), 被攻击英雄.get英雄所在位置(), "魔抗↓", false));
				被攻击英雄链表.add(被攻击英雄);
				if (状态 == null)
				{
					被攻击英雄.添加魔法抗性减少状态(new 魔法抗性降低状态类());
				}
				else
				{
					状态.重置状态回合数();
					状态.增加一层(false);
				}
				造成总伤害 += 被攻击英雄.被造成魔法伤害(释放此技能的英雄, (int)(释放此技能的英雄.get对战中法术强度() * 技能附加伤害所占法术强度比例 + 技能基础伤害), 数值穿透, 百分比穿透, 并行命令);
			}
		}
		并行命令.添加命令(new 释放命令类(释放此技能的英雄.get英雄().get英雄皮肤文件名(), 被攻击英雄链表));
		命令组装器类.实例.添加命令(并行命令);
		释放此技能的英雄.法术吸血(造成总伤害, true);
		return true;
	}
	@Override
	public String get技能描述() {
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
