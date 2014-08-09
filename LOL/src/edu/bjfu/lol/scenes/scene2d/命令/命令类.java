package edu.bjfu.lol.scenes.scene2d.命令;

import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.screen.对战屏幕类;

/**
 * 由服务器端生成命令，发送到客户端展现游戏对战动作
 */
public abstract class 命令类
{
	/**
	 * 实现该方法生成动作并添加到对战屏幕动作队列中
	 */
	public void 生成并添加动作(对战屏幕类 对战屏幕)
	{
		对战屏幕.添加动作(生成动作());
	}
	/**
	 * 实现该方法生成动作，用于组合成并行动作
	 */
	protected abstract 动作类 生成动作();
}
