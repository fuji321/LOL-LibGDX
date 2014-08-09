package edu.bjfu.lol.英雄.九尾妖狐;

import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;

class 摄魂夺魄技能类 extends 技能类
{
	private static final int 最大层数 = 9;
	private static final float 额外吸血百分比 = 35f / 100;
	private static final String 技能名 = "摄魂夺魄";
	private static final String 技能描述 = String.format("%s  在技能命中敌人后,阿狸会获得一层摄魂夺魄的充能(一个技\n能最多可以获得%d层充能).在获得%d层充能效果后,下一个技能会获得额外\n的%.0f%%法术吸血(若技能未命中，则该效果不会消耗).",
			技能名, 最大层数, 最大层数, 额外吸血百分比 * 100);
	protected static class 摄魂夺魄状态类 extends 状态类
	{
		private 对战中英雄类 受状态作用英雄;
		private static class 额外法术吸血状态类 extends 状态类
		{
			public 额外法术吸血状态类()
			{
				super(Integer.MAX_VALUE);
			}
			@Override
			public void 作用(对战中英雄类 受状态作用英雄)
			{
				受状态作用英雄.添加法术吸血百分比变化(额外吸血百分比);
			}
			@Override
			public String get状态描述()
			{
				return "摄魂夺魄，额外法术吸血";
			}
		}
		public 摄魂夺魄状态类(对战中英雄类 受状态作用英雄) {
			super(Integer.MAX_VALUE, 最大层数, 0);
			this.受状态作用英雄 = 受状态作用英雄;
		}
		private void 被通知本次技能命中个数(boolean is我方, int 英雄所在位置, int 命中个数)
		{
			while (命中个数 > 0)
			{
				增加一层(false);
				命中个数--;
			}
			if (get当前层数() == 最大层数)
			{
				命令组装器类.实例.添加命令(new 状态文本显示命令类(is我方, 英雄所在位置, "摄魂夺魄充能完毕", true));
				受状态作用英雄.添加法术吸血增加状态(new 额外法术吸血状态类());
			}
		}
		private void 被通知本次技能吸血()
		{
			if (get当前层数() == 最大层数)
			{
				层数清零();
			}
		}
		@Override
		public String get状态描述() {
			return 技能描述;
		}
		protected static void 法术吸血(boolean is我方, int 英雄所在位置, 对战中英雄类 释放此技能的英雄, int 命中个数, int 造成伤害, boolean is群体伤害)
		{
			摄魂夺魄状态类 摄魂夺魄状态 = (摄魂夺魄状态类) 释放此技能的英雄.get法术吸血增加状态(摄魂夺魄状态类.class);
			摄魂夺魄状态.被通知本次技能命中个数(is我方, 英雄所在位置, 命中个数);
			释放此技能的英雄.法术吸血(造成伤害, is群体伤害);
			摄魂夺魄状态.被通知本次技能吸血();
		}
	}
	public 摄魂夺魄技能类()
	{
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		对战中英雄.添加法术吸血增加状态(new 摄魂夺魄状态类(对战中英雄));
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
