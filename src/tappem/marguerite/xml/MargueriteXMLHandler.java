package tappem.marguerite.xml;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import tappem.marguerite.BusStop;

public class MargueriteXMLHandler extends DefaultHandler{


	Boolean currentElement = false;
	String currentValue = null;
	 XMLModel nextbus = null;
	

	public XMLModel getNextBus() {
		return nextbus;
	}

	

	/** Called when tag starts ( ex:- <bus_stop name="" id="">AndroidPeople</name>
	 * -- <name> )*/

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;
		if (qName.equals("bus_stop"))
		{
			/** Start */
			nextbus = new XMLModel();
			String attr = attributes.getValue("name");
			String attr2 = attributes.getValue("id");
			nextbus.setStop(new BusStop(Integer.parseInt(attr2), attr));
		} else if (qName.equals("bus_line")) {
			nextbus.addBusLine();
			/** Get attribute value */		
			String attr = attributes.getValue("name");
			nextbus.setBusLineName(attr);
		}

	}

	/** Called when tag closing ( ex:- <name>AndroidPeople</name>
	 * -- </name> )*/

	public void endElement(String uri, String localName, String qName)
	throws SAXException {

		currentElement = false;

		/** set value */
		if (qName.equalsIgnoreCase("icon_label"))
			nextbus.setBusLineIcon(currentValue);
		else if (qName.equalsIgnoreCase("minutes_to_bus"))
			nextbus.setBusLineMinutes(currentValue);
		else if (qName.equalsIgnoreCase("status"))
			nextbus.setBusLineStatus(currentValue);
		else if (qName.equalsIgnoreCase("direction"))
			nextbus.setBusLineDirection(currentValue);
		else if (qName.equalsIgnoreCase("bus_stop"))
			nextbus.addBusLine();
		

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
