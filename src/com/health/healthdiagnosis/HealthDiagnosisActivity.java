package com.health.healthdiagnosis;

import com.health.healthdiagnosis.DiagnosisGridViewItemsAdapter.GridViewItem;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class HealthDiagnosisActivity extends Activity {
	
	private final String TAG = "HealthDiagnosisActivity";
	private GridView mDiagnosisItemsGridView = null;
	private DiagnosisGridViewItemsAdapter mGridViewAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate enter.");
		super.onCreate(savedInstanceState);
		Log.i(TAG, "setContentView enter");
		setContentView(R.layout.activity_health_diagnosis);
		Log.i(TAG, "setContentView exit");
		
		initUI();
		Log.i(TAG, "onCreate exit.");
	}

	private void initUI() {
		// TODO Auto-generated method stub
		Log.i(TAG, "initUI enter.");
		mDiagnosisItemsGridView = (GridView) findViewById(R.id.diagnosis_items_gridview);
		mGridViewAdapter = new DiagnosisGridViewItemsAdapter(this);
		mDiagnosisItemsGridView.setAdapter(mGridViewAdapter);
		mDiagnosisItemsGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
		Log.i(TAG, "initUI exit.");
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
	
	

}
