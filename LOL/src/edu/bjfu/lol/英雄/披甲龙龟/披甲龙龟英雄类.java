package edu.bjfu.lol.英雄.披甲龙龟;

import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄属性类;
import edu.bjfu.lol.静态.英雄类;

public class 披甲龙龟英雄类 extends 英雄类 {

	private 技能类[] 技能数组 = new 技能类[5];
	private 英雄属性类 英雄属性 = new 英雄属性类();
	public 披甲龙龟英雄类() {
		技能数组[0] = new 锥刺甲壳技能类();
		技能数组[1] = new 动力冲刺技能类(); 
		技能数组[2] = new 尖刺防御技能类();
		技能数组[3] = new 破甲嘲讽技能类();
		技能数组[4] = new 地动山摇技能类();
		英雄属性.set最大生命值(420);
		英雄属性.set护甲值(25);
		英雄属性.set魔法抗性(30);
		英雄属性.set生命回复(8);
		英雄属性.set攻击力(50);
		英雄属性.set先手值(335);
		英雄属性.set攻击速度(0.63f);
	}
	@Override
	public 英雄属性类 get英雄属性() {
		return 英雄属性;
	}
	@Override
	public 技能类[] get技能数组() {
		return 技能数组;
	}
	@Override
	public String get英雄名字() {
		return "披甲龙龟";
	}
	@Override
	public String get英雄皮肤文件名()
	{
		return "Rammus";
	}
	@Override
	public boolean is近战(对战中英雄类 对战中英雄)
	{
		return true;
	}
	@Override
	public String get英雄普攻声音文件名(对战中英雄类 对战中英雄)
	{
		return "RammusAttack.mp3";
	}
}
