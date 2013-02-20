package com.movie.authentication;
import java.io.IOException;
import java.util.Map;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.movie.R;
import com.movie.activity.MoviesListActvity;
import com.movie.client.NetworkUtilities;
import com.movie.database.MovieProvider;
import com.movie.database.UserAuthenticateProvider;
import com.movie.database.UserColumns;


public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    /** The Intent extra to store password. */
    public static final String PARAM_PASSWORD = "password";

    /** The Intent extra to store username. */
    public static final String PARAM_USERNAME = "username";
    
    /** The Intent extra to store api_key. */
    public static final String PARAM_API = "api_key";
    
    /** The Intent extra to store username. */
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";
    
    /** Keep track of the login task so can cancel it if requested */

    /** Keep track of the progress dialog so we can dismiss it */
    private ProgressDialog mProgressDialog = null;

    private UserLoginTask mAuthTask = null;
    /**
     * If set we are just checking that the user knows their credentials; this
     * doesn't cause the user's password or authToken to be changed on the
     * device.
     */
    private Boolean mConfirmCredentials = false;//TODO:?
	
	private static final String TAG = AuthenticatorActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle icicle) {
		
		Log.i(TAG, "onCreate(" + icicle + ")");
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.login);
		findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleLogin(v);
			}
		});
	}
	
	/**
     * Represents an asynchronous task used to authenticate a user against the
     * SampleSync Service
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Map<String, String>> {

        @Override
        protected Map<String, String> doInBackground(Void... p) {
            try {
            	String pwd = getPasswordEdit().getText().toString();
            	String unm = getUsernameEdit().getText().toString();
            	String api = getApiEdit().getText().toString();
               // return NetworkUtilities.getAuthenticateUrl(((EditText)findViewById(R.id.api)).getText().toString());
            	//Map<String, String> request_token_map = NetworkUtilities.getRequestTokenId(unm, pwd, api);
            	Map<String, String> sessionIdMap = NetworkUtilities.getSessionId(unm, pwd, api, 
            			"d55d70445d4e662ad911bd36c83423dc1e8c1bad");//request_token_map.get(NetworkUtilities.REQUEST_TOKEN_KEY));
            
            	//sessionIdMap.put(NetworkUtilities.SESSION_ID_KEY, "3ddc1f1913e8113bc65b89006d93c1d476c8a917");
            	sessionIdMap.put(NetworkUtilities.API_KEY, api);
            	sessionIdMap.put(NetworkUtilities.PASSWORD_KEY, pwd);
            	
            			
            //	String allowedRequestToken = "d52b410a0d57076992f7cd3d666094a763107c62";
//            	Log.e(TAG, "NetworkUtilities.getSessionId start");
//            	return NetworkUtilities.getSessionId(api, unm, pwd, request_token_map.get(NetworkUtilities.REQUEST_TOKEN_KEY));
            	return sessionIdMap;
                
            } catch (Exception ex) {
                Log.e(TAG, "UserLoginTask.doInBackground: failed to authenticate " + ex);
                Log.i(TAG, ex.toString());
                return null;
            }
			
        }
        
        @Override
        protected void onPostExecute(Map<String, String> result) {
        	onAuthenticationResult(result);
        }
        
    }
    
    private TextView getMessage() {
    	return (TextView) findViewById(R.id.message);
    }
    
    /**
     * Hides the progress UI for a lengthy operation.
     */
    private void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    
    /**
     * Called when the authentication process completes (see attemptLogin()).
     *
     * @param authToken the authentication token returned by the server, or NULL if
     *            authentication failed.
     */
    public void onAuthenticationResult(Map<String, String> map) {

    	String session_id_s = map.get(NetworkUtilities.SESSION_ID_KEY);
        boolean success = ((session_id_s != null) && (session_id_s.length() > 0));
        
        hideProgress();

        if (success) {
        	Log.i(TAG, "onAuthenticationResult success");
            if (!mConfirmCredentials) {
                finishLogin(session_id_s, map.get(NetworkUtilities.API_KEY), map.get(NetworkUtilities.PASSWORD_KEY));
            } else {
                finishConfirmCredentials(success);
            }
            
        } else {
            Log.e(TAG, "onAuthenticationResult: failed to authenticate");
            if (requestNewAccount()) {
                // "Please enter a valid username/password.
            	getMessage().setText(getText(R.string.login_activity_loginfail_text_both));
            } else {
                // "Please enter a valid password." (Used when the
                // account is already in the database but the password
                // doesn't work.)
            	getMessage().setText(getText(R.string.login_activity_loginfail_text_pwonly));
            }
        }
        
    }

    public void onAuthenticationCancel() {
        Log.i(TAG, "onAuthenticationCancel()");

        // Hide the progress dialog
        hideProgress();
    }
    
    private boolean requestNewAccount() {
    	return getUsernameEdit() == null;
    }
    
    private AccountManager getAccountManager() {
    	return  AccountManager.get(this);
    }
    
    public class OnError implements Callback {

    	@Override
    	public boolean handleMessage(Message msg) {
    	    Log.e("onError","ERROR");
    	    return false;
    	}

    }
    
    public class OnTokenAcquired implements AccountManagerCallback<Bundle> {

    	@Override
    	public void run(AccountManagerFuture<Bundle> result) {
    	    try {
    	        Bundle bundle = result.getResult();
    	        Log.e("onTokenAcquired",bundle.getString(AccountManager.KEY_AUTHTOKEN));
    	    } catch (OperationCanceledException e) {
    	        Log.e("onTokenAcquired","operationcanceled");
    	    } catch (AuthenticatorException e) {
    	        Log.e("onTokenAcquired","authenticatorexception");
    	    } catch (IOException e) {
    	        Log.e("onTokenAcquired","IOException");
    	    }
    	}
    }
    
    /**
     * Called when response is received from the server for authentication
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller. We store the
     * authToken that's returned from the server as the 'password' for this
     * account - so we're never storing the user's actual password locally.
     *
     * @param result the confirmCredentials result.
     */
    private void finishLogin(String sessionId, String api, String password) {

        Log.i(TAG, "finishLogin() sessionId=" + sessionId + ",api=" + api + ",pwd=" + password);
        
        final Account account = new Account(getUsernameEdit().getText().toString(), Constants.ACCOUNT_TYPE);
        
        AccountManager am = AccountManager.get(AuthenticatorActivity.this);
		if (am.addAccountExplicitly(account, password, null)) {
			
			Bundle bundle = new Bundle();
	        bundle.putString(NetworkUtilities.SESSION_ID_KEY, sessionId);
	        bundle.putString(NetworkUtilities.API_KEY, api);
	        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, getUsernameEdit().getText().toString());
	        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
			setAccountAuthenticatorResult(bundle);
	        
	        ContentValues initialValues = new ContentValues();
			initialValues.put(UserColumns.COLUMN_NAME_USER,  account.name);
			initialValues.put(UserColumns.COLUMN_NAME_API,  api);
			initialValues.put(UserColumns.COLUMN_NAME_SESSION_ID,  sessionId);
	        getContentResolver().insert(UserAuthenticateProvider.CONTENT_URI, initialValues);
	        
	        Bundle params = new Bundle();
	        params.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, false);
	        params.putBoolean(ContentResolver.SYNC_EXTRAS_DO_NOT_RETRY, false);
	        params.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, false);
	        ContentResolver.addPeriodicSync(account, MovieProvider.AUTHORITY, params, 30);
	        ContentResolver.setSyncAutomatically(account, MovieProvider.AUTHORITY, true);
	        ContentResolver.requestSync(account, MovieProvider.AUTHORITY,params);
		}
		
		Intent main = new Intent(this, MoviesListActvity.class);
    	this.startActivity(main);
    	getContentResolver().notifyChange(MovieProvider.CONTENT_URI, null);
    	finish();
        
    }
	
    /**
     * Called when response is received from the server for confirm credentials
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller.
     *
     * @param result the confirmCredentials result.
     */
    private void finishConfirmCredentials(boolean result) {
        Log.i(TAG, "finishConfirmCredentials()");
        final Account account = new Account(getUsernameEdit().getText().toString(), Constants.ACCOUNT_TYPE);
        getAccountManager().setPassword(account, getPasswordEdit().getText().toString());
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_BOOLEAN_RESULT, result);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }
    
    private TextView getMessageTextView() {
    	return (TextView) findViewById(R.id.message);
    }
    
    private EditText getUsernameEdit() {
    	return (EditText) findViewById(R.id.username_field);
    }
    
    private EditText getPasswordEdit() {
    	return (EditText) findViewById(R.id.password_field);
    }
    
    private EditText getApiEdit() {
    	return (EditText) findViewById(R.id.api_key_field);
    }
    
    /**
     * Returns the message to be displayed at the top of the login dialog box.
     */
    private CharSequence getMessageText() {
        getString(R.string.label);
        if (TextUtils.isEmpty(getUsernameEdit().getText())) {
            // If no username, then we ask the user to log in using an
            // appropriate service.
            final CharSequence msg = getText(R.string.login_activity_newaccount_text);
            return msg;
        }
        if (TextUtils.isEmpty(getPasswordEdit().getText())) {
            // We have an account but no password
            return getText(R.string.login_activity_loginfail_text_pwmissing);
        }
        return null;
    }
   
    /**
     * Shows the progress UI for a lengthy operation.
     */
    @SuppressWarnings("deprecation")
	private void showProgress() {
        showDialog(0);
    }
    
    /**
     * Handles onClick event on the Submit button. Sends username/password to
     * the server for authentication. The button is configured to call
     * handleLogin() in the layout XML.
     *
     * @param view The Submit button for which this method is invoked
     */
    public void handleLogin(View view) {
        //TODO: here might be an error
    	if (TextUtils.isEmpty(getUsernameEdit().getText().toString()) || TextUtils.isEmpty(getPasswordEdit().getText().toString())) {
    		getMessageTextView().setText(getMessageText());
        } else {
            // Show a progress dialog, and kick off a background task to perform
            // the user login attempt.
            showProgress();
            mAuthTask = new UserLoginTask();
            mAuthTask.execute();
        }
    }
    
    
    
}
