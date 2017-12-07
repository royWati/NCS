package com.example.roywati.ncs.cashier;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.defaults.PrintActivity;
import com.example.roywati.ncs.defaults.PrintData;
import com.example.roywati.ncs.waiter.AppConfig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 5/18/2017.
 */
public class GenerateBill extends AppCompatActivity{
    ListView listView;
    ProgressBar bar;
    TextView textView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashier_view_unpaid);

        listView=(ListView)findViewById(R.id.list_unpaid_orders);
        listView.deferNotifyDataSetChanged();
        bar=(ProgressBar)findViewById(R.id.progressBar_cashier);

        textView=(TextView)findViewById(R.id.amnt);

        new getPaidItem().execute();
    }

    public  void showProgress(boolean state){
        if(state==true){
            bar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }else{
            bar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }

    public class getPaidItem extends AsyncTask<String,String,String> {

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";

        JSONArray unpaidOrder;


        protected void onPreExecute(){
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser=new JSONParser();

            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();

            jsonObjectData.add(new BasicNameValuePair("branchId",AppConfig.branchId));

            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfigCashier.generate_bill,
                    "GET",jsonObjectData);

            Log.d("data",jsonObjectResponse.toString());

            try{


                int success=jsonObjectResponse.getInt(TAG_SUCCESS);
                serverMessage=jsonObjectResponse.getString(TAG_MESSAGE);
                AppConfigCashier.total=jsonObjectResponse.getString("total");



                unpaidOrder=jsonObjectResponse.getJSONArray("unpaid_orders");

                Log.d("data",AppConfig.orderId);


         //       AppConfigCashier.order_item_id=new String[unpaidOrder.length()];
                AppConfigCashier.order_id_generate_bill=new String[unpaidOrder.length()];
                AppConfigCashier.price=new String[unpaidOrder.length()];

                for(int i=0;i<unpaidOrder.length();i++){
                    // JSONObject jsonObject=MenuCategoryArray.getJSONObject(i);
                    JSONObject jsonObject=unpaidOrder.getJSONObject(i);

                    Log.d("menu category",jsonObject.toString());


           //         AppConfigCashier.order_item_id[i]=jsonObject.getString("order_items_id");
                    AppConfigCashier.order_id_generate_bill[i]=jsonObject.getString("order_id");;
                    AppConfigCashier.price[i]=jsonObject.getString("sum");

                 //   Log.d("kit-order id",AppConfigCashier.order_item_id[i]);
                    Log.d("kit-menu id", AppConfigCashier.order_id_generate_bill[i]);
                    Log.d("kit-menu name", AppConfigCashier.price[i]);




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
                textView.setText(AppConfigCashier.total);
                listView.setAdapter(new PaidOrdersAdapter(GenerateBill.this,AppConfigCashier.order_id_generate_bill,AppConfigCashier.price));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView textView=(TextView)view.findViewById(R.id.order_num_cash);
                        AppConfigCashier.orderNumber=textView.getText().toString();

                        //generate bill id
                        PrintData.print_data_id=2;
                        Toast.makeText(getApplicationContext(),AppConfigCashier.orderNumber,Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(GenerateBill.this,PrintActivity.class));
                    }
                });
            }
            else{
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
            showProgress(false);
        }
    }

}
