package edu.bjfu.lol.英雄.寒冰射手;

import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;

class 全神贯注技能类 extends 技能类
{
	private static final float 暴击几率提高百分比 = 0.1f;
	private static final String 技能名 = "全神贯注";
	private static final String 技能描述 = String.format("%s  艾希全神贯注,提高暴击几率%.0f%%.", 技能名, 暴击几率提高百分比 * 100);
	private static class 全神贯注状态类 extends 状态类
	{
		public 全神贯注状态类()
		{
			super(Integer.MAX_VALUE);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			受状态作用英雄.添加暴击几率百分比变化(暴击几率提高百分比);
		}
		@Override
		public String get状态描述()
		{
			return 技能描述;
		}
	}
	public 全神贯注技能类()
	{
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		命令组装器类.实例.添加命令(new 状态文本显示命令类(对战中英雄.is我方(), 对战中英雄.get英雄所在位置(), "暴击几率↑", true));
		对战中英雄.添加暴击几率增加状态(new 全神贯注状态类());
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
