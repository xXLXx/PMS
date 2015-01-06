package com.qrc.pms;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.qrc.pms.adapter.PigListAdapter;
import com.qrc.pms.model.Pig;

//edited
public class CommunityFragment extends SherlockFragment {
	
	public CommunityFragment(){}
	private View detailsModal;
	
	private TextView tvDetailCount;
	private TextView tvDetailPurpose;
	private EditText etDetailCount;
	private Spinner spinnerDetailPurpose;
	
	private int currentPigIdx = -1;
	private PigListAdapter pigListAdapter;

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
		tvDetailPurpose = (TextView) detailsModal.findViewById(R.id.tv_detail_purpose);
		etDetailCount = (EditText) detailsModal.findViewById(R.id.et_detail_count);
		spinnerDetailPurpose = (Spinner) detailsModal.findViewById(R.id.spinner_detail_purpose);
		
		ListView pigListView = (ListView) view.findViewById(R.id.pig_listView);
		final ArrayList<Pig> pigList = new ArrayList<Pig>();
		for (int x = 0; x < 30; x++) {
			pigList.add(new Pig(x % 4 + 1, (int) (System.currentTimeMillis() / 1000 + 86400 * x), x < 10 ? x + " Little Piggies" : "", x));
		}
		pigListAdapter = new PigListAdapter(view.getContext(), pigList);
		pigListView.setAdapter(pigListAdapter);
		pigListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				detailsModal.setVisibility(View.VISIBLE);
				Pig pig = pigList.get(currentPigIdx = arg2);
				((TextView) detailsModal.findViewById(R.id.tv_detail_groupname)).setText(pig.getGroupName());
				tvDetailCount.setText("" + pig.count);
				tvDetailPurpose.setText(pig.getPurpose());
				((TextView) detailsModal.findViewById(R.id.tv_detail_birthdate)).setText(pig.getBirthDate());
				((TextView) detailsModal.findViewById(R.id.tv_detail_dateadded)).setText(pig.getDateAdded());
				
				String extraDate = "";
				TextView tvExtraDate = ((TextView) detailsModal.findViewById(R.id.tv_detail_extradate));
				TextView tvExtraDateLead = ((TextView) detailsModal.findViewById(R.id.tv_detaillead_extradate));
				tvExtraDate.setVisibility(View.VISIBLE);
				tvExtraDateLead.setVisibility(View.VISIBLE);
				if (!(extraDate = pig.getPregnancyDate()).equals("")) {
					tvExtraDate.setText(extraDate);
					tvExtraDateLead.setText(getResources().getString(R.string.date_pregnancy));
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
					pig.purpose = spinnerDetailPurpose.getSelectedItemPosition();
					pigListAdapter.set(currentPigIdx, pig);
				} else {
					tvDetailCount.setVisibility(View.GONE);
					etDetailCount.setText(tvDetailCount.getText());
					etDetailCount.setVisibility(View.VISIBLE);
					tvDetailPurpose.setVisibility(View.GONE);
					spinnerDetailPurpose.setVisibility(View.VISIBLE);
					spinnerDetailPurpose.setSelection(pigListAdapter.getItem(currentPigIdx).purpose);
					((Button) arg0).setText(getResources().getString(R.string.save));
				}
			}
		});
	}
	
	private void resetDetailsModal() {
		detailsModal.setVisibility(View.GONE);
		etDetailCount.setVisibility(View.GONE);
		tvDetailCount.setVisibility(View.VISIBLE);
		spinnerDetailPurpose.setVisibility(View.GONE);
		tvDetailPurpose.setVisibility(View.VISIBLE);
		((Button) detailsModal.findViewById(R.id.btn_edit)).setText(getResources().getString(R.string.edit));
	}
	
	
	
}
