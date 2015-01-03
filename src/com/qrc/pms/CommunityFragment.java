package com.qrc.pms;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;
import com.qrc.pms.R;
import com.qrc.pms.adapter.NavDrawerListAdapter;
import com.qrc.pms.adapter.TopFiveListEntryAdapter;
import com.qrc.pms.model.TopFiveEntryList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

//edited
public class CommunityFragment extends SherlockFragment {
	
	public CommunityFragment(){}
	

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);     
        return rootView;
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		DownloadUpadatesTask download = new DownloadUpadatesTask(getActivity());
		download.execute("acheche");

	}
	
	
	
}
