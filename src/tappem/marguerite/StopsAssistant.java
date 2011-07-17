package tappem.marguerite;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.database.*;
import android.database.sqlite.*;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
 
public class StopsAssistant extends SQLiteOpenHelper
{
	private static final String TAG = "StopsAssistant";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_STOPID = "stop_id";
    public static final String KEY_STOPNAME = "stop_name";
    public static final String KEY_STOPLON = "stop_lon";
    public static final String KEY_STOPLAT= "stop_lat";
    private static final String DB_NAME = "stopdata1";
    private static final int DB_VERSION_NUMBER = 2;
    private static final String DB_TABLE_NAME = "stops";
    //private static final String FTS_VIRTUAL_TABLE = "FTSdictionary";
 
    private static final String DB_CREATE_SCRIPT = "CREATE TABLE stops(stop_id TEXT NOT NULL  ,stop_name TEXT NOT NULL ,stop_lat TEXT NOT NULL ,stop_lon TEXT NOT NULL );";
    private static final String DB_COLUMN_1_NAME = "stop_name";
    private SQLiteDatabase mDatabase;
    private SQLiteDatabase sqliteDBInstance = null;
    private final Context mHelperContext;
    public StopsAssistant(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION_NUMBER);
        mHelperContext=context;
        Log.i("onCreate", "Constructor the database...");
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO: Implement onUpgrade
    }
 
    @Override
    public void onCreate(SQLiteDatabase sqliteDBInstance)
    {
    	mDatabase = sqliteDBInstance;
    	this.sqliteDBInstance=sqliteDBInstance;
        Log.i("onCreate", "Creating the database...");
        sqliteDBInstance.execSQL(DB_CREATE_SCRIPT);
        loadStops();
    }
 
    /**
     * Starts a thread to load the database table with words
     */
    private void loadStops() {
    	try {
            loadWords();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /*new Thread(new Runnable() {
            public void run() {
                try {
                    loadWords();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();*/
    }

    private void loadWords() throws IOException {
        Log.d(TAG, "Loading words...");
        final Resources resources = mHelperContext.getResources();
        InputStream inputStream = resources.openRawResource(R.raw.stopinfo);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line;
            int i=0;
            //stop_id,stop_code,stop_name,stop_lat,stop_lon
            while ((line = reader.readLine()) != null) {
            	if(i==0)
            	{
            		i++;
            		continue;
            	}
                String[] strings = TextUtils.split(line, ",");
                if (strings.length < 2) continue;
                long id = addStops(strings[1].trim(), strings[2].trim(),strings[3].trim(),strings[4].trim());
                if (id < 0) {
                    Log.e(TAG, "unable to add word: " + strings[0].trim());
                }
            }
        } finally {
            reader.close();
        }
        Log.d(TAG, "DONE loading words.");
    }
    
    /**
     * Add a word to the dictionary.
     * @return rowId or -1 if failed
     */
    public long addStops(String stopid, String stopname , String stoplon , String stoplat) {
        ContentValues initialValues = new ContentValues();
        /*
         * public static final String KEY_STOPID = "stop_id";
    public static final String KEY_STOPNAME = "stop_name";
    public static final String KEY_STOPLON = "stop_lon";
    public static final String KEY_STOPLAT= "stop_lat";
         */
        initialValues.put(KEY_STOPID, stopid);
        initialValues.put(KEY_STOPNAME, stopname);
        initialValues.put(KEY_STOPLON, stoplon);
        initialValues.put(KEY_STOPLAT, stoplat);
        Log.d(TAG,stopid+ ","+ stopname + ","+ stoplon + "," + stoplat);
        return sqliteDBInstance.insert(DB_TABLE_NAME, null, initialValues);
    }

    public void openDB() throws SQLException
    {
       // Log.i("openDB", "Checking sqliteDBInstance...");
        if(this.sqliteDBInstance == null)
        {
         //   Log.i("openDB", "Creating sqliteDBInstance...");
            this.sqliteDBInstance = this.getWritableDatabase();
        }
    }
 
    public void closeDB()
    {
        if(this.sqliteDBInstance != null)
        {
            if(this.sqliteDBInstance.isOpen())
                this.sqliteDBInstance.close();
        }
    }
 
    
    /**
	 * Return a Cursor positioned at the defined stop
	 */
	public Cursor fetchStop(String stopId) throws SQLException {
		Log.i("fetchStop", stopId);
		Cursor mCursor = sqliteDBInstance.query( DB_TABLE_NAME, new String[] {
			   KEY_STOPID, KEY_STOPNAME,KEY_STOPLON,KEY_STOPLAT },
				KEY_STOPID + "=" + stopId, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	/**
	 * Return a Cursor positioned at the defined stop
	 */
	public Cursor fetchStopUsingName(String stopName) throws SQLException {
		Log.i("fetchStop", stopName);
		Cursor mCursor = sqliteDBInstance.query( DB_TABLE_NAME, new String[] {
			   KEY_STOPID, KEY_STOPNAME,KEY_STOPLON,KEY_STOPLAT },
				KEY_STOPNAME + "=" + stopName, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Return a Cursor positioned at the defined stop
	 */
	public Cursor fetchStopusingRID(String rowId) throws SQLException {
		Log.i("fetchStop using rowid", rowId);
		
		Cursor mCursor = sqliteDBInstance.query( DB_TABLE_NAME, new String[] {
			   KEY_STOPID, KEY_STOPNAME,KEY_STOPLON,KEY_STOPLAT },
				"rowId" + "=" + rowId, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
    public String[] getAllStops()
    {
        Cursor cursor = this.sqliteDBInstance.query(DB_TABLE_NAME, new String[] {DB_COLUMN_1_NAME}, null, null, null, null, null);
 
        if(cursor.getCount() >0)
        {
            String[] str = new String[cursor.getCount()];
            int i = 0;
 
            while (cursor.moveToNext())
            {
                 str[i] = cursor.getString(cursor.getColumnIndex(DB_COLUMN_1_NAME));
                 i++;
             }
            return str;
        }
        else
        {
            return new String[] {};
        }
    }
}