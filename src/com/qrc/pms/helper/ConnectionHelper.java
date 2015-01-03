package com.qrc.pms.helper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.qrc.pms.config.Config;

public class ConnectionHelper {
	
	public static boolean isNetworkAvailable(Activity activity) {
	    ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public static boolean downloadFile(String uri, String folder, String filepath, boolean createTmp) throws IOException, MalformedURLException {
		URL url = new URL(uri);
    	Log.d("DownloadFile: ", uri);
    	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(Config.CONNECTION_TIMEOUTE_MILIS);
        connection.setReadTimeout(Config.CONNECTION_TIMEOUTE_MILIS);
//    	connection.setRequestMethod("GET");
//    	connection.setDoOutput(true);
    	connection.connect();
    	InputStream xml = connection.getInputStream();
    	
    	File folderF = new File(folder);
    	if(!folderF.exists()) {
    		folderF.mkdirs();
    		Log.d("Created Folder:", "" + folderF); 
    	}
    	
    	File fileF;
    	if(createTmp) {
    		fileF = new File(filepath + ".tmp");
    	}
    	else {
    		fileF = new File(filepath);
    	}
    	
    	OutputStream out = new FileOutputStream(fileF);
		byte[] buffer = new byte[1024];
		int read;
		
		while ((read = xml.read(buffer)) != -1)
			out.write(buffer, 0, read);
		xml.close();
		xml = null;
		out.flush();
		out.close();
		out = null;
		Log.d("File ", "Copied: " + fileF);
    	
		if(createTmp) {
			if(fileF.length() == 0) {
				fileF.delete();
			}
			else {
				File currFile = new File(filepath);
				if(currFile.exists()) {
					currFile.delete();
				}
				fileF.renameTo(currFile);
			}
		}
		
		return true;
	}
	
	public static void saveLastUpdate(SharedPreferences sharedPrefs, String uri) {	
		String str = getStringFrom(uri);
		
    	Editor editor = sharedPrefs.edit();
		editor.putString("", str);
		editor.commit();
		
		///Log.d("Shared Preferences", "Saved " + Config.PREFITEM_CONFIG_LASTUPDATE + ": " + str);
	}
	
	public static String getStringFrom(String uri) {
		String str = "";
		try {
			URL url = new URL(uri);
			Log.d("DownloadString: ", uri);
	    	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    	connection.setConnectTimeout(Config.CONNECTION_TIMEOUTE_MILIS);
	    	connection.setReadTimeout(Config.CONNECTION_TIMEOUTE_MILIS);
//	    	connection.setRequestMethod("GET");
//	    	connection.setDoOutput(true);
	    	connection.connect();
	    	InputStream xml = connection.getInputStream();
	    	
	    	byte[] buffer = new byte[1024];
			int read;
			
			while ((read = xml.read(buffer)) != -1)
				str = str.concat(new String(buffer, 0, read));
	    
		} catch(MalformedURLException e) {
        	e.printStackTrace();
        } catch(IOException e) {
        	e.printStackTrace();
        }
		
		return str;
	}

}
