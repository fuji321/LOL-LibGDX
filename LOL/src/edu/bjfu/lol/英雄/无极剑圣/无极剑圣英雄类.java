package edu.bjfu.lol.英雄.无极剑圣;

import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄属性类;
import edu.bjfu.lol.静态.英雄类;

public class 无极剑圣英雄类 extends 英雄类
{
	private 技能类[] 技能数组 = new 技能类[5];
	private 英雄属性类 英雄属性 = new 英雄属性类();
	public 无极剑圣英雄类()
	{
		// 设置技能
		技能数组[0] = new 双重打击技能类();
		技能数组[1] = new 阿尔法突袭技能类();
		技能数组[2] = new 冥想技能类();
		技能数组[3] = new 无极剑道技能类();
		技能数组[4] = new 高原血统技能类();
		// 设置英雄属性
		英雄属性.set攻击力(58);
		英雄属性.set攻击速度(0.679f);
		英雄属性.set先手值(355);
		英雄属性.set护甲值(18);
		英雄属性.set魔法抗性(31);
		英雄属性.set最大生命值(536);
		英雄属性.set生命回复(7);
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
		return "无极剑圣";
	}
	@Override
	public String get英雄皮肤文件名()
	{
		return "MasterYi";
	}
	@Override
	public boolean is近战(对战中英雄类 对战中英雄)
	{
		return true;
	}
	@Override
	public String get英雄普攻声音文件名(对战中英雄类 对战中英雄)
	{
		return "MasterYiAttack.mp3";
	}
}
