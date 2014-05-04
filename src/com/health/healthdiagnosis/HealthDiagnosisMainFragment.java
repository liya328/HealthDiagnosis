package com.health.healthdiagnosis;

import com.health.healthdiagnosis.data.HealthSharedPreference;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class HealthDiagnosisMainFragment extends Fragment{
	
	final static String TAG = "HealthDiagnosisMainFragment";
	
	private Context mContext;
	private GridView mDiagnosisItemsGridView = null;
	
	public HealthDiagnosisMainFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void setContext(Context context)
	{
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View fragmentView = inflater.inflate(R.layout.health_diagnosis_main_fragment, container, false);
		((HealthDiagnosisFragmentActivity)mContext).initPreference();
		((HealthDiagnosisFragmentActivity)mContext).createDB();
		initUI(fragmentView);
		return fragmentView;
	}
	
	private void initUI(View fragmentView) {
		// TODO Auto-generated method stub
		Log.i(TAG, "initUI enter.");
		
		mDiagnosisItemsGridView = (GridView) (fragmentView.findViewById(R.id.diagnosis_items_grid_view));
		mDiagnosisItemsGridView.setAdapter(HealthDiagnosisFragmentActivity.mGridViewAdapter);
		HealthDiagnosisFragmentActivity.mGridViewAdapter.setAdapterData(HealthSharedPreference.mDiagnosisItemName.split(","));
		HealthDiagnosisFragmentActivity.mGridViewAdapter.notifyDataSetChanged();
		
		mDiagnosisItemsGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				((HealthDiagnosisFragmentActivity)mContext).gridViewItemClickProcess(position);
			}
		});
		
		mDiagnosisItemsGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				return ((HealthDiagnosisFragmentActivity)mContext).gridViewItemLongClickProcess(position);
			}
		});
		
		
//		mInputAddView = (InputImageView) findViewById(R.id.add_diagnosis_gridview_item);
//		mInputAddView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Log.i(TAG, "addGridViewItem,mInputAddView was clicked.");
//			}
//		});
		
		Log.i(TAG, "initUI exit.");
	}
	
}
