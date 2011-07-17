package tappem.marguerite;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.maps.GeoPoint;


public class BusStop {

	private String stopId;
	private String stopLabel;
	private GeoPoint location;
	private ArrayList<BusLine> busLines;
	private String tagId;
	
	
	
	
	public BusStop(int stopId, String stopLabel)
	{
		this.stopId = Integer.toString(stopId);
		this.stopLabel = stopLabel;
		this.location = null;
		this.busLines = null;
		this.tagId = null;
		
	}
	
	public BusStop(String stopId, String stopLabel, GeoPoint location)
	{
		this.stopId = stopId;
		this.stopLabel = stopLabel;
		this.location = location;
		this.busLines = null;
		
	}
	public BusStop(String stopId, String stopLabel, String tagId, ArrayList<BusLine> busLines)
	{
		this.stopId = stopId;
		this.stopLabel = stopLabel;
		this.location = null;
		this.busLines = busLines;
		this.tagId = tagId;
	}
	public BusStop(String stopId, String stopLabel, ArrayList<BusLine> busLines)
	{
		this.stopId = stopId;
		this.stopLabel = stopLabel;
		this.location = null;
		this.busLines = busLines;
		this.tagId = null;
	}
	public BusStop(String stopId, String stopLabel, String tagId, GeoPoint location, ArrayList<BusLine> busLines)
	{
		this.stopId = stopId;
		this.stopLabel = stopLabel;
		this.location = location;
		this.busLines = busLines;
		this.tagId = tagId;
		
	}
	
	public BusStop(String string, String string2) {
		this(string,string2,new ArrayList<BusLine>());
	}

