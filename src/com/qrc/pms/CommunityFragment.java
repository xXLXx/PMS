package com.qrc.pms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONException;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.actionbarsherlock.app.SherlockFragment;
import com.qrc.pms.adapter.PigListAdapter;
import com.qrc.pms.model.Pig;
import com.qrc.pms.model.Pig.Feeds;

//edited
public class CommunityFragment extends SherlockFragment {
	
	public CommunityFragment(){}
	private View detailsModal;
	
	private TextView tvDetailCount;
	private TextView tvGroupName;
	private Button btnPregnant;
	
	private int currentPigIdx = -1;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);     
        return rootView;
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		detailsModal = view.findViewById(R.id.details_modal);

		tvDetailCount = (TextView) detailsModal.findViewById(R.id.tv_detail_count);
		tvGroupName = (TextView) detailsModal.findViewById(R.id.tv_detail_groupname);
		btnPregnant = (Button) detailsModal.findViewById(R.id.btn_pregnant);
		
		ListView pigListView = (ListView) view.findViewById(R.id.pig_listView);
		
		pigListView.setAdapter(((MainActivity) getActivity()).pigListAdapter);
		pigListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				showDetailsModal(arg2);
			}
		});
		
		((Button) view.findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				detailsModal.setVisibility(View.GONE);
			}
		});
		
		((Button) view.findViewById(R.id.btn_sell)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		((Button) view.findViewById(R.id.btn_pregnant)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String title = getResources().getString(R.string.date_giving_birth);
				if (((TextView) arg0).getText().equals(getResources().getString(R.string.pregnant))) {
					title = getResources().getString(R.string.date_pregnancy);
				}
				
				((MainActivity) getActivity()).showAlertDialog(
						getActivity(),
						title,
						"",
						true,
						true,
						"Cancel",
						"Update",
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								
							}
						},
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								Calendar date_birth = Calendar.getInstance();
								DatePicker datePicker = (DatePicker) getActivity().findViewById(R.id.extradate_datepicker);
								TimePicker timePicker = (TimePicker) getActivity().findViewById(R.id.extradate_timepicker);
								date_birth.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), 
							             timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
								
								Pig pig = ((MainActivity) getActivity()).pigListAdapter.getItem(currentPigIdx);
								pig.setExtraDate((int) (date_birth.getTimeInMillis() / 1000));
								try {
									((MainActivity) getActivity()).updatePig(currentPigIdx, pig);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						},
						getActivity().getLayoutInflater().inflate(R.layout.extradate_picker, null, false));
			}
		});
	}
	
	private void showDetailsModal(int position) {
				detailsModal.setVisibility(View.VISIBLE);
		Pig pig = ((MainActivity) getActivity()).pigListAdapter.getItem(currentPigIdx = position);
				((TextView) detailsModal.findViewById(R.id.tv_detail_groupname)).setText(pig.getGroupName());
				tvDetailCount.setText("" + pig.count);
				((TextView) detailsModal.findViewById(R.id.tv_detail_birthdate)).setText(pig.getBirthDate());
				((TextView) detailsModal.findViewById(R.id.tv_detail_dateadded)).setText(pig.getDateAdded());
		((TextView) detailsModal.findViewById(R.id.tv_detail_purpose)).setText(pig.getPurpose());
		Feeds feeds = pig.getFeeds();
		((TextView) detailsModal.findViewById(R.id.tv_feeds)).setText(feeds == null ? "" : feeds.feed);
		((TextView) detailsModal.findViewById(R.id.tv_feeds_consumption)).setText(feeds == null ? "" : feeds.consumption);
		((ImageView) detailsModal.findViewById(R.id.img_qrcode)).setImageBitmap(pig.getQrCodeBitmap());
				
				String extraDate = "";
				TextView tvExtraDate = ((TextView) detailsModal.findViewById(R.id.tv_detail_extradate));
				TextView tvExtraDateLead = ((TextView) detailsModal.findViewById(R.id.tv_detaillead_extradate));
				tvExtraDate.setVisibility(View.VISIBLE);
				tvExtraDateLead.setVisibility(View.VISIBLE);
		
		if (pig.purpose == Pig.PURPOSE_SOW) {
			btnPregnant.setVisibility(View.VISIBLE);
			btnPregnant.setText(getResources().getString(R.string.pregnant));
		} else {
			btnPregnant.setVisibility(View.GONE);
		}
		
				if (!(extraDate = pig.getPregnancyDate()).equals("")) {
					tvExtraDate.setText(extraDate);
					tvExtraDateLead.setText(getResources().getString(R.string.date_pregnancy));
			btnPregnant.setText(getResources().getString(R.string.gave_birth));
				} else if (!(extraDate = pig.getMilkingDate()).equals("")) {
					tvExtraDate.setText(extraDate);
					tvExtraDateLead.setText(getResources().getString(R.string.date_giving_birth));
				} else {
					tvExtraDate.setVisibility(View.GONE);
					tvExtraDateLead.setVisibility(View.GONE);
				}
			}
		});
		
		((Button) view.findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				resetDetailsModal();
			}
		});
		
		((Button) view.findViewById(R.id.btn_edit)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (((Button) arg0).getText().equals(getResources().getString(R.string.save))) {
					resetDetailsModal();
					Pig pig = pigListAdapter.getItem(currentPigIdx);
					pig.count = Integer.parseInt(etDetailCount.getText().toString());
					pigListAdapter.set(currentPigIdx, pig);
				} else {
					tvDetailCount.setVisibility(View.GONE);
					etDetailCount.setText(tvDetailCount.getText());
					etDetailCount.setVisibility(View.VISIBLE);
					tvDetailPurpose.setVisibility(View.GONE);
					((Button) arg0).setText(getResources().getString(R.string.save));
				}
			}
		});
	}
	
	private void resetDetailsModal() {
		detailsModal.setVisibility(View.GONE);
		etDetailCount.setVisibility(View.GONE);
		tvDetailCount.setVisibility(View.VISIBLE);
		tvDetailPurpose.setVisibility(View.VISIBLE);
		((Button) detailsModal.findViewById(R.id.btn_edit)).setText(getResources().getString(R.string.edit));
	}
	
	
	
}
