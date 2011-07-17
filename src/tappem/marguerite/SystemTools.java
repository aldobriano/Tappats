package tappem.marguerite;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class SystemTools {

	
	public static boolean checkInternet(Context context)
	{
		
	    boolean HaveConnectedWifi = false;
	    boolean HaveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo)
	    {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                HaveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                HaveConnectedMobile = true;
	    }
	    return HaveConnectedWifi || HaveConnectedMobile;
	}

	public static String getUniqueId(Context c)
	{
		String m_szImei = "";
		//1 compute IMEI
		try{
			TelephonyManager TelephonyMgr = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
			m_szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
		}catch(Exception e)
		{}
		
        //2 compute DEVICE ID
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
        	Build.BOARD.length()%10+ Build.BRAND.length()%10 + 
        	Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 + 
        	Build.DISPLAY.length()%10 + Build.HOST.length()%10 + 
        	Build.ID.length()%10 + Build.MANUFACTURER.length()%10 + 
        	Build.MODEL.length()%10 + Build.PRODUCT.length()%10 + 
        	Build.TAGS.length()%10 + Build.TYPE.length()%10 + 
        	Build.USER.length()%10 ; //13 digits
        
        if(m_szDevIDShort == null)
        	m_szDevIDShort = "";
        //3 android ID - unreliable
        String m_szAndroidID = Secure.getString(c.getContentResolver(), Secure.ANDROID_ID); 
        if(m_szAndroidID == null)
        	m_szAndroidID = "";
        
    	//6 SUM THE IDs
    	String m_szLongID = m_szImei + m_szDevIDShort + m_szAndroidID;
    	MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		m.update(m_szLongID.getBytes(),0,m_szLongID.length());
		byte p_md5Data[] = m.digest();
		
		String m_szUniqueID = new String();
		for (int i=0;i<p_md5Data.length;i++) {
			int b =  (0xFF & p_md5Data[i]);
			// if it is a single digit, make sure it have 0 in front (proper padding)
			if (b <= 0xF) m_szUniqueID+="0";
			// add number to string
			m_szUniqueID+=Integer.toHexString(b); 
		}
		m_szUniqueID = m_szUniqueID.toUpperCase();
		
		return m_szUniqueID;
	}
	
	public static GeoPoint getGeoPoint(double latitude, double longitude)
	{
		return new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
	}
}
