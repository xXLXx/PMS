package com.qrc.pms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Currency;

import android.content.Intent;
import android.os.Bundle;
import android.sax.RootElement;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

//edited
public class FindPeopleFragment extends SherlockFragment {
//	private GalleryViewPager mViewPager;
	
	public static Button connect_to_facebook;
	public View rootView;
	
	public FindPeopleFragment(){
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
         rootView = inflater.inflate(R.layout.fragment_find_people, container, false);
        return rootView;
    }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	   
		connect_to_facebook = (Button) view.findViewById(R.id.btn_connect_fb);
		
		connect_to_facebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				
			}
		});
			
//        CustomPagerAdapter adapter = new CustomPagerAdapter(new int[] {R.drawable.antismoke, R.drawable.antismoke1, R.drawable.antismoke2, R.drawable.antismoke3});
//        ViewPager myPager = (ViewPager) getActivity().findViewById(R.id.customviewpager);
//        myPager.setAdapter(adapter);
//        myPager.setCurrentItem(0);
		
//		String[] urls = null;
//        List<String> items = new ArrayList<String>();
//		try {
//			urls = getActivity().getAssets().list("");
//	
//	        for (String filename : urls) 
//	        {
//	        	if (filename.matches(".+\\.people.jpg")) 
//	        	{
//	        		String path = getActivity().getFilesDir() + "/" + filename;
//	        		copy(getActivity().getAssets().open(filename), new File(path) );
//	        		items.add(path);
//	        	}
//	        }
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		FilePagerAdapter pagerAdapter = new FilePagerAdapter(getActivity(), items);
//		mViewPager = (GalleryViewPager) getActivity().findViewById(R.id.viewer);
//        mViewPager.setOffscreenPageLimit(3);
//        mViewPager.setAdapter(pagerAdapter);
	}
	
	public void copy(InputStream in, File dst) throws IOException {

//        OutputStream out = new FileOutputStream(dst);
//
//        // Transfer bytes from in to out
//        byte[] buf = new byte[1024];
//        int len;
//        while ((len = in.read(buf)) > 0) {
//            out.write(buf, 0, len);
//        }
//        in.close();
//        out.close();
    }
}
