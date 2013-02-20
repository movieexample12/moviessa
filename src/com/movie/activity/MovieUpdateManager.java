package com.movie.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.movie.client.RawMovie;
import com.movie.database.MovieColumns;
import com.movie.database.MovieProvider;

/* only updates the listview*/
public class MovieUpdateManager {
	Adapter adapter;
	Context mContext;
	public MovieUpdateManager(Adapter adapter, Context mContext) {
		this.adapter = adapter;
		this.mContext = mContext;
	}
	
	public void update() {
		Log.e(MovieUpdateManager.class.getSimpleName(), " inside update() method ");
		Cursor c = mContext.getContentResolver().query(MovieProvider.CONTENT_URI, null, null,
                null, null);
		
		c.moveToFirst();
		do {
			int id = c.getInt(c.getColumnIndex(MovieColumns.COLUMN_NAME_ID));
			RawMovie m = new RawMovie(id, 
					c.getString(c.getColumnIndex(MovieColumns.COLUMN_NAME_BACKDROP_PATH)),
					c.getString(c.getColumnIndex(MovieColumns.COLUMN_NAME_ORIGINAL_TITLE)), 
					c.getString(c.getColumnIndex(MovieColumns.COLUMN_NAME_RELEASE_DATE)), 
					c.getString(c.getColumnIndex(MovieColumns.COLUMN_NAME_POSTER_PATH)), 
					c.getString(c.getColumnIndex(MovieColumns.COLUMN_NAME_ORIGINAL_TITLE)),
					c.getDouble(c.getColumnIndex(MovieColumns.COLUMN_NAME_VOTE_AVERAGE)),
					c.getInt(c.getColumnIndex(MovieColumns.COLUMN_NAME_VOTE_COUNT))
					);
			adapter.addRawMovie(m);
			adapter.notifyDataSetChanged();
		} while (c.moveToNext());
		
	}
	
	public static void addMovie(Context context, RawMovie movie, MovieCache cache, String photoUrl) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(MovieColumns.COLUMN_NAME_BACKDROP_PATH, photoUrl + movie.getBackdrop_path());
		
		initialValues.put(MovieColumns.COLUMN_NAME_ID, movie.getId());
		initialValues.put(MovieColumns.COLUMN_NAME_ORIGINAL_TITLE, movie.getOriginal_title());
		initialValues.put(MovieColumns.COLUMN_NAME_POSTER_PATH, photoUrl + movie.getPoster_path());
		String filename = cache.addMoviePoster(movie.getId(), photoUrl + movie.getPoster_path());
		initialValues.put(MovieColumns.COLUMN_NAME_POSTER_FILENAME, filename);
		initialValues.put(MovieColumns.COLUMN_NAME_SAVED_TO_EXTERNAL_DEVICE, cache.useExternalDevice(context));
		initialValues.put(MovieColumns.COLUMN_NAME_RELEASE_DATE, movie.getRelease_date());
		initialValues.put(MovieColumns.COLUMN_NAME_TITLE, movie.getTitle());
		initialValues.put(MovieColumns.COLUMN_NAME_VOTE_AVERAGE, movie.getVote_average());
		initialValues.put(MovieColumns.COLUMN_NAME_VOTE_COUNT, movie.getVote_count());
		
		context.getContentResolver().insert(MovieProvider.CONTENT_URI, initialValues);
		context.getContentResolver().notifyChange(MovieProvider.CONTENT_URI, null);
	}
}
