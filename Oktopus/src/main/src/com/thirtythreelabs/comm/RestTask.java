package com.thirtythreelabs.comm;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import com.thirtythreelabs.util.WriteLog;
import android.widget.Toast;


public class RestTask extends AsyncTask<HttpUriRequest, Void, String> {
	
	public static final String HTTP_RESPONSE = "httpResponse";
	private Context mContext; 
	private HttpClient mClient; 
	private String mAction;
	private WriteLog mLog;

	private SharedPreferences mSharedPrefs;

	public RestTask(Context context, String action) { 
		mContext = context;
		mAction = action;
		mClient = new DefaultHttpClient();
		mLog = new WriteLog();
		mLog.appendLog("Starting RestTask");
		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public RestTask(Context context, String action, HttpClient client) { 
		mContext = context;
		mAction = action;
		mClient = client;
		mLog = new WriteLog();
		mLog.appendLog("Starting RestTask");
	}

	@Override
	protected String doInBackground(HttpUriRequest... params) {
		mLog.appendLog("resttask.doinbackground");
		try{
			HttpUriRequest request = params[0];
			mLog.appendLog("Uri: " + request.toString());
			HttpResponse serverResponse = mClient.execute(request);
			BasicResponseHandler handler = new BasicResponseHandler(); 
			String response = handler.handleResponse(serverResponse);

			return response;
		} catch (Exception e) {
			mLog.appendLog("RestTask Exception: " + e.toString());
			e.printStackTrace();
			return null;
		} 
	}
	
	@Override
	protected void onPostExecute(String result) {
		Intent intent = new Intent(mAction);
		mLog.appendLog("resttask.onpostexecute");
		if(mAction.equals(ItemsComm.GET_ITEMS_ACTION)){
			SharedPreferences.Editor editor = mSharedPrefs.edit();
			editor.putString(HTTP_RESPONSE, result);

			editor.commit();

		} else{
			intent.putExtra(HTTP_RESPONSE, result); //Broadcast the completion
		}

		mContext.sendBroadcast(intent);
	}

}