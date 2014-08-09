package edu.bjfu.lol.英雄.暮光之眼;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;

import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.普通攻击触发效果状态类;
import edu.bjfu.lol.静态.技能类;

class 忍法诛邪斩技能类 extends 技能类
{
	private static final int 每过几回合 = 5;
	private static final int 基础伤害值 = 8;
	private static final float 额外伤害所占最大生命值百分比 = 10f / 100;
	private static final String 技能名 = "忍法诛邪斩";
	private static final String 技能描述 = String.format("%s  每过%d回合,慎的下一普通攻击就会造成额外%d(+%.0f%%最大生\n命值)魔法伤害",
			技能名, 每过几回合, 基础伤害值, 额外伤害所占最大生命值百分比 * 100);
	protected static class 忍法诛邪斩状态类 extends 普通攻击触发效果状态类
	{
		public 忍法诛邪斩状态类()
		{
			super(Integer.MAX_VALUE);
		}
		@Override
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器)
		{
			int 已过回合数 = (int) get已持续回合数();
			Gdx.app.debug("忍法诛邪斩技能类.忍法诛邪斩状态类.普通攻击", String.format("忍法诛邪斩，已过%d回合\n", 已过回合数));
			if (已过回合数 >= 每过几回合)
			{
				Gdx.app.debug("忍法诛邪斩技能类.忍法诛邪斩状态类.普通攻击", "释放忍法诛邪斩");
				并行命令类 并行命令 = new 并行命令类();
				并行命令.添加命令(new 状态文本显示命令类(攻击者.is我方(), 攻击者.get英雄所在位置(), "忍法诛邪斩", true));
				int 伤害 = 被攻击者.被造成魔法伤害(攻击者, (int) (攻击者.get对战中最大生命值() * 额外伤害所占最大生命值百分比), 攻击者.get对战中数值法术穿透(), 攻击者.get对战中百分比法术穿透(), 并行命令);
				命令组装器类.实例.添加命令(并行命令);
				攻击者.法术吸血(伤害, false);
				重置状态回合数();
			}
		}
		@Override
		public String get状态描述()
		{
			return 技能描述;
		}
		protected boolean is被动准备好了()
		{
			return get已持续回合数() >= 每过几回合;
		}
	}
	public 忍法诛邪斩技能类()
	{
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		对战中英雄.添加普通攻击触发效果状态(new 忍法诛邪斩状态类());
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
