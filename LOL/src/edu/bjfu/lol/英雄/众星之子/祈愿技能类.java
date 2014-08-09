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

class 祈愿技能类 extends 技能类 {
	private static final int 第一次释放回合数 = 3;
	private static final int 释放间隔回合数 = 15;
	private static final int 技能基础回复值 = 200;
	private static final float 技能附加回复生命值所占法术强度比例 = 0.7f;
	private static final String 技能名 = "祈愿";
	private static final String 技能描述 = String.format("%s  索拉卡召唤圣洁的能量,瞬间大幅回复自己和所有友方英雄%d\n(+%.1f法术强度)生命值.",
			技能名
			,技能基础回复值, 技能附加回复生命值所占法术强度比例);
	public 祈愿技能类() {
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private float 接收者X;
		private float 接收者Y;
		private String 英雄皮肤文件名;
		private boolean is有音效;
		public 释放命令类(float 接收者x, float 接收者y, String 英雄皮肤文件名, boolean is有音效)
		{
			接收者X = 接收者x;
			接收者Y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
			this.is有音效 = is有音效;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(接收者X, 接收者Y, 英雄皮肤文件名, is有音效);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(1f);
		private 粒子演员类 粒子;
		private Music 音效;
		private 释放动作类(float 接收者X, float 接收者Y, String 英雄皮肤文件名, boolean is有音效)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "SorakaSkillW.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(接收者X, 接收者Y);
			粒子.addAction(延迟);
			if (is有音效)
			{
				音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "SorakaSkillR.mp3"));
				音效.setOnCompletionListener(new 音乐播放完自动销毁类());
			}
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
			if (音效 != null)
			{
				音效.play();
			}
		}
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄) {
		int 增加生命值 = (int) (技能基础回复值 + 技能附加回复生命值所占法术强度比例 * 释放此技能的英雄.get对战中法术强度());
		并行命令类 并行命令 = new 并行命令类();
		boolean is有音效 = true;
		for (对战中英雄类 英雄 : 释放此技能的英雄.get所在阵容().get对战中英雄数组())
		{
			if (英雄 != null && !英雄.is阵亡())
			{
				if (英雄.is我方())
				{
					并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名(), is有音效));
				}
				else
				{
					并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名(), is有音效));
				}
				is有音效 = false;
				英雄.增加生命值(增加生命值, 并行命令);
			}
		}
		命令组装器类.实例.添加命令(并行命令);
		return true;
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
