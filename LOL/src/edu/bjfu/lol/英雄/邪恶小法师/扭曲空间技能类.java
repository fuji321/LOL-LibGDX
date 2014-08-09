package edu.bjfu.lol.英雄.邪恶小法师;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
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

class 扭曲空间技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 10;
	private static final int 眩晕回合数 = 1;
	private static final float 命中率 = 70f/ 100;
	private static final String 技能名 = "扭曲空间";
	private static final String 技能描述 = String.format("%s  维伽在敌方随机区域创造一个扭曲空间,在此区域边界的敌人\n会被眩晕%d回合,命中率%.0f%%.",
			技能名, 
			眩晕回合数, 命中率 * 100);
	public 扭曲空间技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	private static class 释放命令类 extends 命令类
	{
		private float 接收者x;
		private float 接收者y;
		private String 英雄皮肤文件名;
		public 释放命令类(float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			this.接收者x = 接收者x;
			this.接收者y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(接收者x, 接收者y, 英雄皮肤文件名);
		}
	}
	private static class 释放动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(1.2f);
		private 粒子演员类 粒子;
		private Music 音效;
		private 释放动作类(float 接收者X, float 接收者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "VeigarSkillE.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(接收者X, 接收者Y);
			粒子.addAction(延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "VeigarSkillE.mp3"));
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
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		float 接收者x = 0;
		float 接收者y = 0;
		int 接收者总数 = 0;
		并行命令类 并行命令 = new 并行命令类();
		for (对战中英雄类 敌人 : 敌方阵容.get随机被攻击小范围英雄数组())
		{
			if (敌人 != null && !敌人.is阵亡())
			{
				接收者总数++;
				if (敌人.is我方())
				{
					接收者x += 对战屏幕类.我方X坐标数组[敌人.get英雄所在位置()-1];
					接收者y += 对战屏幕类.我方Y坐标数组[敌人.get英雄所在位置()-1];
				}
				else
				{
					接收者x += 对战屏幕类.敌方X坐标数组[敌人.get英雄所在位置()-1];
					接收者y += 对战屏幕类.敌方Y坐标数组[敌人.get英雄所在位置()-1];
				}
				if (计算类.根据_概率_计算是否发生(命中率))
				{
					敌人.被眩晕(眩晕回合数, 并行命令);
				}
			}
		}
		接收者x /= 接收者总数;
		接收者y /= 接收者总数;
		并行命令.添加命令(new 释放命令类(接收者x, 接收者y, 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
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
		return "C";
	}
}
