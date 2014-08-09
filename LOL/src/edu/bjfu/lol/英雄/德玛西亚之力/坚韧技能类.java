package edu.bjfu.lol.英雄.德玛西亚之力;

import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;

class 坚韧技能类 extends 技能类
{
	private static final float 每回合回复最大生命值百分比 = 0.5f / 100;
	private static final String 技能名 = "坚韧";
	private static final String 技能描述 = String.format("%s  盖伦每回合回复自己最大生命值的%.1f%%.", 技能名, 每回合回复最大生命值百分比 * 100);
	private static class 坚韧状态类 extends 状态类
	{
		public 坚韧状态类() {
			super(Integer.MAX_VALUE);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄) {
			并行命令类 并行命令 = new 并行命令类();
			并行命令.添加命令(new 状态文本显示命令类(受状态作用英雄.is我方(), 受状态作用英雄.get英雄所在位置(), "坚韧", true));
			受状态作用英雄.增加生命值((int)(受状态作用英雄.get对战中最大生命值() * 每回合回复最大生命值百分比), 并行命令);
			命令组装器类.实例.添加命令(并行命令);
		}
		@Override
		public String get状态描述() {
			return 技能描述;
		}
	}
	public 坚韧技能类()
	{
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		对战中英雄.添加生命值增加状态(new 坚韧状态类());
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
		return "A";
	}
}
