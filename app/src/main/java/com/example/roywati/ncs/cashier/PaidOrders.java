package com.example.roywati.ncs.cashier;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.defaults.NoDataException;
import com.example.roywati.ncs.defaults.PrintActivity;
import com.example.roywati.ncs.waiter.AppConfig;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class PaidOrders extends AppCompatActivity {
    ProgressBar bar;
    ListView listView;
    TextView textView;

    public class getPaidItem extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        String serverMessage = "request not sent";
        int successState = 0;
        JSONArray unpaidOrder;

        protected void onPreExecute() {
            super.onPreExecute();
            PaidOrders.this.showProgress(true);
        }

        protected String doInBackground(String... strings) {

            try {
                JSONParser jsonParser = new JSONParser();
                List<NameValuePair> jsonObjectData = new ArrayList();
                jsonObjectData.add(new BasicNameValuePair("branchId", AppConfig.branchId));
                JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname +
                        AppConfigCashier.get_paid_orders, HttpGet.METHOD_NAME, jsonObjectData);
                Log.d("data", jsonObjectResponse.toString());

                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
                AppConfigCashier.total = jsonObjectResponse.getString("total");
                this.unpaidOrder = jsonObjectResponse.getJSONArray("unpaid_orders");
                Log.d("data", AppConfig.orderId);
                AppConfigCashier.order_id = new String[this.unpaidOrder.length()];
                AppConfigCashier.price = new String[this.unpaidOrder.length()];
                AppConfigCashier.order_discount_amount = new String[this.unpaidOrder.length()];
                AppConfigCashier.order_discount_percentage = new String[this.unpaidOrder.length()];

                for (int i = 0; i < this.unpaidOrder.length(); i++) {
                    JSONObject jsonObject = this.unpaidOrder.getJSONObject(i);
                    Log.d("menu category", jsonObject.toString());
                    AppConfigCashier.order_id[i] = jsonObject.getString("order_id");
                    AppConfigCashier.price[i] = jsonObject.getString("sum");

                    AppConfigCashier.order_discount_percentage[i] = jsonObject.getString("discount_percentage");
                    AppConfigCashier.order_discount_amount[i] = jsonObject.getString("discount_amount");

                    Log.d("kit-menu id", AppConfigCashier.order_id[i]);
                    Log.d("kit-menu name", AppConfigCashier.price[i]);
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
                PaidOrders.this.textView.setText(AppConfigCashier.total);
                PaidOrders.this.listView.setAdapter(new PaidOrdersAdapter(PaidOrders.this, AppConfigCashier.order_id, AppConfigCashier.price));
                PaidOrders.this.listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        int pos= (int) adapterView.getItemIdAtPosition(i);

                        AppConfigCashier.order_discount_am=AppConfigCashier.order_discount_amount[pos];
                        AppConfigCashier.order_discount_perce=AppConfigCashier.order_discount_percentage[pos];

                        AppConfigCashier.orderNumber = ((TextView) view.findViewById(R.id.order_num_cash)).getText().toString();
                        PaidOrders.this.startActivity(new Intent(PaidOrders.this, PrintActivity.class));
                    }
                });
            }else if(successState==500){
                Toast.makeText(getApplicationContext(), "Network Error!!", 1).show();
            } else {
                Toast.makeText(PaidOrders.this.getApplicationContext(), this.serverMessage, 1).show();
            }
            PaidOrders.this.showProgress(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.cashier_view_unpaid);
        this.listView = (ListView) findViewById(R.id.list_unpaid_orders);
        this.listView.deferNotifyDataSetChanged();
        this.bar = (ProgressBar) findViewById(R.id.progressBar_cashier);
        this.textView = (TextView) findViewById(R.id.amnt);
        new getPaidItem().execute(new String[0]);
    }

    public void showProgress(boolean state) {
        if (state) {
            this.bar.setVisibility(0);
            this.listView.setVisibility(8);
            this.textView.setVisibility(8);
            return;
        }
        this.bar.setVisibility(8);
        this.listView.setVisibility(0);
        this.textView.setVisibility(0);
    }
}
