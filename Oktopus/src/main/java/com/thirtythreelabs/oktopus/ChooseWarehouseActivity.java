package com.thirtythreelabs.oktopus;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.res.Resources;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thirtythreelabs.adapters.WarehouseAdapter;
import com.thirtythreelabs.comm.HeadersByWarehouseComm;
import com.thirtythreelabs.comm.JsonToHeadersByWarehouse;
import com.thirtythreelabs.comm.RestTask;
import com.thirtythreelabs.oktopus.R;
import com.thirtythreelabs.systemmodel.Operator;
import com.thirtythreelabs.systemmodel.Warehouse;
import com.thirtythreelabs.systemmodel.Locations;
import com.thirtythreelabs.util.WriteLog;


public class ChooseWarehouseActivity extends Activity implements OnClickListener{

	private WriteLog mLog = new WriteLog();
	
	private ListView mWarehousesListView;
	private WarehouseAdapter mWarehouseAdapter;
	private List<Warehouse> mWarehouseList;
	private Operator mOperator;
	private Locations mLocations;
	private String mOperatorName;
	
	private ProgressDialog mProgressDialog;
	
	private HeadersByWarehouseComm mHeadersByWarehouseComm;
	private JsonToHeadersByWarehouse mJsonToHeadersByWarehouse = new JsonToHeadersByWarehouse(this);
	
	private Button mExitButton;
	private Button mRetryButton;
	
	private final Handler mHandler = new Handler();
	private boolean mIsRefreshing;
	private static final int REFRESH_TIME = 1000*60;
	
	@Override
	public void onCreate(Bundle mBundle) {
		super.onCreate(mBundle);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.choose_warehouse);
				
		mWarehousesListView = (ListView) findViewById(R.id.warehousesListview);
		mWarehouseAdapter = new WarehouseAdapter(this, this);
		
		mWarehousesListView.setAdapter(mWarehouseAdapter);
        
		
		mExitButton = (Button) findViewById(R.id.exitButton);
		mExitButton.setOnClickListener(this);
		
		mRetryButton = (Button) findViewById(R.id.refreshButton);
		mRetryButton.setOnClickListener(this);

		mOperator = (Operator) getIntent().getSerializableExtra("operator");
		mLocations = (Locations) getIntent().getSerializableExtra("locations");
		mOperatorName = mOperator.getOperatorName();
		
		mHeadersByWarehouseComm = new HeadersByWarehouseComm(this, this, null, true, mOperator.getOperatorId(), mLocations.getLocationId(), mLocations.getCurrentLocation());

		populateWarehouses(mOperator.getOperatorWarehouses());

		
		mWarehousesListView.setClickable(true);
		mWarehousesListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				String tempWarehouseId = mWarehouseList.get(position).getWarehouseId();
		        
				mOperator.setOperatorCurrentWarehoueseId(tempWarehouseId);
	        	gotoOrdersActivity();
	        	
