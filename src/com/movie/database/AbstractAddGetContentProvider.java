/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.movie.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Provides access to the dictionary database.
 */
public abstract class AbstractAddGetContentProvider extends ContentProvider {

    // MIME types used for searching words or looking up a single definition
    public static final String CINEMAS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.example.android.searchabledict";
    public static final String DEFINITION_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.example.android.searchabledict";

    abstract public Uri getContentIdUriBase();
    abstract public Uri getContentUri();
    abstract public String getDictionaryName();
    abstract public String getAuthority();
    abstract public String getTableName();
    abstract public String getNullableColumnForInsert();
    abstract protected Cursor getEntity(String[] selectionArgs);
    abstract protected Cursor getEntities(String[] selectionArgs);
    protected MovieDatabase mDictionary;
    protected MovieOpenHelper mOpenHelper;
    // UriMatcher stuff
    private static final int ENTITIES = 0;
    private static final int ENTITY_ID = 1;

    /**
     * A UriMatcher instance
     */
    private UriMatcher sUriMatcher = null;
    
    @Override
    public boolean onCreate() {
    	
    	sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(getAuthority(), getDictionaryName() + "/", ENTITIES);
        sUriMatcher.addURI(getAuthority(), getDictionaryName() + "/#", ENTITY_ID);
        mDictionary = new MovieDatabase(getContext());
        mOpenHelper = new MovieOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case ENTITY_ID:
           
            	return getEntity(selectionArgs);
            case ENTITIES: 	
            	return getEntities(selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }
    
     /**
     * This method is required in order to query the supported types.
     * It's also useful in our own query() method to determine the type of Uri received.
     */
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ENTITY_ID:
                return DEFINITION_MIME_TYPE;
            case ENTITIES:
            	throw new IllegalArgumentException("Add was called!!! " + uri);
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
    	// Validates the incoming URI. Only the full provider URI is allowed for inserts.
        if (sUriMatcher.match(uri) != ENTITIES) {
            throw new IllegalArgumentException("Here should be ADD uri! " + uri);
        }

        // Gets the current system time in milliseconds
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        long rowId = db.insertWithOnConflict(getTableName(),       
            getNullableColumnForInsert(),         
            values,                          
            SQLiteDatabase.CONFLICT_REPLACE
        );

        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(getContentIdUriBase(), rowId);

            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        // If the insert didn't succeed, then the rowID is <= 0. Throws an exception.
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

   
    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;

        switch (sUriMatcher.match(uri)) {

            case ENTITIES:
                count = db.update(getTableName(), values, where, whereArgs );
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

}
