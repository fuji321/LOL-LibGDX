package edu.bjfu.lol.英雄.暴走萝莉;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

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
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 超究极死神飞弹技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 3;
	private static final int 释放间隔回合数 = 15;
	private static final int 第一排基础伤害 = 150;
	private static final int 第二排基础伤害 = 200;
	private static final int 第三排基础伤害 = 250;
	private static final float 附加伤害所占攻击力比例 = 0.5f;
	private static final float 目标失去生命值百分比 = 25f / 100;
	private static final float 周围敌人承受伤害百分比 = 80f / 100;
	private static final float 命中率 = 70f / 100;
	private static final String 技能名 = "超究极死神飞弹";
	private static final String 技能描述 = String.format("%s  射出一支火箭,火箭穿越时间越长,伤害值越大,在击中\n第一个敌人后火箭爆炸,输出%d/%d/%d(+%.1f攻击力)加上%.0f%%目标\n失去生命值的物理伤害,附近的敌人会承受%.0f%%伤害,命中率%.0f%%.", 
			技能名,
			第一排基础伤害, 第二排基础伤害, 第三排基础伤害, 附加伤害所占攻击力比例, 目标失去生命值百分比 * 100, 周围敌人承受伤害百分比 * 100, 命中率 * 100);
	public 超究极死神飞弹技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(0.1f);
		private 粒子演员类 粒子;
		private Music 音效;
		public 释放动作类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "JinxSkillR.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			float 角度 = (float)(Math.atan((接收者y-发出者y)/(接收者x-发出者x)) / Math.PI * 180) - 90;
			if (接收者x < 发出者x)
			{
				角度 += 180;
			}
			粒子效果.findEmitter("Untitled").getRotation().setHigh(角度);
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者x, 发出者y);
			绝对移动Action 移动Action = new 绝对移动Action(接收者x, 接收者y, 0.3f);
			粒子.addAction(Actions.sequence(移动Action, 延迟));
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "JinxSkillR.mp3"));
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
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		对战中英雄类 命中敌人 = 敌方阵容.get随机被攻击英雄(true);
		if (计算类.根据_概率_计算是否发生(命中率))
		{
			int 命中敌人最大生命值 = 命中敌人.get对战中最大生命值();
			int 技能伤害 = (int) (释放此技能的英雄.get对战中攻击力() * 附加伤害所占攻击力比例 + 目标失去生命值百分比 * ((命中敌人最大生命值 - 命中敌人.get对战中生命值()) / 命中敌人最大生命值));
			int 数值穿透 = 释放此技能的英雄.get对战中数值护甲穿透();
			float 百分比穿透 = 释放此技能的英雄.get对战中百分比护甲穿透();
			int 总伤害 = 0;
			switch (命中敌人.get英雄所在位置())
			{
				case 1:
				case 2:
					技能伤害 += 第一排基础伤害;
					break;
				case 3:
				case 4:
					技能伤害 += 第二排基础伤害;
					break;
				case 5:
					技能伤害 += 第三排基础伤害;
					break;
			}
			并行命令类 并行命令 = new 并行命令类();
			总伤害 += 命中敌人.被造成物理伤害(释放此技能的英雄, 技能伤害, 数值穿透, 百分比穿透, 并行命令);
			if (释放此技能的英雄.is我方())
			{
				并行命令.添加命令(new 释放命令类(
						对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.敌方X坐标数组[命中敌人.get英雄所在位置() - 1],
						对战屏幕类.敌方Y坐标数组[命中敌人.get英雄所在位置() - 1],
						释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			else
			{
				并行命令.添加命令(new 释放命令类(
						对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
						对战屏幕类.我方X坐标数组[命中敌人.get英雄所在位置() - 1],
						对战屏幕类.我方Y坐标数组[命中敌人.get英雄所在位置() - 1],
						释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			int 溅射伤害 = (int) (技能伤害 * 周围敌人承受伤害百分比);
			for (对战中英雄类 周围敌人 : 敌方阵容.get被攻击英雄所在小范围英雄数组(命中敌人))
			{
				if (周围敌人 != null && !周围敌人.is阵亡() && 周围敌人 != 命中敌人)
				{
					if (计算类.根据_概率_计算是否发生(命中率))
					{
						总伤害 += 周围敌人.被造成物理伤害(释放此技能的英雄, 溅射伤害, 数值穿透, 百分比穿透, 并行命令);
					}
				}
			}
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
		return "E";
	}
	@Override
	public boolean is大招()
	{
		return true;
	}
}
