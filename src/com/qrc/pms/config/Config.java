package com.qrc.pms.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

public class Config {
	
	public static String ROOT_FOLDER = Environment.getExternalStorageDirectory() + "/CSIS/";
	public static String KEY;
	public static double lat = 0;
	public static double lng = 0;
	public static int CONNECTION_TIMEOUTE_MILIS = 10000;
	public static String FILE_PATH = ROOT_FOLDER + "download.xml"; 
	
	public static String PREF_NAME = "PMS";
	public static String PREF_NAME_TIMESLOTS = "PMS_timeslots";
	
	public static String[] TIME_SLOTS = {"06:00", "11:00", "17:00"};
}
