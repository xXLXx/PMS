package com.qrc.pms;

import java.io.File;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlSerializer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.qrc.pms.R;
import com.qrc.pms.config.Config;
import com.qrc.pms.helper.FileHelper;
import com.qrc.pms.model.DataEntryList;
import com.qrc.pms.views.CameraView;

@SuppressWarnings("deprecation")
public class CameraActivity extends Activity{
	
	private Camera mCamera;
	private CameraView mCameraView;
	SlidingDrawer sLidingDrawer;
	static FrameLayout frame_bg_buttoncam;
	FrameLayout frame_cam_preview;
	Button captureButton, uploadBtn, cancelbtn;
	Boolean isInternetPresent = false;
	ConnectionDetector conDetect;
	byte image[];
	String FileDetails;
	View parentView;
	View parent;
	boolean isDrawerOpened = false;
	ProgressDialog dialog = null;
//	private LocationManager locMngr;

	boolean isNetworkEnabled = false;
	boolean isGPSEnabled = false;
	boolean canGetLocation = false;
	boolean isPictureTaken = false;
	Location location;
	TextView latLng_view, date_view;
	DataEntryList data = new DataEntryList();
	TextView addressCustom;
	EditText comments;
	Camera camera;
	
	 
	static CountDownTimer hideAnimTimer;
	static boolean isCameraFrameHidden = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		mCamera = getCameraInstance();
		mCameraView = new CameraView(this, mCamera);	
//		setRequestedOrientation(Configuration.ORIENTATION_UNDEFINED);
		frame_cam_preview =  (FrameLayout) findViewById(R.id.activity_camera_preview);
		frame_cam_preview.addView(mCameraView);		
		frame_bg_buttoncam = (FrameLayout) findViewById(R.id.camera_bg);
		captureButton = (Button) findViewById(R.id.button_capture);
		cancelbtn= (Button) findViewById(R.id.btn_cancel);
		sLidingDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
		parent = (View) findViewById(R.id.camera_parent_preview);
		sLidingDrawer.setVisibility(View.INVISIBLE);
		conDetect = new ConnectionDetector(getApplicationContext());
		uploadBtn = (Button) findViewById(R.id.btn_send);
		addressCustom = (TextView) findViewById(R.id.txt_username);
		comments = (EditText) findViewById(R.id.editText1_comment);				
		latLng_view = (TextView) findViewById(R.id.txtView_latLng);		
		date_view = (TextView) findViewById(R.id.txtView_date);
		isInternetPresent = conDetect.isConnetedinInternet();
	      
	    Log.e("DATA", "" + data.lat + " " + data.lng + "" + data.date);	    
	    addressCustom.setText("No Location Found! \n (Please turn On your GPS or Internet)");
	   
			captureButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub					
						//setRequestedOrientation(-1);					
						mCameraView.isPictureTaken = true;
						mCamera.takePicture(null, null, mPicture);
						frame_bg_buttoncam.setVisibility(View.GONE);  
				
						
						String date = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_12HOUR);
						date_view.setText(date);
						data.date = date;
						
						Log.e("COMM", "" + data.comments);
							
							if(Config.lat == 0 && Config.lng == 0)
							{
								uploadBtn.setVisibility(View.GONE);
								addressCustom.setVisibility(View.VISIBLE);
								latLng_view.setVisibility(View.GONE);
							}
							else
							{
								uploadBtn.setVisibility(View.VISIBLE);
								addressCustom.setVisibility(View.GONE);
								latLng_view.setVisibility(View.VISIBLE);
								latLng_view.setText(Config.lat + ", " + Config.lng);
							}
					}
				});
		
		
		
		cancelbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//setRequestedOrientation(0);
				
				toggleSlidingDrawer(false);
				mCamera.startPreview();
				frame_bg_buttoncam.setVisibility(View.VISIBLE);
								
			}
		});
		
	
		uploadBtn.setVisibility(View.VISIBLE);
		uploadBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//setRequestedOrientation(0);
				data.lat = "" + Config.lat;
				data.lng = "" + Config.lng;
				
				data.comments = ""+ comments.getText();
				comments.setText("");
				data.addressCustom = ""+ addressCustom.getText();
				addressCustom.setText("");
				
				isInternetPresent = conDetect.isConnetedinInternet();
				
			
				if(isInternetPresent)
				{					
					
					showAlertDialog(CameraActivity.this, "Internet Connection", "You have internet connection", true);
					dialog = ProgressDialog.show(CameraActivity.this, "", "Sending file...", true);
			
					Thread thread =  new Thread(new Runnable() {
						
						@SuppressLint("SimpleDateFormat")
						@Override
						public void run() {
							// TODO Auto-generated method stub
							String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
							File mediaFile = getOutputMediaFile(timeStamp);
							while(mediaFile == null) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							File details = getDetails(timeStamp);
							while(details == null) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							String xml = getXml();
							while(xml.equals("")) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							
							FileHelper.savedFile(mediaFile, image, details, xml);
							FileHelper.sendFiles(FileHelper.getFiles(Config.ROOT_FOLDER), CameraActivity.this, dialog);
						}
					});
					thread.start();
					
				}
				else
				{
					showAlertDialog(CameraActivity.this, "No Internet Connection", "You don't have internet connection", true);
					
					Thread thread =  new Thread(new Runnable() {
											
						@SuppressLint("SimpleDateFormat")
						@Override
						public void run() {
							// TODO Auto-generated method stub
							String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date());
							File mediaFile = getOutputMediaFile(timeStamp);
							while(mediaFile == null) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							File details = getDetails(timeStamp);
							while(details == null) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							String xml = getXml();
							while(xml.equals("")) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							
							FileHelper.savedFile(mediaFile, image, details, xml);
						}
					});
					thread.start();
				
				}
			
				toggleSlidingDrawer(false);
				mCamera.startPreview();
				frame_bg_buttoncam.setVisibility(View.VISIBLE);
		}
		});
		

		updateSlidingDrawer();
		
//		
//		initHideAnimTimer();
	}
	
//	private void initHideAnimTimer() {
//		hideAnimTimer = new CountDownTimer(LayoutConfig.HIDETIMER_MILIS, LayoutConfig.HIDETIMER_MILIS) {
//			
//			@Override
//			public void onTick(long arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onFinish() {
//				// TODO Auto-generated method stub
//				hideHideFrame();
//			}
//		};
//	}
//	
//	public static void showHideFrame() {
//		if(isCameraFrameHidden && frame_bg_buttoncam.getVisibility() == View.VISIBLE) {
//			int trans = 0;
//			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//				trans = -frame_bg_buttoncam.getWidth();
//			}
//			
//			ViewPropertyAnimator.animate(frame_bg_buttoncam).translationX(trans)
//			.setDuration(200).setInterpolator(new OvershootInterpolator()).setListener(new AnimatorListener() {
//				
//				@Override
//				public void onAnimationStart(Animator arg0) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public void onAnimationRepeat(Animator arg0) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public void onAnimationEnd(Animator arg0) {
//					// TODO Auto-generated method stub
//					frame_bg_buttoncam.clearAnimation();
//					isCameraFrameHidden = false;
//					
//					hideAnimTimer.start();
//				}
//	
//				@Override
//				public void onAnimationCancel(Animator arg0) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//			}).start();
//		}
//	}
	
