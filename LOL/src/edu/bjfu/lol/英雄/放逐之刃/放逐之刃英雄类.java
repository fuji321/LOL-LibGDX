package edu.bjfu.lol.英雄.放逐之刃;

import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄属性类;
import edu.bjfu.lol.静态.英雄类;

public class 放逐之刃英雄类 extends 英雄类
{
	private 技能类[] 技能数组 = new 技能类[5];
	private 英雄属性类 英雄属性 = new 英雄属性类();
	public 放逐之刃英雄类()
	{
		技能数组[0] = new 符文之刃技能类();
		技能数组[1] = new 折翼之舞技能类(); 
		技能数组[2] = new 震魂怒吼技能类();
		技能数组[3] = new 勇往直前技能类();
		技能数组[4] = new 放逐之锋技能类();
		英雄属性.set最大生命值(414);
		英雄属性.set护甲值(15);
		英雄属性.set先手值(345);
		英雄属性.set攻击力(54);
		英雄属性.set生命回复(5);
		英雄属性.set魔法抗性(30);
		英雄属性.set攻击速度(0.679f);
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
		return "放逐之刃";
	}
	@Override
	public String get英雄皮肤文件名()
	{
		return "Riven";
	}
	@Override
	public boolean is近战(对战中英雄类 对战中英雄)
	{
		return true;
	}
	@Override
	public String get英雄普攻声音文件名(对战中英雄类 对战中英雄)
	{
		return "RivenAttack.mp3";
	}
}
