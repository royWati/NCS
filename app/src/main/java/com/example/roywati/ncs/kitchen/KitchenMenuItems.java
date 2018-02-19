package com.example.roywati.ncs.kitchen;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.defaults.NoDataException;
import com.example.roywati.ncs.waiter.AppConfig;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class KitchenMenuItems extends AppCompatActivity {
    ProgressBar bar;
    GridView gridView;
    RelativeLayout rela;

    public class ViewOrderStatusType extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        JSONArray order_status;
        String serverMessage = "request not sent";
        int successState = 0;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... strings) {

            try {
                JSONParser jsonParser = new JSONParser();
                List<NameValuePair> jsonObjectData = new ArrayList();
                jsonObjectData.add(new BasicNameValuePair("homepageId", AppConfigKitchen.homepageId));
                jsonObjectData.add(new BasicNameValuePair("branchId", AppConfig.branchId));
                Log.d("kitchen homepage id", AppConfigKitchen.homepageId);
                JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfigKitchen.get_process, HttpGet.METHOD_NAME, jsonObjectData);
                Log.d("data sub category", jsonObjectResponse.toString());

                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
                this.order_status = jsonObjectResponse.getJSONArray("order_status");
                AppConfigKitchen.orders = new String[this.order_status.length()];
                AppConfigKitchen.tables = new String[this.order_status.length()];
                for (int i = 0; i < this.order_status.length(); i++) {
                    JSONObject jsonObject = this.order_status.getJSONObject(i);
                    Log.d("status y", jsonObject.toString());
                    AppConfigKitchen.orders[i] = jsonObject.getString("order_id");
                    AppConfigKitchen.tables[i] = jsonObject.getString("table_name");
                    Log.d("process statement order", AppConfigKitchen.orders[i]);
                    Log.d("process statement table", AppConfigKitchen.tables[i]);
                    if (success == 1) {
                        this.successState = 1;
                    }
                }
            } catch (Exception e) {
               successState=500;

            }
            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("successState", String.valueOf(this.successState));
            if (this.successState == 1) {
                AppConfigKitchen.num = Integer.parseInt(AppConfigKitchen.homepageId);
                if (AppConfigKitchen.num == 5) {
                    KitchenMenuItems.this.gridView.setAdapter(new PendingAdapter(KitchenMenuItems.this, AppConfigKitchen.orders, AppConfigKitchen.tables));
                    KitchenMenuItems.this.gridView.setOnItemClickListener(new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            TextView txt = (TextView) view.findViewById(R.id.order_item_no_pending);
                            AppConfigKitchen.titleProcess = "pending items";
                            AppConfigKitchen.selectedKitchenOrder = txt.getText().toString();
                            Log.d("selected order item: ", AppConfigKitchen.selectedKitchenOrder);
                            KitchenMenuItems.this.startActivity(new Intent(KitchenMenuItems.this, MenuItemState.class));
                        }
                    });
                } else if (AppConfigKitchen.num == 6) {
                    KitchenMenuItems.this.gridView.setAdapter(new ProcessingAdapter(KitchenMenuItems.this, AppConfigKitchen.orders, AppConfigKitchen.tables));
                    KitchenMenuItems.this.gridView.setOnItemClickListener(new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            TextView txt = (TextView) view.findViewById(R.id.order_item_id);
                            AppConfigKitchen.titleProcess = "Processing items";
                            AppConfigKitchen.selectedKitchenOrder = txt.getText().toString();
                            Log.d("selected order item: ", AppConfigKitchen.selectedKitchenOrder);
                            KitchenMenuItems.this.startActivity(new Intent(KitchenMenuItems.this, MenuItemState.class));
                        }
                    });
                } else if (AppConfigKitchen.num == 7) {
                    KitchenMenuItems.this.gridView.setAdapter(new ProcessedAdapter(KitchenMenuItems.this, AppConfigKitchen.orders, AppConfigKitchen.tables));
                    KitchenMenuItems.this.gridView.setOnItemClickListener(new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            TextView txt = (TextView) view.findViewById(R.id.order_item);
                            AppConfigKitchen.titleProcess = "Processed items";
                            AppConfigKitchen.selectedKitchenOrder = txt.getText().toString();
                            Log.d("selected order item: ", AppConfigKitchen.selectedKitchenOrder);
                            KitchenMenuItems.this.startActivity(new Intent(KitchenMenuItems.this, MenuItemState.class));
                        }
                    });
                }
             ///   Toast.makeText(KitchenMenuItems.this.getApplicationContext(), this.serverMessage, 1).show();
                return;
            }else if(successState==500){
                Toast.makeText(getApplicationContext(), "Network Error!!", 1).show();
            }
            Toast.makeText(KitchenMenuItems.this.getApplicationContext(), this.serverMessage, 1).show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.kitchen_homepage);
        setTitle(AppConfigKitchen.titleProcess);
        this.gridView = (GridView) findViewById(R.id.homepage_kitchen_gridview);
        new ViewOrderStatusType().execute(new String[0]);
        this.bar = (ProgressBar) findViewById(R.id.progressBar);
        this.rela = (RelativeLayout) findViewById(R.id.relativeLayout);


    }

    public void showProgress(boolean state) {
        if (state) {
            this.bar.setVisibility(0);
            this.bar.setVisibility(0);
            this.bar.setVisibility(0);
        }
    }
}
