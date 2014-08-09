package edu.bjfu.lol.scenes.scene2d.命令;

import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.动作.状态文本显示动作类;
import edu.bjfu.lol.screen.对战屏幕类;

public class 状态文本显示命令类 extends 命令类
{
	private boolean is我方;
	private int 英雄所在位置;
	private String 文本;
	private boolean is增益;
	public 状态文本显示命令类(boolean is我方, int 英雄所在位置, String 文本, boolean is增益)
	{
		this.is我方 = is我方;
		this.英雄所在位置 = 英雄所在位置;
		this.文本 = 文本;
		this.is增益 = is增益;
	}
	@Override
	protected 动作类 生成动作()
	{
		return new 状态文本显示动作类(对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置), 文本, is增益);
	}
}
