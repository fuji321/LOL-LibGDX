package edu.bjfu.lol.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class 图像计算类
{
	public static void 调整演员至不超过指定最大宽(Actor 演员, float 最大宽)
	{
		if (最大宽 < 演员.getWidth())
		{
			演员.setSize(最大宽, 演员.getHeight() * 最大宽 / 演员.getWidth());
		}
	}
	public static void 调整演员至不超过指定最大高(Actor 演员, float 最大高)
	{
		if (最大高 < 演员.getHeight())
		{
			演员.setSize(演员.getWidth() * 最大高 / 演员.getHeight(), 最大高);
		}
	}
}
