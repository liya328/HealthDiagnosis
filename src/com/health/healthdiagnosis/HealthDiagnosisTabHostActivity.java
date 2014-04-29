package com.health.healthdiagnosis;

import android.app.Activity;
import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class HealthDiagnosisTabHostActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.health_diagnosis_main_tabhost);
		TabHost tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("main_view").setIndicator("Main").setContent(R.id.main_view));
		tabHost.addTab(tabHost.newTabSpec("detail_view").setIndicator("Detail").setContent(R.id.detail_view));
	}

}
