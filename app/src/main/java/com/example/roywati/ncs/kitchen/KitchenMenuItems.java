package com.example.roywati.ncs.kitchen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.waiter.AppConfig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 5/13/2017.
 */
public class KitchenMenuItems extends AppCompatActivity {

    GridView gridView;
    RelativeLayout rela;
    ProgressBar bar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kitchen_homepage);
        setTitle(AppConfigKitchen.titleProcess);
        gridView=(GridView)findViewById(R.id.homepage_kitchen_gridview);

        new ViewOrderStatusType().execute();
        bar=(ProgressBar)findViewById(R.id.progressBar);
        rela=(RelativeLayout)findViewById(R.id.relativeLayout);


    }

    public  void showProgress(boolean state){
        if(state==true){
            bar.setVisibility(View.VISIBLE);
            bar.setVisibility(View.VISIBLE);
            bar.setVisibility(View.VISIBLE);
        }else{

        }
    }
public class ViewOrderStatusType extends AsyncTask<String,String,String>{
    int successState=0;
    String serverMessage="request not sent";

    String TAG_MESSAGE="message";
    String TAG_SUCCESS="success";





    JSONArray order_status;

    protected void onPreExecute(){
        super.onPreExecute();
//        showProgress(true);
    }


    @Override
    protected String doInBackground(String... strings) {

        JSONParser jsonParser=new JSONParser();
        List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();
        jsonObjectData.add(new BasicNameValuePair("homepageId", AppConfigKitchen.homepageId));
        jsonObjectData.add(new BasicNameValuePair("branchId", AppConfig.branchId));

        Log.d("kitchen homepage id",AppConfigKitchen.homepageId);


        JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfigKitchen.get_process,
                "GET",jsonObjectData);

        Log.d("data sub category",jsonObjectResponse.toString());


        try{
            int success=jsonObjectResponse.getInt(TAG_SUCCESS);
            serverMessage=jsonObjectResponse.getString(TAG_MESSAGE);

            order_status=jsonObjectResponse.getJSONArray("order_status");

            AppConfigKitchen.orders=new String[order_status.length()];
            AppConfigKitchen.tables=new String[order_status.length()];

            for(int i=0;i<order_status.length();i++){
                // JSONObject jsonObject=MenuCategoryArray.getJSONObject(i);
                JSONObject jsonObject=order_status.getJSONObject(i);

                Log.d("status y",jsonObject.toString());


                AppConfigKitchen.orders[i]=jsonObject.getString("order_id");
                AppConfigKitchen.tables[i]=jsonObject.getString("table_name");

                Log.d("process statement order", AppConfigKitchen.orders[i]);
                Log.d("process statement table", AppConfigKitchen.tables[i]);


                if(success==1){
                    successState=1;
                }
            }



        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String s){
        super.onPostExecute(s);

        Log.d("successState",String.valueOf(successState));
        if(successState==1) {

            AppConfigKitchen.num=Integer.parseInt(AppConfigKitchen.homepageId);

            if( AppConfigKitchen.num==5){
                gridView.setAdapter(new PendingAdapter(KitchenMenuItems.this,AppConfigKitchen.orders,AppConfigKitchen.tables));

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView txt=(TextView)view.findViewById(R.id.order_item_no_pending);

                        AppConfigKitchen.titleProcess="pending items";
                        AppConfigKitchen.selectedKitchenOrder=txt.getText().toString();
                        Log.d("selected order item: ",AppConfigKitchen.selectedKitchenOrder);
                        startActivity(new Intent(KitchenMenuItems.this,MenuItemState.class));


                    }
                });

            }else if(AppConfigKitchen.num==6){
                gridView.setAdapter(new ProcessingAdapter(KitchenMenuItems.this,AppConfigKitchen.orders,AppConfigKitchen.tables));

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView txt=(TextView)view.findViewById(R.id.order_item_id);

                        AppConfigKitchen.titleProcess="Processing items";
                        AppConfigKitchen.selectedKitchenOrder=txt.getText().toString();
                        Log.d("selected order item: ",AppConfigKitchen.selectedKitchenOrder);
                        startActivity(new Intent(KitchenMenuItems.this,MenuItemState.class));
                    }
                });
            }else if(AppConfigKitchen.num==7){
                gridView.setAdapter(new ProcessedAdapter(KitchenMenuItems.this,AppConfigKitchen.orders,AppConfigKitchen.tables));
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView txt=(TextView)view.findViewById(R.id.order_item);

                        AppConfigKitchen.titleProcess="Processed items";
                        AppConfigKitchen.selectedKitchenOrder=txt.getText().toString();
                        Log.d("selected order item: ",AppConfigKitchen.selectedKitchenOrder);
                        startActivity(new Intent(KitchenMenuItems.this,MenuItemState.class));
                    }
                });
            }

            Toast.makeText(getApplicationContext(), serverMessage, Toast.LENGTH_LONG).show();

    }
         else{
            Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
        }
  //      showProgress(false);
    }
}

}
