package com.qrc.pms.model;

import java.text.SimpleDateFormat;
import java.util.Locale;

import net.glxn.qrgen.QRCode;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.Contents;
import com.qrc.pms.R;

public class Pig {
	public static final int PURPOSE_NURSING = 1;
	public static final int PURPOSE_FATTENING = 2;
	public static final int PURPOSE_PREGNANT_SOW = 3;
	public static final int PURPOSE_MILKING_SOW = 4;
	
	public static String[] PURPOSE_LIST = {"Not Specified", "Nursing Piglet", "Fattening Pig", "Pregnant Sow / Gilt", "Milking Sow"};
	
	public int purpose = 0;
	private int birthDate = 0;
	/**
	 * For pregnant date or date of giving birth
	 */
	private int extraDate = 0;
	
	/**
	 * Optional fields
	 */
	private String groupName = "";
	public int count = 1;
	private int dateAdded = 0;
	
	private String id;
	
	public Pig(String id, int purpose, int birthDate) {
		this(id, purpose, birthDate, "", 1, 0);
	}
	
	public Pig(String id, int purpose, int birthDate, String groupName, int count) {
		this(id, purpose, birthDate, groupName, count, 0);
	}
	
	public Pig(String id, int purpose, int birthDate, String groupName, int count, int extraDate) {
		super();
		this.purpose = purpose;
		this.birthDate = birthDate;
		this.groupName = groupName;
		this.count = count;
		this.extraDate = extraDate;
		this.dateAdded = (int) (System.currentTimeMillis() / 1000);
	}
	
	public static void setPurposeList(Resources res) {
		PURPOSE_LIST = res.getStringArray(R.array.purpose_list);
	}
	
	public String getGroupName() {
		return groupName.equals("") ? "Pig Added " + formatDate(dateAdded) : groupName;
	}
	
	public Bitmap getQrCodeBitmap() {
//		return QRCode.from(getSerializedString()).bitmap();
		return null;
	}
	
	public String getSerializedString() throws JSONException {
		JSONObject data = new JSONObject();
		data.put("birthdate", birthDate);
		data.put("dateAdded", dateAdded);
		data.put("id", id);
		return data.toString();
	}
	
	public String getPig(String json) {
		return null;
	}
	
	public int indexOfPurpose(String purpose) {
		for (int x = 0; x < PURPOSE_LIST.length; x++) if (PURPOSE_LIST[x].equals(purpose)) {
			return x;
		}
		
		return 0;
	}
	
	public String formatDate(int seconds) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yy", Locale.ROOT);
		return dateFormat.format(seconds * 1000);
	}
	
	public String getPregnancyDate() {
		if (purpose == PURPOSE_PREGNANT_SOW && extraDate > 0) {
			return formatDate(extraDate);
		}
		
		return "";
	}
	
	public String getGivingBirthDate() {
		if (purpose == PURPOSE_MILKING_SOW && extraDate > 0) {
			return formatDate(extraDate);
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
	
	public Feeds getFeeds() {
		Feeds feeds = null;
		int changeInDate = 0;
		
		switch (purpose) {
		case PURPOSE_NURSING:
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
			}
			break;

		case PURPOSE_FATTENING:
			changeInDate = (int) ((System.currentTimeMillis() / 1000 - birthDate) / 86400);
			if (changeInDate >= 42 && changeInDate <= 55) {
				feeds = new Feeds("NUTRILAC-PLUS PELLET", "0.5 to 0.8 kg per day");
			} else if (changeInDate >= 55 && changeInDate <= 90) {
				feeds = new Feeds("NUTRI-START", "1.25 to 1.5 kg per day");
			} else if (changeInDate >= 90 && changeInDate <= 120) {
				feeds = new Feeds("NUTRI-GRO", "1.75 to 2.0 kg per day");
			} else if (changeInDate >= 120 && changeInDate <= 150) {
				feeds = new Feeds("NUTRI-BIG", "2.3 to 2.5 kg per day");
			}
			break;
			
		case PURPOSE_PREGNANT_SOW:
			changeInDate = (int) ((System.currentTimeMillis() / 1000 - birthDate) / 86400);
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
			} else if (changeInDate >= 106 && changeInDate <= 114) {
				String feed = "LITTER-SAVER";
				if (changeInDate <= 109) {
					feeds = new Feeds(feed, "1.75 kgs/day");
				} else {
					feeds = new Feeds(feed, "1.00 kg/day");
				}
			}
			break;
			
		case PURPOSE_MILKING_SOW:
			int changeInDateSec = (int) (System.currentTimeMillis() / 1000 - birthDate);
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
			} else if (changeInDate <= 30) {
				feeds = new Feeds(feed, "2.5 kgs");
			}
			
			break;
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
