package com.example.roywati.ncs.cashier;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.defaults.LoginActivity;
import com.example.roywati.ncs.defaults.NoDataException;
import com.example.roywati.ncs.defaults.PrintData;
import com.example.roywati.ncs.kitchen.KitchenHomePage;
import com.example.roywati.ncs.waiter.AppConfig;
import com.example.roywati.ncs.waiter.ViewOrders;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class CashierHomepage extends AppCompatActivity {
    Button btn;
    Button clearBtn;
    GridView gridView;
    LinearLayout linearLayout;
    ProgressBar prog;

    public class closeOrders extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        String serverMessage = "request not sent";
        int successState = 0;

        protected void onPreExecute() {
            super.onPreExecute();
            CashierHomepage.this.showProgressBar(true);
        }

        protected String doInBackground(String... strings) {
            try {
                JSONParser jsonParser = new JSONParser();
                List<NameValuePair> jsonObjectData = new ArrayList();
                jsonObjectData.add(new BasicNameValuePair("userId", AppConfig.userId));
                jsonObjectData.add(new BasicNameValuePair("branchId", AppConfig.branchId));
                JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfigCashier.close_orders, HttpGet.METHOD_NAME, jsonObjectData);
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
            //    Toast.makeText(CashierHomepage.this, this.serverMessage, 1).show();
            }else if(successState==500){
                Toast.makeText(CashierHomepage.this.getApplicationContext(), "Network Error!!", 1).show();
            } else {
                Toast.makeText(CashierHomepage.this, this.serverMessage, 1).show();
            }
            CashierHomepage.this.showProgressBar(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.cashier_homepage);
        this.linearLayout = (LinearLayout) findViewById(R.id.linearItems);
        this.prog = (ProgressBar) findViewById(R.id.progressBar);
        this.gridView = (GridView) findViewById(R.id.grid_view_cashier);
        this.btn = (Button) findViewById(R.id.logout_cashier);
        this.clearBtn = (Button) findViewById(R.id.clear_orders);
        this.gridView.setAdapter(new CashierHomepageAdapter(this, AppConfig.menuItemId, AppConfig.menuItemName));
        this.gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppConfigCashier.homepageId = ((TextView) view.findViewById(R.id.cashier_homepage_id)).getText().toString();
                int num = Integer.parseInt(AppConfigCashier.homepageId);
                if (num == 8) {
                    CashierHomepage.this.startActivity(new Intent(CashierHomepage.this, cashierViewOrders.class));
                } else if (num == 14) {
                    CashierHomepage.this.startActivity(new Intent(CashierHomepage.this, GenerateBill.class));
                } else if (num == 10) {
                    CashierHomepage.this.startActivity(new Intent(CashierHomepage.this, PaidOrders.class));
                }
            }
        });
        this.btn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PrintData.TenderedAmount = "0";
                PrintData.printSale = "0";
                PrintData.ChangeGiven = "0";
                Intent intent = new Intent(CashierHomepage.this, LoginActivity.class);
                intent.setFlags(268468224);
                CashierHomepage.this.startActivity(intent);
            }
        });
        this.clearBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PrintData.TenderedAmount = "0";
                PrintData.printSale = "0";
                PrintData.ChangeGiven = "0";
                new closeOrders().execute(new String[0]);
            }
        });
    }

    public void showProgressBar(boolean state) {
        if (state) {
            this.linearLayout.setVisibility(8);
            this.prog.setVisibility(0);
            return;
        }
        this.linearLayout.setVisibility(0);
        this.prog.setVisibility(8);
    }

    public void onBackPressed(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setMessage((CharSequence) "You will be logged out!!");
        alertDialog.setPositiveButton((CharSequence) "Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(CashierHomepage.this, LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });
        alertDialog.setNegativeButton((CharSequence) "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();

    }
}
