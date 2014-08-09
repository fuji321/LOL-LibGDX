package edu.bjfu.lol.scenes.scene2d.动作;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import edu.bjfu.lol.scenes.scene2d.actions.相对旋转Action;
import edu.bjfu.lol.scenes.scene2d.actions.相对移动Action;


public class 攻击前摇动作类 extends 动作类
{
	private static final int 远程晃动距离 = 10;
	private static final float 远程总体晃动时间 = 0.3f;
	private static final float 近战摇摆角度 = 15f;
	public static final float 近战总体摆动时间 = 0.3f; 
	private Actor 英雄图片;
	private SequenceAction 远程跳动Action;
	private 相对移动Action 远程跳回Action;
	private SequenceAction 近战摇摆Action;
	private 相对旋转Action 近战回弹Action;
	private int Z序号;
	private boolean is近战;
	public 攻击前摇动作类(Actor 英雄图片, boolean is奇数次攻击, boolean is近战, boolean is我方)
	{
		this.英雄图片 = 英雄图片;
		this.is近战 = is近战;
		if (is近战)
		{
			RotateByAction 摇动动作 = Actions.rotateBy(is奇数次攻击 ? 近战摇摆角度 : -近战摇摆角度, 近战总体摆动时间 / 2);
			近战回弹Action = new 相对旋转Action(is奇数次攻击 ? -近战摇摆角度 : 近战摇摆角度, 近战总体摆动时间 / 2);
			近战摇摆Action = Actions.sequence(摇动动作, 近战回弹Action);
		}
		else
		{
			MoveByAction 跳出动作 = Actions.moveBy(0, is我方 ? 远程晃动距离 : -远程晃动距离, 远程总体晃动时间 / 2);
			远程跳回Action = new 相对移动Action(0, is我方 ? -远程晃动距离 : 远程晃动距离, 远程总体晃动时间 / 2);
			远程跳动Action = Actions.sequence(跳出动作, 远程跳回Action);
		}
	}
	@Override
	public boolean is完成()
	{
		if (is近战)
		{
			if (近战回弹Action.isAction已结束())
			{
				英雄图片.setZIndex(Z序号);
				return true;
			}
		}
		else
		{
			if (远程跳回Action.isAction已结束())
			{
				英雄图片.setZIndex(Z序号);
				return true;
			}
		}
		return false;
	}
	@Override
	public void 开始()
	{
		Z序号 = 英雄图片.getZIndex();
		英雄图片.setZIndex(Integer.MAX_VALUE);
		if (is近战)
		{
			英雄图片.addAction(近战摇摆Action);
		}
		else
		{
			英雄图片.addAction(远程跳动Action);
		}
	}
}
