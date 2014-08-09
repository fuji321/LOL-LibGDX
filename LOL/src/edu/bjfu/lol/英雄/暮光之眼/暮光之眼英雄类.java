package edu.bjfu.lol.英雄.暮光之眼;

import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.英雄.暮光之眼.忍法诛邪斩技能类.忍法诛邪斩状态类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄属性类;
import edu.bjfu.lol.静态.英雄类;

public class 暮光之眼英雄类 extends 英雄类
{
	private 技能类[] 技能数组 = new 技能类[5];
	private 英雄属性类 英雄属性 = new 英雄属性类();
	public 暮光之眼英雄类()
	{
		技能数组[0] = new 忍法诛邪斩技能类();
		技能数组[1] = new 奥义却邪技能类();
		技能数组[2] = new 奥义空我技能类();
		技能数组[3] = new 奥义影缚技能类();
		技能数组[4] = new 秘奥义慈悲度魂落技能类();
		英雄属性.set护甲值(19);
		英雄属性.set魔法抗性(30);
		英雄属性.set先手值(335);
		英雄属性.set攻击速度(0.651f);
		英雄属性.set攻击力(58);
		英雄属性.set生命回复(8);
		英雄属性.set最大生命值(513);
	}
	@Override
	public 英雄属性类 get英雄属性()
	{
		return 英雄属性;
	}
	@Override
	public 技能类[] get技能数组()
	{
		return 技能数组;
	}
	@Override
	public String get英雄名字()
	{
		return "暮光之眼";
	}
	@Override
	public String get英雄皮肤文件名()
	{
		return "Shen";
	}
	@Override
	public boolean is近战(对战中英雄类 对战中英雄)
	{
		return true;
	}
	@Override
	public String get英雄普攻声音文件名(对战中英雄类 对战中英雄)
	{
		if (((忍法诛邪斩状态类)对战中英雄.get普通攻击触发效果状态(忍法诛邪斩技能类.忍法诛邪斩状态类.class)).is被动准备好了())
		{
			return "ShenAttackWithSkillPassive.mp3";
		}
		else
		{
			return "ShenAttackWithoutSkillPassive.mp3";
		}
	}
}
