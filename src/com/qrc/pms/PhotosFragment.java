package com.qrc.pms;

import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

//edited
public class PhotosFragment extends SherlockFragment {
	private GalleryViewPager mViewPager;
	
	public PhotosFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
         
        return rootView;
    }
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
//		 CustomPagerAdapter adapter = new CustomPagerAdapter(new int[] {R.drawable.fact});
//	        ViewPager myPager = (ViewPager) getActivity().findViewById(R.id.customviewpager);
//	        myPager.setAdapter(adapter);
//	        myPager.setCurrentItem(0);
		
//		String[] urls = null;
//        List<String> items = new ArrayList<String>();
//		try {
//			urls = getActivity().getAssets().list("");
//	
//	        for (String filename : urls) 
//	        {
//	        	if (filename.matches(".+\\.photos.jpg")) 
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
	
//	
//	public void copy(InputStream in, File dst) throws IOException {
//
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
//    }
}
