package com.movie.activity;

import java.util.ArrayList;
import java.util.List;

import com.movie.R;
import com.movie.client.RawMovie;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//SimpleCursorAdapter
public class Adapter extends BaseAdapter {
	private static final String TAG = Adapter.class.getSimpleName();
	private List<RawMovie> movies = new ArrayList<RawMovie>();
	
	private Activity activity;
    private LayoutInflater inflater;
    public Adapter(Activity a) {
    	activity = a;
    }
    
    public void addRawMovies(List<RawMovie> RawMovies) {
        this.movies.addAll(RawMovies);
    }
    
    public void addRawMovie(RawMovie movie) {
        this.movies.add(movie);
    }
    
    public void clear() {
    	this.movies.clear();
    }
    
    @Override
	public int getCount() {
    	if (movies == null) {
    		return 0;
    	}
		return movies.size();
	}

	@Override
	public Object getItem(int position) {
		if (position >= getCount()) {
			return null;
		}
		return movies.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	static class ViewHolder {
	    TextView title;
	    TextView releaseDate;
	    TextView rating;
	    ImageView image;
	    int position;
    }
	
	@Override
	public View getView(final int position, View vi, final ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (vi == null) {
			if (inflater == null) {
				inflater = activity.getLayoutInflater();
			}
			vi = inflater.inflate(R.layout.list_item, parent, false);
			holder.image = (ImageView) vi.findViewById(R.id.image);
			holder.title = (TextView) vi.findViewById(R.id.title);
			holder.releaseDate = (TextView) vi.findViewById(R.id.release_date);
			holder.rating = (TextView) vi.findViewById(R.id.vote_average);
			vi.setTag(holder);
        } else {
        	holder = (ViewHolder) vi.getTag(); 
        }
	 
        RawMovie movie = (RawMovie)getItem(position);
        if (movie != null) {
	        holder.title.setText(String.valueOf(movie.getTitle()));
	        if (movie.getRelease_date() != null) {
	        	holder.releaseDate.setText(movie.getRelease_date());
	        }
	        
	        holder.rating.setText(Double.valueOf(movie.getVote_average()).toString());
	        Log.i(TAG, String.valueOf(movie.getId()));
	        
	        Bitmap poster = new MovieCache(activity).getPoster(movie.getId());
	        holder.image.setImageBitmap(poster);
        }
        return vi;
	}
	
	
}
