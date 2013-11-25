package com.health.healthdiagnosis.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	private final String TAG = "SQLiteHelper";
	public final static int DATABASE_VERSION = 1;
	public final static String DATABASE_NAME = "health_diagnosis.db";
	public final static String DATABASE_TABLE = "health_diagnosis_table";
	
	public SQLiteHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE health_diagnosis_table(id INTEGER PRIMARY KEY,breakfast INTEGER," +
				"sleep INTEGER,getup INTEGER,faeces INTEGER,piss INTEGER,water INTEGER)";
		Log.i(TAG, "create Database,create health_diagnosis_table.");
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.i(TAG, "update Database from " + oldVersion + " version to " + newVersion + " .");
	}

}
