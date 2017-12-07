package com.example.roywati.ncs.defaults;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.roywati.ncs.R;
import com.example.roywati.ncs.cashier.CashierHomepage;
import com.example.roywati.ncs.kitchen.KitchenHomePage;
import com.example.roywati.ncs.waiter.AppConfig;
import com.example.roywati.ncs.waiter.Homepage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    String strEmail;
    private EditText email1;
    private Button log1;

    ProgressBar prog;
    CheckInternetSettings net;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email1= (EditText)findViewById(R.id.email);
        log1 =(Button) findViewById(R.id.log);
        prog=(ProgressBar)findViewById(R.id.progressBar);

        net=new CheckInternetSettings(this);
        log1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmail=email1.getText().toString();

            //    Toast.makeText(getApplicationContext(),strEmail,Toast.LENGTH_SHORT).show();

                if (net.isNetworkConnected()){
                    new Loginprocess().execute();
                }else{
                    net.showSettingsAlert();
                }

            }
        });
    }

    public void showBar(boolean state) {
        if (state == true) {
            log1.setVisibility(View.GONE);
            prog.setVisibility(View.VISIBLE);

        } else {
            log1.setVisibility(View.VISIBLE);
            prog.setVisibility(View.GONE);
        }
    }
    public class Loginprocess extends AsyncTask<String,String,String> {

        int successState=0;
        //server response
        String sucessResponse="success";
        String messageResponse="message";

        //course string array
        String menuResponse="homepage";

        //names of the rows in the json objects

        String TAG_ID="homepage_id";
        String TAG_NAME="homepage_name";


        //json array object

        JSONArray homepageItems,userDetails;

        //server response
        int success;
        String serverMessage;
        protected void onPreExecute(){
            super.onPreExecute();
            showBar(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();

           jsonObjectData.add(new BasicNameValuePair("email",strEmail));

            JSONParser jsonParser=new JSONParser();

            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfig.login_url,
                    "GET",jsonObjectData);


            Log.d("Data items",jsonObjectResponse.toString());

            try {

                success=jsonObjectResponse.getInt(sucessResponse);
                serverMessage=jsonObjectResponse.getString(messageResponse);

                    //getting the json array from the json object
                    homepageItems=jsonObjectResponse.getJSONArray("homepage");

                    //storing the array elements into our local variables
                    AppConfig.menuItemName=new String[homepageItems.length()];
                    AppConfig.menuItemId=new String[homepageItems.length()];


                    for(int i=0;i<homepageItems.length();i++){
                        JSONObject jsonObject=homepageItems.getJSONObject(i);

                        Log.d("homepage",jsonObject.toString());

                        AppConfig.menuItemId[i]=jsonObject.getString(TAG_ID);
                        AppConfig.menuItemName[i]=jsonObject.getString(TAG_NAME);

                    }
                    userDetails=jsonObjectResponse.getJSONArray("userDetails");
                    JSONObject js=userDetails.getJSONObject(0);

                    AppConfig.userId=js.getString("userId");
                    AppConfig.branchId=js.getString("branchId");

                    Log.d("userId is", AppConfig.userId);
                    Log.d("branch id is", AppConfig.branchId);

                    if(success==1){
                        successState=1;
                    }else if(success==2){
                    successState=2;
                }else if(success==3){
                        successState=3;
                    }



            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            if (successState==1) {
               startActivity(new Intent(LoginActivity.this,Homepage.class));
                Toast.makeText(getApplicationContext(),AppConfig.userId,Toast.LENGTH_LONG).show();
            }else if(successState==2){
                startActivity(new Intent(LoginActivity.this,KitchenHomePage.class));
             //   AppConfig.userId= AppConfigKitchen.userId;
                Toast.makeText(getApplicationContext(),AppConfig.userId,Toast.LENGTH_LONG).show();
            }else if(successState==3){

                startActivity(new Intent(LoginActivity.this, CashierHomepage.class));
           //     AppConfig.userId=AppConfigCashier.userId;
                Toast.makeText(getApplicationContext(),AppConfig.userId,Toast.LENGTH_LONG).show();
            }
            else {

                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();

            }

            showBar(false);
        }
    }
}
