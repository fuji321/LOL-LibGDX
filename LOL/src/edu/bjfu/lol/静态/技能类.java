package edu.bjfu.lol.静态;

import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中阵容类;

public abstract class 技能类 {
	private int 第一次释放回合数;
	private int 释放间隔回合数;
	public int get第一次释放回合数() {
		return 第一次释放回合数;
	}
	public int get释放间隔回合数() {
		return 释放间隔回合数;
	}
	public 技能类(int 第一次释放回合数, int 释放间隔回合数)
	{
		this.第一次释放回合数 = 第一次释放回合数;
		this.释放间隔回合数 = 释放间隔回合数;
	}
	// 当子类技能需要添加被动状态时需要重载该方法
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{}
	/**
	 * 子类需实现该方法实现技能的释放
	 * @return 释放是否成功
	 */
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄){return true;}
	/**
	 * 子类实现该方法返回技能描述
	 */
	public abstract String get技能描述();
	/**
	 * 子类实现该方法返回技能名
	 */
	public abstract String get技能名();
	public abstract String get技能文件后缀();
	/**
	 * 默认为false,大招技能需重载该方法
	 */
	public boolean is大招()
	{
		return false;
	}
}
