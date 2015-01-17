package com.qrc.pms.config;

import android.os.Environment;

public class Config {
	
	public static String ROOT_FOLDER = Environment.getExternalStorageDirectory() + "/PMS/";
	public static String KEY;
	public static int CONNECTION_TIMEOUTE_MILIS = 10000;
	
	public static String PREF_NAME = "PMS";
	public static String PREF_NAME_TIMESLOTS = "PMS_timeslots";
	
	public static String[] TIME_SLOTS = {"06:00", "11:00", "17:00"};
}
