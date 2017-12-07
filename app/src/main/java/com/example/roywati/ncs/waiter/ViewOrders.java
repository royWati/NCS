package com.example.roywati.ncs.waiter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 5/16/2017.
 */
public class ViewOrders extends AppCompatActivity {

ListView listView;
    ContentLoadingProgressBar progressBar;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_waiter_order);

        listView=(ListView)findViewById(R.id.viewOrder);
        progressBar=(ContentLoadingProgressBar)findViewById(R.id.progr);

        new viewOrderItems().execute();

    }

    public void showProgress(boolean state){
        if(state==true) {
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }

    }

    public class viewOrderItems extends AsyncTask<String,String,String> {

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";

        JSONArray waiterOrders;



        protected void onPreExecute(){
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser=new JSONParser();

            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();

            jsonObjectData.add(new BasicNameValuePair("userId",AppConfig.userId));

            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfig.view_waiter_orders,
                    "GET",jsonObjectData);

            Log.d("data",jsonObjectResponse.toString());

            try{


                int success=jsonObjectResponse.getInt(TAG_SUCCESS);
                serverMessage=jsonObjectResponse.getString(TAG_MESSAGE);

                waiterOrders=jsonObjectResponse.getJSONArray("waiter_orders");

                Log.d("data",AppConfig.orderId);


                AppConfig.ordersids=new String[waiterOrders.length()];
                AppConfig.tablesnames=new String[waiterOrders.length()];
                AppConfig.statustypes=new String[waiterOrders.length()];
                AppConfig.checkoutTimes=new String[waiterOrders.length()];

                for(int i=0;i<waiterOrders.length();i++){
                    // JSONObject jsonObject=MenuCategoryArray.getJSONObject(i);
                    JSONObject jsonObject=waiterOrders.getJSONObject(i);

                    Log.d("menu category",jsonObject.toString());


                    AppConfig.ordersids[i]=jsonObject.getString("order_id");
                    AppConfig.tablesnames[i]=jsonObject.getString("table_name");
                    AppConfig.statustypes[i]=jsonObject.getString("order_status_name");
                    AppConfig.checkoutTimes[i]=jsonObject.getString("Checkout");

                    Log.d("view order id name", AppConfig.ordersids[i]);
                    Log.d("view order table name", AppConfig.tablesnames[i]);
                    Log.d("view order status ", AppConfig.statustypes[i]);
                    Log.d("view order check", AppConfig.checkoutTimes[i]);
                    //   Log.d("menu name", AppConfig.menuCategoryId[1]);


                }
                if(success==1){
                    successState=1;
                }



            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String s){
            super.onPostExecute(s);

            if(successState==1){
                listView.setAdapter(new viewOrderAdapter(ViewOrders.this,AppConfig.ordersids,AppConfig.tablesnames,
                        AppConfig.statustypes,AppConfig.checkoutTimes));
            }
            else{
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
           showProgress(false);
        }
    }
}
