package edu.bjfu.lol.scenes.scene2d.ui;

import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import edu.bjfu.lol.主类;
import edu.bjfu.lol.玩家信息类;
import edu.bjfu.lol.utils.图像计算类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.静态.英雄类;

public class 椭圆转盘演员类 extends Actor
{
	private static final float 一格所占角度 = (float) (2 * Math.PI / 5); 
	private static float 纵半轴长度 =  4.5f / 17.5f;
	public static float 转盘中心横坐标 = 常量类.屏幕宽 / 2 - 10;
	public static float 转盘中心纵坐标 = 320;
	public static final float 图最大宽度 = 常量类.屏幕宽 * 0.8f;
	public static final float 图最大高度 = 350;
	private 图类[] 图数组 = new 图类[5];
	private float 屏幕正对位置;
	private FloatAction 回弹动作 = new FloatAction();
	private boolean is触屏已松开 = true;
	private class 图类 extends Image
	{
		// 图在椭圆中的横坐标
		private float 横坐标;
		// 图在椭圆中的纵坐标
		private float 纵坐标;
		public 图类(英雄类 英雄)
		{
			super(英雄 == null ? 主类.实例.getUI皮肤资源().getRegion("HeroNull") : 英雄类.get英雄图像(英雄.get英雄皮肤文件名()));
		}
	}
	/**
	 * 为了绘图时先画靠近屏幕的，即纵坐标较小的
	 */
	private static final Comparator<图类> 图类绘图比较器 = new Comparator<图类>()
	{
		@Override
		public int compare(图类 o1, 图类 o2)
		{
			if (o2.纵坐标 > o1.纵坐标)
			{
				return 1;
			}
			else if (o2.纵坐标 < o1.纵坐标)
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
	};
	public void 重置英雄图像(int 英雄下标)
	{
		图类 旧图 = 图数组[英雄下标]; 
		图数组[英雄下标] = new 图类(玩家信息类.get阵容().get英雄数组()[英雄下标]);
		图像计算类.调整演员至不超过指定最大宽(图数组[英雄下标], 图最大宽度);
		图像计算类.调整演员至不超过指定最大高(图数组[英雄下标], 图最大高度);
		for (int i = 0; i < 排序绘图数组.length; i++)
		{
			if (旧图 == 排序绘图数组[i])
			{
				排序绘图数组[i] = 图数组[英雄下标];
				break;
			}
		}
	}
	public void 重置英雄图像()
	{
		英雄类[] 英雄数组 = 玩家信息类.get阵容().get英雄数组();
		for (int i = 0; i < 图数组.length; i++)
		{
			图数组[i].remove();
			图数组[i] = new 图类(英雄数组[i]);
		}
		for (Image 图 : 图数组)
		{
			图像计算类.调整演员至不超过指定最大宽(图, 图最大宽度);
			图像计算类.调整演员至不超过指定最大高(图, 图最大高度);
		}
		排序绘图数组 = Arrays.copyOf(图数组, 图数组.length);
	}
	/**
	 * @param 长宽比 横轴与纵轴的比例
	 */
	public 椭圆转盘演员类()
	{
		英雄类[] 英雄数组 = 玩家信息类.get阵容().get英雄数组();
		for (int i = 0; i < 图数组.length; i++)
		{
			图数组[i] = new 图类(英雄数组[i]);
		}
		for (Image 图 : 图数组)
		{
			图像计算类.调整演员至不超过指定最大宽(图, 图最大宽度);
			图像计算类.调整演员至不超过指定最大高(图, 图最大高度);
		}
		排序绘图数组 = Arrays.copyOf(图数组, 图数组.length);
		回弹动作.setDuration(0.3f);
		回弹动作.setEnd(0);
	}
	/**
	 * 用于绘图前先排序的数组
	 */
	private 图类[] 排序绘图数组;
	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		// 触屏已松开且尚未回弹至规定位置
		if (is触屏已松开 && 回弹动作.getValue() != 回弹动作.getEnd())
		{
			回弹动作.act(Gdx.graphics.getDeltaTime());
			屏幕正对位置 = 回弹动作.getValue();
		}
		算各个图的位置和缩放大小();
		Arrays.sort(排序绘图数组, 图类绘图比较器);
		for (图类 图 : 排序绘图数组)
		{
			图.draw(batch, parentAlpha);
		}
	}
	/**
	 * @param 旋转量 正数为逆时针旋转，周期为5
	 */
	public void 旋转屏幕正对位置(float 旋转量)
	{
		屏幕正对位置 += 旋转量 + 5;
		屏幕正对位置 %= 5f;
	}
	/**
	 * 椭圆参数方程
	 * x=cos(t)
	 * y=sin(t) * 纵半轴长度
	 */
	private void 算各个图的位置和缩放大小()
	{
		for (int 号 = 0; 号 < 5; 号++)
		{
			// 因为画图的0度是从xy坐标的-90度开始的
			float 角度 = (float) ((屏幕正对位置 + 号) * 一格所占角度 - Math.PI / 2);
			图数组[号].横坐标 = (float) Math.cos(角度);
			图数组[号].纵坐标 = (float) (Math.sin(角度) * 纵半轴长度);
			float 缩放比例 = (纵半轴长度 - 图数组[号].纵坐标) / 2 / 纵半轴长度 + 0.1f;
			图数组[号].setScale(缩放比例);
			图数组[号].setPosition(转盘中心横坐标 + getScaleX() * 图数组[号].横坐标 - 图数组[号].getWidth() * 缩放比例 / 2, 转盘中心纵坐标 + getScaleY() * 图数组[号].纵坐标);
			图数组[号].setColor(new Color(缩放比例, 缩放比例, 缩放比例, 1));
		}
	}
	public void 被通知触屏已松开()
	{
		is触屏已松开 = true;
		回弹动作.setStart(屏幕正对位置);
		回弹动作.setValue(屏幕正对位置);
		回弹动作.setEnd(Math.round(屏幕正对位置));
		回弹动作.restart();
	}
	public void 被通知触屏已按下()
	{
		is触屏已松开 = false;
	}
	public boolean is回弹结束()
	{
		return is触屏已松开 && 回弹动作.getValue() == 回弹动作.getEnd();
	}
	public int get屏幕正对英雄下标()
	{
		int 结果 = (int) 屏幕正对位置 % 5;
		if (结果 != 0)
		{
			结果 = 5 - 结果;
		}
		return 结果;
	}
	public void 转至目标位置(int 目标位置)
	{
		if (目标位置 != 0)
		{
			目标位置 = 5 - 目标位置;
		}
		if (目标位置 == 4 && 屏幕正对位置 == 0)
		{
			屏幕正对位置 = 5;
		}
		else if (目标位置 == 0 && 屏幕正对位置 == 4)
		{
			目标位置 = 5;
		}
		else if (目标位置 == 1 && 屏幕正对位置 == 5)
		{
			屏幕正对位置 = 0;
		}
		else if (目标位置 == 5 && 屏幕正对位置 == 1)
		{
			目标位置 = 0;
		}
		回弹动作.setStart(屏幕正对位置);
		回弹动作.setValue(屏幕正对位置);
		回弹动作.setEnd(目标位置);
		回弹动作.restart();
	}
}
