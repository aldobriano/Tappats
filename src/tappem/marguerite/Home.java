package tappem.marguerite;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


import tappem.marguerite.BusLine;
import tappem.marguerite.BusStop;
import tappem.marguerite.MargueriteTransportation;
import tappem.marguerite.MyLocation.LocationResult;
import tappem.marguerite.server.NearbyStopsServerTask;
import tappem.marguerite.server.NextBusServerTask;
import tappem.marguerite.server.NfcTagServerTask;
import tappem.marguerite.threads.DownloadTask;
import tappem.marguerite.threads.DownloadThread;
import tappem.marguerite.threads.DownloadThreadListener;
import tappem.nfc.NdefMessageParser;
import tappem.nfc.ParsedNdefRecord;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.location.Location;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class Home extends Activity implements DownloadThreadListener, OnClickListener, OnItemClickListener {
	public static int HOME_SCREEN = 0;
	public static int FAVORITES_SCREEN = 1;
	public static int CURRENT_SCREEN = 2;
	private int state;   // 1 - visible,  0 - anything else
	private MargueriteTransportation serverData;
	private String currentStopId;
	private String currentStopName;
	private Location currentLocation;
private Context context; 
	public static String uniqueId;

	private DownloadThread downloadThread;
	NfcAdapter mAdapter;
	PendingIntent mPendingIntent;
	private Handler handler;
	ArrayList<BusStop> stops;
	boolean hasNFC;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		state = 1;
		setContentView(R.layout.tappats_home);
		setProgressBarVisible(true);
		showEmptyList();
		//showProgressDialog();
		//SystemTools.checkInternet(this);

		PackageManager pm = getPackageManager();
		hasNFC = pm.hasSystemFeature(PackageManager.FEATURE_NFC);
		
		//checkVersion();

		Button homeBtn = (Button) findViewById(R.id.navbar_home);
		homeBtn.setEnabled(false);
		String uri = "drawable/" + "navbar_home_over";
		int imageResource = getResources().getIdentifier(uri, null, getPackageName());
		homeBtn.setBackgroundResource(imageResource);
		homeBtn.setOnClickListener(this);

		Button favoritesBtn = (Button) findViewById(R.id.navbar_favorites);
		favoritesBtn.setOnClickListener(this);

		Button currentBtn = (Button) findViewById(R.id.navbar_current);
		currentBtn.setOnClickListener(this);

		Button settingsBtn = (Button) findViewById(R.id.navbar_settings);
		settingsBtn.setOnClickListener(this);



		

	}

	public void onStart()
	{
		super.onStart();
		uniqueId = SystemTools.getUniqueId(this);

		serverData = new MargueriteTransportation(SystemTools.getUniqueId(this));
		checkVersion();




		MyLocation myLocation = new MyLocation();
		LocationResult locationResult = new LocationResult(){
			public void gotLocation(final Location location){
				//Got the location!
				currentLocation = location;
				
				if(currentLocation != null){
				if(state == 1){
					downloadThread.enqueueDownload(new NearbyStopsServerTask(location.getLatitude(), location.getLongitude()), context);
				}else
				{
				}
				}else
				{
					String text = "Your current GPS location couldn't be found.  Please verify your GPS is turned on and try again. \n" +
							"If you know the desired stop name, use the search button to search for the stop.";
					
					
					
					downloadThread.updateUI(text, context);
					setProgressBarVisible(true);
					
				}
				
			}

		};

		myLocation.getLocation(this, locationResult);
		showStatusTextInList("...acquiring your gps location...");








		// Create and launch the download thread
		downloadThread = new DownloadThread(this);
		downloadThread.start();

		// Create the Handler. It will implicitly bind to the Looper
		// that is internally created for this thread (since it is the UI thread)
		handler = new Handler();
		if(hasNFC)
		{		mAdapter = NfcAdapter.getDefaultAdapter(this);
		// Create a generic PendingIntent that will be deliver to this activity. The NFC stack
		// will fill in the intent with the details of the discovered tag before delivering to
		// this activity.
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		}
	}
	
	
	@Override
	public void onNewIntent(Intent intent) {
		setIntent(intent);
		if(hasNFC){
		resolveNFCIntent(intent);
		}
	}
	
	public boolean handleNdef(NdefMessage[] ndefMessages) {
		
			
		NdefMessage tappemUrl = ndefMessages[0];
		Log.d("LIGHTPAD", "NFC TAG handling");
		//NdefRecord[] caca = bluetoothTag.getRecords();
		ArrayList<ParsedNdefRecord> pnd = (ArrayList<ParsedNdefRecord>) NdefMessageParser.parse(tappemUrl);
		for(ParsedNdefRecord record: pnd){
			try{
			Uri testing = ((UriRecord) record).getUri();
			
		//	Log.d("LIGHTPAD", "NFC TAG:" + testing.getAuthority());
			
		String query = testing.getEncodedQuery();
		String path = testing.getPath();
		String authority = testing.getAuthority();
		String host = testing.getHost();
		Log.d("TAPPEM_DEBUG", "Host: " + host);
		Log.d("TAPPEM_DEBUG", "Authority: " + authority);
		Log.d("TAPPEM_DEBUG", "Path: " + path);
		Log.d("TAPPEM_DEBUG", "Query: " + query);
			}catch(Exception e)
			{
				
			}
		}

		
		return true;
		
	//	return false;
	}
	
	private void resolveNFCIntent(Intent intent)
	{
		// Parse the intent
		String action = intent.getAction();
		
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
		{
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			
			String tagId = BusTagRead.asHex(tagFromIntent.getId());
			
			
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
                boolean ndefDecoded = handleNdef(msgs);
            }
			
			
			
			
			setProgressBarVisible(true);
	        try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	        downloadThread.enqueueDownload(new NfcTagServerTask(tagId), context);
			
			
			
			
			
		}
		else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			// When a tag is discovered we send it to the service to be save. We
			// include a PendingIntent for the service to call back onto. This
			// will cause this activity to be restarted with onNewIntent(). At
			// that time we read it from the database and view it.
			//Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			
			String text = "Please scan the NFC tag again";
			
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(this, text, duration);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			
			
		

		}
	}
	protected void showEmptyList()
	{

		updateList(null);

	}
	
	
	protected void showStatusTextInList(String statusText)
	{
		
			String[] names = new String[] { "Linux", statusText, "Eclipse", "Suse",
					"Ubuntu"};
			// Create an ArrayAdapter, that will actually make the Strings above
			// appear in the ListView
			ListView l1 = (ListView) findViewById(R.id.bus_stop_list);
			l1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			l1.setClickable(false);
			l1.setOnItemClickListener(this);
			
			l1.setAdapter(new ArrayAdapter<String>(this,
					R.layout.status_text_list, names));
			
			
	}
	protected void updateList(ArrayList<BusStop> b)
	{
		stops = b;
		//System.out.println("updatelist");

		// Get all of the rows from the database and create the item list
		ListView l1 = (ListView) findViewById(R.id.bus_stop_list);
		l1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		l1.setClickable(true);
		l1.setOnItemClickListener(this);
		l1.setAdapter(new StopsEfficientAdapter(this, stops, R.layout.nearby_stop_list));
	}
	
	public void displayText(String text)
	{
		
		
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	public void handleUpdateUI(final String text)
	{
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				
				displayText(text);



			}
		});
		
	}
	// note! this might be called from another thread
	@Override
	public void handleDownloadThreadUpdate(final DownloadTask dt) {
		// we want to modify the progress bar so we need to do it from the UI thread 
		// how can we make sure the code runs in the UI thread? use the handler!
		handler.post(new Runnable() {

			@Override
			public void run() {
				int total = downloadThread.getTotalQueued();
				int completed = downloadThread.getTotalCompleted();

				//System.out.println("progress so far: " + completed + " / " + total);

				if(dt instanceof NearbyStopsServerTask)
				{
					//System.out.println("here ");
					
					
					updateList((ArrayList<BusStop>) dt.getResult());
					setProgressBarVisible(false);

				}else if(dt instanceof NfcTagServerTask)
				{
					
					
					handleBusStop((String) dt.getResult());
					setProgressBarVisible(false);
				}




			}
		});
	}
	public void handleBusStop(String stopId)
	{
		if(stopId == null || stopId.equals("ERROR"))
		{
			String text = "Sorry, this tag hasn't been activated yet, use GPS instead! Our team is working on it! Thanks for letting us know.";

			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(this, text, duration);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}else{
			Intent i = new Intent(this, NextBus.class);
			i.putExtra(BusAdapter.STOP_ID, stopId);
			startActivity(i);
		}
		
		
 	}
	public void setProgressBarVisible(boolean visible)
	{
		ProgressBar pb = (ProgressBar) findViewById(R.id.pb);
		if(!visible)
			pb.setVisibility(View.GONE);
		else
			pb.setVisibility(View.VISIBLE);
	}
	

	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(BusAdapter.STOP_ID, currentStopId);

	}

	void resolveIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		currentStopId = extras != null ? extras.getString(BusAdapter.STOP_ID) : null;
		currentStopName = extras != null ? extras.getString(BusAdapter.STOP_NAME) : null;
		uniqueId = extras != null ? extras.getString(BusAdapter.PHONE_ID) : null;
		
	}


	protected void onPause()
	{
		if(hasNFC)
			mAdapter.disableForegroundDispatch(this);
		super.onPause();
		state = 0;
		saveState();
		
	}

	protected void onResume()
	{
		if(hasNFC)
			mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
		resolveIntent(getIntent());
		super.onResume();
		state = 1;

		//fillData();
	}
	protected void onDestroy()
	{
		finish();
		super.onDestroy();
	}

	private void saveState()
	{
		//TODO
	}

	public void checkVersion()
	{
		URL sourceUrl;
		try {
			sourceUrl = new URL("http://margueritenfc.heroku.com/version.xml");


			BufferedReader in = new BufferedReader(
					new InputStreamReader(
							sourceUrl.openStream()));

			String version = in.readLine();
			in.close();

			//System.out.println(version);

			if(!BusAdapter.VERSION.equals(version))
			{
				CharSequence text = "A new version of TapPATS has been released. Please update your app!";
				int duration = Toast.LENGTH_LONG;

				Toast toast = Toast.makeText(this, text, duration);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();



				//app is outdated
				String url = "http://www.tappem.com/stanfordtappats.htm";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);

				finish();
			}


		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e)
		{}
	}




	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.navbar_home:


			break;
		case R.id.navbar_current:

			if(currentStopId != null && currentStopName != null){

				Intent i = new Intent(this, NextBus.class);
				i.putExtra(BusAdapter.PHONE_ID, uniqueId);
				i.putExtra(BusAdapter.STOP_ID, currentStopId);
				i.putExtra(BusAdapter.STOP_NAME, currentStopName);
				startActivity(i);
			//	finish();
			}else
			{
				String text = "Whoa! You haven't selected your current stop. Please pick one from the list!";
				
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}

			break;
		case R.id.navbar_favorites:
			//Toast.makeText(this, "You clicked the button favorites", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(this, FavoritesScreen.class);
			i.putExtra(BusAdapter.PHONE_ID, uniqueId);
			if(currentStopId != null && currentStopName != null)
			{
				i.putExtra(BusAdapter.STOP_ID, currentStopId);
				i.putExtra(BusAdapter.STOP_NAME, currentStopName);
			}
			startActivity(i);
			//finish();

			break;
		case R.id.navbar_settings:
			Intent i1 = new Intent(this, Settings.class);
			i1.putExtra(BusAdapter.PHONE_ID, uniqueId);
			if(currentStopId != null && currentStopName != null)
			{
				i1.putExtra(BusAdapter.STOP_ID, currentStopId);
				i1.putExtra(BusAdapter.STOP_NAME, currentStopName);
			}
			startActivity(i1);

			break;
		default:
			Toast.makeText(this, "You clicked", Toast.LENGTH_SHORT).show();
			break;
		}


	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int listIndex,
			long arg3) {
		
		try{
			if(stops != null || !stops.isEmpty() || stops.size() >= listIndex || !stops.get(listIndex).getStopLabel().equals("")){
				//arg2 is the list index cool.
				currentStopId = stops.get(listIndex).getStopId();
				currentStopName = stops.get(listIndex).getStopLabel();
				Intent i = new Intent(this, NextBus.class);
				i.putExtra(BusAdapter.STOP_ID, currentStopId);
				i.putExtra(BusAdapter.STOP_NAME, currentStopName);
				i.putExtra(BusAdapter.PHONE_ID, uniqueId);
				startActivity(i);
				//finish();
			}
		}catch(Exception e)
		{}
	}

	









}
