package edu.bjfu.lol.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction;

public class 保持中心X大小绝对变化Action extends SizeToAction implements Action结束状态接口
{
	private boolean isAction已结束;
	public 保持中心X大小绝对变化Action(float width, float height, float duration)
	{
		setSize(width, height);
		setDuration(duration);
	}
	protected void update (float percent) {
		float x = actor.getX();
		float 中心X = x + actor.getWidth() / 2;
		super.update(percent);
		actor.setX(中心X - actor.getWidth() / 2);
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
