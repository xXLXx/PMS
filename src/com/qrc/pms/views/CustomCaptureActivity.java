package com.qrc.pms.views;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.qrc.pms.R;
import com.qrc.pms.model.Pig;

public final class CustomCaptureActivity extends CaptureActivity{

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
			data = new JSONObject(rawResult.getBarcodeFormat().toString());

			Pig pig = new Pig(data.getInt("purpose"), data.getInt("birthdate"), data.getString("groupName"));
			
			((TextView) findViewById(R.id.tv_groupname)).setText(pig.getGroupName());
			((TextView) findViewById(R.id.tv_detail_purpose)).setText(pig.getPurpose());
			((TextView) findViewById(R.id.tv_detail_dateadded)).setText(pig.getDateAdded());
			((TextView) findViewById(R.id.tv_detail_birthdate)).setText(pig.getBirthDate());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		super.handleDecode(rawResult, barcode, scaleFactor);
	}
	
	
}
