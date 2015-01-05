package com.qrc.pms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;	
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.Session;

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;

//edited
public class FindPeopleFragment extends SherlockFragment {
//	private GalleryViewPager mViewPager;
	
	private static final String TAG = null;
	public static Button connect_to_facebook;
	public View rootView;
	
	public FindPeopleFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
         rootView = inflater.inflate(R.layout.fragment_find_people, container, false);
        return rootView;
    }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	   
//		connect_to_facebook = (LoginButton) view.findViewById(R.id.authButton);
//		
//		
//		connect_to_facebook.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				
//
//			}
//		});

	}
	



	
}
