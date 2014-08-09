package edu.bjfu.lol.英雄.无极剑圣;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;

import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.普通攻击触发效果状态类;
import edu.bjfu.lol.静态.技能类;

class 双重打击技能类 extends 技能类
{
	private static final int 每第几次攻击 = 4;
	private static final float 第四次攻击附带攻击减少百分比 = 50f / 100;
	private static final String 技能名 = "双重打击";
	private static final String 技能描述 = String.format("%s  易大师每第%d次攻击将进行双重打击,第二次打击将造成%.0f%%\n伤害.", 技能名, 每第几次攻击, 第四次攻击附带攻击减少百分比 * 100);
	private static class 双重打击状态类 extends 普通攻击触发效果状态类
	{
		public 双重打击状态类()
		{
			super(Integer.MAX_VALUE, 每第几次攻击, 0);
		}
		@Override
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器)
		{
			增加一层(false);
			Gdx.app.debug("双重打击技能类.双重打击状态类.普通攻击", String.format("%s双重打击状态，现在是第%d次攻击\n", 攻击者.get阵容名所在位置英雄名字(), get当前层数()));
			if (get当前层数() == 每第几次攻击)
			{
				命令组装器类.实例.添加命令(new 状态文本显示命令类(攻击者.is我方(), 攻击者.get英雄所在位置(), "双重打击", true));
				Gdx.app.debug("双重打击技能类.双重打击状态类.普通攻击", String.format("%s触发双重打击，攻击减少%.0f%%\n", 攻击者.get阵容名所在位置英雄名字(), 第四次攻击附带攻击减少百分比 * 100));
				if (!被攻击者.is阵亡())
				{
					攻击者.普通攻击(被攻击者, 第四次攻击附带攻击减少百分比, true, true);
					层数置一();
				}
			}
		}
		@Override
		public String get状态描述()
		{
			return 技能描述;
		}
	}
	public 双重打击技能类()
	{
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		对战中英雄.添加普通攻击触发效果状态(new 双重打击状态类());
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
