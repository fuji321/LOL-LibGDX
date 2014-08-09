package edu.bjfu.lol.utils;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;

public class 音乐播放完自动销毁类 implements OnCompletionListener
{
	@Override
	public void onCompletion(Music music)
	{
		// dispose之前必须先stop，否则会抛出异常
		music.stop();
		music.dispose();
	}
}