package edu.bjfu.lol.动态;

import java.util.Iterator;


public abstract class 状态类
{
	private final int 最大持续回合数;
	// 经实验得知float用于跟int作运算/比较足够精确
	private float 已经持续回合数;
	private int 最大层数;
	private int 当前层数;
	public 状态类(int 最大持续回合数)
	{
		this.最大持续回合数 = 最大持续回合数;
	}
	public 状态类(int 最大持续回合数, int 最大层数, int 初始层数)
	{
		this.最大持续回合数 = 最大持续回合数;
		this.最大层数 = 最大层数;
		this.当前层数 = 初始层数;
	}
	public float get已持续回合数()
	{
		return 已经持续回合数;
	}
	/**
	 * 
	 * @param 循环 true代表若增加层数大于最大层数将置层数为1，否则将保持最大层数
	 */
	public void 增加一层(boolean 循环)
	{
		当前层数++;
		if (当前层数 > 最大层数)
		{
			if (循环)
			{
				当前层数 = 1;
			}
			else
			{
				当前层数 = 最大层数;
			}
		}
	}
	public void 层数置一()
	{
		当前层数 = 1;
	}
	public void 层数清零()
	{
		当前层数 = 0;
	}
	public void 层数减一()
	{
		if (当前层数 > 0)
		{
			当前层数--;
		}
	}
	public int get当前层数()
	{
		return 当前层数;
	}
	/**
	 * 适用于没有存在于状态集合中的状态
	 */
	public 状态类 被通知时间已经过一回合()
	{
		已经持续回合数++;
		return 已经持续回合数 >= 最大持续回合数 ? null : this;
	}
	/**
	 * 通知时间已经过一回合(迭代器)
	 * @param 迭代器 由于当状态持续回合数到时需要从状态集合中移除，所以需要使用迭代器
	 */
	public void 被通知时间已经过一回合(Iterator<? extends 状态类> 迭代器)
	{
		被通知时间已经过一回合(迭代器, 0);
	}
	public void 状态延长(int 回合数)
	{
		已经持续回合数 -= 回合数;
	}
	public void 重置状态回合数()
	{
		已经持续回合数 = 0;
	}
	/**
	 * 通知时间已经过一回合(迭代器)
	 * @param 迭代器 由于当状态持续回合数到时需要从状态集合中移除，所以需要使用迭代器
	 * @param 持续时间缩减百分比持续时间缩减百分比用于缩减状态持续时间，它通过与最大持续回合数运算得出结果
	 */
	public void 被通知时间已经过一回合(Iterator<? extends 状态类> 迭代器, float 持续时间缩减百分比)
	{
		已经持续回合数 += 1f / (1-持续时间缩减百分比);
		if (已经持续回合数 >= 最大持续回合数)
		{
			迭代器.remove();
		}
	}
	/**
	 * 调用该方法使状态作用于英雄
	 */
	public void 作用(对战中英雄类 受状态作用英雄)
	{}
	/**
	 * 子类应实现该方法返回状态描述
	 * @return
	 */
	public abstract String get状态描述();
	/**
	 * 调试用
	 */
	public String get状态信息()
	{
		return String.format("%s，%.1f/%d", get状态描述(), 已经持续回合数, 最大持续回合数);
	}
}