	public static HashMap<Integer,String> populateBusStopList(){
		HashMap<Integer, String> stops = new HashMap<Integer,String>();
		stops.put(166, "1050 Arastradero");
		stops.put(188, "1050 Arastradero (AM)");
		stops.put(193, "1050 Arastradero (PM)");
		stops.put(134, "1101 Welch");
		stops.put(159, "1500 Page Mill");
		stops.put(145, "217 Bowdoin");
		stops.put(144, "227 Bowdoin");
		stops.put(182, "3145 Porter (Cir.)");
		stops.put(199, "3145 Porter Drive");
		stops.put(146, "3300 Hillview");
		stops.put(149, "3373 Hillview");
		stops.put(187, "3412 Hillview");
		stops.put(194, "3431 Hillview");
		stops.put(190, "3475 Deer Creek (AM)");
		stops.put(191, "3475 Deer Creek (PM)");
		stops.put(189, "3495 Deer Creek (AM)");
		stops.put(192, "3495 Deer Creek (PM)");
		stops.put(137, "770 Welch");
		stops.put(135, "777 Welch");
		stops.put(136, "900 Welch");
		stops.put(166, "Arastradero, 1050");
		stops.put(193, "Arastradero, 1050 (PM)");
		stops.put(46, "BarnesCT@Olmsted");
		stops.put(53, "Birch@Cal.AVE");
		stops.put(107, "Blake Wilbur");
		stops.put(141, "Blake Wilbur@Pasteur");
		stops.put(145, "Bowdoin, 217");
		stops.put(144, "Bowdoin, 227");
		stops.put(176, "Building 41");
		stops.put(174, "Building 84");
		stops.put(51, "Cal.AVE@AshAVE");
		stops.put(52, "Cal.AVE@Birch");
		stops.put(49, "Cal.AVE@ElCamino");
		stops.put(118, "Cal.AVE@Hanover");
		stops.put(117, "Cal.AVE@Wellesley");
		stops.put(115, "Cal.AVE@Yale");
		stops.put(55, "Cal.AVE Caltrain");
		stops.put(50, "Cal.AVE Near Ash Ave.");
		stops.put(55, "Caltrain@Cal.AVE ");
		stops.put(66, "Caltrain@Palo Alto Transit Center");
		stops.put(96, "Campus Oval");
		stops.put(24, "CampusDR@Alvarado Row");
		stops.put(19, "CampusDR@Bodwoin");
		stops.put(17, "CampusDR@EscondidoRD");
		stops.put(26, "CampusDR@Mayfld S");
		stops.put(25, "CampusDR@Mayfld N");
		stops.put(18, "CampusDR@Mirrelees");
		stops.put(82, "CampusDR@MSOB");
		stops.put(81, "CampusDR@PanamaST");
		stops.put(14, "CampusDR@SerraST");
		stops.put(23, "CampusDR@Stern Hall");
		stops.put(21, "CampusDR@Vaden HCtr");
		stops.put(178, "CampusDR@Via Ortega");
		stops.put(22, "CampusDR@Wilbur Hl");
		stops.put(20, "CampusDR@Wilbur Lot");
		stops.put(139, "Cancer Center");
		stops.put(151, "Classic-Hyatt");
		stops.put(186, "Coyote Hill@Hillview");
		stops.put(190, "Deer Creek, 3475 (AM)");
		stops.put(191, "Deer Creek, 3475 (PM)");
		stops.put(189, "Deer Creek, 3495 (AM)");
		stops.put(192, "Deer Creek, 3495 (PM)");
		stops.put(110, "EHS on Oak Rd.");
		stops.put(127, "ElCamino@Cal.AVE 2 SA ShopCTR");
		stops.put(126, "ElCamino@Cal.AVE 2 Campus");
		stops.put(165, "ElCamino@Galvez");
		stops.put(64, "ElCamino@HomerAVE");
		stops.put(198, "ElCamino@Page Mill");
		stops.put(63, "ElCamino@TwnCountry");
		stops.put(65, "ElCamino@WellsAVE");
		stops.put(68, "Emerson@LyttonPZA");
		stops.put(110, "Env. Hlth. Safety on Oak Rd.");
		stops.put(175, "Fire Station");
		stops.put(7, "Galvez@Alumni Ctr.");
		stops.put(62, "Galvez@ElCamino");
		stops.put(5, "Galvez@Memorial Way");
		stops.put(6, "Galvez@Montag Hl");
		stops.put(97, "Galvez@Serra");
		stops.put(8, "Galvez@Track Hse.");
		stops.put(173, "Gate 17");
		stops.put(121, "Hanover@California (to VA)");
		stops.put(119, "Hanover@Page Mill (to VA)");
		stops.put(149, "Hillview, 3373");
		stops.put(187, "Hillview, 3412");
		stops.put(194, "Hillview, 3431");
		stops.put(185, "Hillview@Porter");
		stops.put(195, "Hillview@Coyote Hill");
		stops.put(100, "Hoover Pav.-PaloRD");
		stops.put(39, "HoskinsCt@OlmstedRD");
		stops.put(43, "HulmeCt@OlmstedRD");
		stops.put(151, "Hyatt-Classic");
		stops.put(30, "LomitaDR@NG Sculpture Garden");
		stops.put(29, "LomitaDR@ Santa Theresa");
		stops.put(67, "Lytton@AlmaST");
		stops.put(74, "Medical Center@QuarryRD");
		stops.put(112, "Medical Foundation, Palo Alto");
		stops.put(103, "Medical School@QuarryRD");
		stops.put(147, "Miranda@Hillview");
		stops.put(109, "OakRD@EHS");
		stops.put(109, "OakRD@Env. Hlth. Safety");
		stops.put(45, "Olmsted@BarnesCT");
		stops.put(40, "Olmsted@HoskinsCT");
		stops.put(44, "Olmsted@HulmeCT");
		stops.put(143, "Olmsted@Oberlin");
		stops.put(42, "Olmsted@ThoburnCT");
		stops.put(142, "Olmsted@Wellesley");
		stops.put(47, "Olmsted@Yale");
		stops.put(96, "Oval");
		stops.put(159, "Page Mill, 1500");
		stops.put(183, "Page Mill&amp;ElCamino");
		stops.put(184, "Page Mill@Hanover AM");
		stops.put(197, "Page Mill@Hanover PM");
		stops.put(164, "Page Mill@Peter Coutts E");
		stops.put(163, "Page Mill@Peter Coutts W");
		stops.put(120, "Page Mill@Hanover");
		stops.put(112, "Palo Alto Medical Foundation");
		stops.put(66, "Palo Alto Transit Center");
		stops.put(100, "PaloRD@Hoover Pav.");
		stops.put(32, "Panama@Roble Gym");
		stops.put(93, "Panama@Via Ortega.");
		stops.put(87, "Parking Structure 5-To Campus");
		stops.put(89, "Parking Structure 5-Away from Campus");
		stops.put(106, "Pasteur@PS3");
		stops.put(160, "Pasteur@Sand Hill");
		stops.put(105, "Pasteur@Stanford Hospital");
		stops.put(196, "Hillview@Porter");
		stops.put(199, "Porter, 3145");
		stops.put(180, "PS1-RothWAY@CampusDR ");
		stops.put(89, "PS5-Away from Campus");
		stops.put(87, "PS5-To Campus");
		stops.put(72, "Quarry@Arboretum");
		stops.put(101, "Quarry@Hoover Pav.");
		stops.put(73, "Quarry@Medical Ctr.");
		stops.put(75, "Quarry@Medical Sch.");
		stops.put(70, "Quarry@Psych Lot");
		stops.put(71, "Quarry@Vineyard");
		stops.put(69, "Quarry@WelchRD");
		stops.put(180, "RothWAY@CampusDR-PS1");
		stops.put(31, "SamMorWay@Panama");
		stops.put(130, "San Antonio Shopping Center");
		stops.put(92, "Sand Hill@Oak Creek Apts.");
		stops.put(161, "Sand Hill@Pasteur");
		stops.put(85, "Sand Hill@SharonRD");
		stops.put(91, "Sand Hill@Stock Farm");
		stops.put(37, "Santa Theresa@Governors Cor");
		stops.put(38, "Santa Theresa@Los Arboles");
		stops.put(35, "Santa Theresa@Roble Fld.");
		stops.put(36, "Santa Theresa@West Res.");
		stops.put(78, "Serra@AllenBLD");
		stops.put(10, "Serra@BurnhamPAV");
		stops.put(153, "Serra@El Camino");
		stops.put(11, "Serra@Encina Hall");
		stops.put(79, "Serra@GatesBLD");
		stops.put(4, "Serra@Hoover Tower");
		stops.put(12, "Serra@KnigntCTR");
		stops.put(95, "Serra@Lasuen Mall");
		stops.put(2, "Serra@Main Quad");
		stops.put(13, "Serra@ManzanitaFLD");
		stops.put(3, "Serra@Mem. Aud.");
		stops.put(1, "Serra@Oval");
		stops.put(80, "Serra@PackardBLD");
		stops.put(99, "Serra@Schwab Res.CTR");
		stops.put(76, "Serra@Via Ortega");
		stops.put(86, "Sharon@Sand Hill");
		stops.put(84, "SLAC-SLAC Hotel");
		stops.put(108, "Stanford West Apts.");
		stops.put(88, "Stock Farm Lot@OakRD");
		stops.put(90, "Stock Farm Lot@STKFRM");
		stops.put(41, "ThoburnCT@Olmsted");
		stops.put(55, "Transit CTR-Cal.AVE ");
		stops.put(66, "Transit CTR-Palo Alto");
		stops.put(27, "Tresidder Union");
		stops.put(28, "Tresidder Union Lot");
		stops.put(148, "VA Hospital");
		stops.put(177, "Via Ortega@Campus");
		stops.put(162, "Via Ortega@Cogen");
		stops.put(94, "Via Ortega@Panama");
		stops.put(138, "Via Ortega@Serra Mall");
		stops.put(134, "Welch, 1101");
		stops.put(137, "Welch, 770");
		stops.put(135, "Welch, 777");
		stops.put(136, "Welch, 900");
		stops.put(48, "Yale@Olmsted");
		
		return stops;
	}
	
		
	public boolean isEmpty()
	{
		if(stopLabel.equals("") && stopId.equals(""))
			return true;
		return false;
	}
	public String getStopId() {
		return stopId;
	}
	public void setStopId(String stopId) {
		this.stopId = stopId;
	}
	public String getStopLabel() {
		return stopLabel;
	}
	public void setStopLabel(String stopLabel) {
		this.stopLabel = stopLabel;
	}
	
	public GeoPoint getLocation() {
		return location;
	}

	public void setLocation(GeoPoint location) {
		this.location = location;
	}
	
	
	public ArrayList<BusLine> getBusLines() {
		return busLines;
	}

	public void setBusLines(ArrayList<BusLine> busLines) {
		this.busLines = busLines;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String toString()
	{
		return stopLabel;
	}
	
}
