package edu.bjfu.lol.英雄.嗜血猎手;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import edu.bjfu.lol.scenes.scene2d.actions.延迟Action;
import edu.bjfu.lol.scenes.scene2d.ui.动画演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.动作.攻击前摇动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 嗜血攻击技能类 extends 技能类 {

	private static final int 第一次释放回合数 = 2;
	private static final int 释放间隔回合数 = 5;
	private static final int 技能基础伤害 = 75;
	private static final float 技能附加伤害所占法术强度比例 = 1;
	private static final float 目标生命值百分比 = 8f / 100;
	private static final float 治疗效果百分比 = 80f / 100;
	private static final String 技能名 = "嗜血攻击";
	private static final String 技能描述 = String.format("%s  猛击敌人,造成%d(+%.0f法术强度)和目标%.0f%%最大生命值(+%.0f法术\n强度)之间更大的魔法伤害,并治疗自己,治疗效果为他造成的伤害的%.0f%%.",
			技能名
			,技能基础伤害, 技能附加伤害所占法术强度比例, 目标生命值百分比 * 100, 技能附加伤害所占法术强度比例, 治疗效果百分比 * 100);
	public 嗜血攻击技能类() {
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private float 发出者X;
		private float 发出者Y;
		private String 英雄皮肤文件名;
		public 释放命令类(float 发出者x, float 发出者y, String 英雄皮肤文件名)
		{
			发出者X = 发出者x;
			发出者Y = 发出者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(发出者X, 发出者Y, 英雄皮肤文件名);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(攻击前摇动作类.近战总体摆动时间);
		private Music 音效;
		private 动画演员类 被攻击动画;
		private 释放动作类(float 发出者X, float 发出者Y, String 英雄皮肤文件名)
		{
			Skin 皮肤资源 = 英雄类.get皮肤资源(英雄皮肤文件名);
			TextureRegion[] 攻击帧数组 = new TextureRegion[4];
			攻击帧数组[0] = 皮肤资源.getRegion("SkillQA");
			攻击帧数组[1] = 皮肤资源.getRegion("SkillQB");
			攻击帧数组[2] = 皮肤资源.getRegion("SkillQC");
			攻击帧数组[3] = 皮肤资源.getRegion("SkillQD");
			float 普通攻击动画比例 = 0.7f;
			被攻击动画 = new 动画演员类(延迟.getDuration() / 攻击帧数组.length, 攻击帧数组, Animation.NORMAL);
			被攻击动画.setWidth(对战屏幕类.英雄图片最大宽度 * 普通攻击动画比例);
			被攻击动画.setHeight(对战屏幕类.英雄图片最大高度 * 普通攻击动画比例);
			被攻击动画.setPosition(发出者X - 被攻击动画.getWidth() / 2, 发出者Y - 被攻击动画.getHeight() / 2);
			被攻击动画.addAction(延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "WarwickSkillQ.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
		@Override
		public boolean is完成()
		{
			if (延迟.isAction已结束())
			{
				被攻击动画.remove();
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
			对战屏幕类.实例.添加演员(被攻击动画);
			音效.play();
		}
	}
	@Override
	public boolean 释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
	{
		对战中英雄类 被攻击英雄 = 敌方阵容.get默认被攻击英雄(释放此技能的英雄);
		if (被攻击英雄 != null)
		{
			并行命令类 并行命令 = new 并行命令类();
			int 最大生命值百分比生命值 = (int) (被攻击英雄.get对战中最大生命值() * 目标生命值百分比);
			if (释放此技能的英雄.is我方())
			{
				并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置()-1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			else
			{
				并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置()-1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置()-1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
			}
			int 造成伤害 = 被攻击英雄.被造成魔法伤害(释放此技能的英雄, (int) (释放此技能的英雄.get对战中法术强度() * 技能附加伤害所占法术强度比例 + (技能基础伤害 > 最大生命值百分比生命值 ? 技能基础伤害 : 最大生命值百分比生命值)), 释放此技能的英雄.get对战中数值法术穿透(), 释放此技能的英雄.get对战中百分比法术穿透(), 并行命令);
			释放此技能的英雄.增加生命值((int) (治疗效果百分比 * 造成伤害), 并行命令);
			命令组装器类.实例.添加命令(并行命令);
			释放此技能的英雄.法术吸血(造成伤害, false);
			return true;
		}
		else
		{
			return false;
		}
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
		return "B";
	}
}
