package com.movie.activity;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class MovieContentObserver extends ContentObserver {

	MovieUpdateManager updateManager;
	
	public MovieContentObserver(Handler handler, MovieUpdateManager updateManager) {
		super(handler);
		this.updateManager = updateManager;
    }

	@Override
	public void onChange(boolean selfChange, Uri uri) {
		super.onChange(selfChange, uri);
		Log.e(MovieContentObserver.class.getSimpleName(), " inside onChange method ");
		updateManager.update();
	}
	
}
