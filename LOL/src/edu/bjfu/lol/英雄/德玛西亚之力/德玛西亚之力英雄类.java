package edu.bjfu.lol.英雄.德玛西亚之力;

import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄属性类;
import edu.bjfu.lol.静态.英雄类;

public class 德玛西亚之力英雄类 extends 英雄类 {
	private 技能类[] 技能数组 = new 技能类[5];
	private 英雄属性类 英雄属性 = new 英雄属性类();
	public 德玛西亚之力英雄类()
	{
		技能数组[0] = new 坚韧技能类();
		技能数组[1] = new 致命打击技能类();
		技能数组[2] = new 勇气技能类();
		技能数组[3] = new 审判技能类();
		技能数组[4] = new 德玛西亚正义技能类();
		英雄属性.set攻击力(56);
		英雄属性.set最大生命值(611);
		英雄属性.set生命回复(20);
		英雄属性.set攻击速度(0.625f);
		英雄属性.set先手值(345);
		英雄属性.set护甲值(22);
		英雄属性.set魔法抗性(31);
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
		return "德玛西亚之力";
	}
	@Override
	public String get英雄皮肤文件名()
	{
		return "Garen";
	}
	@Override
	public boolean is近战(对战中英雄类 对战中英雄)
	{
		return true;
	}
	@Override
	public String get英雄普攻声音文件名(对战中英雄类 对战中英雄)
	{
		if (对战中英雄.get普通攻击触发效果状态(致命打击技能类.下一次攻击造成额外伤害状态类.class) == null)
		{
			return "GarenAttack.mp3";
		}
		else
		{
			return "GarenSkillQSilence.mp3";
		}
	}
}
