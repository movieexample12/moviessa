/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.movie.syncadapter;

import java.util.List;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.movie.activity.MovieCache;
import com.movie.activity.MovieUpdateManager;
import com.movie.client.NetworkUtilities;
import com.movie.client.RawConfigurationList;
import com.movie.client.RawMovie;
import com.movie.database.ConfigurationColumns;
import com.movie.database.ConfigurationProvider;
import com.movie.database.MovieColumns;
import com.movie.database.MovieProvider;
import com.movie.database.UserAuthenticateProvider;
import com.movie.database.UserColumns;

/**
 * SyncAdapter implementation for syncing sample SyncAdapter contacts to the
 * platform ContactOperations provider.  This sample shows a basic 2-way
 * sync between the client and a sample server.  It also contains an
 * example of how to update the contacts' status messages, which
 * would be useful for a messaging or social networking client.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SyncAdapter";
    
    private final Context mContext;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
        ContentProviderClient provider, SyncResult syncResult) {
    	Log.i(TAG, "onPerformSync");
        try {
        	Uri uri = new Uri.Builder()
	            .scheme( ContentResolver.SCHEME_CONTENT )
	            .authority( UserAuthenticateProvider.AUTHORITY )
	            .appendPath( UserAuthenticateProvider.DICTIONARY_NAME )
	            .appendPath( "1" ).build();
        	Cursor cursor = mContext.getContentResolver().query(uri, null, UserColumns.COLUMN_NAME_API,
                    new String[] {account.name}, null);
        	String api = cursor.getString(cursor.getColumnIndex(UserColumns.COLUMN_NAME_API));
        	//get configuration
        	ContentValues initialValues = new ContentValues();
        	RawConfigurationList conf = NetworkUtilities.getConfiguration(api);
        	
        	initialValues.put(ConfigurationColumns.COLUMN_NAME_BASE_URL, conf.getImages().getBase_url());
        	initialValues.put(ConfigurationColumns.COLUMN_NAME_LOGO_SIZE, conf.getImages().getLogo_sizes().get(1));
        	initialValues.put(ConfigurationColumns.COLUMN_NAME_POSTER_SIZE, conf.getImages().getPoster_sizes().get(1));
        	mContext.getContentResolver().insert(ConfigurationProvider.CONTENT_URI, initialValues);	
        	
        	//get movies
        	List<RawMovie> serverMovies = NetworkUtilities.getPopularMoviesList(api, 1, 10);
        	Log.i(TAG, " count of movies: " + serverMovies.size());
        	for (RawMovie movie: serverMovies) {
        		String photoUrl = conf.getImages().getBase_url() + conf.getImages().getPoster_sizes().get(1);
        		MovieUpdateManager.addMovie(mContext, movie, getMovieCache(), photoUrl);
        	}
        	
        	
        	//TODO: add movies to View
        } catch (final Exception e) {
            Log.e(TAG, "AuthenticatorException", e);
            syncResult.stats.numParseExceptions++;
        } 
    }

    protected MovieCache getMovieCache() {
    	return new MovieCache(mContext);
    }

}

