package edu.bjfu.lol.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

public class 绝对移动Action extends MoveToAction implements Action结束状态接口
{
	private boolean isAction已结束;
	public 绝对移动Action(float x, float y, float duration)
	{
		setPosition(x, y);
		setDuration(duration);
	}
	@Override
	protected void end()
	{
		isAction已结束 = true;
	}
	@Override
	public boolean isAction已结束()
	{
		return isAction已结束;
	}
}
