package tappem.marguerite.xml;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.android.maps.GeoPoint;

import tappem.marguerite.BusStop;

public class NearbyStopXMLHandler extends DefaultHandler{


	Boolean currentElement = false;
	String currentValue = null;
	 XMLModelList stops = null;
	 int lat;
	int lon;

	public XMLModelList getNearbyStops() {
		return stops;
	}

	

	/** Called when tag starts ( ex:- <bus_stop name="" id="">AndroidPeople</name>
	 * -- <name> )*/

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		//System.out.println("in startElement " + qName + "  " + localName);
		currentElement = true;
		if (qName.equals("nearby"))
		{
			/** Start */
			stops = new XMLModelList();
		}
		if (qName.equals("bus_stop"))
		{
			
			String attr = attributes.getValue("name");
			String attr2 = attributes.getValue("id");
			//System.out.println("new stop " + attr);
			stops.setStop(new BusStop(Integer.parseInt(attr2), attr));
		} else if (qName.equals("bus_line")) {
		//	System.out.println("added bus line in start");
			stops.addBusLine();
			
		}

	}

	/** Called when tag closing ( ex:- <name>AndroidPeople</name>
	 * -- </name> )*/

	public void endElement(String uri, String localName, String qName)
	throws SAXException {

		currentElement = false;
		//System.out.println("in end element " +  qName + "  " + localName);
		/** set value */
		if (qName.equalsIgnoreCase("icon_label"))
			stops.setBusLineIcon(currentValue);
		else if (qName.equalsIgnoreCase("lon"))
			lon = (int) (1000000 * Float.parseFloat(currentValue));
		else if (qName.equalsIgnoreCase("lat"))
			lat = (int) (1000000 * Float.parseFloat(currentValue));
		else if (qName.equalsIgnoreCase("bus_line"))
			stops.addBusLine();
		else if (qName.equalsIgnoreCase("bus_stop")){
			
			
			//System.out.println("before stop and line " + lat + "  " + lon);
			//stops.setStopLocation(new GeoPoint(lat,lon));
			//System.out.println("before2 stop and line");
			stops.addBusStop();
			
			//System.out.println("added stop and line");
		}

	}

	/** Called to get tag characters ( ex:- <name>AndroidPeople</name>
	 * -- to get AndroidPeople Character ) */

	public void characters(char[] ch, int start, int length)
	throws SAXException {
		currentValue = "";
		if (currentElement) {
			currentValue = new String(ch, start, length);
			currentElement = false;
		}

	}


}
