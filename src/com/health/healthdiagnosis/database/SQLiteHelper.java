package com.health.healthdiagnosis.database;

import com.health.healthdiagnosis.data.HealthSharedPreference;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	private final String TAG = "SQLiteHelper";
	private Context mContext = null;
	public static int DATABASE_VERSION = 1;
	public final static String DATABASE_NAME = "health_diagnosis.db";
	public final static String DATABASE_BACKUP_TABLE = "health_diagnosis_backup";
	public final static String DATABASE_BACKUP_TEMP_TABLE = "health_diagnosis_backup_temp";
	public final static String DATABASE_TABLE = "health_diagnosis_table";
	private String mAddedColumn = null;
	private String mDeletedColumn = null;
	
	private final int DATABASE_TABLE_UPDATE_FLAG = 0;
	private final int DATABASE_TABLE_UPDATE_ADD_FLAG = DATABASE_TABLE_UPDATE_FLAG + 1;
	private final int DATABASE_TABLE_UPDATE_DELETE_FLAG = DATABASE_TABLE_UPDATE_FLAG - 1;
	private int mUpdateFlag = DATABASE_TABLE_UPDATE_FLAG;

	public SQLiteHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
	}

	public SQLiteHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
		Log.i(TAG, "SQLiteHelper constructor,create health_diagnosis.db in " + version + " version");
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	/*
	 * onCreate method in SQLiteOpenHelper class will be called only once when the database is not existed,
	 * so it will not be called once the database is created.
	 * 
	 * */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onCreate,create health_diagnosis_table in " + DATABASE_NAME);
		String[] tableColumnName = HealthSharedPreference.mDiagnosisItemName.split(",");
		String sql = "CREATE TABLE " + DATABASE_TABLE + " (id INTEGER PRIMARY KEY";
		for(int i = 0;i < tableColumnName.length; i ++)
		{
			sql = sql + "," + tableColumnName[i].toLowerCase() + " INTEGER";
		}
		sql = sql + ")";
		Log.i(TAG, "onCreate,create health_diagnosis_table with sql = " + sql);
		db.execSQL(sql);
	}

	/*
	 * onUpgrade method in SQLiteOpenHelper class will be called if the version of database was changed,
	 * and the version is from the constructor method.
	 * 
	 * */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onUpgrade,update health_diagnosis_table in " + DATABASE_NAME);
		Log.i(TAG, "onUpgrade,update health_diagnosis_table from " + oldVersion + " version to " + newVersion + " version.");
		if(newVersion > oldVersion)
		{
			switch (mUpdateFlag) {
			case DATABASE_TABLE_UPDATE_ADD_FLAG:
				updateDBByAddItem(db);
				break;
			case DATABASE_TABLE_UPDATE_DELETE_FLAG:
				updateDBByDeleteItem(db);
				break;

			default:
				break;
			}
		}
	}
	
	public void updateDBByInsertValue(SQLiteDatabase db,ContentValues contentValue)
	{
		long rowId = db.insert(SQLiteHelper.DATABASE_TABLE, null, contentValue);
		if(rowId > 0)
		{
			Log.i(TAG, "gridViewItemClickProcess,insert data to table success.");
		}
		else
		{
			Log.e(TAG, "gridViewItemClickProcess,insert data to table failed.");
		}

	}
	
	// for debug
	public void retrieveDB(SQLiteDatabase db)
	{
		Cursor cursor = db.query(SQLiteHelper.DATABASE_TABLE, HealthSharedPreference.mDiagnosisItemName.split(","), "", null, null, null, null);
		String[] items = HealthSharedPreference.mDiagnosisItemName.toLowerCase().split(",");
		while(cursor.moveToNext()){
			long itemsValue = 0;
			String logText = "";
			for(int i = 0;i < items.length; i ++)
			{
				itemsValue = cursor.getLong(cursor.getColumnIndex(items[i]));
				logText = logText + "," + items[i] + " = " + itemsValue;
			}
			Log.i(TAG, "retrieveDB,the table item " + logText + " .");
		}
	}
	
	private void updateDBByAddItem(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		String sql = "ALTER TABLE " + DATABASE_TABLE + " ADD COLUMN " + mAddedColumn.toLowerCase() + " INTEGER";
		Log.i(TAG, "updateDBByAddItem,add new column " + mAddedColumn.toLowerCase());
		Log.i(TAG, "updateDBByAddItem,adding sql statement is " + sql);
		db.execSQL(sql);
//		db.close();
	}
	
	private void updateDBByDeleteItem(SQLiteDatabase db)
	{
		db.beginTransaction();
		try{
			
			// create back-up table
			String sql = "DROP TABLE IF EXISTS " + DATABASE_BACKUP_TABLE;
			db.execSQL(sql);
			String[] tableColumnName = HealthSharedPreference.mOldDiagnosisItemName.split(",");
			sql = "CREATE TABLE " + DATABASE_BACKUP_TABLE + " (id INTEGER PRIMARY KEY";
			Log.i(TAG, "updateDBByDeleteItem,add will be deleted column is " + mDeletedColumn);
			for(int i = 0;i < tableColumnName.length; i ++)
			{
//				if(tableColumnName[i].toLowerCase() != mDeletedColumn)
				{
					sql = sql + "," + tableColumnName[i].toLowerCase() + " INTEGER";
				}
			}
			sql = sql + ")";
			Log.i(TAG, "updateDBByDeleteItem,deleting one column sql statement is " + sql);
			db.execSQL(sql);
			
			//insert data without deleted column from now table
			sql = "INSERT INTO " + DATABASE_BACKUP_TABLE + " SELECT " + "id," 
			+ HealthSharedPreference.mOldDiagnosisItemName.toLowerCase() + " FROM " + DATABASE_TABLE;
			Log.i(TAG, "updateDBByDeleteItem,sql statement of copying now table to back up table is " + sql);
			db.execSQL(sql);
			
//			//for debug
//			Cursor cursor = db.query(SQLiteHelper.DATABASE_BACKUP_TABLE, HealthSharedPreference.mOldDiagnosisItemName.split(","), "", null, null, null, null);
//			String[] items = HealthSharedPreference.mOldDiagnosisItemName.toLowerCase().split(",");
//			while(cursor.moveToNext()){
//				long itemsValue = 0;
//				String logText = "";
//				for(int i = 0;i < items.length; i ++)
//				{
//					itemsValue = cursor.getLong(cursor.getColumnIndex(items[i]));
//					logText = logText + "," + items[i] + " = " + itemsValue;
//				}
//				Log.i(TAG, "updateDBByDeleteItem,the back-up table item " + logText + " .");
//			}
			
			//create temp back-up table
			sql = "DROP TABLE IF EXISTS " + DATABASE_BACKUP_TEMP_TABLE;
			db.execSQL(sql);
			tableColumnName = HealthSharedPreference.mOldDiagnosisItemName.split(",");
			sql = "CREATE TABLE " + DATABASE_BACKUP_TEMP_TABLE + " (id INTEGER PRIMARY KEY";
			Log.i(TAG, "updateDBByDeleteItem,add will be deleted column is " + mDeletedColumn);
			for(int i = 0;i < tableColumnName.length; i ++)
			{
//				if(tableColumnName[i].toLowerCase() != mDeletedColumn)
				{
					sql = sql + "," + tableColumnName[i].toLowerCase() + " INTEGER";
				}
			}
			sql = sql + ")";
			Log.i(TAG, "updateDBByDeleteItem,deleting one column sql statement is " + sql);
			db.execSQL(sql);
			
			//insert data without deleted column from now table
			sql = "INSERT INTO " + DATABASE_BACKUP_TEMP_TABLE + " SELECT " + "id," 
			+ HealthSharedPreference.mOldDiagnosisItemName.toLowerCase() + " FROM " + DATABASE_TABLE; // + " where " + mDeletedColumn + " = 0" ;
			Log.i(TAG, "updateDBByDeleteItem,sql statement of copying now table to back up table is " + sql);
			db.execSQL(sql);
			
//			//for debug
//			cursor = db.query(SQLiteHelper.DATABASE_BACKUP_TEMP_TABLE, HealthSharedPreference.mOldDiagnosisItemName.split(","), "", null, null, null, null);
//			items = HealthSharedPreference.mOldDiagnosisItemName.toLowerCase().split(",");
//			while(cursor.moveToNext()){
//				long itemsValue = 0;
//				String logText = "";
//				for(int i = 0;i < items.length; i ++)
//				{
//					itemsValue = cursor.getLong(cursor.getColumnIndex(items[i]));
//					logText = logText + "," + items[i] + " = " + itemsValue;
//				}
//				Log.i(TAG, "updateDBByDeleteItem,the back-up table item " + logText + " .");
//			}
			
			//renew DATABASE_TABLE from DATABASE_BACKUP_TABLE
			sql = "DROP TABLE IF EXISTS " + DATABASE_TABLE;
			db.execSQL(sql);
			tableColumnName = HealthSharedPreference.mDiagnosisItemName.split(",");
			sql = "CREATE TABLE " + DATABASE_TABLE + " (id INTEGER PRIMARY KEY";
			for(int i = 0;i < tableColumnName.length; i ++)
			{
//				if(tableColumnName[i].toLowerCase() != mDeletedColumn)
				{
					sql = sql + "," + tableColumnName[i].toLowerCase() + " INTEGER";
				}
			}
			sql = sql + ")";
			Log.i(TAG, "updateDBByDeleteItem,deleting one column sql statement is " + sql);
			db.execSQL(sql);
			
			//insert data without deleted column from now table
			sql = "INSERT INTO " + DATABASE_TABLE + " SELECT " + "id," 
			+ HealthSharedPreference.mDiagnosisItemName.toLowerCase()+ " FROM " + DATABASE_BACKUP_TEMP_TABLE;
			Log.i(TAG, "updateDBByDeleteItem,sql statement of copying back up table to now table is " + sql);
			db.execSQL(sql);
			
//			//for debug
//			cursor = db.query(SQLiteHelper.DATABASE_TABLE, HealthSharedPreference.mDiagnosisItemName.split(","), "", null, null, null, null);
//			items = HealthSharedPreference.mDiagnosisItemName.toLowerCase().split(",");
//			while(cursor.moveToNext()){
//				long itemsValue = 0;
//				String logText = "";
//				for(int i = 0;i < items.length; i ++)
//				{
//					itemsValue = cursor.getLong(cursor.getColumnIndex(items[i]));
//					logText = logText + "," + items[i] + " = " + itemsValue;
//				}
//				Log.i(TAG, "updateDBByDeleteItem,the new now table item " + logText + " .");
//			}
			
			sql = "DROP TABLE IF EXISTS " + DATABASE_BACKUP_TEMP_TABLE;
			db.execSQL(sql);
//			db.delete(DATABASE_BACKUP_TEMP_TABLE, null, null);
			
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		
//		db.close();
		
	}

	public void setAddedColumn(String addedColumn)
	{
		mAddedColumn = addedColumn;
		mUpdateFlag = DATABASE_TABLE_UPDATE_ADD_FLAG;
	}
	
	public void setDeletedColumn(String deletedColumn)
	{
		mDeletedColumn = deletedColumn;
		mUpdateFlag = DATABASE_TABLE_UPDATE_DELETE_FLAG;
	}

}
