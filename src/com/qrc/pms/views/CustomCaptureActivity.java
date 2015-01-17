package com.qrc.pms.views;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.qrc.pms.R;
import com.qrc.pms.model.Pig;

public final class CustomCaptureActivity extends CaptureActivity{

	Pig pig;
	
	@Override
	public void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		setContentView(R.layout.custom_capture);
	}

	@Override
	public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		JSONObject data;
		try {
			data = new JSONObject(rawResult.getText());

			pig = new Pig(data.getInt("purpose"), data.getInt("birthdate"));
			pig.dateAdded = data.getLong("dateAdded");
			
			findViewById(R.id.table_qrdetails).setVisibility(View.VISIBLE);
			findViewById(R.id.tv_invalid_qr).setVisibility(View.GONE);
//			((TextView) findViewById(R.id.tv_groupname)).setText(pig.getGroupName());
			((TextView) findViewById(R.id.tv_detail_purpose)).setText(pig.getPurpose());
			((TextView) findViewById(R.id.tv_detail_dateadded)).setText(pig.getDateAdded());
			((TextView) findViewById(R.id.tv_detail_birthdate)).setText(pig.getBirthDate());
			((TextView) findViewById(R.id.tv_detail_age)).setText(pig.getAge());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			findViewById(R.id.table_qrdetails).setVisibility(View.GONE);
			findViewById(R.id.tv_invalid_qr).setVisibility(View.VISIBLE);
		}
		// TODO Auto-generated method stub
		super.handleDecode(rawResult, barcode, scaleFactor);
		
		findViewById(R.id.button_gone1).setVisibility(View.GONE);
		findViewById(R.id.button_gone2).setVisibility(View.GONE);
		findViewById(R.id.button_gone3).setVisibility(View.GONE);
		findViewById(R.id.button_gone4).setVisibility(View.GONE);
	}
	
	public void gotoDetails(View v) {
		Intent intent = new Intent();
		intent.putExtra("pigId", "" + pig.dateAdded);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
	
	public void cancel(View v) {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}
	
}
