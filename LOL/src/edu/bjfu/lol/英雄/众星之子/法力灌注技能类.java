package edu.bjfu.lol.英雄.众星之子;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
import edu.bjfu.lol.scenes.scene2d.ui.粒子演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 法力灌注技能类 extends 技能类 {

	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 5;
	private static final int 技能基础伤害 = 50;
	private static final float 技能附加伤害所占法术强度比例 = 0.6f;
	private static final int 沉默持续回合数 = 1;
	private static final String 技能名 = "法力灌注";
	private static final String 技能描述 = String.format("%s  对敌方随机目标释放造成%d(+%.1f法术强度)点魔法伤害,并将\n目标沉默%d回合.",
			技能名
			,技能基础伤害, 技能附加伤害所占法术强度比例, 沉默持续回合数);
	public 法力灌注技能类() {
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private float 接收者x;
		private float 接收者y;
		private String 英雄皮肤文件名;
		public 释放命令类(float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			this.接收者x = 接收者x;
			this.接收者y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(接收者x, 接收者y, 英雄皮肤文件名);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(0.85f);
		private 粒子演员类 粒子;
		private Music 音效;
		private 释放动作类(float 接收者X, float 接收者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "SorakaSkillE.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(接收者X, 接收者Y);
			粒子.addAction(延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "SorakaSkillE.mp3"));
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
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄) {
		对战中英雄类 被攻击英雄 = 敌方阵容.get随机被攻击英雄(false);
		if (被攻击英雄 != null)
		{
			并行命令类 并行命令 = new 并行命令类();
			被攻击英雄.被沉默(沉默持续回合数, 并行命令);
			if (被攻击英雄.is我方())
			{
				并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[被攻击英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[被攻击英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			else
			{
				并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[被攻击英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[被攻击英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			int 造成伤害 = 被攻击英雄.被造成魔法伤害(释放此技能的英雄, (int) (释放此技能的英雄.get对战中法术强度() * 技能附加伤害所占法术强度比例 + 技能基础伤害), 释放此技能的英雄.get对战中数值法术穿透(), 释放此技能的英雄.get对战中百分比法术穿透(), 并行命令);
			命令组装器类.实例.添加命令(并行命令);
			释放此技能的英雄.法术吸血(造成伤害, false);
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
		return "D";
	}
}
