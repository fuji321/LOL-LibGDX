package edu.bjfu.lol.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class 延迟Action extends TemporalAction implements Action结束状态接口
{
	public 延迟Action(float duration)
	{
		setDuration(duration);
	}
	private boolean isAction已结束;
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
	@Override
	protected void update(float percent){}
}
