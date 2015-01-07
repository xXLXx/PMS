package com.qrc.pms;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.qrc.pms.adapter.NavDrawerListAdapter;
import com.qrc.pms.config.Config;
import com.qrc.pms.helper.ConnectionHelper;
import com.qrc.pms.model.NavDrawerItem;
import com.qrc.pms.model.Pig;
import com.qrc.pms.service.NotifierService;
import com.qrc.pms.service.NotifierService.NotifierBinder;


public class MainActivity extends SherlockFragmentActivity implements LocationListener{
	public DrawerLayout mDrawerLayout;
	public ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private LocationManager locMngr;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; 
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;	
	
	public boolean isAdmin = false;
	
//	public boolean isMain = t;
	// nav drawer title
	public CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	public String[] navMenuTitles;
	public TypedArray navMenuIcons;

	public ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	
	public JSONObject pigs = new JSONObject();
	public SharedPreferences prefs;
	
	NotifierService notifierService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Pig.setPurposeList(this.getResources());
		
		getLocation();
		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// What's hot, We  will add a counter here
//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
		

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		//edited
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//edited
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				//edited
				getSupportActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				//edited
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				//edited
				getSupportActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				//edited
				supportInvalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
			try {
				ConnectionHelper.downloadFile("http://192.168.184.1/csis/dl_list", Config.ROOT_FOLDER, Config.FILE_PATH, false);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		});
		t.start();
		
		prefs = getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}
	
	public boolean addPig(Pig pig) throws JSONException {
//		pigs.put(pig.getId(), pig.getSerializedObject(false));
//		
//		Log.e("asd", pigs.toString());
		prefs.edit().putString(pig.getId(), pig.getSerializedString(false)).commit();
		return false;
	}
	
	public boolean updatePig(Pig pig) throws JSONException {
		prefs.edit().putString(pig.getId(), pig.getSerializedString(false)).commit();
		return false;
	}
	
	public boolean removePig(String id) {
		prefs.edit().remove(id).commit();
		return false;
	}

	//edited
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	//edited
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		//edited
		toggleMenuDrawer();
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	//edited
	public void toggleMenuDrawer() {
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawers();
		}
		else {
			mDrawerLayout.openDrawer(mDrawerList);
		}
	}


	//edited
	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	public void displayView(int position) {
		// update the main content by replacing fragments
		SherlockFragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			if (isAdmin) {
				Log.e("dsfsf", "" + isAdmin);
				navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
			} else {
				// loginfunction
			}
			fragment = new FindPeopleFragment();
			break;
		case 2:
			fragment = new CommunityFragment();
			break;
		case 3:
			fragment = new PhotosFragment();
			break;
		case 4:
			fragment = new PagesFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {
			//edited
			FragmentManager fragmentManager = getSupportFragmentManager();
			//edited
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
	
	private void startNotifierService() {
		Intent i = new Intent(this, NotifierService.class);
		bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
		startService(i);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		startNotifierService();
		super.onResume();
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			NotifierBinder binder = (NotifierBinder) service;
			notifierService = (NotifierService) binder.getService();
			Toast.makeText(getApplicationContext(), "Connected to service", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			notifierService = null;
			Toast.makeText(getApplicationContext(), "Disconnected to service", Toast.LENGTH_SHORT).show();
		}
		
	};

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		//edited
		getSupportActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}


	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		Config.lat = arg0.getLatitude();
		Config.lng = arg0.getLongitude();
		Toast.makeText(this, "" + Config.lat + " " + Config.lng, Toast.LENGTH_LONG).show();
	}


	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	

	
	public void getLocation() {
		Log.e("hhhg", "hjgj");
		
        try {
        	locMngr = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        
        	Log.e("hhhg", "hjgj");
        	
            locMngr.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            
            locMngr.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
	
	
	

}
