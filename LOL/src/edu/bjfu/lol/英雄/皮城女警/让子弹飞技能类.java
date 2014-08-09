package edu.bjfu.lol.英雄.皮城女警;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

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
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 让子弹飞技能类 extends 技能类 {
	
	private static final int 第一次释放回合数 = 3;
	private static final int 释放间隔回合数 = 12;
	private static final int 技能基础伤害 = 250;
	private static final float 技能附加伤害所占攻击力比例 = 2.0f;
	private static final String 技能名 = "让子弹飞";
	private static final String 技能描述 = String.format("%s  凯特琳精心准备完美一击,对敌方受伤最重英雄造成%d(+%.0f攻\n击力)物理伤害,目标前方英雄会为目标英雄拦截子弹.",
			技能名
			,技能基础伤害, 技能附加伤害所占攻击力比例);
	public 让子弹飞技能类() {
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 瞄准命令类 extends 命令类
	{
		private float 接收者X;
		private float 接收者Y;
		private String 英雄皮肤文件名;
		public 瞄准命令类(float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			接收者X = 接收者x;
			接收者Y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 瞄准动作类(接收者X, 接收者Y, 英雄皮肤文件名);
		}
	}
	private class 瞄准动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(1.3f);;
		private 粒子演员类 粒子;
		private Music 音效; 
		public 瞄准动作类(float 接收者X, float 接收者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "CaitlynSkillRFocus.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(接收者X, 接收者Y);
			粒子.addAction(延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "CaitlynSkillR.mp3"));
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
	private class 释放命令类 extends 命令类
	{
		private float 发出者X;
		private float 发出者Y;
		private float 接收者X;
		private float 接收者Y;
		private String 英雄皮肤文件名;
		public 释放命令类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			发出者X = 发出者x;
			发出者Y = 发出者y;
			接收者X = 接收者x;
			接收者Y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(发出者X, 发出者Y, 接收者X, 接收者Y, 英雄皮肤文件名);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 绝对移动Action 移动Action;
		private 粒子演员类 粒子; 
		public 释放动作类(float 发出者X, float 发出者Y, float 接收者X, float 接收者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "CaitlynSkillR.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者X, 发出者Y);
			移动Action = new 绝对移动Action(接收者X, 接收者Y, 0.3f);
			粒子.addAction(移动Action);
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
		}
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄) {
		对战中英雄类 被攻击英雄 = 敌方阵容.get受伤最重英雄(false);
		if (被攻击英雄 != null)
		{
			if (释放此技能的英雄.is我方())
			{
				命令组装器类.实例.添加命令(new 瞄准命令类(
						对战屏幕类.敌方X坐标数组[被攻击英雄.get英雄所在位置() - 1],
						对战屏幕类.敌方Y坐标数组[被攻击英雄.get英雄所在位置() - 1],
						释放此技能的英雄.get英雄().get英雄皮肤文件名()));
				命令组装器类.实例.添加命令(new 释放命令类(
						对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.敌方X坐标数组[被攻击英雄.get英雄所在位置() - 1],
						对战屏幕类.敌方Y坐标数组[被攻击英雄.get英雄所在位置() - 1], 
						释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			else
			{
				命令组装器类.实例.添加命令(new 瞄准命令类(
						对战屏幕类.我方X坐标数组[被攻击英雄.get英雄所在位置() - 1],
						对战屏幕类.我方Y坐标数组[被攻击英雄.get英雄所在位置() - 1],
						释放此技能的英雄.get英雄().get英雄皮肤文件名()));
				命令组装器类.实例.添加命令(new 释放命令类(
						对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.我方X坐标数组[被攻击英雄.get英雄所在位置() - 1],
						对战屏幕类.我方Y坐标数组[被攻击英雄.get英雄所在位置() - 1], 
						释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			被攻击英雄 = 敌方阵容.get被攻击英雄前方可为目标抵挡伤害英雄(被攻击英雄.get英雄所在位置());
			int 造成伤害 = 被攻击英雄.被造成物理伤害(释放此技能的英雄, (int) (技能基础伤害 + 技能附加伤害所占攻击力比例 * 释放此技能的英雄.get对战中攻击力()), 释放此技能的英雄.get对战中数值护甲穿透(), 释放此技能的英雄.get对战中百分比护甲穿透(), null);
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
	public String get技能名() {
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
