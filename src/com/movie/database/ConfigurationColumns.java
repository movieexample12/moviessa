package com.movie.database;

import android.provider.BaseColumns;

/* "base_url": "http://d3gtl9l2a4fn1j.cloudfront.net/t/p/",
        "secure_base_url": "https://d3gtl9l2a4fn1j.cloudfront.net/t/p/",
        "poster_sizes": [
            "w92",
            "w154",
            "w185",
            "w342",
            "w500",
            "original"
        ],
        "backdrop_sizes": [
            "w300",
            "w780",
            "w1280",
            "original"
        ],
        "profile_sizes": [
            "w45",
            "w185",
            "h632",
            "original"
        ],
        "logo_sizes": [
            "w45",
            "w92",
            "w154",
            "w185",
            "w300",
            "w500",
            "original"
        ]
        we will save one poster, one logo and url
 **/
public class ConfigurationColumns implements BaseColumns {
	
	public final static String COLUMN_NAME_BASE_URL                = "base_url";
	public final static String COLUMN_NAME_POSTER_SIZE             = "poster_size";
	public final static String COLUMN_NAME_LOGO_SIZE               = "logo_size";

}
