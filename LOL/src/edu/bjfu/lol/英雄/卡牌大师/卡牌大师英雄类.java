package edu.bjfu.lol.英雄.卡牌大师;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

import edu.bjfu.lol.scenes.scene2d.actions.绝对移动Action;
import edu.bjfu.lol.scenes.scene2d.ui.粒子演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.英雄.卡牌大师.选牌技能类.攻击附加特效状态类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄属性类;
import edu.bjfu.lol.静态.英雄类;

public class 卡牌大师英雄类 extends 英雄类
{
	private 技能类[] 技能数组 = new 技能类[4];
	private 英雄属性类 英雄属性 = new 英雄属性类();
	public 卡牌大师英雄类()
	{
		技能数组[0] = new 万能牌技能类();
		技能数组[1] = new 选牌技能类();
		技能数组[2] = new 卡牌骗术技能类();
		技能数组[3] = new 命运技能类();
		英雄属性.set最大生命值(466);
		英雄属性.set生命回复(5);
		英雄属性.set攻击力(50);
		英雄属性.set攻击速度(0.651f);
		英雄属性.set先手值(330);
		英雄属性.set护甲值(14);
		英雄属性.set魔法抗性(30);
	}
	@Override
	public 英雄属性类 get英雄属性()
	{
		return 英雄属性;
	}
	@Override
	public 技能类[] get技能数组()
	{
		return 技能数组;
	}
	@Override
	public String get英雄名字()
	{
		return "卡牌大师";
	}
	@Override
	public String get英雄皮肤文件名()
	{
		return "TwistedFate";
	}
	private class 普攻命令类 extends 命令类
	{
		private float 发出者x;
		private float 发出者y;
		private float 接收者x;
		private float 接收者y;
		private String 英雄皮肤文件名;
		private int 效果;
		public 普攻命令类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名, int 效果)
		{
			this.发出者x = 发出者x;
			this.发出者y = 发出者y;
			this.接收者x = 接收者x;
			this.接收者y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
			this.效果 = 效果;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 普攻动作类(发出者x, 发出者y, 接收者x, 接收者y, 英雄皮肤文件名, 效果);
		}
	}
	private class 普攻动作类 extends 动作类
	{
		private 绝对移动Action 移动Action;
		private 粒子演员类 粒子;
		private Music 音效;
		public 普攻动作类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名, int 效果)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.loadEmitters(Gdx.files.internal(常量类.粒子目录 + "TwistedFateAttack.p"));
			float 角度 = (float)(Math.atan((接收者y-发出者y)/(接收者x-发出者x)) / Math.PI * 180) - 90;
			if (接收者x < 发出者x)
			{
				角度 += 180;
			}
			ParticleEmitter 喷射器 = 粒子效果.findEmitter("Untitled");
			喷射器.getRotation().setHigh(角度);
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者x, 发出者y);
			移动Action = new 绝对移动Action(接收者x, 接收者y, 0.5f);
			粒子.addAction(移动Action);
			if (效果 >= 0)
			{
				switch (效果)
				{
					case 攻击附加特效状态类.红牌:
						音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "TwistedFateSkillWRed.mp3"));
						喷射器.setImagePath("SkillWRed.png");
						break;
					case 攻击附加特效状态类.黄牌:
						音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "TwistedFateSkillWYellow.mp3"));
						喷射器.setImagePath("SkillWYellow.png");
						break;
					case 攻击附加特效状态类.蓝牌:
						音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "TwistedFateSkillWBlue.mp3"));
						喷射器.setImagePath("SkillWBlue.png");
						break;
				}
			}
			else
			{
				音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "TwistedFateAttack.mp3"));
			}
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
			粒子效果.loadEmitterImages(英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
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
	public 命令类 get普通攻击命令(int 发出者x, int 发出者y, int 接收者x, int 接收者y, boolean is奇数次攻击, String 英雄皮肤文件名, 对战中英雄类 对战中英雄)
	{
		攻击附加特效状态类 攻击附加特效状态 = (攻击附加特效状态类) 对战中英雄.get普通攻击触发效果状态(选牌技能类.攻击附加特效状态类.class);
		if (攻击附加特效状态 != null)
		{
			return new 普攻命令类(发出者x, 发出者y, 接收者x, 接收者y, 英雄皮肤文件名, 攻击附加特效状态.get牌());
		}
		else
		{
			return new 普攻命令类(发出者x, 发出者y, 接收者x, 接收者y, 英雄皮肤文件名, -1);
		}
	}
	@Override
	public boolean is近战(对战中英雄类 对战中英雄)
	{
		return false;
	}
}
