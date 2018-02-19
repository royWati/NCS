package com.example.roywati.ncs.waiter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.defaults.NoDataException;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class ViewOrders extends AppCompatActivity {
    ListView listView;
    ContentLoadingProgressBar progressBar;

    public class viewOrderItems extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        String serverMessage = "request not sent";
        int successState = 0;
        JSONArray waiterOrders;

        protected void onPreExecute() {
            super.onPreExecute();
            ViewOrders.this.showProgress(true);
        }

        protected String doInBackground(String... strings) {

            try {
                JSONParser jsonParser = new JSONParser();
                List<NameValuePair> jsonObjectData = new ArrayList();
                jsonObjectData.add(new BasicNameValuePair("userId", AppConfig.userId));
                JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfig.view_waiter_orders, HttpGet.METHOD_NAME, jsonObjectData);
                Log.d("data", jsonObjectResponse.toString());

                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
                this.waiterOrders = jsonObjectResponse.getJSONArray("waiter_orders");
                Log.d("data", AppConfig.orderId);
                AppConfig.ordersids = new String[this.waiterOrders.length()];
                AppConfig.tablesnames = new String[this.waiterOrders.length()];
                AppConfig.statustypes = new String[this.waiterOrders.length()];
                AppConfig.checkoutTimes = new String[this.waiterOrders.length()];
                for (int i = 0; i < this.waiterOrders.length(); i++) {
                    JSONObject jsonObject = this.waiterOrders.getJSONObject(i);
                    Log.d("menu category", jsonObject.toString());
                    AppConfig.ordersids[i] = jsonObject.getString("order_id");
                    AppConfig.tablesnames[i] = jsonObject.getString("table_name");
                    AppConfig.statustypes[i] = jsonObject.getString("order_status_name");
                    AppConfig.checkoutTimes[i] = jsonObject.getString("Checkout");
                    Log.d("view order id name", AppConfig.ordersids[i]);
                    Log.d("view order table name", AppConfig.tablesnames[i]);
                    Log.d("view order status ", AppConfig.statustypes[i]);
                    Log.d("view order check", AppConfig.checkoutTimes[i]);
                }
                if (success == 1) {
                    this.successState = 1;
                }
            } catch (Exception e) {
                successState=500;
            }
            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (this.successState == 1) {
                ViewOrders.this.listView.setAdapter(
                        new viewOrderAdapter(ViewOrders.this, AppConfig.ordersids, AppConfig.tablesnames,
                                AppConfig.statustypes, AppConfig.checkoutTimes));
            }else if (successState==500){
                Toast.makeText(ViewOrders.this.getApplicationContext(), "Network error!!",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(ViewOrders.this.getApplicationContext(), this.serverMessage, 1).show();
            }
            ViewOrders.this.showProgress(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.view_waiter_order);
        this.listView = (ListView) findViewById(R.id.viewOrder);
        this.progressBar = (ContentLoadingProgressBar) findViewById(R.id.progr);
        new viewOrderItems().execute(new String[0]);
    }

    public void showProgress(boolean state) {
        if (state) {
            this.progressBar.setVisibility(0);
            this.listView.setVisibility(8);
            return;
        }
        this.progressBar.setVisibility(8);
        this.listView.setVisibility(0);
    }
}
