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
	
	private String[] mDiagnosisItemName = {
			"大便","小便","经期",
			"感冒","咳嗽","痘痘",
			"颈椎","食欲","情绪"
	};
	
	public DiagnosisGridViewItemsAdapter(Context context)
	{
		mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDiagnosisItemName.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
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
	
	class GridViewItem{// will be good for expend
		ImageView mImageView;
		TextView mTextView;
	}

}
