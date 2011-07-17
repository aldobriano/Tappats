package tappem.marguerite.xml;


import java.util.ArrayList;

import tappem.marguerite.BusLine;
import tappem.marguerite.BusStop;

public class XMLModel {

	private BusStop stop;
	private ArrayList<BusLine> busLines = new ArrayList<BusLine>();
	private BusLine temp;
	
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
	
	public void setBusLineDirection(String serviceto)
	{
		if(temp == null)
			temp = new BusLine();
		temp.setServiceTo(serviceto);
	}
	
	public void addBusLine(){
		if(temp != null && !temp.isEmpty()){
			busLines.add(temp);
			temp = new BusLine();
		}
		
	}
	public BusStop getStop() {
		return stop;
	}
	public void setStop(BusStop stop) {
		this.stop = stop;
	}
	public ArrayList<BusLine> getBusLines() {
		return busLines;
	}
	public void setBusLines(ArrayList<BusLine> busLines) {
		this.busLines = busLines;
	}
	
	
	
	
}
