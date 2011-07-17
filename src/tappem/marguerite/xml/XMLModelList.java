package tappem.marguerite.xml;


import java.util.ArrayList;

import com.google.android.maps.GeoPoint;

import tappem.marguerite.BusLine;
import tappem.marguerite.BusStop;

public class XMLModelList {

	private ArrayList<BusStop> stops;
	private BusStop currentStop;
	private ArrayList<BusLine> busLines = new ArrayList<BusLine>();
	private BusLine temp;
	
	public XMLModelList()
	{
		stops = new ArrayList<BusStop>();
	}
	public void setBusLineName(String name)
	{
		if(temp == null)
			temp = new BusLine();
		
		temp.setLineName(name);
	}
	public void setBusLineIcon(String iconpath)
	{
		if(temp == null)
			temp = new BusLine();
		
		temp.setIcon(iconpath);
	}
	
	public void setBusLineMinutes(String mins)
	{
		if(temp == null)
			temp = new BusLine();
		
		temp.setEstimatedDepartureTime(mins);
	}
	
	public void setBusLineStatus(String status)
	{
		if(temp == null)
			temp = new BusLine();
		
		temp.setStatus(status);
	}
	
	public void setStopLocation(GeoPoint location)
	{
		currentStop.setLocation(location);
	}
	
	public void setBusLineDirection(String serviceto)
	{
		if(temp == null)
			temp = new BusLine();
		temp.setServiceTo(serviceto);
	}
	
	public void addBusLine(){
		if(temp != null && !temp.isEmpty()){
			System.out.println("Adding line in addBusLine()");
			busLines.add(temp);
			temp = new BusLine();
		}
		System.out.println("NOT Adding line in addBusLine()");
		temp = new BusLine();
	}
	public void addBusStop()
	{
		if(currentStop != null && !currentStop.isEmpty()){
			System.out.println("ADDING " + stops.size() + "A BUS STOP " + currentStop.getStopLabel());
			ArrayList<BusLine> tempList = new ArrayList<BusLine>();
			for(BusLine l : busLines)
			{
				tempList.add(l);
			}
			stops.add(new BusStop(currentStop.getStopId(), currentStop.getStopLabel(),tempList));
			busLines = new ArrayList<BusLine>();
			currentStop = null;
			System.out.println(" MY SIZE: " + stops.size());
		}
	}
	public ArrayList<BusStop> getAllStops() {
		System.out.println(stops.size() + "     ALLL");
		return stops;
	}
	public void setStop(BusStop stop) {
		this.currentStop = stop;
	}
	public ArrayList<BusLine> getBusLines() {
		return busLines;
	}
	public void setBusLines(ArrayList<BusLine> busLines) {
		this.busLines = busLines;
	}
	
	
	
	
}
