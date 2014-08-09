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
import edu.bjfu.lol.静态.英雄类;
import edu.bjfu.lol.静态.阵容类;

public class 玩家信息类
{
	private static final LinkedList<英雄类> 英雄列表 = new LinkedList<英雄类>();
	private static final 阵容类 阵容 = new 阵容类();
	private static final Preferences 阵容信息;
	static
	{
		阵容信息 = Gdx.app.getPreferences("玩家阵容信息");
		英雄列表.add(new 暴走萝莉英雄类());
		英雄列表.add(new 德玛西亚之力英雄类());
		英雄列表.add(new 放逐之刃英雄类());
		英雄列表.add(new 寒冰射手英雄类());
		英雄列表.add(new 九尾妖狐英雄类());
		英雄列表.add(new 卡牌大师英雄类());
		英雄列表.add(new 暮光之眼英雄类());
		英雄列表.add(new 披甲龙龟英雄类());
		英雄列表.add(new 皮城女警英雄类());
		英雄列表.add(new 审判天使英雄类());
		英雄列表.add(new 嗜血猎手英雄类());
		英雄列表.add(new 无极剑圣英雄类());
		英雄列表.add(new 邪恶小法师英雄类());
		英雄列表.add(new 迅捷斥候英雄类());
		英雄列表.add(new 众星之子英雄类());
		英雄类[] 英雄数组 = new 英雄类[5];
		for (int i = 0; i < 英雄数组.length; i++)
		{
			String 类名 = 阵容信息.getString(i+"");
			if (类名.length() > 0)
			{
				try
				{
					for (英雄类 英雄 : 英雄列表)
					{
						if (英雄.getClass().getName().equals(类名))
						{
							英雄数组[i] = 英雄;
							break;
						}
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		阵容.set英雄数组(英雄数组);
	}
	public static LinkedList<英雄类> get英雄列表()
	{
		return 英雄列表;
	}
	public static 阵容类 get阵容()
	{
		return 阵容;
	}
	public static void 保存阵容()
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
