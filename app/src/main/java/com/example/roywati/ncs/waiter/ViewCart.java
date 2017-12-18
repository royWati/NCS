package com.example.roywati.ncs.waiter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.defaults.PrintActivity;
import com.example.roywati.ncs.defaults.PrintData;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class ViewCart extends AppCompatActivity {
    public static TextView holder;
    public static ListView listView;
    public static TextView order;
    public static ProgressBar prog_v;
    public static TextView txtAmount;
    Button checkout_cart;
    Button clearTable;
    Spinner spinner;

    public class checkoutCart extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        String serverMessage = "request not sent";
        int successState = 0;
        String total;

        protected void onPreExecute() {
            super.onPreExecute();
            ViewCart.this.showProgressBar(true);
        }

        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser();
            List<NameValuePair> jsonObjectData = new ArrayList();
            jsonObjectData.add(new BasicNameValuePair("orderId", AppConfig.orderId));
            jsonObjectData.add(new BasicNameValuePair("tableId", AppConfig.tableId_selected));
            jsonObjectData.add(new BasicNameValuePair("userId", AppConfig.userId));
            JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfig.checkout_cart, HttpGet.METHOD_NAME, jsonObjectData);
            Log.d("data sub category", jsonObjectResponse.toString());
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
            Log.d("successState", String.valueOf(this.successState));
            Log.d("successState", AppConfig.total_value_in_cart);
            if (this.successState == 1) {
                PrintData.print_data_id = 1;
                AppConfig.checkout_status = 1;
                AppConfig.clear_a_table_key = false;
                ViewCart.this.startActivity(new Intent(ViewCart.this, PrintActivity.class));
                Toast.makeText(ViewCart.this.getApplicationContext(), this.serverMessage, 1).show();
            } else {
                Toast.makeText(ViewCart.this.getApplicationContext(), this.serverMessage, 1).show();
            }
            ViewCart.this.showProgressBar(false);
        }
    }

    public class view_Cart extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        JSONArray Tables;
        JSONArray cartItems;
        String serverMessage = "request not sent";
        int successState = 0;
        String total;

        protected void onPreExecute() {
            super.onPreExecute();
            ViewCart.this.showProgressBar(true);
        }

        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser();
            List<NameValuePair> jsonObjectData = new ArrayList();
            jsonObjectData.add(new BasicNameValuePair("orderId", AppConfig.orderId));
            jsonObjectData.add(new BasicNameValuePair("branchId", AppConfig.branchId));
            Log.d("menuSubCatId", AppConfig.orderId);
            JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfig.view_cart, HttpGet.METHOD_NAME, jsonObjectData);
            Log.d("data sub category", jsonObjectResponse.toString());
            try {
                int i;
                JSONObject jsonObject;
                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
                AppConfig.total_value_in_cart = jsonObjectResponse.getString("total");
                Log.d("total value of items", AppConfig.total_value_in_cart);
                this.cartItems = jsonObjectResponse.getJSONArray("cart_items");
                this.Tables = jsonObjectResponse.getJSONArray("tables");
                Log.d("data", AppConfig.orderId);
                AppConfig.viewCartItemId = new String[this.cartItems.length()];
                AppConfig.viewCartItemName = new String[this.cartItems.length()];
                AppConfig.viewCartMenuItemId = new String[this.cartItems.length()];
                AppConfig.viewCartItemPrice = new String[this.cartItems.length()];
                AppConfig.tableId = new String[this.Tables.length()];
                AppConfig.tableName = new String[this.Tables.length()];
                for (i = 0; i < this.cartItems.length(); i++) {
                    jsonObject = this.cartItems.getJSONObject(i);
                    Log.d("menu sub category", jsonObject.toString());
                    AppConfig.viewCartItemId[i] = jsonObject.getString("order_items_id");
                    AppConfig.viewCartItemName[i] = jsonObject.getString("menu_item_name");
                    AppConfig.viewCartMenuItemId[i] = jsonObject.getString("menu_item_id");
                    AppConfig.viewCartItemPrice[i] = jsonObject.getString("price");
                    Log.d("view cart id", AppConfig.viewCartItemId[i]);
                    Log.d("view cart name", AppConfig.viewCartItemName[i]);
                    Log.d("menu item id", AppConfig.viewCartMenuItemId[i]);
                    Log.d("price", AppConfig.viewCartItemPrice[i]);
                }
                for (i = 0; i < this.Tables.length(); i++) {
                    jsonObject = this.Tables.getJSONObject(i);
                    Log.d("tables ", jsonObject.toString());
                    AppConfig.tableId[i] = jsonObject.getString("table_id");
                    AppConfig.tableName[i] = jsonObject.getString("table_name");
                    Log.d("table id", AppConfig.tableId[i]);
                    Log.d("table name", AppConfig.tableName[i]);
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
            Log.d("successState", String.valueOf(this.successState));
            Log.d("total amount", AppConfig.total_value_in_cart);
            ViewCart.txtAmount.setText(AppConfig.total_value_in_cart);
            if (this.successState == 1) {
                ViewCart.listView.setAdapter(new ViewCartAdapter(ViewCart.this, AppConfig.viewCartItemName, AppConfig.viewCartItemId, AppConfig.viewCartItemPrice));
                ViewCart.this.spinner.setAdapter(new ArrayAdapter(ViewCart.this, 17367049, AppConfig.tableName));
                ViewCart.this.spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        AppConfig.tableId_selected = AppConfig.tableId[(int) adapterView.getItemIdAtPosition(i)];
                        Toast.makeText(ViewCart.this.getApplicationContext(), "table selected:" + AppConfig.tableId_selected, 0).show();
                    }

                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                ViewCart.this.checkout_cart.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (AppConfig.checkout_status == 1) {
                            ViewCart.this.startActivity(new Intent(ViewCart.this, PrintActivity.class));
                        } else if (AppConfig.checkout_status == 0) {
                            new checkoutCart().execute(new String[0]);
                        }
                    }
                });
                Toast.makeText(ViewCart.this.getApplicationContext(), this.serverMessage, 1).show();
            } else {
                Toast.makeText(ViewCart.this.getApplicationContext(), this.serverMessage, 1).show();
            }
            ViewCart.this.showProgressBar(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("view cart Order No." + AppConfig.orderId);
        setContentView((int) R.layout.view_cart_design);
        listView = (ListView) findViewById(R.id.list_view_cart);
        this.checkout_cart = (Button) findViewById(R.id.btnCheckout);
        txtAmount = (TextView) findViewById(R.id.TOTAL_amount);
        holder = (TextView) findViewById(R.id.holder);
        this.clearTable = (Button) findViewById(R.id.clear_a_table);
        prog_v = (ProgressBar) findViewById(R.id.progressBar_view_cart);
        this.spinner = (Spinner) findViewById(R.id.spinner_view_cart);
        this.clearTable.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AppConfig.clear_a_table_key = true;
                ViewCart.this.startActivity(new Intent(ViewCart.this, ClearTable.class));
            }
        });
        new view_Cart().execute(new String[0]);
    }

    public void showProgressBar(boolean state) {
        if (state) {
            prog_v.setVisibility(0);
            listView.setVisibility(8);
            txtAmount.setVisibility(8);
            holder.setVisibility(8);
            this.checkout_cart.setVisibility(8);
            this.clearTable.setVisibility(8);
            return;
        }
        prog_v.setVisibility(8);
        listView.setVisibility(0);
        txtAmount.setVisibility(0);
        holder.setVisibility(0);
        this.clearTable.setVisibility(0);
        this.checkout_cart.setVisibility(0);
    }
}
