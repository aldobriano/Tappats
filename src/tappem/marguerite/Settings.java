package tappem.marguerite;



import tappem.marguerite.server.NfcTagServerTask;
import tappem.marguerite.threads.DownloadTask;
import tappem.marguerite.threads.DownloadThread;
import tappem.marguerite.threads.DownloadThreadListener;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;


public class Settings extends Activity implements DownloadThreadListener, OnClickListener{
	
	private int state;   // 1 - visible,  0 - anything else
	
	private String currentStopId;
	private String currentStopName;
	
	public static String uniqueId;
	boolean hasNFC;
	private DownloadThread downloadThread;
	NfcAdapter mAdapter;
	PendingIntent mPendingIntent;
	private Handler handler;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		state = 1;
		setContentView(R.layout.tappats_settings);
		setProgressBarVisible(false);
		PackageManager pm = getPackageManager();
		hasNFC = pm.hasSystemFeature(PackageManager.FEATURE_NFC);
		
		Button url = (Button) findViewById(R.id.urlTappem);
		url.setOnClickListener(this);
		
		Button homeBtn = (Button) findViewById(R.id.navbar_home);
		homeBtn.setOnClickListener(this);

		Button favoritesBtn = (Button) findViewById(R.id.navbar_favorites);
		favoritesBtn.setOnClickListener(this);

		Button currentBtn = (Button) findViewById(R.id.navbar_current);
		currentBtn.setOnClickListener(this);

		Button settingsBtn = (Button) findViewById(R.id.navbar_settings);
		settingsBtn.setEnabled(false);
		String uri = "drawable/" + "navbar_settings_over";
		int imageResource = getResources().getIdentifier(uri, null, getPackageName());
		settingsBtn.setBackgroundResource(imageResource);
		settingsBtn.setOnClickListener(this);
		
		// Create and launch the download thread
		downloadThread = new DownloadThread(this);
		downloadThread.start();

		// Create the Handler. It will implicitly bind to the Looper
		// that is internally created for this thread (since it is the UI thread)
		handler = new Handler();
		if(hasNFC)
		{
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		// Create a generic PendingIntent that will be deliver to this activity. The NFC stack
      // will fill in the intent with the details of the discovered tag before delivering to
      // this activity.
      mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		setIntent(intent);
		if(hasNFC)
		resolveNFCIntent(intent);
		
	}
	
	private void resolveNFCIntent(Intent intent)
	{
		// Parse the intent
		String action = intent.getAction();
		
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
		{
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			
			String tagId = BusTagRead.asHex(tagFromIntent.getId());
			
			setProgressBarVisible(true);
	        try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	        downloadThread.enqueueDownload(new NfcTagServerTask(tagId),this);
			
			
			
			
			
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

				if(dt instanceof NfcTagServerTask)
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

	

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.navbar_home:
			Intent i = new Intent(this, Home.class);
			i.putExtra(BusAdapter.PHONE_ID, uniqueId);
			if(currentStopId != null && currentStopName != null)
			{
				i.putExtra(BusAdapter.STOP_ID, currentStopId);
				i.putExtra(BusAdapter.STOP_NAME, currentStopName);
			}
			startActivity(i);
			finish();

			break;
		case R.id.navbar_current:

			if(currentStopId != null && currentStopName != null){

				Intent i1 = new Intent(this, NextBus.class);
				i1.putExtra(BusAdapter.PHONE_ID, uniqueId);
				i1.putExtra(BusAdapter.STOP_ID, currentStopId);
				i1.putExtra(BusAdapter.STOP_NAME, currentStopName);
				startActivity(i1);
				finish();
			}else
			{
				String text = "Whoa! You haven't selected your current stop. Please pick one from the list!";
				
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(this, text, duration);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}

			break;
		case R.id.navbar_favorites:
			//Toast.makeText(this, "You clicked the button favorites", Toast.LENGTH_SHORT).show();
			Intent i1 = new Intent(this, FavoritesScreen.class);
			i1.putExtra(BusAdapter.PHONE_ID, uniqueId);
			if(currentStopId != null && currentStopName != null)
			{
				i1.putExtra(BusAdapter.STOP_ID, currentStopId);
				i1.putExtra(BusAdapter.STOP_NAME, currentStopName);
			}
			startActivity(i1);
			//finish();

			break;
		case R.id.navbar_settings:
			Toast.makeText(this, "You clicked the button settings", Toast.LENGTH_SHORT).show();

			break;
		case R.id.urlTappem:
			String url = "http://www.tappem.com/";
			Intent i2 = new Intent(Intent.ACTION_VIEW);
			i2.setData(Uri.parse(url));
			startActivity(i2);

			finish();
			break;
		}


	}

	



}
