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
import tappem.marguerite.xml.XMLModel;

import android.util.Log;

/**
 * Gets the next bus from margueritenfc.heroku.com/nextbus/?
 * 
 */
public class NextBusServerTask extends DownloadTask {

	private static final String TAG = NextBusServerTask.class.getSimpleName();
	
	private String stopId;
	private String phoneId;
	private String userInterface;
	
	private BusStop elementToReturn;
	private int status = 0;  // 0 - working,  -1  fail,  1 completed
	
	public NextBusServerTask(String stopId, String phoneId, String userInterface) {
		this.stopId = stopId;
		this.phoneId = phoneId;
		this.userInterface = userInterface;
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
			"http://margueritenfc.heroku.com/nextbus/" + stopId + "?phoneid=" + phoneId + "?userinterface=" + userInterface);
			/** Create handler to handle XML Tags ( extends DefaultHandler ) */
			MargueriteXMLHandler myXMLHandler = new MargueriteXMLHandler();
			xr.setContentHandler(myXMLHandler);

			xr.parse(new InputSource(sourceUrl.openStream()));




			/** Get result from MyXMLHandler SitlesList Object */
			XMLModel parsedInfo = myXMLHandler.getNextBus();
			
			

			ArrayList<BusLine> buses = parsedInfo.getBusLines();
			
			elementToReturn = parsedInfo.getStop();
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

			
			elementToReturn.setBusLines(buses);
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
	
	public BusStop getResult()
	{
		if(status == 1)
			return elementToReturn;
		else
			return null;
	}
}
