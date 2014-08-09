package edu.bjfu.lol.英雄.众星之子;

import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;

class 奉献技能类 extends 技能类
{
	private static final int 魔法抗性提高值 = 16;
	private static final String 技能名 = "奉献";
	private static final String 技能描述 = String.format("%s  索拉卡使她自己和友方英雄的魔法抗性提高了%d.", 技能名, 魔法抗性提高值);
	private static class 奉献状态类 extends 状态类
	{
		public 奉献状态类() {
			super(Integer.MAX_VALUE);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄) {
			受状态作用英雄.添加魔法抗性数值变化(魔法抗性提高值);
		}
		@Override
		public String get状态描述() {
			return 技能描述;
		}
	}
	public 奉献技能类()
	{
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄) {
		// 友方英雄添加奉献状态
		对战中阵容类 友方阵容 = 对战中英雄.get所在阵容();
		for (对战中英雄类 友方英雄 : 友方阵容.get对战中英雄数组())
		{
			if (友方英雄 != null && !友方英雄.is阵亡())
			{
				友方英雄.添加魔法抗性增加状态(new 奉献状态类());
			}
		}
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
