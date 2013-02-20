package com.movie.client;

import java.util.List;

/* "images": {
        "base_url": "http://d3gtl9l2a4fn1j.cloudfront.net/t/p/",
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
    },
    "change_keys": [
        "adult",
        "also_known_as",
        "alternative_titles",
        "biography",
        "birthday",
        "budget",
        "cast",
        "character_names",
        "crew",
        "deathday",
        "general",
        "genres",
        "homepage",
        "images",
        "imdb_id",
        "name",
        "original_title",
        "overview",
        "plot_keywords",
        "production_companies",
        "production_countries",
        "releases",
        "revenue",
        "runtime",
        "spoken_languages",
        "status",
        "tagline",
        "title",
        "trailers",
        "translations"
    ]*/
public class RawConfigurationList {
	private List<String> change_keys;
	private RawImageConfiguration images;

	public RawImageConfiguration getImages() {
		return images;
	}

	public void setImages(RawImageConfiguration images) {
		this.images = images;
	}

	public List<String> getChange_keys() {
		return change_keys;
	}

	public void setChange_keys(List<String> change_keys) {
		this.change_keys = change_keys;
	}
}
