package edu.bjfu.lol.英雄.放逐之刃;

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
import edu.bjfu.lol.动态.对战中英雄类.护盾状态类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.英雄.放逐之刃.符文之刃技能类.符文之刃状态类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 勇往直前技能类 extends 技能类 {

	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 5;
	private static final int 护盾基础值 = 70;
	private static final float 附加护盾所占攻击力比例 = 1;
	private static final int 护盾持续回合数 = 1;
	private static final String 技能名 = "勇往直前";
	private static final String 技能描述 = String.format("%s  瑞雯进行一次快速冲刺,并且武装上一层护盾,格挡将要来临的\n最多%d(+%.0f攻击力)伤害,护盾持续%d回合.", 
			技能名, 
			护盾基础值, 附加护盾所占攻击力比例, 护盾持续回合数);
	private static class 格挡护盾状态类 extends 护盾状态类
	{	
		public 格挡护盾状态类(int 护盾值) {
			super(护盾持续回合数, 护盾值);
		}
		@Override
		public String get状态描述() {
			return "勇往直前，护盾格挡伤害";
		}
	}
	public 勇往直前技能类() {
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
		private 延迟Action 延迟 = new 延迟Action(1f);
		private 粒子演员类 粒子;
		private Music 音效;
		private 释放动作类(float 发出者X, float 发出者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "RivenSkillE.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者X, 发出者Y);
			粒子.addAction(延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "RivenSkillE.mp3"));
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
		并行命令类 并行命令 = new 并行命令类();
		if (释放此技能的英雄.is我方())
		{
			并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		else
		{
			并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		并行命令.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "护盾", true));
		命令组装器类.实例.添加命令(并行命令);
		释放此技能的英雄.添加护盾状态(new 格挡护盾状态类((int) (护盾基础值 + 附加护盾所占攻击力比例 * 释放此技能的英雄.get对战中攻击力())));
		符文之刃状态类.被通知技能命中次数(1, 释放此技能的英雄);
		return true;
	}
	@Override
	public String get技能描述() {
		return 技能描述;
	}
	@Override
	public String get技能名() {
		return 技能名;
	}
	@Override
	public String get技能文件后缀()
	{
		return "D";
	}
}
