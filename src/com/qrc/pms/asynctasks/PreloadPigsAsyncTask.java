package com.qrc.pms.asynctasks;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

import com.qrc.pms.MainActivity;
import com.qrc.pms.config.Config;
import com.qrc.pms.helper.ConnectionHelper;
import com.qrc.pms.model.Log;
import com.qrc.pms.model.Pig;

public class PreloadPigsAsyncTask extends AsyncTask<Object, Integer, Integer>{
	
	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_FAILED = 0;
	public boolean done = false;
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		
		executeProcess(arg0);
		while (!done) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			executeProcess(arg0);
		}
		
		return STATUS_SUCCESS;
	}
	
	private void executeProcess(final Object... arg0) {
		Editor editor = ((MainActivity)arg0[0]).prefs.edit();
		
		String lastUpdate = ConnectionHelper.getStringFrom(Config.BASE_URL + "pig.php?f=lastUpdate");
		SharedPreferences lastUpdatePrefs =  ((Context)arg0[0]).getSharedPreferences(Config.PREF_NAME_LAST_UPDATE, Context.MODE_PRIVATE);
		String currentUpdate = lastUpdatePrefs.getString("lastUpdate", "0");
		
		android.util.Log.w("PreloadAsyncTask", "Test for updates: " + currentUpdate + ":" + lastUpdate);
		
		if (!lastUpdate.equals(currentUpdate)) {
			android.util.Log.w("PreloadAsyncTask", "Got update hit");
			/**
			 * Get Pigs
			 */
			String pigsJson = ConnectionHelper.getStringFrom(Config.BASE_URL + "pig.php?f=getPigs");
			try {
				JSONArray pigsArr = new JSONArray(pigsJson);
				
				((MainActivity)arg0[0]).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub

						((MainActivity)arg0[0]).pigListAdapter.clearAll();
					}
				});
				editor.clear();
				
				for (int x = 0; x < pigsArr.length(); x++) {
					JSONObject pigObj = pigsArr.getJSONObject(x);
					Pig pig = new Pig(
						pigObj.getString("id"),
						pigObj.getInt("purpose"),
						pigObj.getInt("birthdate"),
						pigObj.getString("groupName"),
						pigObj.getInt("count"),
						pigObj.getInt("pregnancyDate"),
						pigObj.getInt("milkingDate"),
						pigObj.getLong("dateAdded"),
						pigObj.getInt("pregnancyCount"),
						pigObj.getInt("removed")
					);
					((MainActivity)arg0[0]).pigListAdapter.add(pig);
					editor.putString(pig.getId(), pig.getSerializedString(false));
				}
				
				editor.commit();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
//				return STATUS_FAILED;
			}
			
			/**
			 * Get Vaccine Logs
			 */
			String vaccineLogsJson = ConnectionHelper.getStringFrom(Config.BASE_URL + "pig.php?f=getVaccineLogs");
			try {
				JSONArray vaccineLogsArr = new JSONArray(vaccineLogsJson);
				
				long currentPigId = -1;
				
				Editor logsEditor = null;
				
				for (int x = 0; x < vaccineLogsArr.length(); x++) {
					JSONObject logObj = vaccineLogsArr.getJSONObject(x);
					long tmpPidId = logObj.getLong("pigId");
					if (currentPigId != tmpPidId) {
						if (logsEditor != null) {
							logsEditor.commit();
						}
						
						String prefName = Config.PREF_NAME_VACCINE_LOGS + currentPigId;
						logsEditor = ((Context)arg0[0]).getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
						logsEditor.clear();
						
						currentPigId = tmpPidId;
					} else if (logsEditor != null){
						logsEditor.putString(logObj.getString("createdAt"), logObj.toString());
						
						if (logObj.has("done")) {
							if (logObj.getInt("done") == 1) {
								Editor doneLogs = ((Context)arg0[0]).getSharedPreferences(Config.PREF_NAME_VACCINE_DONE + logObj.getInt("pigId"), Context.MODE_PRIVATE).edit();
								doneLogs.putBoolean("" + Log.getGroupId(logObj.getInt("createdAt")), true);
								doneLogs.commit();
							}
							
						}
					}
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
//				return STATUS_FAILED;
			}
			
			/**
			 * Get Feeds Logs
			 */
			String feedsLogsJson = ConnectionHelper.getStringFrom(Config.BASE_URL + "pig.php?f=getFeedsLogs");
			try {
				JSONArray feedsLogsArr = new JSONArray(feedsLogsJson);
				
				long currentPigId = -1;
				
				Editor logsEditor = null;
				
				for (int x = 0; x < feedsLogsArr.length(); x++) {
					JSONObject logObj = feedsLogsArr.getJSONObject(x);
					long tmpPidId = logObj.getLong("pigId");
					if (currentPigId != tmpPidId) {
						if (logsEditor != null) {
							logsEditor.commit();
						}
						
						String prefName = Config.PREF_NAME_FEEDS_LOGS + currentPigId;
						logsEditor = ((Context)arg0[0]).getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
						logsEditor.clear();
						
						currentPigId = tmpPidId;
					} else if (logsEditor != null){
						logsEditor.putString(logObj.getString("createdAt"), logObj.toString());
						
						if (logObj.has("done")) {
							if (logObj.getInt("done") == 1) {
								Editor doneLogs = ((Context)arg0[0]).getSharedPreferences(Config.PREF_NAME_FEEDS_DONE + logObj.getInt("pigId"), Context.MODE_PRIVATE).edit();
								doneLogs.putBoolean("" + Log.getGroupId(logObj.getInt("createdAt")), true);
								doneLogs.commit();
							}
							
						}
					}
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
//				return STATUS_FAILED;
			}
			
			lastUpdatePrefs.edit().putString("lastUpdate", lastUpdate).commit();
		}
		
		/**
		 * Uncomitted server post changes
		 */
		final SharedPreferences failedPrefs = ((Context)arg0[0]).getSharedPreferences(Config.PREF_NAME_FAILEDPOSTS, Context.MODE_PRIVATE);
		Map<String, ?> failedMap = failedPrefs.getAll();
		for (Object failed : failedMap.values()) {
			final Pig pig = Pig.getPig(failed.toString());
			if (pig.action.equals("addPig")) {
				((MainActivity)arg0[0]).pigListAdapter.add(pig);
			} else if (pig.action.equals("updatePig")) {
				((MainActivity)arg0[0]).pigListAdapter.set(((MainActivity)arg0[0]).pigListAdapter.getPosition(pig.getId()), pig);
			}
			
			try {
				editor.putString(pig.getId(), pig.getSerializedString(false));
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (ConnectionHelper.sendPost(Config.BASE_URL + "pig.php?f=" + pig.action, pig.getHashMap(false)).equals("1")) {
							failedPrefs.edit().remove(pig.getId()).commit();
						}
					}
				}).start();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/**
		 * Uncomitted server logs changes
		 */
		final SharedPreferences failedLogsPrefs = ((Context)arg0[0]).getSharedPreferences(Config.PREF_NAME_FAILEDLOGS, Context.MODE_PRIVATE);
		Map<String, ?> failedLogsMap = failedLogsPrefs.getAll();
		for (Object failed : failedLogsMap.values()) {
			final Log log = Log.getLog(failed.toString());
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (ConnectionHelper.sendPost(Config.BASE_URL + "pig.php?f=" + log.getAction(), log.getHashMap()).equals("1")) {
						failedLogsPrefs.edit().remove(log.getId()).commit();
					}
				}
			}).start();
		}
	}

	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}
