package com.example.roywati.ncs.waiter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
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

public class AddOns extends AppCompatActivity {
    Button btn;
    GridView gridView;

    public class addItem extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        JSONArray add_ons_orders;
        String serverMessage = "request not sent";
        int successState = 0;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... strings) {

            try {
                JSONParser jsonParser = new JSONParser();
                List<NameValuePair> jsonObjectData = new ArrayList();
                jsonObjectData.add(new BasicNameValuePair("userId", AppConfig.userId));
                JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfig.add_ons, HttpGet.METHOD_NAME, jsonObjectData);
                Log.d("data", jsonObjectResponse.toString());

                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
                this.add_ons_orders = jsonObjectResponse.getJSONArray("orders");
                Log.d("data", AppConfig.orderId);
                AppConfig.tables = new String[this.add_ons_orders.length()];
                AppConfig.orders = new String[this.add_ons_orders.length()];
                AppConfig.addOn_tables = new String[this.add_ons_orders.length()];
                for (int i = 0; i < this.add_ons_orders.length(); i++) {
                    JSONObject jsonObject = this.add_ons_orders.getJSONObject(i);
                    Log.d("menu sub category", jsonObject.toString());
                    AppConfig.tables[i] = jsonObject.getString("order_id");
                    AppConfig.orders[i] = jsonObject.getString("order_status_id");
                    AppConfig.addOn_tables[i] = jsonObject.getString("table_name");
                    Log.d("sub menu name", AppConfig.tables[i]);
                    Log.d("sub menu name", AppConfig.orders[i]);
                    Log.d("sub menu name", AppConfig.addOn_tables[i]);
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
                AddOns.this.gridView.setAdapter(new AddOnsAdapter(AddOns.this, AppConfig.orders, AppConfig.tables, AppConfig.addOn_tables));
                AddOns.this.gridView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        AppConfig.orderId = ((TextView) view.findViewById(R.id.order_no_selected)).getText().toString();
                        AppConfig.add_on_event = true;
                        AddOns.this.startActivity(new Intent(AddOns.this, AddOnMenuCategory.class));

                    }
                });
                Log.d("new order", AppConfig.orderId);
                return;
            }else if(successState==500){
                Toast.makeText(getApplicationContext(), "Network Error!!", 1).show();
            }

        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.kitchen_homepage);
        this.btn = (Button) findViewById(R.id.logout_kitchen);
        this.gridView = (GridView) findViewById(R.id.homepage_kitchen_gridview);
        this.btn.setVisibility(8);
        new addItem().execute(new String[0]);
    }
}
