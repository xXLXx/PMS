package com.qrc.pms.views;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.qrc.pms.R;

public final class CustomCaptureActivity extends CaptureActivity{

	@Override
	public void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		setContentView(R.layout.custom_capture);
	}

	@Override
	public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		// TODO Auto-generated method stub
		super.handleDecode(rawResult, barcode, scaleFactor);
		
		
	}
	
	
}
