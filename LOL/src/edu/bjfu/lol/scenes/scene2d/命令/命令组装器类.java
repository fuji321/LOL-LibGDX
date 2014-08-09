package edu.bjfu.lol.scenes.scene2d.命令;

import java.util.LinkedList;
import java.util.Queue;

import edu.bjfu.lol.scenes.scene2d.命令.命令类;

public class 命令组装器类
{
	public static 命令组装器类 实例 = new 命令组装器类();
	private Queue<命令类> 命令队列 = new LinkedList<命令类>();
	private Queue<命令类> 延迟添加命令队列 = new LinkedList<命令类>();
	/**
	 * 将命令添加到延迟添加队列中，该命令将会在下一个添加命令被调用时添加到那个命令的后面
	 */
	public void 延迟添加命令(命令类 命令)
	{
		延迟添加命令队列.add(命令);
	}
	public void 添加命令(命令类 命令)
	{
		命令队列.add(命令);
		if (!延迟添加命令队列.isEmpty())
		{
			命令队列.addAll(延迟添加命令队列);
			延迟添加命令队列.clear();
		}
	}
	/**
	 * 命令组装完之后调用该方法得到命令队列
	 */
	public Queue<命令类> get命令队列()
	{
		if (!延迟添加命令队列.isEmpty())
		{
			命令队列.addAll(延迟添加命令队列);
			延迟添加命令队列.clear();
		}
		return 命令队列;
	}
}
