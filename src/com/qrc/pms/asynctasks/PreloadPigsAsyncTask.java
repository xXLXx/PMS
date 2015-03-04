package com.qrc.pms.asynctasks;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

import com.qrc.pms.MainActivity;
import com.qrc.pms.config.Config;
import com.qrc.pms.helper.ConnectionHelper;
import com.qrc.pms.model.Pig;

public class PreloadPigsAsyncTask extends AsyncTask<Object, Integer, Integer>{
	
	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_FAILED = 0;
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		Editor editor = ((MainActivity)arg0[0]).prefs.edit();
		
		String pigsJson = ConnectionHelper.getStringFrom(Config.BASE_URL + "pig.php?f=getPigs");
		try {
			JSONArray pigsArr = new JSONArray(pigsJson);

			((MainActivity)arg0[0]).pigListAdapter.clearAll();
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
					pigObj.getInt("pregnancyCount")
				);
				((MainActivity)arg0[0]).pigListAdapter.add(pig);
				editor.putString(pig.getId(), pig.getSerializedString(false));
			}
			
			editor.commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return STATUS_FAILED;
		}
		
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
		
		return STATUS_SUCCESS;
	}

	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}
