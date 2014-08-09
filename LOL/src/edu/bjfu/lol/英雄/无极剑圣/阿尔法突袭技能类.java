package edu.bjfu.lol.英雄.无极剑圣;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
import edu.bjfu.lol.scenes.scene2d.ui.动画演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.动作.攻击前摇动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.screen.对战屏幕类.英雄造型类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.普通攻击触发效果状态类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 阿尔法突袭技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 6;
	private static final float 技能命中率 = 0.5f;
	private static final int 技能基础伤害 = 25;
	private static final float 技能附加伤害所占攻击力比例 = 1.0f;
	private static final float 暴击时额外伤害占攻击力比例 = 0.6f;
	private static final String 技能名 = "阿尔法突袭";
	private static final String 技能描述 = String.format("%s  易飞速穿越战场,对敌方小范围敌人各造成%d(+%.1f攻击力)\n物理伤害,命中率%.0f%%,阿尔法突袭能够暴击,暴击时造成额外%.1f攻击力物\n理伤害,每次普通攻击减少该技能1回合冷却时间.",
			技能名
			,技能基础伤害, 技能附加伤害所占攻击力比例, 技能命中率 * 100, 暴击时额外伤害占攻击力比例);
	private static class 普通攻击减少阿尔法突袭技能冷却状态类 extends 普通攻击触发效果状态类
	{
		public 普通攻击减少阿尔法突袭技能冷却状态类()
		{
			super(Integer.MAX_VALUE);
		}
		@Override
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器)
		{
			攻击者.get对战中技能数组()[0].减少技能冷却回合数(1);
			命令组装器类.实例.添加命令(new 状态文本显示命令类(攻击者.is我方(), 攻击者.get英雄所在位置(), "阿尔法突袭冷却减少", true));
			Gdx.app.debug("阿尔法突袭技能类.普通攻击减少阿尔法突袭技能冷却状态类.普通攻击", String.format("%s阿尔法突袭，减少阿尔法突袭技能冷却时间1回合\n", 攻击者.get阵容名所在位置英雄名字()));
		}
		@Override
		public String get状态描述()
		{
			return "阿尔法突袭，每次普通攻击减少该技能1回合冷却时间";
		}
	}
	public 阿尔法突袭技能类() {
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private boolean is我方;
		private int 英雄所在位置;
		private String 英雄皮肤文件名;
		private LinkedList<Vector2> 位置链表 = new LinkedList<Vector2>();
		public 释放命令类(boolean is我方, int 英雄所在位置, String 英雄皮肤文件名, LinkedList<对战中英雄类> 被攻击英雄链表)
		{
			this.is我方 = is我方;
			this.英雄所在位置 = 英雄所在位置;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
			for (对战中英雄类 英雄 : 被攻击英雄链表)
			{
				if (!is我方)
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
			return new 释放动作类(is我方, 英雄所在位置, 英雄皮肤文件名, 位置链表);
		}
		
	}
	private class 释放动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(攻击前摇动作类.近战总体摆动时间);
		private Music 音效;
		private 英雄造型类 释放技能英雄造型;
		private 动画演员类[] 被攻击动画数组;
		private 释放动作类(boolean is我方, int 英雄所在位置, String 英雄皮肤文件名, LinkedList<Vector2> 位置链表)
		{
			释放技能英雄造型 = 对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置);
			被攻击动画数组 = new 动画演员类[位置链表.size()];
			Skin 皮肤资源 = 英雄类.get皮肤资源(英雄皮肤文件名);
			TextureRegion[] 帧数组 = new TextureRegion[3];
			帧数组[0] = 皮肤资源.getRegion("SkillQA");
			帧数组[1] = 皮肤资源.getRegion("SkillQB");
			帧数组[2] = 皮肤资源.getRegion("SkillQC");
			for (int i = 0; i < 位置链表.size(); i++)
			{
				被攻击动画数组[i] = new 动画演员类(攻击前摇动作类.近战总体摆动时间 / 3, 帧数组, Animation.NORMAL);
				被攻击动画数组[i].setWidth(对战屏幕类.英雄图片最大宽度 * 0.7f);
				被攻击动画数组[i].setHeight(对战屏幕类.英雄图片最大高度 * 0.7f);
				Vector2 位置 = 位置链表.get(i);
				被攻击动画数组[i].setPosition(位置.x - 被攻击动画数组[i].getWidth() / 2, 位置.y - 被攻击动画数组[i].getHeight() / 2);
			}
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "MasterYiSkillQ.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		@Override
		public boolean is完成()
		{
			if (延迟.isAction已结束())
			{
				for (动画演员类 动画 : 被攻击动画数组)
				{
					动画.remove();
				}
				释放技能英雄造型.setColor(1,1,1,1);
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
			for (动画演员类 动画 : 被攻击动画数组)
			{
				对战屏幕类.实例.添加演员(动画);
			}
			释放技能英雄造型.setColor(1,1,1,0);
			释放技能英雄造型.addAction(延迟);
			音效.play();
		}
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄) {
		int 技能总体造成伤害 = 0;
		boolean is命中 = false;
		boolean is释放 = false;
		int 数值穿透 = 释放此技能的英雄.get对战中数值护甲穿透();
		float 百分比穿透 = 释放此技能的英雄.get对战中百分比护甲穿透();
		并行命令类 并行命令 = new 并行命令类();
		LinkedList<对战中英雄类> 被攻击英雄链表 = new LinkedList<对战中英雄类>();
		for (对战中英雄类 敌方英雄 : 敌方阵容.get默认被攻击小范围英雄数组(释放此技能的英雄))
		{
			if (敌方英雄 != null && !敌方英雄.is阵亡() && !敌方英雄.is对战中隐身())
			{
				is释放 = true;
				// 是否命中
				if (计算类.根据_概率_计算是否发生(技能命中率))
				{
					被攻击英雄链表.add(敌方英雄);
					int 攻击力 = 释放此技能的英雄.get对战中攻击力();
					int 物理伤害 = 技能基础伤害 + (int)(攻击力 * 技能附加伤害所占攻击力比例);
					// 是否暴击
					if (计算类.根据_概率_计算是否发生(释放此技能的英雄.get对战中暴击几率()))
					{
						物理伤害 += 暴击时额外伤害占攻击力比例 * 攻击力;
					}
					技能总体造成伤害 += 敌方英雄.被造成物理伤害(释放此技能的英雄, 物理伤害, 数值穿透, 百分比穿透, 并行命令);
					is命中 = true;
				}
			}
		}
		if (is命中)
		{
			释放此技能的英雄.法术吸血(技能总体造成伤害, true);
		}
		if (is释放)
		{
			命令组装器类.实例.添加命令(new 释放命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), 释放此技能的英雄.get英雄().get英雄皮肤文件名(), 被攻击英雄链表));
			命令组装器类.实例.延迟添加命令(并行命令);
		}
		return is释放;
	}
	@Override
	public String get技能描述() {
		return 技能描述;
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		对战中英雄.添加普通攻击触发效果状态(new 普通攻击减少阿尔法突袭技能冷却状态类());
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
