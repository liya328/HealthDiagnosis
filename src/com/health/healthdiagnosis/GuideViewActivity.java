package com.health.healthdiagnosis;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnGenericMotionListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideViewActivity extends Activity {

	private final String TAG = "GuideViewActivity";
	private ViewPager mViewPager;
	private GestureDetector mGestureDetector;
	private int mFlaggingWidth = 0;
	private ArrayList<View> mPageViewArray;
	private ViewGroup mMainLayout;//container for page view
	private ViewGroup mViewPagerIndex;// for little circle icon
	private LinearLayout mTabLayout;
	private boolean isLeftBorder = false;
	private boolean isRightBorder = false;
	private int mValueInPageScrolledPrevious = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LayoutInflater inflater = getLayoutInflater();
		mPageViewArray = new ArrayList<View>();
		mPageViewArray.add(inflater.inflate(R.layout.guide_view_item_one, null));
		mPageViewArray.add(inflater.inflate(R.layout.guide_view_item_two, null));
		mPageViewArray.add(inflater.inflate(R.layout.guide_view_item_three, null));
		
		mMainLayout = (ViewGroup) inflater.inflate(R.layout.guide_view_main, null);
		mViewPager = (ViewPager) mMainLayout.findViewById(R.id.guide_view_main_pager);
		mTabLayout = (LinearLayout) mMainLayout.findViewById(R.id.guide_dots_layout);
		ImageView imageView = (ImageView) mTabLayout.getChildAt(0);
		imageView.setImageResource(R.drawable.guide_dots_selected);
		
		setContentView(mMainLayout);
		
		mGestureDetector = new GestureDetector(new GuideViewGestureListener());
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mFlaggingWidth = dm.widthPixels / 3;
		
		mViewPager.setAdapter(new GuideViewPagerAdapter());
		mViewPager.setOnPageChangeListener(new GuideViewPagerChangeListener());
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if(mGestureDetector.onTouchEvent(ev))
		{
			
		}
		return super.dispatchTouchEvent(ev);
	}



	class GuideViewPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPageViewArray.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
			// TODO Auto-generated method stub
			super.restoreState(state, loader);
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return super.saveState();
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			Log.i(TAG, "GuideViewPagerAdapter,destroyItem " + position);
			((ViewPager)container).removeView(mPageViewArray.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			Log.i(TAG, "GuideViewPagerAdapter,instantiateItem " + position);
			((ViewPager)container).addView(mPageViewArray.get(position));
			return mPageViewArray.get(position);
		}
		
	}
	
	class GuideViewPagerChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			Log.i(TAG, "GuideViewPagerChangeListener,onPageScrollStateChanged " + arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			isLeftBorder = false;
			isRightBorder = false;
			Log.i(TAG, "GuideViewPagerChangeListener,onPageScrolled " + arg0 + ", " + arg1 + ", " + arg2);
			if(arg0 == 0 && (arg2 - mValueInPageScrolledPrevious) == 0)
			{
				isLeftBorder = true;
			}
			else if(arg0 == (mPageViewArray.size() - 1) && (arg2 - mValueInPageScrolledPrevious) == 0)
			{
				isRightBorder = true;
			}
			Log.i(TAG, "GuideViewPagerChangeListener,onPageScrolled isLeftBorder = " + isLeftBorder + ", isRightBorder = " + isRightBorder + ".");
			mValueInPageScrolledPrevious = arg2;
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			Log.i(TAG, "GuideViewPagerChangeListener,onPageSelected " + arg0);
			ImageView imageView = null;
			for(int i = 0; i < mTabLayout.getChildCount(); i ++)
			{
				imageView = (ImageView) mTabLayout.getChildAt(i);
				if(i == arg0)
				{
					imageView.setImageResource(R.drawable.guide_dots_selected);
				}
				else
				{
					imageView.setImageResource(R.drawable.guide_dots);
				}
			}
		}
		
	}
	
	class GuideViewGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onFling,isRightBorder = " + isRightBorder);
			if(isRightBorder){
				Log.i(TAG, "onFling,e1.x = " + e1.getX() + ", and e2.x = " + e2.getX());
				if(Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY()) && (e1.getX() - e2.getX() >= mFlaggingWidth ))
				{
//					Intent intent = new Intent();
//					intent.setComponent(new ComponentName("com.health.healthdiagnosis", "com.health.healthdiagnosis.HealthDiagnosisActivity"));
					Intent intent = new Intent(GuideViewActivity.this,HealthDiagnosisFragmentActivity.class);
					startActivity(intent);
					finish();
					return true;
				}
			}
			return false;
		}
		
	}
	
}
