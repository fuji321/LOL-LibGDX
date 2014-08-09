package edu.bjfu.lol;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import edu.bjfu.lol.英雄.九尾妖狐.九尾妖狐英雄类;
import edu.bjfu.lol.英雄.众星之子.众星之子英雄类;
import edu.bjfu.lol.英雄.卡牌大师.卡牌大师英雄类;
import edu.bjfu.lol.英雄.嗜血猎手.嗜血猎手英雄类;
import edu.bjfu.lol.英雄.审判天使.审判天使英雄类;
import edu.bjfu.lol.英雄.寒冰射手.寒冰射手英雄类;
import edu.bjfu.lol.英雄.德玛西亚之力.德玛西亚之力英雄类;
import edu.bjfu.lol.英雄.披甲龙龟.披甲龙龟英雄类;
import edu.bjfu.lol.英雄.放逐之刃.放逐之刃英雄类;
import edu.bjfu.lol.英雄.无极剑圣.无极剑圣英雄类;
import edu.bjfu.lol.英雄.暮光之眼.暮光之眼英雄类;
import edu.bjfu.lol.英雄.暴走萝莉.暴走萝莉英雄类;
import edu.bjfu.lol.英雄.皮城女警.皮城女警英雄类;
import edu.bjfu.lol.英雄.迅捷斥候.迅捷斥候英雄类;
import edu.bjfu.lol.英雄.邪恶小法师.邪恶小法师英雄类;
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.英雄类;
import edu.bjfu.lol.静态.阵容类;

public class 对手信息类
{
	private static final 阵容类 阵容 = new 阵容类();
	private static final Preferences 阵容信息;
	private static final LinkedList<Class<? extends 英雄类>> 英雄类列表 = new LinkedList<Class<? extends 英雄类>>();
	static
	{
		阵容信息 = Gdx.app.getPreferences("对手阵容信息");
		英雄类列表.add(暴走萝莉英雄类.class);
		英雄类列表.add(德玛西亚之力英雄类.class);
		英雄类列表.add(放逐之刃英雄类.class);
		英雄类列表.add(寒冰射手英雄类.class);
		英雄类列表.add(九尾妖狐英雄类.class);
		英雄类列表.add(卡牌大师英雄类.class);
		英雄类列表.add(暮光之眼英雄类.class);
		英雄类列表.add(披甲龙龟英雄类.class);
		英雄类列表.add(皮城女警英雄类.class);
		英雄类列表.add(审判天使英雄类.class);
		英雄类列表.add(嗜血猎手英雄类.class);
		英雄类列表.add(无极剑圣英雄类.class);
		英雄类列表.add(邪恶小法师英雄类.class);
		英雄类列表.add(迅捷斥候英雄类.class);
		英雄类列表.add(众星之子英雄类.class);
		英雄类[] 英雄数组 = new 英雄类[5];
		boolean is第一次生成阵容 = true;
		for (int i = 0; i < 英雄数组.length; i++)
		{
			String 类名 = 阵容信息.getString(i+"");
			if (类名.length() > 0)
			{
				try
				{
					英雄数组[i] = (英雄类) Class.forName(类名).newInstance();
					is第一次生成阵容 = false;
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		if (is第一次生成阵容)
		{
			英雄数组 = 生成随机英雄数组(英雄类列表);
			阵容.set英雄数组(英雄数组);
			保存阵容();
		}
		else
		{
			阵容.set英雄数组(英雄数组);
		}
	}
	private static 英雄类[] 生成随机英雄数组(LinkedList<Class<? extends 英雄类>> 类列表)
	{
		英雄类[] 英雄数组 = new 英雄类[5];
		LinkedList<Class<? extends 英雄类>> 上场英雄类列表 = new LinkedList<Class<? extends 英雄类>>();
		上场英雄类列表.addAll(类列表);
		for (int i = 0; i < 英雄数组.length; i++)
		{
			int 随机数 = 计算类.随机整数值(上场英雄类列表.size() + 1);
			if (随机数 < 上场英雄类列表.size())
			{
				try
				{
					英雄数组[i] = 上场英雄类列表.get(随机数).newInstance();
					上场英雄类列表.remove(随机数);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		return 英雄数组;
	}
	private static boolean is两个英雄数组一样(英雄类[] 数组一, 英雄类[] 数组二)
	{
		boolean is一样 = true;
		for (int i = 0; i < 数组一.length; i++)
		{
			if (数组一[i] == null)
			{
				if (数组二[i] != null)
				{
					is一样 = false;
					break;
				}
			}
			else
			{
				if (数组二[i] != null && 数组一[i].getClass() != 数组二[i].getClass())
				{
					is一样 = false;
					break;
				}
				else
				{
					is一样 = false;
					break;
				}
			}
		}
		return is一样;
	}
	public static 阵容类 get阵容()
	{
		return 阵容;
	}
	public static void 生成不一样的随机阵容()
	{
		英雄类[] 先前英雄数组 = 阵容.get英雄数组();
		while (true)
		{
			英雄类[] 新英雄数组 = 生成随机英雄数组(英雄类列表);
			if (!is两个英雄数组一样(先前英雄数组, 新英雄数组))
			{
				阵容.set英雄数组(新英雄数组);
				break;
			}
		}
		保存阵容();
	}
	private static void 保存阵容()
	{
		英雄类[] 英雄数组 = 阵容.get英雄数组();
		for (int i = 0; i < 5; i++)
		{
			if (英雄数组[i] != null)
			{
				阵容信息.putString(i+"", 英雄数组[i].getClass().getName());
			}
			else
			{
				阵容信息.remove(i+"");
			}
		}
		阵容信息.flush();
	}
}
