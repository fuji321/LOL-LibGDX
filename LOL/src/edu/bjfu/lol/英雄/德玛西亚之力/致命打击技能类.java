package edu.bjfu.lol.英雄.德玛西亚之力;

import java.util.Iterator;

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
import edu.bjfu.lol.动态.对战中英雄类.普通攻击触发效果状态类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 致命打击技能类 extends 技能类 {
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 6;
	private static final float 先手值提升百分比 = 15f / 100;
	private static final int 先手值提升持续回合数 = 2;
	private static final int 下次攻击造成伤害状态持续回合数 = 2;
	private static final int 下次攻击造成伤害基础值 = 30;
	private static final float 下次攻击造成伤害附加伤害所占攻击力比例 = 1.4f;
	private static final int 沉默持续回合数 = 1;
	private static final String 技能名 = "致命打击";
	private static final String 技能描述 = String.format("%s  盖伦提升%.0f%%的先手值,持续%d回合,在接下来的%d回合内,他的\n下次普通攻击会造成%d(+%.1f攻击力)物理伤害,并沉默目标%d回合.",
			技能名
			,先手值提升百分比 * 100, 先手值提升持续回合数, 下次攻击造成伤害状态持续回合数, 下次攻击造成伤害基础值, 下次攻击造成伤害附加伤害所占攻击力比例, 沉默持续回合数);
	private static class 先手值提升状态类 extends 状态类
	{
		public 先手值提升状态类() {
			super(先手值提升持续回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄) {
			受状态作用英雄.添加先手值百分比变化(先手值提升百分比);
		}
		@Override
		public String get状态描述() {
			return "致命打击，提升先手值";
		}	
	}
	protected static class 下一次攻击造成额外伤害状态类 extends 普通攻击触发效果状态类
	{
		public 下一次攻击造成额外伤害状态类() {
			super(下次攻击造成伤害状态持续回合数);
		}
		@Override
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器) {
			Gdx.app.debug("致命打击技能类.下一次攻击造成额外伤害状态类.普通攻击", String.format("%s致命打击普通攻击效果触发，沉默%s并造成额外伤害\n", 攻击者.get阵容名所在位置英雄名字(), 被攻击者.get阵容名所在位置英雄名字()));
			并行命令类 并行命令 = new 并行命令类();
			被攻击者.被沉默(沉默持续回合数, 并行命令);
			int 造成的伤害 = 被攻击者.被造成物理伤害(攻击者, (int)(攻击者.get对战中攻击力() * 下次攻击造成伤害附加伤害所占攻击力比例) + 下次攻击造成伤害基础值, 攻击者.get对战中数值护甲穿透(), 攻击者.get对战中百分比护甲穿透(), 并行命令);
			命令组装器类.实例.添加命令(并行命令);
			攻击者.法术吸血(造成的伤害, false);
			迭代器.remove();
		}
		@Override
		public String get状态描述() {
			return "致命打击，下次攻击造成额外物理伤害，并沉默目标";
		}
	}
	public 致命打击技能类() {
		super(第一次释放回合数, 释放间隔回合数);
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄) {
		并行命令类 并行命令 = new 并行命令类();
		if (释放此技能的英雄.is我方())
		{
			并行命令.添加命令(new 开启致命打击命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		else
		{
			并行命令.添加命令(new 开启致命打击命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));	
		}
		释放此技能的英雄.添加先手值增加状态(new 先手值提升状态类());
		并行命令.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "先手值↑,下次普攻沉默目标", true));
		命令组装器类.实例.添加命令(并行命令);
		释放此技能的英雄.添加普通攻击触发效果状态(new 下一次攻击造成额外伤害状态类());
		return true;
	}
	private class 开启致命打击命令类 extends 命令类
	{
		private float 发出者X;
		private float 发出者Y;
		private String 英雄皮肤文件名;
		public 开启致命打击命令类(float 发出者x, float 发出者y, String 英雄皮肤文件名)
		{
			发出者X = 发出者x;
			发出者Y = 发出者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 开启致命打击动作类(发出者X, 发出者Y, 英雄皮肤文件名);
		}
	}
	private class 开启致命打击动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(0.6f);
		private 粒子演员类 粒子;
		private Music 音效;
		private 开启致命打击动作类(float 发出者X, float 发出者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "GarenSkillQ.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者X, 发出者Y);
			粒子.addAction(延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "GarenSkillQ.mp3"));
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
