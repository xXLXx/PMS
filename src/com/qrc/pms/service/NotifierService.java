package com.qrc.pms.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.qrc.pms.MainActivity;
import com.qrc.pms.R;
import com.qrc.pms.config.Config;

public class NotifierService extends Service{
	private final IBinder mBinder = new NotifierBinder();
	public boolean cancel = false;
	
	private Notification n;
	private NotificationManager notificationManager;
	
	private SoundPool soundPool = null;
	private int snd_alarm = -1;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		
		Calendar c = Calendar.getInstance();
		String timeNow = new SimpleDateFormat("hh:mm a").format(c.getTime());
		
		n = new NotificationCompat.Builder(this)
	        .setContentTitle("PMS QR Code")
	        .setContentText("It's " + timeNow + "! Your pigs require feeding.")
	        .setSmallIcon(R.drawable.piggy_bank_logo)
	        .setContentIntent(pIntent)
	        .setAutoCancel(true)
	        .build();
		

		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
		
		soundPool = new SoundPool(10, AudioManager.STREAM_ALARM, 0);
		snd_alarm = soundPool.load(this, R.raw.request_alarm, 1);
		
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				while (!cancel) {
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (inWhiteList()) {
						notificationManager.notify(0, n);
						soundPool.play(snd_alarm, 1f, 1f, 1, 0, 1f);
					}
				}
				return null;
			}
			
		}.execute();
//		showNotification();
		
		return Service.START_NOT_STICKY;
	}
	
	private boolean inWhiteList() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
		String dateNow = dateFormat.format(new Date());
		
		for (int item : Config.TIME_SLOTS) {
			if (item == Integer.parseInt(dateNow)) {
				return true;
			}
		}
		return false;
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
