package com.example.roywati.ncs.defaults;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.roywati.ncs.R;
import com.example.roywati.ncs.cashier.CashierHomepage;
import com.example.roywati.ncs.kitchen.KitchenHomePage;
import com.example.roywati.ncs.waiter.AppConfig;
import com.example.roywati.ncs.waiter.Homepage;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText email1;
    private Button log1;
    CheckInternetSettings net;
    ProgressBar prog;
    String strEmail;

    public class Loginprocess extends AsyncTask<String, String, String> {
        String TAG_ID = "homepage_id";
        String TAG_NAME = "homepage_name";
        JSONArray homepageItems;
        String menuResponse = "homepage";
        String messageResponse = "message";
        JSONArray printerDetails;
        String serverMessage;
        int success;
        int successState = 0;
        String sucessResponse = "success";
        JSONArray userDetails;

        protected void onPreExecute() {
            super.onPreExecute();
            LoginActivity.this.showBar(true);
        }

        protected String doInBackground(String... strings) {
            List<NameValuePair> jsonObjectData = new ArrayList();
            jsonObjectData.add(new BasicNameValuePair("email", LoginActivity.this.strEmail));
            JSONObject jsonObjectResponse = new JSONParser().makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfig.login_url, HttpGet.METHOD_NAME, jsonObjectData);
            Log.d("Data items", jsonObjectResponse.toString());
            try {
                this.success = jsonObjectResponse.getInt(this.sucessResponse);
                this.serverMessage = jsonObjectResponse.getString(this.messageResponse);
                this.homepageItems = jsonObjectResponse.getJSONArray("homepage");
                AppConfig.menuItemName = new String[this.homepageItems.length()];
                AppConfig.menuItemId = new String[this.homepageItems.length()];
                for (int i = 0; i < this.homepageItems.length(); i++) {
                    JSONObject jsonObject = this.homepageItems.getJSONObject(i);
                    Log.d("homepage", jsonObject.toString());
                    AppConfig.menuItemId[i] = jsonObject.getString(this.TAG_ID);
                    AppConfig.menuItemName[i] = jsonObject.getString(this.TAG_NAME);
                }
                this.userDetails = jsonObjectResponse.getJSONArray("userDetails");
                JSONObject js = this.userDetails.getJSONObject(0);
                AppConfig.userId = js.getString("userId");
                AppConfig.branchId = js.getString("branchId");
                this.printerDetails = jsonObjectResponse.getJSONArray("printers");
                JSONObject js_printer = this.printerDetails.getJSONObject(0);
                Log.d("printer object", js_printer.toString());
                AppConfig.printMac = js_printer.getString("printer_MAC ");
                AppConfig.printer_name = js_printer.getString("printer_name");
                Log.d("printMac", AppConfig.printMac);
                Log.d("printer_name", AppConfig.printer_name);
                if (this.success == 1) {
                    this.successState = 1;
                } else if (this.success == 2) {
                    this.successState = 2;
                } else if (this.success == 3) {
                    this.successState = 3;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (this.successState == 1) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, Homepage.class));
                Toast.makeText(LoginActivity.this.getApplicationContext(), AppConfig.userId, 1).show();
            } else if (this.successState == 2) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, KitchenHomePage.class));
                Toast.makeText(LoginActivity.this.getApplicationContext(), AppConfig.userId, 1).show();
            } else if (this.successState == 3) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, CashierHomepage.class));
                Toast.makeText(LoginActivity.this.getApplicationContext(), AppConfig.userId, 1).show();
            } else {
                Toast.makeText(LoginActivity.this.getApplicationContext(), this.serverMessage, 1).show();
            }
            LoginActivity.this.showBar(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        this.email1 = (EditText) findViewById(R.id.email);
        this.log1 = (Button) findViewById(R.id.log);
        this.prog = (ProgressBar) findViewById(R.id.progressBar);
        this.net = new CheckInternetSettings(this);
        this.log1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LoginActivity.this.strEmail = LoginActivity.this.email1.getText().toString();
                if (LoginActivity.this.net.isNetworkConnected()) {
                    new Loginprocess().execute(new String[0]);
                } else {
                    LoginActivity.this.net.showSettingsAlert();
                }
            }
        });
    }

    public void showBar(boolean state) {
        if (state) {
            this.log1.setVisibility(8);
            this.prog.setVisibility(0);
            return;
        }
        this.log1.setVisibility(0);
        this.prog.setVisibility(8);
    }
}
