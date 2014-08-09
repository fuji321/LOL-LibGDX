package edu.bjfu.lol.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class 围绕原点转圈移动Action extends TemporalAction
{
	private float 原点x;
	private float 原点y;
	private float 半径;
	public 围绕原点转圈移动Action(float 原点x, float 原点y, float 半径, float duration)
	{
		this.半径 = 半径;
		this.原点x = 原点x;
		this.原点y = 原点y;
		setDuration(duration);
	}
	@Override
	protected void update(float percent)
	{
		double 弧度 = percent * 2 * Math.PI;
		actor.setX((float) (Math.cos(弧度) * 半径 + 原点x));
		actor.setY((float) (Math.sin(弧度) * 半径 + 原点y));
	}
	@Override
	public boolean act(float delta)
	{
		return super.act(delta);
	}
}