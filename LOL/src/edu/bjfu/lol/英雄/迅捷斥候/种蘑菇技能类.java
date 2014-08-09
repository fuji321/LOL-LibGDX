package edu.bjfu.lol.英雄.迅捷斥候;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
import edu.bjfu.lol.scenes.scene2d.ui.粒子演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 种蘑菇技能类 extends 技能类 {

	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 7;
	private static final int 基础伤害 = 200;
	private static final float 附加伤害所占法术强度比例 = 0.8f;
	private static final float 先手值减少百分比 = 30f / 100;
	private static final int 先手值减少持续回合数 = 2;
	private static final float 命中率 = 50f / 100;
	private static final String 技能名 = "种蘑菇";
	private static final String 技能描述 = String.format("%s  使用一个储存的蘑菇来设置陷阱,在随机目标处引爆,毒散发到\n附近的敌人,造成%d(+%.1f法术强度)魔法伤害,减少%.0f%%先手值,持续%d回\n合,命中率%.0f%%.", 
			技能名,
			基础伤害, 附加伤害所占法术强度比例, 先手值减少百分比 * 100, 先手值减少持续回合数, 命中率 * 100);
	private static class 先手值减少状态类 extends 状态类
	{

		public 先手值减少状态类() {
			super(先手值减少持续回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄) {
			受状态作用英雄.添加先手值百分比变化(-先手值减少百分比);
		}
		@Override
		public String get状态描述() {
			return "种蘑菇，先手值减少";
		}
	}
	public 种蘑菇技能类() {
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private float 接收者x;
		private float 接收者y;
		private String 英雄皮肤文件名;
		public 释放命令类(float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			this.接收者x = 接收者x;
			this.接收者y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(接收者x, 接收者y, 英雄皮肤文件名);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 延迟Action 蘑菇延迟 = new 延迟Action(1f);
		private boolean is蘑菇移除;
		private 延迟Action 爆炸延迟 = new 延迟Action(0.6f);
		private 粒子演员类 爆炸粒子;
		private 粒子演员类 蘑菇粒子;
		private Music 音效;
		public 释放动作类(float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			// 蘑菇
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "TeemoSkillR.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			蘑菇粒子 = new 粒子演员类(粒子效果);
			蘑菇粒子.setPosition(接收者x, 接收者y);
			蘑菇粒子.addAction(蘑菇延迟);
			// 爆炸
			粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "TeemoSkillE.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			爆炸粒子 = new 粒子演员类(粒子效果);
			爆炸粒子.setPosition(接收者x, 接收者y);
			爆炸粒子.addAction(爆炸延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "TeemoSkillR.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		@Override
		public boolean is完成()
		{
			if (蘑菇延迟.isAction已结束())
			{
				if (!is蘑菇移除)
				{
					蘑菇粒子.remove();
					蘑菇粒子.dispose();
					对战屏幕类.实例.添加演员(爆炸粒子);
					is蘑菇移除 = true;
					return false;
				}
				else if (爆炸延迟.isAction已结束())
				{
					爆炸粒子.remove();
					爆炸粒子.dispose();
					return true;
				}
			}
			return false;
		}
		@Override
		public void 开始()
		{
			对战屏幕类.实例.添加演员(蘑菇粒子);
			音效.play();
		}
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄) {
		int 总伤害 = 0;
		int 技能伤害 = (int) (基础伤害 + 附加伤害所占法术强度比例 * 释放此技能的英雄.get对战中法术强度());
		boolean is命中 = false;
		int 数值穿透 = 释放此技能的英雄.get对战中数值法术穿透();
		float 百分比穿透 = 释放此技能的英雄.get对战中百分比法术穿透();
		float 接收者x = 0;
		float 接收者y = 0;
		int 接收者总数 = 0;
		并行命令类 并行命令 = new 并行命令类();
		for (对战中英雄类 敌人 : 敌方阵容.get随机被攻击小范围英雄数组())
		{
			if (敌人 != null && !敌人.is阵亡())
			{
				接收者总数++;
				if (敌人.is我方())
				{
					接收者x += 对战屏幕类.我方X坐标数组[敌人.get英雄所在位置()-1];
					接收者y += 对战屏幕类.我方Y坐标数组[敌人.get英雄所在位置()-1];
				}
				else
				{
					接收者x += 对战屏幕类.敌方X坐标数组[敌人.get英雄所在位置()-1];
					接收者y += 对战屏幕类.敌方Y坐标数组[敌人.get英雄所在位置()-1];
				}
				if (计算类.根据_概率_计算是否发生(命中率))
				{
					并行命令.添加命令(new 状态文本显示命令类(敌人.is我方(), 敌人.get英雄所在位置(), "先手↓", false));
					敌人.添加先手值减少状态(new 先手值减少状态类());
					总伤害 += 敌人.被造成魔法伤害(释放此技能的英雄, 技能伤害, 数值穿透, 百分比穿透, 并行命令);
					is命中 = true;
				}
			}
		}
		接收者x /= 接收者总数;
		接收者y /= 接收者总数;
		命令组装器类.实例.添加命令(new 释放命令类(接收者x, 接收者y, 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		命令组装器类.实例.添加命令(并行命令);
		if (is命中)
		{
			释放此技能的英雄.法术吸血(总伤害, true);
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
		return "E";
	}
	@Override
	public boolean is大招()
	{
		return true;
	}
}
