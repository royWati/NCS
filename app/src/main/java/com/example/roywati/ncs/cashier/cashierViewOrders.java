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
import com.example.roywati.ncs.waiter.AppConfig;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class cashierViewOrders extends AppCompatActivity {
    ProgressBar bar;
    ListView listView;
    TextView textView;

    public class getUnpaidItem extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        String serverMessage = "request not sent";
        int successState = 0;
        JSONArray unpaidOrder;

        protected void onPreExecute() {
            super.onPreExecute();
            cashierViewOrders.this.showProgress(true);
        }

        protected String doInBackground(String... strings) {

            try {
                JSONParser jsonParser = new JSONParser();
                List<NameValuePair> jsonObjectData = new ArrayList();
                jsonObjectData.add(new BasicNameValuePair("branchId", AppConfig.branchId));
                JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname +
                        AppConfigCashier.get_unpaid_orders, HttpGet.METHOD_NAME, jsonObjectData);
                Log.d("data", jsonObjectResponse.toString());

                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
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
            } catch (NullPointerException e) {
              successState=500;
            }
            catch (JSONException e) {
                successState=500;
            }
            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (this.successState == 1) {
                cashierViewOrders.this.listView.setAdapter(new CashierViewOrderAdapter(cashierViewOrders.this, AppConfigCashier.order_id, AppConfigCashier.price));
                cashierViewOrders.this.listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        int pos= (int) adapterView.getItemIdAtPosition(i);

                        AppConfigCashier.order_discount_am=AppConfigCashier.order_discount_amount[pos];
                        AppConfigCashier.order_discount_perce=AppConfigCashier.order_discount_percentage[pos];
                        AppConfigCashier.orderNumber = ((TextView) view.findViewById(R.id.order_num_cash_unpaid)).getText().toString();
                        AppConfigCashier.amount = ((TextView) view.findViewById(R.id.item_price_cash_unpaid)).getText().toString();
                        cashierViewOrders.this.startActivity(new Intent(cashierViewOrders.this, MakePayment.class));
                    }
                });
            }else if(successState==500){
                    Toast.makeText(getApplicationContext(), "Network Error!!", 1).show();
            }

            else {
                cashierViewOrders.this.textView.setVisibility(0);
                Toast.makeText(cashierViewOrders.this.getApplicationContext(), this.serverMessage, 1).show();
            }
            cashierViewOrders.this.showProgress(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.cashier_view_unpaid);
        setTitle("Select order for payment");
        this.listView = (ListView) findViewById(R.id.list_unpaid_orders);
        this.bar = (ProgressBar) findViewById(R.id.progressBar_cashier);
        this.textView = (TextView) findViewById(R.id.showText_for_unpaid);
        new getUnpaidItem().execute(new String[0]);
    }

    public void showProgress(boolean state) {
        if (state) {
            this.bar.setVisibility(0);
            this.listView.setVisibility(8);
            return;
        }
        this.bar.setVisibility(8);
        this.listView.setVisibility(0);
    }


}
