package edu.bjfu.lol.英雄.卡牌大师;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
import edu.bjfu.lol.scenes.scene2d.ui.粒子演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.utils.图像计算类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.普通攻击触发效果状态类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 选牌技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 3;
	private static final int 攻击附加特效回合数 = 2;
	private static final int 蓝牌技能基础伤害 = 40;
	private static final int 红牌技能基础伤害 = 30;
	private static final int 金牌技能基础伤害 = 15;
	private static final float 技能附加伤害所占攻击力比例 = 1;
	private static final float 技能附加伤害所占法术强度比例 = 0.4f;
	private static final float 先手值降低百分比 = 30f / 100;
	private static final int 先手值降低持续回合数 = 1;
	private static final int 眩晕回合数 = 1;
	private static final String 技能名 = "选牌";
	private static final String 技能描述 = String.format("%s  随机选牌,下次攻击附加特效,持续%d回合.蓝牌造成%d(+%.0f攻击力)\n(+%.1f法术强度)魔法伤害,红牌对范围敌人造成%d(+%.0f攻击力)(%.1f法术\n强度)魔法伤害并降低先手值%.0f%%,持续%d回合,金色卡牌造成%d(+%.0f攻击力)\n(+%.1f法术强度)魔法伤害并眩晕目标%d回合.", 
			技能名
			, 攻击附加特效回合数
			, 蓝牌技能基础伤害, 技能附加伤害所占攻击力比例, 技能附加伤害所占法术强度比例, 
			红牌技能基础伤害, 技能附加伤害所占攻击力比例, 技能附加伤害所占法术强度比例, 先手值降低百分比 * 100, 先手值降低持续回合数, 
			金牌技能基础伤害, 技能附加伤害所占攻击力比例, 技能附加伤害所占法术强度比例, 眩晕回合数);
	private static class 先手值降低状态类 extends 状态类
	{
		public 先手值降低状态类()
		{
			super(先手值降低持续回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			受状态作用英雄.添加先手值百分比变化(-先手值降低百分比);
		}
		@Override
		public String get状态描述()
		{
			return "红牌，先手值降低";
		}
	}
	private static class 红牌效果命令类 extends 命令类
	{
		private float 接收者x;
		private float 接收者y;
		private String 英雄皮肤文件名;
		public 红牌效果命令类(float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			this.接收者x = 接收者x;
			this.接收者y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 红牌效果动作类(接收者x, 接收者y, 英雄皮肤文件名);
		}
	}
	private static class 红牌效果动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(0.8f);
		private 粒子演员类 粒子;
		private 红牌效果动作类(float 接收者X, float 接收者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "TwistedFateSkillWEffect.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
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
	protected static class 攻击附加特效状态类 extends 普通攻击触发效果状态类
	{
		public static final int 黄牌 = 0;
		public static final int 红牌 = 1;
		public static final int 蓝牌 = 2;
		private int 牌;
		public 攻击附加特效状态类()
		{
			super(攻击附加特效回合数);
			牌 = 计算类.随机整数值(3);
		}
		@Override
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器)
		{
			对战中英雄类 被攻击英雄 = 被攻击者;
			int 技能伤害 = (int) (攻击者.get对战中攻击力() * 技能附加伤害所占攻击力比例 + 攻击者.get对战中法术强度() * 技能附加伤害所占法术强度比例);
			int 造成伤害 = 0;
			boolean is群体伤害 = false;
			int 数值穿透 = 攻击者.get对战中数值法术穿透();
			float 百分比穿透 = 攻击者.get对战中百分比法术穿透();
			switch (牌)
			{
				// 蓝牌
				case 蓝牌:
					Gdx.app.debug("选牌技能类.攻击附加特效状态类.普通攻击", "选中蓝牌");
					技能伤害 += 蓝牌技能基础伤害;
					造成伤害 = 被攻击英雄.被造成魔法伤害(攻击者, 技能伤害, 数值穿透, 百分比穿透, null);
					break;
				// 红牌
				case 红牌:
					Gdx.app.debug("选牌技能类.攻击附加特效状态类.普通攻击", "选中红牌");
					技能伤害 += 红牌技能基础伤害;
					并行命令类 并行命令 = new 并行命令类();
					if (被攻击者.is我方())
					{
						并行命令.添加命令(new 红牌效果命令类(对战屏幕类.我方X坐标数组[被攻击者.get英雄所在位置()-1], 对战屏幕类.我方Y坐标数组[被攻击者.get英雄所在位置()-1], 攻击者.get英雄().get英雄皮肤文件名()));
					}
					else
					{
						并行命令.添加命令(new 红牌效果命令类(对战屏幕类.敌方X坐标数组[被攻击者.get英雄所在位置()-1], 对战屏幕类.敌方Y坐标数组[被攻击者.get英雄所在位置()-1], 攻击者.get英雄().get英雄皮肤文件名()));
					}
					for (对战中英雄类 被溅射英雄 : 被攻击者.get所在阵容().get默认被攻击小范围英雄数组(攻击者))
					{
						if (被溅射英雄 != null && !被溅射英雄.is阵亡())
						{
							并行命令.添加命令(new 状态文本显示命令类(被溅射英雄.is我方(), 被溅射英雄.get英雄所在位置(), "先手值↓", false));
							被溅射英雄.添加先手值减少状态(new 先手值降低状态类());
							造成伤害 += 被溅射英雄.被造成魔法伤害(攻击者, 技能伤害, 数值穿透, 百分比穿透, 并行命令);
						}
					}
					命令组装器类.实例.添加命令(并行命令);
					is群体伤害 = true;
					break;
				// 金牌
				case 黄牌:
					Gdx.app.debug("选牌技能类.攻击附加特效状态类.普通攻击", "选中金牌");
					技能伤害 += 金牌技能基础伤害;
					被攻击英雄.被眩晕(眩晕回合数, null);
					造成伤害 += 被攻击英雄.被造成魔法伤害(攻击者, 技能伤害, 数值穿透, 百分比穿透, null);
					break;
			}
			攻击者.法术吸血(造成伤害, is群体伤害);
			迭代器.remove();
		}
		@Override
		public String get状态描述()
		{
			return "选牌，攻击附加特效";
		}
		protected int get牌()
		{
			return 牌;
		}
	}
	public 选牌技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private float 发出者x;
		private float 发出者y;
		private String 英雄皮肤文件名;
		private int 牌;
		public 释放命令类(float 发出者x, float 发出者y, String 英雄皮肤文件名, int 牌)
		{
			this.发出者x = 发出者x;
			this.发出者y = 发出者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
			this.牌 = 牌;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(发出者x, 发出者y, 英雄皮肤文件名, 牌);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(0.7f);
		private Image 牌图片;
		private Music 音效;
		private 释放动作类(float 发出者X, float 发出者Y, String 英雄皮肤文件名, int 牌)
		{
			Skin 皮肤资源 = 英雄类.get皮肤资源(英雄皮肤文件名);
			switch (牌)
			{
				case 攻击附加特效状态类.红牌:
					牌图片 = new Image(皮肤资源.getRegion("SkillWSelectedRed"));
					break;
				case 攻击附加特效状态类.黄牌:
					牌图片 = new Image(皮肤资源.getRegion("SkillWSelectedYellow"));
					break;
				case 攻击附加特效状态类.蓝牌:
					牌图片 = new Image(皮肤资源.getRegion("SkillWSelectedBlue"));
					break;
			}
			图像计算类.调整演员至不超过指定最大宽(牌图片, 80);
			牌图片.setPosition(发出者X - 牌图片.getWidth() / 2, 发出者Y - 牌图片.getHeight() / 2);
			牌图片.addAction(延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "TwistedFateSkillW.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		public boolean is完成()
		{
			if (延迟.isAction已结束())
			{
				牌图片.remove();
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
			对战屏幕类.实例.添加演员(牌图片);
			音效.play();
		}
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		攻击附加特效状态类 攻击附加特效状态 = new 攻击附加特效状态类();
		if (释放此技能的英雄.is我方())
		{
			命令组装器类.实例.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名(), 攻击附加特效状态.get牌()));
		}
		else
		{
			命令组装器类.实例.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名(), 攻击附加特效状态.get牌()));
		}
		释放此技能的英雄.添加普通攻击触发效果状态(攻击附加特效状态);
		return true;
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
		return "B";
	}
}
