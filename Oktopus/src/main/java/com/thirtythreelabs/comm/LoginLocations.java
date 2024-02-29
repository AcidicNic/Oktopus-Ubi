package com.thirtythreelabs.comm;


        import java.net.URI;

        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.entity.StringEntity;
        import org.json.JSONObject;

        import android.app.Activity;
        import android.content.Context;
        import android.widget.Toast;
        
        import com.thirtythreelabs.util.Config;
        import com.thirtythreelabs.util.WriteLog;

public class LoginLocations {
    private Activity mActivity;
    private Context mContext;
    private String mLang;
    private boolean mOnline;

    private JsonToOperator mJsonToOperator;

    private WriteLog mLog = new WriteLog();

    public static final String GET_LOGINLOCATIONS_ACTION = "com.thirtythreelabs.oktopus.GET_LOGINLOCATIONS_ACTION";

    private String GET_LOGINLOCATIONS_URI;



    public LoginLocations (Context tempContext, Activity tempActivity, String tempLang, boolean tempOnline){
        mActivity = tempActivity;
        mContext = tempContext;
        mLang = tempLang;
        mOnline = tempOnline;

        mJsonToOperator = new JsonToOperator(tempActivity);
    }



    public void loginLocationsOperator(int pCompanyId, String pOperatorLogin) {
        //Create the search request

        String mJson;
        GET_LOGINLOCATIONS_URI = Config.URL + "loginlocations";

        if(mOnline){
            try{
                String url = String.format(GET_LOGINLOCATIONS_URI);
                HttpPost postRequest = new HttpPost(new URI(url));

                JSONObject object = new JSONObject();
                try {
                    object.put("CompanyId", pCompanyId);
                    object.put("OperatorLogin", pOperatorLogin);

                    mLog.appendLog(object.toString());
                } catch (Exception ex) {
                    // setToast("Error: " + ex);
                }

                mJson = object.toString();
                StringEntity entity = new StringEntity(mJson);

                // log and toast url and mJson
                mLog.appendLog("url: " + url);
                mLog.appendLog("mJson: " + mJson);


                postRequest.setEntity(entity);
                postRequest.setHeader("Content-Type", "application/json");

                RestTask task = new RestTask(mContext, GET_LOGINLOCATIONS_ACTION);

                task.execute(postRequest);

            } catch (Exception e) {
                 e.printStackTrace();
                 Toast.makeText(mContext, "Error #1: " + e, Toast.LENGTH_LONG).show();
            }

        } else{


        }
    }

}