		        //setToast(tempWarehouseId);
				
			}
			
		});
		
		TextView mFloor = (TextView) findViewById(R.id.operatorAndFloor);
	    mFloor.setText(getString(R.string.app_name) + " | " + mOperatorName);

	}

	private final Runnable mTimerRunnable = new Runnable() {
		@Override
        public void run() {
            mHeadersByWarehouseComm.getHeadersByWarehouse();
            if(mIsRefreshing){
            	mHandler.postDelayed(this, REFRESH_TIME);
            }
        }
    };	
	
    
	@Override
	public void onResume() {
		super.onResume();
		getHeadersByWarehouse();
		registerReceiver(receiver, new IntentFilter(HeadersByWarehouseComm.GET_HEADERS_BY_WAREHOUSE_ACTION)); 
		
		mIsRefreshing = true;
		mHandler.postDelayed(mTimerRunnable, REFRESH_TIME);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
		
		mIsRefreshing = false;
		mHandler.removeCallbacks(mTimerRunnable);
	}
	
	@Override
    public void onDestroy() {        
        super.onDestroy();
	}
        
	
	public void setToast(String myToast){
		mLog.appendLog(myToast);
		Toast.makeText(this, myToast, Toast.LENGTH_SHORT).show();
    }

	private void getHeadersByWarehouse(){
		CharSequence literal = getString(R.string.DOWNLOADING_ORDERS);
		mProgressDialog = ProgressDialog.show(this, "", literal, true);
		
		mHeadersByWarehouseComm.getHeadersByWarehouse();
	}
	
	public void populateWarehouses(List<Warehouse> pWarehouseList){
		mWarehouseList = pWarehouseList;
		mWarehouseAdapter.clear();
		int lmt;

		if (mLocations.getCurrentLocation().equals("TORRE")) {
			lmt = 3;
			if (mWarehouseList.size() == 1) {
				Warehouse tempp = new Warehouse();
				tempp.setWarehouseId("2");
				tempp.setWarehouseName(getResources().getString(R.string.WAREHOUSE) + " 2");
				Warehouse tempp2 = new Warehouse();
				tempp2.setWarehouseId("3");
				tempp2.setWarehouseName(getResources().getString(R.string.WAREHOUSE) + " 3");
				mWarehouseList.add(tempp);
				mWarehouseList.add(tempp2);
			}
			if (mWarehouseList.size() == 2) {
				Warehouse tempp = new Warehouse();
				tempp.setWarehouseId("3");
				mWarehouseList.add(tempp);
			}
			mLog.appendLog("LOCATION: 'TORRE', LMT: 3, mWarehouseList len: " + mWarehouseList.size());
		}
		else if (mLocations.getCurrentLocation().equals("PLANTA")) {
			lmt = 1;
			mLog.appendLog("LOCATION: 'PLANTA', LMT: 1, mWarehouseList len: " + mWarehouseList.size());
		}
		else {
			lmt = mWarehouseList.size();
		}
		try {
			int i = 1;
			for (Warehouse tempWarehouse : mWarehouseList) {
				if (i <= lmt) {
					mWarehouseAdapter.add(tempWarehouse);
				}
				i++;
			}
		}catch (Exception e){
			mLog.appendLog(e.toString());
		}

        mWarehouseAdapter.notifyDataSetChanged();
        
    }
	
	private BroadcastReceiver receiver = new BroadcastReceiver() { 
		@Override
		public void onReceive(Context context, Intent intent) {
		String mJson = intent.getStringExtra(RestTask.HTTP_RESPONSE);

		mLog.appendLog(mJson);

		List<Warehouse> tempWarehouseList = mJsonToHeadersByWarehouse.getOrdersFromJson(mJson);

//		populateWarehouses(tempWarehouseList);
		for (Warehouse tempWarehouse : tempWarehouseList) {
			for (Warehouse tempWarehouseLocal : mWarehouseList) {
				if(tempWarehouse.getWarehouseId().equals(tempWarehouseLocal.getWarehouseId())){
					tempWarehouseLocal.setQtyOfHeaders(tempWarehouse.getQtyOfHeaders());
					tempWarehouseLocal.setQtyOfOrders(tempWarehouse.getQtyOfOrders());
				}
			}
        }
		mWarehouseAdapter.notifyDataSetChanged();

		if(mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	        
		} 
	};
	
	
	public void gotoLoginActivity(){
		Intent tempIntent = null;

		tempIntent = new Intent(this, LoginActivity.class);
		
		startActivity(tempIntent);
	}
		
	private void gotoOrdersActivity(){
		Intent intent = new Intent(this, OrdersActivity.class);
		
		intent.putExtra("operator", mOperator);
		intent.putExtra("locations", mLocations);

		startActivity(intent);
	}




	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.exitButton) {
			gotoLoginActivity();
		} else if (v.getId() == R.id.refreshButton) {
			getHeadersByWarehouse();
		}

	}

}
