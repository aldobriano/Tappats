package tappem.marguerite;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


import tappem.marguerite.BusStop;
import tappem.marguerite.MargueriteTransportation;

import android.app.Activity;
import android.location.Location;

import android.os.Bundle;

import android.content.Intent;

import android.database.Cursor;

import android.view.Gravity;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.AdapterView;

import android.widget.Button;

import android.widget.ListView;

import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class FavoritesScreen extends Activity implements OnClickListener, OnItemClickListener{
	public static int HOME_SCREEN = 0;
	public static int FAVORITES_SCREEN = 1;
	public static int CURRENT_SCREEN = 2;

	private String currentStopId;
	private String currentStopName;

	public static String uniqueId;

	private int state;
	ArrayList<BusStop> stops;

	private FavoritesDbAdapter dbHelper;

	//private static final int DELETE_ID = Menu.FIRST + 1;
	private Cursor cursor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tappats_favorites);



		//SystemTools.checkInternet(this);


		Button homeBtn = (Button) findViewById(R.id.navbar_home);
		homeBtn.setOnClickListener(this);

		Button favoritesBtn = (Button) findViewById(R.id.navbar_favorites);
		favoritesBtn.setEnabled(false);
		String uri = "drawable/" + "navbar_favorite_over";
		int imageResource = getResources().getIdentifier(uri, null, getPackageName());
		favoritesBtn.setBackgroundResource(imageResource);
		favoritesBtn.setOnClickListener(this);

		Button currentBtn = (Button) findViewById(R.id.navbar_current);
		currentBtn.setOnClickListener(this);

		Button settingsBtn = (Button) findViewById(R.id.navbar_settings);
		settingsBtn.setOnClickListener(this);


		dbHelper = new FavoritesDbAdapter(this);
		





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
	protected void updateList(ArrayList<BusStop> b)
	{
		stops = b;

		// Get all of the rows from the database and create the item list


		ListView l1 = (ListView) findViewById(R.id.favorites_stop_listview);
		l1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		l1.setClickable(true);
		l1.setOnItemClickListener(this);
		l1.setAdapter(new StopsEfficientAdapter(this, stops, R.layout.favorites_stop_list));
	}

	private void fillData() {
		BusLine a = new BusLine("Line X");
		
		a.setIcon("bus");
		ArrayList<BusLine> testLine = new ArrayList<BusLine>();
		testLine.add(a);
		
		
		cursor = dbHelper.fetchAllFavorites();
		startManagingCursor(cursor);

		ArrayList<BusStop> e2r = new ArrayList<BusStop>();
		System.out.println("NUM OF FAVORITES: " + cursor.getCount());
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {

			e2r.add(new BusStop(cursor.getString(1),cursor.getString(2),testLine));
			//System.out.println("Added a stop  " + cursor.getString(2) + "  ");
			cursor.moveToNext();
		}


		cursor.close();
		System.out.println("Stops " + e2r.size());

		//		BusLine a = new BusLine("Line X");
		//		a.setIcon("x");
		//
//		ArrayList<BusLine> testLine = new ArrayList<BusLine>();
//		testLine.add(a);
//		a.setIcon("slac");
//		testLine.add(a);
//		a.setIcon("y");
//		
//		testLine.add(a);
//		a.setIcon("rp");
//		testLine.add(a);
//		testLine.add(a);
//
//		BusStop one = new BusStop("66","Palo Alto Transit Center", testLine);
//		BusStop two = new BusStop("67","Lytton @ Alma", testLine);
//		BusStop three = new BusStop("68","Lytton Plaza", testLine);
//		BusStop four = new BusStop("69","Palo Medical Foundation", testLine);
//		BusStop five = new BusStop("70","Pill Hill & Bowdoin", testLine);
//
//		ArrayList<BusStop> e2r = new ArrayList<BusStop>();
//		e2r.add(one);
//		e2r.add(two);
//		e2r.add(three);
//		e2r.add(four);
//		e2r.add(five);
		updateList(e2r);

	}



	protected void onPause()
	{
		super.onPause();
		state = 0;
		saveState();
		finish();

	}

	protected void onResume()
	{
		resolveIntent(getIntent());
		super.onResume();
		state = 1;
		dbHelper.open();
		fillData();
		dbHelper.close();
		//fillData();
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
			if(stops != null || !stops.isEmpty() || stops.size() <= listIndex || !stops.get(listIndex).getStopLabel().equals("")){
				//arg2 is the list index cool.
				currentStopId = stops.get(listIndex).getStopId();
				currentStopName = stops.get(listIndex).getStopLabel();
				Intent i = new Intent(this, NextBus.class);
				i.putExtra(BusAdapter.STOP_ID, currentStopId);
				i.putExtra(BusAdapter.STOP_NAME, currentStopName);
				i.putExtra(BusAdapter.PHONE_ID, uniqueId);
				startActivity(i);
				finish();
			}
		}catch(Exception e)
		{System.out.println("AHHH!! EXCEPTIONN!!!");}
	}











}
