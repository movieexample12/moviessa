package com.movie.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.movie.database.ConfigurationColumns;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class MovieCache {
	
	private static final String TAG = MovieCache.class.getSimpleName();
	private static final String IMAGES_LIST_KEY       = "posters";
	private static final String THUMB_KEY			  = "thumb";
	private static final String EXTERNAL_FOLDER		  = "Pictures";
	private static final String SEPARATOR 			  = "/"; 
	private static final String EXTENSION			  = ".jpg"; 
	
	private static Map<String, MovieCache> mapMovieCache = new HashMap<String, MovieCache>();
	
	private Context context;
//	private IPreferences preferences;
	
	public MovieCache(Context context) {
		this.context = context;
		//this.preferences = preferences;
	}
	
    private static String getShortName(String type, int id) {
    	return  type + id + EXTENSION;
    }
	
    private void writeFile(InputStream imageIS, OutputStream os, byte[] data, String filename) throws IOException {
		imageIS.read(data);
        os.write(data);
        imageIS.close();
        os.close();
    }
    
	private void createInternalStoragePrivatePicture(InputStream imageIS, String filename) {
		try {
	        OutputStream os = context.openFileOutput(filename, Context.MODE_PRIVATE);
	        byte[] data = new byte[imageIS.available()];
	        writeFile(imageIS, os, data, filename);
		} catch (IOException e) {
	        Log.w("InternalStorage", filename, e);
	    }
	}
	
	private static File getExternalFile(String filename) {
		String pathToExternalStorage = Environment.getExternalStorageDirectory().getAbsolutePath() + SEPARATOR + EXTERNAL_FOLDER;
    	File appDirectory = new File(pathToExternalStorage);   
    	if (!appDirectory.exists()) {
    		appDirectory.mkdir();
    	}
    	return new File(appDirectory.getAbsolutePath() + SEPARATOR + filename);
	}
	private void createExternalStoragePrivatePicture(InputStream imageIS, String filename) {
	    try {
	        OutputStream os = new FileOutputStream(getExternalFile(filename));
	        byte[] data = new byte[imageIS.available()];
	        writeFile(imageIS, os, data, filename);
	    } catch (IOException e) {
	        Log.w("ExternalStorage", filename, e);
	    }
	}
	
	public Boolean useExternalDevice(Context context) {
	    return true;//preferences.useExternalStorage();	
	}
	
	private void createPicture(String saveTo, String urlStr) {
		try {
			URL url = new URL(urlStr);
		    if (useExternalDevice(context)) {
				createExternalStoragePrivatePicture(url.openConnection().getInputStream(), saveTo);
		    } else {
				createInternalStoragePrivatePicture(url.openConnection().getInputStream(), saveTo);
		    }
		    
		} catch (Exception e) {
			Log.e(MovieCache.class.getSimpleName(), e.getMessage());
		}
	}
	
	public static boolean externalDeviceAvailable() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		
		return mExternalStorageAvailable && mExternalStorageWriteable;
	}
	
	public String addMoviePoster(int id, String path) {
		Log.i(TAG, "addMoviePoster");
		String filename = getShortName(ConfigurationColumns.COLUMN_NAME_POSTER_SIZE, id);
		createPicture(filename, path);
	  	return filename;
	}
	
	private File getFile(Context c, String name) {
		File f = null ;
		if (useExternalDevice(c)) {
			f = getExternalFile(name);  
		} else {
			f = c.getFileStreamPath (name);
		}
		return f;
	}
	
	public synchronized Bitmap getPoster(int movieId, Context context) {
		File f = getFile(context, getShortName(ConfigurationColumns.COLUMN_NAME_POSTER_SIZE, movieId));
		if (!f.exists()) {
		    return null;
		}
	    return BitmapFactory.decodeFile(f.getAbsolutePath());
	}
	
	public Bitmap getPoster(int position) {
	     return getPoster(position, context);
	}
	
	public Bitmap getThumb(int position) {
		File f = getFile(context, getShortName(THUMB_KEY, position));
		if (!f.exists()) {
			return null;
		}
		return BitmapFactory.decodeFile(f.getAbsolutePath());
	}
	
	public void removeImages(int i) {
		File [] fs = new File[] {getFile(context, getShortName(THUMB_KEY, i)), getFile(context, getShortName(IMAGES_LIST_KEY, i))};
		for (File f: fs) {
			f.delete();
		}
	}
	
	public static void addCacheToMap(String name, MovieCache cache) {
		mapMovieCache.put(name, cache);
	}
	
	public static MovieCache getCacheFromMap(String name) {
		return mapMovieCache.get(name);
	}
}
