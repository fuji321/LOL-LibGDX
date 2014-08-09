package edu.bjfu.lol.英雄.德玛西亚之力;

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

class 勇气技能类 extends 技能类 {
	private static final int 第一次释放回合数 = 2;
	private static final int 释放间隔回合数 = 8;
	private static final int 减伤状态持续回合数 = 2;
	private static final float 护甲魔法抗性提高百分比 = 20f / 100;
	private static final float 减伤百分比 = 20f / 100;
	private static final String 技能名 = "勇气";
	private static final String 技能描述 = String.format("%s 被动，盖伦的护甲和魔法抗性提高%.0f%%.主动,盖伦在接下来的%d回合内\n施加一个防御护盾,减少所受伤害%.0f%%.",
			技能名
			,护甲魔法抗性提高百分比 * 100, 减伤状态持续回合数, 减伤百分比 * 100);
	private static class 护甲提高状态类 extends 状态类
	{
		public 护甲提高状态类()
		{
			super(Integer.MAX_VALUE);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			受状态作用英雄.添加护甲百分比变化(护甲魔法抗性提高百分比);
		}
		@Override
		public String get状态描述()
		{
			return "勇气，被动，盖伦的护甲提高";
		}
	}
	private static class 魔法抗性提高状态类 extends 状态类
	{
		public 魔法抗性提高状态类()
		{
			super(Integer.MAX_VALUE);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			受状态作用英雄.添加魔法抗性百分比变化(护甲魔法抗性提高百分比);
		}
		@Override
		public String get状态描述()
		{
			return "勇气，被动，盖伦的魔法抗性提高";
		}
	}
	private static class 伤害减免状态 extends 状态类
	{
		public 伤害减免状态() {
			super(减伤状态持续回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄) {
			受状态作用英雄.添加伤害百分比减免(减伤百分比);
		}
		@Override
		public String get状态描述() {
			return "勇气，减少所受伤害";
		}
	}
	public 勇气技能类() {
		super(第一次释放回合数, 释放间隔回合数);
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		对战中英雄.添加护甲值增加状态(new 护甲提高状态类());
		对战中英雄.添加魔法抗性增加状态(new 魔法抗性提高状态类());
	}
	private class 释放勇气命令类 extends 命令类
	{
		private float 发出者X;
		private float 发出者Y;
		private String 英雄皮肤文件名;
		public 释放勇气命令类(float 发出者x, float 发出者y, String 英雄皮肤文件名)
		{
			发出者X = 发出者x;
			发出者Y = 发出者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放勇气动作类(发出者X, 发出者Y, 英雄皮肤文件名);
		}
	}
	private class 释放勇气动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(0.85f);
		private 粒子演员类 粒子;
		private Music 音效;
		private 释放勇气动作类(float 发出者X, float 发出者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "GarenSkillW.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者X, 发出者Y);
			粒子.addAction(延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "GarenSkillW.mp3"));
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
		并行命令类 并行命令 = new 并行命令类();
		并行命令.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "伤害减免", true));
		if (释放此技能的英雄.is我方())
		{
			并行命令.添加命令(new 释放勇气命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		else
		{
			并行命令.添加命令(new 释放勇气命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		命令组装器类.实例.添加命令(并行命令);
		释放此技能的英雄.添加伤害减免状态(new 伤害减免状态());
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
