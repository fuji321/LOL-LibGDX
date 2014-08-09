package edu.bjfu.lol.英雄.寒冰射手;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;

import edu.bjfu.lol.scenes.scene2d.actions.相对移动Action;
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
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 鹰击长空技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 2;
	private static final int 释放间隔回合数 = 10;
	private static final int 视野保留回合数 = 2;
	private static final String 技能名 = "鹰击长空";
	private static final String 技能描述 = String.format("%s  艾希将一个鹰灵进行实体化,并让它进行侦查,将会显示敌方\n隐身单位,视野保留%d回合.",
			技能名
			,视野保留回合数);
	private static class 显隐状态类 extends 对战中英雄类.显隐状态类
	{
		public 显隐状态类()
		{
			super(视野保留回合数);
		}
		@Override
		public String get状态描述()
		{
			return "鹰击长空，该单位被艾希的鹰灵侦查到";
		}
	}
	public 鹰击长空技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private boolean is我方释放;
		private String 英雄皮肤文件名;
		public 释放命令类(boolean is我方释放, String 英雄皮肤文件名)
		{
			this.is我方释放 = is我方释放;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(is我方释放, 英雄皮肤文件名);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 相对移动Action 移动Action;
		private 粒子演员类 粒子;
		private Music 音效;
		public 释放动作类(boolean is我方释放, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "AsheSkillE.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			if (is我方释放)
			{
				粒子.setPosition(常量类.屏幕宽 / 2, 0);
				移动Action = new 相对移动Action(0, 常量类.屏幕高, 1f);
			}
			else
			{
				ParticleEmitter 鸟 = 粒子效果.findEmitter("bird");
				ScaledNumericValue 旋转值 = 鸟.getRotation();
				旋转值.setHigh(180);
				粒子.setPosition(常量类.屏幕宽 / 2, 常量类.屏幕高);
				移动Action = new 相对移动Action(0, -常量类.屏幕高, 1f);
			}
			粒子.addAction(移动Action);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "AsheSkillE.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		@Override
		public boolean is完成()
		{
			if (移动Action.isAction已结束())
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
		命令组装器类.实例.添加命令(new 释放命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		并行命令类 并行命令 = new 并行命令类();
		for (对战中英雄类 敌方英雄 : 敌方阵容.get对战中英雄数组())
		{
			if (敌方英雄 != null && !敌方英雄.is阵亡())
			{
				并行命令.添加命令(new 状态文本显示命令类(敌方英雄.is我方(), 敌方英雄.get英雄所在位置(), "显隐", false));
				敌方英雄.添加显隐状态(new 显隐状态类(), 并行命令);
			}
		}
		命令组装器类.实例.添加命令(并行命令);
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
