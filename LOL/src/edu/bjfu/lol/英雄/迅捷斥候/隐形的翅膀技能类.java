package edu.bjfu.lol.英雄.迅捷斥候;

import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;

class 隐形的翅膀技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 15	;
	private static final float 攻速提升百分比 = 40f / 100;
	private static final int 持续回合数 = 1;
	private static final String 技能名 = "隐形的翅膀";
	private static final String 技能描述 = String.format("%s  提莫进入潜行状态,并获得\"出奇制胜\"增益效果,提升%.0f%%攻\n击速度,持续%d回合.", 
			技能名,
			攻速提升百分比 * 100, 持续回合数);
	private static class 攻速提升状态类 extends 状态类
	{
		public 攻速提升状态类()
		{
			super(持续回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			受状态作用英雄.添加攻击速度百分比变化(攻速提升百分比);
		}
		@Override
		public String get状态描述()
		{
			return "隐形的翅膀，攻速提升";
		}
	}
	public 隐形的翅膀技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	@Override
	public String get技能描述()
	{
		return 技能描述;
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		并行命令类 并行命令 = new 并行命令类();
		并行命令.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "攻速↑，隐身", false));
		释放此技能的英雄.添加攻击速度增加状态(new 攻速提升状态类());
		释放此技能的英雄.隐身(持续回合数, 并行命令);
		命令组装器类.实例.添加命令(并行命令);
		return true;
	}
	@Override
	public String get技能名()
	{
		return 技能名;
	}
	@Override
	public String get技能文件后缀()
	{
		return "A";
	}
}
