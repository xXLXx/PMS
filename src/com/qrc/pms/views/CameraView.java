package com.qrc.pms.views;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback{

	private SurfaceHolder mHolder;
    private Camera mCamera;
   // private DisplayMetrics displayMetrics;
    public boolean isPictureTaken = false;

    public CameraView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	public CameraView(Context context, Camera camera) {
		super(context);
		// TODO Auto-generated constructor stub
		
		mCamera = camera;
		mHolder = getHolder();
		mHolder.addCallback(this);
		//displayMetrics = getResources().getDisplayMetrics();
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	protected void setDisplayOrientation(Camera camera, int angle){
	    Method downPolymorphic;
	    try
	    {
	        downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
	        if (downPolymorphic != null)
	            downPolymorphic.invoke(camera, new Object[] { angle });
	    }
	    catch (Exception e1)
	    {
	    }
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
		// TODO Auto-generated method stub
		 if (mHolder.getSurface() == null)
	        {
			 return;
	        }
		 
		try
		{
			if(!isPictureTaken)
			{	
				Camera.Parameters parameters = mCamera.getParameters();
	        	List<Size> sizes = parameters.getSupportedPreviewSizes();
	        	Size optimalSize = getOptimalPreviewSize(sizes, width, height);
	        	parameters.setPreviewSize(optimalSize.width, optimalSize.height);
	            requestLayout();
	            
	            mCamera.setParameters(parameters);
	            mCamera.setPreviewDisplay(mHolder);
	            mCamera.startPreview();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		// TODO Auto-generated method stub
	
		try
		{
			mCamera.setPreviewDisplay(surfaceHolder);
			mCamera.startPreview();
			
		}
		catch (IOException e) {
			// TODO: handle exception
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		// TODO Auto-generated method stub
		//mCamera.release();
		//mCamera.stopPreview();
	
		    mCamera.stopPreview();
		   // mCamera.release();
		 
		
	}
	


	 /**
     * Calculate the optimal size of camera preview
     * @param sizes
     * @param w
     * @param h
     * @return
     */
	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null)
        	return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) 
        {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) 
            	continue;
            if (Math.abs(size.height - targetHeight) < minDiff) 
            {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) 
        {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes)
            {
                if (Math.abs(size.height - targetHeight) < minDiff) 
                {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
//			CameraActivity.showHideFrame();
		}
		return super.onTouchEvent(event);
	}


}
