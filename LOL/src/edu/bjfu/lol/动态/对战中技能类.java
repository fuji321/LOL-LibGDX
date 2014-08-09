package edu.bjfu.lol.动态;

import com.badlogic.gdx.Gdx;

import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.静态.技能类;

public class 对战中技能类{
	private int 技能释放倒计回合数;
	private 技能类 技能;
	public 对战中技能类(技能类 技能)
	{
		this.技能 = 技能;
		技能释放倒计回合数 = 技能.get第一次释放回合数() - 1;
	}
	/**
	 * 调用此方法通知技能已过一回合
	 */
	public void 通知时间已过一回合()
	{
		技能释放倒计回合数--;
	}
	public void 重置技能冷却()
	{
		技能释放倒计回合数 = 0;
	}
	public void 减少技能冷却回合数(int 回合数)
	{
		技能释放倒计回合数 -= 回合数;
	}
	public int get剩余冷却回合数()
	{
		return 技能释放倒计回合数 <= 0 ? 0 : 技能释放倒计回合数;
	}
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		if (技能释放倒计回合数 <= 0)
		{
			Gdx.app.debug("对战中技能类.释放", String.format("%s释放%s\n", 释放此技能的英雄.get阵容名所在位置英雄名字(), 技能.get技能名()));
			if (技能.is大招())
			{
				命令组装器类.实例.添加命令(new 对战屏幕类.英雄释放大招前摇命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), 释放此技能的英雄.get英雄().get英雄皮肤文件名(), 技能.get技能名()));
			}
			else
			{
				命令组装器类.实例.添加命令(new 对战屏幕类.英雄释放技能前摇命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), 释放此技能的英雄.get英雄().get英雄皮肤文件名(), 技能.get技能文件后缀(), 技能.get技能名()));
			}
			boolean is释放成功 = 技能.释放(敌方阵容, 释放此技能的英雄);
			if (is释放成功)
			{
				技能释放倒计回合数 = 技能.get释放间隔回合数();	
			}
			命令组装器类.实例.添加命令(new 对战屏幕类.英雄释放技能后摇命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置()));
			return is释放成功;
		}
		return false;
	}
	public String get技能描述()
	{
		return 技能.get技能描述();
	}
}
