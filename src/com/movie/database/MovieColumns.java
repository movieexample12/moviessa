package com.movie.database;

import android.provider.BaseColumns;

/* example of data
 * "backdrop_path": "/hbn46fQaRmlpBuUrEiFqv0GDL6Y.jpg",
   "id": 24428,
   "original_title": "The Avengers",
   "release_date": "2012-05-04",
   "poster_path": "/cezWGskPY5x7GaglTTRN4Fugfb8.jpg",
   "title": "The Avengers",
   "vote_average": 8.5,
   "vote_count": 117 
 **/
/** @name MovieColumns
 * */
public class MovieColumns implements BaseColumns {
	
	public final static String COLUMN_NAME_BACKDROP_PATH                    = "backdrop_path";
	public final static String COLUMN_NAME_ID                               = "id";
	public final static String COLUMN_NAME_ORIGINAL_TITLE                   = "original_title";
	public final static String COLUMN_NAME_RELEASE_DATE                     = "release_date";
	public final static String COLUMN_NAME_POSTER_PATH                      = "poster_path";
	public final static String COLUMN_NAME_POSTER_FILENAME                  = "poster_filename";
	public static final String COLUMN_NAME_TITLE                            = "title";
	public static final String COLUMN_NAME_VOTE_AVERAGE                     = "vote_average";
	public static final String COLUMN_NAME_VOTE_COUNT                       = "vote_count";
	public static final String COLUMN_NAME_SAVED_TO_EXTERNAL_DEVICE         = "saved_to_external_device";

}
