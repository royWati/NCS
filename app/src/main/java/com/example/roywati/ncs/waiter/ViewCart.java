package com.example.roywati.ncs.waiter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.PrintActivity;
import com.example.roywati.ncs.defaults.PrintData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roy Wati on 5/11/2017.
 */
public class ViewCart extends AppCompatActivity{

   public static ListView listView;
    Button checkout_cart;
    public static TextView txtAmount;
    public static TextView order;
    public static TextView holder;
    public static ProgressBar prog_v;
    Spinner spinner;

    Button clearTable;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("view cart Order No."+AppConfig.orderId);

        setContentView(R.layout.view_cart_design);

        listView=(ListView)findViewById(R.id.list_view_cart);
        checkout_cart=(Button)findViewById(R.id.btnCheckout);
        txtAmount=(TextView)findViewById(R.id.TOTAL_amount);
        holder=(TextView)findViewById(R.id.holder);

        clearTable=(Button)findViewById(R.id.clear_a_table);

        prog_v=(ProgressBar)findViewById(R.id.progressBar_view_cart);

        spinner=(Spinner)findViewById(R.id.spinner_view_cart);

        clearTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConfig.clear_a_table_key=true;

                startActivity(new Intent(ViewCart.this,ClearTable.class));
            }
        });

       // new GetViewCart(ViewCart.this,listView,txtAmount,order,holder,prog_v);

        new view_Cart().execute();

    }

    public void showProgressBar(boolean state){
        if(state==true){
            prog_v.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            txtAmount.setVisibility(View.GONE);
            holder.setVisibility(View.GONE);
            checkout_cart.setVisibility(View.GONE);
            clearTable.setVisibility(View.GONE);
        }else{
            prog_v.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            txtAmount.setVisibility(View.VISIBLE);
            holder.setVisibility(View.VISIBLE);
            clearTable.setVisibility(View.VISIBLE);
            checkout_cart.setVisibility(View.VISIBLE);
        }
    }

    public class view_Cart extends AsyncTask<String,String,String>{

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";

        String total;



        JSONArray cartItems,Tables;

        protected void onPreExecute(){
            super.onPreExecute();
           showProgressBar(true);
        }


        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser=new JSONParser();

            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();
            jsonObjectData.add(new BasicNameValuePair("orderId",AppConfig.orderId));
            jsonObjectData.add(new BasicNameValuePair("branchId",AppConfig.branchId));

            Log.d("menuSubCatId",AppConfig.orderId);
            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfig.view_cart,
                    "GET",jsonObjectData);

            Log.d("data sub category",jsonObjectResponse.toString());

            try{
                int success=jsonObjectResponse.getInt(TAG_SUCCESS);
                serverMessage=jsonObjectResponse.getString(TAG_MESSAGE);
                AppConfig.total_value_in_cart=jsonObjectResponse.getString("total");

                Log.d("total value of items", AppConfig.total_value_in_cart);


                cartItems=jsonObjectResponse.getJSONArray("cart_items");
                Tables=jsonObjectResponse.getJSONArray("tables");


                    Log.d("data",AppConfig.orderId);
                    AppConfig.viewCartItemId=new String[cartItems.length()];
                    AppConfig.viewCartItemName=new String[cartItems.length()];
                       AppConfig.viewCartMenuItemId=new String[cartItems.length()];
                     AppConfig.viewCartItemPrice=new String[cartItems.length()];



                AppConfig.tableId=new String[Tables.length()];
                AppConfig.tableName=new String[Tables.length()];


                for(int i=0;i<cartItems.length();i++){
                        // JSONObject jsonObject=MenuCategoryArray.getJSONObject(i);
                        JSONObject jsonObject=cartItems.getJSONObject(i);

                        Log.d("menu sub category",jsonObject.toString());


                        AppConfig.viewCartItemId[i]=jsonObject.getString("order_items_id");
                        AppConfig.viewCartItemName[i]=jsonObject.getString("menu_item_name");
                        AppConfig.viewCartMenuItemId[i]=jsonObject.getString("menu_item_id");
                        AppConfig.viewCartItemPrice[i]=jsonObject.getString("price");

                        Log.d("view cart id", AppConfig.viewCartItemId[i]);
                        Log.d("view cart name", AppConfig.viewCartItemName[i]);
                        Log.d("menu item id", AppConfig.viewCartMenuItemId[i]);
                        Log.d("price", AppConfig.viewCartItemPrice[i]);



                    }
                for(int i=0;i<Tables.length();i++){
                    // JSONObject jsonObject=MenuCategoryArray.getJSONObject(i);
                    JSONObject jsonObject=Tables.getJSONObject(i);

                    Log.d("tables ",jsonObject.toString());


                    AppConfig.tableId[i]=jsonObject.getString("table_id");
                    AppConfig.tableName[i]=jsonObject.getString("table_name");


                    Log.d("table id", AppConfig.tableId[i]);
                    Log.d("table name", AppConfig.tableName[i]);




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

            Log.d("successState",String.valueOf(successState));

            Log.d("total amount",AppConfig.total_value_in_cart);

            txtAmount.setText(AppConfig.total_value_in_cart);

            if(successState==1){
                listView.setAdapter(new ViewCartAdapter(ViewCart.this,AppConfig.viewCartItemName,AppConfig.viewCartItemId,AppConfig.viewCartItemPrice));

                ArrayAdapter adapter=new ArrayAdapter(ViewCart.this,android.R.layout.simple_spinner_dropdown_item,AppConfig.tableName);



                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        int pos= (int) adapterView.getItemIdAtPosition(i);
                        AppConfig.tableId_selected=AppConfig.tableId[pos];

                        Toast.makeText(getApplicationContext(),"table selected:"+AppConfig.tableId_selected,Toast.LENGTH_SHORT).show();
                    }



                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                checkout_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new checkoutCart().execute();
                    }
                });
             //   txtAmount.setText(AppConfig.total_value_in_cart);

                //  startActivity(new Intent(MenuSubCategory.this,SubMenuItems.class));
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();

            }
            else{

                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
//            order.setText("OrderNo:"+AppConfig.orderId);
            showProgressBar(false);
        }
    }
    public class checkoutCart extends AsyncTask<String,String,String> {

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";

        String total;




        protected void onPreExecute(){
            super.onPreExecute();
               showProgressBar(true);

        }

        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser=new JSONParser();

            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();
            jsonObjectData.add(new BasicNameValuePair("orderId",AppConfig.orderId));
            jsonObjectData.add(new BasicNameValuePair("tableId",AppConfig.tableId_selected));
            jsonObjectData.add(new BasicNameValuePair("userId",AppConfig.userId));

            // Log.d("menuSubCatId",AppConfig.orderId);
            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfig.checkout_cart,
                    "GET",jsonObjectData);

            Log.d("data sub category",jsonObjectResponse.toString());

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

            Log.d("successState",AppConfig.total_value_in_cart);

            if(successState==1){

                PrintData.print_data_id=1;
                AppConfig.clear_a_table_key=false;
                startActivity(new Intent(ViewCart.this,PrintActivity.class));
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();

            }
            else{

                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
            showProgressBar(false);

        }
    }



}
