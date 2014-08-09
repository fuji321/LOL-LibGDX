package edu.bjfu.lol.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class 动画演员类 extends Actor
{
	private Animation 动画;
	private float 经过时间;
	public 动画演员类(float 帧间间隔, TextureRegion[] 帧数组, int 播放模式)
	{
		动画 = new Animation(帧间间隔, 帧数组);
		动画.setPlayMode(播放模式);
		setSize(帧数组[0].getRegionWidth(), 帧数组[0].getRegionHeight());
	}
	@Override
	public void act(float delta)
	{
		super.act(delta);
		经过时间 += delta;
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		batch.draw(动画.getKeyFrame(经过时间), getX(), getY(), getWidth(), getHeight());
	}
	public boolean is已经结束()
	{
		return 动画.isAnimationFinished(经过时间);
	}
}
