package edu.bjfu.lol.scenes.scene2d.命令;

import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.动作.攻击前摇动作类;
import edu.bjfu.lol.screen.对战屏幕类;

public class 攻击前摇命令类 extends 命令类
{
	private boolean is我方;
	private int 英雄所在位置;
	private boolean is奇数次攻击;
	private boolean is近战;
	public 攻击前摇命令类(boolean is我方, int 英雄所在位置, boolean is奇数次攻击, boolean is近战)
	{
		this.is我方 = is我方;
		this.英雄所在位置 = 英雄所在位置;
		this.is奇数次攻击 = is奇数次攻击;
		this.is近战 = is近战;
	}
	@Override
	protected 动作类 生成动作()
	{
		return new 攻击前摇动作类(对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置), is奇数次攻击, is近战, is我方);
	}
}
