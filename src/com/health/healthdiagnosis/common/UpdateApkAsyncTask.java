package com.health.healthdiagnosis.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.health.healthdiagnosis.HealthDiagnosisFragmentActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class UpdateApkAsyncTask extends AsyncTask<URL, Integer, Boolean> {

	final static String TAG = "UpdateApkAsyncTask";
	private String mDownloadPath = null;
	private String mDownloadApkName = "HealthDiagnosis.apk";
	private String mDownloadApkPathAndName = null;
	
	private Context mContext;
	
	public void setContext(Context context){
		mContext = context;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(URL... params) {
		// TODO Auto-generated method stub
		try {
			HttpURLConnection conn = (HttpURLConnection) params[0].openConnection();
			if(conn.getResponseCode() != HttpURLConnection.HTTP_OK)// the network is ok or not
			{
				Log.i(TAG, "downloadApk,the network is not well.");
				publishProgress(1);//Network is not well
				return false;
			}
			else
			{
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED))// the sdcard is ok or not
				{
					Log.i(TAG, "downloadApk,the sdcard does not exist.");
					publishProgress(2);//No sdcard
					return false;
				}
				else
				{
					mDownloadPath = Environment.getExternalStorageDirectory() + "/Download";
					File file = new File(mDownloadPath);
					if(! file.exists())
					{
						file.mkdir();
					}
					
					File downloadApkName = new File(mDownloadPath, mDownloadApkName);
					if(! downloadApkName.exists())
					{
						downloadApkName.createNewFile();
					}
					else
					{
						downloadApkName.delete();
					}
					
					InputStream is = conn.getInputStream();
					OutputStream os = new FileOutputStream(downloadApkName);
					byte[] buffer = new byte[2048];
					int length = 0;
					while((length = is.read(buffer)) != -1)
					{
						os.write(buffer, 0, length);
					}
					
					is.close();
					os.flush();
					os.close();
					
					publishProgress(3);//download success
					mDownloadApkPathAndName = downloadApkName.getAbsolutePath();
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		if(result)
		{
			((HealthDiagnosisFragmentActivity)mContext).installApk(mDownloadApkPathAndName);
		}
	}

}
