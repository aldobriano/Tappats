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
import tappem.marguerite.xml.TagsXMLHandler;
import tappem.marguerite.xml.XMLModel;

import android.util.Log;

/**
 * Gets the next bus from margueritenfc.heroku.com/tags/?.xml
 * 
 */
public class NfcTagServerTask extends DownloadTask {

	private static final String TAG = NfcTagServerTask.class.getSimpleName();
	
	private String tagId;
	
	
	private String elementToReturn;
	private int status = 0;  // 0 - working,  -1  fail,  1 completed
	
	public NfcTagServerTask(String tagId) {
		this.tagId = tagId;
		
	}
	
	@Override
	public void run() {
		System.out.println("Running nfc");
		status = 0;
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


		 elementToReturn = myXMLHandler.getStopId();

//			/** Get result from MyXMLHandler SitlesList Object */
//			XMLModel parsedInfo = myXMLHandler.getNextBus();
//			
//			

			

			
			
			status = 1;
		} catch (Exception e) {
			elementToReturn = "ERROR";
			Log.e(TAG, "Error in DownloadTask");
			e.printStackTrace();
			status = -1;
		}
		
		
	}
	
	public int getStatus()
	{
		return status;
	}
	
	public String getResult()
	{
		if(status == 1)
			return elementToReturn;
		else
			return null;
	}
}
