package edu.bjfu.lol.英雄.嗜血猎手;

import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄属性类;
import edu.bjfu.lol.静态.英雄类;

public class 嗜血猎手英雄类 extends 英雄类 {

	private 技能类[] 技能数组 = new 技能类[5];
	private 英雄属性类 英雄属性 = new 英雄属性类();
	public 嗜血猎手英雄类()
	{
		技能数组[0] = new 血之饥渴技能类();
		技能数组[1] = new 嗜血攻击技能类();
		技能数组[2] = new 猎手怒吼技能类();
		技能数组[3] = new 血迹追踪技能类();
		技能数组[4] = new 无尽束缚技能类();
		英雄属性.set攻击力(60);
		英雄属性.set攻击速度(0.679f);
		英雄属性.set先手值(345);
		英雄属性.set护甲值(20);
		英雄属性.set魔法抗性(31);
		英雄属性.set最大生命值(526);
		英雄属性.set生命回复(8);
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
		return "嗜血猎手";
	}
	@Override
	public String get英雄皮肤文件名()
	{
		return "Warwick";
	}
	@Override
	public boolean is近战(对战中英雄类 对战中英雄)
	{
		return true;
	}
	@Override
	public String get英雄普攻声音文件名(对战中英雄类 对战中英雄)
	{
		return "WarwickAttack.mp3";
	}
}
