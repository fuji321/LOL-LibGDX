package edu.bjfu.lol.screen;

import com.badlogic.gdx.Screen;

/**
 * 为了避免直接实现接口一大堆用不着的方法阻碍视线
 */
public class 屏幕类 implements Screen
{
	@Override
	public void render(float delta){}
	@Override
	public void resize(int width, int height){}
	@Override
	public void show(){}
	@Override
	public void hide(){}
	@Override
	public void pause(){}
	@Override
	public void resume(){}
	@Override
	public void dispose(){}
}
