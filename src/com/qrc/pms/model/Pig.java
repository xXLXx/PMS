package com.qrc.pms.model;

import java.text.SimpleDateFormat;
import java.util.Locale;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.qrc.pms.R;

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
	/**
	 * For pregnant date or date of giving birth
	 */
	private int pregnancyDate = 0;
	private int milkingDate = 0;
	public int pregnancyCount = 0;
	
	/**
	 * Optional fields
	 */
	public String groupName = "";
	public int count = 1;
	private long dateAdded = 0;
	
	private String id;
	
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
		this("", purpose, birthDate, groupName, count, 0, 0, System.currentTimeMillis(), 0);
	}
	
	public Pig(String id, int purpose, int birthDate, String groupName, int count, int pregnancyDate, int milkingDate, long dateAdded,
			int pregnancyCount) {
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
	}
	
	public static void setPurposeList(Resources res) {
		PURPOSE_LIST = res.getStringArray(R.array.purpose_list);
	}
	
	public String getId() {
		return "" + dateAdded;
	}
	
	public String getGroupName() {
		return groupName.equals("") ? "Pig Added " + formatDate(dateAdded) : groupName;
	}
	
	public Bitmap getQrCodeBitmap() {
		try {
			return ((QRCode) QRCode
					.from(getSerializedString(true))
					.to(ImageType.JPG)
					.withSize(400, 400)
					.withHint(EncodeHintType.CHARACTER_SET, "UTF-8")
					.withErrorCorrection(ErrorCorrectionLevel.H))
					.bitmap();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String getSerializedString(boolean minimal) throws JSONException {
		return getSerializedObject(minimal).toString();
	}
	
	public Object getSerializedObject(boolean minimal) throws JSONException {
		JSONObject data = new JSONObject();
		data.put("purpose", purpose);
		data.put("birthdate", birthDate);
		data.put("dateAdded", dateAdded);
		data.put("groupName", groupName);
		
		if (!minimal) {
			data.put("id", id);
			data.put("count", count);
			data.put("pregnancyDate", pregnancyDate);
			data.put("milkingDate", milkingDate);
			data.put("pregnancyCount", pregnancyCount);
		}
		
		return data;
	}
	
	public static Pig getPig(SharedPreferences prefs, String id) {
		return Pig.getPig(prefs.getString(id, ""));
	}
	
	public static Pig getPig(String json) {
		try {
			JSONObject data = new JSONObject(json);
			return new Pig(
					data.getString("id"),
					data.getInt("purpose"),
					data.getInt("birthdate"),
					data.getString("groupName"),
					data.getInt("count"),
					data.getInt("pregnancyDate"),
					data.getInt("milkingDate"),
					data.getLong("dateAdded"),
					data.getInt("pregnancyCount")
					);
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
	
	public String formatDate(long milliseconds) {
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
			if (milkingDate >= 0) {
				pregnancyCount++;
				pregnancyDate = extraDateSec;
				milkingDate = 0;
			} else {
				pregnancyDate = 0;
				milkingDate = extraDateSec;
			}
		}
	}
	
	public Feeds getFeeds() {
		Feeds feeds = null;
		int changeInDate = 0;
		
		changeInDate = (int) ((System.currentTimeMillis() / 1000 - birthDate) / 86400);
		
		if (changeInDate >= 3 && changeInDate <= 5) {
			feeds = new Feeds("MILKO-PLUS", "50 ml/head/day");
		} else if (changeInDate >= 6 && changeInDate <= 35) {
			String feed = "NUTRILAC BOOSTER CRUMBLE";
			if (changeInDate <= 28) {
				feeds = new Feeds(feed, "20 grams/head/day");
			} else {
				feeds = new Feeds(feed, "300 grams/head/day");
			}
		} else if (changeInDate >= 36 && changeInDate <= 42) {
			feeds = new Feeds("NUTRILAC-PLUS PELLET", "350 grams/head/day");
		} else if (changeInDate >= 42 && changeInDate <= 55) {
			feeds = new Feeds("NUTRILAC-PLUS PELLET", "0.5 to 0.8 kg per day");
		} else if (changeInDate >= 55 && changeInDate <= 90) {
			feeds = new Feeds("NUTRI-START", "1.25 to 1.5 kg per day");
		} else if (changeInDate >= 90 && changeInDate <= 120) {
			feeds = new Feeds("NUTRI-GRO", "1.75 to 2.0 kg per day");
		} else if (
				(changeInDate >= 120 && changeInDate <= 150) ||
				(changeInDate > 150 && (purpose == PURPOSE_FATTENING ||
				(purpose == PURPOSE_SOW && milkingDate <= 0 && pregnancyDate <= 0)))) {
			feeds = new Feeds("NUTRI-BIG", "2.3 to 2.5 kg per day");
		} else if (purpose == PURPOSE_SOW) {
			if (milkingDate > 0) {
				int changeInDateSec = (int) (System.currentTimeMillis() / 1000 - milkingDate);
				changeInDate = (int) (changeInDateSec / 86400);
				String feed = "LITTER-SAVER";
				
				if (changeInDateSec / 3600 <= 6) {
					feeds = new Feeds("Minimal feeding (as needed) " + feed, "Maximum of 100 grams");
				} else if (changeInDate >= 2 && changeInDate <= 27) {
					feeds = new Feeds(feed, "3 kgs. plus 250g per piglet");
				} else if (changeInDate <= 28) {
					feeds = new Feeds(feed, "4.0 kgs");
				} else if (changeInDate <= 29) {
					feeds = new Feeds(feed, "3.0 kgs");
				} else {
					feeds = new Feeds(feed, "2.5 kgs");
				}
			} else if (pregnancyDate > 0) {
				changeInDate = (int) ((System.currentTimeMillis() / 1000 - pregnancyDate) / 86400);
				if (changeInDate >= 1 && changeInDate <= 21) {
					feeds = new Feeds("NUTRI-GRO", "1.50 kgs/day");
				} else if (changeInDate >= 22 && changeInDate <= 105) {
					String feed = "PREG-SOW";
					if (changeInDate <= 30) {
						feeds = new Feeds(feed, "1.75 kgs/day");
					} else if (changeInDate <= 61) {
						feeds = new Feeds(feed, "2.00 kgs/day");
					} else if (changeInDate <= 91) {
						feeds = new Feeds(feed, "2.25 kgs/day");
					} else {
						feeds = new Feeds(feed, "2.00 kgs/day");
					}
				} else if (changeInDate >= 106) {
					String feed = "LITTER-SAVER";
					if (changeInDate <= 109) {
						feeds = new Feeds(feed, "1.75 kgs/day");
					} else {
						feeds = new Feeds(feed, "1.00 kg/day");
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
		
	}
}
