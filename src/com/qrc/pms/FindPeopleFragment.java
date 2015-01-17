package com.qrc.pms;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.SessionLoginBehavior;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.google.zxing.Result;

//edited
public class FindPeopleFragment extends SherlockFragment {
	public View rootView;
	
	public FindPeopleFragment(){}
	private UiLifecycleHelper uLh;
	public Button btn_localLogin;
	public String userCode = "abc123";
	public EditText inputUserCode;
	
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
		 btn_localLogin = (Button) view.findViewById(R.id.btn_localLogin);
		 inputUserCode = (EditText) view.findViewById(R.id.txt_userCode);
		 
	        authButton.setFragment(this);
	        authButton.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
//	        authButton.setReadPermissions(Arrays.asList("user_likes", "user_status"));
	        
	        btn_localLogin.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					if(inputUserCode.getEditableText().toString().equals("")){
						
						((MainActivity)getActivity()).showAlertDialog(getActivity(), "Error", 
								"User Code Required", true, false, "OK", 
								"", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub
										
									}
								}, null, null);
					} else if (inputUserCode.getEditableText().toString().equals(userCode)) {
						
						 ((MainActivity) getActivity()).isAdmin = true;
						 ((MainActivity) getActivity()).displayView(0);
						 ((MainActivity) getActivity()).isLoogedIn = true;
						  
					} else if(inputUserCode.getEditableText().toString() != "" && 
							inputUserCode.getEditableText().toString() != userCode){
								
						((MainActivity)getActivity()).showAlertDialog(getActivity(), "Error", 
								"Invalid user code!", true, false, "OK", 
								"", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub
										
									}
								}, null, null);
					}
				}
			});
	        
	        
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
