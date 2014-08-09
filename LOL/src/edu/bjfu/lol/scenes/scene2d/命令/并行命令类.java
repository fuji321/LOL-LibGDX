package edu.bjfu.lol.scenes.scene2d.命令;

import java.util.LinkedList;

import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.动作.并行动作类;
import edu.bjfu.lol.screen.对战屏幕类;

/**
 * 并行命令类用于生成并行动作类展示对战结果
 */
public class 并行命令类 extends 命令类
{
	private LinkedList<命令类> 命令链表 = new LinkedList<命令类>();
	@Override
	public void 生成并添加动作(对战屏幕类 对战屏幕)
	{
		并行动作类 并行动作 = new 并行动作类();
		for (命令类 命令 : 命令链表)
		{
			并行动作.添加动作(命令.生成动作());
		}
		对战屏幕类.实例.添加动作(并行动作);
	}
	public void 添加命令(命令类 命令)
	{
		命令链表.add(命令);
	}
	/**
	 * 并行动作不可再组成并行动作，所以返回null即可
	 */
	@Override
	public 动作类 生成动作()
	{
		return null;
	}
}
