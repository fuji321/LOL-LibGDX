package edu.bjfu.lol.英雄.寒冰射手;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import edu.bjfu.lol.scenes.scene2d.actions.绝对移动Action;
import edu.bjfu.lol.scenes.scene2d.ui.粒子演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄属性类;
import edu.bjfu.lol.静态.英雄类;

public class 寒冰射手英雄类 extends 英雄类
{
	private 技能类[] 技能数组 = new 技能类[5];
	private 英雄属性类 英雄属性 = new 英雄属性类();
	public 寒冰射手英雄类()
	{
		// 设置技能
		技能数组[0] = new 全神贯注技能类();
		技能数组[1] = new 冰霜射击技能类();
		技能数组[2] = new 万箭齐发技能类();
		技能数组[3] = new 鹰击长空技能类();
		技能数组[4] = new 魔法水晶箭技能类();
		// 设置英雄属性
		英雄属性.set攻击力(49);
		英雄属性.set攻击速度(0.658f);
		英雄属性.set先手值(325);
		英雄属性.set护甲值(15);
		英雄属性.set魔法抗性(30);
		英雄属性.set最大生命值(474);
		英雄属性.set生命回复(5);
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
		return "寒冰射手";
	}
	@Override
	public String get英雄皮肤文件名()
	{
		return "Ashe";
	}
	@Override
	public boolean is近战(对战中英雄类 对战中英雄)
	{
		return false;
	}
	private class 普攻命令类 extends 命令类
	{
		private float 发出者x;
		private float 发出者y;
		private float 接收者x;
		private float 接收者y;
		private String 英雄皮肤文件名;
		public 普攻命令类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名)
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
			return new 普攻动作类(发出者x, 发出者y, 接收者x, 接收者y, 英雄皮肤文件名);
		}
	}
	private class 普攻动作类 extends 动作类
	{
		private 绝对移动Action 移动Action;
		private 粒子演员类 粒子;
		private Music 音效;
		public 普攻动作类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "AsheAttack.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			float 角度 = (float)(Math.atan((接收者y-发出者y)/(接收者x-发出者x)) / Math.PI * 180) - 90;
			if (接收者x < 发出者x)
			{
				角度 += 180;
			}
			粒子效果.findEmitter("Untitled").getRotation().setHigh(角度);
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者x, 发出者y);
			移动Action = new 绝对移动Action(接收者x, 接收者y, 0.5f);
			粒子.addAction(移动Action);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "AsheAttack.mp3"));
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
	public 命令类 get普通攻击命令(int 发出者x, int 发出者y, int 接收者x, int 接收者y, boolean is奇数次攻击, String 英雄皮肤文件名, 对战中英雄类 对战中英雄)
	{
		return new 普攻命令类(发出者x, 发出者y, 接收者x, 接收者y, 英雄皮肤文件名);
	}
}
