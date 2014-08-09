package edu.bjfu.lol.英雄.嗜血猎手;

import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.主动持续施法状态类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;

class 无尽束缚技能类 extends 技能类
{
	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 12;
	private static final int 压制回合数 = 1;
	private static final int 发起攻击次数 = 5;
	private static final int 技能基础伤害 = 50;
	private static final float 技能附加伤害所占攻击力比例 = 0.5f;
	private static final float 额外生命偷取百分比 = 30f/ 100;
	private static final String 技能名 = "无尽束缚";
	private static final String 技能描述 = String.format("%s  沃里克扑向敌方随机英雄,并将目标压制%d回合,在此期间,沃\n里克会向目标发起%d次攻击,并造成总共%d(+%.1f攻击力)魔法伤害(触发\n%d次普通攻击),释放技能时间内获得%.0f%%额外生命偷取.",
			技能名
			,压制回合数, 发起攻击次数, 技能基础伤害, 技能附加伤害所占攻击力比例, 发起攻击次数, 额外生命偷取百分比 * 100);
	private static class 束缚主动持续施法状态类 extends 主动持续施法状态类
	{
		private 对战中英雄类 被束缚英雄;
		public 束缚主动持续施法状态类(对战中英雄类 被束缚英雄)
		{
			super(压制回合数);
			this.被束缚英雄 = 被束缚英雄;
		}
		@Override
		protected void 被通知被打断(对战中英雄类 被打断英雄)
		{
			if (!被打断英雄.is阵亡())
			{
				被打断英雄.移除生命偷取增加状态(额外生命偷取状态类.class);
			}
			if (!被束缚英雄.is阵亡())
			{
				被束缚英雄.移除被压制状态(被压制状态类.class, null);
			}
		}
		@Override
		public String get状态描述()
		{
			return "无尽束缚持续施法";
		}
	}
	private static class 被压制状态类 extends edu.bjfu.lol.动态.对战中英雄类.被压制状态类
	{
		public 被压制状态类()
		{
			super(压制回合数);
		}
		@Override
		public String get状态描述()
		{
			return "被无尽束缚压制";
		}
	}
	private static class 额外生命偷取状态类 extends 状态类
	{
		public 额外生命偷取状态类()
		{
			super(压制回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄)
		{
			受状态作用英雄.添加生命偷取百分比变化(额外生命偷取百分比);
		}
		@Override
		public String get状态描述()
		{
			return "无尽束缚释放期间，沃里克获得额外生命偷取";
		}
	}
	public 无尽束缚技能类()
	{
		super(第一次释放回合数, 释放间隔回合数);
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		对战中英雄类 被攻击英雄 = 敌方阵容.get随机被攻击英雄(false);
		if (被攻击英雄 != null)
		{
			并行命令类 并行命令 = new 并行命令类();
			并行命令.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "额外生命偷取", true));
			被攻击英雄.添加被压制状态(new 被压制状态类(), 并行命令);
			命令组装器类.实例.添加命令(并行命令);
			释放此技能的英雄.添加生命偷取增加状态(new 额外生命偷取状态类());
			int 造成魔法伤害 = 被攻击英雄.被造成魔法伤害(释放此技能的英雄, (int) (技能基础伤害 + 技能附加伤害所占攻击力比例 * 释放此技能的英雄.get对战中攻击力()), 释放此技能的英雄.get对战中数值法术穿透(), 释放此技能的英雄.get对战中百分比法术穿透(), null);
			释放此技能的英雄.法术吸血(造成魔法伤害, false);
			for (int i = 0; i < 发起攻击次数 && !被攻击英雄.is阵亡(); i++)
			{
				释放此技能的英雄.普通攻击(被攻击英雄, 0, true, true);
			}
			释放此技能的英雄.设置主动持续施法状态(new 束缚主动持续施法状态类(被攻击英雄));
			return true;
		}
		else
		{
			return false;
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
		return "E";
	}
	@Override
	public boolean is大招()
	{
		return true;
	}
}
