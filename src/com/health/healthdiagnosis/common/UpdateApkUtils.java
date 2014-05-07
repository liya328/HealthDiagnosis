package com.health.healthdiagnosis.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.health.healthdiagnosis.ui.GlobalConstValues;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

public class UpdateApkUtils {

	final String TAG = "UpdateApkUtils";
	private String mDownloadPath = null;
	private String mDownloadApkName = "HealthDiagnosis.apk";
	private UpdateApkListener mUpdateApkListener = null;
	
	//AsyncTask?? or Thread?? or Service??
	
	public interface UpdateApkListener{
		public void onDownloadApkError(Message message);
		public void onDownloadApkSucess(Message message);
	}
	
	public UpdateApkUtils(Context context) {	
		mUpdateApkListener = (UpdateApkListener)context;
	}
	
	public void downloadApk()
	{
		new Thread(
				new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub//http://172.17.127.55:8080/HealthDiagnosis.apk
				try {
					URL url = new URL(GlobalConstValues.DOWNLOAD_APK_URL);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					if(conn.getResponseCode() != HttpURLConnection.HTTP_OK)// the network is ok or not
					{
						Log.i(TAG, "downloadApk,the network is not well.");
						Message message = Message.obtain();
						message.what = 1;//Network is not well
						mUpdateApkListener.onDownloadApkError(message);
					}
					else
					{
						if(Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED))// the sdcard is ok or not
						{
							Log.i(TAG, "downloadApk,the sdcard does not exist.");
							Message message = Message.obtain();
							message.what = 2;//No sdcard
							mUpdateApkListener.onDownloadApkError(message);
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
							
							Bundle bundle = new Bundle();
							Message message = Message.obtain();
							message.what = 3;
							bundle.putString("newapkpath", downloadApkName.getAbsolutePath());
							message.setData(bundle);
							mUpdateApkListener.onDownloadApkSucess(message);
						}
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
				).start();
	}
	
}
