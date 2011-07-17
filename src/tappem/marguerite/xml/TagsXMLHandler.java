
package tappem.marguerite.xml;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class TagsXMLHandler extends DefaultHandler{


	Boolean currentElement = false;
	String currentValue = null;
	 
	String stop_id;
	String tag_id;
	
	

	public String getStopId() {
		return stop_id;
	}
	public String getTagId()
	{
		return tag_id;
	}
	

	/** Called when tag starts ( ex:- <bus_stop name="" id="">AndroidPeople</name>
	 * -- <name> )*/

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (qName.equals("tag"))
		{
			/** Start */
			stop_id = "";
			tag_id = "";
			
		}	
		

	}

	/** Called when tag closing ( ex:- <name>AndroidPeople</name>
	 * -- </name> )*/

	public void endElement(String uri, String localName, String qName)
	throws SAXException {

		currentElement = false;

		/** set value */
		if (qName.equalsIgnoreCase("stop-id"))
			stop_id = currentValue;
		else if (qName.equalsIgnoreCase("id"))
			tag_id = currentValue;
		
			
		

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
