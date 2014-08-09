package edu.bjfu.lol.英雄.德玛西亚之力;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.utils.图像计算类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.主动持续施法状态类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 审判技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 3;
	private static final int 释放间隔回合数 = 8;
	private static final int 持续施法回合数 = 2;
	private static final int 技能基础伤害 = 20;
	private static final float 技能附加伤害所占攻击力百分比 = 70f / 100;
	private static final float 命中率 = 0.7f;
	private static final float 先手值降低百分比 = 5f / 100;
	private static final int 先手值降低持续回合数 = 2;
	private static final String 技能名 = "审判";
	private static final String 技能描述 = String.format("%s  盖伦快速地挥舞大剑,每回合对敌方小范围造成%d加上他攻击力的\n%.0f%%的物理伤害,伤害可以暴击,持续%d回合,命中率%.0f%%,如果命中敌人盖\n伦自身的先手值会降低%.0f%%,持续%d回合.",
			技能名
			,技能基础伤害, 技能附加伤害所占攻击力百分比 * 100, 持续施法回合数, 命中率 * 100, 先手值降低百分比 * 100, 先手值降低持续回合数);
	private static class 审判主动持续施法状态类 extends 主动持续施法状态类
	{
		public 审判主动持续施法状态类()
		{
			super(持续施法回合数);
		}
		@Override
		public void 主动施法(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
		{
			super.主动施法(敌方阵容, 释放此技能的英雄);
			审判技能类.施法(敌方阵容, 释放此技能的英雄);
		}
		@Override
		public String get状态描述()
		{
			return "审判技能持续施法";
		}
	}
	private static class 先手值降低状态类 extends 状态类
	{
		public 先手值降低状态类()
		{
			super(先手值降低持续回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			受状态作用英雄.添加先手值百分比变化(-先手值降低百分比);
		}
		@Override
		public String get状态描述()
		{
			return "审判技能命中敌人，盖伦自身先手值降低";
		}
	}
	public 审判技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	private static class 释放命令类 extends 命令类
	{
		private float 发出者X;
		private float 发出者Y;
		private String 英雄皮肤文件名;
		public 释放命令类(float 发出者x, float 发出者y, String 英雄皮肤文件名)
		{
			发出者X = 发出者x;
			发出者Y = 发出者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(发出者X, 发出者Y, 英雄皮肤文件名);
		}
	}
	private static class 释放动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(1.9f);
		private Image 大剑;
		private Music 音效;
		private 释放动作类(float 发出者X, float 发出者Y, String 英雄皮肤文件名)
		{
			大剑 = new Image(英雄类.get皮肤资源(英雄皮肤文件名).getRegion("SkillE"));
			图像计算类.调整演员至不超过指定最大高(大剑, 250);
			大剑.setOrigin(大剑.getWidth() / 2, 0);
			RotateByAction 旋转Action = Actions.rotateBy(120, 0.1f);
			RepeatAction 永久旋转Action = Actions.forever(旋转Action);
			大剑.addAction(new ParallelAction(永久旋转Action, 延迟));
			大剑.setPosition(发出者X, 发出者Y);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "GarenSkillE.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		public boolean is完成()
		{
			if (延迟.isAction已结束())
			{
				大剑.remove();
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
			对战屏幕类.实例.添加演员(大剑);
			音效.play();
		}
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		审判技能类.施法(敌方阵容, 释放此技能的英雄);
		释放此技能的英雄.设置主动持续施法状态(new 审判主动持续施法状态类());
		return true;
	}
	private static void 施法(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		boolean is暴击 = 计算类.根据_概率_计算是否发生(释放此技能的英雄.get对战中暴击几率());
		int 伤害值 = (int) (释放此技能的英雄.get对战中攻击力() * 技能附加伤害所占攻击力百分比 + 技能基础伤害);
		并行命令类 并行命令 = new 并行命令类();
		if (is暴击)
		{
			并行命令.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "暴击", true));
			伤害值 *= 2;
		}
		int 总体造成伤害 = 0;
		int 数值穿透 = 释放此技能的英雄.get对战中数值护甲穿透();
		float 百分比穿透 = 释放此技能的英雄.get对战中百分比护甲穿透();
		if (释放此技能的英雄.is我方())
		{
			并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		else
		{
			并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		for (对战中英雄类 敌方英雄 : 敌方阵容.get默认被攻击小范围英雄数组(释放此技能的英雄))
		{
			if (敌方英雄 != null && !敌方英雄.is阵亡())
			{
				if (计算类.根据_概率_计算是否发生(命中率))
				{
					总体造成伤害 += 敌方英雄.被造成物理伤害(释放此技能的英雄, 伤害值, 数值穿透, 百分比穿透, 并行命令);
				}
			}
		}
		状态类 先手值降低状态 = 释放此技能的英雄.get先手值减少状态(先手值降低状态类.class); 
		if (先手值降低状态 != null)
		{
			并行命令.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "先手值↓", false));
			先手值降低状态.重置状态回合数();
		}
		else
		{
			释放此技能的英雄.添加先手值减少状态(new 先手值降低状态类());
		}
		释放此技能的英雄.法术吸血(总体造成伤害, true);
		命令组装器类.实例.添加命令(并行命令);
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
		return "D";
	}
}