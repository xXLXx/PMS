package com.qrc.pms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import ru.truba.touchgallery.GalleryWidget.FilePagerAdapter;
import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;

import com.actionbarsherlock.app.SherlockFragment;
import com.qrc.pms.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

//edited
public class WhatsHotFragment extends SherlockFragment {
	private GalleryViewPager mViewPager;
	public WhatsHotFragment(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_whats_hot, container, false);
       
        
        return rootView;
    }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		String[] urls = null;
        List<String> items = new ArrayList<String>();
		try {
			urls = getActivity().getAssets().list("");
	
	        for (String filename : urls) 
	        {
	        	if (filename.matches(".+\\.i.png")) 
	        	{
	        		String path = getActivity().getFilesDir() + "/" + filename;
	        		copy(getActivity().getAssets().open(filename), new File(path) );
	        		items.add(path);
	        	}
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FilePagerAdapter pagerAdapter = new FilePagerAdapter(getActivity(), items);
		mViewPager = (GalleryViewPager) getActivity().findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
	}
	
	
	public void copy(InputStream in, File dst) throws IOException {

        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}