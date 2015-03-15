package com.qrc.pms.config;

import android.os.Environment;

public class Config {
	
	public static String ROOT_FOLDER = Environment.getExternalStorageDirectory() + "/PMS/";
	public static String KEY;
	public static int CONNECTION_TIMEOUTE_MILIS = 10000;
	
	public static String PREF_NAME = "PMS";
	public static String PREF_NAME_TIMESLOTS = "PMS_timeslots";
	public static String PREF_NAME_FAILEDPOSTS = "PMS_failed";
	public static String PREF_NAME_FAILEDLOGS = "PMS_failedlogs";
	public static String PREF_NAME_VACCINE_LOGS = "PMS_vaccine_";
	public static String PREF_NAME_FEEDS_LOGS = "PMS_feeds_";
	public static String PREF_NAME_VACCINE_DONE = "PMS_vaccine_done_";
	public static String PREF_NAME_FEEDS_DONE = "PMS_feeds_done_";
	public static String PREF_NAME_LAST_UPDATE = "PMS_last_update";
	
	public static String[] TIME_SLOTS = {"06:00", "11:00", "17:00"};
	
	public static String BASE_URL = "http://192.168.212.1/pms/";
}
