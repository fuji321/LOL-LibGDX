package edu.bjfu.lol.英雄.放逐之刃;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;

import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.普通攻击触发效果状态类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;

class 符文之刃技能类 extends 技能类
{
	private static final int 最大层数 = 3;
	private static final float 额外伤害百分比 = 35f / 100;
	private static final String 技能名 = "符文之刃";
	private static final String 技能描述 = String.format("%s  瑞雯的技能将为她的剑刃充能,以使她的下一次普通攻击造成\n额外%.0f%%伤害,瑞雯最多可以充能%d次,每次攻击消耗一层充能.", 
			技能名, 额外伤害百分比 * 100, 最大层数);
	protected static class 符文之刃状态类 extends 普通攻击触发效果状态类
	{
		public 符文之刃状态类()
		{
			super(Integer.MAX_VALUE, 最大层数, 0);
		}
		@Override
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器)
		{
			if (get当前层数() > 0)
			{
				Gdx.app.debug("符文之刃技能类.符文之刃状态类.普通攻击", "层数:" + get当前层数());
				并行命令类 并行命令 = new 并行命令类();
				攻击者.被造成物理伤害(攻击者, (int)(额外伤害百分比 * 攻击者.get对战中攻击力()), 攻击者.get对战中数值护甲穿透(), 攻击者.get对战中百分比护甲穿透(), 并行命令);
				并行命令.添加命令(new 状态文本显示命令类(攻击者.is我方(), 攻击者.get英雄所在位置(), "符文之刃", true));
				命令组装器类.实例.添加命令(并行命令);
				层数减一();
			}
		}
		@Override
		public String get状态描述()
		{
			return 技能描述;
		}
		public static void 被通知技能命中次数(int 技能释放次数, 对战中英雄类 瑞雯)
		{
			状态类 符文之刃状态 = 瑞雯.get普通攻击触发效果状态(符文之刃状态类.class);
			while (技能释放次数 > 0)
			{
				符文之刃状态.增加一层(false);
				技能释放次数--;
			}
		}
	}
	public 符文之刃技能类()
	{
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		对战中英雄.添加普通攻击触发效果状态(new 符文之刃状态类());
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
