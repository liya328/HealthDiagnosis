package com.health.healthdiagnosis;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.health.healthdiagnosis.common.VersionUtil;
import com.health.healthdiagnosis.data.HealthSharedPreference;
import com.health.healthdiagnosis.database.SQLiteHelper;
import com.health.healthdiagnosis.ui.GlobalConstValues;
import com.health.healthdiagnosis.ui.InputImageView;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.ShareActionProvider.OnShareTargetSelectedListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class HealthDiagnosisActivity extends Activity {
	
	private final String TAG = "HealthDiagnosisActivity";
	private GridView mDiagnosisItemsGridView = null;
//	private InputImageView mInputAddView = null;
	private EditText mActionViewEditText = null;
	private ShareActionProvider mShareActionProvider = null;
	private DiagnosisGridViewItemsAdapter mGridViewAdapter = null;
	private SQLiteHelper mSqliteHelper = null;
	private HealthSharedPreference mHealthSharedPrefs = null;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			final AppInfo appInfo = (AppInfo)msg.obj;
			if(appInfo != null)
			{
				//new AlertDialog
				AlertDialog alertDialog = new AlertDialog.Builder(HealthDiagnosisActivity.this).create();
				if(appInfo.isNew())//has new app to update
				{
					Log.i(TAG, "HealthDiagnosis could be updated.");
					alertDialog.setTitle("HealthDiagnosis Update");
					alertDialog.setMessage("Current application version is " + appInfo.preVersionNo + ",and now newest version "
							+ appInfo.versionNo + " with some new features could be updated,would you like to do?");

					alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							// to install newest apk
							if(appInfo.apkLocation != null)
							{
								installApk(appInfo.apkLocation);
							}
						}
					});
					
					alertDialog.setButton2("Later", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
						
					});
					alertDialog.show();
				}
			}
		}
		
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate enter.");
		super.onCreate(savedInstanceState);
		Log.i(TAG, "setContentView enter");
		setContentView(R.layout.activity_health_diagnosis);
		Log.i(TAG, "setContentView exit");
		
		initPreference();
		createDB();
		initUI();
		
		final ActionBar actionBar = getActionBar();
		actionBar.getNavigationMode();
		
		//start thread that check the application version no whether is latest
		new UpdateThread().start();
		
		Log.i(TAG, "onCreate exit.");
	}

	private void initPreference() {
		// TODO Auto-generated method stub
		mHealthSharedPrefs = new HealthSharedPreference(this);
		mHealthSharedPrefs.initPreference();
	}

	private void createDB() {
		// TODO Auto-generated method stub
		mSqliteHelper = new SQLiteHelper(this, SQLiteHelper.DATABASE_NAME, null, SQLiteHelper.DATABASE_VERSION);
	}

	private void initUI() {
		// TODO Auto-generated method stub
		Log.i(TAG, "initUI enter.");
		mDiagnosisItemsGridView = (GridView) findViewById(R.id.diagnosis_items_gridview);
		mGridViewAdapter = new DiagnosisGridViewItemsAdapter(this,getData());
		mDiagnosisItemsGridView.setAdapter(mGridViewAdapter);
		mDiagnosisItemsGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				gridViewItemClickProcess(position);
			}
		});
		mDiagnosisItemsGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				
				
				return gridViewItemLongClickProcess(position);
			}
		});
		
		
//		mInputAddView = (InputImageView) findViewById(R.id.add_diagnosis_gridview_item);
//		mInputAddView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Log.i(TAG, "addGridViewItem,mInputAddView was clicked.");
//			}
//		});
		
		Log.i(TAG, "initUI exit.");
	}

	private String[] getData() {
		// TODO Auto-generated method stub
		return mHealthSharedPrefs.mDiagnosisItemName.split(",");
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
		mHealthSharedPrefs.mDiagnosisItemName = editTextValue + "," + mHealthSharedPrefs.mDiagnosisItemName;
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
		
		return super.onOptionsItemSelected(item);//true;
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
		Log.i(TAG, "deleteGridViewItem,mDiagnosisItemName = " + mHealthSharedPrefs.mDiagnosisItemName);
	}

	private void addGridViewItem() {
		// TODO Auto-generated method stub
		Log.i(TAG, "addGridViewItem,mDiagnosisItemName = " + mHealthSharedPrefs.mDiagnosisItemName);
		
//		mDiagnosisItemName = mDiagnosisItemName + ",Tongue";
//		mGridViewAdapter.setAdapterData(mDiagnosisItemName.split(","));
//		mGridViewAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	protected void gridViewItemClickProcess(int position) {
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
	
	protected boolean gridViewItemLongClickProcess(int position) {
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
	
	private void installApk(String location)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),location)), "application/vnd.android.package-archive");
		startActivity(intent);
	}
	
	class UpdateThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			Properties props = new Properties();
			InputStream in = null;
			try {
				
				//read version no from local properties file instead of internet server
				in = getAssets().open("apk.properties");
//				in = getClassLoader().getResourceAsStream("apk.properties");//apk.properties
				props.load(in);
				int lastestVersionCode = Integer.parseInt(props.getProperty("lastest_versionCode"));
				String lastestVersionName = props.getProperty("lastest_versionName");
				String apkLocation = props.getProperty("lastest_apk_location");
				
				Log.i(TAG, "HealthDiagnosis app lastest version code = " + lastestVersionCode + ",lastest version name = " + lastestVersionName
						+ ",and apk location = " + apkLocation);
				
				//version no from current local application
				int curVersionCode = VersionUtil.getVersionNo(HealthDiagnosisActivity.this);
				String curVersionName = VersionUtil.getVersionName(HealthDiagnosisActivity.this);
				
				Log.i(TAG, "HealthDiagnosis app current version code = " + curVersionCode + ",current version name = " + curVersionName);
				
				AppInfo appInfo = new AppInfo(curVersionCode,lastestVersionCode,curVersionName,lastestVersionName,apkLocation);
				//send a message to handler from global message pool that has existed at android system
				Message msg = mHandler.obtainMessage();
				msg.obj = appInfo;
				msg.sendToTarget();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	class AppInfo{
		int preVersionNo;
		int versionNo;
		String preVersionName;
		String versionName;
		String apkLocation;
		
		public AppInfo(int preVersionNo,int versionNo,String preVersionName,String versionName,String apkLocation)
		{
			super();
			this.preVersionNo = preVersionNo;
			this.preVersionName = preVersionName;
			this.versionNo = versionNo;
			this.versionName = versionName;
			this.apkLocation = apkLocation;
		}
		
		public boolean isNew()
		{
			return versionNo > preVersionNo;
		}
		
	}

}
