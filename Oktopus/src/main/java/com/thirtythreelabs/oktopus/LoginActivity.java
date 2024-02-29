package com.thirtythreelabs.oktopus;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;

import com.thirtythreelabs.comm.JsonToOperator;
import com.thirtythreelabs.comm.JsonToLocations;
import com.thirtythreelabs.comm.LoginComm;
import com.thirtythreelabs.comm.LoginLocations;
import com.thirtythreelabs.comm.RestTask;
import com.thirtythreelabs.flowmodel.Flow;
import com.thirtythreelabs.systemmodel.Operator;
import com.thirtythreelabs.systemmodel.Locations;
import com.thirtythreelabs.util.ConvertWordToNumber;
import com.thirtythreelabs.util.WriteLog;
import com.thirtythreelabs.util.readXmlResource;
import com.thirtythreelabs.util.Config;


public class LoginActivity extends Activity implements OnClickListener, View.OnFocusChangeListener {
	
	private WriteLog mLog = new WriteLog();
	
	private Flow mOktopusFlow;
	
	private String mCompanyId = "1";
	private ActionBar mActionBar;
	private Operator mOperator;
	private Locations mLocations;
	
	private EditText mOperatorNameText;
	private EditText mOperatorPasswordText;
	private Button mLoginButton;
	private Button editURL;
	private Spinner mLocationSpin;
	private ArrayList<String> mLocationArray;
	private ArrayAdapter<String> dataAdapter;
	private String selectedLocation = "";
	private String mOperatorName;

	
	private NfcAdapter mNfcAdapter;
	private ProgressDialog mProgressDialog;
	private LoginComm mLoginComm;
	private LoginLocations mLoginLocationsComm;
	private JsonToOperator mJsonToOperator;
	private JsonToLocations mJsonToLocations;
	private String url = "http://192.168.73.11/smpm/rest/";

    PendingIntent mNfcPendingIntent;
    IntentFilter[] mReadTagFilters;
    private boolean mWriteMode = false;
	
    ConvertWordToNumber mConvertWordToNumber = new ConvertWordToNumber();
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	    //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.login);


		editURL = (Button) findViewById(R.id.editURL);
		editURL.setOnClickListener(this);

		mOperatorNameText = (EditText)findViewById(R.id.etNombre);


		mOperatorNameText.setOnFocusChangeListener(this);




		mOperatorPasswordText = (EditText)findViewById(R.id.etPassword);
		mLocationSpin = (Spinner)findViewById(R.id.mLocationSpin);
		mLocationSpin.setVisibility(View.GONE);
		
		mLoginButton = (Button)findViewById(R.id.ButtonEntrar);
//		mLoginButton.setFocusable(true);
//		mLoginButton.setFocusableInTouchMode(true);///add this line
//		 mLoginButton.requestFocus();
		mLoginButton.setOnClickListener(this);
		
		mLoginComm = new LoginComm(this, this, null, true);
		mLoginLocationsComm = new LoginLocations(this, this, null, true);

		mJsonToOperator = new JsonToOperator(this);
		mJsonToLocations = new JsonToLocations(this);
		
		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String version = pInfo.versionName;
        
		mLog.appendLog("Start. Version: " + version);
		
		getOktopusFlow();

		TextView mTestText = (TextView) findViewById(R.id.testText);

