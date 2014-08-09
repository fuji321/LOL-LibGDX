package edu.bjfu.lol.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class 粒子演员类 extends Actor implements Disposable
{
	private ParticleEffect 粒子效果;
	public 粒子演员类(ParticleEffect 粒子效果)
	{
		this.粒子效果 = 粒子效果;
	}
	@Override
	public void dispose()
	{
		粒子效果.dispose();
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		粒子效果.setPosition(getX(), getY());
		粒子效果.draw(batch);
	}
	@Override
	public void act(float delta)
	{
		super.act(delta);
		粒子效果.update(delta);
	}
	@Override
	public void setPosition(float x, float y)
	{
		super.setPosition(x, y);
	}
}
