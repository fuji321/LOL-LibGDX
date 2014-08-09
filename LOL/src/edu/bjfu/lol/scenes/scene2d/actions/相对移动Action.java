package edu.bjfu.lol.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;

public class 相对移动Action extends MoveByAction implements Action结束状态接口
{
	private boolean isAction已结束;
	public 相对移动Action(float amountX, float amountY, float duration)
	{
		setAmount(amountX, amountY);
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
