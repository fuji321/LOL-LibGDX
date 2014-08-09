package edu.bjfu.lol.英雄.审判天使;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import edu.bjfu.lol.scenes.scene2d.ui.动画演员类;
import edu.bjfu.lol.scenes.scene2d.ui.粒子演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.动作.攻击前摇动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄属性类;
import edu.bjfu.lol.静态.英雄类;

public class 审判天使英雄类 extends 英雄类
{
	private 技能类[] 技能数组 = new 技能类[5];
	private 英雄属性类 英雄属性 = new 英雄属性类();
	public 审判天使英雄类()
	{
		技能数组[0] = new 圣焰技能类();
		技能数组[1] = new 清算技能类();
		技能数组[2] = new 神圣祝福技能类();
		技能数组[3] = new 正义之怒技能类();
		技能数组[4] = new 神圣庇护技能类();
		英雄属性.set先手值(335);
		英雄属性.set攻击力(56);
		英雄属性.set攻击速度(0.638f);
		英雄属性.set护甲值(21);
		英雄属性.set魔法抗性(30);
		英雄属性.set最大生命值(511);
		英雄属性.set生命回复(8);
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
		return "审判天使";
	}
	@Override
	public String get英雄普攻声音文件名(对战中英雄类 对战中英雄)
	{
		return "KayleAttackWithoutSkillE.mp3";
	}
	@Override
	public String get英雄皮肤文件名()
	{
		return "Kayle";
	}
	private class 普攻命令类 extends 命令类
	{
		private int 发出者X;
		private int 发出者Y;
		private int 接收者X;
		private int 接收者Y;
		private boolean is奇数次攻击;
		private String 英雄皮肤文件名;
		public 普攻命令类(int 发出者x, int 发出者y, int 接收者x, int 接收者y, boolean is奇数次攻击, String 英雄皮肤文件名)
		{
			发出者X = 发出者x;
			发出者Y = 发出者y;
			接收者X = 接收者x;
			接收者Y = 接收者y;
			this.is奇数次攻击 = is奇数次攻击;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 普通攻击动作类(发出者X, 发出者Y, 接收者X, 接收者Y, is奇数次攻击, 英雄皮肤文件名);
		}
	}
	private class 普通攻击动作类 extends 动作类
	{
		private static final float 普通攻击动画比例 = 0.7f;
		private static final float 总体动画时间 = 攻击前摇动作类.近战总体摆动时间;
		private 动画演员类 发起者动画;
		private 粒子演员类 接收者粒子;
		private Music 音效;
		private 普通攻击动作类(int 发出者X, int 发出者Y, int 接收者X, int 接收者Y, boolean is奇数次攻击, String 英雄皮肤文件名)
		{
			TextureRegion[] 发出者图片数组 = new TextureRegion[4];
			for (int i = 0; i < 发出者图片数组.length; i++)
			{
				发出者图片数组[i] = 对战屏幕类.实例.get皮肤资源().getRegion("AttackGiver" + (is奇数次攻击 ? "Odd" : "Even") + (char)(i + 'A'));
			}
			发起者动画 = new 动画演员类(总体动画时间 / 发出者图片数组.length, 发出者图片数组, Animation.NORMAL);
			发起者动画.setWidth(对战屏幕类.英雄图片最大宽度 * 普通攻击动画比例);
			发起者动画.setHeight(对战屏幕类.英雄图片最大高度 * 普通攻击动画比例);
			发起者动画.setPosition(发出者X - 发起者动画.getWidth() / 2, 发出者Y - 发起者动画.getHeight() / 2);
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "KayleSkillEAttack.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			接收者粒子 = new 粒子演员类(粒子效果);
			接收者粒子.setPosition(接收者X, 接收者Y);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "KayleAttackWithSkillE.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		@Override
		public boolean is完成()
		{
			if (发起者动画.is已经结束())
			{
				发起者动画.remove();
				接收者粒子.remove();
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
			对战屏幕类.实例.添加演员(发起者动画);
			对战屏幕类.实例.添加演员(接收者粒子);
			音效.play();
		}
	}
	@Override
	public 命令类 get普通攻击命令(int 发出者x, int 发出者y, int 接收者x, int 接收者y, boolean is奇数次攻击, String 英雄皮肤文件名, 对战中英雄类 对战中英雄)
	{
		return new 普攻命令类(发出者x, 发出者y, 接收者x, 接收者y, is奇数次攻击, 英雄皮肤文件名);
	}
	@Override
	public boolean is近战(对战中英雄类 对战中英雄)
	{
		return 对战中英雄.get普通攻击触发效果状态(正义之怒技能类.普攻附加伤害状态类.class) == null;
	}
}
