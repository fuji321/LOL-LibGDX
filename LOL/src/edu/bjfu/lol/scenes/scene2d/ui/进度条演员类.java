package edu.bjfu.lol.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class 进度条演员类 extends Actor
{
	TextureRegion 前;
	TextureRegion 后;
	float 进度;
	public 进度条演员类(TextureRegion 前, TextureRegion 后, float 宽, int 高)
	{
		this.前 = 前;
		this.后 = 后;
		setSize(宽, 高);
	}
	public void set进度(float 进度)
	{
		this.进度 = 进度;
	}
	public boolean is完成()
	{
		return 进度 >= 1;
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		batch.draw(后, getX(), getY(), getWidth(), getHeight());
		batch.draw(前, getX(), getY(), getWidth() * 进度, getHeight());
	}
}
