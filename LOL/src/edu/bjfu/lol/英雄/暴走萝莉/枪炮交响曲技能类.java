package edu.bjfu.lol.英雄.暴走萝莉;

import java.util.Iterator;

import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.普通攻击触发效果状态类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;

class 枪炮交响曲技能类 extends 技能类 {
	private static final float 额外伤害百分比 = 10f / 100;
	private static final float 每层提高攻速百分比 = 3f / 100;
	private static final int 攻速提升持续回合数 = 2;
	private static final String 技能名 = "枪炮交响曲";
	private static final String 技能描述 = String.format("%s  金克丝的普通攻击对目标和周围敌人造成%.0f%%额外伤害,且\n每次普通攻击提高攻速%.0f%%,持续%d回合.", 
			技能名, 额外伤害百分比 * 100, 每层提高攻速百分比 * 100, 攻速提升持续回合数);
	private static class 普通攻击溅射状态类 extends 普通攻击触发效果状态类
	{
		public 普通攻击溅射状态类() {
			super(Integer.MAX_VALUE);
		}
		@Override
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器) {
			并行命令类 并行命令 = new 并行命令类();
			for (对战中英雄类 敌人 : 被攻击者.get所在阵容().get默认被攻击小范围英雄数组(攻击者))
			{
				if (敌人 != null && !敌人.is阵亡())
				{
					并行命令.添加命令(new 状态文本显示命令类(敌人.is我方(), 敌人.get英雄所在位置(), "溅射", false));
					敌人.被造成物理伤害(攻击者, (int)((敌人 == 被攻击者 ? 额外伤害百分比 : (额外伤害百分比 + 1)) * 攻击者.get对战中攻击力()), 攻击者.get对战中数值护甲穿透(), 攻击者.get对战中百分比护甲穿透(), 并行命令);
				}
			}
			命令组装器类.实例.添加命令(并行命令);
		}
		@Override
		public String get状态描述() {
			return "枪炮交响曲，普通攻击造成溅射伤害";
		}
	}
	private static class 普通攻击攻速提升状态类 extends 普通攻击触发效果状态类
	{
		private static class 攻速提升状态类 extends 状态类
		{
			public 攻速提升状态类() {
				super(攻速提升持续回合数, Integer.MAX_VALUE, 1);
			}
			@Override
			public void 作用(对战中英雄类 受状态作用英雄) {
				受状态作用英雄.添加攻击速度百分比变化(get当前层数() * 每层提高攻速百分比);
			}
			@Override
			public String get状态描述() {
				return "枪炮交响曲，攻速提升层数" + get当前层数();
			}
		}
		public 普通攻击攻速提升状态类() {
			super(Integer.MAX_VALUE);
		}
		@Override
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器) {
			状态类 攻速提升状态 = 攻击者.get攻击速度增加状态(攻速提升状态类.class);
			if (攻速提升状态 == null)
			{
				攻击者.添加攻击力增加状态(new 攻速提升状态类());
			}
			else
			{
				命令组装器类.实例.添加命令(new 状态文本显示命令类(攻击者.is我方(), 攻击者.get英雄所在位置(), "攻速↑", true));
				攻速提升状态.增加一层(false);
				攻速提升状态.重置状态回合数();
			}
		}
		@Override
		public String get状态描述() {
			return "枪炮交响曲，普通攻击会提高攻速";
		}
	}
	public 枪炮交响曲技能类() {
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
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
	public void 添加被动状态(对战中英雄类 对战中英雄) {
		对战中英雄.添加普通攻击触发效果状态(new 普通攻击溅射状态类());
		对战中英雄.添加普通攻击触发效果状态(new 普通攻击攻速提升状态类());
	}
	@Override
	public String get技能文件后缀()
	{
		return "B";
	}
}
