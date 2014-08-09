package edu.bjfu.lol.逻辑;

import java.util.Random;

public class 计算类 {
	
	// 随机产生器
	private static final Random RANDOM = new Random();
	public static boolean 随机布尔值()
	{
		return RANDOM.nextBoolean();
	}
	/**
	 * 返回[0,上限)上的随机整数值
	 */
	public static int 随机整数值(int 上限)
	{
		return RANDOM.nextInt(上限);
	}
	/**
	 * 最高值应该大于最低值
	 * @param 最低值 正数
	 * @param 最高值 正数
	 * @param 百分比 正数
	 * @return
	 */
	public static int 根据_最低值_最高值_百分比_计算值(int 最低值, int 最高值, float 百分比)
	{
		int 相差值 = 最高值 - 最低值;
		return (int) (最低值 + 相差值 * 百分比);
	}
	public static boolean 根据_概率_计算是否发生(float 概率)
	{
		int x = RANDOM.nextInt(100);
		return x < 100 * 概率;
	}
	/**
	 * @param 初始值 正数
	 * @param 数值变化
	 * @param 百分比变化
	 * @return 正数
	 */
	public static int 根据_初始值_数值变化_百分比变化_计算值(int 初始值, int 数值变化, float 百分比变化)
	{
		初始值 = 根据_百分比变化_计算值(初始值, 百分比变化);
		初始值 = 根据_数值变化_计算值(初始值, 数值变化);
		return 初始值;
	}
	/**
	 * 根据护甲或抗性计算对应减伤
	 * @return 负数
	 */
	public static float 根据_数值_计算百分比减免(int 护甲或抗性)
	{
		return -1.0f * 护甲或抗性 / (100 + 护甲或抗性);
	}
	/**
	 * @param 数值 正数
	 * @param 百分比变化
	 * @return 正数
	 */
	public static int 根据_百分比变化_计算值(int 数值, float 百分比变化)
	{
		数值 *= 1 + 百分比变化;
		return 数值 >= 0 ? 数值 : 0;
	}
	/**
	 * @param 初始百分比 正数
	 * @param 百分比变化
	 * @return 正数
	 */
	public static float 初始百分比_百分比变化_相加(float 初始百分比, float 百分比变化)
	{
		初始百分比 += 百分比变化;
		return 初始百分比 >= 0 ? 初始百分比 : 0;
	}
	/**
	 * @param 初始百分比 正数
	 * @param 百分比变化
	 * @return 正数
	 */
	public static float 初始百分比_百分比变化_相乘(float 初始百分比, float 百分比变化)
	{
		初始百分比 *= 1 + 百分比变化;
		return 初始百分比 >= 0 ? 初始百分比 : 0;
	}
	public static int 根据_数值变化_计算值(int 初始值, int 数值变化)
	{
		初始值 += 数值变化;
		return 初始值 >= 0 ? 初始值 : 0;
	}
	public static int 根据_攻击速度_计算攻击总次数(float 攻击速度)
	{
		return 攻击速度 < 1 ? 1 : (int)攻击速度;
	}
}
