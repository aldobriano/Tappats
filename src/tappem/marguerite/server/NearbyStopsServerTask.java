package tappem.marguerite.server;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import tappem.marguerite.BusLine;
import tappem.marguerite.BusStop;
import tappem.marguerite.threads.DownloadTask;
import tappem.marguerite.xml.MargueriteXMLHandler;
import tappem.marguerite.xml.NearbyStopXMLHandler;
import tappem.marguerite.xml.XMLModel;
import tappem.marguerite.xml.XMLModelList;

import android.util.Log;

/**
 * Gets the next bus from margueritenfc.heroku.com/nextbus/?
 * 
 */
public class NearbyStopsServerTask extends DownloadTask {

	private static final String TAG = NearbyStopsServerTask.class.getSimpleName();
	
	private double lat;
	private double lon;
	
	
	private ArrayList<BusStop> elementToReturn;
	private int status = 0;  // 0 - working,  -1  fail,  1 completed
	
	public NearbyStopsServerTask(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
		
	}
	
	@Override
	public void run() {
		status = 0;
		try {
			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			/** Send URL to parse XML Tags */
			URL sourceUrl = new URL(
			"http://margueritenfc.heroku.com/get_nearby?lat=" + lat + "&lon=" + lon);
			/** Create handler to handle XML Tags ( extends DefaultHandler ) */
			NearbyStopXMLHandler myXMLHandler = new NearbyStopXMLHandler();
			xr.setContentHandler(myXMLHandler);

			xr.parse(new InputSource(sourceUrl.openStream()));




			/** Get result from MyXMLHandler SitlesList Object */
			XMLModelList parsedInfo = myXMLHandler.getNearbyStops();
			
			

			elementToReturn = parsedInfo.getAllStops();
			
			status = 1;
		} catch (Exception e) {
			Log.e(TAG, "Error in DownloadTask");
			e.printStackTrace();
			status = -1;
		}
		
		
	}
	
	public int getStatus()
	{
		return status;
	}
	
	public ArrayList<BusStop> getResult()
	{
		
		if(status == 1)
			return elementToReturn;
		else
			return null;
	}
}
