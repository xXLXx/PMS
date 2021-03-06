package com.qrc.pms.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.qrc.pms.R;
import com.qrc.pms.config.Config;

public class Pig {
	public static final int PURPOSE_FATTENING = 1;
	public static final int PURPOSE_SOW = 2;
	public static String[] PURPOSE_LIST;
	
	public static final int STAGE_NURSING = 1;
	public static final int STAGE_FATTENED = 2;
	public static final int STAGE_PREGNANT = 3;
	public static final int STAGE_MILKING = 4;
	public static String[] STAGES = {"Unknown", "Piglet", "Fattened Pig", "Pregnant Sow", "Milking Sow"};
	
	public int purpose = 0;
	public int birthDate = 0;
	public int removed = 0;
	/**
	 * For pregnant date or date of giving birth
	 */
	public int pregnancyDate = 0;
	private int milkingDate = 0;
	public int pregnancyCount = 0;
	
	/**
	 * Optional fields
	 */
	public String groupName = "";
	public int count = 1;
	public long dateAdded = 0;
	
	private String id;
		
	/**
	 * Extra data for additional info
	 */
	public String action = "";
	
	/**
	 * Inherited attr from logs
	 */
	private int lastVaccineLog = 0;
	private int lastFeedsLog = 0;
	
	public Pig(int purpose, int birthDate) {
		this(purpose, birthDate, "");
	}
	
	public Pig(int purpose, int birthDate, String groupName) {
		this(purpose, birthDate, groupName, 0);
	}
	
	/**
	 * For add pig required fields
	 * @param id
	 * @param purpose
	 * @param birthDate
	 * @param groupName
	 * @param count
	 */
	public Pig(int purpose, int birthDate, String groupName, int count) {
		this("", purpose, birthDate, groupName, count, 0, 0, System.currentTimeMillis(), 0, 0);
	}
	
	public Pig(String id, int purpose, int birthDate, String groupName, int count, int pregnancyDate, int milkingDate, long dateAdded,
			int pregnancyCount, int removed) {
		super();
		this.id = id;
		this.purpose = purpose;
		this.birthDate = birthDate;
		this.groupName = groupName;
		this.count = purpose == PURPOSE_SOW ? 1 : count;
		this.pregnancyDate = pregnancyDate;
		this.milkingDate = milkingDate;
		this.dateAdded = dateAdded;
		this.pregnancyCount = pregnancyCount;
		this.removed = removed;
	}
	
	public static void setPurposeList(Resources res) {
		PURPOSE_LIST = res.getStringArray(R.array.purpose_list);
	}
	
	public String getId() {
		return "" + dateAdded;
	}
	
	public int getCount() {
		return count - removed;
	}
	
	public String getGroupName() {
		return groupName.equals("") ? "Pig Added " + formatDate(dateAdded) : groupName;
	}
	
