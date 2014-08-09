package edu.bjfu.lol.英雄.嗜血猎手;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;

import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.普通攻击触发效果状态类;
import edu.bjfu.lol.静态.技能类;

class 血之饥渴技能类 extends 技能类
{
	private static final int 每层造成魔法伤害值 = 3;
	private static final int 最多叠加层数 = 3;
	private static final int 持续回合数 = 2;
	private static final String 技能名 = "血之饥渴";
	private static final String 技能描述 = String.format("%s  沃里克每次攻击造成%d点魔法伤害并回复等量生命值,对同一\n个单位持续攻击时效果叠加,最多叠加%d次,持续%d回合.", 技能名, 每层造成魔法伤害值, 最多叠加层数, 持续回合数);
	private static class 血之饥渴状态类 extends 普通攻击触发效果状态类
	{
		private 对战中英雄类 上一次被攻击英雄;
		private static class 血之饥渴持续状态类 extends 普通攻击触发效果状态类
		{
			public 血之饥渴持续状态类() {
				super(持续回合数, 最多叠加层数, 1);
			}
			@Override
			public String get状态描述() {
				return "血之饥渴持续状态";
			}
		}
		public 血之饥渴状态类() {
			super(Integer.MAX_VALUE);
		}
		@Override
		public String get状态描述() {
			return 技能描述;
		}
		@Override
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器) {
			普通攻击触发效果状态类 持续状态 = (普通攻击触发效果状态类) 攻击者.get普通攻击触发效果状态(血之饥渴持续状态类.class); 
			if (持续状态 == null)
			{
				上一次被攻击英雄 = 被攻击者;
				持续状态 = new 血之饥渴持续状态类();
				攻击者.添加普通攻击触发效果状态(持续状态);
			}
			else
			{
				持续状态.重置状态回合数();
				if (上一次被攻击英雄 != 被攻击者)
				{
					上一次被攻击英雄 = 被攻击者;
					持续状态.层数置一();
				}
				else
				{
					持续状态.增加一层(false);
				}
			}
			命令组装器类.实例.添加命令(new 状态文本显示命令类(攻击者.is我方(), 攻击者.get英雄所在位置(), "血之饥渴,层数" + 持续状态.get当前层数(), true));
			并行命令类 并行命令 = new 并行命令类();
			Gdx.app.debug("血之饥渴技能类.血之饥渴状态类.普通攻击", String.format("%s触发血之饥渴，当前层数为%d\n", 攻击者.get阵容名所在位置英雄名字(), 持续状态.get当前层数()));
			int 魔法伤害 = 被攻击者.被造成魔法伤害(攻击者, 持续状态.get当前层数() * 每层造成魔法伤害值, 攻击者.get对战中数值法术穿透(), 攻击者.get对战中百分比法术穿透(), 并行命令);
			攻击者.增加生命值(持续状态.get当前层数() * 每层造成魔法伤害值, 并行命令);
			命令组装器类.实例.添加命令(并行命令);
			攻击者.法术吸血(魔法伤害, false);			
		}
	}
	public 血之饥渴技能类()
	{
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		对战中英雄.添加普通攻击触发效果状态(new 血之饥渴状态类());
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
