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
 * Created by Chris on 5/15/2017.
 */
public class AddOnMenuCategory extends AppCompatActivity {

    LinearLayout linearLayout;
    ProgressBar prog;
    GridView gridView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridlayout_menu_category);

        linearLayout=(LinearLayout)findViewById(R.id.linearItems);
        prog=(ProgressBar)findViewById(R.id.progressBarMenu);
        gridView=(GridView)findViewById(R.id.grid_view_cat);

        new getMenuCategory().execute();




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
    public class getMenuCategory extends AsyncTask<String,String,String> {

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";




        JSONArray MenuCategoryArray;



        protected void onPreExecute(){
            super.onPreExecute();
            showProgressBar(true);
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser=new JSONParser();

            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();
           jsonObjectData.add(new BasicNameValuePair("userId","1"));

            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfig.add_on_menu,
                    "GET",jsonObjectData);

            Log.d("data",jsonObjectResponse.toString());

            try{


                int success=jsonObjectResponse.getInt(TAG_SUCCESS);
                serverMessage=jsonObjectResponse.getString(TAG_MESSAGE);

                MenuCategoryArray=jsonObjectResponse.getJSONArray("Menu_category");







                Log.d("data",AppConfig.orderId);


                AppConfig.menuCategoryId=new String[MenuCategoryArray.length()];
                AppConfig.menuCategoryName=new String[MenuCategoryArray.length()];


                for(int i=0;i<MenuCategoryArray.length();i++){
                    // JSONObject jsonObject=MenuCategoryArray.getJSONObject(i);
                    JSONObject jsonObject=MenuCategoryArray.getJSONObject(i);

                    Log.d("menu category",jsonObject.toString());


                    AppConfig.menuCategoryId[i]=jsonObject.getString("menu_category_id");
                    AppConfig.menuCategoryName[i]=jsonObject.getString("menu_category_name");

                    Log.d("add on menu id", AppConfig.menuCategoryId[i]);
                    Log.d("add on menu name", AppConfig.menuCategoryName[i]);
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
                gridView.setAdapter(new MenuCategoryAdapter(AddOnMenuCategory.this,AppConfig.menuCategoryName,AppConfig.menuCategoryId));
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView textView=(TextView)view.findViewById(R.id.menuCategoryId);
                        TextView txtName=(TextView)view.findViewById(R.id.menuCategoryName);
                        AppConfig.menuCatId=textView.getText().toString();
                        AppConfig.menuCatName=txtName.getText().toString();

                        startActivity(new Intent(AddOnMenuCategory.this,MenuSubCategory.class));

                        Toast.makeText(getApplicationContext(), AppConfig.menuCatId,Toast.LENGTH_LONG).show();


                    }
                });

//                    Toast.makeText(getApplicationContext(),serverMessage+ AppConfig.orderId,Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
            showProgressBar(false);
        }
    }


}