	public void saveQrCode(File pictureFile) throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(pictureFile);
		getQrCode().writeTo(fos);
	}
	
	@SuppressLint("SimpleDateFormat")
	public File getOutputMediaFile(String timeStamp) {
		  File childFolder = new File(Config.ROOT_FOLDER);
		   
		  if (!childFolder.exists()){
            childFolder.mkdirs();
            Log.e("File Created:", "" + childFolder);
		  }
		  
		  File mediaFile;
		  mediaFile = new File(childFolder + File.separator + timeStamp + ".jpg");
		  
		  Log.e("File Created:", "" + mediaFile);
	      return mediaFile;
		  
	}
	
	private QRCode getQrCode() {
		try {
			return ((QRCode) QRCode
					.from(getSerializedString(true))
					.to(ImageType.JPG)
					.withSize(800, 800)
					.withHint(EncodeHintType.CHARACTER_SET, "UTF-8")
					.withErrorCorrection(ErrorCorrectionLevel.H));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Bitmap getQrCodeBitmap() {
		return getQrCode().bitmap();
	}
	
	public String getSerializedString(boolean minimal) throws JSONException {
		return getSerializedObject(minimal, "").toString();
	}
	
	public String getSerializedString(boolean minimal, String action) throws JSONException {
		return getSerializedObject(minimal, action).toString();
	}
	
	public Object getSerializedObject(boolean minimal) throws JSONException {
		return getSerializedObject(minimal, "");
	}
	
	public Object getSerializedObject(boolean minimal, String action) throws JSONException {
		JSONObject data = new JSONObject();
		data.put("purpose", purpose);
		data.put("birthdate", birthDate);
		data.put("dateAdded", dateAdded);
		if (!action.equals("")) {
			data.put("action", action);
		}
		
		if (!minimal) {
			data.put("groupName", groupName);
			data.put("id", id);
			data.put("count", count);
			data.put("pregnancyDate", pregnancyDate);
			data.put("milkingDate", milkingDate);
			data.put("pregnancyCount", pregnancyCount);
			data.put("removed", removed);
		}
		
		return data;
	}
	
	public HashMap<String, String> getHashMap(boolean minimal) {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("purpose", "" + purpose);
		data.put("birthdate", "" + birthDate);
		data.put("dateAdded", "" + dateAdded);
		
		
		if (!minimal) {
			data.put("groupName", groupName);
			data.put("id", id);
			data.put("count", "" + count);
			data.put("pregnancyDate", "" + pregnancyDate);
			data.put("milkingDate", "" + milkingDate);
			data.put("pregnancyCount", "" + pregnancyCount);
			data.put("removed", "" + removed);
		}
		
		return data;
	}
	
	public static Pig getPig(SharedPreferences prefs, String id) {
		return Pig.getPig(prefs.getString(id, ""));
	}
	
	public static Pig getPig(String json) {
		try {
			JSONObject data = new JSONObject(json);
			Pig pig = new Pig(
					data.getString("id"),
					data.getInt("purpose"),
					data.getInt("birthdate"),
					data.getString("groupName"),
					data.getInt("count"),
					data.getInt("pregnancyDate"),
					data.getInt("milkingDate"),
					data.getLong("dateAdded"),
					data.getInt("pregnancyCount"),
					data.getInt("removed")
					);
			if (data.has("action")) {
				pig.action = data.getString("action");
			}
			return pig;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int indexOfPurpose(String purpose) {
		for (int x = 0; x < PURPOSE_LIST.length; x++) if (PURPOSE_LIST[x].equals(purpose)) {
			return x;
		}
		
		return 0;
	}
	
	public String formatDate(int seconds) {
		return formatDate((long)seconds * 1000);
	}
	
	public static String formatDate(long milliseconds) {
		return DateFormat.format("MM/dd/yyyy", milliseconds).toString();
	}
	
	public String getPregnancyDate() {
		if (purpose == PURPOSE_SOW && pregnancyDate > 0 && milkingDate <= 0) {
			return formatDate(pregnancyDate);
		}
		
		return "";
	}
	
	public String getMilkingDate() {
		if (purpose == PURPOSE_SOW && milkingDate > 0) {
			return formatDate(milkingDate);
		}
		
		return "";
	}
	
	public String getBirthDate() {
		return formatDate(birthDate);
	}
	
	public String getDateAdded() {
		return formatDate(dateAdded);
	}
	
	public String getPurpose() {
		if (purpose < 1 || purpose >= PURPOSE_LIST.length) {
			return PURPOSE_LIST[0];
		} else {
			return PURPOSE_LIST[purpose];
		}
	}
	
	public void setExtraDate(int extraDateSec) {
		if (purpose == PURPOSE_SOW) {
			if (milkingDate > 0 || (milkingDate <= 0 && pregnancyDate <= 0)) {
				pregnancyCount++;
				pregnancyDate = extraDateSec;
				milkingDate = 0;
			} else {
				pregnancyDate = 0;
				milkingDate = extraDateSec;
			} 
		}
	}
	
	public String getAge() {
		int changeInDate = (int) ((System.currentTimeMillis() / 1000 - birthDate) / 86400);
		
		if (changeInDate < 0) {
			return "Not yet born";
		} else if (changeInDate <= 1) {
			return changeInDate + " day old";
		}
        return changeInDate + " days old";
	}
	
	public String getVaccine(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(Config.PREF_NAME_VACCINE_DONE + getId(), Context.MODE_PRIVATE);
		if (prefs.getBoolean("" + com.qrc.pms.model.Log.getGroupId((int) (System.currentTimeMillis() / 1000)), false)) {
			return "";
		}
		
		return getVaccine();
	}
	
	public boolean hasFeedsNotification(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(Config.PREF_NAME_FEEDS_DONE + getId(), Context.MODE_PRIVATE);
		return !prefs.getBoolean("" + com.qrc.pms.model.Log.getGroupId((int) (System.currentTimeMillis() / 1000)), false);
	}
	
	public String getVaccine() {
		String vaccine = "";
		
		int changeInDate = (int) ((System.currentTimeMillis() / 1000 - birthDate) / 86400);
		
		if (changeInDate == 1) {
			vaccine = "Instant Iron - Drops 1ml";
		} else if (changeInDate == 3) {
			vaccine = "Iron Dextran - Inject 1ml";
		} else if (changeInDate == 7) {
			vaccine = "Respisure - Inject 2ml";
		} else if (changeInDate == 14) {
			vaccine = "Iron Dextran - Inject 1ml, Castration - Inject Terramycin Long Acting 1ml";
		} else if (changeInDate == 21) {
			vaccine = "Respisure - Inject 2ml";
		} else if (changeInDate == 28) {
			vaccine = "Hog Cholera Vaccine (Dosage depending on brand)";
		} else if (changeInDate == 30) {
			vaccine = "Weaning, Inject Terramycin Long Acting";
		} else if (changeInDate == 55 || changeInDate == 90 || changeInDate == 120) {
			vaccine = "Deworm";
		}
		
		return vaccine;
	}
	
	public Feeds getFeeds() {
		Feeds feeds = new Feeds("Not Specified", "");
		int changeInDate = 0;
		
		changeInDate = (int) ((System.currentTimeMillis() / 1000 - birthDate) / 86400);
		
		if (changeInDate >= 3 && changeInDate <= 5) {
			feeds = new Feeds("MILKO-PLUS", "50 ml/head/day\n(16.67 ml/serving)");
		} else if (changeInDate >= 6 && changeInDate <= 35) {
			String feed = "NUTRILAC BOOSTER CRUMBLE";
			if (changeInDate <= 28) {
				feeds = new Feeds(feed, "20 grams/head/day\n(6.67 grams/serving)");
			} else {
				feeds = new Feeds(feed, "300 grams/head/day\n(100 grams/serving)");
			}
		} else if (changeInDate >= 36 && changeInDate <= 42) {
			feeds = new Feeds("NUTRILAC-PLUS PELLET", "350 grams/head/day\n(116.67 grams/serving)");
		} else if (changeInDate >= 42 && changeInDate <= 55) {
			feeds = new Feeds("NUTRILAC-PLUS PELLET", "0.5 to 0.8 kg per day\n(0.17 to 0.27 kg/serving)");
		} else if (changeInDate >= 55 && changeInDate <= 90) {
			feeds = new Feeds("NUTRI-START", "1.25 to 1.5 kg per day\n(0.42 kg to 0.5 kg/serving)");
		} else if (changeInDate >= 90 && changeInDate <= 120) {
			feeds = new Feeds("NUTRI-GRO", "1.75 to 2.0 kg per day\n(0.58 to 0.67 kg/serving)");
		} else if (
				(changeInDate >= 120 && changeInDate <= 150) ||
				(changeInDate > 150 && (purpose == PURPOSE_FATTENING ||
				(purpose == PURPOSE_SOW && milkingDate <= 0 && pregnancyDate <= 0)))) {
			feeds = new Feeds("NUTRI-BIG", "2.3 to 2.5 kg per day\n(0.77 to 0.83 kg/serving)");
		} else if (purpose == PURPOSE_SOW) {
			if (milkingDate > 0) {
				int changeInDateSec = (int) (System.currentTimeMillis() / 1000 - milkingDate);
				changeInDate = (int) (changeInDateSec / 86400);
				String feed = "LITTER-SAVER";
				
				if (changeInDateSec / 3600 <= 6) {
					feeds = new Feeds("Minimal feeding (as needed) " + feed, "Maximum of 100 grams\n(33.33 grams/serving)");
				} else if (changeInDate >= 2 && changeInDate <= 27) {
					feeds = new Feeds(feed, "3 kgs. plus 250g per piglet\n(1 kg plus 83g per piglet/serving)");
				} else if (changeInDate <= 28) {
					feeds = new Feeds(feed, "4.0 kgs\n(1.33 kgs/serving)");
				} else if (changeInDate <= 29) {
					feeds = new Feeds(feed, "3.0 kgs\n(1 kg/serving)");
				} else {
					feeds = new Feeds(feed, "2.5 kgs\n(0.83 kg/serving)");
				}
			} else if (pregnancyDate > 0) {
				changeInDate = (int) ((System.currentTimeMillis() / 1000 - pregnancyDate) / 86400);
				if (changeInDate >= 1 && changeInDate <= 21) {
					feeds = new Feeds("NUTRI-GRO", "1.50 kgs/day\n(0.5 kg/serving)");
				} else if (changeInDate >= 22 && changeInDate <= 105) {
					String feed = "PREG-SOW";
					if (changeInDate <= 30) {
						feeds = new Feeds(feed, "1.75 kgs/day\n(0.58 kg/serving)");
					} else if (changeInDate <= 61) {
						feeds = new Feeds(feed, "2.00 kgs/day\n(0.67 kg/serving)");
					} else if (changeInDate <= 91) {
						feeds = new Feeds(feed, "2.25 kgs/day\n(0.75 kg/serving)");
					} else {
						feeds = new Feeds(feed, "2.00 kgs/day\n(0.67 kg/serving)");
					}
				} else if (changeInDate >= 106) {
					String feed = "LITTER-SAVER";
					if (changeInDate <= 109) {
						feeds = new Feeds(feed, "1.75 kgs/day\n(0.58 kg/serving)");
					} else {
						feeds = new Feeds(feed, "1.00 kg/day\n(0.33 kg/serving)");
					}
				}
			}
		}
		
		return feeds;
	}
	
	public class Feeds {
		public String feed;
		public String consumption;
		
		public Feeds(String feed, String consumption) {
			super();
			this.feed = feed;
			this.consumption = consumption;
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return feed + " " + consumption;
		}
	}
}