//	public static void hideHideFrame() {
//		Log.e("" + isCameraFrameHidden, "" + frame_bg_buttoncam.getVisibility());
//		if(!isCameraFrameHidden && frame_bg_buttoncam.getVisibility() == View.VISIBLE) {
//			ViewPropertyAnimator.animate(frame_bg_buttoncam).translationX(frame_bg_buttoncam.getWidth())
//			.setDuration(200).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListener() {
//				
//				@Override
//				public void onAnimationStart(Animator arg0) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public void onAnimationRepeat(Animator arg0) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public void onAnimationEnd(Animator arg0) {
//					// TODO Auto-generated method stub
//					isCameraFrameHidden = true;
//				}
//
//				@Override
//				public void onAnimationCancel(Animator arg0) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//			}).start();
//		}
//		
//	}
//	
	
	@Override
	public void onAttachedToWindow() {
//		isCameraFrameHidden = false;
//		hideAnimTimer.start();
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
	}



	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		updateSlidingDrawer();
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}



	private void updateSlidingDrawer()
	{
		parent.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				int h;
				if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
				{
//					w = sLidingDrawer.getWidth() > sLidingDrawer.getHeight() ? sLidingDrawer.getWidth() : sLidingDrawer.getHeight();
					h = parent.getHeight() < parent.getWidth() ? parent.getHeight() : parent.getWidth();
				}
				else
				{
				//	w = sLidingDrawer.getWidth() < sLidingDrawer.getHeight() ? sLidingDrawer.getWidth() : sLidingDrawer.getHeight();
					h = parent.getHeight() > parent.getWidth() ? parent.getHeight() : parent.getWidth();

				}
//				
				
				Button handle = (Button) findViewById(R.id.handle);
				FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) sLidingDrawer.getLayoutParams();
				layoutParams.height = h / 2 + handle.getHeight();
				sLidingDrawer.setLayoutParams(layoutParams);
				sLidingDrawer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}
	
 
	
	public void showAlertDialog(Context context, String title, String message, Boolean status)
	{

		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		alertDialog.show();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private Camera getCameraInstance() 
	{
		
		 camera = null;
		 
		 try
		 {
			 camera = Camera.open();
		
		 }
		 catch (Exception e) {
			// TODO: handle exception
		}
		
		return camera;
	}


	@SuppressLint("SimpleDateFormat")
	private File getOutputMediaFile(String timeStamp) {
		  File childFolder = new File(Config.ROOT_FOLDER  + timeStamp);
		   
		  if (!childFolder.exists()){
            childFolder.mkdirs();
            Log.e("File Created:", "" + childFolder);
		  }
		  
		  File mediaFile;
		  mediaFile = new File(childFolder + File.separator + timeStamp + ".jpg.csis");
		  
		  Log.e("File Created:", "" + mediaFile);
	      return mediaFile;
		  
	}
	@SuppressLint("SimpleDateFormat")
	private File getDetails(String timeStamp)
	{
		// DataEntryList data = new DataEntryList();
		  File childFolder = new File(Config.ROOT_FOLDER  + timeStamp);
   
		  File txtFile;
     
		  txtFile = new File(childFolder + File.separator + timeStamp + ".xml.csis");
      
      return txtFile;
	}
	
	
	private String getXml()
	{
		  XmlSerializer serializer = Xml.newSerializer();
	      StringWriter writer = new StringWriter();
	   
	      Log.e("FSDFSD", "" + data.lat + " " + data.lng);
	      try
	      {
	      	  serializer.setOutput(writer);
	      	  serializer.startDocument("UTF-8", true);
	      	  serializer.startTag("", "details");
	      	
	      	  serializer.startTag("", "date");
	          serializer.text(data.date);
	          serializer.endTag("", "date");
	          
	          serializer.startTag("", "lat");
	          serializer.text(data.lat);
	          serializer.endTag("", "lat");
	          
	          serializer.startTag("", "lng");
	          serializer.text(data.lng);
	          serializer.endTag("", "lng");
	          
	          serializer.startTag("", "address");
	          serializer.text(data.addressCustom);
	          serializer.endTag("", "address");

	          serializer.startTag("", "comments");
	          serializer.text(data.comments);
	          serializer.endTag("", "comments");
	          
	          serializer.endTag("", "details");
	          serializer.endDocument();
	      	
	      }
	      catch (Exception e) {
				// TODO: handle exception
			}
	      return writer.toString();
	}

	
	private void toggleSlidingDrawer(boolean open) {
		
		if(open) 
		{
			sLidingDrawer.setVisibility(View.VISIBLE);
			if(!sLidingDrawer.isOpened())						
				sLidingDrawer.animateOpen();
			captureButton.setClickable(false);

		}
		else
		{
			if(sLidingDrawer.isOpened())				
				sLidingDrawer.animateClose();			
			sLidingDrawer.setVisibility(View.GONE);
			captureButton.setClickable(true);

		}
	}
	
	
	 private PictureCallback mPicture = new PictureCallback() 
	 {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			image = data;
			
			sLidingDrawer.setVisibility(View.VISIBLE);
			toggleSlidingDrawer(true);
			
		}					
	};

	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		 mCameraView.getHolder().removeCallback(mCameraView);
		mCamera.release();
		  mCamera = null;
		super.onDestroy();
	}

	
	/**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            Config.lat = location.getLatitude();
        }
         
        // return latitude
        return Config.lat;
    }
     
    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            Config.lng = location.getLongitude();
        }
         
        // return longitude
        return Config.lng;
    }
    
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
     
 

}
