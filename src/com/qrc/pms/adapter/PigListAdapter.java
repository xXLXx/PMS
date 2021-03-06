package com.qrc.pms.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qrc.pms.MainActivity;
import com.qrc.pms.R;
import com.qrc.pms.config.Config;
import com.qrc.pms.model.Log;
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
		sort();
	}
	
	public void sort() {
		Collections.sort(pigList, new Comparator<Pig>() {

			@Override
			public int compare(Pig lhs, Pig rhs) {
				// TODO Auto-generated method stub
				final int sortOrder = ((MainActivity) context).sortOrder;
				switch (sortOrder) {
				case R.id.radio_birthdate:
					return (int) (rhs.birthDate - lhs.birthDate);
				case R.id.radio_purpose:
					return (int) (rhs.purpose - lhs.purpose);	
				default:
					return (int) (rhs.dateAdded - lhs.dateAdded);
				}
			}
		});
		notifyDataSetChanged();
	}
	
	public String getTotalPigCount(){
		long totalPigcount= 0;
		long soldCount = 0;
		
		for (Pig pig : pigList) {
			if(soldCount <= 0){
				soldCount++;			
			}
			totalPigcount += pig.getCount();
		}
				
	  return "" + totalPigcount;
	}
	

	public int getPosition(String id) {
		for (int x = 0; x < getCount(); x++) {
			Pig pig = getItem(x);
			if (pig.getId().equals(id)) {
				return x;
			}
		}
		return -1;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pigList.size();
	}
	
	public void clearAll() {
		pigList.clear();
		notifyDataSetChanged();
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
	
	public void add(Pig object) {
		pigList.add(object);
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		public TextView tvGroupName;
		public TextView tvCount;
		public TextView tvPurpose;
		public TextView tvBday;
		public TextView tvSold;
		public View listEntryParent;
		public View listEntryBase;
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
			holder.tvSold = (TextView) convertView.findViewById(R.id.tv_sold);
			holder.listEntryParent = convertView.findViewById(R.id.listentry_parent);
			holder.listEntryBase = convertView.findViewById(R.id.listentry_base);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Pig pig = pigList.get(position);
		
//		if (!((MainActivity) context).isAdmin && pig.getCount() == 0) {
//			return null;
//		}
		
		holder.tvGroupName.setText(pig.getGroupName());
		holder.tvCount.setText("" + pig.getCount());
		holder.tvPurpose.setText(pig.getPurpose());
		holder.tvBday.setText(pig.getBirthDate());
		
		if (pig.getCount() <= 0) {
			holder.listEntryParent.setBackgroundColor(context.getResources().getColor(R.color.result_view));
			holder.tvSold.setVisibility(View.VISIBLE);
			if (pig.count == pig.removed) {
				holder.tvSold.setText(context.getResources().getString(R.string.removed));
			} else {
				holder.tvSold.setText(context.getResources().getString(R.string.sold));
			}
		} else {
			holder.listEntryParent.setBackgroundDrawable(null);
			holder.tvSold.setVisibility(View.GONE);
		}
		
		int nowSec = (int) (System.currentTimeMillis() / 1000);
		if (nowSec >= pig.pregnancyDate && nowSec - pig.pregnancyDate <= 86400 * 3) {
			holder.listEntryBase.setBackgroundColor(Color.parseColor("#74d58512"));
		} else if (!pig.getVaccine(context).equals("")) {
			holder.listEntryBase.setBackgroundColor(Color.parseColor("#74D95082"));
		} else if (pig.hasFeedsNotification(context)) {
			holder.listEntryBase.setBackgroundColor(Color.parseColor("#74D95082"));
		} else {
			holder.listEntryBase.setBackgroundDrawable(null);
		}
		
		return convertView;
	}

}
