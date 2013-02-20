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

import android.database.Cursor;
import android.net.Uri;

/**
 * Provides access to the dictionary database.
 */
public class UserAuthenticateProvider extends AbstractAddGetContentProvider {
    String TAG = UserAuthenticateProvider.class.getName();

    public static final String AUTHORITY = "com.movie.database.UserAuthenticateProvider";
    public static final String DICTIONARY_NAME = "authenticate";
 
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DICTIONARY_NAME);
  

    /**
     * The content URI base for a single note. Callers must
     * append a numeric note id to this Uri to retrieve a note
     */
    public static final Uri CONTENT_ID_URI_BASE = Uri.parse(CONTENT_URI + "/#");

	@Override
	public Uri getContentIdUriBase() {
		return CONTENT_ID_URI_BASE;
	}

	@Override
	public Uri getContentUri() {
		return CONTENT_URI;
	}

	@Override
	public String getDictionaryName() {
		return DICTIONARY_NAME;
	}

	@Override
	public String getAuthority() {
		return AUTHORITY;
	}

	@Override
	public String getTableName() {
		return MovieDatabase.AUTHENTICATE_TABLE_NAME;
	}

	@Override
	public String getNullableColumnForInsert() {
		return UserColumns.COLUMN_NAME_USER;
	}

	@Override
	protected Cursor getEntity(String[] selectionArgs) {
		return mDictionary.getUser(selectionArgs, null);
	}

	@Override
	protected Cursor getEntities(String[] selectionArgs) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
