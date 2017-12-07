package com.example.roywati.ncs.waiter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 5/14/2017.
 */
public class ClearTable extends AppCompatActivity {

    LinearLayout linearLayout;
    ProgressBar prog;
    GridView gridView;

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridlayout_menu_category);

        linearLayout=(LinearLayout)findViewById(R.id.linearItems);
        prog=(ProgressBar)findViewById(R.id.progressBarMenu);
        gridView=(GridView)findViewById(R.id.grid_view_cat);

        new Selecttables().execute();


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

    public class Selecttables extends AsyncTask<String,String,String> {

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";




        JSONArray Tables;




        protected void onPreExecute(){
            super.onPreExecute();
            showProgressBar(true);
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser=new JSONParser();

            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();
            jsonObjectData.add(new BasicNameValuePair("branchId",AppConfig.branchId));


            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfig.select_all_tables,
                    "GET",jsonObjectData);

            Log.d("data",jsonObjectResponse.toString());

            try{


                int success=jsonObjectResponse.getInt(TAG_SUCCESS);
                serverMessage=jsonObjectResponse.getString(TAG_MESSAGE);

                Tables=jsonObjectResponse.getJSONArray("tables");


                Log.d("data",AppConfig.orderId);


                AppConfig.tableId=new String[Tables.length()];
                AppConfig.tableName=new String[Tables.length()];


                for(int i=0;i<Tables.length();i++){
                    // JSONObject jsonObject=MenuCategoryArray.getJSONObject(i);
                    JSONObject jsonObject=Tables.getJSONObject(i);

                    Log.d("menu category",jsonObject.toString());


                    AppConfig.tableId[i]=jsonObject.getString("table_id");
                    AppConfig.tableName[i]=jsonObject.getString("table_name");

                    Log.d("table id", AppConfig.tableId[i]);
                    Log.d("table name", AppConfig.tableName[i]);
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
                gridView.setAdapter(new MenuCategoryAdapter(ClearTable.this,AppConfig.tableName,AppConfig.tableId));

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        AppConfig.deleteTableId_pos= (int) adapterView.getItemIdAtPosition(i);
                        TextView txt=(TextView)findViewById(R.id.menuCategoryId);

                        AppConfig.deleteTableId=txt.getText().toString();

                        new clearTableInBranch().execute();
                    }
                });
            }
            else{
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
            showProgressBar(false);
        }
    }
    public class clearTableInBranch extends AsyncTask<String,String,String> {

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";


        protected void onPreExecute(){
            super.onPreExecute();
            showProgressBar(true);
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser=new JSONParser();

            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();
            jsonObjectData.add(new BasicNameValuePair("branchId",AppConfig.branchId));
            jsonObjectData.add(new BasicNameValuePair("tableId",AppConfig.tableId[AppConfig.deleteTableId_pos]));


            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfig.clear_tables,
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
                if(AppConfig.clear_a_table_key==true){
                    startActivity(new Intent(ClearTable.this,ViewCart.class));
                }else{
                    startActivity(getIntent());
                    finish();
                }

            }
            else{
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
            showProgressBar(false);
        }
    }

}
