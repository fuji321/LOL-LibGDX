package edu.bjfu.lol.英雄.审判天使;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
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
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.英雄.审判天使.圣焰技能类.圣焰状态类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 清算技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 4;
	private static final int 技能基础伤害 = 60;
	private static final float 技能附加伤害所占攻击力比例 = 1;
	private static final float 技能附加伤害所占法术强度比例 = 1;
	private static final float 先手值降低百分比 = 35f / 100;
	private static final int 先手值降低持续回合数 = 2;
	private static final String 技能名 = "清算";
	private static final String 技能描述 = String.format("%s  引爆一个目标,造成%d(+%.1f攻击力)(+%.1f法术强度)魔法伤害,减\n少目标%.0f%%先手值,持续%d回合,并施加一层圣焰效果.",
			技能名
			,技能基础伤害, 技能附加伤害所占攻击力比例, 技能附加伤害所占法术强度比例, 先手值降低百分比 * 100, 先手值降低持续回合数);
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
			return "清算，先手值降低";
		}
	}
	public 清算技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private float 发出者X;
		private float 发出者Y;
		private float 接收者X;
		private float 接收者Y;
		private String 英雄皮肤文件名;
		public 释放命令类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			发出者X = 发出者x;
			发出者Y = 发出者y;
			接收者X = 接收者x;
			接收者Y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(发出者X, 发出者Y, 接收者X, 接收者Y, 英雄皮肤文件名);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(0.4f);
		private 粒子演员类 粒子;
		private Music 音效; 
		public 释放动作类(float 发出者X, float 发出者Y, float 接收者X, float 接收者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "KayleSkillQ.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者X, 发出者Y);
			绝对移动Action 移动Action = new 绝对移动Action(接收者X, 接收者Y, 0.35f);
			粒子.addAction(Actions.sequence(移动Action, 延迟));
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "KayleSkillQ.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		@Override
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
		对战中英雄类 被攻击英雄 = 敌方阵容.get默认被攻击英雄(释放此技能的英雄);
		if (被攻击英雄 != null)
		{
			并行命令类 并行命令 = new 并行命令类();
			if (释放此技能的英雄.is我方())
			{
				并行命令.添加命令(new 释放命令类(
						对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.敌方X坐标数组[被攻击英雄.get英雄所在位置() - 1],
						对战屏幕类.敌方Y坐标数组[被攻击英雄.get英雄所在位置() - 1], 
						释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			else
			{
				并行命令.添加命令(new 释放命令类(
						对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.我方X坐标数组[被攻击英雄.get英雄所在位置() - 1],
						对战屏幕类.我方Y坐标数组[被攻击英雄.get英雄所在位置() - 1], 
						释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			并行命令.添加命令(new 状态文本显示命令类(被攻击英雄.is我方(), 被攻击英雄.get英雄所在位置(), "先手值↓,双抗↓", false));
			被攻击英雄.添加先手值减少状态(new 先手值降低状态类());
			圣焰状态类.施加圣焰效果(被攻击英雄, 并行命令);
			命令组装器类.实例.添加命令(并行命令);
			int 造成伤害 = 被攻击英雄.被造成魔法伤害(释放此技能的英雄, (int) (技能基础伤害 + 释放此技能的英雄.get对战中攻击力() * 技能附加伤害所占攻击力比例 + 释放此技能的英雄.get对战中法术强度() * 技能附加伤害所占法术强度比例), 释放此技能的英雄.get对战中数值法术穿透(), 释放此技能的英雄.get对战中百分比法术穿透(), null);
			释放此技能的英雄.法术吸血(造成伤害, false);
			return true;
		}
		else
		{
			return false;
		}
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
