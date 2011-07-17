package tappem.marguerite.xml;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.android.maps.GeoPoint;

import tappem.marguerite.BusStop;

public class StopsXMLHandler extends DefaultHandler{


	Boolean currentElement = false;
	String currentValue = null;
	 XMLModel model = null;
	int lat;
	int lon;
	String id;
	String label;
	
	

	public XMLModel getModel() {
		return model;
	}

	

	/** Called when tag starts ( ex:- <bus_stop name="" id="">AndroidPeople</name>
	 * -- <name> )*/

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (qName.equals("stop"))
		{
			/** Start */
			model = new XMLModel();
			
		}	
		

	}

	/** Called when tag closing ( ex:- <name>AndroidPeople</name>
	 * -- </name> )*/

	public void endElement(String uri, String localName, String qName)
	throws SAXException {

		currentElement = false;

		/** set value */
		if (qName.equalsIgnoreCase("label"))
			label = currentValue;
		else if (qName.equalsIgnoreCase("lon"))
			lon = (int) (1000000 * Float.parseFloat(currentValue));
		else if (qName.equalsIgnoreCase("id"))
			id = currentValue;
		else if (qName.equalsIgnoreCase("lat"))
			lat = (int) (1000000 * Float.parseFloat(currentValue));
		else if (qName.equalsIgnoreCase("stop"))
			model.setStop(new BusStop(id, label, new GeoPoint(lat,lon)));
			
		

	}

	/** Called to get tag characters ( ex:- <name>AndroidPeople</name>
	 * -- to get AndroidPeople Character ) */

	public void characters(char[] ch, int start, int length)
	throws SAXException {

		if (currentElement) {
			currentValue = new String(ch, start, length);
			currentElement = false;
		}

	}


}
