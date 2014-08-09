package edu.bjfu.lol.英雄.德玛西亚之力;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
import edu.bjfu.lol.scenes.scene2d.actions.相对移动Action;
import edu.bjfu.lol.scenes.scene2d.ui.粒子演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 德玛西亚正义技能类 extends 技能类 {
	
	private static final int 第一次释放回合数 = 3;
	private static final int 释放间隔回合数 = 10;
	private static final int 技能基础伤害值 = 175;
	private static final float 目标每失去生命值数 = 3.5f;
	private static final int 增加伤害值 = 1;
	private static final String 技能名 = "德玛西亚正义";
	private static final String 技能描述 = String.format("%s  盖伦召唤德玛西亚之力,试图斩杀敌方受伤最重英雄,目标\n损失的生命越多,则此技能造成的伤害越高.造成%d魔法伤害,敌人每失去\n%.1f生命值就会多承受%d伤害.",
			技能名
			, 技能基础伤害值, 目标每失去生命值数, 增加伤害值);
	public 德玛西亚正义技能类() {
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private float 发出者X;
		private float 发出者Y;
		private String 英雄皮肤文件名;
		public 释放命令类(float 发出者x, float 发出者y, String 英雄皮肤文件名)
		{
			发出者X = 发出者x;
			发出者Y = 发出者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(发出者X, 发出者Y, 英雄皮肤文件名);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(0.75f);
		private 粒子演员类 粒子;
		private Music 音效;
		private 释放动作类(float 发出者X, float 发出者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "GarenSkillR.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			int 位移 = -200;
			粒子.setPosition(发出者X, 发出者Y - 位移);
			相对移动Action 移动Action = new 相对移动Action(0, 位移, 0.2f);
			SequenceAction 顺序Action = Actions.sequence(移动Action, 延迟);
			粒子.addAction(顺序Action);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "GarenSkillR.mp3"));
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
		对战中英雄类 被攻击英雄 = 敌方阵容.get受伤最重英雄(false);
		if (被攻击英雄 != null)
		{
			if (被攻击英雄.is我方())
			{
				命令组装器类.实例.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[被攻击英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[被攻击英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			else
			{
				命令组装器类.实例.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[被攻击英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[被攻击英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			int 增加伤害 = (int) ((被攻击英雄.get对战中最大生命值() - 被攻击英雄.get对战中生命值()) / 目标每失去生命值数 * 增加伤害值);
			int 造成魔法伤害 = 被攻击英雄.被造成魔法伤害(释放此技能的英雄, 技能基础伤害值 + 增加伤害, 释放此技能的英雄.get对战中数值法术穿透(), 释放此技能的英雄.get对战中百分比法术穿透(), null);
			释放此技能的英雄.法术吸血(造成魔法伤害, false);
			return true;
		}
		else
		{
			return false;
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
		return "E";
	}
	@Override
	public boolean is大招()
	{
		return true;
	}
}
