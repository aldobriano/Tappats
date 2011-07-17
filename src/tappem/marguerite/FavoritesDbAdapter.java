package tappem.marguerite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class FavoritesDbAdapter {

	// Database fields
	public static final String KEY_ROWID = "_id";
	public static final String KEY_STOPID = "stop_id";
	public static final String KEY_STOPNAME = "stop_name";
	//public static final String KEY_DESCRIPTION = "description";
	private static final String DATABASE_TABLE = "favorites";
	private Context context;
	private SQLiteDatabase database;
	private Favorites dbHelper;

	public FavoritesDbAdapter(Context context) {
		this.context = context;
	}

	public FavoritesDbAdapter open() throws SQLException {
		dbHelper = new Favorites(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * Create a new favorite If the fvaorite is successfully created return the new
	 * rowId for that note, otherwise return a -1 to indicate failure.
	 */
	public long createFavorites(String stop_id, String stop_name) {
		ContentValues initialValues = createContentValues(stop_id, stop_name);

		return database.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Update the favorite
	 */
	public boolean updateFavorites(long rowId, String stopid, String stopname) {
		ContentValues updateValues = createContentValues(stopid, stopname);

		return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "="
				+ rowId, null) > 0;
	}

	/**
	 * Deletes favorite
	 */
	public boolean deleteFavorites(String stopId) {
		return database.delete(DATABASE_TABLE, KEY_STOPID + "=" + stopId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all favorites in the database
	 * 
	 * @return Cursor over all ids
	 */
	public Cursor fetchAllFavorites() {
		String select = "Select * from favorites"; 
		return database.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_STOPID, KEY_STOPNAME },null, null, null, null , null);
	}

	/**
	 * Return a Cursor positioned at the defined todo
	 */
	public Cursor fetchFavorites(String stopId) throws SQLException {
		Cursor mCursor = database.query( DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_STOPID, KEY_STOPNAME },
				KEY_STOPID + "=" + stopId, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	private ContentValues createContentValues(String stopid, String stopname) {
		ContentValues values = new ContentValues();
		values.put(KEY_STOPID, stopid);
		values.put(KEY_STOPNAME, stopname);
		//values.put(KEY_DESCRIPTION, description);
		return values;
	}
}
