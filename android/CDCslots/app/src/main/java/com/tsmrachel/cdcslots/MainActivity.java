package com.tsmrachel.cdcslots;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SyncAdapterType;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;



import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d("cdcSlots", "In OnCreate");
//        invokeWS();
        Intent intent = new Intent(this, MyInstanceIDListenerService.class);
        startService(intent);

        invokeWS();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        else if (id == R.id.action_refresh) {
            Log.d("cdcSlots", "action_refresh called");
            invokeWS();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void renderList(SparseArray groups) {

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandableListView);
        ExpandableListAdapter adapter = new ExpandableListAdapter(this, groups);
        listView.setAdapter(adapter);
    }


    /**
     * Method that performs RESTful webservice invocations
     */
    public void invokeWS(){


        // Show Progress Dialog
//        prgDialog.show();

        // Make RESTful webservice call using AsyncHttpClient object
        Log.d("cdcSlots", "In invokeWS");

        AsyncHttpClient client = new AsyncHttpClient();

        client.setConnectTimeout(120000);

        ProgressBar spinner = (ProgressBar) findViewById(R.id.progressBar);

        spinner.setVisibility(View.VISIBLE);

        client.get("http://192.168.56.1:3000/getSlots", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                try {
                    // JSON Object


                    Log.d("cdcSlots", "in OnSuccess");

                    SparseArray<Group> groups = parseResponse(response);

                    MainActivity.this.renderList(groups);

                    ((TextView) findViewById(R.id.textView)).setText("Last Updated : " + android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()));

                    ProgressBar spinner = (ProgressBar) findViewById(R.id.progressBar);

                    spinner.setVisibility(View.GONE);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                Log.d("cdcSlots", "In OnFailure");

                ProgressBar spinner = (ProgressBar) findViewById(R.id.progressBar);

                spinner.setVisibility(View.GONE);

                openAlertDialog();


            }

            protected SparseArray parseResponse(byte[] responseBody) throws JSONException {

                Log.d("cdcSlots", "in parseResponse");

                SparseArray<Group> groups = new SparseArray<Group>();


                int sparseArrayCount = -1;

                try {
                    // JSON Object

                    String decoded = new String(responseBody, "UTF-8");

                    JSONArray jsonData = new JSONArray(decoded);

                    JSONArray tempArray;

                    int jsonDataLength = jsonData.length();

                    for (int j = 0; j < jsonDataLength; j++) {

                        tempArray = jsonData.getJSONArray(j);
                        int tempArrayLength = tempArray.length();

                        if (tempArrayLength == 0) {
                            continue;
                        } else {
                            sparseArrayCount += 1;
                        }


                        Group group = new Group(tempArray.get(0).toString() + ' ' + tempArray.get(1).toString());

                        Log.d("cdcSlots", "group" + j + " : " + tempArray.get(0).toString() + ' ' + tempArray.get(1).toString());

                        for (int i = 2; i < tempArrayLength; i++) {
                            if (!(tempArray.get(i).toString() == "null")) {
                                Log.d("cdcSlots", "tempArray" + i + " : " + tempArray.get(i));
                                group.children.add(tempArray.get(i).toString());
                            }
                        }

//                        cause appending by j, if there is a null in one, it will append null
                        groups.append(sparseArrayCount, group);

                        Log.d("cdcSlots", "groups : " + groups);


                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }

                return groups;

            }

        });

    }

    public void openAlertDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this); //context
        builder1.setTitle("Error");
        builder1.setMessage("Something seems to have gone wrong, please try again!");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}

