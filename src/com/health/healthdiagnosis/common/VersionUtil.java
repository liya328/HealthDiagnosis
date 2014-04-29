package com.health.healthdiagnosis.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class VersionUtil {
	private static final String TAG = "VertionUtil";
	
	public static int getVersionNo(Context context)
	{
		int versionNo = -1;
		try {
			versionNo = context.getPackageManager().getPackageInfo("com.health.healthdiagnosis", PackageManager.PERMISSION_GRANTED)
					.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return versionNo;
	}
	
	public static String getVersionName(Context context)
	{
		String versionName = "";
		try {
			versionName = context.getPackageManager().getPackageInfo("com.health.healthdiagnosis", PackageManager.PERMISSION_GRANTED)
					.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionName;
	}
}
