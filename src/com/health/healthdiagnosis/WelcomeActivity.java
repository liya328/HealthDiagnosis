package com.health.healthdiagnosis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class WelcomeActivity extends Activity {

	private final String TAG = "WelcomeActivity";
	boolean isFirstIn = true;
	boolean isClick = false;
	
	private static final int GO_HOME = 10000;
	private static final int GO_GUIDE = 10001;
	
	private static final long DELAY_MILLIS = 3000;
	private static final String SHARE_PREFERENCE_NAME = "first_pref";
	SharedPreferences mPreferences;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what)
			{
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	private OnClickListener mClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			isClick = true;
			
			SharedPreferences preferences = getSharedPreferences(SHARE_PREFERENCE_NAME, MODE_PRIVATE);
			isFirstIn = preferences.getBoolean("isFirstIn", true);
			
			Log.i(TAG, "onClick,isFirstIn is " + isFirstIn);
			if(!isFirstIn)
			{
				if(mHandler.hasMessages(GO_HOME))
				{
					mHandler.removeMessages(GO_HOME);
				}
				mHandler.sendEmptyMessage(GO_HOME);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_view_main);
		
		intPreferenc();
		
		getWindow().getDecorView().findViewById(android.R.id.content).setOnClickListener(mClickListener);
		init();
	}

	private void intPreferenc() {
		// TODO Auto-generated method stub
		mPreferences = getSharedPreferences(SHARE_PREFERENCE_NAME, MODE_PRIVATE);
	}

	private void init() {
		// TODO Auto-generated method stub
		isFirstIn = mPreferences.getBoolean("isFirstIn", true);
		
		Log.i(TAG, "init,isFirstIn is " + isFirstIn + " and isClick is " + isClick);
		if(isFirstIn)
		{
			if(mHandler.hasMessages(GO_HOME))
			{
				mHandler.removeMessages(GO_HOME);
			}
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, DELAY_MILLIS);
		}
		else if(!isFirstIn && !isClick)
		{
			mHandler.sendEmptyMessageDelayed(GO_HOME, DELAY_MILLIS);
		}
	}

	protected void goHome() {
		// TODO Auto-generated method stub
//		Intent intent = new Intent(WelcomeActivity.this,HealthDiagnosisActivity.class);
//		Intent intent = new Intent(WelcomeActivity.this,HealthDiagnosisTabHostActivity.class);
		Intent intent = new Intent(WelcomeActivity.this,HealthDiagnosisFragmentActivity.class);
		startActivity(intent);
		finish();
	}

	protected void goGuide() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(WelcomeActivity.this,GuideViewActivity.class);
		startActivity(intent);
		finish();
		
		mPreferences.edit().putBoolean("isFirstIn", false).commit();
		isFirstIn = false;
	}
	
}
