package tappem.marguerite.xml;


import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import tappem.marguerite.BusLine;
import tappem.marguerite.BusStop;

public class XMLParsing {

	public static void main(String[] args)
	{
		System.out.println("HELLO");
		try{
			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			/** Send URL to parse XML Tags */
			URL sourceUrl = new URL(
			"http://margueritenfc.heroku.com/nextbus/66?phoneid=Aldo?userinterface=nfc");
			/** Create handler to handle XML Tags ( extends DefaultHandler ) */
			MargueriteXMLHandler myXMLHandler = new MargueriteXMLHandler();
			xr.setContentHandler(myXMLHandler);
			xr.parse(new InputSource(sourceUrl.openStream()));



			/** Get result from MyXMLHandler SitlesList Object */
			XMLModel parsedInfo = myXMLHandler.getNextBus();

			BusStop stop = parsedInfo.getStop();
			System.out.println("Bus Stop:  name " + stop.getStopLabel() + "   id = " + stop.getStopId());

			ArrayList<BusLine> all = parsedInfo.getBusLines();
			for(BusLine bus: all)
			{
				System.out.println("--------------  Bus Line ---------");
				System.out.println("Minuste left: " + bus.getEstimatedDepartureTime());
				System.out.println("Icon of bus: " + bus.getIcon());
				System.out.println("Direction: " + bus.getServiceTo());
				System.out.println("Name:  " + bus.getLineName());
				System.out.println("Status:  " + bus.getStatus());
			}
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}
	}
}
