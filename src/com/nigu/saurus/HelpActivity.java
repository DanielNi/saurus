package com.nigu.saurus;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_help);
		
		CircleView active = (CircleView) findViewById(R.id.active_example);
		active.changeToActive();
		
		CircleView blank = (CircleView) findViewById(R.id.blank_example);
		blank.changeToFakeExample();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}
}
