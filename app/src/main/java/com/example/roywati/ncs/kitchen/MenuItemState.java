package com.example.roywati.ncs.kitchen;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.waiter.AppConfig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 5/13/2017.
 */
public class MenuItemState extends Activity {

    ProgressBar bar;

    ListView listView;
    FloatingActionButton fabBtn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kitchen_order_item);
        listView=(ListView)findViewById(R.id.list_kitchen_order);
        fabBtn=(FloatingActionButton)findViewById(R.id.view_cart_menu_state);


        bar=(ProgressBar)findViewById(R.id.progressBar);
        changeFabImg();
        new getItemState().execute();

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new changeOrderState().execute();
            }
        });


    }
    public void changeFabImg(){
        if(AppConfigKitchen.num==5){

            fabBtn.setImageResource(R.drawable.pending);
        }
        if(AppConfigKitchen.num==6){
            fabBtn.setImageResource(R.drawable.processing);
        }
        if(AppConfigKitchen.num==7){
            fabBtn.setImageResource(R.drawable.processed);
        }
    }

    public class getItemState extends AsyncTask<String,String,String> {

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";

        JSONArray kitchenOrder;


        protected void onPreExecute(){
            super.onPreExecute();
      //      showProgressBar(true);
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser=new JSONParser();

            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();

            jsonObjectData.add(new BasicNameValuePair("orderId",AppConfigKitchen.selectedKitchenOrder));

            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfigKitchen.view_selected_order,
                    "GET",jsonObjectData);

            Log.d("data",jsonObjectResponse.toString());

            try{


                int success=jsonObjectResponse.getInt(TAG_SUCCESS);
                serverMessage=jsonObjectResponse.getString(TAG_MESSAGE);

                kitchenOrder=jsonObjectResponse.getJSONArray("kitchenOrder");

                Log.d("data",AppConfig.orderId);


                AppConfigKitchen.orderItem_id=new String[kitchenOrder.length()];
                AppConfigKitchen.menuItemId=new String[kitchenOrder.length()];
                AppConfigKitchen.menuitemname=new String[kitchenOrder.length()];
                AppConfigKitchen.quantity=new String[kitchenOrder.length()];

                for(int i=0;i<kitchenOrder.length();i++){
                    // JSONObject jsonObject=MenuCategoryArray.getJSONObject(i);
                    JSONObject jsonObject=kitchenOrder.getJSONObject(i);

                    Log.d("menu category",jsonObject.toString());


                    AppConfigKitchen.orderItem_id[i]=jsonObject.getString("order_items_id");
                    AppConfigKitchen.menuItemId[i]=jsonObject.getString("menu_item_id");;
                    AppConfigKitchen.menuitemname[i]=jsonObject.getString("menu_item_name");;
                    AppConfigKitchen.quantity[i]=jsonObject.getString("quantity");;

                    Log.d("kit-order id", AppConfigKitchen.orderItem_id[i]);
                    Log.d("kit-menu id", AppConfigKitchen.menuItemId[i]);
                    Log.d("kit-menu name", AppConfigKitchen.menuitemname[i]);
                    Log.d("kit-item quantity", AppConfigKitchen.quantity[i]);
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
                listView.setAdapter(new MenuItemSelectedAdapter(MenuItemState.this,AppConfigKitchen.menuitemname,AppConfigKitchen.quantity));
//
            }
            else{
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
       //     showProgressBar(false);
        }
    }

    public class changeOrderState extends AsyncTask<String,String,String> {

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";

        JSONArray kitchenOrder;


        protected void onPreExecute(){
            super.onPreExecute();
            //      showProgressBar(true);
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser=new JSONParser();

            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();

            jsonObjectData.add(new BasicNameValuePair("orderId",AppConfigKitchen.selectedKitchenOrder));
            jsonObjectData.add(new BasicNameValuePair("homepageId",AppConfigKitchen.homepageId));

            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfigKitchen.change_order_state,
                    "GET",jsonObjectData);

            Log.d("data",jsonObjectResponse.toString());

            try{


                int success=jsonObjectResponse.getInt(TAG_SUCCESS);
                serverMessage=jsonObjectResponse.getString(TAG_MESSAGE);


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
                    startActivity(new Intent(MenuItemState.this,KitchenHomePage.class));
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
            //     showProgressBar(false);
        }
    }

}
