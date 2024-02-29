package com.thirtythreelabs.comm;


import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.thirtythreelabs.oktopus.R;
import com.thirtythreelabs.systemmodel.Locations;
import com.thirtythreelabs.util.WriteLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonToLocations {

	private Activity mActivity;
	private WriteLog mLog = new WriteLog();

	public JsonToLocations(Activity tempActivity){
		mActivity = tempActivity;
	}
	
	@SuppressWarnings("null")
	public Locations getLocationsFromJson(String json) {
    	Locations mLocations = new Locations();

        try {            
        	mLog.appendLog(json);
        	
        	ArrayList <String> tempOperatorLocations =  new ArrayList<String>();;
        	JSONObject operatorObject = new JSONObject(json);


			mLocations.setLoginError(operatorObject.getString("Error"));
			mLocations.setLoginErrorMessage(operatorObject.getString("ErrorMessage"));

			mLocations.setLocationId(operatorObject.getInt("JLocID"));
        	JSONArray tempLocations = operatorObject.getJSONArray("Locations");

            for (int i = 0; i < tempLocations.length(); i++) {
                JSONObject tempItem = tempLocations.getJSONObject(i);
//                String locationId = new String();
				tempOperatorLocations.add(tempItem.getString("TIPOUBICOD"));
            }

			mLocations.setLocations(tempOperatorLocations);

        }
        catch (JSONException e) {
            Log.e("JsonToOperator", "Failed to parse JSON. ", e);
        }
        
        return mLocations;
    }
}
