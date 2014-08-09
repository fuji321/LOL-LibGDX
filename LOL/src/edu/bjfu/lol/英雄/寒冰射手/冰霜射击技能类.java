package edu.bjfu.lol.英雄.寒冰射手;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;

import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.普通攻击触发效果状态类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;

class 冰霜射击技能类 extends 技能类
{
	private static final float 先手值降低百分比 = -0.15f;
	private static final int 先手值降低持续回合数 = 2;
	private static final String 技能名 = "冰霜射击";
	private static final String 技能描述 = String.format("%s  艾希的每次普通攻击减少目标%.0f%%的先手值,持续%d回合,该效\n果不可叠加.",
			技能名, -先手值降低百分比 * 100, 先手值降低持续回合数);
	protected static class 冰霜射击状态类 extends 普通攻击触发效果状态类
	{
		private static class 先手值降低状态类 extends 状态类
		{
			public 先手值降低状态类()
			{
				super(先手值降低持续回合数);
			}
			@Override
			public void 作用(对战中英雄类 受状态作用英雄)
			{
				受状态作用英雄.添加先手值百分比变化(先手值降低百分比);
			}
			@Override
			public String get状态描述()
			{
				return "受到冰霜射击作用，先手值降低";
			}
		}
		public 冰霜射击状态类()
		{
			super(Integer.MAX_VALUE);
		}
		@Override
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器)
		{
			Gdx.app.debug("冰霜射击技能类.冰霜射击状态类.普通攻击", String.format("来自冰霜射击，%s向%s添加先手值降低状态\n", 攻击者.get阵容名所在位置英雄名字(), 被攻击者.get阵容名所在位置英雄名字()));
			添加先手值降低状态(被攻击者, null);
		}
		protected static void 添加先手值降低状态(对战中英雄类 被攻击者, 并行命令类 并行命令)
		{
			if (并行命令 == null)
			{
				命令组装器类.实例.添加命令(new 状态文本显示命令类(被攻击者.is我方(), 被攻击者.get英雄所在位置(), "先手值↓", false));
			}
			else
			{
				并行命令.添加命令(new 状态文本显示命令类(被攻击者.is我方(), 被攻击者.get英雄所在位置(), "先手值↓", false));
			}
			状态类 先手值降低状态 = 被攻击者.get先手值减少状态(先手值降低状态类.class);
			// 该状态不可叠加
			if (先手值降低状态 == null)
			{
				被攻击者.添加先手值减少状态(new 先手值降低状态类());
			}
			else
			{
				先手值降低状态.重置状态回合数();
			}
		}
		@Override
		public String get状态描述()
		{
			return 技能描述;
		}
	}
	public 冰霜射击技能类()
	{
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		对战中英雄.添加普通攻击触发效果状态(new 冰霜射击状态类());
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
		return "B";
	}
}
