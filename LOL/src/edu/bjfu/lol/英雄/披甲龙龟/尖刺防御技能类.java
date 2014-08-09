package edu.bjfu.lol.英雄.披甲龙龟;

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
import edu.bjfu.lol.动态.对战中英雄类.被普通攻击触发效果状态类;
import edu.bjfu.lol.动态.对战中阵容类;
import edu.bjfu.lol.动态.状态类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄类;

class 尖刺防御技能类 extends 技能类 {

	private static final int 第一次释放回合数 = 1;
	private static final int 释放间隔回合数 = 7;
	private static final int 持续回合数 = 3;
	private static final int 护甲魔抗提高值 = 40;
	private static final int 反弹基础伤害 = 15;
	private static final float 反弹附加伤害所占护甲比例 = 0.1f;
	private static final String 技能名 = "尖刺防御";
	private static final String 技能描述 = String.format("%s  拉莫斯进入防御状态,持续%d回合,提高%d护甲和魔法抗性并被\n普通攻击时反弹%d(+%.1f护甲值)魔法伤害给攻击者.", 
			技能名
			,持续回合数, 护甲魔抗提高值, 反弹基础伤害, 反弹附加伤害所占护甲比例);
	private static class 护甲提高状态类 extends 状态类
	{
		public 护甲提高状态类() {
			super(持续回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄) {
			受状态作用英雄.添加护甲数值变化(护甲魔抗提高值);
		}
		@Override
		public String get状态描述() {
			return "尖刺防御，提高护甲";
		}
	}
	private static class 魔抗提高状态类 extends 状态类
	{
		public 魔抗提高状态类() {
			super(持续回合数);
		}
		@Override
		public void 作用(对战中英雄类 受状态作用英雄) {
			受状态作用英雄.添加魔法抗性数值变化(护甲魔抗提高值);
		}
		@Override
		public String get状态描述() {
			return "尖刺防御，提高魔法抗性";
		}
	}
	private static class 被攻击反弹伤害状态类 extends 被普通攻击触发效果状态类
	{
		public 被攻击反弹伤害状态类() {
			super(持续回合数);
		}
		@Override
		public void 被普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者) {
			并行命令类 并行命令 = new 并行命令类();
			并行命令.添加命令(new 状态文本显示命令类(被攻击者.is我方(), 被攻击者.get英雄所在位置(), "攻击反弹", false));
			攻击者.被造成魔法伤害(攻击者, (int) (反弹基础伤害 + 反弹附加伤害所占护甲比例 * 被攻击者.get对战中护甲值()), 被攻击者.get对战中数值法术穿透(), 被攻击者.get对战中百分比法术穿透(), 并行命令);
			命令组装器类.实例.添加命令(并行命令);
		}
		@Override
		public String get状态描述() {
			return "尖刺防御，被普通攻击反弹伤害";
		}
	}
	public 尖刺防御技能类() {
		super(第一次释放回合数, 释放间隔回合数);
	}
	private class 释放命令类 extends 命令类
	{
		private float 接收者X;
		private float 接收者Y;
		private String 英雄皮肤文件名;
		public 释放命令类(float 接收者x, float 接收者y, String 英雄皮肤文件名)
		{
			接收者X = 接收者x;
			接收者Y = 接收者y;
			this.英雄皮肤文件名 = 英雄皮肤文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 释放动作类(接收者X, 接收者Y, 英雄皮肤文件名);
		}
	}
	private class 释放动作类 extends 动作类
	{
		private 延迟Action 延迟 = new 延迟Action(0.85f);
		private 粒子演员类 粒子;
		private Music 音效;
		private 释放动作类(float 接收者X, float 接收者Y, String 英雄皮肤文件名)
		{
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "RammusSkillW.p"), 英雄类.get皮肤资源(英雄皮肤文件名).getAtlas());
			粒子 = new 粒子演员类(粒子效果);
			粒子.setPosition(接收者X, 接收者Y);
			粒子.addAction(延迟);
			音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "RammusSkillW.mp3"));
			音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		}
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
		并行命令类 并行命令 = new 并行命令类();
		并行命令.添加命令(new 状态文本显示命令类(释放此技能的英雄.is我方(), 释放此技能的英雄.get英雄所在位置(), "双抗↑,攻击反弹", true));
		if (释放此技能的英雄.is我方())
		{
			并行命令.添加命令(new 释放命令类(对战屏幕类.我方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.我方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		else
		{
			并行命令.添加命令(new 释放命令类(对战屏幕类.敌方X坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 对战屏幕类.敌方Y坐标数组[释放此技能的英雄.get英雄所在位置() - 1], 释放此技能的英雄.get英雄().get英雄皮肤文件名()));
		}
		命令组装器类.实例.添加命令(并行命令);
		释放此技能的英雄.添加护甲值增加状态(new 护甲提高状态类());
		释放此技能的英雄.添加魔法抗性增加状态(new 魔抗提高状态类());
		释放此技能的英雄.添加被普通攻击触发效果状态(new 被攻击反弹伤害状态类());
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
		return "C";
	}
}
