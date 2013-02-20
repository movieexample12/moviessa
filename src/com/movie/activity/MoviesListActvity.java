package com.movie.activity;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.movie.R;
import com.movie.client.RawMovie;
import com.movie.database.MovieColumns;
import com.movie.database.MovieProvider;

@SuppressLint("SimpleDateFormat")
public class MoviesListActvity extends ListActivity implements OnScrollListener {
	
	protected MySimpleCursorAdapter adapter = null;
	protected ListView list = null;
	private ProgressDialog progressDialog = null;
    private int pageNum = 1;
	MovieCache cache; 
    protected View footer;
	private boolean limitExtended = false;
	private boolean searchMode = false;
	private String  query = "";
	MovieUpdateManager updateManager;
	MovieContentObserver movieContentObserver;
	
	private MovieCache getMovieCache() {
		if (cache == null) {
			cache = new MovieCache(this);
		}
		return cache;
		
	}
	
	class MySimpleCursorAdapter extends SimpleCursorAdapter {
		

		public MySimpleCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to, int flags) {
			super(context, layout, c, from, to, flags);
		}

		public void setViewImage(android.widget.ImageView arg0, String arg1) {
			try {
				Bitmap poster = getMovieCache().getPoster(Integer.valueOf(arg1));
				((ImageView) findViewById(R.id.image)).setImageBitmap(poster);
			} catch (Exception ex) {
				Log.e("MySimpleCursorAdapter", ex.getMessage());
			}
			
		};
	}
	
	public MySimpleCursorAdapter getAdapter() {
		if (adapter == null) {
			String [] projection = new String [] {MovieColumns.COLUMN_NAME_ID, MovieColumns.COLUMN_NAME_TITLE, MovieColumns.COLUMN_NAME_VOTE_AVERAGE, MovieColumns.COLUMN_NAME_RELEASE_DATE, 
					MovieColumns.COLUMN_NAME_POSTER_FILENAME, MovieColumns.COLUMN_NAME_POSTER_PATH};
			Cursor c = getContentResolver().query(MovieProvider.CONTENT_URI, projection, null,
	                null, null);
			if (c == null) {
				MovieUpdateManager.addMovie(this, new RawMovie(0, "", "", "", "", "", 0, 0), getMovieCache(), "");
			}
			c = getContentResolver().query(MovieProvider.CONTENT_URI, projection, null, null, null);
			String[] from = new String[] {MovieColumns.COLUMN_NAME_POSTER_FILENAME, MovieColumns.COLUMN_NAME_TITLE, MovieColumns.COLUMN_NAME_RELEASE_DATE, MovieColumns.COLUMN_NAME_VOTE_AVERAGE };
			int[] to = new int[] { R.id.image, R.id.title, R.id.release_date, R.id.vote_average };
			int flags = 0;
			adapter = new MySimpleCursorAdapter(this, R.layout.list_item, c, from, to, flags);
			
		}
		return adapter;
	}
	
//	public MovieUpdateManager getUpdateManager() {
//		if (updateManager == null) {
//			updateManager = new MovieUpdateManager(getAdapter(), this);
//		}
//		return updateManager;
//	}
//	public MovieContentObserver getMovieContentObserver() {
//		if (movieContentObserver == null) {
//			movieContentObserver = new MovieContentObserver(new Handler(), getUpdateManager());
//		}
//		return movieContentObserver;
//	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_activity);
        
//        getContentResolver().registerContentObserver(MovieProvider.CONTENT_URI, 
//                 false, getMovieContentObserver());
//      
//        getListView().setAdapter(getAdapter());
        setListAdapter(getAdapter());
    }

    protected void loadPage() {
//    	getUpdateManager().update();
    }
    
    public void onScroll(AbsListView view, int firstVisible, int visibleCount, int totalCount) {
		if (!searchMode && !limitExtended && (firstVisible + visibleCount >= totalCount && totalCount != 0)) {
		    loadPage();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
	
}
