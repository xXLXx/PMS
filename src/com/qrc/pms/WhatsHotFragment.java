package com.qrc.pms;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragment;
import com.qrc.pms.utils.InputFilterMinMax;

//edited
public class WhatsHotFragment extends SherlockFragment {
	public WhatsHotFragment(){}
	
	public Spinner spinner_purpose;
	public EditText num_of_pigs, group_name;
	public Button add_pigs;
	public AlertDialog alertErroraddPig;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_whats_hot, container, false);
        return rootView;
    }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		spinner_purpose = (Spinner) view.findViewById(R.id.spinner_add_purpose);
		num_of_pigs = (EditText) view.findViewById(R.id.input_add_noof_pigs);
		add_pigs = (Button) view.findViewById(R.id.btn_add_pigs);
		group_name = (EditText) view.findViewById(R.id.input_add_group_name);
		
		num_of_pigs.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "" + Integer.MAX_VALUE)});
		
		spinner_purpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				if(arg2 == 1) {
					num_of_pigs.setEnabled(false);
					num_of_pigs.setText("" + 1);
				}else {
					num_of_pigs.setEnabled(true);
					num_of_pigs.setText("");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		  add_pigs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(num_of_pigs.getEditableText().toString().equals("") ||
						group_name.getEditableText().toString().equals("")){
					((MainActivity) getActivity()).showAlertDialog(getActivity(),"Error", 
								"Please Fill in all the Fields", true, false, "OK", "");
				} else {
					((MainActivity) getActivity()).showAlertDialog(getActivity(),"Confirmation", 
							"Are you sure you want to add this Pig?", true, true, "Add", "Cancel");

				}
				
			
			}
		});
		
	}
	
	

}
