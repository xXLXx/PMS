package com.qrc.pms.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.qrc.pms.MainActivity;
import com.qrc.pms.R;
import com.qrc.pms.config.Config;

public class NotifierService extends Service{
	private final IBinder mBinder = new NotifierBinder();
	public boolean cancel = false;
	
	private NotificationCompat.Builder builder;
	private NotificationManager notificationManager;
	
	private SoundPool soundPool = null;
	private int snd_alarm = -1;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		
		builder = new NotificationCompat.Builder(this)
	        .setContentTitle("PMS QR Code")
	        .setContentText("")
	        .setSmallIcon(R.drawable.piggy_bank_logo)
	        .setContentIntent(pIntent)
	        .setAutoCancel(true);
		

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
				Date nextTime = new Date();
				nextTime.setMinutes(nextTime.getMinutes() + 1);
				nextTime.setSeconds(0);
				
				long timeDifference = nextTime.getTime() - System.currentTimeMillis();
				try {
					Thread.sleep(timeDifference);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				alarm();
				while (!cancel) {
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					alarm();
				}
				return null;
			}
			
		}.execute();
//		showNotification();
		
		return Service.START_NOT_STICKY;
	}
	
	private void alarm() {
		Log.e("TIMESLOTS", Config.TIME_SLOTS[0] + " " + Config.TIME_SLOTS[1] + " " + Config.TIME_SLOTS[2]);
		if (inWhiteList()) {
			Date date = new Date();
			String timeNow = new SimpleDateFormat("hh:mm a").format(date);
			
			builder.setContentText("It's " + timeNow + "! Your pigs require feeding.");
			notificationManager.notify(0, builder.build());
			soundPool.play(snd_alarm, 1f, 1f, 1, 0, 1f);
		}
	}
	
	private boolean inWhiteList() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		String dateNow = dateFormat.format(new Date());
		
		for (String item : Config.TIME_SLOTS) {
			if (item.equals(dateNow)) {
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
