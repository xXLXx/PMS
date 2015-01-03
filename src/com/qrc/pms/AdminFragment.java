package com.qrc.pms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;

//edited
public class AdminFragment extends SherlockFragment {
	
	
	public View view_list_fraghome;
	RelativeLayout layout;
	
	
	public AdminFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		view_list_fraghome = inflater.inflate(R.layout.fragment_home, container, false);
		return view_list_fraghome;
    }
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		
	
	}
	

}

