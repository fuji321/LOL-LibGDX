package edu.bjfu.lol.英雄.皮城女警;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;

import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.动态.对战中英雄类;
import edu.bjfu.lol.动态.对战中英雄类.普通攻击触发效果状态类;
import edu.bjfu.lol.静态.技能类;

class 爆头技能类 extends 技能类
{
	private static final int 每几次攻击 = 4;
	private static final int 效果触发层数 = 每几次攻击 + 1;
	private static final float 附加伤害百分比 = 50f / 100;
	private static final String 技能名 = "爆头";
	private static final String 技能描述 = String.format("%s  每%d次普通攻击,凯特琳下一击附带爆头效果,对敌人造成%.0f%%伤害.", 技能名, 每几次攻击, 附加伤害百分比 * 100);
	private static class 爆头状态类 extends 普通攻击触发效果状态类 
	{
		public 爆头状态类() {
			super(Integer.MAX_VALUE, 效果触发层数, 0);
		}
		@Override
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器) {
			增加一层(false);
			Gdx.app.debug("爆头技能类.爆头状态类.普通攻击", String.format("%s爆头状态，现在是第%d次攻击\n", 攻击者.get阵容名所在位置英雄名字(), get当前层数()));
			if (get当前层数() == 效果触发层数)
			{
				并行命令类 并行命令 = new 并行命令类();
				并行命令.添加命令(new 状态文本显示命令类(攻击者.is我方(), 攻击者.get英雄所在位置(), "爆头", true));
				int 伤害值 = 被攻击者.被造成物理伤害(攻击者, 攻击者.get对战中攻击力(), 攻击者.get对战中数值护甲穿透(), 攻击者.get对战中百分比护甲穿透(), 并行命令);
				命令组装器类.实例.添加命令(并行命令);
				攻击者.生命偷取(伤害值);
				层数清零();
			}
		}
		@Override
		public String get状态描述() {
			return 技能描述;
		}
	}
	public 爆头技能类()
	{
		super(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	@Override
	public void 添加被动状态(对战中英雄类 对战中英雄) {
		对战中英雄.添加普通攻击触发效果状态(new 爆头状态类());
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
