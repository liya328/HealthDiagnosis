package com.health.healthdiagnosis;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HealthDiagnosisDetailFragment extends Fragment {

	final String TAG = "HealthDiagnosisDetailFragment";
	
	private Context mFragmentContext = null;
	private ListView mListView = null;
	private String[] mKeyArray = null;
	private DiagnosisDetailListViewAdapter mDetailListViewAdapter = null;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		mFragmentContext = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View fragmentView = inflater.inflate(R.layout.health_diagnosis_detail_fragment, container,false);
		initUI(fragmentView);
		return fragmentView;
	}
	
	public void setDetailFragmentData(String[] keyArray)
	{
		mKeyArray = keyArray;
	}

	private void initUI(View fragmentView) {
		// TODO Auto-generated method stub
		Log.i(TAG, "initUI enter.");
		mListView = (ListView) fragmentView.findViewById(R.id.diagnosis_items_detail_list_view);
		mDetailListViewAdapter = new DiagnosisDetailListViewAdapter(mFragmentContext);
		mListView.setAdapter(mDetailListViewAdapter);
		Log.i(TAG, "initUI exist.");
	}
	
	class DiagnosisDetailListViewAdapter extends BaseAdapter{

		private Context mContext;
		private ListViewItem mItem = null;
		
		public DiagnosisDetailListViewAdapter(Context context) {
			mContext = context;
		}
		
		public DiagnosisDetailListViewAdapter(Context context,String[] data) {
			mContext = context;
			mKeyArray = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mKeyArray.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mKeyArray[arg0];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null)
			{
				mItem = new ListViewItem();
				LayoutInflater inflator = LayoutInflater.from(mContext);
				convertView = inflator.inflate(R.layout.diagnosis_detail_list_view_item, parent, false);
				mItem.mKey = (TextView) convertView.findViewById(R.id.diagnosis_detail_list_view_item_key);
				mItem.mValue = (TextView) convertView.findViewById(R.id.diagnosis_detail_list_view_item_value);
				convertView.setTag(mItem);
			}
			else
			{
				mItem = (ListViewItem) convertView.getTag();
			}
			mItem.mKey.setText(mKeyArray[position]);
			mItem.mValue.setText("0%");
			return convertView;
		}
		
		class ListViewItem{
			TextView mKey;
			TextView mValue;
		}
		
	}

}
