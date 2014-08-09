package edu.bjfu.lol.英雄.皮城女警;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
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

class 和平使者技能类 extends 技能类 {

	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 5;
	private static final int 技能基础伤害 = 20;
	private static final float 技能附加伤害所占攻击力比例 = 1.3f;
	private static final float 伤害依次递减百分比 = 10f / 100;
	private static final String 技能名 = "和平使者";
	private static final String 技能描述 = String.format("%s  凯特琳加速转动步枪进行穿透射击,对目标造成%d(+%.1f攻击\n力)物理伤害.对目标身后目标的伤害依次递减%.0f%%.",
			技能名
			,技能基础伤害, 技能附加伤害所占攻击力比例, 伤害依次递减百分比 * 100);
	public 和平使者技能类() {
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
		private 相对移动Action 移动Action;
		private 粒子演员类 粒子;
		private Music 音效;
		public 释放动作类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			// 射击目标
			double 射程 = Math.sqrt((接收者y - 发出者y) * (接收者y - 发出者y) + (接收者x - 发出者x) * (接收者x - 发出者x)) * 2;
			double 目标角度 = Math.atan((接收者y - 发出者y) / (接收者x - 发出者x)) / Math.PI * 180;
			if (接收者x < 发出者x)
			{
				目标角度 += 180;
			}
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "CaitlynSkillQ.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子效果.findEmitter("Untitled").getRotation().setHigh((float) 目标角度 - 90);
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者x, 发出者y);
			double 弧度 = 目标角度 / 180 * Math.PI;
			float 相对目标X = (float) (射程 * Math.cos(弧度));
			float 相对目标Y = (float) (射程 * Math.sin(弧度));
			延迟Action 延迟 = new 延迟Action(0.18f);
			移动Action = new 相对移动Action(相对目标X, 相对目标Y, 0.5f);
			粒子.addAction(Actions.sequence(延迟, 移动Action));
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "CaitlynSkillQ.mp3"));
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
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄) {
		int 技能伤害 = (int) (释放此技能的英雄.get对战中攻击力() * 技能附加伤害所占攻击力比例 + 技能基础伤害);
		int 总体伤害 = 0;
		int 之前命中个数 = 0;
		int 数值穿透 = 释放此技能的英雄.get对战中数值护甲穿透();
		float 百分比穿透 = 释放此技能的英雄.get对战中百分比护甲穿透();
		并行命令类 并行命令 = new 并行命令类();
		float 接收者x = 0;
		float 接收者y = 0;
		int 接收者总数 = 0;
		for (对战中英雄类 敌人 : 敌方阵容.get竖排被攻击英雄数组(释放此技能的英雄))
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
				总体伤害 += 敌人.被造成物理伤害(释放此技能的英雄, (int) (技能伤害 * (1 - 之前命中个数 * 伤害依次递减百分比)), 数值穿透, 百分比穿透, 并行命令);
				之前命中个数++;
			}
		}
		接收者x /= 接收者总数;
		接收者y /= 接收者总数;
		if (释放此技能的英雄.is我方())
		{
			并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 接收者x, 接收者y, 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		else
		{
			并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 接收者x, 接收者y, 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		命令组装器类.实例.添加命令(并行命令);
		if (之前命中个数 > 0)
		{
			释放此技能的英雄.法术吸血(总体伤害, true);
		}
		return true;
	}
	@Override
	public String get技能描述() {
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
