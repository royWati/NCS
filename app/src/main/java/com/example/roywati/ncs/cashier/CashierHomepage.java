package com.example.roywati.ncs.cashier;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.defaults.LoginActivity;
import com.example.roywati.ncs.defaults.PrintData;
import com.example.roywati.ncs.waiter.AppConfig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 5/15/2017.
 */
public class CashierHomepage extends AppCompatActivity{

    LinearLayout linearLayout;
    ProgressBar prog;
    GridView gridView;
    Button btn,clearBtn;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashier_homepage);

        linearLayout=(LinearLayout)findViewById(R.id.linearItems);
        prog=(ProgressBar)findViewById(R.id.progressBar);
        gridView=(GridView)findViewById(R.id.grid_view_cashier);
        btn=(Button)findViewById(R.id.logout_cashier);
        clearBtn=(Button)findViewById(R.id.clear_orders);

        gridView.setAdapter(new CashierHomepageAdapter(CashierHomepage.this,AppConfig.menuItemId,AppConfig.menuItemName));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView=(TextView)view.findViewById(R.id.cashier_homepage_id);
                AppConfigCashier.homepageId=textView.getText().toString();

                int num=Integer.parseInt(AppConfigCashier.homepageId);
                if(num==8){
                    Intent intent=new Intent(CashierHomepage.this,cashierViewOrders.class);
                 //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else if(num==14){
                    startActivity(new Intent(CashierHomepage.this,GenerateBill.class));
                }else if(num==10){
                   startActivity(new Intent(CashierHomepage.this,PaidOrders.class));
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PrintData.TenderedAmount="0";
                PrintData.printSale="0";
                PrintData.ChangeGiven="0";

                Intent intent=new Intent(CashierHomepage.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrintData.TenderedAmount="0";
                PrintData.printSale="0";
                PrintData.ChangeGiven="0";

                new closeOrders().execute();
            }
        });


    }
    public void showProgressBar(boolean state) {
        if (state == true) {
            linearLayout.setVisibility(View.GONE);
            prog.setVisibility(View.VISIBLE);

        } else {
            linearLayout.setVisibility(View.VISIBLE);
            prog.setVisibility(View.GONE);
        }

    }

    public class closeOrders extends AsyncTask<String,String,String>{

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            showProgressBar(true);
        }

        protected String doInBackground(String... strings) {
            JSONParser jsonParser=new JSONParser();
            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();


            jsonObjectData.add(new BasicNameValuePair("userId",AppConfig.userId));

            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfigCashier.close_orders,
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
                Toast.makeText(CashierHomepage.this,serverMessage,Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(CashierHomepage.this,serverMessage,Toast.LENGTH_LONG).show();
            }
           showProgressBar(false);
        }
    }


}
