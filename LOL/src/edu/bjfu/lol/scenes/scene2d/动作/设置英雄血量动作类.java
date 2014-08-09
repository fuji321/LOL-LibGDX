package edu.bjfu.lol.scenes.scene2d.动作;

import edu.bjfu.lol.screen.对战屏幕类.英雄造型类;

public class 设置英雄血量动作类 extends 动作类
{
	private 英雄造型类 英雄;
	private float 百分比;
	public 设置英雄血量动作类(英雄造型类 英雄, float 百分比)
	{
		this.英雄 = 英雄;
		this.百分比 = 百分比;
	}
	@Override
	public boolean is完成()
	{
		return true;
	}
	@Override
	public void 开始()
	{
		英雄.设置血量百分比(百分比);
	}
}