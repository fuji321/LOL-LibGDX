package edu.bjfu.lol.英雄.披甲龙龟;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
import edu.bjfu.lol.scenes.scene2d.ui.粒子演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.被动持续释放状态类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 地动山摇技能类 extends 技能类 {

	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 15;
	private static final int 基础伤害 = 65;
	private static final float 附加伤害所占法术强度比例 = 0.3f;
	private static final float 命中率 = 70f / 100;
	private static final int 持续回合数 = 4;
	private static final String 技能名 = "地动山摇";
	private static final String 技能描述 = String.format("%s  拉莫斯释放毁灭震荡波,每回合对敌人每人造成%d(+%.1f法术\n强度)魔法伤害,命中率%.0f%%，持续%d回合.", 
			技能名
			,基础伤害, 附加伤害所占法术强度比例, 命中率 * 100, 持续回合数);
	private static class 地动山摇被动持续释放状态类 extends 被动持续释放状态类
	{
		public 地动山摇被动持续释放状态类(int 最大持续回合数) {
			super(最大持续回合数);
		}
		@Override
		public void 被动释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄) {
			super.被动释放(敌方阵容, 释放此技能的英雄);
			技能释放(敌方阵容, 释放此技能的英雄);
		}
		@Override
		public String get状态描述() {
			return "地动山摇被动持续释放";
		}
	}
	private static class 释放命令类 extends 命令类
	{
		private float 接收者x;
		private float 接收者y;
		private String 英雄皮肤文件名;
		private boolean 有音效;
		public 释放命令类(float 接收者x, float 接收者y, String 英雄皮肤文件名, boolean 有音效)
		{
			this.接收者x = 接收者x;
			this.接收者y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
			this.有音效 = 有音效;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(接收者x, 接收者y, 英雄皮肤文件名, 有音效);
		}
	}
	private static class 释放动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(0.5f);
		private 粒子演员类 粒子;
		private Music 音效;
		private 释放动作类(float 接收者X, float 接收者Y, String 英雄皮肤文件名, boolean 有音效)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "RammusSkillR.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(接收者X, 接收者Y);
			粒子.addAction(延迟);
			if (有音效)
			{
				音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "RammusSkillR.mp3"));
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
	private static void 技能释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		int 技能伤害 = (int) (基础伤害 + 附加伤害所占法术强度比例 * 释放此技能的英雄.get对战中法术强度());
		int 造成伤害 = 0;
		boolean is命中 = false;
		int 数值穿透 = 释放此技能的英雄.get对战中数值法术穿透();
		float 百分比穿透 = 释放此技能的英雄.get对战中百分比法术穿透();
		并行命令类 并行命令 = new 并行命令类();
		if (释放此技能的英雄.is我方())
		{
			并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名(), true));
		}
		else
		{
			并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名(), true));
		}
		for (对战中英雄类 敌方英雄 : 敌方阵容.get对战中英雄数组())
		{
			if (敌方英雄 != null && !敌方英雄.is阵亡())
			{
				if (敌方英雄.is我方())
				{
					并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[敌方英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[敌方英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名(), false));
				}
				else
				{
					并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[敌方英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[敌方英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名(), false));
				}
				if (计算类.根据_概率_计算是否发生(命中率))
				{
					造成伤害 += 敌方英雄.被造成魔法伤害(释放此技能的英雄, 技能伤害, 数值穿透, 百分比穿透, 并行命令);
					is命中 = true;
				}
			}
		}
		命令组装器类.实例.添加命令(并行命令);
		if (is命中)
		{
			释放此技能的英雄.法术吸血(造成伤害, true);
		}
	}
	public 地动山摇技能类() {
		super(第一次释放回合数, 释放间隔回合数);
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄) {
		技能释放(敌方阵容, 释放此技能的英雄);
		释放此技能的英雄.添加被动持续释放状态(new 地动山摇被动持续释放状态类(持续回合数 - 1));
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
		return "E";
	}
	@Override
	public boolean is大招()
	{
		return true;
	}
}