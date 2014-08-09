package edu.bjfu.lol.英雄.迅捷斥候;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
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
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.普通攻击触发效果状态类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 毒性射击技能类 extends 技能类
{
	private static final int 立即基础伤害 = 10;
	private static final float 立即附加伤害所占法术强度比例 = 0.3f;
	private static final int 随后基础伤害 = 6;
	private static final float 随后附加伤害所占法术强度比例 = 0.1f;
	private static final int 随后伤害持续回合数 = 1;
	private static final String 技能名 = "毒性射击";
	private static final String 技能描述 = String.format("%s  提莫的普通攻击会使目标中毒,立即造成%d(+%.1f法术强度)\n魔法伤害,并在随后的%d回合里每回合造成%d(+%.1f法术强度)魔法伤害.", 
			技能名, 立即基础伤害, 立即附加伤害所占法术强度比例, 随后伤害持续回合数, 随后基础伤害, 随后附加伤害所占法术强度比例);
	private static class 毒性射击状态类 extends 普通攻击触发效果状态类
	{
		private static class 中毒状态类 extends 状态类
		{
			private int 数值穿透;
			private float 百分比穿透;
			private int 伤害值;
			private 对战中英雄类 攻击者;
			public 中毒状态类(对战中英雄类 攻击者, int 伤害值, int 数值穿透, float 百分比穿透) {
				super(随后伤害持续回合数 + 1);
				this.伤害值 = 伤害值;
				this.数值穿透 = 数值穿透;
				this.百分比穿透 = 百分比穿透;
				this.攻击者 = 攻击者;
			}
			@Override
			public void 作用(对战中英雄类 受状态作用英雄) {
				并行命令类 并行命令 = new 并行命令类();
				并行命令.添加命令(new 状态文本显示命令类(受状态作用英雄.is我方(), 受状态作用英雄.get英雄所在位置(), "中毒", false));
				受状态作用英雄.被造成魔法伤害(攻击者, 伤害值, 数值穿透, 百分比穿透, 并行命令);
				命令组装器类.实例.添加命令(并行命令);
			}
			@Override
			public String get状态描述() {
				return "毒性射击，中毒每回合造成魔法伤害";
			}
		}
		public 毒性射击状态类() {
			super(Integer.MAX_VALUE);
		}
		private static class 释放命令类 extends 命令类
		{
			private float 接收者X;
			private float 接收者Y;
			private String 英雄皮肤文件名;
			public 释放命令类(float 接收者x, float 接收者y, String 英雄皮肤文件名)
			{
				接收者X = 接收者x;
				接收者Y = 接收者y;
				this.英雄皮肤文件名 = 英雄皮肤文件名;
			}
			@Override
			protected 动作类 生成动作()
			{
				return new 释放动作类(接收者X, 接收者Y, 英雄皮肤文件名);
			}
		}
		private static class 释放动作类 extends 动作类
		{
			private 延迟Action 延迟 = new 延迟Action(0.8f);
			private 粒子演员类 粒子;
			private 释放动作类(float 接收者X, float 接收者Y, String 英雄皮肤文件名)
			{
				ParticleEffect 粒子效果 = new ParticleEffect();
				粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "TeemoSkillE.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
				粒子 = new 粒子演员类(粒子效果);
				粒子.setPosition(接收者X, 接收者Y);
				粒子.addAction(延迟);
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
			}
		}
		@Override
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器) {
			int 法术强度 = 攻击者.get对战中法术强度();
			int 数值穿透 = 攻击者.get对战中数值法术穿透();
			float 百分比穿透 = 攻击者.get对战中百分比法术穿透();
			状态类 中毒状态 = 被攻击者.get生命值减少状态(中毒状态类.class);
			if (中毒状态 == null)
			{
				被攻击者.添加生命值减少状态(new 中毒状态类(攻击者, (int) (随后基础伤害 + 随后附加伤害所占法术强度比例 * 法术强度), 数值穿透, 百分比穿透));
			}
			else
			{
				中毒状态.重置状态回合数();
			}
			并行命令类 并行命令 = new 并行命令类();
			if (被攻击者.is我方())
			{
				并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[被攻击者.get英雄所在位置()-1], 对战屏幕类.我方Y坐标数组[被攻击者.get英雄所在位置()-1], 攻击者.get英雄().get英雄皮肤文件名()));
			}
			else
			{
				并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[被攻击者.get英雄所在位置()-1], 对战屏幕类.敌方Y坐标数组[被攻击者.get英雄所在位置()-1], 攻击者.get英雄().get英雄皮肤文件名()));
			}
			被攻击者.被造成魔法伤害(攻击者, (int) (立即基础伤害 + 立即附加伤害所占法术强度比例 * 法术强度), 数值穿透, 百分比穿透, 并行命令);
			命令组装器类.实例.添加命令(并行命令);
		}
		@Override
		public String get状态描述() {
			return 技能描述;
		}
	}
	public 毒性射击技能类()
	{
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		对战中英雄.添加普通攻击触发效果状态(new 毒性射击状态类());
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
