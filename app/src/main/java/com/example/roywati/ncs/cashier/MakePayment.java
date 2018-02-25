package com.example.roywati.ncs.cashier;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.defaults.NoDataException;
import com.example.roywati.ncs.defaults.PrintActivity;
import com.example.roywati.ncs.defaults.PrintData;
import com.example.roywati.ncs.waiter.AppConfig;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MakePayment extends AppCompatActivity {
    TextView amountGiven;
    String amountGivenToCashier;
    Button button;
    TextView changeDue;
    EditText edit_text;
    TextView orderid;
    TextView totalAmount,discount_amount;
    TextView wrongPayment;

   // RelativeLayout rela_disc;

    FloatingActionButton fab_dis;
    public class updatePayment extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        String serverMessage = "request not sent";
        int successState = 0;

        protected void onPreExecute() {
            super.onPreExecute();
            MakePayment.this.button.setVisibility(8);
        }

        protected String doInBackground(String... strings) {

            try {
                JSONParser jsonParser = new JSONParser();
                List<NameValuePair> jsonObjectData = new ArrayList();
                jsonObjectData.add(new BasicNameValuePair("orderId", AppConfigCashier.orderNumber));
                jsonObjectData.add(new BasicNameValuePair("amount", AppConfigCashier.amount));
                jsonObjectData.add(new BasicNameValuePair("amountGiven", AppConfigCashier.amountGivenToCashier));
                jsonObjectData.add(new BasicNameValuePair("userId", AppConfig.userId));
                jsonObjectData.add(new BasicNameValuePair("branchId", AppConfig.branchId));
                JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfigCashier.make_payment, HttpGet.METHOD_NAME, jsonObjectData);
                Log.d("orderId and menuCatId", AppConfig.orderId + " " + AppConfig.menuCatId);
                Log.e("amountGiven", AppConfigCashier.amountGivenToCashier);
                Log.e("userId", AppConfig.userId);

                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
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
            Log.d("successState", String.valueOf(this.successState));
            if (this.successState == 1) {
                PrintData.print_data_id = 3;
             //   Toast.makeText(MakePayment.this, this.serverMessage, 1).show();
                MakePayment.this.startActivity(new Intent(MakePayment.this, PrintActivity.class));
                return;
            }else if(successState==500){
             //   Toast.makeText(getApplicationContext(), "Network Error!!", 1).show();
            }
            Toast.makeText(MakePayment.this, this.serverMessage, 1).show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.cashier_payment);
        setTitle("Payment");
        this.orderid = (TextView) findViewById(R.id.orderId_payment);
        this.wrongPayment = (TextView) findViewById(R.id.wrong_payment);
        this.amountGiven = (TextView) findViewById(R.id.amount_given);
        this.changeDue = (TextView) findViewById(R.id.change_due);
        this.totalAmount = (TextView) findViewById(R.id.total_order_price);
        this.edit_text = (EditText) findViewById(R.id.editText);
        this.button = (Button) findViewById(R.id.makePayment);
        this.orderid.setText(AppConfigCashier.orderNumber);
        this.totalAmount.setText(AppConfigCashier.amount);
        fab_dis=findViewById(R.id.discount_load);

        discount_amount=findViewById(R.id.discount_amount_val);
    //    rela_disc=findViewById(R.id.linear_discounts);



       fab_dis.setVisibility(View.GONE);


        this.button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                AppConfigCashier.amount=totalAmount.getText().toString();

                MakePayment.this.amountGivenToCashier = MakePayment.this.edit_text.getText().toString();
                AppConfigCashier.amountGivenToCashier = MakePayment.this.amountGivenToCashier;
                AppConfigCashier.changeAmount = Integer.parseInt(MakePayment.this.amountGivenToCashier) -
                        Integer.parseInt(AppConfigCashier.amount);
                MakePayment.this.amountGiven.setText(MakePayment.this.amountGivenToCashier);
                MakePayment.this.changeDue.setText(String.valueOf(AppConfigCashier.changeAmount));
                if (AppConfigCashier.changeAmount > 0 || AppConfigCashier.changeAmount == 0) {
                    new updatePayment().execute(new String[0]);
                } else {
                    MakePayment.this.wrongPayment.setVisibility(0);
                }
            }
        });
    }


}
