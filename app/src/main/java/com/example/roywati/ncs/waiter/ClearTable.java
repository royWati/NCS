package com.example.roywati.ncs.waiter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.defaults.NoDataException;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class ClearTable extends AppCompatActivity {
    GridView gridView;
    LinearLayout linearLayout;
    ProgressBar prog;

    public class Selecttables extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        JSONArray Tables;
        String serverMessage = "request not sent";
        int successState = 0;

        protected void onPreExecute() {
            super.onPreExecute();
            ClearTable.this.showProgressBar(true);
        }

        protected String doInBackground(String... strings) {

            try {
                JSONParser jsonParser = new JSONParser();
                List<NameValuePair> jsonObjectData = new ArrayList();
                jsonObjectData.add(new BasicNameValuePair("branchId", AppConfig.branchId));
                JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfig.select_all_tables, HttpGet.METHOD_NAME, jsonObjectData);
                Log.d("data", jsonObjectResponse.toString());

                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
                this.Tables = jsonObjectResponse.getJSONArray("tables");
                Log.d("data", AppConfig.orderId);
                AppConfig.tableId = new String[this.Tables.length()];
                AppConfig.tableName = new String[this.Tables.length()];
                for (int i = 0; i < this.Tables.length(); i++) {
                    JSONObject jsonObject = this.Tables.getJSONObject(i);
                    Log.d("menu category", jsonObject.toString());
                    AppConfig.tableId[i] = jsonObject.getString("table_id");
                    AppConfig.tableName[i] = jsonObject.getString("table_name");
                    Log.d("table id", AppConfig.tableId[i]);
                    Log.d("table name", AppConfig.tableName[i]);
                }
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
            if (this.successState == 1) {
                ClearTable.this.gridView.setAdapter(new MenuCategoryAdapter(ClearTable.this, AppConfig.tableName, AppConfig.tableId));
                ClearTable.this.gridView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        AppConfig.deleteTableId_pos = (int) adapterView.getItemIdAtPosition(i);
                        AppConfig.deleteTableId = ((TextView) ClearTable.this.findViewById(R.id.menuCategoryId)).getText().toString();
                        new clearTableInBranch().execute(new String[0]);
                    }
                });
            } else if(successState==500){
                Toast.makeText(getApplicationContext(), "Network Error!!", 1).show();
            }else {
                Toast.makeText(ClearTable.this.getApplicationContext(), this.serverMessage, 1).show();
            }
            ClearTable.this.showProgressBar(false);
        }
    }

    public class clearTableInBranch extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        String serverMessage = "request not sent";
        int successState = 0;

        protected void onPreExecute() {
            super.onPreExecute();
            ClearTable.this.showProgressBar(true);
        }

        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser();
            List<NameValuePair> jsonObjectData = new ArrayList();
            jsonObjectData.add(new BasicNameValuePair("branchId", AppConfig.branchId));
            jsonObjectData.add(new BasicNameValuePair("tableId", AppConfig.tableId[AppConfig.deleteTableId_pos]));
            JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfig.clear_tables, HttpGet.METHOD_NAME, jsonObjectData);
            Log.d("data", jsonObjectResponse.toString());
            try {
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
            if (this.successState != 1) {
                Toast.makeText(ClearTable.this.getApplicationContext(), this.serverMessage, 1).show();
            } else if (AppConfig.clear_a_table_key) {
                ClearTable.this.startActivity(new Intent(ClearTable.this, ViewCart.class));
            } else {
                ClearTable.this.startActivity(ClearTable.this.getIntent());
                ClearTable.this.finish();
            }
            ClearTable.this.showProgressBar(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.gridlayout_menu_category);
        this.linearLayout = (LinearLayout) findViewById(R.id.linearItems);
        this.prog = (ProgressBar) findViewById(R.id.progressBarMenu);
        this.gridView = (GridView) findViewById(R.id.grid_view_cat);
        new Selecttables().execute(new String[0]);
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
}
