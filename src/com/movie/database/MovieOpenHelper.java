package com.movie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MovieOpenHelper extends SQLiteOpenHelper {

	private static final String TAG = MovieOpenHelper.class.getName();

	private static boolean tableCreated = false;
	
    private SQLiteDatabase mDatabase;

    private static final String MOVIES_TABLE_CREATE =
		"CREATE TABLE " + MovieDatabase.MOVIES_TABLE_NAME + " ("
				+ MovieColumns._ID + " TEXT PRIMARY KEY,"
                + MovieColumns.COLUMN_NAME_ID + " TEXT,"
	            + MovieColumns.COLUMN_NAME_BACKDROP_PATH + " TEXT,"
	            + MovieColumns.COLUMN_NAME_ORIGINAL_TITLE + " TEXT,"
	            + MovieColumns.COLUMN_NAME_POSTER_PATH + " TEXT,"
	            + MovieColumns.COLUMN_NAME_RELEASE_DATE + " TEXT,"
	            + MovieColumns.COLUMN_NAME_POSTER_FILENAME + " TEXT,"
	            + MovieColumns.COLUMN_NAME_SAVED_TO_EXTERNAL_DEVICE + " TEXT,"
	            + MovieColumns.COLUMN_NAME_TITLE + " TEXT,"
	            + MovieColumns.COLUMN_NAME_VOTE_AVERAGE + " REAL,"
	            + MovieColumns.COLUMN_NAME_VOTE_COUNT + " INTEGER"
	            + ");";
    
    private static final String AUTHENTICATE_TABLE_CREATE =
		"CREATE TABLE " + MovieDatabase.AUTHENTICATE_TABLE_NAME + " ("
				+ UserColumns._ID + " TEXT PRIMARY KEY,"
                + UserColumns.COLUMN_NAME_API + " TEXT,"
                + UserColumns.COLUMN_NAME_SESSION_ID + " TEXT,"
                + UserColumns.COLUMN_NAME_USER + " TEXT"
                + ");";
    
    private static final String CONFIGURATION_TABLE_CREATE =
		"CREATE TABLE " + MovieDatabase.CONFIGURATION_TABLE_NAME + " ("
				+ ConfigurationColumns._ID + " TEXT PRIMARY KEY,"
                + ConfigurationColumns.COLUMN_NAME_BASE_URL + " TEXT,"
                + ConfigurationColumns.COLUMN_NAME_LOGO_SIZE + " TEXT,"
                + ConfigurationColumns.COLUMN_NAME_POSTER_SIZE + " TEXT"
                + ");";


    public MovieOpenHelper(Context context) {
        super(context, MovieDatabase.DATABASE_NAME, null, MovieDatabase.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!tableCreated) {
        	tableCreated = true;
        	mDatabase = db;
            mDatabase.execSQL("DROP TABLE IF EXISTS " + MovieDatabase.MOVIES_TABLE_NAME);//TODO:remove
            mDatabase.execSQL(MOVIES_TABLE_CREATE);
            mDatabase.execSQL("DROP TABLE IF EXISTS " + MovieDatabase.AUTHENTICATE_TABLE_NAME);
            mDatabase.execSQL(AUTHENTICATE_TABLE_CREATE);
            mDatabase.execSQL("DROP TABLE IF EXISTS " + MovieDatabase.CONFIGURATION_TABLE_NAME);
            mDatabase.execSQL(CONFIGURATION_TABLE_CREATE);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + MovieDatabase.MOVIES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieDatabase.AUTHENTICATE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieDatabase.CONFIGURATION_TABLE_NAME);
        tableCreated = false;
        onCreate(db);
    }

}
