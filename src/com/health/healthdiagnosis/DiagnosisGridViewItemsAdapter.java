package com.health.healthdiagnosis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DiagnosisGridViewItemsAdapter extends BaseAdapter {
	
	private final String TAG = "DiagnosisItemsAdapter";
	private Context mContext;
	private GridViewItem mGridViewItem;
	
	private String[] mDiagnosisItemName = null;
	
	public DiagnosisGridViewItemsAdapter(Context context)
	{
		mContext = context;
	}
	
	public DiagnosisGridViewItemsAdapter(Context context,String[] data)
	{
		mContext = context;
		mDiagnosisItemName = data;
	}
	
	public void setAdapterData(String[] data)
	{
		mDiagnosisItemName = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDiagnosisItemName.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mDiagnosisItemName[arg0];
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
			mGridViewItem = new GridViewItem();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.diagnosis_grid_view_item, parent, false);
			mGridViewItem.mTextView = (TextView) convertView.findViewById(R.id.diagnosis_grid_view_item_textview);
			mGridViewItem.mImageView = (ImageView) convertView.findViewById(R.id.diagnosis_grid_view_item_imageview);
			convertView.setTag(mGridViewItem);
		}
		else
		{
			mGridViewItem = (GridViewItem) convertView.getTag();
		}
		mGridViewItem.mTextView.setText(mDiagnosisItemName[position]);
		return convertView;
	}
	
	class GridViewItem{// will be good for expand
		ImageView mImageView;
		TextView mTextView;
	}

}
