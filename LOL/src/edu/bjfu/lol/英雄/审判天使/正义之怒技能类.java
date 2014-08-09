package edu.bjfu.lol.英雄.审判天使;

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
import edu.bjfu.lol.英雄.审判天使.圣焰技能类.圣焰状态类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 正义之怒技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 8;
	private static final int 技能基础伤害 = 20;
	private static final float 技能附加伤害所占法术强度比例 = 0.4f;
	private static final float 技能附加伤害所占攻击力比例 = 0.2f;
	private static final int 持续回合数 = 5;
	private static final String 技能名 = "正义之怒";
	private static final String 技能描述 = String.format("%s  凯尔每次普通攻击会造成额外的%d(+%.1f法术强度)魔法伤害,\n目标周围的敌人会受到%d(+%.1f攻击力)(+%.1f法术强度)魔法伤害,持续\n%d回合.",
			技能名
			,技能基础伤害, 技能附加伤害所占法术强度比例, 技能基础伤害, 技能附加伤害所占攻击力比例, 技能附加伤害所占法术强度比例, 持续回合数);
	protected static class 普攻附加伤害状态类 extends 普通攻击触发效果状态类
	{
		public 普攻附加伤害状态类()
		{
			super(持续回合数);
		}
		@Override
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器)
		{
			Gdx.app.debug("正义之怒技能类.普攻附加伤害状态类.普通攻击", String.format("%s触发正义之怒效果\n", 攻击者.get阵容名所在位置英雄名字()));
			int 造成伤害 = 0;
			int 主体伤害 = (int) (技能基础伤害 + 技能附加伤害所占法术强度比例 * 攻击者.get对战中法术强度());
			int 溅射伤害 = (int) (主体伤害 + 技能附加伤害所占攻击力比例 * 攻击者.get对战中攻击力());
			int 数值穿透 = 攻击者.get对战中数值法术穿透();
			float 百分比穿透 = 攻击者.get对战中百分比法术穿透();
			并行命令类 并行命令 = new 并行命令类();
			for (对战中英雄类 英雄 : 被攻击者.get所在阵容().get默认被攻击小范围英雄数组(攻击者))
			{
				if (英雄 != null && !英雄.is阵亡())
				{
					if (英雄 == 被攻击者)
					{
						造成伤害 += 英雄.被造成魔法伤害(攻击者, 主体伤害, 数值穿透, 百分比穿透, 并行命令);
					}
					else
					{
						圣焰状态类.施加圣焰效果(英雄, 并行命令);
						造成伤害 += 英雄.被造成魔法伤害(攻击者, 溅射伤害, 数值穿透, 百分比穿透, 并行命令);
					}
				}
			}
			命令组装器类.实例.添加命令(并行命令);
			攻击者.法术吸血(造成伤害, false);
		}
		@Override
		public String get状态描述()
		{
			return "凯尔每次普通攻击造成额外魔法伤害，目标周围的敌人也会受到伤害";
		}
	}
	public 正义之怒技能类()
	{
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
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "KayleSkillE.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(接收者X, 接收者Y);
			粒子.addAction(延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "KayleSkillE.mp3"));
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
		并行命令类 并行命令 = new 并行命令类();
		if (释放此技能的英雄.is我方())
		{
			并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		else
		{
			并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		并行命令.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "普攻附带伤害", true));
		命令组装器类.实例.添加命令(并行命令);
		释放此技能的英雄.添加普通攻击触发效果状态(new 普攻附加伤害状态类());
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
		return "D";
	}
}
