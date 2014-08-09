package edu.bjfu.lol.英雄.审判天使;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;

import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.普通攻击触发效果状态类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;

class 圣焰技能类 extends 技能类
{
	private static final float 护甲魔抗减少百分比 = 3f / 100;
	private static final int 效果持续回合数 = 2;
	private static final int 效果最大层数 = 5;
	private static final String 技能名 = "圣焰";
	private static final String 技能描述 = String.format("%s  当凯尔攻击一个敌方英雄,目标损失%.0f%%护甲和魔法抗性,持续%d回合,\n此效果最多可以叠加%d次.",
			技能名, 护甲魔抗减少百分比 * 100, 效果持续回合数, 效果最大层数);
	protected static class 圣焰状态类 extends 普通攻击触发效果状态类
	{
		private static class 护甲减少状态类 extends 状态类
		{
			public 护甲减少状态类()
			{
				super(效果持续回合数, 效果最大层数, 1);
			}
			@Override
			public void 作用(对战中英雄类 受状态作用英雄)
			{
				受状态作用英雄.添加护甲百分比变化(-护甲魔抗减少百分比 * get当前层数());
			}
			@Override
			public String get状态描述()
			{
				return "圣焰，护甲减少";
			}
		}
		private static class 魔抗减少状态类 extends 状态类
		{
			public 魔抗减少状态类()
			{
				super(效果持续回合数, 效果最大层数, 1);
			}
			@Override
			public void 作用(对战中英雄类 受状态作用英雄)
			{
				受状态作用英雄.添加魔法抗性百分比变化(-护甲魔抗减少百分比 * get当前层数());
			}
			@Override
			public String get状态描述()
			{
				return "圣焰，魔抗减少";
			}
		}
		public 圣焰状态类()
		{
			super(Integer.MAX_VALUE);
		}
		@Override
		public String get状态描述()
		{
			return 技能描述;
		}
		@Override
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器)
		{
			施加圣焰效果(被攻击者, null);
		}
		protected static void 施加圣焰效果(对战中英雄类 被施加英雄, 并行命令类 并行命令)
		{
			Gdx.app.debug("圣焰技能类.圣焰状态类.施加圣焰效果", String.format("%s被施加圣焰效果\n", 被施加英雄.get阵容名所在位置英雄名字()));
			状态类 护甲减少状态 = 被施加英雄.get护甲减少状态(护甲减少状态类.class);
			if (并行命令 == null)
			{
				命令组装器类.实例.添加命令(new 状态文本显示命令类(被施加英雄.is我方(), 被施加英雄.get英雄所在位置(), "双抗↓", false));
			}
			else
			{
				并行命令.添加命令(new 状态文本显示命令类(被施加英雄.is我方(), 被施加英雄.get英雄所在位置(), "双抗↓", false));
			}
			if (护甲减少状态 == null)
			{
				被施加英雄.添加护甲值减少状态(new 护甲减少状态类());
				被施加英雄.添加魔法抗性减少状态(new 魔抗减少状态类());
			}
			else
			{
				状态类 魔抗减少状态 = 被施加英雄.get魔法抗性减少状态(魔抗减少状态类.class);
				护甲减少状态.增加一层(false);
				魔抗减少状态.增加一层(false);
				护甲减少状态.重置状态回合数();
				魔抗减少状态.重置状态回合数();
			}
		}
	}
	public 圣焰技能类()
	{
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		对战中英雄.添加普通攻击触发效果状态(new 圣焰状态类());
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
