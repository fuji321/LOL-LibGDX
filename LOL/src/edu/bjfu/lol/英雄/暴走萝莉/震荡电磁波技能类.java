package edu.bjfu.lol.英雄.暴走萝莉;

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
import edu.bjfu.lol.动态.对战中英雄类.显隐状态类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 震荡电磁波技能类 extends 技能类 {

	private static final int 第一次释放回合数 = 2;
	private static final int 释放间隔回合数 = 5;
	private static final int 基础伤害 = 30;
	private static final float 附加伤害所占攻击力比例 = 1.4f;
	private static final float 先手值减少百分比 = 30f / 100;
	private static final int 显隐回合数 = 1;
	private static final float 命中率 = 70f / 100;
	private static final String 技能名 = "震荡电磁波";
	private static final String 技能描述 = String.format("%s  金克丝使用她的震荡手枪\"电磁器\"来向随机目标发射震荡\n波,对其造成%d(+%.1f攻击力)的物理伤害,显示该单位并减少先手值%.0f%%,\n持续%d回合,命中率%.0f%%.", 
			技能名, 基础伤害, 附加伤害所占攻击力比例, 先手值减少百分比 * 100, 显隐回合数, 命中率 * 100);
	private static class 先手值减少状态类 extends 状态类
	{
		public 先手值减少状态类() {
			super(显隐回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄) {
			受状态作用英雄.添加先手值百分比变化(-先手值减少百分比);
		}
		@Override
		public String get状态描述() {
			return "震荡电磁波，减少先手值";
		}
	}
	private static class 暴露状态类 extends 显隐状态类
	{
		public 暴露状态类() {
			super(显隐回合数);
		}
		@Override
		public String get状态描述() {
			return "震荡电磁波，被暴露";
		}
	}
	public 震荡电磁波技能类() {
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
		public 释放动作类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "JinxSkillW.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
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
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "JinxSkillW.mp3"));
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
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄) {
		对战中英雄类 敌人 = 敌方阵容.get随机被攻击英雄(true);
		if (计算类.根据_概率_计算是否发生(命中率))
		{
			并行命令类 并行命令 = new 并行命令类();
			并行命令.添加命令(new 状态文本显示命令类(敌人.is我方(), 敌人.get英雄所在位置(), "暴露,先手值↓", false));
			if (释放此技能的英雄.is我方())
			{
				并行命令.添加命令(new 释放命令类(
						对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.敌方X坐标数组[敌人.get英雄所在位置() - 1],
						对战屏幕类.敌方Y坐标数组[敌人.get英雄所在位置() - 1],
						释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			else
			{
				并行命令.添加命令(new 释放命令类(
						对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.我方X坐标数组[敌人.get英雄所在位置() - 1],
						对战屏幕类.我方Y坐标数组[敌人.get英雄所在位置() - 1],
						释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			敌人.添加显隐状态(new 暴露状态类(), 并行命令);
			敌人.添加先手值减少状态(new 先手值减少状态类());
			int 造成伤害 = 敌人.被造成物理伤害(释放此技能的英雄, (int) (基础伤害 + 附加伤害所占攻击力比例 * 释放此技能的英雄.get对战中攻击力()), 释放此技能的英雄.get对战中数值护甲穿透(), 释放此技能的英雄.get对战中百分比护甲穿透(), 并行命令);
			释放此技能的英雄.法术吸血(造成伤害, false);
			命令组装器类.实例.添加命令(并行命令);
		}
		else
		{
			if (释放此技能的英雄.is我方())
			{
				命令组装器类.实例.添加命令(new 释放命令类(
						对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						常量类.屏幕宽 / 2,
						常量类.屏幕高,
						释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			else
			{
				命令组装器类.实例.添加命令(new 释放命令类(
						对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						常量类.屏幕宽 / 2,
						0,
						释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
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
		return "C";
	}
}
