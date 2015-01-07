package com.qrc.pms.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class NotifierService extends Service{
	private final IBinder mBinder = new NotifierBinder();
	public boolean cancel = false;
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.e("run", "asd");
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				while (!cancel) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.e("asd", "try");
				}
				return null;
			}
			
		}.execute();
//		showNotification();
		
		return Service.START_NOT_STICKY;
	}
	
	private void showNotification() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Not yet");
        alert.setTitle("Error");
        alert.setPositiveButton("OK", null);
        alert.create().show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	public class NotifierBinder extends Binder {
		public NotifierService getService() {
			return NotifierService.this;
		}
	}

}
