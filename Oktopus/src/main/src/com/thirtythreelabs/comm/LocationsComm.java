package com.thirtythreelabs.comm;

import android.os.StrictMode;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.thirtythreelabs.oktopus.R;
import com.thirtythreelabs.util.Config;
import com.thirtythreelabs.util.WriteLog;

public class LocationsComm {

    private boolean mOnline;
    private String mJStoID;
    private String mJLocID;

    private String mLocations;

    private WriteLog mLog = new WriteLog();

    public static final String GET_ITEMS_ACTION = "com.thirtythreelabs.oktopus.GET_ITEMS_ACTION";

    private static final String GET_LOCATIONS_URI = Config.URL + "getubicacion/";

    public LocationsComm (String tempJStoID, String tempJLocID, boolean tempOnline){
        mOnline = tempOnline;
        mJStoID = tempJStoID;
        mJLocID = tempJLocID;
    }

    public String getItemLocations() {

        if(mOnline){
            try{
                mLocations = "";
                mLog.appendLog("ItemsComm: getItems()");

                String url = String.format(GET_LOCATIONS_URI);
                HttpPost postRequest = new HttpPost(new URI(url));

                String mJson;
                JSONObject object = new JSONObject();
                try {
                    object.put("JLocID", mJLocID);

                    object.put("JStoID", mJStoID);

                    mLog.appendLog("LocationsComm json:");
                    mLog.appendLog(object.toString());


                } catch (Exception ex) {
                    mLog.appendLog("Error: " + ex);
                }

                mJson = object.toString();
                StringEntity entity = new StringEntity(mJson);

                mLog.appendLog(mJson);

                postRequest.setEntity(entity);
                postRequest.setHeader("Content-Type", "application/json");

                HttpClient client = new DefaultHttpClient();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);

                HttpResponse response = client.execute(postRequest);

                String responseString = EntityUtils.toString(response.getEntity());

                JSONObject locationsJson = new JSONObject(responseString);

                mLog.appendLog(responseString);

                if (locationsJson.getString("Exist").equals("S")) {
                    JSONArray locationsArray = locationsJson.getJSONArray("SDTUbicacion");
                    for (int i = 0 ; i < locationsArray.length(); i++) {
                        JSONObject loc = locationsArray.getJSONObject(i);
                        String locationName = loc.getString("Location");
                        String shelfID = loc.getString("ESTID");
                        String porID = loc.getString("PORID");
                        String levelID = loc.getString("NIVID");
                        mLocations += locationName + " - EST: " + shelfID + "  - PORT: " + porID + " - NIVEL: " + levelID + System.getProperty("line.separator");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{

            mLocations = "";

			/*
				if(mLang == "es" || mLang == "espa�ol"){
					mJson = "{\"Lines\":[{\"HeaderId\":\"27876\",\"LineId\":3,\"ItemId\":\"CU3AXS039\",\"ItemDescription\":\"CORREA GATES 13X1041\",\"ItemHowToRead\":\"A, X, S, 0, 39\",\"ItemDescriptionHowtoRead\":\"CORREA INDUSTRIAL\",\"ItemInventory\":\"913.00\",\"BrandId\":\"GATES\",\"BrandName\":\"GATES\",\"ItemImage\":\"\",\"UnitId\":0,\"UnitDescription\":\"UNIDADES\",\"ItemOrigin\":\"BRASIL\",\"ItemQuantity\":\"4.00\",\"ItemStatusId\":\"F\",\"ItemStatusDescription\":\"\",\"ItemLastOperatorId\":0,\"ItemLastOperatorName\":\"\",\"ItemXCoord\":\"\",\"ItemYCoord\":\"\",\"ItemZCoord\":\"\",\"ItemSupplierId\":500094,\"ItemSupplierName\":\"GATES BRASIL\"},{\"HeaderId\":\"27876\",\"LineId\":4,\"ItemId\":\"CU3AXS040\",\"ItemDescription\":\"CORREA GATES 13X1067\",\"ItemHowToRead\":\"A, X, S, 0, 40\",\"ItemDescriptionHowtoRead\":\"CORREA INDUSTRIAL\",\"ItemInventory\":\"451.00\",\"BrandId\":\"GATES\",\"BrandName\":\"GATES\",\"ItemImage\":\"\",\"UnitId\":0,\"UnitDescription\":\"UNIDADES\",\"ItemOrigin\":\"BRASIL\",\"ItemQuantity\":\"2.00\",\"ItemStatusId\":\"F\",\"ItemStatusDescription\":\"\",\"ItemLastOperatorId\":0,\"ItemLastOperatorName\":\"\",\"ItemXCoord\":\"\",\"ItemYCoord\":\"\",\"ItemZCoord\":\"\",\"ItemSupplierId\":500094,\"ItemSupplierName\":\"GATES BRASIL\"},{\"HeaderId\":\"27876\",\"LineId\":8,\"ItemId\":\"CU3AXS058\",\"ItemDescription\":\"CORREA GATES 13X1524\",\"ItemHowToRead\":\"A, X, S, 0, 58\",\"ItemDescriptionHowtoRead\":\"CORREA INDUSTRIAL\",\"ItemInventory\":\"216.00\",\"BrandId\":\"GATES\",\"BrandName\":\"GATES\",\"ItemImage\":\"\",\"UnitId\":0,\"UnitDescription\":\"UNIDADES\",\"ItemOrigin\":\"BRASIL\",\"ItemQuantity\":\"2.00\",\"ItemStatusId\":\"F\",\"ItemStatusDescription\":\"\",\"ItemLastOperatorId\":0,\"ItemLastOperatorName\":\"\",\"ItemXCoord\":\"\",\"ItemYCoord\":\"\",\"ItemZCoord\":\"\",\"ItemSupplierId\":500094,\"ItemSupplierName\":\"GATES BRASIL\"},{\"HeaderId\":\"27876\",\"LineId\":9,\"ItemId\":\"CU3AXS059\",\"ItemDescription\":\"CORREA GATES 13X1549\",\"ItemHowToRead\":\"A, X, S, 0, 59\",\"ItemDescriptionHowtoRead\":\"CORREA INDUSTRIAL\",\"ItemInventory\":\"266.00\",\"BrandId\":\"GATES\",\"BrandName\":\"GATES\",\"ItemImage\":\"\",\"UnitId\":0,\"UnitDescription\":\"UNIDADES\",\"ItemOrigin\":\"BRASIL\",\"ItemQuantity\":\"2.00\",\"ItemStatusId\":\"F\",\"ItemStatusDescription\":\"\",\"ItemLastOperatorId\":0,\"ItemLastOperatorName\":\"\",\"ItemXCoord\":\"\",\"ItemYCoord\":\"\",\"ItemZCoord\":\"\",\"ItemSupplierId\":500094,\"ItemSupplierName\":\"GATES BRASIL\"},{\"HeaderId\":\"27876\",\"LineId\":1,\"ItemId\":\"CU3G6PK1740\",\"ItemDescription\":\"CORR M-V K060685 VW GOL SANTANA 95 ALT DH PG206 1.9DFULL\",\"ItemHowToRead\":\"G, 6, P, K, 17, 40\",\"ItemDescriptionHowtoRead\":\"CORREA MICRO V\",\"ItemInventory\":\"395.00\",\"BrandId\":\"GATES\",\"BrandName\":\"GATES\",\"ItemImage\":\"\",\"UnitId\":0,\"UnitDescription\":\"UNIDADES\",\"ItemOrigin\":\"BRASIL\",\"ItemQuantity\":\"1.00\",\"ItemStatusId\":\"F\",\"ItemStatusDescription\":\"\",\"ItemLastOperatorId\":0,\"ItemLastOperatorName\":\"\",\"ItemXCoord\":\"\",\"ItemYCoord\":\"\",\"ItemZCoord\":\"\",\"ItemSupplierId\":500094,\"ItemSupplierName\":\"GATES BRASIL\"}],\"Error\":\"\",\"ErrorMessage\":\"\"}";
				}else{
					mJson = "{\"Lines\":[{\"HeaderId\":\"27876\",\"LineId\":3,\"ItemId\":\"CU3AXS039\",\"ItemDescription\":\"CORREA GATES 13X1041\",\"ItemHowToRead\":\"A, X, S, 0, 39\",\"ItemDescriptionHowtoRead\":\"CORREA INDUSTRIAL\",\"ItemInventory\":\"913.00\",\"BrandId\":\"GATES\",\"BrandName\":\"GATES\",\"ItemImage\":\"\",\"UnitId\":0,\"UnitDescription\":\"UNIDADES\",\"ItemOrigin\":\"BRASIL\",\"ItemQuantity\":\"4.00\",\"ItemStatusId\":\"F\",\"ItemStatusDescription\":\"\",\"ItemLastOperatorId\":0,\"ItemLastOperatorName\":\"\",\"ItemXCoord\":\"\",\"ItemYCoord\":\"\",\"ItemZCoord\":\"\",\"ItemSupplierId\":500094,\"ItemSupplierName\":\"GATES BRASIL\"},{\"HeaderId\":\"27876\",\"LineId\":4,\"ItemId\":\"CU3AXS040\",\"ItemDescription\":\"CORREA GATES 13X1067\",\"ItemHowToRead\":\"A, X, S, 0, 40\",\"ItemDescriptionHowtoRead\":\"CORREA INDUSTRIAL\",\"ItemInventory\":\"451.00\",\"BrandId\":\"GATES\",\"BrandName\":\"GATES\",\"ItemImage\":\"\",\"UnitId\":0,\"UnitDescription\":\"UNIDADES\",\"ItemOrigin\":\"BRASIL\",\"ItemQuantity\":\"2.00\",\"ItemStatusId\":\"F\",\"ItemStatusDescription\":\"\",\"ItemLastOperatorId\":0,\"ItemLastOperatorName\":\"\",\"ItemXCoord\":\"\",\"ItemYCoord\":\"\",\"ItemZCoord\":\"\",\"ItemSupplierId\":500094,\"ItemSupplierName\":\"GATES BRASIL\"},{\"HeaderId\":\"27876\",\"LineId\":8,\"ItemId\":\"CU3AXS058\",\"ItemDescription\":\"CORREA GATES 13X1524\",\"ItemHowToRead\":\"A, X, S, 0, 58\",\"ItemDescriptionHowtoRead\":\"CORREA INDUSTRIAL\",\"ItemInventory\":\"216.00\",\"BrandId\":\"GATES\",\"BrandName\":\"GATES\",\"ItemImage\":\"\",\"UnitId\":0,\"UnitDescription\":\"UNIDADES\",\"ItemOrigin\":\"BRASIL\",\"ItemQuantity\":\"2.00\",\"ItemStatusId\":\"F\",\"ItemStatusDescription\":\"\",\"ItemLastOperatorId\":0,\"ItemLastOperatorName\":\"\",\"ItemXCoord\":\"\",\"ItemYCoord\":\"\",\"ItemZCoord\":\"\",\"ItemSupplierId\":500094,\"ItemSupplierName\":\"GATES BRASIL\"},{\"HeaderId\":\"27876\",\"LineId\":9,\"ItemId\":\"CU3AXS059\",\"ItemDescription\":\"CORREA GATES 13X1549\",\"ItemHowToRead\":\"A, X, S, 0, 59\",\"ItemDescriptionHowtoRead\":\"CORREA INDUSTRIAL\",\"ItemInventory\":\"266.00\",\"BrandId\":\"GATES\",\"BrandName\":\"GATES\",\"ItemImage\":\"\",\"UnitId\":0,\"UnitDescription\":\"UNIDADES\",\"ItemOrigin\":\"BRASIL\",\"ItemQuantity\":\"2.00\",\"ItemStatusId\":\"F\",\"ItemStatusDescription\":\"\",\"ItemLastOperatorId\":0,\"ItemLastOperatorName\":\"\",\"ItemXCoord\":\"\",\"ItemYCoord\":\"\",\"ItemZCoord\":\"\",\"ItemSupplierId\":500094,\"ItemSupplierName\":\"GATES BRASIL\"},{\"HeaderId\":\"27876\",\"LineId\":1,\"ItemId\":\"CU3G6PK1740\",\"ItemDescription\":\"CORR M-V K060685 VW GOL SANTANA 95 ALT DH PG206 1.9DFULL\",\"ItemHowToRead\":\"G, 6, P, K, 17, 40\",\"ItemDescriptionHowtoRead\":\"CORREA MICRO V\",\"ItemInventory\":\"395.00\",\"BrandId\":\"GATES\",\"BrandName\":\"GATES\",\"ItemImage\":\"\",\"UnitId\":0,\"UnitDescription\":\"UNIDADES\",\"ItemOrigin\":\"BRASIL\",\"ItemQuantity\":\"1.00\",\"ItemStatusId\":\"F\",\"ItemStatusDescription\":\"\",\"ItemLastOperatorId\":0,\"ItemLastOperatorName\":\"\",\"ItemXCoord\":\"\",\"ItemYCoord\":\"\",\"ItemZCoord\":\"\",\"ItemSupplierId\":500094,\"ItemSupplierName\":\"GATES BRASIL\"}],\"Error\":\"\",\"ErrorMessage\":\"\"}";
				}

				List<Item> tempItemsList = mJsonToItem.getItemsFromJson(mJson);

				((ItemsActivity) mActivity).populateItems(tempItemsList);

			*/
        }
        return mLocations;
    }

}
