package edu.bjfu.lol.英雄.邪恶小法师;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
import edu.bjfu.lol.scenes.scene2d.actions.绝对移动Action;
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

class 黑暗物质技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 5;
	private static final int 基础伤害 = 120;
	private static final float 附加伤害所占法术强度比例 = 1;
	private static final String 技能名 = "黑暗物质";
	private static final String 技能描述 = String.format("%s  黑暗物质将从天而降,对敌方随机小范围单位造成%d(+%.0f法术\n强度)魔法伤害.", 
			技能名,
			基础伤害, 附加伤害所占法术强度比例);
	public 黑暗物质技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	private static class 释放命令类 extends 命令类
	{
		private float 接收者X;
		private float 接收者Y;
		private String 英雄皮肤文件名;
		public 释放命令类(float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			接收者X = 接收者x;
			接收者Y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(接收者X, 接收者Y, 英雄皮肤文件名);
		}
	}
	private static class 释放动作类 extends 动作类
	{
		private static final int 下落高度 = 300;
		private 延迟Action 下落前延迟 = new 延迟Action(0.9f);
		private Actor 下落前延迟演员 = new Actor();
		private 绝对移动Action 下落Action;
		private boolean is下落已开始;
		private boolean is下落已移除;
		private 延迟Action 爆炸延迟 = new 延迟Action(0.25f);
		private 粒子演员类 下落粒子;
		private 粒子演员类 爆炸粒子;
		private Music 音效;
		private 释放动作类(float 接收者X, float 接收者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "VeigarSkillWExplode.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			爆炸粒子 = new 粒子演员类(粒子效果);
			爆炸粒子.setPosition(接收者X, 接收者Y);
			爆炸粒子.addAction(爆炸延迟);
			粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "VeigarSkillWFall.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			下落粒子 = new 粒子演员类(粒子效果);
			下落粒子.setPosition(接收者X, 接收者Y + 下落高度);
			下落Action = new 绝对移动Action(接收者X, 接收者Y, 0.4f);
			下落粒子.addAction(下落Action);
			下落前延迟演员.addAction(下落前延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "VeigarSkillW.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		@Override
		public boolean is完成()
		{
			if (下落前延迟.isAction已结束())
			{
				if (!is下落已开始)
				{
					对战屏幕类.实例.添加演员(下落粒子);
					is下落已开始 = true;
				}
				else if (下落Action.isAction已结束())
				{
					if (!is下落已移除)
					{
						下落粒子.remove();
						下落粒子.dispose();
						对战屏幕类.实例.添加演员(爆炸粒子);
						is下落已移除 = true;
					}
					else if (爆炸延迟.isAction已结束())
					{
						爆炸粒子.remove();
						爆炸粒子.dispose();
						return true;
					}
				}
			}
			return false;
		}
		@Override
		public void 开始()
		{
			对战屏幕类.实例.添加演员(下落前延迟演员);
			音效.play();
		}
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		int 技能伤害 = (int) (基础伤害 + 附加伤害所占法术强度比例 * 释放此技能的英雄.get对战中法术强度());
		int 造成伤害 = 0;
		boolean is命中 = false;
		int 数值穿透 = 释放此技能的英雄.get对战中数值护甲穿透();
		float 百分比穿透 = 释放此技能的英雄.get对战中百分比护甲穿透();
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
				造成伤害 += 敌人.被造成魔法伤害(释放此技能的英雄, 技能伤害, 数值穿透, 百分比穿透, 并行命令);
				is命中 = true;
			}
		}
		接收者x /= 接收者总数;
		接收者y /= 接收者总数;
		命令组装器类.实例.添加命令(new 释放命令类(接收者x, 接收者y, 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		命令组装器类.实例.添加命令(并行命令);
		if (is命中)
		{
			释放此技能的英雄.法术吸血(造成伤害, true);
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
		return "B";
	}
}
