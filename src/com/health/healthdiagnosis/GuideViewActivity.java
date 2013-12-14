package com.health.healthdiagnosis;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class GuideViewActivity extends Activity {

	private final String TAG = "GuideViewActivity";
	private ViewPager mViewPager;
	private ArrayList<View> mPageViewArray;
	private ViewGroup mMainLayout;//container for page view
	private ViewGroup mViewPagerIndex;// for little circle icon
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LayoutInflater inflater = getLayoutInflater();
		mPageViewArray = new ArrayList<View>();
		mPageViewArray.add(inflater.inflate(R.layout.guide_view_item_one, null));
		mPageViewArray.add(inflater.inflate(R.layout.guide_view_item_one, null));
		mPageViewArray.add(inflater.inflate(R.layout.guide_view_item_one, null));
		
		mMainLayout = (ViewGroup) inflater.inflate(R.layout.guide_view_main, null);
		mViewPager = (ViewPager) mMainLayout.findViewById(R.id.guide_view_main_pager);
		
		setContentView(mMainLayout);
		mViewPager.setAdapter(new GuideViewPagerAdapter());
		mViewPager.setOnPageChangeListener(new GuideViewPagerChangeListener());
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
			Log.i(TAG, "GuideViewPagerChangeListener,onPageScrolled " + arg0 + ", " + arg1 + ", " + arg2);
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			Log.i(TAG, "GuideViewPagerChangeListener,onPageSelected " + arg0);
			if(arg0 == (mPageViewArray.size() - 1))
			{
				Intent intent = new Intent();
			}
		}
		
	}
	
}
