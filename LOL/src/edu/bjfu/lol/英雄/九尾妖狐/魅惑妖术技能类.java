package edu.bjfu.lol.英雄.九尾妖狐;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
import edu.bjfu.lol.scenes.scene2d.actions.绝对移动Action;
import edu.bjfu.lol.scenes.scene2d.ui.粒子演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.英雄.九尾妖狐.摄魂夺魄技能类.摄魂夺魄状态类;
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 魅惑妖术技能类 extends 技能类 {

	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 4;
	private static final int 基础伤害 = 60;
	private static final float 附加伤害所占法术强度比例 = 0.35f;
	private static final float 命中率 = 50f / 100;
	private static final int 魅惑持续回合数 = 1;
	private static final String 技能名 = "魅惑妖术";
	private static final String 技能描述 = String.format("%s  阿狸献出红唇热吻,对随机目标造成%d(+%.2f法术强度)并将\n目标魅惑,命中率%.0f%%,让目标意乱情迷地走向阿狸,魅惑效果持续%d回合.", 
			技能名
			,基础伤害, 附加伤害所占法术强度比例, 命中率 * 100, 魅惑持续回合数);
	public 魅惑妖术技能类() {
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 魅惑妖术命令类 extends 命令类
	{
		private float 发出者x;
		private float 发出者y;
		private float 接收者x;
		private float 接收者y;
		private String 英雄皮肤文件名;
		public 魅惑妖术命令类(float 发出者x, float 发出者y, float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			this.发出者x = 发出者x;
			this.发出者y = 发出者y;
			this.接收者x = 接收者x;
			this.接收者y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 魅惑妖术动作类(发出者x, 发出者y, 接收者x, 接收者y, 英雄皮肤文件名);
		}
	}
	private class 魅惑妖术动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(0.2f);
		private 粒子演员类 粒子;
		private Music 音效; 
		public 魅惑妖术动作类(float 发出者X, float 发出者Y, float 接收者X, float 接收者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "AhriSkillE.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(发出者X, 发出者Y);
			绝对移动Action 移动Action = new 绝对移动Action(接收者X, 接收者Y, 0.6f);
			SequenceAction 序列Action = Actions.sequence(移动Action, 延迟);
			粒子.addAction(序列Action);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "AhriSkillE.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		@Override
		public boolean is完成()
		{
			if (延迟.isAction已结束())
			{
				粒子.remove();
				粒子.dispose();
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
			对战屏幕类.实例.添加演员(粒子);
			音效.play();
		}
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄) {
		对战中英雄类 敌人 = 敌方阵容.get随机被攻击英雄(true);
		if (释放此技能的英雄.is我方())
		{
			命令组装器类.实例.添加命令(new 魅惑妖术命令类(
					对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
					对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
					对战屏幕类.敌方X坐标数组[敌人.get英雄所在位置() - 1],
					对战屏幕类.敌方Y坐标数组[敌人.get英雄所在位置() - 1], 
					释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		else
		{
			命令组装器类.实例.添加命令(new 魅惑妖术命令类(
					对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
					对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1],
					对战屏幕类.我方X坐标数组[敌人.get英雄所在位置() - 1],
					对战屏幕类.我方Y坐标数组[敌人.get英雄所在位置() - 1],
					释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		if (计算类.根据_概率_计算是否发生(命中率))
		{
			并行命令类 并行命令 = new 并行命令类();
			敌人.被嘲讽(魅惑持续回合数, 并行命令);
			int 造成伤害 = 敌人.被造成魔法伤害(释放此技能的英雄, (int) (基础伤害  + 附加伤害所占法术强度比例 * 释放此技能的英雄.get对战中法术强度()), 释放此技能的英雄.get对战中数值法术穿透(), 释放此技能的英雄.get对战中百分比法术穿透(), 并行命令);
			命令组装器类.实例.添加命令(并行命令);
			摄魂夺魄状态类.法术吸血(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), 释放此技能的英雄, 1, 造成伤害, false);
		}
		return true;
	}
	@Override
	public String get技能描述() {
		return 技能描述;
	}
	@Override
	public String get技能名() {
		return 技能名;
	}
	@Override
	public String get技能文件后缀()
	{
		return "D";
	}
}
