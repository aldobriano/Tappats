package tappem.marguerite;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import tappem.marguerite.xml.MargueriteXMLHandler;
import tappem.marguerite.xml.TagsXMLHandler;
import tappem.marguerite.xml.XMLModel;


public class MargueriteTransportation {
	private String client;
	public static String TAG_ID = "TAG_ID";
	private HashMap<Integer,String> stops;
	public static String noMoreBuses = "No More Buses Today";
	public MargueriteTransportation(String name)
	{
		client = name;
		stops = BusStop.populateBusStopList();
	}

	public BusStop getNextBuses(String tag) throws IOException
	{
		//this should query the server with the client info and the tagid to keep a log of the users usage	
		//TODO
		String stopId = getStopIdFromTagId(tag);

		return getNextBusesWithStopId(stopId, NextBus.uniqueId, "nfc");

	}
	public ArrayList<BusStop> getStopsNearby(double lat, double lon) throws IOException
	{
		//TODO
		BusLine a = new BusLine("Line X");
		a.setIcon("x");
		
		ArrayList<BusLine> testLine = new ArrayList<BusLine>();
		testLine.add(a);
		
		BusStop one = new BusStop("66","Palo Alto Transit Center", testLine);
		BusStop two = new BusStop("67","Lytton @ Alma", testLine);
		BusStop three = new BusStop("68","Lytton Plaza", testLine);
		BusStop four = new BusStop("69","Palo Medical Foundation", testLine);
		BusStop five = new BusStop("70","Pill Hill & Bowdoin", testLine);
		
		ArrayList<BusStop> e2r = new ArrayList<BusStop>();
		e2r.add(one);
		e2r.add(two);
		e2r.add(three);
		//e2r.add(four);
		//e2r.add(five);
		
		return e2r;
	}
	public String getStopIdFromTagId(String tagId)
	{
		try {
			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			/** Send URL to parse XML Tags */
			URL sourceUrl = new URL(
			"http://margueritenfc.heroku.com/tags/" + tagId + ".xml");
			/** Create handler to handle XML Tags ( extends DefaultHandler ) */
			TagsXMLHandler myXMLHandler = new TagsXMLHandler();
			xr.setContentHandler(myXMLHandler);

			xr.parse(new InputSource(sourceUrl.openStream()));


			
			return myXMLHandler.getStopId();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		


		
	}
	public String getStopLabel(String stopId)
	{
		//TODO
		return stops.get(Integer.parseInt(stopId));
	}
	public BusStop getNextBusesWithStopId(String stopId, String phoneId, String userInterface) throws IOException
	{
		
		
		try {
			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			/** Send URL to parse XML Tags */
			URL sourceUrl = new URL(
			"http://margueritenfc.heroku.com/nextbus/" + stopId + "?phoneid=" + phoneId + "?userinterface=" + userInterface);
			/** Create handler to handle XML Tags ( extends DefaultHandler ) */
			MargueriteXMLHandler myXMLHandler = new MargueriteXMLHandler();
			xr.setContentHandler(myXMLHandler);

			xr.parse(new InputSource(sourceUrl.openStream()));




			/** Get result from MyXMLHandler SitlesList Object */
			XMLModel parsedInfo = myXMLHandler.getNextBus();
			
			

			ArrayList<BusLine> buses = parsedInfo.getBusLines();
			
			BusStop b2r = parsedInfo.getStop();
//			for(BusLine bus: buses)
//			{
//				System.out.println("--------------  Bus Line ---------");
//				System.out.println("Minuste left: " + bus.getEstimatedDepartureTime());
//				System.out.println("Icon of bus: " + bus.getIcon());
//				System.out.println("Direction: " + bus.getServiceTo());
//				System.out.println("Name:  " + bus.getLineName());
//				System.out.println("Status:  " + bus.getStatus());
//			}



			Collections.sort(buses);

			
			b2r.setBusLines(buses);
			return b2r;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	static String getTime()
	{
		long now = System.currentTimeMillis();

		Date d = new Date(now);
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return String.format("%tl:%tM %tp", now, d, c);

	}
	/**
	 * in hh:mm AM/PM
	 * @param time1
	 * @param time2
	 * @return
	 */
	static String getRemainingTime(String time1, String time2)
	{
		int hour1 = 0;
		int hour2 = 0;
		if(time1.contains("PM") || time1.contains("pm"))
		{
			hour1+= 12;
		}
		if(time2.contains("PM") || time2.contains("pm"))
		{
			hour2+= 12;
		}

		String time1Array[] = time1.split(":");
		String time2Array[] = time2.split(":");

		hour1+= Integer.parseInt(time1Array[0]);
		hour2+= Integer.parseInt(time2Array[0]);


		int minutes1 = 0;
		int minutes2 = 0;

		minutes1 = Integer.parseInt(time1Array[1].split(" ")[0]);
		minutes2 = Integer.parseInt(time2Array[1].split(" ")[0]);

		int difMinutes = minutes2 - minutes1;
		int difhours = hour2 - hour1;

		if(difhours != 0)
		{
			difMinutes = difMinutes + 60*Math.abs(difhours);
			//return  "" + difMinutes + " min";
			return  "" + difMinutes;
		}else
		{
			//			if(difMinutes < 0 )
			//				difMinutes = 0;
			//return  "" + difMinutes + " min";
			return  "" + difMinutes;
		}



	}

}


