package edu.bjfu.lol.scenes.scene2d.动作;


public class 动作类
{
	/**
	 * 返回动作是否已完成,且完成后会自动完成结束清理，所以必须保证只调用一次
	 */
	public boolean is完成(){return true;}
	/**
	 * 只能被调用一次使动作开始
	 */
	public void 开始(){}
}
