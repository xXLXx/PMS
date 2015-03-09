package com.qrc.pms;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.actionbarsherlock.app.SherlockFragment;
import com.qrc.pms.config.Config;
import com.qrc.pms.helper.FileHelper;
import com.qrc.pms.model.Pig;
import com.qrc.pms.utils.InputFilterMinMax;

//edited
public class WhatsHotFragment extends SherlockFragment {
	public WhatsHotFragment(){}
	
	public Spinner spinner_purpose;
	public EditText num_of_pigs, group_name;
	public Calendar date_birth;
	public Button add_pigs;
	public AlertDialog alertErroraddPig;
	private CheckBox checkboxAsGroup;
	Boolean isInternetPresent = false;
	Integer purpose, birthDate, count;
	String groupName;
	

	DatePicker datePicker;
	TimePicker timePicker;
	long datetimeBirth;

	Pig pigAddedDetails;
	
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
		datePicker = (DatePicker) view.findViewById(R.id.datePicker1);
		timePicker = (TimePicker) view.findViewById(R.id.alarm_morning);
		num_of_pigs.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "" + Integer.MAX_VALUE)});
		checkboxAsGroup = (CheckBox) view.findViewById(R.id.checkbox_as_group);
		
		final String defaultGroupName = "Pig Added " + Pig.formatDate(System.currentTimeMillis());
		group_name.setHint(defaultGroupName);
		
		spinner_purpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				if(arg2 == 2) {
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
			
		  add_pigs.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(num_of_pigs.getEditableText().toString().equals("")){
					((MainActivity) getActivity()).showAlertDialog(getActivity(),"Error", 
							"Please Fill in all the Fields", true, false, "OK", "",
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									
								}
							},
							null, null);
				} else {
					((MainActivity) getActivity()).showAlertDialog(getActivity(),"Confirmation", 
							"Are you sure you want to add this Pig?", true, true, "Cancel", "Add",
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									
								}
							},
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									if (group_name.getEditableText().toString().equals("")) {
										group_name.setText(defaultGroupName);
									}
									addPigDetails(checkboxAsGroup.isChecked());
								}
							}, null);
					
				}
				
			
			}
		});
		
	}
	
	
	public void  addPigDetails(Boolean asGroup) {
		
		date_birth = Calendar.getInstance();
		
		Log.e("", "" + datePicker.getYear());
		Log.e("", "" + datePicker.getMonth());
		Log.e("", "" + datePicker.getDayOfMonth());
		Log.e("", "" + timePicker.getCurrentHour());
		Log.e("", "" + timePicker.getCurrentMinute());
		
		date_birth.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), 
	             timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
		datetimeBirth = (long) date_birth.getTimeInMillis() / 1000 ;
		
		purpose = (int) spinner_purpose.getSelectedItemId();
		birthDate = (int) datetimeBirth;
		groupName = group_name.getEditableText().toString();
		count = Integer.parseInt(num_of_pigs.getEditableText().toString()); 
		
		Log.e("", "" + birthDate);
		
	    try {
	    	int loop = asGroup ? 1 : count;
	    	for (int x = 1; x <= loop; x++) {
		    	pigAddedDetails = new Pig(purpose, birthDate, groupName, asGroup ? count : 1);
				((MainActivity) getActivity()).openListPosition = ((MainActivity) getActivity()).addPig(pigAddedDetails);

	    		try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			((MainActivity) getActivity()).displayView(2);
			
			Thread thread =  new Thread(new Runnable() {
				
				@SuppressLint("SimpleDateFormat")
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
					File mediaFile = pigAddedDetails.getOutputMediaFile(timeStamp);
					
					while(mediaFile == null) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
									
					try {
						pigAddedDetails.saveQrCode(mediaFile);
						
						FileHelper.sendFiles(FileHelper.getLastFile(Config.ROOT_FOLDER), getActivity());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			thread.start();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		pigAddedDetails.getQrCodeBitmap();
	   
	}

}
