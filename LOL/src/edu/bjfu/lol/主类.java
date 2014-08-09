package edu.bjfu.lol;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import edu.bjfu.lol.screen.加载屏幕类;
import edu.bjfu.lol.utils.常量类;
import edu.bjfu.lol.utils.音乐播放完自动销毁类;

public class 主类 extends Game
{
	private static final String UI目录 = 常量类.图片目录 + "UI/";
	private static final String Hero目录 = 常量类.图片目录 + "Hero/";
	// 程序开始主类只有一个实例
	public static 主类 实例;
	// 用于切换屏幕的栈
	private Stack<Screen> 屏幕栈 = new Stack<Screen>();
	private Music 背景音乐;
	private AssetManager 资源管理器 = new AssetManager();
	private Skin UI皮肤资源;
	private Map<String, Skin> 英雄皮肤资源映射 = new HashMap<String, Skin>();
	private boolean is初始化完成;
	private Sound 按钮声音;
	@Override
	public void create()
	{
		// debug
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		实例 = this;
		// 进入主屏幕
		Music 欢迎音效 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "Welcome.mp3"));
		欢迎音效.setOnCompletionListener(new 音乐播放完自动销毁类());
		欢迎音效.play();
		// 背景音乐
		背景音乐 = Gdx.audio.newMusic(Gdx.files.internal(常量类.声音目录 + "Background.mp3"));
		// 设置循环
		背景音乐.setLooping(true);
		// 音量
		背景音乐.setVolume(0.4f);
		// 按钮声音
		按钮声音 = Gdx.audio.newSound(Gdx.files.internal(常量类.声音目录 + "ButtonClick.mp3"));
		准备初始化游戏资源();
		// 进入加载屏幕
		setScreen(new 加载屏幕类());
	}
	public void 进入屏幕(Screen 屏幕)
	{
		屏幕栈.push(屏幕);
		this.setScreen(屏幕);
	}
	public void 退出屏幕()
	{
		// 释放资源
		屏幕栈.pop().dispose();
		this.setScreen(屏幕栈.peek());
	}
	public void 回到主屏幕()
	{
		this.setScreen(屏幕栈.firstElement());
		while (屏幕栈.size() > 1)
		{
			屏幕栈.pop().dispose();
		}
	}
	public void 播放按钮声音()
	{
		按钮声音.play();
	}
	public void 播放背景音乐()
	{
		if (!背景音乐.isPlaying())
		{
			背景音乐.play();
		}
	}
	public void 停止播放背景音乐()
	{
		if (背景音乐.isPlaying())
		{
			背景音乐.stop();
		}
	}
	public float get初始化进度()
	{
		float 资源管理器进度 = 资源管理器.getProgress();
		if (资源管理器进度 >= 1)
		{
			if (is初始化完成)
			{
				return 1;
			}
			else
			{
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						英雄皮肤资源映射.put("Ahri", new Skin(资源管理器.get(Hero目录 + "Ahri.txt", TextureAtlas.class)));
						英雄皮肤资源映射.put("Ashe", new Skin(资源管理器.get(Hero目录 + "Ashe.txt", TextureAtlas.class)));
						英雄皮肤资源映射.put("Caitlyn", new Skin(资源管理器.get(Hero目录 + "Caitlyn.txt", TextureAtlas.class)));
						英雄皮肤资源映射.put("Garen", new Skin(资源管理器.get(Hero目录 + "Garen.txt", TextureAtlas.class)));
						英雄皮肤资源映射.put("Jinx", new Skin(资源管理器.get(Hero目录 + "Jinx.txt", TextureAtlas.class)));
						英雄皮肤资源映射.put("Kayle", new Skin(资源管理器.get(Hero目录 + "Kayle.txt", TextureAtlas.class)));
						英雄皮肤资源映射.put("MasterYi", new Skin(资源管理器.get(Hero目录 + "MasterYi.txt", TextureAtlas.class)));
						英雄皮肤资源映射.put("Rammus", new Skin(资源管理器.get(Hero目录 + "Rammus.txt", TextureAtlas.class)));
						英雄皮肤资源映射.put("Riven", new Skin(资源管理器.get(Hero目录 + "Riven.txt", TextureAtlas.class)));
						英雄皮肤资源映射.put("Shen", new Skin(资源管理器.get(Hero目录 + "Shen.txt", TextureAtlas.class)));
						英雄皮肤资源映射.put("Soraka", new Skin(资源管理器.get(Hero目录 + "Soraka.txt", TextureAtlas.class)));
						英雄皮肤资源映射.put("Teemo", new Skin(资源管理器.get(Hero目录 + "Teemo.txt", TextureAtlas.class)));
						英雄皮肤资源映射.put("TwistedFate", new Skin(资源管理器.get(Hero目录 + "TwistedFate.txt", TextureAtlas.class)));
						英雄皮肤资源映射.put("Veigar", new Skin(资源管理器.get(Hero目录 + "Veigar.txt", TextureAtlas.class)));
						英雄皮肤资源映射.put("Warwick", new Skin(资源管理器.get(Hero目录 + "Warwick.txt", TextureAtlas.class)));
						is初始化完成 = true;
					}
				}).start();
				return 0.99f;
			}
		}
		else
		{
			return 资源管理器进度;
		}
	}
	private void 准备初始化游戏资源()
	{
		资源管理器.load(UI目录 + "Font.fnt", BitmapFont.class);
		资源管理器.load(Hero目录 + "Ahri.txt", TextureAtlas.class);
		资源管理器.load(Hero目录 + "Ashe.txt", TextureAtlas.class);
		资源管理器.load(Hero目录 + "Caitlyn.txt", TextureAtlas.class);
		资源管理器.load(Hero目录 + "Garen.txt", TextureAtlas.class);
		资源管理器.load(Hero目录 + "Jinx.txt", TextureAtlas.class);
		资源管理器.load(Hero目录 + "Kayle.txt", TextureAtlas.class);
		资源管理器.load(Hero目录 + "MasterYi.txt", TextureAtlas.class);
		资源管理器.load(Hero目录 + "Rammus.txt", TextureAtlas.class);
		资源管理器.load(Hero目录 + "Riven.txt", TextureAtlas.class);
		资源管理器.load(Hero目录 + "Shen.txt", TextureAtlas.class);
		资源管理器.load(Hero目录 + "Soraka.txt", TextureAtlas.class);
		资源管理器.load(Hero目录 + "Teemo.txt", TextureAtlas.class);
		资源管理器.load(Hero目录 + "TwistedFate.txt", TextureAtlas.class);
		资源管理器.load(Hero目录 + "Veigar.txt", TextureAtlas.class);
		资源管理器.load(Hero目录 + "Warwick.txt", TextureAtlas.class);
		资源管理器.load(UI目录 + "Pack.txt", TextureAtlas.class);
	}
	public void 初始化游戏资源()
	{
		资源管理器.update();
	}
	public Skin getUI皮肤资源()
	{
		if (UI皮肤资源 == null)
		{
			UI皮肤资源 = new Skin(Gdx.files.internal(UI目录 + "Skin.json"), 资源管理器.get(UI目录 + "Pack.txt", TextureAtlas.class));
		}
		return UI皮肤资源;
	}
	public Skin get英雄皮肤资源(String 英雄皮肤文件名)
	{
		return 英雄皮肤资源映射.get(英雄皮肤文件名);
	}
}
