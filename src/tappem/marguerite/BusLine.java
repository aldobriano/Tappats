package tappem.marguerite;

import java.util.HashMap;

public class BusLine implements Comparable{

	private String lineName ="";
	private String stopId = "";
	private String estimatedDepartureTime = "";
	private String serviceTo = "";
	private String status = "";
	private String icon = "";
	
	private static final HashMap<String,String> busIcons = new HashMap<String,String>(){
	{	
        put("Line A", "a");
        put("A-Line PM EXP", "a"); 
        put("Line B Counter-Clockwise", "bccw");
        put("B-Line CCW EXP", "bccw");
        put("Line B Clockwise", "bcw");
        put("Line C", "c");
        put("Midnight Express", "m");
        put("Medical Center Loop", "mc");
        put("Palm Drive Commuter Express", "palm");
        put("Research Park", "rp");
        put("Shopping Express", "se");
        put("Line S", "slac");
        put("SLAC", "slac");
        put("Medical Center to 1050 Arastradero", "tenfiftya");
        put("Medical Center to Hillview / VA", "va");
        put("Line O Loop", "rp");
    }};
	
    public BusLine()
    {
    	super();
    }
    public BusLine(String lineName)
    {
    	this.lineName = lineName;
    }
    public BusLine(String lineName, String stopId, String estimatedDepartureTime, String serviceTo) {
		new BusLine(lineName,stopId,estimatedDepartureTime,serviceTo,"","");
		
	}
	public BusLine(String lineName, String stopId, String estimatedDepartureTime, String serviceTo, String status, String icon) {
		
		this.lineName = lineName;
		this.stopId = stopId;
		this.estimatedDepartureTime = estimatedDepartureTime;
		this.serviceTo = serviceTo;
		this.status = status;
		this.icon = icon;
	}
	public boolean isEmpty()
	{
		if(lineName.equals("") && stopId.equals("") && estimatedDepartureTime.equals("") && icon.equals(""))
			return true;
		else return false;
	}
	public static String getIconPath(String name)
	{
		return busIcons.get(name);
	}
	
	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}


	public String getStopId() {
		return stopId;
	}


	public void setStopId(String stopId) {
		this.stopId = stopId;
	}





	public String getEstimatedDepartureTime() {
		return estimatedDepartureTime;
	}


	public void setEstimatedDepartureTime(String estimatedDepartureTime) {
		this.estimatedDepartureTime = estimatedDepartureTime;
	}


	public String getServiceTo() {
		return serviceTo;
	}

	public void setServiceTo(String serviceTo) {
		this.serviceTo = serviceTo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getImageStringId()
	{
		//TODO make it dynamic to check which bus line and icon to select
		return "bus_line_icon";
	}
	
	public String toString()
	{
		String tt = "Bus Line Name: " + lineName;
		tt = tt + "  Service To: " + serviceTo;
		return tt + "Estimated Departure: " + estimatedDepartureTime;
	}

	@Override
	public int compareTo(Object arg0) {
		BusLine o = (BusLine) arg0;
		String e = o.getEstimatedDepartureTime();
		Integer other;
		Integer me;
		if(e.equals(MargueriteTransportation.noMoreBuses))
			other = 999999;
		else
			other = Integer.parseInt(e);
		if(estimatedDepartureTime.equals(MargueriteTransportation.noMoreBuses))
			me = 9999999;
		else
			me = Integer.parseInt(estimatedDepartureTime);
		
		return (me.compareTo(other));
	}
}
