package com.tsmrachel.cdcslots;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MyInstanceIDListenerService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS

    public static final String ACTION_SENDREGISTRATIONTOSERVER = "com.tsmrachel.cdcslots.action.SENDREGISTRATIONTOSERVER";

    public static Boolean SENT_TOKEN_TO_SERVER = false;

    public MyInstanceIDListenerService() {
        super("MyInstanceIDListenerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Initially this call goes out to the network to retrieve the token, subsequent calls
        // are local.
        InstanceID instanceID = InstanceID.getInstance(this);
        String token = null;
        try {

            token = instanceID.getToken("134561701358",
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("cdcSlots", "GCM Registration Token: " + token);

        SendRegistrationToServer(token);

        // You should store a boolean that indicates whether the generated token has been
        // sent to your server. If the boolean is false, send the token to your server,
        // otherwise your server should have already received the token.
        //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply()
    }

    /**
     * Handle action SendRegistrationToServer in the provided background thread with the provided
     * parameters.
     */
    private void SendRegistrationToServer(String token) {
        // TODO: Handle action SendRegistrationToServer

        /**
         * perform RESTful webservice invocations
         */

        // Show Progress Dialog
        //prgDialog.show();

        // Make RESTful webservice call using AsyncHttpClient object
        Log.d("cdcSlots", "In SendRegistrationToServer");

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams param = new RequestParams("token", token);

        client.post("http://192.168.56.1:3000/registerToken", param, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // Hide Progress Dialog
//                prgDialog.hide();
                try {

                    Log.d("cdcSlots", "in OnSuccess");
                    SENT_TOKEN_TO_SERVER = true;
                    Log.d("cdcSlots", "SENT_TOKEN_TO_SERVER : " + SENT_TOKEN_TO_SERVER);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                // Hide Progress Dialog
                //prgDialog.hide();

                Log.d("cdcSlots", "In OnFailure");
            }

            protected String parseResponse(byte[] responseBody) throws JSONException {

                Log.d("cdcSlots", "in parseResponse");
                return "not implemented";

            }

        });
    }

}


