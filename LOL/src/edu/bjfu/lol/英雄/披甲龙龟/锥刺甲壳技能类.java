package edu.bjfu.lol.英雄.披甲龙龟;

import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;

class 锥刺甲壳技能类 extends 技能类
{
	private static final float 转化百分比 = 25f / 100;
	private static final String 技能名 = "锥刺甲壳";
	private static final String 技能描述 = String.format("%s  拉莫斯将%.0f%%的护甲值转化为攻击力.", 技能名, 转化百分比 * 100);
	private static class 锥刺甲壳状态类 extends 状态类 
	{
		public 锥刺甲壳状态类() {
			super(Integer.MAX_VALUE);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄) {
			受状态作用英雄.添加攻击力数值变化((int) (受状态作用英雄.get对战中护甲值() * 转化百分比));
		}
		@Override
		public String get状态描述() {
			return 技能描述;
		}
	}
	public 锥刺甲壳技能类()
	{
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		对战中英雄.添加攻击力增加状态(new 锥刺甲壳状态类());
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
