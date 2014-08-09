package edu.bjfu.lol.英雄.九尾妖狐;

import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
import edu.bjfu.lol.scenes.scene2d.actions.绝对移动Action;
import edu.bjfu.lol.scenes.scene2d.ui.粒子演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.英雄.九尾妖狐.摄魂夺魄技能类.摄魂夺魄状态类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 妖异狐火技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 2;
	private static final int 释放间隔回合数 = 5;
	private static final int 基础伤害 = 40;
	private static final float 附加伤害所占法术强度比例 = 0.4f;
	private static final float 额外狐火造成伤害百分比 = 50f / 100;
	private static final String 技能名 = "妖异狐火";
	private static final String 技能描述 = String.format("%s  阿狸放出三团狐火,它们会锁定敌方小范围三名敌人进行攻击,\n造成%d(+%.1f法术强度)魔法伤害,在命中同一目标时,额外的狐火会造成\n%.0f%%伤害.",
			技能名
			,基础伤害, 附加伤害所占法术强度比例, 额外狐火造成伤害百分比 * 100);
	public 妖异狐火技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 狐火命令类 extends 命令类
	{
		private float 发出者x;
		private float 发出者y;
		private float 接收者x;
		private float 接收者y;
		private String 英雄皮肤文件名;
		public 狐火命令类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名)
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
			return new 异妖狐火动作类(发出者x, 发出者y, 接收者x, 接收者y, 英雄皮肤文件名);
		}
	}
	private class 异妖狐火动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(0.25f);
		private 粒子演员类 粒子;
		private Music 音效; 
		public 异妖狐火动作类(float 发出者X, float 发出者Y, float 接收者X, float 接收者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "AhriSkillW.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者X, 发出者Y);
			绝对移动Action 移动Action = new 绝对移动Action(接收者X, 接收者Y, 0.55f);
			SequenceAction 序列Action = Actions.sequence(移动Action, 延迟);
			粒子.addAction(序列Action);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "AhriSkillW.mp3"));
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
	// 用于释放技能的集合
	private HashSet<对战中英雄类> 被妖异狐火命中英雄集合 = new HashSet<对战中英雄类>();
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄) {
		被妖异狐火命中英雄集合.clear();
		int 技能伤害 = (int) (基础伤害 + 附加伤害所占法术强度比例 * 释放此技能的英雄.get对战中法术强度());
		int 额外狐火伤害 = (int) (技能伤害 * 额外狐火造成伤害百分比);
		int 造成伤害 = 0;
		int 命中个数 = 0;
		int 数值穿透 = 释放此技能的英雄.get对战中数值法术穿透();
		float 百分比穿透 = 释放此技能的英雄.get对战中百分比法术穿透();
		for (对战中英雄类 敌人 : 敌方阵容.get默认被攻击小范围英雄数组(释放此技能的英雄))
		{
			if (敌人 != null && !敌人.is阵亡() && !敌人.is对战中隐身())
			{
				if (释放此技能的英雄.is我方())
				{
					命令组装器类.实例.添加命令(new 狐火命令类(
							对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
							对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
							对战屏幕类.敌方X坐标数组[敌人.get英雄所在位置() - 1],
							对战屏幕类.敌方Y坐标数组[敌人.get英雄所在位置() - 1], 
							释放此技能的英雄.get英雄().get英雄皮肤文件名()));
				}
				else
				{
					命令组装器类.实例.添加命令(new 狐火命令类(
							对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
							对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
							对战屏幕类.我方X坐标数组[敌人.get英雄所在位置() - 1],
							对战屏幕类.我方Y坐标数组[敌人.get英雄所在位置() - 1],
							释放此技能的英雄.get英雄().get英雄皮肤文件名()));
				}
				if (被妖异狐火命中英雄集合.contains(敌人))
				{
					造成伤害 += 敌人.被造成魔法伤害(释放此技能的英雄, 额外狐火伤害, 数值穿透, 百分比穿透, null);
				}
				else
				{
					命中个数++;
					造成伤害 += 敌人.被造成魔法伤害(释放此技能的英雄, 技能伤害, 数值穿透, 百分比穿透, null);
					被妖异狐火命中英雄集合.add(敌人);
				}
			}
		}
		if (命中个数 > 0)
		{
			摄魂夺魄状态类.法术吸血(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), 释放此技能的英雄, 命中个数, 造成伤害, true);
		}
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
		return "C";
	}
}
