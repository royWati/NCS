package com.example.roywati.ncs.waiter;

import android.os.AsyncTask;
import android.util.Log;
import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.defaults.NoDataException;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class GetViewCart extends AsyncTask<String, String, String> {
    String TAG_MESSAGE = "message";
    String TAG_SUCCESS = "success";
    String serverMessage = "request not sent";
    int successState = 0;
    String total;

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... strings) {

        try {
            JSONParser jsonParser = new JSONParser();
            List<NameValuePair> jsonObjectData = new ArrayList();
            jsonObjectData.add(new BasicNameValuePair("orderId", AppConfig.orderId));
            jsonObjectData.add(new BasicNameValuePair("tableId", AppConfig.tableId_selected));
            jsonObjectData.add(new BasicNameValuePair("userId", AppConfig.userId));
            JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfig.checkout_cart, HttpGet.METHOD_NAME, jsonObjectData);
            Log.d("data sub category", jsonObjectResponse.toString());

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
        if (this.successState != 1) {
        }
    }
}
