package edu.bjfu.lol.英雄.放逐之刃;

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
import edu.bjfu.lol.英雄.放逐之刃.符文之刃技能类.符文之刃状态类;
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 折翼之舞技能类 extends 技能类 {

	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 6;
	private static final int 基础伤害 = 30;
	private static final float 附加伤害所占攻击力比例 = 0.7f;
	private static final float 命中率 = 60f / 100;
	private static final int 击飞回合数 = 1;
	private static final String 技能名 = "折翼之舞";
	private static final String 技能描述 = String.format("%s  第一击:斩击,造成%d(+%.1f攻击力)物理伤害,第二击:斩击,造\n成%d(+%.1f攻击力)物理伤害,第三击:猛击地面,造成%d(+%.1f攻击力)物理\n伤害,将周围的敌人击飞,命中率%.0f%%,击飞回合数%d.", 
			技能名, 
			基础伤害, 附加伤害所占攻击力比例,
			基础伤害, 附加伤害所占攻击力比例,
			基础伤害, 附加伤害所占攻击力比例,
			命中率 * 100, 击飞回合数);
	public 折翼之舞技能类() {
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private float 接收者X;
		private float 接收者Y;
		private String 英雄皮肤文件名;
		private boolean is播放音效;
		public 释放命令类(float 接收者x, float 接收者y, String 英雄皮肤文件名, boolean is播放音效)
		{
			接收者X = 接收者x;
			接收者Y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
			this.is播放音效 = is播放音效;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(接收者X, 接收者Y, 英雄皮肤文件名, is播放音效);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(0.25f);
		private 粒子演员类 粒子;
		private Music 音效;
		private 释放动作类(float 接收者X, float 接收者Y, String 英雄皮肤文件名, boolean is播放音效)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "RivenSkillQ.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(接收者X, 接收者Y);
			粒子.addAction(延迟);
			if (is播放音效)
			{
				音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "RivenSkillQ.mp3"));
				音效.setOnCompletionListener(new 音乐播放完自动销毁类());
			}
		}
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
			if (音效 != null)
			{
				音效.play();
			}
		}
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄) {
		对战中英雄类 选中敌人 = 敌方阵容.get默认被攻击英雄(释放此技能的英雄);
		if (选中敌人 != null)
		{
			int 总伤害 = 0;
			int 技能命中次数 = 0;
			int 数值穿透 = 释放此技能的英雄.get对战中数值护甲穿透();
			float 百分比穿透 = 释放此技能的英雄.get对战中百分比护甲穿透();
			int 技能伤害 = (int) (基础伤害 + 附加伤害所占攻击力比例 * 释放此技能的英雄.get对战中攻击力());
			for (int 第几击 = 1; 第几击 <= 3; 第几击++)
			{
				boolean is命中 = false;
				并行命令类 并行命令 = new 并行命令类();
				if (选中敌人.is我方())
				{
					并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[选中敌人.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[选中敌人.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名(), 第几击 == 1));
				}
				else
				{
					并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[选中敌人.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[选中敌人.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名(), 第几击 == 1));
				}
				for (对战中英雄类 敌人 : 敌方阵容.get被攻击英雄所在小范围英雄数组(选中敌人))
				{
					if (敌人 != null && !敌人.is阵亡())
					{
						if (计算类.根据_概率_计算是否发生(命中率))
						{
							if (第几击 == 3)
							{
								敌人.被击飞(击飞回合数, 并行命令);
							}
							总伤害 += 敌人.被造成物理伤害(释放此技能的英雄, 技能伤害, 数值穿透, 百分比穿透, 并行命令);
							is命中 = true;
						}
					}
				}
				命令组装器类.实例.添加命令(并行命令);
				if (is命中)
				{
					技能命中次数++;
				}
			}
			if (技能命中次数 > 0)
			{
				符文之刃状态类.被通知技能命中次数(3, 释放此技能的英雄);
				释放此技能的英雄.法术吸血(总伤害, true);
			}
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
		return "B";
	}
}
