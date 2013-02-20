package com.movie.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;

/** 
 * Describes cinema database
 * */
public class MovieDatabase {

	public final static String CONFIGURATION_TABLE_NAME        = "tconf";
 	public final static String MOVIES_TABLE_NAME 			   = "tmovies";
 	public final static String AUTHENTICATE_TABLE_NAME         = "tauth";
	public final static String DATABASE_NAME 	               = "dbmovies";
	public final static int    DATABASE_VERSION 	           = 2;
	
    private final MovieOpenHelper mDatabaseOpenHelper;

    /**
     * Constructor
     * @param context The Context within which to work, used to create the DB
     */
    public MovieDatabase(Context context) {
        mDatabaseOpenHelper = new MovieOpenHelper(context);
    }
    
    /**
     * Returns a Cursor positioned at the word specified by rowId
     *
     * @param rowId id of word to retrieve
     * @param columns The columns to include, if null then all are included
     * @return Cursor positioned to matching word, or null if not found.
     */
    public Cursor getConfiguration() {
        return query(null, null, null, CONFIGURATION_TABLE_NAME);
    }
    
    /**
     * Returns a Cursor positioned at the word specified by rowId
     *
     * @param rowId id of word to retrieve
     * @param columns The columns to include, if null then all are included
     * @return Cursor positioned to matching word, or null if not found.
     */
    public Cursor getMovie(String[] selectionArgs, String[] columns) {
        String selection = MovieColumns.COLUMN_NAME_ID + " = ?";
        return queryMovie(selection, selectionArgs, columns);
    }
    
    public Cursor getMovies() {
        return query(null, null, null, MOVIES_TABLE_NAME);
//        return queryMovie(selection, selectionArgs, columns);
    }
    
    /**
     * Returns a Cursor positioned at the word specified by rowId
     *
     * @param rowId id of word to retrieve
     * @param columns The columns to include, if null then all are included
     * @return Cursor positioned to matching word, or null if not found.
     */
    public Cursor getUser(String[] selectionArgs, String[] columns) {
        String selection = UserColumns.COLUMN_NAME_USER + " = ?";
        return queryUser(selection, selectionArgs, columns);
    }
    
    private Cursor queryUser(String selection, String[] selectionArgs, String[] columns) {
        return query(selection, selectionArgs, columns, AUTHENTICATE_TABLE_NAME);
    }

    private Cursor queryMovie(String selection, String[] selectionArgs, String[] columns) {
        return query(selection, selectionArgs, columns, MOVIES_TABLE_NAME);
    }
    
    private Cursor query(String selection, String[] selectionArgs, String[] columns, String tableName) {
        /* The SQLiteBuilder provides a map for all possible columns requested to
         * actual columns in the database, creating a simple column alias mechanism
         * by which the ContentProvider does not need to know the real column names
         */
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(tableName);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
    

}
