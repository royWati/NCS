package com.example.roywati.ncs.waiter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
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
 * Created by Chris on 5/14/2017.
 */
public class AddOns extends AppCompatActivity {

    GridView gridView;
    Button btn;
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.kitchen_homepage);

        btn=(Button)findViewById(R.id.logout_kitchen);
        gridView=(GridView)findViewById(R.id.homepage_kitchen_gridview);

        btn.setVisibility(View.GONE);
        new addItem().execute();

    }

//    public void showProgressBar(boolean state) {
//        if (state == true) {
//            linearLayout.setVisibility(View.GONE);
//            prog.setVisibility(View.VISIBLE);
//
//        } else {
//            linearLayout.setVisibility(View.VISIBLE);
//            prog.setVisibility(View.GONE);
//        }
//
//    }

    public class addItem extends AsyncTask<String,String,String> {

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";




        //  JSONArray oderId;
        JSONArray add_ons_orders;



        protected void onPreExecute(){
            super.onPreExecute();
       //     showProgressBar(true);
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser=new JSONParser();

            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();
            jsonObjectData.add(new BasicNameValuePair("userId",AppConfig.userId));


            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfig.add_ons,
                    "GET",jsonObjectData);

            Log.d("data",jsonObjectResponse.toString());

            try{


                int success=jsonObjectResponse.getInt(TAG_SUCCESS);
                serverMessage=jsonObjectResponse.getString(TAG_MESSAGE);

                add_ons_orders=jsonObjectResponse.getJSONArray("orders");


                Log.d("data",AppConfig.orderId);


                AppConfig.tables=new String[add_ons_orders.length()];
                AppConfig.orders=new String[add_ons_orders.length()];
                AppConfig.addOn_tables=new String[add_ons_orders.length()];

                for(int i=0;i<add_ons_orders.length();i++){
                    // JSONObject jsonObject=MenuCategoryArray.getJSONObject(i);
                    JSONObject jsonObject=add_ons_orders.getJSONObject(i);

                    Log.d("menu sub category",jsonObject.toString());


                    AppConfig.tables[i]=jsonObject.getString("order_id");
                    AppConfig.orders[i]=jsonObject.getString("order_status_id");
                    AppConfig.addOn_tables[i]=jsonObject.getString("table_name");

                    Log.d("sub menu name", AppConfig.tables[i]);
                    Log.d("sub menu name", AppConfig.orders[i]);
                    Log.d("sub menu name", AppConfig.addOn_tables[i]);
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

                gridView.setAdapter(new AddOnsAdapter(AddOns.this,AppConfig.orders,AppConfig.tables,AppConfig.addOn_tables));

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView txt=(TextView)view.findViewById(R.id.order_no_selected);
                        AppConfig.orderId=txt.getText().toString();

                        AppConfig.add_on_event=true;
                        startActivity(new Intent(AddOns.this,AddOnMenuCategory.class));
                        Toast.makeText(getApplicationContext(), AppConfig.orderId,Toast.LENGTH_SHORT).show();
                    }
                });

                Log.d("new order",AppConfig.orderId);
            }
            else{
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
       //     showProgressBar(false);
        }
    }

}
