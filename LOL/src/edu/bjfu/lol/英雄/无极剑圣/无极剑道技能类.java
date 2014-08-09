package edu.bjfu.lol.英雄.无极剑圣;

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

class 无极剑道技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 6;
	private static final float 被动增加攻击力百分比 = 0.1f;
	private static final int 主动增加真实伤害 = 10;
	private static final float 主动增加真实伤害所占攻击力比例 = 0.1f;
	private static final int 主动持续回合数 = 3;
	private static final String 技能名 = "无极剑道";
	private static final String 技能描述 = String.format("%s  无极剑道,被动:增加%.0f%%攻击力.主动:普通攻击造成额外的\n%d(+%.1f攻击力真实伤害),持续%d回合,在主动效果结束后,直到该技能重\n新冷却期间,被动效果无效.",
			技能名
			,被动增加攻击力百分比 * 100, 主动增加真实伤害, 主动增加真实伤害所占攻击力比例, 主动持续回合数);
	private class 普通攻击额外真实伤害状态类 extends 普通攻击触发效果状态类
	{
		public 普通攻击额外真实伤害状态类()
		{
			super(主动持续回合数);
		}
		@Override
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器)
		{
			int 真实伤害 = (int) (主动增加真实伤害 + 攻击者.get对战中攻击力() * 主动增加真实伤害所占攻击力比例);
			if (!被攻击者.is阵亡())
			{
				Gdx.app.debug("无极剑道技能类.普通攻击额外真实伤害状态类.普通攻击", String.format("%s无极剑道，造成真实伤害\n", 攻击者.get阵容名所在位置英雄名字()));
				命令组装器类.实例.添加命令(new 状态文本显示命令类(攻击者.is我方(), 攻击者.get英雄所在位置(), "无极剑道真实伤害", true));
				被攻击者.被造成真实伤害(攻击者, 真实伤害, null);
			}
		}
		@Override
		public String get状态描述()
		{
			return "无极剑道，普通攻击造成额外真实伤害";
		}
	}
	private class 被动增加攻击力状态类 extends 状态类
	{
		public 被动增加攻击力状态类()
		{
			super(Integer.MAX_VALUE);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			if (受状态作用英雄.get普通攻击触发效果状态(普通攻击额外真实伤害状态类.class) != null)
			{
				受状态作用英雄.添加攻击力百分比变化(被动增加攻击力百分比);
			}
		}
		@Override
		public String get状态描述()
		{
			return "无极剑道被动，增加攻击力";
		}
	}
	public 无极剑道技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 开启无极剑道命令类 extends 命令类
	{
		private float 发出者X;
		private float 发出者Y;
		private String 英雄皮肤文件名;
		public 开启无极剑道命令类(float 发出者x, float 发出者y, String 英雄皮肤文件名)
		{
			发出者X = 发出者x;
			发出者Y = 发出者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 开启无极剑道动作类(发出者X, 发出者Y, 英雄皮肤文件名);
		}
	}
	private class 开启无极剑道动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(0.8f);
		private 粒子演员类 粒子;
		private Music 音效;
		private 开启无极剑道动作类(float 发出者X, float 发出者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "MasterYiSkillE.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者X, 发出者Y);
			粒子.addAction(延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "MasterYiSkillE.mp3"));
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
		并行命令.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "普攻附带真实伤害", true));
		if (释放此技能的英雄.is我方())
		{
			并行命令.添加命令(new 开启无极剑道命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置()-1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		else
		{
			并行命令.添加命令(new 开启无极剑道命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置()-1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		命令组装器类.实例.添加命令(并行命令);
		释放此技能的英雄.添加普通攻击触发效果状态(new 普通攻击额外真实伤害状态类());
		return true;
	}
	@Override
	public String get技能描述()
	{
		return 技能描述;
	}
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		对战中英雄.添加攻击力增加状态(new 被动增加攻击力状态类());
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