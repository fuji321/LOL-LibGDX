package edu.bjfu.lol.scenes.scene2d.动作;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import edu.bjfu.lol.主类;
import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
import edu.bjfu.lol.scenes.scene2d.actions.相对移动Action;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.screen.对战屏幕类.英雄造型类;

public class 状态文本显示动作类 extends 动作类
{
	private static LabelStyle 增益标签样式;
	private static LabelStyle 减益标签样式;
	private Label 标签;
	private 延迟Action 技能提示延迟Action = new 延迟Action(0.8f);
	private 英雄造型类 英雄造型;
	public 状态文本显示动作类(英雄造型类 英雄造型, String 文本, boolean is增益)
	{
		this.英雄造型 = 英雄造型;
		if (is增益)
		{
			if (增益标签样式 == null)
			{
				增益标签样式 = new LabelStyle(主类.实例.getUI皮肤资源().getFont("default"), new Color(52f/255, 201f/255, 223f/255, 1));
			}
			标签 = new Label(文本, 增益标签样式);
		}
		else
		{
			if (减益标签样式 == null)
			{
				减益标签样式 = new LabelStyle(主类.实例.getUI皮肤资源().getFont("default"), new Color(230f/255, 40f/255, 30f/255, 1));
			}
			标签 = new Label(文本, 减益标签样式);
		}
		float 字体缩放比例 = 0.6f;
		标签.setFontScale(字体缩放比例);
		标签.setSize(标签.getWidth() * 字体缩放比例, 标签.getHeight() * 字体缩放比例);
		相对移动Action 技能提示上升Action = new 相对移动Action(0, 标签.getHeight(), 0.2f);
		SequenceAction 序列Action = Actions.sequence(技能提示上升Action, 技能提示延迟Action);
		标签.addAction(序列Action);
	}
	@Override
	public boolean is完成()
	{
		if (技能提示延迟Action.isAction已结束())
		{
			标签.remove();
			return true;
		}
		else
		{
			return false;
		}
	}
	@Override
	public void 开始()
	{
		标签.setPosition(英雄造型.getX() + 英雄造型.getWidth() / 2 - 标签.getWidth() / 2, 英雄造型.getHeight() + 英雄造型.getY() - 标签.getHeight());
		对战屏幕类.实例.添加演员(标签);
	}
}
