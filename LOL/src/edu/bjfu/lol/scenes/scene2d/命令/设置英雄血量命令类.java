package edu.bjfu.lol.scenes.scene2d.命令;

import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.动作.设置英雄血量动作类;
import edu.bjfu.lol.screen.对战屏幕类;

public class 设置英雄血量命令类 extends 命令类
{
	private boolean is我方;
	private int 英雄所在位置;
	private float 百分比;
	public 设置英雄血量命令类(boolean is我方, int 英雄所在位置, float 百分比)
	{
		this.is我方 = is我方;
		this.英雄所在位置 = 英雄所在位置;
		this.百分比 = 百分比;
	}
	@Override
	protected 动作类 生成动作()
	{
		return new 设置英雄血量动作类(对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置), 百分比);
	}
}
