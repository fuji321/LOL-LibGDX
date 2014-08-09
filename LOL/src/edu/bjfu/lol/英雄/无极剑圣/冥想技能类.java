package edu.bjfu.lol.英雄.无极剑圣;

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
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 冥想技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 4;
	private static final int 释放间隔回合数 = 8;
	private static final int 施法持续回合数 = 2;
	private static final int 技能基础回复生命值 = 30;
	private static final float 技能附加回复生命值所占法术强度比例 = 0.3f;
	private static final float 技能每失去生命值百分比 = 0.01f;
	private static final float 技能治疗效果提升百分比 = 0.01f;
	private static final float 技能伤害降低百分比 = 0.5f;
	private static final String 技能名 = "冥想";
	private static final String 技能描述 = String.format("%s  易大师开始念咒,在%d回合里每回合回复%d(+%.1f法术强度)生命值,\n易大师每失去%.1f%%生命值,这个治疗效果就会提升%.1f%%.念咒时,易大师\n所受的伤害会降低%.0f%%.",
			技能名
			,施法持续回合数, 技能基础回复生命值, 技能附加回复生命值所占法术强度比例, 技能每失去生命值百分比 * 100, 技能治疗效果提升百分比 * 100, 技能伤害降低百分比 * 100);
	private class 冥想状态类 extends 对战中英雄类.主动持续施法状态类 
	{
		public 冥想状态类(int 最大持续回合数)
		{
			super(最大持续回合数);
		}
		@Override
		public void 主动施法(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
		{
			super.主动施法(敌方阵容, 释放此技能的英雄);
			冥想技能类.施法(敌方阵容, 释放此技能的英雄);
		}
		@Override
		public String get状态描述()
		{	
			return "冥想，每回合回复生命值";
		}
		@Override
		protected void 被通知被打断(对战中英雄类 被打断英雄)
		{
			Gdx.app.debug("冥想技能类.冥想状态类.被通知被打断", String.format("%s冥想状态被打断\n", 被打断英雄.get阵容名所在位置英雄名字()));
			被打断英雄.移除伤害减免状态(伤害降低状态类.class);
		}
	}
	private static class 伤害降低状态类 extends 状态类
	{
		public 伤害降低状态类()
		{
			super(施法持续回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			受状态作用英雄.添加伤害百分比减免(技能伤害降低百分比);
		}
		@Override
		public String get状态描述()
		{
			return "冥想，易大师所受的伤害降低";
		}
	}
	public 冥想技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 开启冥想命令类 extends 命令类
	{
		private float 发出者X;
		private float 发出者Y;
		private String 英雄皮肤文件名;
		public 开启冥想命令类(float 发出者x, float 发出者y, String 英雄皮肤文件名)
		{
			发出者X = 发出者x;
			发出者Y = 发出者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 开启冥想动作类(发出者X, 发出者Y, 英雄皮肤文件名);
		}
	}
	private class 开启冥想动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(1f);
		private 粒子演员类 粒子;
		private Music 音效;
		private 开启冥想动作类(float 发出者X, float 发出者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "MasterYiSkillW.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者X, 发出者Y);
			粒子.addAction(延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "MasterYiSkillW.mp3"));
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
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		释放此技能的英雄.设置主动持续施法状态(new 冥想状态类(施法持续回合数));
		并行命令类 并行命令 = new 并行命令类();
		if (释放此技能的英雄.is我方())
		{
			并行命令.添加命令(new 开启冥想命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置()-1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		else
		{
			并行命令.添加命令(new 开启冥想命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置()-1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		并行命令.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "伤害减免", true));
		命令组装器类.实例.添加命令(并行命令);
		冥想技能类.施法(敌方阵容, 释放此技能的英雄);
		释放此技能的英雄.添加伤害减免状态(new 伤害降低状态类());
		return true;
	}
	@Override
	public String get技能描述()
	{
		return 技能描述;
	}
	private static void 施法(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		int 治疗效果值 = 技能基础回复生命值;
		治疗效果值 += 释放此技能的英雄.get对战中法术强度() * 技能附加回复生命值所占法术强度比例;
		int 生命值 = 释放此技能的英雄.get对战中生命值();
		int 最大生命值 = 释放此技能的英雄.get对战中最大生命值();
		float 失去生命值百分比 = (最大生命值 - 生命值) * 1.0f / 最大生命值;
		int 失去技能计算失去生命值百分比数量 = (int)(失去生命值百分比 / 技能每失去生命值百分比);
		float 效果提升百分比 = 技能治疗效果提升百分比 * 失去技能计算失去生命值百分比数量;
		治疗效果值 = 计算类.根据_百分比变化_计算值(治疗效果值, 效果提升百分比);
		并行命令类 并行命令 = new 并行命令类();
		并行命令.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "冥想", true));
		释放此技能的英雄.增加生命值(治疗效果值, 并行命令);
		命令组装器类.实例.添加命令(并行命令);
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
