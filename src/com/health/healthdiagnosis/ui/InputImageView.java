package com.health.healthdiagnosis.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public class InputImageView extends View {//

	private final String TAG = "InputImageView";
	
	public InputImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public InputImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public InputImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		// TODO Auto-generated method stub
		outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI;
		outAttrs.inputType = EditorInfo.TYPE_CLASS_TEXT;
		return new HealthInputConnection(this,false);
	}
	
	class HealthInputConnection extends BaseInputConnection{

		public HealthInputConnection(View targetView, boolean fullEditor) {
			super(targetView, fullEditor);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean commitText(CharSequence text, int newCursorPosition) {
			// TODO Auto-generated method stub
			Log.i(TAG, "soft keyboard input text = "  + text);
			return true;
		}

		@Override
		public boolean sendKeyEvent(KeyEvent event) {
			// TODO Auto-generated method stub
			Log.i(TAG, "sendKeyEvent, " + event.getKeyCode());
			return true;
		}
		
	}

}
