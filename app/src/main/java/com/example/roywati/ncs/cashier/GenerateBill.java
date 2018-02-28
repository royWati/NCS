package com.example.roywati.ncs.cashier;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.roywati.ncs.defaults.PrintData;
import com.example.roywati.ncs.waiter.AppConfig;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class GenerateBill extends AppCompatActivity {
    ProgressBar bar;
    ListView listView;
    TextView textView;

    public static  int price_pos;
    public static  int discount_pos;

    public class getPaidItem extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        String serverMessage = "request not sent";
        int successState = 0;
        JSONArray unpaidOrder;

        protected void onPreExecute() {
            super.onPreExecute();

            GenerateBill.this.showProgress(true);

            new getDiscounts().execute();
        }

        protected String doInBackground(String... strings) {

            try {
                JSONParser jsonParser = new JSONParser();
                List<NameValuePair> jsonObjectData = new ArrayList();
                jsonObjectData.add(new BasicNameValuePair("branchId", AppConfig.branchId));
                JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname +
                        AppConfigCashier.generate_bill, HttpGet.METHOD_NAME, jsonObjectData);
                Log.d("data", jsonObjectResponse.toString());


                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
                AppConfigCashier.total = jsonObjectResponse.getString("total");
                this.unpaidOrder = jsonObjectResponse.getJSONArray("unpaid_orders");
                Log.d("data", AppConfig.orderId);
                AppConfigCashier.order_id_generate_bill = new String[this.unpaidOrder.length()];
                AppConfigCashier.price = new String[this.unpaidOrder.length()];
                for (int i = 0; i < this.unpaidOrder.length(); i++) {
                    JSONObject jsonObject = this.unpaidOrder.getJSONObject(i);
                    Log.d("menu category", jsonObject.toString());
                    AppConfigCashier.order_id_generate_bill[i] = jsonObject.getString("order_id");
                    AppConfigCashier.price[i] = jsonObject.getString("sum");
                    Log.d("kit-menu id", AppConfigCashier.order_id_generate_bill[i]);
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

                GenerateBill.this.textView.setText(AppConfigCashier.total);
                GenerateBill.this.listView.setAdapter(new PaidOrdersAdapter(GenerateBill.this, AppConfigCashier.order_id_generate_bill, AppConfigCashier.price));
                GenerateBill.this.listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        price_pos= (int) adapterView.getItemIdAtPosition(i);
                        AppConfigCashier.orderNumber = ((TextView) view.findViewById(R.id.order_num_cash)).getText().toString();
                        PrintData.print_data_id = 2;
                        popDiscounts(price_pos);


                    }
                });
            }else if(successState==500){
                Toast.makeText(getApplicationContext(), "Network Error!!", 1).show();
            } else {
                Toast.makeText(GenerateBill.this.getApplicationContext(), this.serverMessage, 1).show();
            }
            GenerateBill.this.showProgress(false);
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

    public class getDiscounts extends AsyncTask<String,String,String>{

        int success=0;
        protected void onPreExecute(){
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser = new JSONParser();
            List<NameValuePair> jsonObjectData = new ArrayList();
            jsonObjectData.add(new BasicNameValuePair("userId", AppConfigCashier.userId));

            JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal +
                    AppConfig.hostname + AppConfigCashier.get_discounts, HttpGet.METHOD_NAME, jsonObjectData);

            try{
                success=jsonObjectResponse.getInt("success");

                if(success==1){
                    JSONArray data=jsonObjectResponse.getJSONArray("discounts");

                    AppConfigCashier.discount_name=new String[data.length()];
                    AppConfigCashier.discount_value=new String[data.length()];




                    for (int i=0;i<data.length();i++){
                        JSONObject jobj=data.getJSONObject(i);
                        AppConfigCashier.discount_value[i]=jobj.getString("value");
                        AppConfigCashier.discount_name[i]=jobj.getString("name");
                    }


                }
            }catch (Exception e){

            }

            return null;
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);

            if(success !=1){
                Toast.makeText(getApplicationContext(),"error while loading discounts",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void popDiscounts(final int pricePosition){
        View view= LayoutInflater.from(this).inflate(R.layout.select_discount,null);
        ListView pop_listView=view.findViewById(R.id.list_disc);

        final AlertDialog alertDialog=new AlertDialog.Builder(this).create();
        alertDialog.setView(view);
        alertDialog.setCancelable(false);

        pop_listView.setAdapter(new DiscountsAdapter(this,AppConfigCashier.discount_name,AppConfigCashier.discount_value));

        pop_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialog.cancel();
                int pos= (int) parent.getItemIdAtPosition(position);
             //   discountCalc(AppConfigCashier.discount_value[pos],pricePosition);



                Log.d("position of price index",String.valueOf(pos));

                discounted_value=AppConfigCashier.discount_value[pos];
                AppConfigCashier.order_discount_perce=AppConfigCashier.discount_value[pos];

                discountCalc(AppConfigCashier.order_discount_perce,pricePosition);
                new post_amount().execute();

            }
        });
        alertDialog.show();
        alertDialog.setCancelable(true);

    }

    static String final_amount;
    static String discounted_value;

    public void discountCalc(String value,int position){
        int initial_amount=Integer.parseInt(AppConfigCashier.price[position]);

        AppConfigCashier.order_discount_am=String.valueOf((initial_amount*Integer.parseInt(value))/100);

    }


    public class post_amount extends AsyncTask<String,String,String>{

        int success=0;

        ProgressDialog dialog=new ProgressDialog(GenerateBill.this);
        protected void onPreExecute(){

            super.onPreExecute();

            dialog.setMessage("updating amount. please wait..");
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser=new JSONParser();
            List parameters = new ArrayList();
            parameters.add(new BasicNameValuePair("order_id",AppConfigCashier.orderNumber));
            parameters.add(new BasicNameValuePair("discount",discounted_value));


            JSONObject jsonObject=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfigCashier.update_amount
            ,"GET",parameters);

            Log.d("request",AppConfig.protocal+AppConfig.hostname+AppConfigCashier.update_amount);
            Log.d("response",jsonObject.toString());

            try{
                success=jsonObject.getInt("success");
            }catch (Exception e){

            }
            return null;
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            dialog.cancel();
            if(success==1){
                GenerateBill.this.startActivity(new Intent(GenerateBill.this, PrintActivity.class));
            }else{
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
