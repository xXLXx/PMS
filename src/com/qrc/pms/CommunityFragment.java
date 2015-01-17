package com.qrc.pms;

import java.util.Calendar;

import org.json.JSONException;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.actionbarsherlock.app.SherlockFragment;
import com.qrc.pms.adapter.PigListAdapter;
import com.qrc.pms.model.Pig;
import com.qrc.pms.model.Pig.Feeds;
import com.qrc.pms.utils.InputFilterMinMax;

//edited
public class CommunityFragment extends SherlockFragment {
	
	private int openListPosition = -1;
	private View detailsModal;
	
	private TextView tvDetailCount, totalPig;
	private TextView tvGroupName;
	private Button btnPregnant;
	private TableRow pregnantCountRow;
	long setTotalPig;
	private int currentPigIdx = -1;
	
	public CommunityFragment() {}

	@Override
	public void setArguments(Bundle args) {
		this.openListPosition = args.getInt("openListPosition");
		// TODO Auto-generated method stub
		super.setArguments(args);
	}

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
		pregnantCountRow = (TableRow) detailsModal.findViewById(R.id.pregnant_count_row);
		totalPig = (TextView) view.findViewById(R.id.total_count);
		
		ListView pigListView = (ListView) view.findViewById(R.id.pig_listView);
		
		pigListView.setAdapter(((MainActivity) getActivity()).pigListAdapter);
		
		totalPig.setText(((MainActivity) getActivity()).pigListAdapter.getTotalPigCount());
		
		
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
				View view = getActivity().getLayoutInflater().inflate(R.layout.sell_form, null, false);
				((EditText)view.findViewById(R.id.et_sell_count)).setFilters(
						new InputFilter[]{
							new InputFilterMinMax("1", "" + ((MainActivity) getActivity()).pigListAdapter.getItem(currentPigIdx).count)
						});
				((MainActivity) getActivity()).showAlertDialog(
						getActivity(),
						"Sell Pigs",
						"",
						true,
						true,
						"Cancel",
						"Update",
						null,
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								Pig pig = ((MainActivity) getActivity()).pigListAdapter.getItem(currentPigIdx);
								pig.count = pig.count - Integer.parseInt(((EditText)((Dialog)arg0).findViewById(R.id.et_sell_count)).getEditableText().toString());
								
								try {
									((MainActivity) getActivity()).updatePig(currentPigIdx, pig);

									showDetailsModal(currentPigIdx);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						},
						view);
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
						null,
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								Calendar date_birth = Calendar.getInstance();
								DatePicker datePicker = (DatePicker) ((Dialog)arg0).findViewById(R.id.extradate_datepicker);
								TimePicker timePicker = (TimePicker) ((Dialog)arg0).findViewById(R.id.extradate_timepicker);
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
								
								showDetailsModal(currentPigIdx);
							}
						},
						getActivity().getLayoutInflater().inflate(R.layout.extradate_picker, null, false));
			}
		});
		
		if (openListPosition != -1) {
			showDetailsModal(openListPosition);
			openListPosition = -1;
			((MainActivity) getActivity()).pigListAdapter.sort();
		}
	}
	
	public void showDetailsModal(int position) {
		if (position == -1) {
			position = currentPigIdx;
		}
		
		detailsModal.setVisibility(View.VISIBLE);
		Pig pig = ((MainActivity) getActivity()).pigListAdapter.getItem(currentPigIdx = position);
		((TextView) detailsModal.findViewById(R.id.tv_detail_groupname)).setText(pig.getGroupName());
		tvDetailCount.setText("" + pig.count);
		((TextView) detailsModal.findViewById(R.id.tv_detail_birthdate)).setText(pig.getBirthDate());
		((TextView) detailsModal.findViewById(R.id.tv_detail_age)).setText(pig.getAge());
		((TextView) detailsModal.findViewById(R.id.tv_detail_dateadded)).setText(pig.getDateAdded());
		((TextView) detailsModal.findViewById(R.id.tv_detail_purpose)).setText(pig.getPurpose());
		Feeds feeds = pig.getFeeds();
		((TextView) detailsModal.findViewById(R.id.tv_feeds)).setText(feeds == null ? "" : feeds.feed);
		((TextView) detailsModal.findViewById(R.id.tv_feeds_consumption)).setText(feeds == null ? "" : feeds.consumption);
		((ImageView) detailsModal.findViewById(R.id.img_qrcode)).setImageBitmap(pig.getQrCodeBitmap());
		
		boolean btnSellEnabled = true;
		if (pig.count <= 0) {
			btnSellEnabled = false;
		}
		((Button) detailsModal.findViewById(R.id.btn_sell)).setEnabled(btnSellEnabled);
			
		String extraDate = "";
		TextView tvExtraDate = ((TextView) detailsModal.findViewById(R.id.tv_detail_extradate));
		TextView tvExtraDateLead = ((TextView) detailsModal.findViewById(R.id.tv_detaillead_extradate));
		tvExtraDate.setVisibility(View.VISIBLE);
		tvExtraDateLead.setVisibility(View.VISIBLE);
		
		if (pig.purpose == Pig.PURPOSE_SOW) {
			btnPregnant.setVisibility(View.VISIBLE);
			btnPregnant.setText(getResources().getString(R.string.pregnant));
			
			tvDetailCount.setVisibility(View.VISIBLE);
			pregnantCountRow.setVisibility(View.VISIBLE);
			((TextView) detailsModal.findViewById(R.id.tv_pregnancy_count)).setText("" + pig.pregnancyCount);
		} else {
			btnPregnant.setVisibility(View.GONE);
			pregnantCountRow.setVisibility(View.GONE);
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
		
		if(!((MainActivity) getActivity()).isAdmin){
			((Button) detailsModal.findViewById(R.id.btn_sell)).setVisibility(View.GONE);
			((Button) detailsModal.findViewById(R.id.btn_pregnant)).setVisibility(View.GONE);
		} else if(pig.purpose == Pig.PURPOSE_SOW && ((MainActivity) getActivity()).isAdmin) {
			((Button) detailsModal.findViewById(R.id.btn_sell)).setVisibility(View.VISIBLE);
			((Button) detailsModal.findViewById(R.id.btn_pregnant)).setVisibility(View.VISIBLE);
		}
	}
	
	
}
