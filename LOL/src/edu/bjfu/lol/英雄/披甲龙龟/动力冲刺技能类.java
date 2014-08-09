package edu.bjfu.lol.英雄.披甲龙龟;

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
import edu.bjfu.lol.screen.对战屏幕类.英雄造型类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 动力冲刺技能类 extends 技能类 {

	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 5;
	private static final int 技能基础伤害 = 100;
	private static final float 技能附加伤害所占攻击力比例 = 1;
	private static final float 先手值减少百分比 = 20f / 100;
	private static final float 命中率 = 80f / 100;
	private static final int 持续回合数 = 1;
	private static final String 技能名 = "动力冲刺";
	private static final String 技能描述 = String.format("%s  拉莫斯缩成球状冲击目标,对小范围敌人造成%d(+%.0f攻击力)\n魔法伤害,并减少他们%.0f%%先手值,命中率%.0f%%,持续%d回合.", 
			技能名
			,技能基础伤害, 技能附加伤害所占攻击力比例, 先手值减少百分比 * 100, 命中率 * 100, 持续回合数);
	private static class 先手值减少状态类 extends 状态类
	{
		public 先手值减少状态类() {
			super(持续回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄) {
			受状态作用英雄.添加先手值百分比变化(-先手值减少百分比);
		}
		@Override
		public String get状态描述() {
			return "动力冲刺，减少先手值";
		}
	}
	public 动力冲刺技能类() {
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private boolean is我方;
		private int 英雄所在位置;
		private float 发出者x;
		private float 发出者y;
		private float 接收者x;
		private float 接收者y;
		private String 英雄皮肤文件名;
		public 释放命令类(boolean is我方, int 英雄所在位置, float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			this.is我方 = is我方;
			this.英雄所在位置 = 英雄所在位置;
			this.发出者x = 发出者x;
			this.发出者y = 发出者y;
			this.接收者x = 接收者x;
			this.接收者y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(is我方, 英雄所在位置, 发出者x, 发出者y, 接收者x, 接收者y, 英雄皮肤文件名);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 绝对移动Action 移动Action;
		private 粒子演员类 粒子;
		private Music 音效;
		private 英雄造型类 英雄造型;
		public 释放动作类(boolean is我方, int 英雄所在位置, float 发出者X, float 发出者Y, float 接收者X, float 接收者Y, String 英雄皮肤文件名)
		{
			this.英雄造型 = 对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置);
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "RammusSkillQ.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者X, 发出者Y);
			移动Action = new 绝对移动Action(接收者X, 接收者Y, 0.7f);
			粒子.addAction(移动Action);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "RammusSkillQ.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		@Override
		public boolean is完成()
		{
			if (移动Action.isAction已结束())
			{
				英雄造型.setColor(1, 1, 1, 1);
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
			英雄造型.setColor(1, 1, 1, 0);
			音效.play();
		}
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄) {
		int 技能伤害 = (int) (技能基础伤害 + 技能附加伤害所占攻击力比例 * 释放此技能的英雄.get对战中攻击力());
		int 造成伤害 = 0;
		boolean is命中 = false;
		int 数值穿透 = 释放此技能的英雄.get对战中数值法术穿透();
		float 百分比穿透 = 释放此技能的英雄.get对战中百分比法术穿透();
		并行命令类 并行命令 = new 并行命令类();
		float 接收者x = 0;
		float 接收者y = 0;
		int 接收者总数 = 0;
		for (对战中英雄类 敌方英雄 : 敌方阵容.get默认被攻击小范围英雄数组(释放此技能的英雄))
		{
			if (敌方英雄 != null && !敌方英雄.is阵亡() && !敌方英雄.is对战中隐身())
			{
				接收者总数++;
				if (敌方英雄.is我方())
				{
					接收者x += 对战屏幕类.我方X坐标数组[敌方英雄.get英雄所在位置()-1];
					接收者y += 对战屏幕类.我方Y坐标数组[敌方英雄.get英雄所在位置()-1];
				}
				else
				{
					接收者x += 对战屏幕类.敌方X坐标数组[敌方英雄.get英雄所在位置()-1];
					接收者y += 对战屏幕类.敌方Y坐标数组[敌方英雄.get英雄所在位置()-1];
				}
				if (计算类.根据_概率_计算是否发生(命中率))
				{
					并行命令.添加命令(new 状态文本显示命令类(敌方英雄.is我方(), 敌方英雄.get英雄所在位置(), "先手值↓", false));
					敌方英雄.添加先手值减少状态(new 先手值减少状态类());
					造成伤害 += 敌方英雄.被造成魔法伤害(释放此技能的英雄, 技能伤害, 数值穿透, 百分比穿透, 并行命令);
					is命中 = true;
				}
			}
		}
		接收者x /= 接收者总数;
		接收者y /= 接收者总数;
		if (释放此技能的英雄.is我方())
		{
			命令组装器类.实例.添加命令(new 释放命令类(true, 释放此技能的英雄.get英雄所在位置(), 对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 接收者x, 接收者y, 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		else
		{
			命令组装器类.实例.添加命令(new 释放命令类(false, 释放此技能的英雄.get英雄所在位置(), 对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 接收者x, 接收者y, 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		命令组装器类.实例.添加命令(并行命令);
		if (is命中)
		{
			释放此技能的英雄.法术吸血(造成伤害, true);
		}
		return true;
	}
	@Override
	public String get技能描述() {
		return 技能描述;
	}
	@Override
	public String get技能名() {
		return 技能名;
	}
	@Override
	public String get技能文件后缀()
	{
		return "B";
	}
}
