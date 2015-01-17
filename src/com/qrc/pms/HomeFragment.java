package com.qrc.pms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.nineoldandroids.animation.ObjectAnimator;
import com.qrc.pms.views.CustomCaptureActivity;

//edited
public class HomeFragment extends SherlockFragment {
	
	
	public View view_list_fraghome;
	public static Button image_logo_home;
	private ImageView image_logo_title;
	ObjectAnimator anim;
	ObjectAnimator bounce;
	Toast toast;
	RelativeLayout layout;
	
	
	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		view_list_fraghome = inflater.inflate(R.layout.fragment_home, container, false);
		return view_list_fraghome;
    }
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		image_logo_home = (Button) view_list_fraghome.findViewById(R.id.imageview_logo_home);
		image_logo_title = (ImageView) view_list_fraghome.findViewById(R.id.imageView_logo_text);
			
			bounce = ObjectAnimator.ofFloat(image_logo_home, "translationY", -100, 0);
			bounce.setDuration(2000);
			bounce.setInterpolator(new BounceInterpolator());	
			anim = ObjectAnimator.ofFloat(image_logo_title, "translationX",-100, 0);
			anim.setDuration(1000);
		
			startAnims();
			
			final View pLayout = (View) view_list_fraghome.findViewById(R.id.imageView_home_parent);	
			pLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() 
			{				
				@SuppressWarnings("deprecation")
				@Override
				public void onGlobalLayout() {
					// TODO Auto-generated method stub
	 				LayoutParams layout = (LayoutParams) image_logo_home.getLayoutParams();
					layout.topMargin += pLayout.getHeight() / 2  - image_logo_title.getDrawable().getIntrinsicHeight() - image_logo_home.getBackground().getIntrinsicHeight() / 2;
					image_logo_home.setLayoutParams(layout);
					pLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			});
			
			
			image_logo_home.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					Intent intent = new Intent(getActivity(), CustomCaptureActivity.class);
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			        startActivityForResult(intent, 0);
				}
			});
	
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
              Log.e("data", data.getStringExtra("pigId"));
              ((MainActivity) getActivity()).openListPosition = ((MainActivity) getActivity()).pigListAdapter.getPosition(data.getStringExtra("pigId"));
              ((MainActivity) getActivity()).displayView(2);
        		
              String contents = data.getStringExtra("SCAN_RESULT");
              String format = data.getStringExtra("SCAN_RESULT_FORMAT");
              Log.e("QR Code", contents + "-" + format);
              // Handle successful scan
           } else if (resultCode == Activity.RESULT_CANCELED) {
                 //Handle cancel
           }
        }
	}

	public void startAnims() {
		bounce.start(); 
   	 	anim.start(); 
		toast = Toast.makeText(view_list_fraghome.getContext(), "Press It!", Toast.LENGTH_SHORT);  
		toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
}

