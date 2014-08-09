package edu.bjfu.lol.英雄.卡牌大师;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

import edu.bjfu.lol.scenes.scene2d.actions.相对移动Action;
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
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 万能牌技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 3;
	private static final int 技能基础伤害 = 60;
	private static final float 技能附加伤害所占法术强度比例 = 0.65f;
	private static final float 命中率 = 50f / 100;
	private static final String 技能名 = "万能牌";
	private static final String 技能描述 = String.format("%s  扔出三张卡牌,对敌方全体每人造成%d(+%.2f法术强度)魔法伤\n害,命中率%.0f%%.",
			技能名
			,技能基础伤害, 技能附加伤害所占法术强度比例, 命中率 * 100);
	public 万能牌技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private float 发出者x;
		private float 发出者y;
		private float 接收者x;
		private float 接收者y;
		private String 英雄皮肤文件名;
		public 释放命令类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名)
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
			return new 释放动作类(发出者x, 发出者y, 接收者x, 接收者y, 英雄皮肤文件名);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 相对移动Action[] 移动Action数组;
		private 粒子演员类[] 粒子数组 = new 粒子演员类[3];
		private Music 音效;
		public 释放动作类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			double 射程 = Math.sqrt((接收者y - 发出者y) * (接收者y - 发出者y) + (接收者x - 发出者x) * (接收者x - 发出者x)) * 2;
			double 接收者角度 = Math.atan((接收者y - 发出者y) / (接收者x - 发出者x)) / Math.PI * 180;
			if (接收者x < 发出者x)
			{
				接收者角度 += 180;
			}
			int 中间下标 = 粒子数组.length / 2;
			移动Action数组 = new 相对移动Action[粒子数组.length];
			ParticleEffect[] 粒子效果数组 = new ParticleEffect[粒子数组.length];
			for (int i = 0; i < 粒子数组.length; i++)
			{
				粒子效果数组[i] = new ParticleEffect();
				粒子效果数组[i].loadEmitters(Gdx.files.internal(常量类.粒子目录 + "TwistedFateSkillQ.p"));
				粒子数组[i] = new 粒子演员类(粒子效果数组[i]);
				粒子数组[i].setPosition(发出者x, 发出者y);
				double 角度 = 接收者角度 - (i - 中间下标) * 20;
				ParticleEmitter 粒子喷射 = 粒子效果数组[i].findEmitter("Untitled");
				int 随机数 = 计算类.随机整数值(3);
				switch (随机数)
				{
					case 0:
						粒子喷射.setImagePath("SkillWBlue.png");
						break;
					case 1:
						粒子喷射.setImagePath("SkillWRed.png");
						break;
					case 2:
						粒子喷射.setImagePath("SkillWYellow.png");
						break;
				}
				粒子效果数组[i].loadEmitterImages(英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
				粒子喷射.getRotation().setHigh((float) 角度 - 90);
				double 弧度 = 角度 / 180 * Math.PI;
				float 相对接收者X = (float) (射程 * Math.cos(弧度));
				float 相对接收者Y = (float) (射程 * Math.sin(弧度));
				移动Action数组[i] = new 相对移动Action(相对接收者X, 相对接收者Y, 0.6f);
				粒子数组[i].addAction(移动Action数组[i]);
			}
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "TwistedFateSkillQ.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		@Override
		public boolean is完成()
		{
			if (移动Action数组[0].isAction已结束())
			{
				for (粒子演员类 粒子 : 粒子数组)
				{
					粒子.remove();
					粒子.dispose();
				}
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
			for (粒子演员类 粒子 : 粒子数组)
			{
				对战屏幕类.实例.添加演员(粒子);
			}
			音效.play();
		}
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		int 技能伤害 = (int) (技能基础伤害 + 技能附加伤害所占法术强度比例 * 释放此技能的英雄.get对战中法术强度());
		int 总伤害 = 0;
		boolean is命中 = false;
		int 数值穿透 = 释放此技能的英雄.get对战中数值法术穿透();
		float 百分比穿透 = 释放此技能的英雄.get对战中百分比法术穿透();
		float 接收者x = 0;
		float 接收者y = 0;
		int 接收者总数 = 0;
		并行命令类 并行命令 = new 并行命令类();
		for (对战中英雄类 英雄 : 敌方阵容.get对战中英雄数组())
		{
			if (英雄 != null && !英雄.is阵亡())
			{
				接收者总数++;
				if (英雄.is我方())
				{
					接收者x += 对战屏幕类.我方X坐标数组[英雄.get英雄所在位置()-1];
					接收者y += 对战屏幕类.我方Y坐标数组[英雄.get英雄所在位置()-1];
				}
				else
				{
					接收者x += 对战屏幕类.敌方X坐标数组[英雄.get英雄所在位置()-1];
					接收者y += 对战屏幕类.敌方Y坐标数组[英雄.get英雄所在位置()-1];
				}
				if (计算类.根据_概率_计算是否发生(命中率))
				{
					总伤害 += 英雄.被造成魔法伤害(释放此技能的英雄, 技能伤害, 数值穿透, 百分比穿透, 并行命令);
					is命中 = true;
				}
			}
		}
		接收者x /= 接收者总数;
		接收者y /= 接收者总数;
		if (释放此技能的英雄.is我方())
		{
			命令组装器类.实例.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 接收者x, 接收者y, 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		else
		{
			命令组装器类.实例.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 接收者x, 接收者y, 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		if (is命中)
		{
			命令组装器类.实例.添加命令(并行命令);
			释放此技能的英雄.法术吸血(总伤害, true);
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
		return "A";
	}
}
