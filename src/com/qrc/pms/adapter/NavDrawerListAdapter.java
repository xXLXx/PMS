package com.qrc.pms.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qrc.pms.MainActivity;
import com.qrc.pms.R;
import com.qrc.pms.model.NavDrawerItem;

public class NavDrawerListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	
	private int[] blackListPos = {3};
	
	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size() - (!((MainActivity) context).isAdmin &&
				!((MainActivity) context).isLoogedIn ? blackListPos.length : 0);
	}

	@Override
	public Object getItem(int position) {		
		return !((MainActivity) context).isAdmin && !((MainActivity) context).isLoogedIn &&
				inBlackList(position) ? null : navDrawerItems.get(position);
	}
	
	private boolean inBlackList(int position) {
		for (int pos : blackListPos) {
			if (position == pos) return true;
		}
		return false;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (!((MainActivity) context).isAdmin && inBlackList(position) && !((MainActivity) context).isLoogedIn) 
			return null;
		
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
         
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
//        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
         
        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());        
        txtTitle.setText(navDrawerItems.get(position).getTitle());
        
        // displaying count
        // check whether it set visible or not
        if(navDrawerItems.get(position).getCounterVisibility()){
//        	txtCount.setText(navDrawerItems.get(position).getCount());
        }else{
        	// hide the counter view
//        	txtCount.setVisibility(View.GONE);
        }
        if(((MainActivity) context).isAdmin && position == 1 && ((MainActivity) context).isLoogedIn)
        {
        	txtTitle.setText("Log Out");
        }
        return convertView;
	}
	

}
