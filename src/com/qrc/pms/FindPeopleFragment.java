package com.qrc.pms;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.FbDialog;
import com.facebook.widget.LoginButton;
import com.qrc.pms.model.NavDrawerItem;

//edited
public class FindPeopleFragment extends SherlockFragment {
	public View rootView;
	private UiLifecycleHelper uiHelper;
	
	private static final String TAG = "FindPeopleFragment";
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
		
		 LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
	        authButton.setFragment(this);
	        authButton.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
//	        authButton.setReadPermissions(Arrays.asList("user_likes", "user_status"));
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 uiHelper = new UiLifecycleHelper(getActivity(), callback);
		 uiHelper.onCreate(savedInstanceState);

	}
	

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.e(TAG, "Logged in...");
	        ((MainActivity) getActivity()).displayView(0);
	        ((MainActivity) getActivity()).isAdmin = true;
		    
	    } else if (state.isClosed()) {
	     
	    }
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onResume() {
	    super.onResume();
	    
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    
	    uiHelper.onResume();
	    
	   
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
    
	
}
