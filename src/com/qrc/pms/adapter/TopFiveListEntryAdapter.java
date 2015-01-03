package com.qrc.pms.adapter;

import java.util.ArrayList;
import java.util.List;

import com.qrc.pms.R;
import com.qrc.pms.model.TopFiveEntryList;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TopFiveListEntryAdapter extends BaseAdapter{
	
	private Context context;
	private List<TopFiveEntryList> topFiveEntryLists;
	public TopFiveListEntryAdapter(Context context, ArrayList<TopFiveEntryList> topFiveEntryLists)
	{
		this.context = context;
		this.topFiveEntryLists = topFiveEntryLists;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return topFiveEntryLists.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub	
		
		return topFiveEntryLists.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.top_five_list, null);
        }
		
		TextView txtAddress = (TextView) convertView.findViewById(R.id.address);
		TextView txtTotal = (TextView) convertView.findViewById(R.id.numbers);
		
		TopFiveEntryList top = topFiveEntryLists.get(pos);
		txtAddress.setText(top.address);
		txtTotal.setText("" + top.numbers);
		
		return convertView;
	}
	
	public void add(TopFiveEntryList topFiveList) {
		topFiveEntryLists.add(topFiveList);
		notifyDataSetChanged();
	}
	
	
}
