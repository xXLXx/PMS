package com.qrc.pms;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

	private Context __context;
	
	public ConnectionDetector(Context context)
	{
		this.__context = context;
	}
	
	public boolean isConnetedinInternet()
	{
		ConnectivityManager conn = (ConnectivityManager) __context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(conn != null)
		{
			NetworkInfo[] netfo = conn.getAllNetworkInfo();
			if(netfo != null)
			
				for (int i = 0; i < netfo.length; i++)
				if(netfo[i].getState() == NetworkInfo.State.CONNECTED)
				{
					return true;
				}
			
		}
		
		return false;
	}
}
