package edu.bjfu.lol.动态;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import edu.bjfu.lol.主类;
import edu.bjfu.lol.scenes.scene2d.actions.相对移动Action;
import edu.bjfu.lol.scenes.scene2d.ui.粒子演员类;
import edu.bjfu.lol.scenes.scene2d.动作.动作类;
import edu.bjfu.lol.scenes.scene2d.动作.攻击前摇动作类;
import edu.bjfu.lol.scenes.scene2d.命令.命令类;
import edu.bjfu.lol.scenes.scene2d.命令.命令组装器类;
import edu.bjfu.lol.scenes.scene2d.命令.并行命令类;
import edu.bjfu.lol.scenes.scene2d.命令.攻击前摇命令类;
import edu.bjfu.lol.scenes.scene2d.命令.状态文本显示命令类;
import edu.bjfu.lol.scenes.scene2d.命令.设置英雄血量命令类;
import edu.bjfu.lol.screen.对战屏幕类;
import edu.bjfu.lol.screen.对战屏幕类.英雄造型类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;
import edu.bjfu.lol.逻辑.计算类;
import edu.bjfu.lol.静态.技能类;
import edu.bjfu.lol.静态.英雄属性类;
import edu.bjfu.lol.静态.英雄类;

public class 对战中英雄类 {
	private 对战中阵容类 所在阵容;
	private int 英雄所在位置;
	private 英雄属性变化类 英雄属性变化 = new 英雄属性变化类();
	private 英雄属性类 对战中英雄属性;
	private 对战中技能类[] 对战中技能数组;
	public 对战中技能类[] get对战中技能数组() {
		return 对战中技能数组;
	}
	private int 生命值;
	private 英雄类 英雄;
	private boolean 阵亡;
	private Set<状态类> 攻击力增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 攻击力减少状态集合 = new HashSet<状态类>();
	private Set<状态类> 法术强度增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 法术强度减少状态集合 = new HashSet<状态类>();
	private Set<状态类> 先手值增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 先手值减少状态集合 = new HashSet<状态类>();
	private Set<先手值减少免疫状态类> 先手值减少免疫状态集合 = new HashSet<先手值减少免疫状态类>();
	private Set<先手值清零状态类> 先手值清零状态集合 = new HashSet<先手值清零状态类>();
	private Set<状态类> 护甲值增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 护甲值减少状态集合 = new HashSet<状态类>();
	private Set<状态类> 攻击速度增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 攻击速度减少状态集合 = new HashSet<状态类>();
	private Set<状态类> 魔法抗性增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 魔法抗性减少状态集合 = new HashSet<状态类>();
	private Set<状态类> 最大生命值增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 最大生命值减少状态集合 = new HashSet<状态类>();
	private Set<状态类> 生命值增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 生命值减少状态集合 = new HashSet<状态类>();
	private Set<状态类> 生命回复增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 生命回复减少状态集合 = new HashSet<状态类>();
	private Set<状态类> 护甲穿透数值增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 护甲穿透百分比增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 护甲穿透数值减少状态集合 = new HashSet<状态类>();
	private Set<状态类> 护甲穿透百分比减少状态集合 = new HashSet<状态类>();
	private Set<状态类> 法术穿透数值增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 法术穿透百分比增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 法术穿透数值减少状态集合 = new HashSet<状态类>();
	private Set<状态类> 法术穿透百分比减少状态集合 = new HashSet<状态类>();
	private Set<状态类> 生命偷取增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 生命偷取减少状态集合 = new HashSet<状态类>();
	private Set<状态类> 暴击几率增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 暴击几率减少状态集合 = new HashSet<状态类>();
	private Set<状态类> 法术吸血增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 法术吸血减少状态集合 = new HashSet<状态类>();
	private Set<状态类> 韧性增加状态集合 = new HashSet<状态类>();
	private Set<状态类> 韧性减少状态集合 = new HashSet<状态类>();
	private Set<被沉默状态类> 被沉默状态集合 = new HashSet<被沉默状态类>();
	private Set<被冰冻状态类> 被冰冻状态集合 = new HashSet<被冰冻状态类>();
	private Set<被击飞状态类> 被击飞状态集合 = new HashSet<被击飞状态类>();
	private Set<被嘲讽状态类> 被嘲讽状态集合 = new HashSet<被嘲讽状态类>();
	private Set<被眩晕状态类> 被眩晕状态集合 = new HashSet<被眩晕状态类>();
	private Set<被压制状态类> 被压制状态集合 = new HashSet<被压制状态类>();
	private Set<状态类> 伤害减免状态集合 = new HashSet<状态类>();
	private Set<状态类> 物理伤害减免状态集合 = new HashSet<状态类>();
	private Set<状态类> 魔法伤害减免状态集合 = new HashSet<状态类>();
	private Set<状态类> 敌方有英雄阵亡触发状态集合 = new HashSet<状态类>();
	private Set<敌方阵容状态触发本英雄状态类> 敌方阵容状态触发本英雄状态集合 = new HashSet<敌方阵容状态触发本英雄状态类>();
	private Set<普通攻击触发效果状态类> 普通攻击触发效果状态集合 = new HashSet<普通攻击触发效果状态类>();
	private Set<被普通攻击触发效果状态类> 被普通攻击触发效果状态集合 = new HashSet<被普通攻击触发效果状态类>();
	private Set<隐身状态类> 隐身状态集合 = new HashSet<隐身状态类>();
	private Set<显隐状态类> 显隐状态集合 = new HashSet<显隐状态类>();
	private Set<护盾状态类> 护盾状态集合 = new HashSet<护盾状态类>();
	private Set<护盾状态类> 魔法护盾状态集合 = new HashSet<护盾状态类>();
	private Set<护盾状态类> 无敌状态集合 = new HashSet<护盾状态类>();
	private Set<被致盲状态类> 被致盲状态集合 = new HashSet<被致盲状态类>();
	private Set<被集火状态类> 被集火状态集合 = new HashSet<被集火状态类>();
	private Set<获得攻击后排能力状态类> 获得攻击后排能力状态集合 = new HashSet<获得攻击后排能力状态类>();
	private Set<被动持续释放状态类> 被动持续释放状态集合 = new HashSet<被动持续释放状态类>();
	private boolean is获得攻击后排能力;
	private int 对战中被集火编号;
	private 主动持续施法状态类 主动持续施法状态;
	private boolean is先手值减少免疫;
	private boolean is先手值清零;
	private boolean is隐身;
	private boolean is显隐;
	private int 伤害数值减免;
	private float 伤害百分比减免;
	private int 物理伤害数值减免;
	private float 物理伤害百分比减免;
	private int 魔法伤害数值减免;
	private float 魔法伤害百分比减免;
	private boolean 被致盲;
	private boolean 被沉默;
	private boolean 被冰冻;
	private boolean 被击飞;
	private boolean 被嘲讽;
	private boolean 被眩晕;
	private boolean 被压制;
	private boolean is我方;
	private int 击杀数;
	/**
	 * @param 已阵亡 已阵亡时清空所有的效果
	 */
	public void 重置特殊状态动画效果(并行命令类 并行命令)
	{
		// 已阵亡则清空
		if (阵亡)
		{
			// 重置隐身状态
			并行命令.添加命令(new 设置隐身命令类(is我方, 英雄所在位置, false));
			// 重置沉默状态
			并行命令.添加命令(new 设置沉默命令类(is我方, 英雄所在位置, false));
			// 重置冰冻效果
			并行命令.添加命令(new 设置冰冻命令类(is我方, 英雄所在位置, false));
			// 重置击飞效果
			并行命令.添加命令(new 设置击飞命令类(is我方, 英雄所在位置, false));
			// 重置嘲讽效果
			并行命令.添加命令(new 设置嘲讽命令类(is我方, 英雄所在位置, false));
			// 重置眩晕效果
			并行命令.添加命令(new 设置眩晕命令类(is我方, 英雄所在位置, false));
			// 重置压制效果
			并行命令.添加命令(new 设置压制命令类(is我方, 英雄所在位置, false));
			// 重置致盲效果
			并行命令.添加命令(new 设置致盲命令类(is我方, 英雄所在位置, false));
			// 重置集火效果
			并行命令.添加命令(new 设置集火命令类(is我方, 英雄所在位置, false));
		}
		else
		{
			// 重置隐身状态
			并行命令.添加命令(new 设置隐身命令类(is我方, 英雄所在位置, is对战中隐身()));
			// 重置沉默状态
			并行命令.添加命令(new 设置沉默命令类(is我方, 英雄所在位置, is对战中被沉默()));
			// 重置冰冻效果
			并行命令.添加命令(new 设置冰冻命令类(is我方, 英雄所在位置, is对战中被冰冻()));
			// 重置击飞效果
			并行命令.添加命令(new 设置击飞命令类(is我方, 英雄所在位置, is对战中被击飞()));
			// 重置嘲讽效果
			并行命令.添加命令(new 设置嘲讽命令类(is我方, 英雄所在位置, is对战中被嘲讽()));
			// 重置眩晕效果
			并行命令.添加命令(new 设置眩晕命令类(is我方, 英雄所在位置, is对战中被眩晕()));
			// 重置压制效果
			并行命令.添加命令(new 设置压制命令类(is我方, 英雄所在位置, is对战中被压制()));
			// 重置致盲效果
			并行命令.添加命令(new 设置致盲命令类(is我方, 英雄所在位置, is对战中被致盲()));
			// 重置集火效果
			并行命令.添加命令(new 设置集火命令类(is我方, 英雄所在位置, is对战中被集火()));
		}
	}
	/**
	 * 调用此方法得到信息用于调试
	 */
	public void 输出对战中英雄信息()
	{
		Gdx.app.debug("对战中英雄类.输出对战中英雄信息", String.format("<%s>\n",  get阵容名所在位置英雄名字()));
		计算攻击力();
		计算法术强度();
		计算先手值();
		计算护甲值();
		计算魔法抗性();
		计算攻击速度();
		计算最大生命值();
		计算生命回复();
		计算数值护甲穿透();
		计算百分比护甲穿透();
		计算数值法术穿透();
		计算百分比法术穿透();
		计算生命偷取();
		计算暴击几率();
		计算法术吸血();
		计算韧性();
		Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<英雄初始属性>");
		Gdx.app.debug("对战中英雄类.输出对战中英雄信息", String.format("初始最大生命值:%d,初始攻击力:%d,初始法术强度:%d,初始先手值:%d,初始护甲值:%d,初始魔法抗性:%d\n初始攻击速度:%.2f,初始生命回复:%d,初始护甲穿透:%d|%.1f%%,初始法术穿透:%d|%.1f%%\n初始生命偷取:%.1f%%,初始暴击几率:%.1f%%,初始法术吸血:%.1f%%,初始韧性%.1f%%\n", get初始最大生命值(), get初始攻击力(), get初始法术强度(), get初始先手值(), get初始护甲值(), get初始魔法抗性(), get初始攻击速度(), get初始生命回复(), get初始数值护甲穿透(), get初始百分比护甲穿透() * 100, get初始数值法术穿透(), get初始百分比法术穿透() * 100, get初始生命偷取() * 100, get初始暴击几率() * 100, get初始法术吸血() * 100, get初始韧性() * 100));
		Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<英雄属性变化>");
		Gdx.app.debug("对战中英雄类.输出对战中英雄信息", String.format("攻击力变化:%d|%.1f%%,法术强度变化:%d|%.1f%%,先手值变化:%d|%.1f%%,护甲值变化:%d|%.1f%%\n攻击速度变化:%.1f%%,魔法抗性变化:%d|%.1f%%,最大生命值变化:%d|%.1f%%\n生命回复变化:%d|%.1f%%,护甲穿透变化:%d|%.1f%%,法术穿透变化:%d|%.1f%%\n生命偷取变化:%.1f,暴击几率变化:%.1f%%,法术吸血变化:%.1f%%,韧性变化:%.1f%%\n", 
				英雄属性变化.get攻击力数值变化(), 英雄属性变化.get攻击力百分比变化() * 100, 英雄属性变化.get法术强度数值变化(), 英雄属性变化.get法术强度百分比变化() * 100, 英雄属性变化.get先手值数值变化(), 英雄属性变化.get先手值百分比变化() * 100, 英雄属性变化.get护甲数值变化(), 英雄属性变化.get护甲百分比变化() * 100,
				英雄属性变化.get攻击速度百分比变化() * 100, 英雄属性变化.get魔法抗性数值变化(), 英雄属性变化.get魔法抗性百分比变化() * 100, 英雄属性变化.get最大生命值数值变化(), 英雄属性变化.get最大生命值百分比变化() * 100, 英雄属性变化.get生命回复数值变化(), 英雄属性变化.get生命回复百分比变化() * 100,
				英雄属性变化.get数值护甲穿透变化(), 英雄属性变化.get百分比护甲穿透变化() * 100, 英雄属性变化.get数值法术穿透变化(), 英雄属性变化.get百分比法术穿透变化() * 100, 英雄属性变化.get生命偷取百分比变化() * 100, 英雄属性变化.get暴击几率百分比变化() * 100, 英雄属性变化.get法术吸血百分比变化() * 100, 英雄属性变化.get韧性百分比变化() * 100));
		Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<英雄最终属性>");
		Gdx.app.debug("对战中英雄类.输出对战中英雄信息", String.format("生命值/最大生命值:%d/%d,攻击力:%d,法术强度:%d,先手值:%d,护甲值:%d,魔法抗性:%d\n攻击速度:%.2f,生命回复:%d,护甲穿透:%d|%.1f%%,法术穿透:%d|%.1f%%\n生命偷取:%.1f%%,暴击几率:%.1f%%,法术吸血:%.1f%%,韧性%.1f%%\n", 生命值, 对战中英雄属性.get最大生命值(), 对战中英雄属性.get攻击力(), 对战中英雄属性.get法术强度(), 对战中英雄属性.get先手值(), 对战中英雄属性.get护甲值(), 对战中英雄属性.get魔法抗性(), 对战中英雄属性.get攻击速度(), 对战中英雄属性.get生命回复(), 对战中英雄属性.get数值护甲穿透(), 对战中英雄属性.get百分比护甲穿透() * 100, 对战中英雄属性.get数值法术穿透(), 对战中英雄属性.get百分比法术穿透() * 100, 对战中英雄属性.get生命偷取() * 100, 对战中英雄属性.get暴击几率() * 100, 对战中英雄属性.get法术吸血() * 100, 对战中英雄属性.get韧性() * 100));
		计算被压制属性();
		计算被冰冻属性();
		计算被击飞属性();
		计算被嘲讽属性();
		计算被沉默属性();
		计算被眩晕属性();
		Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<异常状态信息>");
		Gdx.app.debug("对战中英雄类.输出对战中英雄信息", String.format("被沉默%b,被冰冻%b,被击飞%b,被嘲讽%b,被眩晕%b,被压制%b\n", 被沉默, 被冰冻, 被击飞, 被嘲讽, 被眩晕, 被压制));
		if (!普通攻击触发效果状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<普通攻击触发效果状态>");
			for (状态类 状态 : 普通攻击触发效果状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!被普通攻击触发效果状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<被普通攻击触发效果状态>");
			for (状态类 状态 : 被普通攻击触发效果状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!攻击力增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<攻击力增加状态>");
			for (状态类 状态 : 攻击力增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!攻击力减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<攻击力减少状态>");
			for (状态类 状态 : 攻击力减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!法术强度增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<法术强度增加状态>");
			for (状态类 状态 : 法术强度增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!法术强度减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<法术强度减少状态>");
			for (状态类 状态 : 法术强度减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!先手值清零状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<先手值清零状态>");
			for (状态类 状态 : 先手值清零状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!先手值增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<先手值增加状态>");
			for (状态类 状态 : 先手值增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!先手值减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<先手值减少状态>");
			for (状态类 状态 : 先手值减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!先手值减少免疫状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<先手值减少免疫状态>");
			for (状态类 状态 : 先手值减少免疫状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!护甲值增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<护甲值增加状态>");
			for (状态类 状态 : 护甲值增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!护甲值减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<护甲值减少状态>");
			for (状态类 状态 : 护甲值减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!攻击速度增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<攻击速度增加状态>");
			for (状态类 状态 : 攻击速度增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!攻击速度减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<攻击速度减少状态>");
			for (状态类 状态 : 攻击速度减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!魔法抗性增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<魔法抗性增加状态>");
			for (状态类 状态 : 魔法抗性增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!魔法抗性减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<魔法抗性减少状态>");
			for (状态类 状态 : 魔法抗性减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!最大生命值增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<最大生命值增加状态>");
			for (状态类 状态 : 最大生命值增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!最大生命值减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<最大生命值减少状态>");
			for (状态类 状态 : 最大生命值减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!生命值增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<生命值增加状态>");
			for (状态类 状态 : 生命值增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!生命值减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<生命值减少状态>");
			for (状态类 状态 : 生命值减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!生命回复增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<生命回复增加状态>");
			for (状态类 状态 : 生命回复增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!生命回复减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<生命回复减少状态>");
			for (状态类 状态 : 生命回复减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!护甲穿透数值增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<护甲穿透数值增加状态>");
			for (状态类 状态 : 护甲穿透数值增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!护甲穿透百分比增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<护甲穿透百分比增加状态>");
			for (状态类 状态 : 护甲穿透百分比增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!护甲穿透数值减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<护甲穿透数值减少状态>");
			for (状态类 状态 : 护甲穿透数值减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!护甲穿透百分比减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<护甲穿透百分比减少状态>");
			for (状态类 状态 : 护甲穿透百分比减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!法术穿透数值增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<法术穿透数值增加状态>");
			for (状态类 状态 : 法术穿透数值增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!法术穿透数值减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<法术穿透数值减少状态>");
			for (状态类 状态 : 法术穿透数值减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!法术穿透百分比增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<法术穿透百分比增加状态>");
			for (状态类 状态 : 法术穿透百分比增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!法术穿透百分比减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<法术穿透百分比减少状态>");
			for (状态类 状态 : 法术穿透百分比减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!生命偷取增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<生命偷取增加状态>");
			for (状态类 状态 : 生命偷取增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!生命偷取减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<生命偷取减少状态>");
			for (状态类 状态 : 生命偷取减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!暴击几率增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<暴击几率增加状态>");
			for (状态类 状态 : 暴击几率增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!暴击几率减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<暴击几率减少状态>");
			for (状态类 状态 : 暴击几率减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!法术吸血增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<法术吸血增加状态>");
			for (状态类 状态 : 法术吸血增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!法术吸血减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<法术吸血减少状态>");
			for (状态类 状态 : 法术吸血减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!韧性增加状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<韧性增加状态>");
			for (状态类 状态 : 韧性增加状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!韧性减少状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<韧性减少状态>");
			for (状态类 状态 : 韧性减少状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!被压制状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<被压制状态>");
			for (状态类 状态 : 被压制状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!伤害减免状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<伤害减免状态>");
			for (状态类 状态 : 伤害减免状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!物理伤害减免状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<物理伤害减免状态>");
			for (状态类 状态 : 物理伤害减免状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!魔法伤害减免状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<魔法伤害减免状态>");
			for (状态类 状态 : 魔法伤害减免状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!敌方有英雄阵亡触发状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<敌方有英雄阵亡触发状态>");
			for (状态类 状态 : 敌方有英雄阵亡触发状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!敌方阵容状态触发本英雄状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<敌方英雄状态触发本英雄状态>");
			for (状态类 状态 : 敌方阵容状态触发本英雄状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!护盾状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<护盾状态>");
			for (状态类 状态 : 护盾状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!魔法护盾状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<魔法护盾状态>");
			for (状态类 状态 : 魔法护盾状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!无敌状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<无敌状态>");
			for (状态类 状态 : 无敌状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		if (!被动持续释放状态集合.isEmpty())
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<被动持续释放状态>");
			for (状态类 状态 : 被动持续释放状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}	
		}
		if (主动持续施法状态 != null)
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<正在主动持续施法>");
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 主动持续施法状态.get状态信息());
		}
		计算先手值减少免疫属性();
		if (is先手值减少免疫)
		{
			for (状态类 状态 : 先手值减少免疫状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<先手值减少免疫>");
		}
		Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<技能信息>");
		for (int i = 0; i < 对战中技能数组.length; i++)
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息",String.format("%d号技能，技能描述：%s\n冷却倒计回合数%d\n", i+1, 对战中技能数组[i].get技能描述(), 对战中技能数组[i].get剩余冷却回合数()));
		}
		计算伤害减免属性();
		计算物理伤害减免属性();
		计算魔法伤害减免属性();
		Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<伤害减免属性>");
		Gdx.app.debug("对战中英雄类.输出对战中英雄信息",String.format("伤害减免%d|%.1f%%,物理伤害减免%d|%.1f%%,魔法伤害减免%d|%.1f%%\n", 伤害数值减免, 伤害百分比减免 * 100, 物理伤害数值减免, 物理伤害百分比减免 * 100, 魔法伤害数值减免, 魔法伤害百分比减免 * 100));
		Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<隐身相关属性>");
		Gdx.app.debug("对战中英雄类.输出对战中英雄信息",String.format("隐身:%b,显隐%b\n", is对战中隐身(), is对战中隐身()));
		Gdx.app.debug("对战中英雄类.输出对战中英雄信息",String.format("被致盲%b\n", is对战中被致盲()));
		计算先手值清零属性();
		if (is先手值清零)
		{
			Gdx.app.debug("对战中英雄类.输出对战中英雄信息", "<先手值清零>");
			for (状态类 状态 : 先手值清零状态集合)
			{
				Gdx.app.debug("对战中英雄类.输出对战中英雄信息", 状态.get状态信息());
			}
		}
		Gdx.app.debug("对战中英雄类.输出对战中英雄信息",String.format("获得攻击后排能力:%b\n", is对战中获得攻击后排能力()));
	}
	private String get英雄名字()
	{
		return 英雄.get英雄名字();
	}
	public static abstract class 先手值清零状态类 extends 状态类
	{
		public 先手值清零状态类(int 最大持续回合数)
		{
			super(最大持续回合数);
		}
		@Override
		public final void 作用(对战中英雄类 受状态作用英雄)
		{
			if (!受状态作用英雄.is先手值清零)
			{
				受状态作用英雄.is先手值清零 = true;
			}
		}
	}
	public static abstract class 被压制状态类 extends 状态类
	{
		public 被压制状态类(int 最大持续回合数)
		{
			super(最大持续回合数);
		}
		@Override
		public final void 作用(对战中英雄类 受状态作用英雄)
		{
			if (!受状态作用英雄.被压制)
			{
				受状态作用英雄.被压制 = true;
			}
		}
	}
	public static abstract class 先手值减少免疫状态类 extends 状态类
	{
		public 先手值减少免疫状态类(int 最大持续回合数)
		{
			super(最大持续回合数);
		}
		@Override
		public final void 作用(对战中英雄类 受状态作用英雄)
		{
			if (!受状态作用英雄.is先手值减少免疫)
			{
				受状态作用英雄.is先手值减少免疫 = true;
			}
		}
	}
	public static abstract class 敌方阵容状态触发本英雄状态类 extends 状态类
	{
		public 敌方阵容状态触发本英雄状态类(int 最大持续回合数)
		{
			super(最大持续回合数);
		}
		public abstract void 被通知敌方阵容状态(对战中英雄类 被通知英雄, 对战中阵容类 敌方阵容);
	}
	public static abstract class 普通攻击触发效果状态类 extends 状态类
	{
		public 普通攻击触发效果状态类(int 最大持续回合数)
		{
			super(最大持续回合数);
		}
		public 普通攻击触发效果状态类(int 最大持续回合数, int 最大层数, int 初始层数)
		{
			super(最大持续回合数, 最大层数, 初始层数);
		}
		/**
		 * 状态作用于普通攻击时需实现该方法，迭代器是当状态需要从集合中移除时用的
		 */
		public void 普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者, Iterator<普通攻击触发效果状态类> 迭代器){}
	}
	public static abstract class 被动持续释放状态类 extends 状态类
	{
		public 被动持续释放状态类(int 最大持续回合数) {
			super(最大持续回合数);
		}
		public void 被动释放(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
		{
			Gdx.app.debug("对战中英雄类.被动持续释放状态类.被动释放",String.format("%s被动持续释放%s\n", 释放此技能的英雄.get阵容名所在位置英雄名字(), get状态信息()));
		}
	}
	public static abstract class 主动持续施法状态类 extends 状态类
	{
		public 主动持续施法状态类(int 最大持续回合数)
		{
			super(最大持续回合数);
		}
		public void 主动施法(对战中阵容类 敌方阵容, 对战中英雄类 释放此技能的英雄)
		{
			Gdx.app.debug("对战中英雄类.主动持续释放状态类.主动释放", String.format("%s主动持续施法%s\n", 释放此技能的英雄.get阵容名所在位置英雄名字(), get状态信息()));
		}
		private void 主动持续施法被打断(对战中英雄类 被打断英雄)
		{
			被打断英雄.主动持续施法状态 = null;
			被通知被打断(被打断英雄);
		}
		/**
		 * 子类实现该方法做被打断时该做的事
		 */
		protected void 被通知被打断(对战中英雄类 被打断英雄)
		{}
	}
	private static class 隐身状态类 extends 状态类
	{
		public 隐身状态类(int 最大持续回合数)
		{
			super(最大持续回合数);
		}
		@Override
		public final void 作用(对战中英雄类 受状态作用英雄)
		{
			if (!受状态作用英雄.is隐身)
			{
				受状态作用英雄.is隐身 = true;
			}
		}
		@Override
		public String get状态描述()
		{
			return "隐身";
		}	
	}
	public static abstract class 显隐状态类 extends 状态类
	{
		public 显隐状态类(int 最大持续回合数)
		{
			super(最大持续回合数);
		}
		@Override
		public final void 作用(对战中英雄类 受状态作用英雄)
		{
			if (!受状态作用英雄.is显隐)
			{
				受状态作用英雄.is显隐 = true;
			}
		}
	}
	public static abstract class 护盾状态类 extends 状态类
	{
		private int 护盾值;
		/**
		 * 用于有护盾值的护盾
		 * @param 最大持续回合数
		 * @param 护盾值 正数
		 */
		public 护盾状态类(int 最大持续回合数, int 护盾值) {
			super(最大持续回合数);
			this.护盾值 = 护盾值;
		}
		/**
		 * 用于无敌等无护盾值得护盾
		 * @param 最大持续回合数
		 */
		public 护盾状态类(int 最大持续回合数)
		{
			super(最大持续回合数);
		}
		/**
		 * @param 伤害 正数
		 * @param 迭代器 当子类不能吸收伤害后从集合中删除
		 * @return 正数
		 */
		public int 吸收伤害(int 生命值, int 伤害, Iterator<护盾状态类> 迭代器)
		{
			if (护盾值 > 伤害)
			{
				护盾值 -= 伤害;
				return 0;
			}
			else
			{
				迭代器.remove();
				return 伤害 - 护盾值;
			}
		}
	}
	public static abstract class 被普通攻击触发效果状态类 extends 状态类
	{
		public 被普通攻击触发效果状态类(int 最大持续回合数)
		{
			super(最大持续回合数);
		}
		public abstract void 被普通攻击(对战中英雄类 攻击者, 对战中英雄类 被攻击者);
	}
	private static class 获得攻击后排能力状态类 extends 状态类
	{
		public 获得攻击后排能力状态类(int 最大持续回合数)
		{
			super(最大持续回合数);
		}
		@Override
		public final void 作用(对战中英雄类 受状态作用英雄)
		{
			if (!受状态作用英雄.is获得攻击后排能力)
			{
				受状态作用英雄.is获得攻击后排能力 = true;
			}
		}
		@Override
		public String get状态描述()
		{
			return "获得攻击后排能力";
		}
	}
	private static class 被致盲状态类 extends 状态类
	{
		public 被致盲状态类(int 最大持续回合数)
		{
			super(最大持续回合数);
		}
		@Override
		public final void 作用(对战中英雄类 受状态作用英雄)
		{
			if (!受状态作用英雄.被致盲)
			{
				受状态作用英雄.被致盲 = true;
			}
		}
		@Override
		public String get状态描述()
		{
			return "被致盲";
		}
	}
	public static final class 被集火状态类 extends 状态类
	{
		public static int 最大未被集火编号 = -1;
		private static int 状态被集火编号计数器 = 最大未被集火编号 + 1;
		private static int get下一个状态被集火编号()
		{
			状态被集火编号计数器++;
			return 状态被集火编号计数器;
		}
		private int 状态被集火编号 = -1;
		public 被集火状态类(int 最大持续回合数) {
			super(最大持续回合数);
			this.状态被集火编号 = get下一个状态被集火编号();
		}
		@Override
		public final void 作用(对战中英雄类 受状态作用英雄) {
			if (受状态作用英雄.对战中被集火编号 < 状态被集火编号)
			{
				受状态作用英雄.对战中被集火编号 = 状态被集火编号;
			}
		}
		@Override
		public String get状态描述() {
			return "被集火";
		}
	}
	private class 被沉默状态类 extends 状态类
	{
		public 被沉默状态类(int 最大持续回合数) {
			super(最大持续回合数);
		}
		@Override
		public final void 作用(对战中英雄类 受状态作用英雄) {
			if (!受状态作用英雄.被沉默)
			{
				受状态作用英雄.被沉默 = true; 
			}
		}
		@Override
		public String get状态描述() {
			return "被沉默";
		}
	}
	private class 被冰冻状态类 extends 状态类
	{
		public 被冰冻状态类(int 最大持续回合数) {
			super(最大持续回合数);
		}
		@Override
		public final void 作用(对战中英雄类 受状态作用英雄) {
			if (!受状态作用英雄.被冰冻)
			{
				受状态作用英雄.被冰冻 = true;
			}
		}
		@Override
		public String get状态描述() {
			return "被冰冻";
		}
	}
	private class 被击飞状态类 extends 状态类
	{
		public 被击飞状态类(int 最大持续回合数) {
			super(最大持续回合数);
		}
		@Override
		public final void 作用(对战中英雄类 受状态作用英雄) {
			if (!受状态作用英雄.被击飞)
			{
				受状态作用英雄.被击飞 = true;
			}
		}
		@Override
		public String get状态描述() {
			return "被击飞";
		}
		
	}
	private class 被嘲讽状态类 extends 状态类
	{
		public 被嘲讽状态类(int 最大持续回合数) {
			super(最大持续回合数);
		}
		@Override
		public final void 作用(对战中英雄类 受状态作用英雄) {
			if (!受状态作用英雄.被嘲讽)
			{
				受状态作用英雄.被嘲讽 = true;
			}
		}
		@Override
		public String get状态描述() {
			return "被嘲讽";
		}
	}
	private class 被眩晕状态类 extends 状态类
	{
		public 被眩晕状态类(int 最大持续回合数) {
			super(最大持续回合数);
		}
		@Override
		public final void 作用(对战中英雄类 受状态作用英雄) {
			if (!受状态作用英雄.被眩晕)
			{
				受状态作用英雄.被眩晕 = true;
			}
		}
		@Override
		public String get状态描述() {
			return "被眩晕";
		}
	}
	private class 设置致盲命令类 extends 命令类
	{
		private boolean is我方;
		private int 英雄所在位置;
		private boolean is致盲;
		public 设置致盲命令类(boolean is我方, int 英雄所在位置, boolean is致盲)
		{
			this.is我方 = is我方;
			this.英雄所在位置 = 英雄所在位置;
			this.is致盲 = is致盲;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 设置致盲动作类(对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置), is致盲);
		}
	}
	private class 设置致盲动作类 extends 动作类
	{
		private boolean is致盲;
		private 英雄造型类 英雄造型;
		private 设置致盲动作类(英雄造型类 英雄造型, boolean is致盲)
		{
			this.is致盲 = is致盲;
			this.英雄造型 = 英雄造型;
		}
		@Override
		public void 开始()
		{
			英雄造型.set致盲(is致盲);
		}
	}
	private class 设置隐身命令类 extends 命令类
	{
		private boolean is我方;
		private int 英雄所在位置;
		private boolean is隐身;
		public 设置隐身命令类(boolean is我方, int 英雄所在位置, boolean is隐身)
		{
			this.is我方 = is我方;
			this.英雄所在位置 = 英雄所在位置;
			this.is隐身 = is隐身;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 设置隐身动作类(对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置), is隐身);
		}
	}
	private class 设置隐身动作类 extends 动作类
	{
		private boolean is隐身;
		private 英雄造型类 英雄造型;
		private 设置隐身动作类(英雄造型类 英雄造型, boolean is隐身)
		{
			this.is隐身 = is隐身;
			this.英雄造型 = 英雄造型;
		}
		@Override
		public void 开始()
		{
			英雄造型.set隐身(is隐身);
		}
	}
	private class 设置压制命令类 extends 命令类
	{
		private boolean is我方;
		private int 英雄所在位置;
		private boolean is压制;
		public 设置压制命令类(boolean is我方, int 英雄所在位置, boolean is压制)
		{
			this.is我方 = is我方;
			this.英雄所在位置 = 英雄所在位置;
			this.is压制 = is压制;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 设置压制动作类(对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置), is压制);
		}
	}
	private class 设置压制动作类 extends 动作类
	{
		private boolean is压制;
		private 英雄造型类 英雄造型;
		private 设置压制动作类(英雄造型类 英雄造型, boolean is压制)
		{
			this.is压制 = is压制;
			this.英雄造型 = 英雄造型;
		}
		@Override
		public void 开始()
		{
			英雄造型.set压制(is压制);
		}
	}
	private class 设置眩晕命令类 extends 命令类
	{
		private boolean is我方;
		private int 英雄所在位置;
		private boolean is眩晕;
		public 设置眩晕命令类(boolean is我方, int 英雄所在位置, boolean is眩晕)
		{
			this.is我方 = is我方;
			this.英雄所在位置 = 英雄所在位置;
			this.is眩晕 = is眩晕;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 设置眩晕动作类(对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置), is眩晕);
		}
	}
	private class 设置眩晕动作类 extends 动作类
	{
		private boolean is眩晕;
		private 英雄造型类 英雄造型;
		private 设置眩晕动作类(英雄造型类 英雄造型, boolean is眩晕)
		{
			this.is眩晕 = is眩晕;
			this.英雄造型 = 英雄造型;
		}
		@Override
		public void 开始()
		{
			英雄造型.set眩晕(is眩晕);
		}
	}
	private class 设置嘲讽命令类 extends 命令类
	{
		private boolean is我方;
		private int 英雄所在位置;
		private boolean is嘲讽;
		public 设置嘲讽命令类(boolean is我方, int 英雄所在位置, boolean is嘲讽)
		{
			this.is我方 = is我方;
			this.英雄所在位置 = 英雄所在位置;
			this.is嘲讽 = is嘲讽;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 设置嘲讽动作类(对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置), is嘲讽);
		}
	}
	private class 设置嘲讽动作类 extends 动作类
	{
		private boolean is嘲讽;
		private 英雄造型类 英雄造型;
		private 设置嘲讽动作类(英雄造型类 英雄造型, boolean is嘲讽)
		{
			this.is嘲讽 = is嘲讽;
			this.英雄造型 = 英雄造型;
		}
		@Override
		public void 开始()
		{
			英雄造型.set嘲讽(is嘲讽);
		}
	}
	private class 设置击飞命令类 extends 命令类
	{
		private boolean is我方;
		private int 英雄所在位置;
		private boolean is击飞;
		public 设置击飞命令类(boolean is我方, int 英雄所在位置, boolean is击飞)
		{
			this.is我方 = is我方;
			this.英雄所在位置 = 英雄所在位置;
			this.is击飞 = is击飞;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 设置击飞动作类(对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置), is击飞);
		}
	}
	private class 设置击飞动作类 extends 动作类
	{
		private boolean is击飞;
		private 英雄造型类 英雄造型;
		private 设置击飞动作类(英雄造型类 英雄造型, boolean is击飞)
		{
			this.is击飞 = is击飞;
			this.英雄造型 = 英雄造型;
		}
		@Override
		public void 开始()
		{
			英雄造型.set击飞(is击飞);
		}
	}
	private class 设置沉默命令类 extends 命令类
	{
		private boolean is我方;
		private int 英雄所在位置;
		private boolean is沉默;
		public 设置沉默命令类(boolean is我方, int 英雄所在位置, boolean is沉默)
		{
			this.is我方 = is我方;
			this.英雄所在位置 = 英雄所在位置;
			this.is沉默 = is沉默;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 设置沉默动作类(对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置), is沉默);
		}
	}
	private class 设置沉默动作类 extends 动作类
	{
		private boolean is沉默;
		private 英雄造型类 英雄造型;
		private 设置沉默动作类(英雄造型类 英雄造型, boolean is沉默)
		{
			this.is沉默 = is沉默;
			this.英雄造型 = 英雄造型;
		}
		@Override
		public void 开始()
		{
			英雄造型.set沉默(is沉默);
		}
	}
	private class 设置冰冻命令类 extends 命令类
	{
		private boolean is我方;
		private int 英雄所在位置;
		private boolean is冰冻;
		public 设置冰冻命令类(boolean is我方, int 英雄所在位置, boolean is冰冻)
		{
			this.is我方 = is我方;
			this.英雄所在位置 = 英雄所在位置;
			this.is冰冻 = is冰冻;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 设置冰冻动作类(对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置), is冰冻);
		}
	}
	private class 设置冰冻动作类 extends 动作类
	{
		private boolean is冰冻;
		private 英雄造型类 英雄造型;
		private 设置冰冻动作类(英雄造型类 英雄造型, boolean is冰冻)
		{
			this.is冰冻 = is冰冻;
			this.英雄造型 = 英雄造型;
		}
		@Override
		public void 开始()
		{
			英雄造型.set冰冻(is冰冻);
		}
	}
	private class 设置集火命令类 extends 命令类
	{
		private boolean is我方;
		private int 英雄所在位置;
		private boolean is集火;
		public 设置集火命令类(boolean is我方, int 英雄所在位置, boolean is集火)
		{
			this.is我方 = is我方;
			this.英雄所在位置 = 英雄所在位置;
			this.is集火 = is集火;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 设置集火动作类(对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置), is集火);
		}
	}
	private class 设置集火动作类 extends 动作类
	{
		private boolean is集火;
		private 英雄造型类 英雄造型;
		private 设置集火动作类(英雄造型类 英雄造型, boolean is集火)
		{
			this.is集火 = is集火;
			this.英雄造型 = 英雄造型;
		}
		@Override
		public void 开始()
		{
			英雄造型.set集火(is集火);
		}
	}
	private static class 回血命令类 extends 命令类
	{
		private int x;
		private int y;
		private int 数值;
		private 回血命令类(int x, int y, int 数值)
		{
			this.x = x;
			this.y = y;
			this.数值 = 数值;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 回血动作类(x, y, 数值);
		}
	}
	private static class 回血动作类 extends 动作类
	{
		private static final int 跳动距离 = 10;
		private static final float 跳下时间 = 0.4f;
		private static final int 左右波动范围 = 20;
		private static LabelStyle 回血标签样式;
		private SequenceAction 跳动Action;
		private 相对移动Action 跳下Action;
		private Label 回血标签;
		private 回血动作类(int x, int y, int 数值)
		{
			if (回血标签样式 == null)
			{
				回血标签样式 = new LabelStyle(主类.实例.getUI皮肤资源().getFont("default"), Color.GREEN);
			}
			MoveByAction 跳上动作 = Actions.moveBy(0, 跳动距离, 跳下时间);
			跳下Action = new 相对移动Action(0, -跳动距离, 跳下时间);
			跳动Action = Actions.sequence(跳上动作, 跳下Action);
			float 字体缩放比例 = 1.5f;
			回血标签 = new Label(String.valueOf(数值), 回血标签样式);
			回血标签.setFontScale(字体缩放比例);
			回血标签.setSize(回血标签.getWidth() * 字体缩放比例, 回血标签.getHeight() * 字体缩放比例);
			回血标签.addAction(跳动Action);
			回血标签.setPosition(x - 回血标签.getWidth() / 2 + 计算类.随机整数值(左右波动范围 * 2) - 左右波动范围, y - 回血标签.getHeight() / 2);
		}
		@Override
		public boolean is完成()
		{
			if (跳下Action.isAction已结束())
			{
				回血标签.remove();
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
			对战屏幕类.实例.添加演员(回血标签);
		}
	}
	private static class 掉血命令类 extends 命令类
	{
		private int x;
		private int y;
		private int 数值;
		public 掉血命令类(int x, int y, int 数值)
		{
			this.x = x;
			this.y = y;
			this.数值 = 数值;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 掉血动作类(x, y, 数值);
		}
	}
	private static class 掉血动作类 extends 动作类
	{
		private static final int 跳动距离 = 10;
		private static final int 左右波动范围 = 20;
		private static LabelStyle 掉血标签样式;
		private SequenceAction 跳动Action;
		private 相对移动Action 跳下Action;
		private Label 掉血标签;
		private 掉血动作类(int x, int y, int 数值)
		{
			if (掉血标签样式 == null)
			{
				掉血标签样式 = new LabelStyle(主类.实例.getUI皮肤资源().getFont("default"), Color.RED);
			}
			MoveByAction 跳上动作 = Actions.moveBy(0, 跳动距离, 攻击前摇动作类.近战总体摆动时间);
			跳下Action = new 相对移动Action(0, -跳动距离, 攻击前摇动作类.近战总体摆动时间);
			跳动Action = Actions.sequence(跳上动作, 跳下Action);
			掉血标签 = new Label(String.valueOf(数值), 掉血标签样式);
			float 字体缩放比例 = 1.5f;
			掉血标签.setFontScale(字体缩放比例);
			掉血标签.setSize(掉血标签.getWidth() * 字体缩放比例, 掉血标签.getHeight() * 字体缩放比例);
			掉血标签.addAction(跳动Action);
			掉血标签.setPosition(x - 掉血标签.getWidth() / 2 + 计算类.随机整数值(左右波动范围 * 2) - 左右波动范围, y - 掉血标签.getHeight() / 2);
		}
		@Override
		public boolean is完成()
		{
			if (跳下Action.isAction已结束())
			{
				掉血标签.remove();
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
			对战屏幕类.实例.添加演员(掉血标签);
		}
	}
	private static class 被攻击晃动命令类 extends 命令类
	{
		private boolean is我方;
		private int 英雄所在位置;
		public 被攻击晃动命令类(boolean is我方, int 英雄所在位置)
		{
			this.is我方 = is我方;
			this.英雄所在位置 = 英雄所在位置;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 被攻击晃动动作类(对战屏幕类.实例.get英雄造型(is我方, 英雄所在位置), is我方);
		}
	}
	private static class 被攻击晃动动作类 extends 动作类
	{
		private static final int 晃动距离 = 10;
		private static final float 总体晃动时间 = 0.3f;
		private Actor 英雄图片;
		private SequenceAction 晃动Action;
		private 相对移动Action 晃回Action;
		public 被攻击晃动动作类(Actor 英雄图片, boolean is我方)
		{
			this.英雄图片 = 英雄图片;
			MoveByAction 跳上动作 = Actions.moveBy(0, is我方 ? -晃动距离 : 晃动距离, 总体晃动时间 / 2);
			晃回Action = new 相对移动Action(0, is我方 ? 晃动距离 : -晃动距离, 总体晃动时间 / 2);
			晃动Action = Actions.sequence(跳上动作, 晃回Action);
		}
		@Override
		public boolean is完成()
		{
			if (晃回Action.isAction已结束())
			{
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
			英雄图片.addAction(晃动Action);
		}
	}
	private static class 英雄阵亡命令类 extends 命令类
	{
		private int 敌人击杀数;
		private boolean is我方阵亡;
		private int 位置;
		private String 音效文件名;
		public 英雄阵亡命令类(int 敌人击杀数, boolean is我方阵亡, int 位置, String 音效文件名)
		{
			this.敌人击杀数 = 敌人击杀数;
			this.is我方阵亡 = is我方阵亡;
			this.位置 = 位置;
			this.音效文件名 = 音效文件名;
		}
		@Override
		protected 动作类 生成动作()
		{
			return new 英雄阵亡动作类(敌人击杀数, is我方阵亡, 位置, 音效文件名);
		}
	}
	private static class 英雄阵亡动作类 extends 动作类
	{
		private 相对移动Action 移动Action = new 相对移动Action(0, 100, 0.6f);
		private 粒子演员类 骷髅头粒子;
		private boolean is我方阵亡;
		private int 位置;
		private Music 击杀音效;
		private Music 阵亡音效;
		private 英雄阵亡动作类(int 敌人击杀数, boolean is我方阵亡, int 位置, String 音效文件名)
		{
			this.is我方阵亡 = is我方阵亡;
			this.位置 = 位置;
			if (!is我方阵亡)
			{
				if (敌人击杀数 > 1)
				{
					击杀音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "Kill" + (char)(敌人击杀数-1+'A') + ".mp3"));
					击杀音效.setOnCompletionListener(new 音乐播放完自动销毁类());
				}
			}
			else
			{
				阵亡音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + 音效文件名));
				阵亡音效.setOnCompletionListener(new 音乐播放完自动销毁类());
			}
			ParticleEffect 粒子效果 = new ParticleEffect();
			粒子效果.load(Gdx.files.internal(常量类.粒子目录 + "BattleScreenSkull.p"), 对战屏幕类.实例.get皮肤资源().getAtlas());
			骷髅头粒子 = new 粒子演员类(粒子效果);
			if (is我方阵亡)
			{
				骷髅头粒子.setPosition(对战屏幕类.我方X坐标数组[位置], 对战屏幕类.我方Y坐标数组[位置]);
			}
			else
			{
				骷髅头粒子.setPosition(对战屏幕类.敌方X坐标数组[位置], 对战屏幕类.敌方Y坐标数组[位置]);
			}
			骷髅头粒子.addAction(移动Action);
		}
		@Override
		public boolean is完成()
		{
			if (移动Action.isAction已结束())
			{
				对战屏幕类.实例.被通知英雄阵亡(is我方阵亡, 位置);
				骷髅头粒子.remove();
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
			if (阵亡音效 != null)
			{
				阵亡音效.play();
			}
			if (击杀音效 != null)
			{
				击杀音效.play();
			}
			对战屏幕类.实例.添加演员(骷髅头粒子);
		}
	}
	/**
	 * 构造函数
	 * @param 英雄所在位置 1号为1，以此类推
	 */
	public 对战中英雄类 (boolean is我方, 英雄类 英雄, 对战中阵容类 所在阵容, int 英雄所在位置)
	{
		this.所在阵容 = 所在阵容;
		this.英雄所在位置 = 英雄所在位置;
		this.英雄 = 英雄;
		this.is我方 = is我方;
		对战中英雄属性 = new 英雄属性类();
		技能类[] 技能数组 = 英雄.get技能数组();
		对战中技能数组 = new 对战中技能类[技能数组.length];
		for (int i = 0; i < 技能数组.length; i++)
		{
			对战中技能数组[i] = new 对战中技能类(技能数组[i]);
		}
		生命值 = 英雄.get英雄属性().get最大生命值();
	}
	public boolean is我方()
	{
		return is我方;
	}
	public void 添加被动状态()
	{
		英雄.添加被动状态(this);
	}
	/**
	 * 输出信息用于debug
	 */
	public String get阵容名所在位置英雄名字()
	{
		return String.format("<%s，%d号位英雄%s>", get所在阵容().get阵容名(), get英雄所在位置(), get英雄名字());
	}
	public void 被通知敌方阵容有英雄阵亡()
	{
		for (状态类 状态 : 敌方有英雄阵亡触发状态集合)
		{
			状态.作用(this);
		}
	}
	public void 被通知敌方阵容状态(对战中阵容类 敌方阵容)
	{
		for (敌方阵容状态触发本英雄状态类 状态 : 敌方阵容状态触发本英雄状态集合)
		{
			状态.被通知敌方阵容状态(this, 敌方阵容);
		}
	}
	public boolean 释放主动持续施法(对战中阵容类 敌方阵容)
	{
		if (主动持续施法状态 != null)
		{
			主动持续施法状态.主动施法(敌方阵容, this);
			return true;
		}
		else
		{
			return false;
		}
	}
	public void 被动持续释放状态释放(对战中阵容类 敌方阵容)
	{
		for (被动持续释放状态类 状态 : 被动持续释放状态集合)
		{
			状态.被动释放(敌方阵容, this);
		}
	}
	public void 添加攻击力数值变化(int 数值变化)
	{
		英雄属性变化.添加攻击力数值变化(数值变化);
	}
	public void 添加攻击力百分比变化(float 百分比变化)
	{
		英雄属性变化.添加攻击力百分比变化(百分比变化);
	}
	public void 添加法术强度数值变化(int 数值变化)
	{
		英雄属性变化.添加法术强度数值变化(数值变化);
	}
	public void 添加法术强度百分比变化(float 百分比变化)
	{
		英雄属性变化.添加法术强度百分比变化(百分比变化);
	}
	public void 添加先手值数值变化(int 数值变化)
	{
		英雄属性变化.添加先手值数值变化(数值变化);
	}
	public void 添加先手值百分比变化(float 百分比变化)
	{
		英雄属性变化.添加先手值百分比变化(百分比变化);
	}
	public void 添加护甲数值变化(int 数值变化)
	{
		英雄属性变化.添加护甲数值变化(数值变化);
	}
	public void 添加护甲百分比变化(float 百分比变化)
	{
		英雄属性变化.添加护甲百分比变化(百分比变化);
	}
	public void 添加攻击速度百分比变化(float 百分比变化)
	{
		英雄属性变化.添加攻击速度百分比变化(百分比变化);
	}
	public void 添加魔法抗性数值变化(int 数值变化)
	{
		英雄属性变化.添加魔法抗性数值变化(数值变化);
	}
	public void 添加魔法抗性百分比变化(float 百分比变化)
	{
		英雄属性变化.添加魔法抗性百分比变化(百分比变化);
	}
	public void 添加最大生命值数值变化(int 数值变化)
	{
		英雄属性变化.添加最大生命值数值变化(数值变化);
	}
	public void 添加最大生命值百分比变化(float 百分比变化)
	{
		英雄属性变化.添加最大生命值百分比变化(百分比变化);
	}
	public void 添加生命回复数值变化(int 数值变化)
	{
		英雄属性变化.添加生命回复数值变化(数值变化);
	}
	public void 添加生命回复百分比变化(float 百分比变化)
	{
		英雄属性变化.添加生命回复百分比变化(百分比变化);
	}
	public void 添加数值护甲穿透变化(int 数值变化)
	{
		英雄属性变化.添加数值护甲穿透变化(数值变化);
	}
	public void 添加百分比护甲穿透变化(float 百分比变化)
	{
		英雄属性变化.添加百分比护甲穿透变化(百分比变化);
	}
	public void 添加数值法术穿透变化(int 数值变化)
	{
		英雄属性变化.添加数值法术穿透变化(数值变化);
	}
	public void 添加百分比法术穿透变化(float 百分比变化)
	{
		英雄属性变化.添加百分比法术穿透变化(百分比变化);
	}
	public void 添加生命偷取百分比变化(float 百分比变化)
	{
		英雄属性变化.添加生命偷取百分比变化(百分比变化);
	}
	public void 添加暴击几率百分比变化(float 百分比变化)
	{
		英雄属性变化.添加暴击几率百分比变化(百分比变化);
	}
	public void 添加法术吸血百分比变化(float 百分比变化)
	{
		英雄属性变化.添加法术吸血百分比变化(百分比变化);
	}
	public void 添加韧性百分比变化(float 百分比变化)
	{
		英雄属性变化.添加韧性百分比变化(百分比变化);
	}
	/**
	 * @param 减免 正数
	 */
	public void 添加伤害数值减免(int 减免)
	{
		伤害数值减免 += 减免;
	}
	/**
	 * @param 减免 正数
	 */
	public void 添加伤害百分比减免(float 减免)
	{
		伤害百分比减免 += 减免;
	}
	/**
	 * @param 减免 正数
	 */
	public void 添加物理伤害数值减免(int 减免)
	{
		物理伤害数值减免 += 减免;
	}
	/**
	 * @param 减免 正数
	 */
	public void 添加物理伤害百分比减免(float 减免)
	{
		物理伤害百分比减免 += 减免;
	}
	/**
	 * @param 减免 正数
	 */
	public void 添加魔法伤害数值减免(int 减免)
	{
		魔法伤害数值减免 += 减免;
	}
	/**
	 * @param 减免 正数
	 */
	public void 添加魔法伤害百分比减免(float 减免)
	{
		魔法伤害百分比减免 += 减免;
	}
	public void 被集火(int 被集火持续回合数, 并行命令类 并行命令)
	{
		被集火状态集合.add(new 被集火状态类(被集火持续回合数));
		if (并行命令 == null)
		{
			命令组装器类.实例.添加命令(new 设置集火命令类(is我方, 英雄所在位置, is对战中被集火()));
		}
		else
		{
			并行命令.添加命令(new 设置集火命令类(is我方, 英雄所在位置, is对战中被集火()));
		}
	}
	public boolean is正在持续施法()
	{
		return 主动持续施法状态 != null;
	}
	public boolean is对战中被集火()
	{
		return get对战中被集火编号() > 被集火状态类.最大未被集火编号;
	}
	public boolean is对战中被沉默() {
		计算被沉默属性();
		return 被沉默;
	}
	public boolean is对战中被冰冻() {
		计算被冰冻属性();
		return 被冰冻;
	}
	public boolean is对战中被击飞() {
		计算被击飞属性();
		return 被击飞;
	}
	public boolean is对战中被嘲讽() {
		计算被嘲讽属性();
		return 被嘲讽;
	}
	public boolean is对战中被眩晕() {
		计算被眩晕属性();
		return 被眩晕;
	}
	public boolean is对战中被压制()
	{
		计算被压制属性();
		return 被压制;
	}
	public boolean is阵亡() {
		return 阵亡;
	}
	public boolean is对战中获得攻击后排能力()
	{
		计算获得攻击后排能力();
		return is获得攻击后排能力;
	}
	private boolean is对战中被致盲()
	{
		计算被致盲属性();
		return 被致盲;
	}
	private boolean is对战中先手值清零()
	{
		计算先手值清零属性();
		return is先手值清零;
	}
	public boolean is对战中隐身()
	{
		计算隐身属性();
		计算显隐属性();
		计算被集火编号();
		return is隐身 && !is显隐 && 对战中被集火编号 <= 被集火状态类.最大未被集火编号; 
	}
	public 英雄类 get英雄()
	{
		return 英雄;
	}
	/**
	 * 1-5
	 */
	public int get英雄所在位置() {
		return 英雄所在位置;
	}
	public 对战中阵容类 get所在阵容()
	{
		return 所在阵容;
	}
	public int get对战中护甲值()
	{
		计算护甲值();
		return 对战中英雄属性.get护甲值();
	}
	public int get对战中生命值()
	{
		return 生命值;
	}
	public int get对战中最大生命值()
	{
		计算最大生命值();
		return 对战中英雄属性.get最大生命值();
	}
	/**
	 * 根据攻击速度计算本回合攻击次数
	 */
	public int get对战中攻击总次数()
	{
		计算攻击速度();
		return 计算类.根据_攻击速度_计算攻击总次数(对战中英雄属性.get攻击速度());
	}
	public int get对战中先手值()
	{
		计算先手值();
		return 对战中英雄属性.get先手值();
	}
	public int get对战中数值法术穿透()
	{
		计算数值法术穿透();
		return 对战中英雄属性.get数值法术穿透();
	}
	public int get对战中数值护甲穿透()
	{
		计算数值护甲穿透();
		return 对战中英雄属性.get数值护甲穿透();
	}
	public float get对战中百分比法术穿透()
	{
		计算百分比法术穿透();
		return 对战中英雄属性.get百分比法术穿透();
	}
	public float get对战中百分比护甲穿透()
	{
		计算百分比护甲穿透();
		return 对战中英雄属性.get百分比护甲穿透();
	}
	public float get对战中法术吸血()
	{
		计算法术吸血();
		return 对战中英雄属性.get法术吸血();
	}
	public int get对战中攻击力()
	{
		计算攻击力();
		return 对战中英雄属性.get攻击力();
	}
	public float get对战中暴击几率()
	{
		计算暴击几率();
		return 对战中英雄属性.get暴击几率();
	}
	public int get对战中法术强度()
	{
		计算法术强度();
		return 对战中英雄属性.get法术强度();
	}
	public int get对战中被集火编号()
	{
		计算被集火编号();
		return 对战中被集火编号;
	}
	public int get初始最大生命值()
	{
		return 英雄.get英雄属性().get最大生命值();
	}
	public float get初始攻击速度()
	{
		return 英雄.get英雄属性().get攻击速度();
	}
	public int get初始先手值()
	{
		return 英雄.get英雄属性().get先手值();
	}
	public int get初始数值法术穿透()
	{
		return 英雄.get英雄属性().get数值法术穿透();
	}
	public int get初始数值护甲穿透()
	{
		return 英雄.get英雄属性().get数值护甲穿透();
	}
	public float get初始百分比法术穿透()
	{
		return 英雄.get英雄属性().get百分比法术穿透();
	}
	public float get初始百分比护甲穿透()
	{
		return 英雄.get英雄属性().get百分比护甲穿透();
	}
	public float get初始法术吸血()
	{
		return 英雄.get英雄属性().get法术吸血();
	}
	public int get初始攻击力()
	{
		return 英雄.get英雄属性().get攻击力();
	}
	public float get初始暴击几率()
	{
		return 英雄.get英雄属性().get暴击几率();
	}
	public int get初始法术强度()
	{
		return 英雄.get英雄属性().get法术强度();
	}
	public int get初始魔法抗性()
	{
		return 英雄.get英雄属性().get魔法抗性();
	}
	public float get初始生命偷取()
	{
		return 英雄.get英雄属性().get生命偷取();
	}
	public float get初始韧性()
	{
		return 英雄.get英雄属性().get韧性();
	}
	public int get初始护甲值()
	{
		return 英雄.get英雄属性().get护甲值();
	}
	public int get初始生命回复()
	{
		return 英雄.get英雄属性().get生命回复();
	}
	public 状态类 get魔法抗性减少状态(Class<? extends 状态类> clazz)
	{
		return get状态(clazz, 魔法抗性减少状态集合);
	}
	public 状态类 get魔法抗性增加状态(Class<? extends 状态类> clazz)
	{
		return get状态(clazz, 魔法抗性增加状态集合);
	}
	public 状态类 get普通攻击触发效果状态(Class<? extends 状态类> clazz)
	{
		return get状态(clazz, 普通攻击触发效果状态集合);
	}
	public 状态类 get被普通攻击触发效果状态(Class<? extends 状态类> clazz)
	{
		return get状态(clazz, 被普通攻击触发效果状态集合);
	}
	public 状态类 get先手值增加状态(Class<? extends 状态类> clazz)
	{
		return get状态(clazz, 先手值增加状态集合);
	}
	public 状态类 get攻击速度增加状态(Class<? extends 状态类> clazz)
	{
		return get状态(clazz, 攻击速度增加状态集合);
	}
	public 状态类 get先手值减少免疫状态(Class<? extends 状态类> clazz)
	{
		return get状态(clazz, 先手值减少免疫状态集合);
	}
	public 状态类 get先手值减少状态(Class<? extends 状态类> clazz)
	{
		return get状态(clazz, 先手值减少状态集合);
	}
	public 状态类 get伤害减免状态(Class<? extends 状态类> clazz)
	{
		return get状态(clazz, 伤害减免状态集合);
	}
	public 状态类 get显隐状态(Class<? extends 状态类> clazz)
	{
		return get状态(clazz, 显隐状态集合);
	}
	public 状态类 get护甲减少状态(Class<? extends 状态类> clazz)
	{
		return get状态(clazz, 护甲值减少状态集合);
	}
	public 状态类 get护盾状态(Class<? extends 状态类> clazz)
	{
		return get状态(clazz, 护盾状态集合);
	}
	public 状态类 get法术吸血增加状态(Class<? extends 状态类> clazz)
	{
		return get状态(clazz, 法术吸血增加状态集合);
	}
	public 状态类 get法术强度增加状态(Class<? extends 状态类> clazz)
	{
		return get状态(clazz, 法术强度增加状态集合);
	}
	public 状态类 get生命值减少状态(Class<? extends 状态类> clazz)
	{
		return get状态(clazz, 生命值减少状态集合);
	}
	private 状态类 get状态(Class<? extends 状态类> clazz, Set<? extends 状态类> 状态集合)
	{
		for (状态类 状态 : 状态集合)
		{
			if (状态.getClass() == clazz)
			{
				return 状态;
			}
		}
		return null;
	}
	/**
	 * @param 攻击者物理伤害
	 * @param 数值护甲穿透
	 * @param 百分比护甲穿透
	 * @param 并行命令 若为null，则直接添加到对战屏幕动作中 
	 * @return
	 */
	public int 被造成物理伤害(对战中英雄类 攻击释放者, int 攻击者物理伤害, int 数值护甲穿透, float 百分比护甲穿透, 并行命令类 并行命令)
	{
		计算护甲值();
		int 被造成伤害时护甲值 = 计算类.根据_初始值_数值变化_百分比变化_计算值(对战中英雄属性.get护甲值(), 数值护甲穿透, 百分比护甲穿透);
		float 护甲物理伤害百分比减免 = 计算类.根据_数值_计算百分比减免(被造成伤害时护甲值);
		int 所受伤害 = 计算类.根据_百分比变化_计算值(攻击者物理伤害, 护甲物理伤害百分比减免);
		计算伤害减免属性();
		计算物理伤害减免属性();
		所受伤害 = 计算类.根据_初始值_数值变化_百分比变化_计算值(所受伤害, -伤害数值减免, -伤害百分比减免);
		所受伤害 = 计算类.根据_初始值_数值变化_百分比变化_计算值(所受伤害, -物理伤害数值减免, -物理伤害百分比减免);
		int 护盾吸收后伤害 = 所受伤害;
		Iterator<护盾状态类> 护盾迭代器 = 无敌状态集合.iterator();
		while (护盾迭代器.hasNext())
		{
			护盾吸收后伤害 = 护盾迭代器.next().吸收伤害(生命值, 护盾吸收后伤害, 护盾迭代器);
		}
		护盾迭代器 = 护盾状态集合.iterator();
		while (护盾迭代器.hasNext())
		{
			护盾吸收后伤害 = 护盾迭代器.next().吸收伤害(生命值, 护盾吸收后伤害, 护盾迭代器);
		}
		Gdx.app.debug("对战中英雄类.被造成物理伤害",String.format("%s被造成物理伤害，护盾减少%d\n", get阵容名所在位置英雄名字(), 所受伤害 - 护盾吸收后伤害));
		return 减少生命值(攻击释放者, -护盾吸收后伤害, 并行命令);
	}
	/**
	 * @param 攻击者魔法伤害 正数
	 * @param 数值法术穿透 正数
	 * @param 百分比法术穿透 正数
	 * @param 并行动作 若为null，则直接添加到对战屏幕动作中 
	 * @return 实际造成伤害
	 */
	public int 被造成魔法伤害(对战中英雄类 攻击释放者, int 攻击者魔法伤害, int 数值法术穿透, float 百分比法术穿透, 并行命令类 并行命令)
	{
		计算魔法抗性();
		int 被造成伤害时魔法抗性 = 计算类.根据_初始值_数值变化_百分比变化_计算值(对战中英雄属性.get魔法抗性(), 数值法术穿透, 百分比法术穿透);
		float 魔抗魔法伤害百分比减免 = 计算类.根据_数值_计算百分比减免(被造成伤害时魔法抗性);
		int 所受伤害 = 计算类.根据_百分比变化_计算值(攻击者魔法伤害, 魔抗魔法伤害百分比减免);
		计算伤害减免属性();
		计算魔法伤害减免属性();
		所受伤害 = 计算类.根据_初始值_数值变化_百分比变化_计算值(所受伤害, -伤害数值减免, -伤害百分比减免);
		所受伤害 = 计算类.根据_初始值_数值变化_百分比变化_计算值(所受伤害, -魔法伤害数值减免, -魔法伤害百分比减免);
		int 护盾吸收后伤害 = 所受伤害;
		Iterator<护盾状态类> 护盾迭代器 = 无敌状态集合.iterator();
		while (护盾迭代器.hasNext())
		{
			护盾吸收后伤害 = 护盾迭代器.next().吸收伤害(生命值, 护盾吸收后伤害, 护盾迭代器);
		}
		护盾迭代器 = 护盾状态集合.iterator();
		while (护盾迭代器.hasNext())
		{
			护盾吸收后伤害 = 护盾迭代器.next().吸收伤害(生命值, 护盾吸收后伤害, 护盾迭代器);
		}
		int 护盾吸收伤害 = 所受伤害 - 护盾吸收后伤害;
		int 魔法护盾吸收后伤害 = 护盾吸收后伤害;
		护盾迭代器 = 魔法护盾状态集合.iterator();
		while (护盾迭代器.hasNext())
		{
			魔法护盾吸收后伤害 = 护盾迭代器.next().吸收伤害(生命值, 魔法护盾吸收后伤害, 护盾迭代器);
		}
		int 魔法护盾吸收伤害 = 护盾吸收后伤害 - 魔法护盾吸收后伤害;
		Gdx.app.debug("对战中英雄类.被造成魔法伤害",String.format("%s被造成魔法伤害，护盾减少%d，魔法护盾减少%d\n",  get阵容名所在位置英雄名字(), 护盾吸收伤害, 魔法护盾吸收伤害));
		return 减少生命值(攻击释放者, -魔法护盾吸收后伤害, 并行命令);
	}
	/**
	 * @param 攻击者真实伤害 正数
	 * @param 并行动作 若为null，则直接添加到对战屏幕动作中 
	 */
	public void 被造成真实伤害(对战中英雄类 攻击释放者, int 攻击者真实伤害, 并行命令类 并行命令)
	{
		int 护盾吸收后伤害 = 攻击者真实伤害;
		Iterator<护盾状态类> 护盾迭代器 = 无敌状态集合.iterator();
		while (护盾迭代器.hasNext())
		{
			护盾吸收后伤害 = 护盾迭代器.next().吸收伤害(生命值, 护盾吸收后伤害, 护盾迭代器);
		}
		护盾迭代器 = 护盾状态集合.iterator();
		while (护盾迭代器.hasNext())
		{
			护盾吸收后伤害 = 护盾迭代器.next().吸收伤害(生命值, 护盾吸收后伤害, 护盾迭代器);
		}
		Gdx.app.debug("对战中英雄类.被造成真实伤害",String.format("%s被造成真实伤害，护盾减少%d，生命减少%d\n", get阵容名所在位置英雄名字(), 攻击者真实伤害 - 护盾吸收后伤害, 护盾吸收后伤害));
		减少生命值(攻击释放者, -护盾吸收后伤害, 并行命令);
	}
	/**
	 * 调用该方法告之时间已过一回合
	 */
	public void 被通知时间已过一回合()
	{
		// 通知所有状态时间已过一回合
		// 因为可能有状态需要从集合中移除所以需要用到迭代器
		Iterator<? extends 状态类> 迭代器 = 攻击力增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 攻击力减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 敌方有英雄阵亡触发状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 法术强度增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 法术强度减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 先手值增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 先手值减少免疫状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 先手值减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 护甲值增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 护甲值减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 攻击速度增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 攻击速度减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 魔法抗性增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 魔法抗性减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 最大生命值增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 最大生命值减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 生命值增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 生命值减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 生命回复增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 生命回复减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 护甲穿透数值增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 护甲穿透百分比增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 护甲穿透数值减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 护甲穿透百分比减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 法术穿透数值增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 法术穿透百分比增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 法术穿透数值减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 法术穿透百分比减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 生命偷取增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 生命偷取减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 暴击几率增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 暴击几率减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 法术吸血增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 隐身状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 显隐状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 法术吸血减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		// 计算韧性并作用于被沉默、被冰冻、被击飞、被嘲讽、被眩晕状态
		// 先计算韧性再通知韧性状态时间已过一回合
		计算韧性();
		float 韧性 = 对战中英雄属性.get韧性();
		迭代器 = 被沉默状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器, 韧性);
		}
		迭代器 = 被冰冻状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器, 韧性);
		}
		迭代器 = 被击飞状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器, 韧性);
		}
		迭代器 = 被嘲讽状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器, 韧性);
		}
		迭代器 = 被眩晕状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器, 韧性);
		}
		迭代器 = 被压制状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器, 韧性);
		}
		迭代器 = 韧性增加状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 韧性减少状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 伤害减免状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 物理伤害减免状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 魔法伤害减免状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 先手值清零状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 被致盲状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 被集火状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 获得攻击后排能力状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		// 通知所有技能时间已过一回合
		for (int i = 0; i < 对战中技能数组.length; i++)
		{
			对战中技能数组[i].通知时间已过一回合();
		}
		if (主动持续施法状态 != null)
		{
			主动持续施法状态 = (主动持续施法状态类)主动持续施法状态.被通知时间已经过一回合();
		}
		迭代器 = 被动持续释放状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 普通攻击触发效果状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 被普通攻击触发效果状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 护盾状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 魔法护盾状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 无敌状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
		迭代器 = 敌方阵容状态触发本英雄状态集合.iterator();
		while (迭代器.hasNext())
		{
			迭代器.next().被通知时间已经过一回合(迭代器);
		}
	}
	/**
	 * 伤害减少百分比是指这次普通攻击攻击力减少百分比，比如无极剑圣的双重打击的第四下攻击附加的伤害需要减少50%
	 * @param 敌方英雄
	 * @param 攻击力减少百分比 正数
	 * @param is普通攻击触发效果 普通攻击是否触发效果
	 */
	public void 普通攻击(对战中英雄类 敌方英雄, float 攻击力减少百分比, boolean is普通攻击触发效果, boolean is奇数次攻击)
	{
		if (is对战中被致盲())
		{
			Gdx.app.debug("对战中英雄类.普通攻击",String.format("%s被致盲，普通攻击未命中\n", get阵容名所在位置英雄名字()));
		}
		else
		{
			int 攻击力 = 计算类.根据_百分比变化_计算值(get对战中攻击力(), -攻击力减少百分比);
			并行命令类 并行命令 = new 并行命令类();
			// 如果暴击则攻击力翻倍
			if (计算类.根据_概率_计算是否发生(get对战中暴击几率()))
			{
				并行命令.添加命令(new 状态文本显示命令类(is我方, 英雄所在位置, "暴击", true));
				攻击力 *= 2;
			}
			Gdx.app.debug("对战中英雄类.普通攻击",String.format("%s对%s普通攻击\n",  get阵容名所在位置英雄名字(), 敌方英雄.get阵容名所在位置英雄名字()));
			并行命令.添加命令(new 攻击前摇命令类(is我方, 英雄所在位置, is奇数次攻击, is我方));
			if (is我方)
			{
				并行命令.添加命令(英雄.get普通攻击命令(对战屏幕类.我方X坐标数组[英雄所在位置-1], 对战屏幕类.我方Y坐标数组[英雄所在位置-1], 对战屏幕类.敌方X坐标数组[敌方英雄.英雄所在位置-1], 对战屏幕类.敌方Y坐标数组[敌方英雄.英雄所在位置-1], is奇数次攻击, 英雄.get英雄皮肤文件名(), this));
			}
			else
			{
				并行命令.添加命令(英雄.get普通攻击命令(对战屏幕类.敌方X坐标数组[英雄所在位置-1], 对战屏幕类.敌方Y坐标数组[英雄所在位置-1], 对战屏幕类.我方X坐标数组[敌方英雄.英雄所在位置-1], 对战屏幕类.我方Y坐标数组[敌方英雄.英雄所在位置-1], is奇数次攻击, 英雄.get英雄皮肤文件名(), this));
			}
			int 敌方英雄所受实际伤害 = 0;
			if (英雄.is近战(this))
			{
				敌方英雄所受实际伤害 = 敌方英雄.被造成物理伤害(this, 攻击力, get对战中数值护甲穿透(), get对战中百分比护甲穿透(), 并行命令);
				命令组装器类.实例.添加命令(并行命令);
			}
			else
			{
				命令组装器类.实例.添加命令(并行命令);
				敌方英雄所受实际伤害 = 敌方英雄.被造成物理伤害(this, 攻击力, get对战中数值护甲穿透(), get对战中百分比护甲穿透(), null);
			}
			生命偷取(敌方英雄所受实际伤害);
			if (!敌方英雄.is阵亡())
			{
				Iterator<被普通攻击触发效果状态类> 被普通攻击触发效果迭代器 = 敌方英雄.被普通攻击触发效果状态集合.iterator();
				while (被普通攻击触发效果迭代器.hasNext())
				{
					被普通攻击触发效果迭代器.next().被普通攻击(this, 敌方英雄);
				}
			}
		}
		if (is普通攻击触发效果 && !阵亡)
		{
			Iterator<普通攻击触发效果状态类> 普通攻击触发效果迭代器 = 普通攻击触发效果状态集合.iterator();
			while (普通攻击触发效果迭代器.hasNext())
			{
				if (!敌方英雄.is阵亡())
				{
					普通攻击触发效果迭代器.next().普通攻击(this, 敌方英雄, 普通攻击触发效果迭代器);
				}
				else
				{
					break;
				}
			}
		}
	}
	/**
	 * @param 对敌方造成的伤害 正数
	 */
	public void 生命偷取(int 对敌方造成的伤害)
	{
		计算生命偷取();
		对敌方造成的伤害 *= 对战中英雄属性.get生命偷取();
		Gdx.app.debug("对战中英雄类.生命偷取",String.format("%s生命偷取%d\n",  get阵容名所在位置英雄名字(), 对敌方造成的伤害));
		增加生命值(对敌方造成的伤害, null);
	}
	/**
	 * @param 对敌方造成的伤害 正数
	 * @param is群体伤害
	 */
	public void 法术吸血(int 对敌方造成的伤害, boolean is群体伤害)
	{
		计算法术吸血();
		对敌方造成的伤害 *= 对战中英雄属性.get法术吸血();
		if (is群体伤害)
		{
			对敌方造成的伤害 *= 1f/3;
		}
		Gdx.app.debug("对战中英雄类.法术吸血",String.format("%s%s，法术吸血%d\n",  get阵容名所在位置英雄名字(), is群体伤害 ? "群体伤害吸血降为1/3" : "", 对敌方造成的伤害));
		增加生命值(对敌方造成的伤害, null);
	}
	public void 变化生命值()
	{
		for (状态类 状态 : 生命值增加状态集合)
		{
			状态.作用(this);
		}
		for (状态类 状态 : 生命值减少状态集合)
		{
			状态.作用(this);
		}
	}
	public void 回复生命(并行命令类 并行回血命令)
	{
		计算生命回复();
		Gdx.app.debug("对战中英雄类.回复生命",String.format("%s回复生命值\n",  get阵容名所在位置英雄名字()));
		增加生命值(对战中英雄属性.get生命回复(), 并行回血命令);
	}
	public void 添加攻击力增加状态(状态类 状态)
	{
		攻击力增加状态集合.add(状态);
	}
	public void 添加攻击力减少状态(状态类 状态)
	{
		攻击力减少状态集合.add(状态);
	}
	public void 添加法术强度增加状态(状态类 状态)
	{
		法术强度增加状态集合.add(状态);
	}
	public void 添加法术强度减少状态(状态类 状态)
	{
		法术强度减少状态集合.add(状态);
	}
	public void 添加先手值增加状态(状态类 状态)
	{
		先手值增加状态集合.add(状态);
	}
	public void 添加先手值减少免疫状态(先手值减少免疫状态类 状态)
	{
		先手值减少免疫状态集合.add(状态);
	}
	public void 添加先手值减少状态(状态类 状态)
	{
		先手值减少状态集合.add(状态);
	}
	public void 添加护甲值增加状态(状态类 状态)
	{
		护甲值增加状态集合.add(状态);
	}
	public void 添加护甲值减少状态(状态类 状态)
	{
		护甲值减少状态集合.add(状态);
	}
	public void 添加攻击速度增加状态(状态类 状态)
	{
		攻击速度增加状态集合.add(状态);
	}
	public void 添加攻击速度减少状态(状态类 状态)
	{
		攻击速度减少状态集合.add(状态);
	}
	public void 添加魔法抗性增加状态(状态类 状态)
	{
		魔法抗性增加状态集合.add(状态);
	}
	public void 添加魔法抗性减少状态(状态类 状态)
	{
		魔法抗性减少状态集合.add(状态);
	}
	public void 添加最大生命值增加状态(状态类 状态)
	{
		最大生命值增加状态集合.add(状态);
	}
	public void 添加最大生命值减少状态(状态类 状态)
	{
		最大生命值减少状态集合.add(状态);
	}
	public void 添加生命值增加状态(状态类 状态)
	{
		生命值增加状态集合.add(状态);
	}
	public void 添加生命值减少状态(状态类 状态)
	{
		生命值减少状态集合.add(状态);
	}
	public  void 添加生命回复增加状态(状态类 状态)
	{
		生命回复增加状态集合.add(状态);
	}
	public  void 添加生命回复减少状态(状态类 状态)
	{
		生命回复减少状态集合.add(状态);
	}
	public void 添加护甲穿透数值增加状态(状态类 状态)
	{
		护甲穿透数值增加状态集合.add(状态);
	}
	public void 添加护甲穿透百分比增加状态(状态类 状态)
	{
		护甲穿透百分比增加状态集合.add(状态);
	}
	public void 添加护甲穿透数值减少状态(状态类 状态)
	{
		护甲穿透数值减少状态集合.add(状态);
	}
	public void 添加护甲穿透百分比减少状态(状态类 状态)
	{
		护甲穿透百分比减少状态集合.add(状态);
	}
	public void 添加法术穿透数值增加状态(状态类 状态)
	{
		法术穿透数值增加状态集合.add(状态);
	}
	public void 添加法术穿透百分比增加状态(状态类 状态)
	{
		法术穿透百分比增加状态集合.add(状态);
	}
	public void 添加法术穿透数值减少状态(状态类 状态)
	{
		法术穿透数值减少状态集合.add(状态);
	}
	public void 添加法术穿透百分比减少状态(状态类 状态)
	{
		法术穿透百分比减少状态集合.add(状态);
	}
	public void 设置主动持续施法状态(主动持续施法状态类 主动持续施法状态)
	{
		if (this.主动持续施法状态 != null)
		{
			this.主动持续施法状态.被通知被打断(this);
		}
		this.主动持续施法状态 = 主动持续施法状态; 
	}
	public void 添加被动持续释放状态(被动持续释放状态类 状态)
	{
		被动持续释放状态集合.add(状态);
	}
	public void 添加生命偷取增加状态(状态类 状态)
	{
		生命偷取增加状态集合.add(状态);
	}
	public void 添加生命偷取减少状态(状态类 状态)
	{
		生命偷取减少状态集合.add(状态);
	}
	public void 添加暴击几率增加状态(状态类 状态)
	{
		暴击几率增加状态集合.add(状态);
	}
	public void 添加暴击几率减少状态(状态类 状态)
	{
		暴击几率减少状态集合.add(状态);
	}
	public void 添加法术吸血增加状态(状态类 状态)
	{
		法术吸血增加状态集合.add(状态);
	}
	public void 添加显隐状态(显隐状态类 状态, 并行命令类 并行命令)
	{
		显隐状态集合.add(状态);
		if (并行命令 == null)
		{
			命令组装器类.实例.添加命令(new 设置隐身命令类(is我方, 英雄所在位置, is对战中隐身()));
		}
		else
		{
			并行命令.添加命令(new 设置隐身命令类(is我方, 英雄所在位置, is对战中隐身()));
		}
	}
	public void 添加法术吸血减少状态(状态类 状态)
	{
		法术吸血减少状态集合.add(状态);
	}
	public void 添加韧性增加状态(状态类 状态)
	{
		韧性增加状态集合.add(状态);
	}
	public void 添加韧性减少状态(状态类 状态)
	{
		韧性减少状态集合.add(状态);
	}
	public void 添加伤害减免状态(状态类 状态)
	{
		伤害减免状态集合.add(状态);
	}
	public void 添加护盾状态(护盾状态类 状态)
	{
		护盾状态集合.add(状态);
	}
	public void 添加魔法护盾状态(护盾状态类 状态)
	{
		魔法护盾状态集合.add(状态);
	}
	public void 添加无敌状态(护盾状态类 状态)
	{
		无敌状态集合.add(状态);
	}
	public void 添加敌方阵容状态触发本英雄状态(敌方阵容状态触发本英雄状态类 状态)
	{
		敌方阵容状态触发本英雄状态集合.add(状态);
	}
	public void 添加物理伤害减免状态(状态类 状态)
	{
		物理伤害减免状态集合.add(状态);
	}
	public void 添加魔法伤害减免状态(状态类 状态)
	{
		魔法伤害减免状态集合.add(状态);
	}
	public void 添加先手值清零状态(先手值清零状态类 状态)
	{
		先手值清零状态集合.add(状态);
	}
	public void 添加被压制状态(被压制状态类 状态, 并行命令类 并行命令)
	{
		被压制状态集合.add(状态);
		计算被压制属性();
		if (并行命令 == null)
		{
			命令组装器类.实例.添加命令(new 设置压制命令类(is我方, 英雄所在位置, 被压制));
		}
		else
		{
			并行命令.添加命令(new 设置压制命令类(is我方, 英雄所在位置, 被压制));
		}
	}
	public void 添加敌方有英雄阵亡触发状态(状态类 状态)
	{
		敌方有英雄阵亡触发状态集合.add(状态);
	}
	public void 添加普通攻击触发效果状态(普通攻击触发效果状态类 普通攻击触发效果状态)
	{
		普通攻击触发效果状态集合.add(普通攻击触发效果状态);
	}
	public void 添加被普通攻击触发效果状态(被普通攻击触发效果状态类 被普通攻击触发效果状态)
	{
		被普通攻击触发效果状态集合.add(被普通攻击触发效果状态);
	}
	private void 移除状态(Set<? extends 状态类> 状态集合, Class<? extends 状态类> 状态类型)
	{
		Iterator<? extends 状态类> 迭代器 = 状态集合.iterator();
		while (迭代器.hasNext())
		{
			if (迭代器.next().getClass() == 状态类型)
			{
				迭代器.remove();
				break;
			}
		}
	}
	public void 移除攻击力增加状态(Class<? extends 状态类> 状态类型)
	{
		移除状态(攻击力增加状态集合, 状态类型);
	}
	public void 移除攻击力减少状态(Class<? extends 状态类> 状态类型)
	{
		移除状态(攻击力减少状态集合, 状态类型);
	}
	public void 移除法术强度增加状态(Class<? extends 状态类> 状态类型)
	{
		移除状态(法术强度增加状态集合, 状态类型);
	}
	public void 移除法术强度减少状态(Class<? extends 状态类> 状态类型)
	{
		移除状态(法术强度减少状态集合, 状态类型);
	}
	public void 移除先手值增加状态(Class<? extends 状态类> 状态类型)
	{
		移除状态(先手值增加状态集合, 状态类型);
	}
	public void 移除先手值减少免疫状态(Class<? extends 状态类> 状态类型)
	{
		移除状态(先手值减少状态集合, 状态类型);
	}
	public void 移除护甲值增加状态(Class<? extends 状态类> 状态类型)
	{
		移除状态(护甲值增加状态集合, 状态类型);
	}
	public void 移除护甲值减少状态(Class<? extends 状态类> 状态类型)
	{
		移除状态(护甲值减少状态集合, 状态类型);
	}
	public void 移除攻击速度增加状态(Class<? extends 状态类> 状态类型)
	{
		移除状态(攻击速度增加状态集合, 状态类型);
	}
	public void 移除伤害减免状态(Class<? extends 状态类> 状态类型)
	{
		移除状态(伤害减免状态集合, 状态类型);
	}
	public void 移除生命偷取增加状态(Class<? extends 状态类> 状态类型)
	{
		移除状态(生命偷取增加状态集合, 状态类型);
	}
	public void 移除显隐状态(Class<? extends 状态类> 状态类型, 并行命令类 并行命令)
	{
		移除状态(显隐状态集合, 状态类型);
		if (并行命令 == null)
		{
			命令组装器类.实例.添加命令(new 设置隐身命令类(is我方, 英雄所在位置, is对战中隐身()));
		}
		else
		{
			并行命令.添加命令(new 设置隐身命令类(is我方, 英雄所在位置, is对战中隐身()));
		}
	}
	public void 移除被压制状态(Class<? extends 状态类> 状态类型, 并行命令类 并行命令)
	{
		移除状态(被压制状态集合, 状态类型);
		if (并行命令 == null)
		{
			命令组装器类.实例.添加命令(new 设置压制命令类(is我方, 英雄所在位置, is对战中被压制()));
		}
		else
		{
			并行命令.添加命令(new 设置压制命令类(is我方, 英雄所在位置, is对战中被压制()));
		}
	}
	public void 获得攻击后排能力(int 最大持续回合数)
	{
		获得攻击后排能力状态集合.add(new 获得攻击后排能力状态类(最大持续回合数));
	}
	public void 被沉默(int 最大持续回合数, 并行命令类 并行命令)
	{
		被沉默状态集合.add(new 被沉默状态类(最大持续回合数));
		计算被沉默属性();
		if (并行命令 == null)
		{
			命令组装器类.实例.添加命令(new 设置沉默命令类(is我方, 英雄所在位置, 被沉默));
		}
		else
		{
			并行命令.添加命令(new 设置沉默命令类(is我方, 英雄所在位置, 被沉默));
		}
	}
	public void 被冰冻(int 最大持续回合数, 并行命令类 并行命令)
	{
		被冰冻状态集合.add(new 被冰冻状态类(最大持续回合数));
		计算被冰冻属性();
		if (并行命令 == null)
		{
			命令组装器类.实例.添加命令(new 设置冰冻命令类(is我方, 英雄所在位置, 被冰冻));
		}
		else
		{
			并行命令.添加命令(new 设置冰冻命令类(is我方, 英雄所在位置, 被冰冻));
		}
	}
	public void 被击飞(int 最大持续回合数, 并行命令类 并行命令)
	{
		被击飞状态集合.add(new 被击飞状态类(最大持续回合数));
		计算被击飞属性();
		if (并行命令 == null)
		{
			命令组装器类.实例.添加命令(new 设置击飞命令类(is我方, 英雄所在位置, 被击飞));
		}
		else
		{
			并行命令.添加命令(new 设置击飞命令类(is我方, 英雄所在位置, 被击飞));
		}
	}
	public void 被嘲讽(int 最大持续回合数, 并行命令类 并行命令)
	{
		被嘲讽状态集合.add(new 被嘲讽状态类(最大持续回合数));
		计算被嘲讽属性();
		if (并行命令 == null)
		{
			命令组装器类.实例.添加命令(new 设置嘲讽命令类(is我方, 英雄所在位置, 被嘲讽));
		}
		else
		{
			并行命令.添加命令(new 设置嘲讽命令类(is我方, 英雄所在位置, 被嘲讽));
		}
	}
	public void 被眩晕(int 最大持续回合数, 并行命令类 并行命令)
	{
		被眩晕状态集合.add(new 被眩晕状态类(最大持续回合数));
		计算被眩晕属性();
		if (并行命令 == null)
		{
			命令组装器类.实例.添加命令(new 设置眩晕命令类(is我方, 英雄所在位置, 被眩晕));
		}
		else
		{
			并行命令.添加命令(new 设置眩晕命令类(is我方, 英雄所在位置, 被眩晕));
		}
	}
	public void 被致盲(int 最大持续回合数, 并行命令类 并行命令)
	{
		被致盲状态集合.add(new 被致盲状态类(最大持续回合数));
		计算被致盲属性();
		if (并行命令 == null)
		{
			命令组装器类.实例.添加命令(new 设置致盲命令类(is我方, 英雄所在位置, 被致盲));
		}
		else
		{
			并行命令.添加命令(new 设置致盲命令类(is我方, 英雄所在位置, 被致盲));
		}
	}
	public void 隐身(int 最大持续回合数, 并行命令类 并行命令)
	{
		隐身状态集合.add(new 隐身状态类(最大持续回合数));
		计算隐身属性();
		if (并行命令 == null)
		{
			命令组装器类.实例.添加命令(new 设置隐身命令类(is我方, 英雄所在位置, is隐身));
		}
		else
		{
			并行命令.添加命令(new 设置隐身命令类(is我方, 英雄所在位置, is隐身));
		}
	}
	/**
	 * @param 减少的生命值 负数
	 * @param 并行命令 若为null，则直接添加到对战屏幕动作中 
	 * @return 实际减少的生命值 正数
	 */
	private int 减少生命值(对战中英雄类 攻击释放者, int 减少的生命值, 并行命令类 并行命令)
	{
		boolean is传入并行命令参数为null = 并行命令 == null;
		int 减少后生命值 = 生命值 + 减少的生命值;
		// 减少后生命值不能小于0
		if (减少后生命值  <= 0)
		{
			减少的生命值 = 生命值;
			减少后生命值 = 0;
		}
		int 实际减少的生命值  = 生命值 - 减少后生命值;
		生命值 = 减少后生命值;
		if (实际减少的生命值 > 0)
		{
			Gdx.app.debug("对战中英雄类.减少生命值",String.format("实际减少生命值%d\n",  实际减少的生命值));
			if (并行命令 == null)
			{
				并行命令 = new 并行命令类();
				if (is我方)
				{
					并行命令.添加命令(new 掉血命令类(对战屏幕类.我方X坐标数组[英雄所在位置-1], 对战屏幕类.我方Y坐标数组[英雄所在位置-1], 实际减少的生命值));
				}
				else
				{
					并行命令.添加命令(new 掉血命令类(对战屏幕类.敌方X坐标数组[英雄所在位置-1], 对战屏幕类.敌方Y坐标数组[英雄所在位置-1], 实际减少的生命值));
				}
				并行命令.添加命令(new 被攻击晃动命令类(is我方, 英雄所在位置));
				并行命令.添加命令(new 设置英雄血量命令类(is我方, 英雄所在位置, 生命值 * 1f / get对战中最大生命值()));
			}
			else
			{
				if (is我方)
				{
					并行命令.添加命令(new 掉血命令类(对战屏幕类.我方X坐标数组[英雄所在位置-1], 对战屏幕类.我方Y坐标数组[英雄所在位置-1], 实际减少的生命值));
				}
				else
				{
					并行命令.添加命令(new 掉血命令类(对战屏幕类.敌方X坐标数组[英雄所在位置-1], 对战屏幕类.敌方Y坐标数组[英雄所在位置-1], 实际减少的生命值));
				}
				并行命令.添加命令(new 被攻击晃动命令类(is我方, 英雄所在位置));
				并行命令.添加命令(new 设置英雄血量命令类(is我方, 英雄所在位置, 生命值 * 1f / get对战中最大生命值()));
			}
		}
		if (生命值 == 0)
		{
			阵亡 = true;
			所在阵容.被通知最近一次攻击造成本阵容有英雄阵亡(this);
			持续施法被打断();
			攻击释放者.击杀数++;
			Gdx.app.debug("对战中英雄类.减少生命值",String.format("%s击杀数加一，为:%d\n", 攻击释放者.get阵容名所在位置英雄名字(), 攻击释放者.击杀数));
			重置特殊状态动画效果(并行命令);
			并行命令.添加命令(new 英雄阵亡命令类(攻击释放者.击杀数, is我方, 英雄所在位置 - 1, 英雄.get阵亡音效文件名()));
			if (is传入并行命令参数为null)
			{
				命令组装器类.实例.添加命令(并行命令);
			}
			// 英雄阵亡后对战局没有影响可以释放内存
			释放内存();
		}
		return 实际减少的生命值;
	}
	/**
	 * @param 增加的生命值 正数
	 */
	private int 增加生命值(int 增加的生命值)
	{
		计算最大生命值();
		int 最大生命值 = 对战中英雄属性.get最大生命值();
		int 增加后生命值 = 生命值 + 增加的生命值;
		// 增减后生命值不能大于最大生命值
		if (增加后生命值 > 最大生命值)
		{
			增加后生命值 = 最大生命值;
		}
		int 实际增加生命值 = 增加后生命值 - 生命值;
		if (实际增加生命值 > 0)
		{
			Gdx.app.debug("对战中英雄类.增加生命值",String.format("实际增加生命值%d\n",  实际增加生命值));
			生命值 = 增加后生命值;
		}
		return 实际增加生命值;
	}
	/**
	 * @param 增加的生命值 正数
	 * @param 并行回血命令 需要显示为并行回血时传入值，否则传入null即可
	 */
	public int 增加生命值(int 增加的生命值, 并行命令类 并行回血命令)
	{
		int 实际增加生命值 = 增加生命值(增加的生命值);
		if (实际增加生命值 > 0)
		{
			if (并行回血命令 != null)
			{
				if (is我方)
				{
					并行回血命令.添加命令(new 回血命令类(对战屏幕类.我方X坐标数组[英雄所在位置-1], 对战屏幕类.我方Y坐标数组[英雄所在位置-1], 实际增加生命值));
				}
				else
				{
					并行回血命令.添加命令(new 回血命令类(对战屏幕类.敌方X坐标数组[英雄所在位置-1], 对战屏幕类.敌方Y坐标数组[英雄所在位置-1], 实际增加生命值));
				}
				并行回血命令.添加命令(new 设置英雄血量命令类(is我方, 英雄所在位置, 生命值 * 1f / get对战中最大生命值()));
			}
			else
			{
				并行回血命令 = new 并行命令类();
				if (is我方)
				{
					并行回血命令.添加命令(new 回血命令类(对战屏幕类.我方X坐标数组[英雄所在位置-1], 对战屏幕类.我方Y坐标数组[英雄所在位置-1], 实际增加生命值));
				}
				else
				{
					并行回血命令.添加命令(new 回血命令类(对战屏幕类.敌方X坐标数组[英雄所在位置-1], 对战屏幕类.敌方Y坐标数组[英雄所在位置-1], 实际增加生命值));
				}
				并行回血命令.添加命令(new 设置英雄血量命令类(is我方, 英雄所在位置, 生命值 * 1f / get对战中最大生命值()));
				命令组装器类.实例.添加命令(并行回血命令);
			}
		}
		return 实际增加生命值;
	}
	// 将不需要的引用置为null，提示JVM可以回收垃圾了
	private void 释放内存()
	{
		英雄属性变化 = null;
		对战中英雄属性 = null;
		英雄 = null;
		攻击力增加状态集合.clear();
		攻击力增加状态集合 = null;
		法术强度增加状态集合.clear();
		法术强度增加状态集合 = null;
		先手值增加状态集合.clear();
		先手值增加状态集合 = null;
		先手值减少免疫状态集合.clear();
		先手值减少免疫状态集合 = null;
		护甲值增加状态集合.clear();
		护甲值增加状态集合 = null;
		攻击速度增加状态集合.clear();
		攻击速度增加状态集合 = null;
		魔法抗性增加状态集合.clear();
		魔法抗性增加状态集合 = null;
		最大生命值增加状态集合.clear();
		最大生命值增加状态集合 = null;
		生命值增加状态集合.clear();
		生命值增加状态集合 = null;
		生命回复增加状态集合.clear();
		生命回复增加状态集合 = null;
		护甲穿透数值增加状态集合.clear();
		护甲穿透数值增加状态集合 = null;
		护甲穿透百分比增加状态集合.clear();
		护甲穿透百分比增加状态集合 = null;
		法术穿透数值增加状态集合.clear();
		法术穿透数值增加状态集合 = null;
		法术穿透百分比增加状态集合.clear();
		法术穿透百分比增加状态集合 = null;
		生命偷取增加状态集合.clear();
		生命偷取增加状态集合 = null;
		暴击几率增加状态集合.clear();
		暴击几率增加状态集合 = null;
		法术吸血增加状态集合.clear();
		法术吸血增加状态集合 = null;
		获得攻击后排能力状态集合.clear();
		获得攻击后排能力状态集合 = null;
		隐身状态集合.clear();
		隐身状态集合 = null;
		显隐状态集合.clear();
		显隐状态集合 = null;
		韧性增加状态集合.clear();
		韧性增加状态集合 = null;
		攻击力减少状态集合.clear();
		攻击力减少状态集合 = null;
		法术强度减少状态集合.clear();
		法术强度减少状态集合 = null;
		先手值减少状态集合.clear();
		先手值减少状态集合 = null;
		护甲值减少状态集合.clear();
		护甲值减少状态集合 = null;
		被致盲状态集合.clear();
		被致盲状态集合 = null;
		被集火状态集合.clear();
		被集火状态集合 = null;
		攻击速度减少状态集合.clear();
		攻击速度减少状态集合 = null;
		魔法抗性减少状态集合.clear();
		魔法抗性减少状态集合 = null;
		最大生命值减少状态集合.clear();
		最大生命值减少状态集合 = null;
		生命值减少状态集合.clear();
		生命值减少状态集合 = null;
		生命回复减少状态集合.clear();
		生命回复减少状态集合 = null;
		护甲穿透数值减少状态集合.clear();
		护甲穿透数值减少状态集合 = null;
		法术穿透数值减少状态集合.clear();
		法术穿透数值减少状态集合 = null;
		护甲穿透百分比减少状态集合.clear();
		护甲穿透百分比减少状态集合 = null;
		法术穿透百分比减少状态集合.clear();
		法术穿透百分比减少状态集合 = null;
		生命偷取减少状态集合.clear();
		生命偷取减少状态集合 = null;
		暴击几率减少状态集合.clear();
		暴击几率减少状态集合 = null;
		法术吸血减少状态集合.clear();
		法术吸血减少状态集合 = null;
		韧性减少状态集合.clear();
		韧性减少状态集合 = null;
		伤害减免状态集合.clear();
		伤害减免状态集合 = null;
		物理伤害减免状态集合.clear();
		物理伤害减免状态集合 = null;
		魔法伤害减免状态集合.clear();
		魔法伤害减免状态集合 = null;
		先手值清零状态集合.clear();
		先手值清零状态集合 = null;
		被沉默状态集合.clear();
		被沉默状态集合 = null;
		被冰冻状态集合.clear();
		被冰冻状态集合 = null;
		被击飞状态集合.clear();
		被击飞状态集合 = null;
		被嘲讽状态集合.clear();
		被嘲讽状态集合 = null;
		被眩晕状态集合.clear();
		被眩晕状态集合 = null;
		敌方有英雄阵亡触发状态集合.clear();
		敌方有英雄阵亡触发状态集合 = null;
		普通攻击触发效果状态集合.clear();
		普通攻击触发效果状态集合 = null;
		被普通攻击触发效果状态集合.clear();
		被普通攻击触发效果状态集合 = null;
		护盾状态集合.clear();
		护盾状态集合 = null;
		魔法护盾状态集合.clear();
		魔法护盾状态集合 = null;
		无敌状态集合.clear();
		无敌状态集合 = null;
		敌方阵容状态触发本英雄状态集合.clear();
		敌方阵容状态触发本英雄状态集合 = null;
		主动持续施法状态 = null;
		被动持续释放状态集合.clear();
		被动持续释放状态集合 = null;
		所在阵容 = null;
	}
	private void 计算最大生命值()
	{
		计算最大生命值属性变化();
		int 对战中最大生命值 = 计算类.根据_初始值_数值变化_百分比变化_计算值(get初始最大生命值(), 英雄属性变化.get最大生命值数值变化(), 英雄属性变化.get最大生命值百分比变化());
		对战中英雄属性.set最大生命值(对战中最大生命值);
	}
	private void 计算法术强度()
	{
		计算法术强度属性变化();
		int 对战中法术强度 = 计算类.根据_初始值_数值变化_百分比变化_计算值(get初始法术强度(), 英雄属性变化.get法术强度数值变化(), 英雄属性变化.get法术强度百分比变化());
		对战中英雄属性.set法术强度(对战中法术强度);
	}
	private void 计算魔法抗性()
	{
		计算魔法抗性属性变化();
		int 对战中魔法抗性 = 计算类.根据_初始值_数值变化_百分比变化_计算值(get初始魔法抗性(), 英雄属性变化.get魔法抗性数值变化(), 英雄属性变化.get魔法抗性百分比变化());
		对战中英雄属性.set魔法抗性(对战中魔法抗性);
	}
	private void 计算数值法术穿透()
	{
		计算数值法术穿透属性变化();
		int 对战中数值法术穿透 = 计算类.根据_数值变化_计算值(get初始数值法术穿透(), 英雄属性变化.get数值法术穿透变化());
		对战中英雄属性.set数值法术穿透(对战中数值法术穿透);
	}
	private void 计算百分比法术穿透()
	{
		计算百分比法术穿透属性变化();
		float 对战中百分比法术穿透 = 计算类.初始百分比_百分比变化_相加(get初始百分比法术穿透(), 英雄属性变化.get百分比法术穿透变化());
		对战中英雄属性.set百分比法术穿透(对战中百分比法术穿透);
	}
	private void 计算生命偷取()
	{
		计算生命偷取属性变化();
		float 对战中生命偷取 = 计算类.初始百分比_百分比变化_相加(get初始生命偷取(), 英雄属性变化.get生命偷取百分比变化());
		对战中英雄属性.set生命偷取(对战中生命偷取);
	}
	private void 计算暴击几率()
	{
		计算暴击几率属性变化();
		float 对战中暴击几率 = 计算类.初始百分比_百分比变化_相加(get初始暴击几率(), 英雄属性变化.get暴击几率百分比变化());
		对战中英雄属性.set暴击几率(对战中暴击几率);
	}
	private void 计算法术吸血()
	{
		计算法术吸血属性变化();
		float 对战中法术吸血 = 计算类.初始百分比_百分比变化_相加(get初始法术吸血(), 英雄属性变化.get法术吸血百分比变化());
		对战中英雄属性.set法术吸血(对战中法术吸血);
	}
	private void 计算韧性()
	{
		计算韧性属性变化();
		float 对战中韧性 = 计算类.初始百分比_百分比变化_相加(get初始韧性(), 英雄属性变化.get韧性百分比变化());
		对战中英雄属性.set韧性(对战中韧性);
	}
	private void 计算数值护甲穿透()
	{
		计算数值护甲穿透属性变化();
		int 对战中数值护甲穿透 = 计算类.根据_数值变化_计算值(get初始数值护甲穿透(), 英雄属性变化.get数值护甲穿透变化());
		对战中英雄属性.set数值护甲穿透(对战中数值护甲穿透);
	}
	private void 计算百分比护甲穿透()
	{
		计算百分比护甲穿透属性变化();
		float 对战中百分比护甲穿透 = 计算类.初始百分比_百分比变化_相加(get初始百分比护甲穿透(), 英雄属性变化.get百分比护甲穿透变化());
		对战中英雄属性.set百分比护甲穿透(对战中百分比护甲穿透);
	}
	private void 计算护甲值()
	{
		计算护甲属性变化();
		int 对战中护甲 = 计算类.根据_初始值_数值变化_百分比变化_计算值(get初始护甲值(), 英雄属性变化.get护甲数值变化(), 英雄属性变化.get护甲百分比变化());
		对战中英雄属性.set护甲值(对战中护甲);
	}
	private void 计算攻击力()
	{
		计算攻击力属性变化();
		int 对战中攻击力 = 计算类.根据_初始值_数值变化_百分比变化_计算值(get初始攻击力(), 英雄属性变化.get攻击力数值变化(), 英雄属性变化.get攻击力百分比变化());
		对战中英雄属性.set攻击力(对战中攻击力);
	}
	private void 计算攻击速度()
	{
		计算攻击速度属性变化();
		float 对战中攻击速度 = 计算类.初始百分比_百分比变化_相乘(get初始攻击速度(), 英雄属性变化.get攻击速度百分比变化());
		对战中英雄属性.set攻击速度(对战中攻击速度);
	}
	private void 计算先手值()
	{
		if (!is对战中先手值清零())
		{
			计算先手值属性变化();
			int 对战中先手值 = 计算类.根据_初始值_数值变化_百分比变化_计算值(get初始先手值(), 英雄属性变化.get先手值数值变化(), 英雄属性变化.get先手值百分比变化());
			对战中英雄属性.set先手值(对战中先手值);
		}
		else
		{
			对战中英雄属性.set先手值(0);
		}
	}
	private void 计算生命回复()
	{
		计算生命回复属性变化();
		对战中英雄属性.set生命回复(计算类.根据_初始值_数值变化_百分比变化_计算值(get初始生命回复(), 英雄属性变化.get生命回复数值变化(), 英雄属性变化.get生命回复百分比变化()));
	}
	private void 计算攻击力属性变化()
	{
		英雄属性变化.重置攻击力数值变化();
		英雄属性变化.重置攻击力百分比变化();
		for (状态类 状态 : 攻击力增加状态集合)
		{
			状态.作用(this);
		}
		for (状态类 状态 : 攻击力减少状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算法术强度属性变化()
	{
		英雄属性变化.重置法术强度数值变化();
		英雄属性变化.重置法术强度百分比变化();
		for (状态类 状态 : 法术强度增加状态集合)
		{
			状态.作用(this);
		}
		for (状态类 状态 : 法术强度减少状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算先手值属性变化()
	{
		英雄属性变化.重置先手值数值变化();
		英雄属性变化.重置先手值百分比变化();
		for (状态类 状态 : 先手值增加状态集合)
		{
			状态.作用(this);
		}
		计算先手值减少免疫属性();
		if (!is先手值减少免疫)
		{
			for (状态类 状态 : 先手值减少状态集合)
			{
				状态.作用(this);
			}
		}
	}
	private void 计算护甲属性变化()
	{
		英雄属性变化.重置护甲值数值变化();
		英雄属性变化.重置护甲值百分比变化();
		for (状态类 状态 : 护甲值增加状态集合)
		{
			状态.作用(this);
		}
		for (状态类 状态 : 护甲值减少状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算被致盲属性()
	{
		被致盲 = false;
		for (状态类 状态 : 被致盲状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算攻击速度属性变化()
	{
		英雄属性变化.重置攻击速度变化();
		for (状态类 状态 : 攻击速度增加状态集合)
		{
			状态.作用(this);
		}
		for (状态类 状态 : 攻击速度减少状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算魔法抗性属性变化()
	{
		英雄属性变化.重置魔法抗性数值变化();
		英雄属性变化.重置魔法抗性百分比变化();
		for (状态类 状态 : 魔法抗性增加状态集合)
		{
			状态.作用(this);
		}
		for (状态类 状态 : 魔法抗性减少状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算最大生命值属性变化()
	{
		英雄属性变化.重置最大生命值数值变化();
		英雄属性变化.重置最大生命值百分比变化();
		for (状态类 状态 : 最大生命值增加状态集合)
		{
			状态.作用(this);
		}
		for (状态类 状态 : 最大生命值减少状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算生命回复属性变化()
	{
		英雄属性变化.重置生命回复数值变化();
		英雄属性变化.重置生命回复百分比变化();
		for (状态类 状态 : 生命回复增加状态集合)
		{
			状态.作用(this);
		}
		for (状态类 状态 : 生命回复减少状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算数值护甲穿透属性变化()
	{
		英雄属性变化.重置护甲穿透数值变化();
		for (状态类 状态 : 护甲穿透数值增加状态集合)
		{
			状态.作用(this);
		}
		for (状态类 状态 : 护甲穿透数值减少状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算百分比护甲穿透属性变化()
	{
		英雄属性变化.重置护甲穿透百分比变化();
		for (状态类 状态 : 护甲穿透百分比增加状态集合)
		{
			状态.作用(this);
		}
		for (状态类 状态 : 护甲穿透百分比减少状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算数值法术穿透属性变化()
	{
		英雄属性变化.重置法术穿透数值变化();
		for (状态类 状态 : 法术穿透数值增加状态集合)
		{
			状态.作用(this);
		}
		for (状态类 状态 : 法术穿透数值减少状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算百分比法术穿透属性变化()
	{
		英雄属性变化.重置法术穿透百分比变化();
		for (状态类 状态 : 法术穿透百分比增加状态集合)
		{
			状态.作用(this);
		}
		for (状态类 状态 : 法术穿透百分比减少状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算获得攻击后排能力()
	{
		is获得攻击后排能力 = false;
		for (状态类 状态 : 获得攻击后排能力状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算生命偷取属性变化()
	{
		英雄属性变化.重置生命偷取变化();
		for (状态类 状态 : 生命偷取增加状态集合)
		{
			状态.作用(this);
		}
		for (状态类 状态 : 生命偷取减少状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算暴击几率属性变化()
	{
		英雄属性变化.重置暴击几率变化();
		for (状态类 状态 : 暴击几率增加状态集合)
		{
			状态.作用(this);
		}
		for (状态类 状态 : 暴击几率减少状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算法术吸血属性变化()
	{
		英雄属性变化.重置法术吸血变化();
		for (状态类 状态 : 法术吸血增加状态集合)
		{
			状态.作用(this);
		}
		for (状态类 状态 : 法术吸血减少状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算显隐属性()
	{
		is显隐 = false;
		for (状态类 状态 : 显隐状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算被集火编号()
	{
		对战中被集火编号 = 被集火状态类.最大未被集火编号;
		for (状态类 状态 : 被集火状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算隐身属性()
	{
		is隐身 = false;
		for (状态类 状态 : 隐身状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算韧性属性变化()
	{
		英雄属性变化.重置韧性变化();
		for (状态类 状态 : 韧性增加状态集合)
		{
			状态.作用(this);
		}
		for (状态类 状态 : 韧性减少状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算伤害减免属性()
	{
		伤害数值减免 = 0;
		伤害百分比减免 = 0;
		for (状态类 状态 : 伤害减免状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算物理伤害减免属性()
	{
		物理伤害数值减免 = 0;
		物理伤害百分比减免 = 0;
		for (状态类 状态 : 物理伤害减免状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算魔法伤害减免属性()
	{
		魔法伤害数值减免 = 0;
		魔法伤害百分比减免 = 0;
		for (状态类 状态 : 魔法伤害减免状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算先手值清零属性()
	{
		is先手值清零 = false;
		for (状态类 状态 : 先手值清零状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算先手值减少免疫属性()
	{
		is先手值减少免疫 = false;
		for (状态类 状态 : 先手值减少免疫状态集合)
		{
			状态.作用(this);
		}
	}
	private void 计算被沉默属性()
	{
		被沉默 = false;
		for (状态类 状态 : 被沉默状态集合)
		{
			状态.作用(this);
		}
		if (被沉默)
		{
			持续施法被打断();
		}
	}
	private void 计算被冰冻属性()
	{
		被冰冻 = false;
		for (状态类 状态 : 被冰冻状态集合)
		{
			状态.作用(this);
		}
		if (被冰冻)
		{
			持续施法被打断();
		}
	}
	private void 计算被击飞属性()
	{
		被击飞 = false;
		for (状态类 状态 : 被击飞状态集合)
		{
			状态.作用(this);
		}
		if (被击飞)
		{
			持续施法被打断();
		}
	}
	private void 计算被嘲讽属性()
	{
		被嘲讽 = false;
		for (状态类 状态 : 被嘲讽状态集合)
		{
			状态.作用(this);
		}
		if (被嘲讽)
		{
			持续施法被打断();
		}
	}
	private void 计算被眩晕属性()
	{
		被眩晕 = false;
		for (状态类 状态 : 被眩晕状态集合)
		{
			状态.作用(this);
		}
		if (被眩晕)
		{
			持续施法被打断();
		}
	}
	private void 计算被压制属性()
	{
		被压制 = false;
		for (状态类 状态 : 被压制状态集合)
		{
			状态.作用(this);
		}
		if (被压制)
		{
			持续施法被打断();
		}
	}
	private void 持续施法被打断()
	{
		if (主动持续施法状态 != null)
		{
			主动持续施法状态.主动持续施法被打断(this);
			主动持续施法状态 = null;
		}
	}
}