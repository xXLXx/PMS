package com.qrc.pms;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.SessionLoginBehavior;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

//edited
public class FindPeopleFragment extends SherlockFragment {
	public View rootView;
	
	public FindPeopleFragment(){}
	private UiLifecycleHelper uLh;
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
		 LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
	        authButton.setFragment(this);
	        authButton.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
//	        authButton.setReadPermissions(Arrays.asList("user_likes", "user_status"));
	        
	        
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		uLh = ((MainActivity) getActivity()).uiHelper;

		uLh = new UiLifecycleHelper(getActivity(), ((MainActivity)getActivity()).callback);
		uLh.onCreate(savedInstanceState);
	}
	
		
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uLh.onActivityResult(requestCode, resultCode, data);
	}
	

	
}
