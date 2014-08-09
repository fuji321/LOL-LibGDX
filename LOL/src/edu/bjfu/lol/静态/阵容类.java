package edu.bjfu.lol.静态;

public class 阵容类 {
	private 英雄类[] 英雄数组;
	public 英雄类[] get英雄数组() {
		return 英雄数组;
	}
	public void set英雄数组(英雄类[] 英雄数组) {
		this.英雄数组 = 英雄数组;
	}
	public 阵容类(英雄类[] 英雄数组)
	{
		this.英雄数组 = 英雄数组;
	}
	public 阵容类()
	{}
	public boolean is包含该英雄(英雄类 英雄)
	{
		if (英雄 == null)
		{
			return false;
		}
		else
		{
			for (int i = 0; i < 英雄数组.length; i++)
			{
				if (英雄数组[i] == 英雄)
				{
					return true;
				}
			}
			return false;
		}
	}
}
