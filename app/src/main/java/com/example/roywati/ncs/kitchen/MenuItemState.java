package com.example.roywati.ncs.kitchen;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.waiter.AppConfig;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class MenuItemState extends Activity {
    ProgressBar bar;
    FloatingActionButton fabBtn;
    ListView listView;

    public class changeOrderState extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        JSONArray kitchenOrder;
        String serverMessage = "request not sent";
        int successState = 0;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser();
            List<NameValuePair> jsonObjectData = new ArrayList();
            jsonObjectData.add(new BasicNameValuePair("orderId", AppConfigKitchen.selectedKitchenOrder));
            jsonObjectData.add(new BasicNameValuePair("homepageId", AppConfigKitchen.homepageId));
            JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfigKitchen.change_order_state, HttpGet.METHOD_NAME, jsonObjectData);
            Log.d("data", jsonObjectResponse.toString());
            Log.d("home page id", AppConfigKitchen.homepageId);
            Log.d("data", AppConfig.protocal + AppConfig.hostname + AppConfigKitchen.change_order_state);
            try {
                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
                if (success == 1) {
                    this.successState = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (this.successState == 1) {
                MenuItemState.this.startActivity(new Intent(MenuItemState.this, KitchenHomePage.class));
                Toast.makeText(MenuItemState.this.getApplicationContext(), this.serverMessage, 1).show();
                return;
            }
            Toast.makeText(MenuItemState.this.getApplicationContext(), this.serverMessage, 1).show();
        }
    }

    public class getItemState extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        JSONArray kitchenOrder;
        String serverMessage = "request not sent";
        int successState = 0;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser();
            List<NameValuePair> jsonObjectData = new ArrayList();
            jsonObjectData.add(new BasicNameValuePair("orderId", AppConfigKitchen.selectedKitchenOrder));
            JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfigKitchen.view_selected_order, HttpGet.METHOD_NAME, jsonObjectData);
            Log.d("data", AppConfig.protocal + AppConfig.hostname + AppConfigKitchen.view_selected_order);
            Log.d("data", jsonObjectResponse.toString());
            try {
                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
                this.kitchenOrder = jsonObjectResponse.getJSONArray("kitchenOrder");
                Log.d("data", AppConfig.orderId);
                AppConfigKitchen.orderItem_id = new String[this.kitchenOrder.length()];
                AppConfigKitchen.menuItemId = new String[this.kitchenOrder.length()];
                AppConfigKitchen.menuitemname = new String[this.kitchenOrder.length()];
                AppConfigKitchen.quantity = new String[this.kitchenOrder.length()];
                for (int i = 0; i < this.kitchenOrder.length(); i++) {
                    JSONObject jsonObject = this.kitchenOrder.getJSONObject(i);
                    Log.d("menu category", jsonObject.toString());
                    AppConfigKitchen.orderItem_id[i] = jsonObject.getString("order_items_id");
                    AppConfigKitchen.menuItemId[i] = jsonObject.getString("menu_item_id");
                    AppConfigKitchen.menuitemname[i] = jsonObject.getString("menu_item_name");
                    AppConfigKitchen.quantity[i] = jsonObject.getString("quantity");
                    Log.d("kit-order id", AppConfigKitchen.orderItem_id[i]);
                    Log.d("kit-menu id", AppConfigKitchen.menuItemId[i]);
                    Log.d("kit-menu name", AppConfigKitchen.menuitemname[i]);
                    Log.d("kit-item quantity", AppConfigKitchen.quantity[i]);
                }
                if (success == 1) {
                    this.successState = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (this.successState == 1) {
                MenuItemState.this.listView.setAdapter(new MenuItemSelectedAdapter(MenuItemState.this, AppConfigKitchen.menuitemname, AppConfigKitchen.quantity));
            } else {
                Toast.makeText(MenuItemState.this.getApplicationContext(), this.serverMessage, 1).show();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kitchen_order_item);
        this.listView = (ListView) findViewById(R.id.list_kitchen_order);
        this.fabBtn = (FloatingActionButton) findViewById(R.id.view_cart_menu_state);
        this.bar = (ProgressBar) findViewById(R.id.progressBar);
        changeFabImg();
        new getItemState().execute(new String[0]);
        this.fabBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new changeOrderState().execute(new String[0]);
            }
        });
    }

    public void changeFabImg() {
        if (AppConfigKitchen.num == 5) {
            this.fabBtn.setImageResource(R.drawable.pending);
        }
        if (AppConfigKitchen.num == 6) {
            this.fabBtn.setImageResource(R.drawable.processing);
        }
        if (AppConfigKitchen.num == 7) {
            this.fabBtn.setImageResource(R.drawable.processed);
        }
    }
}
