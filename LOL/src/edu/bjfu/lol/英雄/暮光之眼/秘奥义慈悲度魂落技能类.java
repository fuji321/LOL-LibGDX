package edu.bjfu.lol.英雄.暮光之眼;

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
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 秘奥义慈悲度魂落技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 3;
	private static final int 释放间隔回合数 = 15;
	private static final int 持续回合数 = 2;
	private static final int 基础吸收伤害 = 250;
	private static final float 附加吸收伤害所占法术强度比例 = 1.35f;
	private static final String 技能名 = "秘奥义慈悲度魂落";
	private static final String 技能描述 = String.format("%s  慎为受伤最重的友军套上吸收伤害的护盾,在%d回合\n持续时间里为目标吸收%d(+%.2f法术强度)的伤害.",
			技能名
			,持续回合数, 基础吸收伤害, 附加吸收伤害所占法术强度比例);
	private static class 吸收伤害护盾状态类 extends 护盾状态类
	{
		public 吸收伤害护盾状态类(int 护盾值)
		{
			super(持续回合数, 护盾值);
		}
		@Override
		public String get状态描述()
		{
			return "秘奥义慈悲度魂落，护盾为目标吸收伤害";
		}
	}
	public 秘奥义慈悲度魂落技能类()
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
		private 延迟Action 延迟;
		private 粒子演员类 发出粒子;
		private 粒子演员类 接收粒子;
		private Music 音效;
		public 释放动作类(float 发出者X, float 发出者Y, float 接收者X, float 接收者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 发出粒子效果 = new ParticleEffect();
			发出粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "ShenSkillRGiver.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			发出粒子 = new 粒子演员类(发出粒子效果);
			发出粒子.setPosition(发出者X, 发出者Y);
			延迟 = new 延迟Action(1.5f);
			发出粒子.addAction(延迟);
			ParticleEffect 接收粒子效果 = new ParticleEffect();
			接收粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "ShenSkillRReceiver.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			接收粒子 = new 粒子演员类(接收粒子效果);
			接收粒子.setPosition(接收者X, 接收者Y);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "ShenSkillR.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		@Override
		public boolean is完成()
		{
			if (延迟.isAction已结束())
			{
				发出粒子.remove();
				发出粒子.dispose();
				接收粒子.remove();
				接收粒子.dispose();
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
			对战屏幕类.实例.添加演员(发出粒子);
			对战屏幕类.实例.添加演员(接收粒子);
			音效.play();
		}
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		对战中英雄类 受伤最重友军 = 释放此技能的英雄.get所在阵容().get受伤最重英雄(true);
		if (受伤最重友军 != 释放此技能的英雄)
		{
			并行命令类 并行命令 = new 并行命令类();
			if (释放此技能的英雄.is我方())
			{
				并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.我方X坐标数组[受伤最重友军.get英雄所在位置()-1], 对战屏幕类.我方Y坐标数组[受伤最重友军.get英雄所在位置()-1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			else
			{
				并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.敌方X坐标数组[受伤最重友军.get英雄所在位置()-1], 对战屏幕类.敌方Y坐标数组[受伤最重友军.get英雄所在位置()-1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			Gdx.app.debug("秘奥义慈悲度魂落技能类.释放", String.format("给%s释放奥义慈悲度魂落\n", 受伤最重友军.get阵容名所在位置英雄名字()));
			并行命令.添加命令(new 状态文本显示命令类(受伤最重友军.is我方(), 受伤最重友军.get英雄所在位置(), "护盾", true));
			命令组装器类.实例.添加命令(并行命令);
			受伤最重友军.添加护盾状态(new 吸收伤害护盾状态类((int) (释放此技能的英雄.get对战中法术强度() * 附加吸收伤害所占法术强度比例 + 基础吸收伤害)));
			return true;
		}
		else
		{
			return false;
		}
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
		return "E";
	}
	@Override
	public boolean is大招()
	{
		return true;
	}
}
