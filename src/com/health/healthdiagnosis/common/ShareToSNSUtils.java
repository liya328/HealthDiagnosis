package com.health.healthdiagnosis.common;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class ShareToSNSUtils {

	final static String TAG = "ShareToSNSUtils";
	private String mSaveImagePath = null;
	private String mImageName = null;
	
	public Intent getSharedIntentWithPhotoAndText(String photoName,String shareText)
	{
		Intent shareIntent = new Intent();
//		ComponentName comp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareToTimeLineUI");
//		shareIntent.setComponent(comp);
		shareIntent.setAction(Intent.ACTION_SEND);
		File file = new  File(photoName);
		shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		shareIntent.setType("image/*");
		return shareIntent;
	}
	public Intent getSharedIntentWithPhotoAndText(String shareText)
	{
		Intent shareIntent = new Intent();
//		ComponentName comp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareToTimeLineUI");
//		shareIntent.setComponent(comp);
		shareIntent.setAction(Intent.ACTION_SEND);
		File file = new  File(mImageName);
		shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		shareIntent.setType("image/*");
		return shareIntent;
	}
	
	public String getAndSaveCurrentImage(WindowManager windowManager,View decorView)
	{
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		
		Bitmap bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		decorView.setDrawingCacheEnabled(true);
		bmp = decorView.getDrawingCache();
		
		mSaveImagePath = getSDCardPath() + "/DCIM/HealthDiagnosis/ScreenShot";
		
		File path = new File(mSaveImagePath);
		if(!path.exists())
		{
			path.mkdirs();
		}
		
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyymmddhhmmss",Locale.CHINESE);
		mImageName = mSaveImagePath + "/" + dataFormat.format(System.currentTimeMillis()) + ".png";
		
		try {
			File file = new File(mImageName);
			if(!file.exists())
			{
				file.createNewFile();
			}
			
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if(fos != null)
			{
				bmp.compress(CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return mImageName;
	}

	private String getSDCardPath() {
		// TODO Auto-generated method stub
		File sdcardDir = null;
		boolean isSdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if(isSdcardExist)
		{
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}
	
}
