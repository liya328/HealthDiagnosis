package com.health.healthdiagnosis.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	private final String TAG = "SQLiteHelper";
	public static int DATABASE_VERSION = 1;
	public final static String DATABASE_NAME = "health_diagnosis.db";
	public final static String DATABASE_TABLE = "health_diagnosis_table";
	private String mAddedColumn = null;
	
	public SQLiteHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		Log.i(TAG, "SQLiteHelper constructor,create health_diagnosis.db in " + version + " version");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onCreate,create health_diagnosis_table in " + DATABASE_NAME);
		String sql = "CREATE TABLE " + DATABASE_NAME + "(id INTEGER PRIMARY KEY,breakfast INTEGER," +
				"sleep INTEGER,getup INTEGER,faeces INTEGER,piss INTEGER,water INTEGER)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onUpgrade,create health_diagnosis_table in " + DATABASE_NAME);
		Log.i(TAG, "onUpgrade,update health_diagnosis_table from " + oldVersion + " version to " + newVersion + " version.");
		if(newVersion > oldVersion)
		{
			updateDB(db);
		}
	}
	
	private void updateDB(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "ALTER TABLE " + DATABASE_TABLE + " ADD COLUMN " + mAddedColumn.toLowerCase() + " INTEGER";
		Log.i(TAG, "update Database,add new column " + mAddedColumn.toLowerCase());
		Log.i(TAG, "update Database,add sql statement is " + sql);
		db.execSQL(sql);
	}

	public void setAddedColumn(String addedColumn)
	{
		mAddedColumn = addedColumn;
	}

}
