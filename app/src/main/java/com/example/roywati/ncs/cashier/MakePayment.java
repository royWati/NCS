package com.example.roywati.ncs.cashier;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.defaults.PrintActivity;
import com.example.roywati.ncs.defaults.PrintData;
import com.example.roywati.ncs.waiter.AppConfig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 5/17/2017.
 */
public class MakePayment extends AppCompatActivity {

    String  amountGivenToCashier;

    TextView orderid,amountGiven,changeDue,totalAmount,wrongPayment;
    EditText edit_text;
    Button button;
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashier_payment);
        setTitle("Payment");

        orderid=(TextView)findViewById(R.id.orderId_payment);
        wrongPayment=(TextView)findViewById(R.id.wrong_payment);
        amountGiven=(TextView)findViewById(R.id.amount_given);
        changeDue=(TextView)findViewById(R.id.change_due);
        totalAmount=(TextView)findViewById(R.id.total_order_price);

        edit_text=(EditText)findViewById(R.id.editText);
        button=(Button)findViewById(R.id.makePayment);


        orderid.setText(AppConfigCashier.orderNumber);
        totalAmount.setText(AppConfigCashier.amount);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amountGivenToCashier=edit_text.getText().toString();
                AppConfigCashier.amountGivenToCashier=amountGivenToCashier;

                int sale_amount=Integer.parseInt(AppConfigCashier.amount);
                int given_amount=Integer.parseInt(amountGivenToCashier);

                AppConfigCashier.changeAmount=given_amount-sale_amount;
                amountGiven.setText(amountGivenToCashier);

             String strchangeDue=String.valueOf(AppConfigCashier.changeAmount);

                changeDue.setText(strchangeDue);

                if(AppConfigCashier.changeAmount>0||AppConfigCashier.changeAmount==0){
                    new updatePayment().execute();
                }
                else{
                    wrongPayment.setVisibility(View.VISIBLE);
                }




            }
        });



    }

    public class updatePayment extends AsyncTask<String,String,String>{

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        protected String doInBackground(String... strings) {
            JSONParser jsonParser=new JSONParser();
            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();
            jsonObjectData.add(new BasicNameValuePair("orderId", AppConfigCashier.orderNumber));
            jsonObjectData.add(new BasicNameValuePair("amount",AppConfigCashier.amount));
            jsonObjectData.add(new BasicNameValuePair("amountGiven", AppConfigCashier.amountGivenToCashier));
            jsonObjectData.add(new BasicNameValuePair("userId",AppConfig.userId));


            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfigCashier.make_payment,
                    "GET",jsonObjectData);

            Log.d("orderId and menuCatId",AppConfig.orderId+" "+AppConfig.menuCatId);


            Log.e("amountGiven", AppConfigCashier.amountGivenToCashier);
            Log.e("userId",AppConfig.userId);
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
            if(successState==1){
                PrintData.print_data_id=3;
                Toast.makeText(MakePayment.this,serverMessage,Toast.LENGTH_LONG).show();
                startActivity(new Intent(MakePayment.this, PrintActivity.class));
            }
            else{
                Toast.makeText(MakePayment.this,serverMessage,Toast.LENGTH_LONG).show();
            }

        }
    }

    
}
