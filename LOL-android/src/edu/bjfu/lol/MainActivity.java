package edu.bjfu.lol;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
        initialize(new 主类(), cfg);
    }
	@Override
	public void onBackPressed()
	{
		new AlertDialog.Builder(this).setTitle("确认退出吗？")  
	    .setNegativeButton("返回", new DialogInterface.OnClickListener() {  
	        @Override  
	        public void onClick(DialogInterface dialog, int which) {  
	        }
	    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {  
	        @Override  
	        public void onClick(DialogInterface dialog, int which) {  
	        	Gdx.app.exit();
	        }  
	    }).show();  
	}
}