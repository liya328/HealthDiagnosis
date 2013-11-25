package com.health.healthdiagnosis;

import com.health.healthdiagnosis.database.SQLiteHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class HealthDiagnosisActivity extends Activity {
	
	private final String TAG = "HealthDiagnosisActivity";
	private GridView mDiagnosisItemsGridView = null;
	private DiagnosisGridViewItemsAdapter mGridViewAdapter = null;
	private SQLiteHelper mSqliteHelper = null;
	private String[] mDiagnosisItemName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate enter.");
		super.onCreate(savedInstanceState);
		Log.i(TAG, "setContentView enter");
		setContentView(R.layout.activity_health_diagnosis);
		Log.i(TAG, "setContentView exit");
		
		createDB();
		initUI();
		Log.i(TAG, "onCreate exit.");
	}

	private void createDB() {
		// TODO Auto-generated method stub
		mSqliteHelper = new SQLiteHelper(this, SQLiteHelper.DATABASE_NAME, null, SQLiteHelper.DATABASE_VERSION);
		SQLiteDatabase db = mSqliteHelper.getReadableDatabase();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		Log.i(TAG, "initUI enter.");
		mDiagnosisItemsGridView = (GridView) findViewById(R.id.diagnosis_items_gridview);
		getData();
		mGridViewAdapter = new DiagnosisGridViewItemsAdapter(this,mDiagnosisItemName);
		mDiagnosisItemsGridView.setAdapter(mGridViewAdapter);
		mDiagnosisItemsGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				itemClickProcess(position);
			}
		});
		Log.i(TAG, "initUI exit.");
	}

	private void getData() {
		// TODO Auto-generated method stub
		mDiagnosisItemName = new String[]{
				"Breakfast","Sleep","Getup",
				"Faeces","Piss","Water",
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.health_diagnosis, menu);
		return true;
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
	
	protected void itemClickProcess(int position) {
		// TODO Auto-generated method stub
		
		long clickTime = Long.valueOf(System.currentTimeMillis());
		
		if(mSqliteHelper != null)
		{
			SQLiteDatabase db = mSqliteHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			Log.i(TAG, "itemClickProcess,the " + position + " item was clicked at " + clickTime + ",and name is " + mDiagnosisItemName[position]);
			values.put(mDiagnosisItemName[position].toLowerCase(), String.valueOf(clickTime));
			long rowId = db.insert(SQLiteHelper.DATABASE_TABLE, null, values);
			if(rowId > 0)
			{
				Log.i(TAG, "itemClickProcess,insert data to table success.");
			}
			else
			{
				Log.e(TAG, "itemClickProcess,insert data to table failed.");
			}
			db.close();
			
			//for debug
			{
				db = mSqliteHelper.getReadableDatabase();
				Cursor cursor = db.query(SQLiteHelper.DATABASE_TABLE, mDiagnosisItemName, "", null, null, null, null);
				while(cursor.moveToNext()){
					long breakfast = cursor.getLong(cursor.getColumnIndex("breakfast"));
					long sleep = cursor.getLong(cursor.getColumnIndex("sleep"));
					long getup = cursor.getLong(cursor.getColumnIndex("getup"));
					long faeces = cursor.getLong(cursor.getColumnIndex("faeces"));
					long piss = cursor.getLong(cursor.getColumnIndex("piss"));
					long water = cursor.getLong(cursor.getColumnIndex("water"));
					Log.i(TAG, "itemClickProcess,the table item: breakfast = " + breakfast + ",sleep = " + sleep + ",getup = " + getup
							+ ",faeces = "  + faeces + ",piss = " + piss + ",water = " + water + " .");
				}
				db.close();
			}
		}
	}

}
