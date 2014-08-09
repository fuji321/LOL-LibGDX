package edu.bjfu.lol.scenes.scene2d.动作;

import java.util.LinkedList;


public class 并行动作类 extends 动作类
{
	private LinkedList<动作类> 动作链表 = new LinkedList<动作类>();
	public void 添加动作(动作类 动作)
	{
		动作链表.add(动作);
	}
	/**
	 * 所有动作都完成才算完成
	 */
	@Override
	public boolean is完成()
	{
		boolean is完成 = true;
		for (int i = 0; i < 动作链表.size(); i++)
		{
			if (动作链表.get(i).is完成())
			{
				动作链表.remove(i);
				i--;
			}
			else
			{
				is完成 = false;
			}
		}
		return is完成;
	}
	@Override
	public void 开始()
	{
		for (动作类 动作 : 动作链表)
		{
			动作.开始();
		}
	}
}
