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
public class UserColumns implements BaseColumns {
	
	public final static String COLUMN_NAME_USER                = "username";
	public final static String COLUMN_NAME_API                 = "api";
	public final static String COLUMN_NAME_SESSION_ID          = "sessionid";

}