		if(Config.URL.equals(url)){
			mTestText.setVisibility(View.GONE);
		}else{
			mTestText.append("  ---  " + Config.URL);
		}
		
	}
	

	@Override
    public void onResume() {
        super.onResume();
        registerReceiver(loginReceiver, new IntentFilter(LoginComm.GET_LOGIN_ACTION));
		registerReceiver(loginLocationReceiver, new IntentFilter(LoginLocations.GET_LOGINLOCATIONS_ACTION));
	}

    @Override
    public void onPause() {
    	super.onPause();
    	unregisterReceiver(loginReceiver);
		unregisterReceiver(loginLocationReceiver);
    }

	
	@Override
    public void onDestroy() {
        super.onDestroy();
    }
	
	
	
	private void getOktopusFlow(){
		
		Serializer mSerializer = new Persister();
//		 File source = new File("/sdcard/download/Orders.xml");

		try {
			mOktopusFlow = mSerializer.read(Flow.class, readXmlResource.readXml(this, R.raw.orders));
			
			mCompanyId = mOktopusFlow.getCompanyId();
						
		} catch (Exception e) {
			// TODO Auto-generated catch block
			setToast("Error serializando flujo");
			e.printStackTrace();
		}
	}
	

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.ButtonEntrar) {
			CharSequence literal = getString(R.string.PLEASE_WAIT);
			mProgressDialog = ProgressDialog.show(this, "", literal, true);

			operatorLogin();
		}
		if (v.getId() == R.id.editURL){
			startActivity(new Intent(LoginActivity.this, EditURL.class));
		}
	}


	private BroadcastReceiver loginReceiver = new BroadcastReceiver() { 
		@Override
		public void onReceive(Context context, Intent intent) { 

			String mJson = intent.getStringExtra(RestTask.HTTP_RESPONSE);
			
			if(mJson != null){
				mOperator = mJsonToOperator.getOperatorFromJson(mJson);
				
				if(mProgressDialog != null) {
					mProgressDialog.dismiss(); 
				}
				
				if(mOperator.getLoginError().equals("N")){


					if(mLocations.getCurrentLocation().equals("PLANTA")){
						mOperator.setOperatorCurrentWarehoueseId("1");
						gotoOrdersActivity();
					}
					else{
						gotoChooseWarehouseActivity();
					}
					
				}else{
					mOperatorNameText.setText("");
					mOperatorPasswordText.setText("");
					mLocationSpin.setVisibility(View.GONE);
					mLocationSpin.setAdapter(null);
					setToast("ERROR: " + mOperator.getLoginErrorMessage());
				}
			}else{
				setToast("No se puede contactar con el servidor.");
			}
		} 
	};

	private BroadcastReceiver loginLocationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String mJson = intent.getStringExtra(RestTask.HTTP_RESPONSE);

			if(mJson != null){
				mLocations = mJsonToLocations.getLocationsFromJson(mJson);

				if(mProgressDialog != null) {
					mProgressDialog.dismiss();
				}


				if(mLocations.getLoginError().equals("N")) {

					try {
						if (mLocations.getLocations().size() > 1) {
							//dropdown

							dataAdapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_item, mLocations.getLocations());
							dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							mLocationSpin.setAdapter(dataAdapter);
							mLocationSpin.setVisibility(View.VISIBLE);
						}
						else if (mLocations.getLocations().size() == 1) {
							selectedLocation = mLocations.getLocations().get(0);
						}
					} catch (Exception e) {
						selectedLocation = "";
					}
				}
				else {
					setToast("ERROR: " + mLocations.getLoginErrorMessage());
				}
			}
			else {
				setToast("No se puede contactar con el servidor.");
			}
		}
	};


	private void operatorLogin(){
		
		mOperatorName = mOperatorNameText.getText().toString().toUpperCase();
		
		String mOperatorPassword = mOperatorPasswordText.getText().toString();

		if (mLocations.getLocations().size() > 1) {
			selectedLocation = mLocationSpin.getSelectedItem().toString();
			mLocations.setCurrentLocation(selectedLocation);
		}
		else {
			mLocations.setCurrentLocation("");
		}

		//mOktopusFlow.setCompanyLocation(selectedLocation);
		
		mLoginComm.loginOperator(mCompanyId, mOperatorName, mOperatorPassword);


	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(v.getId() == R.id.etNombre) {
			if (!hasFocus) {
				mOperatorName = mOperatorNameText.getText().toString().toUpperCase();
				mLoginLocationsComm.loginLocationsOperator(Integer.parseInt(mCompanyId), mOperatorName);
			}
		}
	}

	private void gotoOrdersActivity(){
		Intent intent = new Intent(this, OrdersActivity.class);
		
		intent.putExtra("operator", mOperator);
		intent.putExtra("locations", mLocations);

		startActivity(intent);
	}
	
	
	private void gotoChooseWarehouseActivity(){
		Intent intent = new Intent(this, ChooseWarehouseActivity.class);
		
		intent.putExtra("operator", mOperator);
		intent.putExtra("locations", mLocations);


		startActivity(intent);
	}
	
	
	public void setToast(String myToast){
		mLog.appendLog(myToast);
		Toast.makeText(this, myToast, Toast.LENGTH_SHORT).show();
    }
}