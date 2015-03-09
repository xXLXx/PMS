package com.qrc.pms.model;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.text.format.DateFormat;

public class Log {
	public static final int TYPE_VACCINE = 0;
	public static final int TYPE_FEEDS = 1;
	
	public String description = "";
	public String notes = "";
	public boolean done = false;
	private int created_at = 0;
	public long pig_id = -1;
	
	/**
	 * For data commit attr
	 */
	public int type;
	
	public Log(String description, String notes, boolean done, int created_at,
			long pig_id, int type) {
		super();
		this.description = description;
		this.notes = notes;
		this.done = done;
		this.created_at = created_at;
		this.pig_id = pig_id;
		this.type = type;
	}
	
	public String getDate() {
		return DateFormat.format("MM/dd/yyyy hh:mmaa", ((long) created_at * 1000)).toString();
	}
	
	public String getId() {
		return "" + created_at;
	}
	
	public int getGroupId() {
		return getGroupId(created_at);
	}
	
	public static int getGroupId(int created) {
		return created / 86400;
	}
	
	public String getAction() {
		return type == TYPE_FEEDS ? "logFeeds" : "logVaccine";
	}
	
	public HashMap<String, String> getHashMap() {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("description", "" + description);
		data.put("notes", "" + notes);
		data.put("done", "" + done);
		data.put("createdAt", "" + created_at);
		data.put("pigId", "" + pig_id);
		
		return data;
	}
	
	public String getSerializedString() throws JSONException {
		return getSerializedObject().toString();
	}
	
	public Object getSerializedObject() throws JSONException {
		JSONObject data = new JSONObject();
		data.put("description", "" + description);
		data.put("notes", "" + notes);
		data.put("done", "" + done);
		data.put("createdAt", "" + created_at);
		data.put("pigId", "" + pig_id);
		data.put("type", "" + type);
		
		return data;
	}
	
	public static Log getLog(String json) {
		try {
			JSONObject data = new JSONObject(json);
			Log log = new Log(
					data.getString("description"),
					data.getString("notes"),
					data.getBoolean("done"),
					data.getInt("createdAt"),
					data.getLong("pigId"),
					data.getInt("type")
					);
			return log;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}