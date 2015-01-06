package com.qrc.pms.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qrc.pms.R;
import com.qrc.pms.model.Pig;

public class PigListAdapter extends BaseAdapter{
	List<Pig> pigList;
	Context context;
	int entryResID = R.layout.pig_list_entry;
	
	public PigListAdapter(Context context, ArrayList<Pig> pigList) {
		super();
		// TODO Auto-generated constructor stub
		this.context = context;
		this.pigList = pigList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pigList.size();
	}

	@Override
	public Pig getItem(int arg0) {
		// TODO Auto-generated method stub
		return pigList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public void set(int position, Pig object) {
		pigList.set(position, object);
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		public TextView tvGroupName;
		public TextView tvCount;
		public TextView tvPurpose;
		public TextView tvBday;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(entryResID, null);
			
			holder = new ViewHolder();
			holder.tvGroupName = (TextView) convertView.findViewById(R.id.tv_groupname);
			holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
			holder.tvPurpose = (TextView) convertView.findViewById(R.id.tv_purpose);
			holder.tvBday = (TextView) convertView.findViewById(R.id.tv_bday);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Pig pig = pigList.get(position);
		holder.tvGroupName.setText(pig.getGroupName());
		holder.tvCount.setText("" + pig.count);
		holder.tvPurpose.setText(pig.getPurpose());
		holder.tvBday.setText(pig.getBirthDate());
		
		return convertView;
	}

}
