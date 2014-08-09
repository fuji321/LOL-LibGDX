package edu.bjfu.lol.英雄.暴走萝莉;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.敌方阵容状态触发本英雄状态类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;

class 罪恶快感技能类 extends 技能类
{
	private static final float 先手值提升百分比 = 50f / 100;
	private static final int 持续回合数 = 2;
	private static final String 技能名 = "罪恶快感";
	private static final String 技能描述 = String.format("%s  在敌人被击杀后,金克丝的先手值会提升%.0f%%,持续%d回合.", 技能名, 先手值提升百分比 * 100, 持续回合数);
	private static class 罪恶快感状态类 extends 敌方阵容状态触发本英雄状态类
	{
		private static class 先手值提升状态类 extends 状态类
		{
			public 先手值提升状态类() 
			{
				super(持续回合数);
			}
			@Override
			public void 作用(对战中英雄类 受状态作用英雄)
			{
				受状态作用英雄.添加先手值百分比变化(先手值提升百分比);
			}
			@Override
			public String get状态描述()
			{
				return "在敌人击杀后，金克丝的先手值会得到显著提升";
			}
		}
		public 罪恶快感状态类()
		{
			super(Integer.MAX_VALUE);
		}
		@Override
		public String get状态描述()
		{
			return 技能描述;
		}
		private class 释放命令类 extends 命令类
		{
			@Override
			protected 动作类 生成动作()
			{
				return new 释放动作类();
			}
		}
		private class 释放动作类 extends 动作类
		{
			private Music 音效;
			public 释放动作类()
			{
				音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "JinxSkillPassive.mp3"));
				音效.setOnCompletionListener(new 音乐播放完自动销毁类());
			}
			@Override
			public void 开始()
			{
				音效.play();
			}
		}
		@Override
		public void 被通知敌方阵容状态(对战中英雄类 被通知英雄, 对战中阵容类 敌方阵容)
		{
			并行命令类 并行命令 = new 并行命令类();
			并行命令.添加命令(new 释放命令类());
			并行命令.添加命令(new 状态文本显示命令类(被通知英雄.is我方(), 被通知英雄.get英雄所在位置(), "罪恶快感", true));
			命令组装器类.实例.添加命令(并行命令);
			状态类 先手值提升状态 = 被通知英雄.get先手值增加状态(先手值提升状态类.class);
			if (先手值提升状态 == null)
			{
				被通知英雄.添加先手值增加状态(new 先手值提升状态类());
			}
			else
			{
				先手值提升状态.重置状态回合数();
			}
		}
	}
	public 罪恶快感技能类()
	{
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄)
	{
		对战中英雄.添加敌方有英雄阵亡触发状态(new 罪恶快感状态类());
	}
	@Override
	public String get技能描述()
	{
		return 技能描述;
	}
	@Override
	public String get技能名()
	{
		return 技能名;
	}
	@Override
	public String get技能文件后缀()
	{
		return "A";
	}
}
