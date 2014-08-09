package edu.bjfu.lol.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;

public class 相对旋转Action extends RotateByAction implements Action结束状态接口
{
	private boolean isAction已结束;
	public 相对旋转Action(float rotationAmount, float duration)
	{
		setAmount(rotationAmount);
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