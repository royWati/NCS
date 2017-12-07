package com.example.roywati.ncs.waiter;

import android.os.AsyncTask;
import android.util.Log;

import com.example.roywati.ncs.defaults.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 5/12/2017.
 */
public class GetViewCart extends AsyncTask<String,String,String> {

    int successState=0;
    String serverMessage="request not sent";

    String TAG_MESSAGE="message";
    String TAG_SUCCESS="success";

    String total;




    protected void onPreExecute(){
        super.onPreExecute();
   //   showProgressBar(true);

    }

    @Override
    protected String doInBackground(String... strings) {
        JSONParser jsonParser=new JSONParser();

        List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();
        jsonObjectData.add(new BasicNameValuePair("orderId",AppConfig.orderId));
        jsonObjectData.add(new BasicNameValuePair("tableId",AppConfig.tableId_selected));
        jsonObjectData.add(new BasicNameValuePair("userId",AppConfig.userId));

       // Log.d("menuSubCatId",AppConfig.orderId);
        JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfig.checkout_cart,
                "GET",jsonObjectData);

        Log.d("data sub category",jsonObjectResponse.toString());

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

        Log.d("successState",String.valueOf(successState));

        Log.d("successState",AppConfig.total_value_in_cart);

        if(successState==1){


         //    startActivity(new Intent(ViewCart.this,Homepage.class));
       //     Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();

        }
        else{

         //   Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
        }


    }
}
