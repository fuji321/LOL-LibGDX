package edu.bjfu.lol.英雄.皮城女警;

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

public class 皮城女警英雄类 extends 英雄类 {
	private 技能类[] 技能数组 = new 技能类[5];
	private 英雄属性类 英雄属性 = new 英雄属性类();
	public 皮城女警英雄类() {
		技能数组[0] = new 爆头技能类();
		技能数组[1] = new 和平使者技能类();
		技能数组[2] = new 约德尔诱捕器技能类();
		技能数组[3] = new _90口径绳网技能类();
		技能数组[4] = new 让子弹飞技能类();
		英雄属性.set最大生命值(470);
		英雄属性.set生命回复(5);
		英雄属性.set护甲值(16);
		英雄属性.set魔法抗性(30);
		英雄属性.set攻击力(50);
		英雄属性.set攻击速度(0.668f);
		英雄属性.set先手值(305);
	}
	@Override
	public 英雄属性类 get英雄属性() {
		return 英雄属性;
	}
	@Override
	public 技能类[] get技能数组() {
		return 技能数组;
	}
	@Override
	public String get英雄名字() {
		return "皮城女警";
	}
	@Override
	public String get英雄皮肤文件名()
	{
		return "Caitlyn";
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
		private Music 音效;
		private 绝对移动Action 移动Action;
		private 粒子演员类 粒子;
		public 释放动作类(float 发出者X, float 发出者Y, float 接收者X, float 接收者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "CaitlynAttack.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者X, 发出者Y);
			移动Action = new 绝对移动Action(接收者X, 接收者Y, 0.3f);
			粒子.addAction(移动Action);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "CaitlynAttack.mp3"));
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
		return new 释放命令类(发出者x, 发出者y, 接收者x, 接收者y, 英雄皮肤文件名);
	}
	@Override
	public boolean is近战(对战中英雄类 对战中英雄)
	{
		return false;
	}
}
