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
import edu.bjfu.lol.动态.对战中技能类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.先手值减少免疫状态类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 高原血统技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 3;
	private static final int 释放间隔回合数 = 8;
	private static final int 技能冷却减少回合数 = 1;
	private static final float 先手值提高百分比 = 0.25f;
	private static final float 攻击速度提高百分比 = 0.3f;
	private static final int 技能持续回合数 = 2;
	private static final int 技能延长回合数 = 1;
	private static final String 技能名 = "高原血统";
	private static final String 技能描述 = String.format("%s  被动:一位敌人阵亡会使易大师的普通技能冷却减少%d回合,主\n动:先手值提高%.0f%%,攻击速度提高%.0f%%,并且免疫先手值降低效果,持续\n%d回合,在激活时一位敌人阵亡会使易大师的高原血统持续时间延长%d回合.",
			技能名
			,技能冷却减少回合数, 先手值提高百分比 * 100, 攻击速度提高百分比 * 100, 技能持续回合数, 技能延长回合数);
	private static class 主动敌人阵亡触发效果状态类 extends 状态类
	{
		private static final String 状态描述 = String.format("高原血统，在激活时一位敌人阵亡会使易大师的高原血统持续时间延长%d回合。", 技能延长回合数);
		public 主动敌人阵亡触发效果状态类()
		{
			super(技能持续回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			命令组装器类.实例.添加命令(new 状态文本显示命令类(受状态作用英雄.is我方(), 受状态作用英雄.get英雄所在位置(), "高原血统延长", true));
			受状态作用英雄.get先手值增加状态(主动先手值状态类.class).状态延长(技能延长回合数);
			受状态作用英雄.get攻击速度增加状态(主动攻击速度状态类.class).状态延长(技能延长回合数);
			受状态作用英雄.get先手值减少免疫状态(主动免疫先手值降低效果状态类.class).状态延长(技能延长回合数);
			主动敌人阵亡触发效果状态类.this.状态延长(技能延长回合数);
		}
		@Override
		public String get状态描述()
		{
			return 状态描述;
		}
	}
	private static class 主动免疫先手值降低效果状态类 extends 先手值减少免疫状态类
	{
		public 主动免疫先手值降低效果状态类()
		{
			super(技能持续回合数);
		}
		@Override
		public String get状态描述()
		{
			return "高原血统，免疫先手值降低效果";
		}
	}
	private class 主动攻击速度状态类 extends 状态类
	{
		public 主动攻击速度状态类()
		{
			super(技能持续回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			受状态作用英雄.添加攻击速度百分比变化(攻击速度提高百分比);
		}
		@Override
		public String get状态描述()
		{
			return "高原血统，攻击速度提高";
		}
	}
	private class 主动先手值状态类 extends 状态类
	{
		public 主动先手值状态类()
		{
			super(技能持续回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			受状态作用英雄.添加先手值百分比变化(先手值提高百分比);
		}
		@Override
		public String get状态描述()
		{
			return "高原血统，先手值提高";
		}
	}
	private static class 高原血统被动状态类 extends 状态类
	{
		private static final String 状态描述 = String.format("高原血统，一位敌人阵亡会使易大师的普通技能冷却减少%d回合。", 技能冷却减少回合数);
		public 高原血统被动状态类()
		{
			super(Integer.MAX_VALUE);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			命令组装器类.实例.添加命令(new 状态文本显示命令类(受状态作用英雄.is我方(), 受状态作用英雄.get英雄所在位置(), "普通技能冷却减少", true));
			对战中技能类[] 对战中技能数组 = 受状态作用英雄.get对战中技能数组();
			for (int i = 0; i < 对战中技能数组.length; i++)
			{
				对战中技能数组[i].减少技能冷却回合数(技能冷却减少回合数);
			}
		}
		@Override
		public String get状态描述()
		{
			return 状态描述;
		}
	}
	public 高原血统技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 开启高原血统命令类 extends 命令类
	{
		private float 发出者X;
		private float 发出者Y;
		private String 英雄皮肤文件名;
		public 开启高原血统命令类(float 发出者x, float 发出者y, String 英雄皮肤文件名)
		{
			发出者X = 发出者x;
			发出者Y = 发出者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 开启高原血统动作类(发出者X, 发出者Y, 英雄皮肤文件名);
		}
	}
	private class 开启高原血统动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(0.8f);
		private 粒子演员类 粒子;
		private Music 音效;
		private 开启高原血统动作类(float 发出者X, float 发出者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "MasterYiSkillR.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者X, 发出者Y);
			粒子.addAction(延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "MasterYiSkillR.mp3"));
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
		并行命令.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "先手值↑,攻速↑,先手值减少免疫", true));
		if (释放此技能的英雄.is我方())
		{
			并行命令.添加命令(new 开启高原血统命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置()-1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		else
		{
			并行命令.添加命令(new 开启高原血统命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置()-1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		命令组装器类.实例.添加命令(并行命令);
		释放此技能的英雄.添加先手值增加状态(new 主动先手值状态类());
		释放此技能的英雄.添加攻击速度增加状态(new 主动攻击速度状态类());
		释放此技能的英雄.添加先手值减少免疫状态(new 主动免疫先手值降低效果状态类());
		释放此技能的英雄.添加敌方有英雄阵亡触发状态(new 主动敌人阵亡触发效果状态类());
		return true;
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		对战中英雄.添加敌方有英雄阵亡触发状态(new 高原血统被动状态类());
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
		return "E";
	}
	@Override
	public boolean is大招()
	{
		return true;
	}
}
