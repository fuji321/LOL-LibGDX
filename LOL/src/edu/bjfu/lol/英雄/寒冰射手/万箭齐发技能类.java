package edu.bjfu.lol.英雄.寒冰射手;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

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
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 万箭齐发技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 4;
	private static final int 技能基础伤害 = 40;
	private static final float 技能附加伤害所占攻击力比例 = 1;
	private static final String 技能名 = "万箭齐发";
	private static final String 技能描述 = String.format("%s  向敌方横排射出多只箭,造成%d(+%.1f攻击力)物理伤害,该技\n能会触发冰霜射击的效果.",
			技能名
			,技能基础伤害, 技能附加伤害所占攻击力比例);
	public 万箭齐发技能类()
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
		private 粒子演员类[] 粒子数组 = new 粒子演员类[7];
		private Music 音效;
		public 释放动作类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			// 万箭齐发的射击接收者
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
				粒子效果数组[i].load(Gdx.files.internal(常量类.粒子目录 + "AsheAttack.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
				粒子数组[i] = new 粒子演员类(粒子效果数组[i]);
				粒子数组[i].setPosition(发出者x, 发出者y);
				double 角度 = 接收者角度 - (i - 中间下标) * 10;
				粒子效果数组[i].findEmitter("Untitled").getRotation().setHigh((float) 角度 - 90);
				double 弧度 = 角度 / 180 * Math.PI;
				float 相对接收者X = (float) (射程 * Math.cos(弧度));
				float 相对接收者Y = (float) (射程 * Math.sin(弧度));
				移动Action数组[i] = new 相对移动Action(相对接收者X, 相对接收者Y, 0.6f);
				粒子数组[i].addAction(移动Action数组[i]);
			}
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "AsheSkillW.mp3"));
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
		对战中英雄类[] 前排被攻击英雄数组 = 敌方阵容.get前排被攻击英雄数组();
		int 总体伤害 = 0;
		int 数值穿透 = 释放此技能的英雄.get对战中数值护甲穿透();
		float 百分比穿透 = 释放此技能的英雄.get对战中百分比护甲穿透();
		float 接收者x = 0;
		float 接收者y = 0;
		int 接收者总数 = 0;
		并行命令类 并行命令 = new 并行命令类();
		boolean is命中 = false;
		for (对战中英雄类 被攻击英雄 : 前排被攻击英雄数组)
		{
			if (被攻击英雄 != null && !被攻击英雄.is阵亡())
			{
				接收者总数++;
				if (被攻击英雄.is我方())
				{
					接收者x += 对战屏幕类.我方X坐标数组[被攻击英雄.get英雄所在位置()-1];
					接收者y += 对战屏幕类.我方Y坐标数组[被攻击英雄.get英雄所在位置()-1];
				}
				else
				{
					接收者x += 对战屏幕类.敌方X坐标数组[被攻击英雄.get英雄所在位置()-1];
					接收者y += 对战屏幕类.敌方Y坐标数组[被攻击英雄.get英雄所在位置()-1];
				}
				is命中 = true;
				冰霜射击技能类.冰霜射击状态类.添加先手值降低状态(被攻击英雄, 并行命令);
				总体伤害 += 被攻击英雄.被造成物理伤害(释放此技能的英雄, (int)(技能基础伤害 + 技能附加伤害所占攻击力比例 * 释放此技能的英雄.get对战中攻击力()), 数值穿透, 百分比穿透, 并行命令);
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
			释放此技能的英雄.法术吸血(总体伤害, true);
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
