package com.movie.client;

import java.text.SimpleDateFormat;
/*
 * "backdrop_path": "/hbn46fQaRmlpBuUrEiFqv0GDL6Y.jpg",
   "id": 24428,
   "original_title": "The Avengers",
   "release_date": "2012-05-04",
   "poster_path": "/cezWGskPY5x7GaglTTRN4Fugfb8.jpg",
   "title": "The Avengers",
   "vote_average": 8.5,
   "vote_count": 117 
 **/

public class RawMovie {
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private final int id;
	private final String backdrop_path;
	private final String original_title;
	private final String release_date;
	private final String poster_path;
	private final String title;
	private final double vote_average;
	private final int vote_count;
//	private final long mRawContactId;
//    private final long mSyncState;
//    private final boolean isDeleted;
//    private final long serverMovieId;
//    private final long rawMovieId;
    
	public RawMovie(int id, String backdrop_path, String original_title,  String release_date, 
			String poster_path, String title, double vote_average, int vote_count
//			, long mRawContactId, long mSyncState, boolean isDeleted,
//			long mServerContactId, long rawMovieId
			) {
		this.id = id;
		this.backdrop_path = backdrop_path;
		this.original_title = original_title;
		this.release_date = release_date;
		this.poster_path = poster_path;
		this.title = title;
		this.vote_count = vote_count;
		this.vote_average = vote_average;
//		this.mRawContactId = mRawContactId;
//		this.mSyncState = mSyncState;
//		this.isDeleted = isDeleted;
//		this.serverMovieId = mServerContactId;
//		this.rawMovieId = rawMovieId;
	}

	
	public int getId() {
		return id;
	}
	
	public String getBackdrop_path() {
		return backdrop_path;
	}
	public String getOriginal_title() {
		return original_title;
	}
	 
	public String getRelease_date() {
		return release_date;
	}
	 
	public String getPoster_path() {
		return poster_path;
	}
	 
	public String getTitle() {
		return title;
	}

	public double getVote_average() {
		return vote_average;
	}
	
	public int getVote_count() {
		return vote_count;
	}
	
//	public long getRawContactId() {
//		return mRawContactId;
//	}
//
//
//	public long getSyncState() {
//		return mSyncState;
//	}
//
//
//	public boolean isDeleted() {
//		return isDeleted;
//	}
//	
//	public long getServerMovieId() {
//		return serverMovieId;
//	}
//
//	public long getRawMovieId() {
//		return rawMovieId;
//	}

}
