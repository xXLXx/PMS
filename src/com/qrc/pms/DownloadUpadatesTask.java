package com.qrc.pms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import com.qrc.pms.R;
import com.qrc.pms.adapter.TopFiveListEntryAdapter;
import com.qrc.pms.config.Config;
import com.qrc.pms.model.TopFiveEntryList;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class DownloadUpadatesTask extends AsyncTask<String, Integer, String>
{
	XMLReader xmlReader;
	UpdatesHandler updatesHandler;	
	ListView topfiveList;
	TopFiveListEntryAdapter adapter;
	Activity activity;

	public DownloadUpadatesTask(Activity activity) {
		super();
		// TODO Auto-generated constructor stub
		topfiveList = (ListView) activity.findViewById(R.id.pig_listView);
		adapter = new TopFiveListEntryAdapter(activity.getApplicationContext(), new ArrayList<TopFiveEntryList>());
		topfiveList.setAdapter(adapter);
		this.activity = activity;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		initializeParser();
		try {
			xmlReader.parse(new InputSource(new FileInputStream(Config.FILE_PATH)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SaxTerminateException e) {
			for(final TopFiveEntryList entryList : updatesHandler.getListEntry()) {
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter.add(entryList);
					}
				});
			}
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
		return null;
	}
	
	
	private void initializeParser()
	{
		try {
			xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			updatesHandler = new UpdatesHandler(); 
			xmlReader.setContentHandler(updatesHandler);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
