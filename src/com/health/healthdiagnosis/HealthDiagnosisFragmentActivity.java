package com.health.healthdiagnosis;

import com.health.healthdiagnosis.data.HealthSharedPreference;
import com.health.healthdiagnosis.database.SQLiteHelper;
import com.health.healthdiagnosis.ui.GlobalConstValues;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class HealthDiagnosisFragmentActivity extends FragmentActivity {

	final static String TAG = "HealthDiagnosisFragmentActivity";
	private EditText mActionViewEditText = null;
	private ShareActionProvider mShareActionProvider = null;
	private HealthSharedPreference mHealthSharedPrefs = null;
	private SQLiteHelper mSqliteHelper = null;
	public static DiagnosisGridViewItemsAdapter mGridViewAdapter = null;
	private HealthDiagnosisFragmentPagerAdapter mHDFragmentPagerAdapter;

	private ViewPager mDiagnosisViewPager = null;
	private ActionBar.TabListener mActionBarTabListener = new ActionBar.TabListener() {

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			mDiagnosisViewPager.setCurrentItem(tab.getPosition());

		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.health_diagnosis_fragment_activity);
		
		mHDFragmentPagerAdapter = new HealthDiagnosisFragmentPagerAdapter(getSupportFragmentManager());
		
		mDiagnosisViewPager = (ViewPager) findViewById(R.id.diagnosis_view_pager);
		mDiagnosisViewPager.setOnPageChangeListener(new HealthDiagnosisPagerListener());
		mDiagnosisViewPager.setAdapter(mHDFragmentPagerAdapter);
		
		final ActionBar mActionBar = getActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for(int i = 0;i < 2;i++)
		{
			switch (i) {
			case 0:
				mActionBar.addTab(mActionBar.newTab().setText("Main").setTabListener(mActionBarTabListener));
				break;
			case 1:
				mActionBar.addTab(mActionBar.newTab().setText("Detail").setTabListener(mActionBarTabListener));
				break;

			default:
				break;
			}
			
		}
		
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		getMenuInflater().inflate(R.menu.health_diagnosis_actions, menu);
		MenuItem addEditItem = menu.findItem(R.id.action_add);
		MenuItem deleteItem = menu.findItem(R.id.action_delete);
		MenuItem shareItem = menu.findItem(R.id.action_share);
		
		mActionViewEditText = (EditText) MenuItemCompat.getActionView(addEditItem);
		MenuItemCompat.setOnActionExpandListener(addEditItem, new OnActionExpandListener() {
			
			@Override
			public boolean onMenuItemActionExpand(MenuItem arg0) {
				// TODO Auto-generated method stub
				Log.i(TAG, "onCreateOptionsMenu,onMenuItemActionExpand.");
				configActionViewEditText();
				
				return true;
			}
			
			@Override
			public boolean onMenuItemActionCollapse(MenuItem arg0) {
				// TODO Auto-generated method stub
				Log.i(TAG, "onCreateOptionsMenu,onMenuItemActionCollapse.");
				return true;
			}
		});
		mShareActionProvider = (ShareActionProvider) shareItem.getActionProvider();
		setShareIntent(getDefaultIntent());
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void setShareIntent(Intent shareIntent)// delay intent value
	{
		if(mShareActionProvider != null)
		{
			mShareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
			mShareActionProvider.setShareIntent(getDefaultIntent());
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onOptionsItemSelected,item id = " + item.getItemId());
		switch (item.getItemId()) {
		case R.id.action_add:
			addGridViewItem();
			break;
		case R.id.action_delete:
			deleteGridViewItem();
			break;
		case R.id.action_share:
			doShareTo();
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void doShareTo() {
		// TODO Auto-generated method stub
//		Intent intent = new Intent(Intent.ACTION_SEND);
//		intent.setType("image/*");
//		Uri uri = Uri.fromFile(getFileStreamPath("ic_launcher.png"));
//		intent.putExtra(Intent.EXTRA_STREAM, uri);
//		setShareIntent(intent);
		
		Intent intent = new Intent();
		ComponentName comp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareToTimeLineUI");
		intent.setComponent(comp);
		intent.setAction(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_TEXT, "The result of health diagnosis");
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(getFileStreamPath("ic_launcher.png")));
		startActivity(Intent.createChooser(intent, getResources().getText(R.string.action_share)));
		
	}
	
	private Intent getDefaultIntent()
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		return intent;
	}

	private void deleteGridViewItem() {
		// TODO Auto-generated method stub
//		Log.i(TAG, "deleteGridViewItem,mDiagnosisItemName = " + mHealthSharedPrefs.mDiagnosisItemName);
	}

	private void addGridViewItem() {
		// TODO Auto-generated method stub
//		Log.i(TAG, "addGridViewItem,mDiagnosisItemName = " + mHealthSharedPrefs.mDiagnosisItemName);
		
//		mDiagnosisItemName = mDiagnosisItemName + ",Tongue";
//		mGridViewAdapter.setAdapterData(mDiagnosisItemName.split(","));
//		mGridViewAdapter.notifyDataSetChanged();
	}

	protected void configActionViewEditText() {
		// TODO Auto-generated method stub
		mActionViewEditText.setHint("Add health item");
//		editView.setFocusable(true);
		mActionViewEditText.setId(GlobalConstValues.ACTION_VIEM_EDIT_TEXT_ID);
		mActionViewEditText.setInputType(EditorInfo.TYPE_TEXT_FLAG_CAP_WORDS);
		mActionViewEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		mActionViewEditText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mActionViewEditText.setText("");
			}
		});
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		mActionViewEditText.setLayoutParams(lp);
		
		mActionViewEditText.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				boolean handled = false;
				if(actionId == EditorInfo.IME_ACTION_DONE){
					handleInputMessage();
					handled = true;
				}
				return handled;
			}
		});
	}
	
	protected void handleInputMessage() {
		// TODO Auto-generated method stub
		
		// Input Soft Keyboard
		InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		input.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
		
		//update UI in Grid View
		String editTextValue = mActionViewEditText.getText().toString();
		mHealthSharedPrefs.mDiagnosisItemName = editTextValue + "," + HealthSharedPreference.mDiagnosisItemName;
		Log.i(TAG, "handleInputMessage,update ui and ui data = " + mHealthSharedPrefs.mDiagnosisItemName);
		mGridViewAdapter.setAdapterData(mHealthSharedPrefs.mDiagnosisItemName.split(","));
		mGridViewAdapter.notifyDataSetChanged();
		mHealthSharedPrefs.updatePreferenceByString(mHealthSharedPrefs.mDiagnosisItemName);
		
		//update Database
		SQLiteHelper.DATABASE_VERSION ++;
		Log.i(TAG, "handleInputMessage,update database and database version is " + SQLiteHelper.DATABASE_VERSION);
		mSqliteHelper = new SQLiteHelper(this, SQLiteHelper.DATABASE_NAME, null, SQLiteHelper.DATABASE_VERSION);
		mSqliteHelper.setAddedColumn(editTextValue);
		mHealthSharedPrefs.updatePreferenceByInt(SQLiteHelper.DATABASE_VERSION);
	}
	
	public void initPreference() {
		// TODO Auto-generated method stub
		mHealthSharedPrefs = new HealthSharedPreference(this);
		mHealthSharedPrefs.initPreference();
	}

	public void createDB() {
		// TODO Auto-generated method stub
		mSqliteHelper = new SQLiteHelper(this, SQLiteHelper.DATABASE_NAME, null, SQLiteHelper.DATABASE_VERSION);
	}
	
	private String[] getData() {
		// TODO Auto-generated method stub
		return mHealthSharedPrefs.mDiagnosisItemName.split(",");
	}
	
	public void gridViewItemClickProcess(int position) {
		// TODO Auto-generated method stub
		Log.i(TAG, "gridViewItemClickProcess enter.");
		long clickTime = Long.valueOf(System.currentTimeMillis());
		
		if(mSqliteHelper != null)
		{
			SQLiteDatabase db = mSqliteHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			Log.i(TAG, "gridViewItemClickProcess,the " + position + " item was clicked at " + clickTime + ",and name is " + (mHealthSharedPrefs.mDiagnosisItemName.split(","))[position]);
			values.put((mHealthSharedPrefs.mDiagnosisItemName.split(","))[position].toLowerCase(), String.valueOf(clickTime));
			long rowId = db.insert(SQLiteHelper.DATABASE_TABLE, null, values);
			if(rowId > 0)
			{
				Log.i(TAG, "gridViewItemClickProcess,insert data to table success.");
			}
			else
			{
				Log.e(TAG, "gridViewItemClickProcess,insert data to table failed.");
			}
			db.close();
			
			//for debug
			{
				db = mSqliteHelper.getReadableDatabase();
				Cursor cursor = db.query(SQLiteHelper.DATABASE_TABLE, mHealthSharedPrefs.mDiagnosisItemName.split(","), "", null, null, null, null);
				String[] items = HealthSharedPreference.mDiagnosisItemName.toLowerCase().split(",");
				while(cursor.moveToNext()){
					long itemsValue = 0;
					String logText = "";
					for(int i = 0;i < items.length; i ++)
					{
						itemsValue = cursor.getLong(cursor.getColumnIndex(items[i]));
						logText = logText + "," + items[i] + " = " + itemsValue;
					}
					Log.i(TAG, "gridViewItemClickProcess,the table item " + logText + " .");
				}
				db.close();
			}
		}
		Log.i(TAG, "gridViewItemClickProcess exit.");
	}
	
	public boolean gridViewItemLongClickProcess(int position) {
		// TODO Auto-generated method stub
		Log.i(TAG, "gridViewItemLongClickProcess enter.");
		
		// update UI in Grid View
		Log.i(TAG, "gridViewItemLongClickProcess,the " + position + " item was long clicked and will be deleted.");
		mHealthSharedPrefs.mDiagnosisItemName.trim();
		String deleteItemText = (mHealthSharedPrefs.mDiagnosisItemName.split(","))[position];
		int indexOfItem = mHealthSharedPrefs.mDiagnosisItemName.indexOf(deleteItemText);
		if( indexOfItem == -1)
		{
			return false;
		}
		Log.i(TAG, "gridViewItemLongClickProcess,before deleted the items are: " + mHealthSharedPrefs.mDiagnosisItemName);
		if(position == (mHealthSharedPrefs.mDiagnosisItemName.split(",")).length - 1)//the last item
		{
			mHealthSharedPrefs.mDiagnosisItemName = mHealthSharedPrefs.mDiagnosisItemName.replace("," + deleteItemText, "");
		}
		else
		{
			mHealthSharedPrefs.mDiagnosisItemName = mHealthSharedPrefs.mDiagnosisItemName.replace(deleteItemText + ",", "");
		}
		Log.i(TAG, "gridViewItemLongClickProcess,after deleted the items are: " + mHealthSharedPrefs.mDiagnosisItemName);
		mGridViewAdapter.setAdapterData(mHealthSharedPrefs.mDiagnosisItemName.split(","));
		mGridViewAdapter.notifyDataSetChanged();
		mHealthSharedPrefs.updatePreferenceByString(mHealthSharedPrefs.mDiagnosisItemName);

		// update Database
		SQLiteHelper.DATABASE_VERSION++;
		Log.i(TAG,"gridViewItemLongClickProcess,update database and database version is "+ SQLiteHelper.DATABASE_VERSION);
		mSqliteHelper = new SQLiteHelper(this, SQLiteHelper.DATABASE_NAME,null, SQLiteHelper.DATABASE_VERSION);
		mSqliteHelper.setDeletedColumn(deleteItemText.toLowerCase());
		mHealthSharedPrefs.updatePreferenceByInt(SQLiteHelper.DATABASE_VERSION);
		
		Log.i(TAG, "gridViewItemLongClickProcess exit.");
		return true;
	}
	

	private class HealthDiagnosisFragmentPagerAdapter extends FragmentPagerAdapter{

		public HealthDiagnosisFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			Log.i(TAG, "HealthDiagnosisFragmentPagerAdapter,getItem() arg0 = " + arg0);
//			Fragment fragment = new HealthDiagnosisFragment();
//			Bundle args = new Bundle();
//			args.putInt(HealthDiagnosisFragment.argObject, arg0 + 1);
//			fragment.setArguments(args);
			Fragment fragment = null;
			switch (arg0) {
			case 0:
				mGridViewAdapter = new DiagnosisGridViewItemsAdapter(HealthDiagnosisFragmentActivity.this,getData());
				HealthDiagnosisMainFragment mainFragment = new HealthDiagnosisMainFragment();
				mainFragment.setContext(HealthDiagnosisFragmentActivity.this);
				fragment = mainFragment;
				break;
			case 1:
			case 2:
				HealthDiagnosisFragment detailFragment = new HealthDiagnosisFragment();
				Bundle args = new Bundle();
				args.putInt(HealthDiagnosisFragment.argObject, arg0 + 1);
				detailFragment.setArguments(args);
				fragment = detailFragment;
				break;

			default:
				break;
			}
			
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 10;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return super.getPageTitle(position);
		}
		
	}
	
	public static class HealthDiagnosisFragment extends Fragment{

		public static final String argObject = "object";
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View fragmentView = inflater.inflate(R.layout.health_diagnosis_detail_fragment, container, false);
			Bundle args = getArguments();
			TextView textView = (TextView) fragmentView.findViewById(R.id.main_text_view);
			textView.setText(argObject);
			return fragmentView;
		}
		
	}

	class HealthDiagnosisPagerListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			getActionBar().setSelectedNavigationItem(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	}

}
