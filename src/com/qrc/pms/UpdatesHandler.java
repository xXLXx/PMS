package com.qrc.pms;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.qrc.pms.model.TopFiveEntryList;

public class UpdatesHandler extends DefaultHandler{

	List<TopFiveEntryList> listEntry;
	String curTag;
	String address = "", numbers = "";
	
	
	public UpdatesHandler() {
		super();
		// TODO Auto-generated constructor stub
		listEntry = new ArrayList<TopFiveEntryList>();
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		Log.e("Handler", "Handler");
		String tmp = new String(ch, start, length);
		if(!tmp.trim().equals(""))
		{
			if(curTag.equals("address"))
			{
				address = address.concat(tmp);
			}
			else if(curTag.equals("numbers"))
			{
				numbers = numbers.concat(tmp);
			}
		}
		
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if(localName.equals("request"))
		{
			listEntry.add(new TopFiveEntryList(address, Integer.valueOf(numbers)));
			address = "";
			numbers = "";
		}
		else if(localName.equals("requests"))
		{
			throw new SaxTerminateException();
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		curTag = localName;
	}
	
	public List<TopFiveEntryList> getListEntry()
	{
		return listEntry;
	}
}
