package com.qrc.pms.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
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

public class LogsListAdapter extends BaseAdapter{
	List<Log> logsList;
	Context context;
	int entryResID = R.layout.logs_listentry;
	
	public LogsListAdapter(Context context, ArrayList<Log> logsList) {
		super();
		// TODO Auto-generated constructor stub
		this.context = context;
		this.logsList = logsList;
	}
	
	public void init(String pigId, int logType, View view) {
		SharedPreferences prefs = (SharedPreferences) context.getSharedPreferences(
				(logType == Log.TYPE_FEEDS ? Config.PREF_NAME_FEEDS_LOGS : Config.PREF_NAME_VACCINE_LOGS) + pigId, Context.MODE_PRIVATE);
		Map<String, ?> prefsMap = prefs.getAll();
		for (Object obj : prefsMap.values()) {
			logsList.add(Log.getLog(obj.toString()));
		}
		
		if (getCount() <= 0) {
			view.findViewById(R.id.tv_empty).setVisibility(View.VISIBLE);
			view.findViewById(R.id.logs_listView).setVisibility(View.GONE);
		}
		
		Collections.sort(logsList, new Comparator<Log>() {

			@Override
			public int compare(Log lhs, Log rhs) {
				// TODO Auto-generated method stub
				return rhs.getId().compareTo(lhs.getId());
			}
		});
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return logsList.size();
	}

	@Override
	public Log getItem(int arg0) {
		// TODO Auto-generated method stub
		return logsList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public void add(Log object) {
		logsList.add(object);
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		public TextView tvDescription;
		public TextView tvNotes;
		public TextView tvDate;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder;
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(entryResID, null);
			
			holder = new ViewHolder();
			holder.tvDescription = (TextView) convertView.findViewById(R.id.tv_description);
			holder.tvNotes = (TextView) convertView.findViewById(R.id.tv_notes);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Log log = logsList.get(position);
		
//		if (!((MainActivity) context).isAdmin && pig.getCount() == 0) {
//			return null;
//		}
		
		holder.tvDescription.setText(log.description);
		holder.tvNotes.setText(log.notes);
		holder.tvDate.setText(log.getDate());
		
		if (log.notes.equals("")) {
			holder.tvNotes.setVisibility(View.GONE);
		}
		
		return convertView;
	}

}
