package edu.bjfu.lol.英雄.众星之子;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 星之祝福技能类 extends 技能类 {
	private static final int 第一次释放回合数 = 2;
	private static final int 释放间隔回合数 = 10;
	private static final int 技能基础回复值 = 70;
	private static final float 技能附加回复值所占法术强度比例 = 0.45f;
	private static final int 增加护甲值 = 25;
	private static final int 护甲增加持续回合数 = 2;
	private static final String 技能名 = "星之祝福";
	private static final String 技能描述 = String.format("%s  索拉卡祝福一名受伤最重的友方单位,为目标回复%d(+%.2f法\n术强度)点生命值,并暂时增加目标%d点护甲,护甲效果持续%d回合.",
			技能名
			,技能基础回复值, 技能附加回复值所占法术强度比例, 增加护甲值, 护甲增加持续回合数);
	private static class 护甲增加状态类 extends 状态类
	{
		public 护甲增加状态类() {
			super(护甲增加持续回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄) {
			受状态作用英雄.添加护甲数值变化(增加护甲值);
		}
		@Override
		public String get状态描述() {
			return "星之祝福，增加护甲";
		}
	}
	public 星之祝福技能类() {
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
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
	private class 释放动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(1f);
		private 粒子演员类 粒子;
		private Music 音效;
		private 释放动作类(float 接收者X, float 接收者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "SorakaSkillW.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(接收者X, 接收者Y);
			粒子.addAction(延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "SorakaSkillW.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
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
			音效.play();
		}
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄) {
		对战中英雄类 受伤最重英雄 = 释放此技能的英雄.get所在阵容().get受伤最重英雄(true);
		并行命令类 并行命令 = new 并行命令类();
		并行命令.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "护甲↑", true));
		if (受伤最重英雄.is我方())
		{
			并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[受伤最重英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[受伤最重英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		else
		{
			并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[受伤最重英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[受伤最重英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		受伤最重英雄.增加生命值((int) (技能基础回复值 + 技能附加回复值所占法术强度比例 * 释放此技能的英雄.get对战中法术强度()), 并行命令);
		命令组装器类.实例.添加命令(并行命令);
		受伤最重英雄.添加护甲值增加状态(new 护甲增加状态类());
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
		return "C";
	}
}
