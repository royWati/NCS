package com.example.roywati.ncs.defaults;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roywati.ncs.R;
import com.example.roywati.ncs.cashier.AppConfigCashier;
import com.example.roywati.ncs.cashier.CashierHomepage;
import com.example.roywati.ncs.waiter.AppConfig;
import com.example.roywati.ncs.waiter.Homepage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PrintActivity extends AppCompatActivity {

    TextView myLabel;

    // will enable user to enter any text to be printed
    EditText myTextbox;

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    String header="QTY\u0020\u0020\u0020\u0020\u0020PRICE\u0020\u0020\u0020\u0020\u0020AMOUNT\n";

    ProgressBar bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        // we are going to have three buttons for specific functions
        Button openButton = (Button) findViewById(R.id.open);
        Button sendButton = (Button) findViewById(R.id.send);
        Button closeButton = (Button) findViewById(R.id.close);

        bar=(ProgressBar)findViewById(R.id.reprogress);

        new getReceiptItems().execute();
// text label and input box
        myLabel = (TextView) findViewById(R.id.label);
        myTextbox = (EditText) findViewById(R.id.entry);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PrintData.TenderedAmount="0";
                PrintData.printSale="0";
                PrintData.ChangeGiven="0";

                if(PrintData.print_data_id==1){
                    Intent intent=new Intent(PrintActivity.this, Homepage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else if(PrintData.print_data_id==2){
                    Intent intent=new Intent(PrintActivity.this, CashierHomepage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else if(PrintData.print_data_id==3){
                    Intent intent=new Intent(PrintActivity.this, CashierHomepage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else{
                    Intent intent=new Intent(PrintActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        });

        openButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    findBT();
                    openBT();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    sendData();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    closeBT();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        try {
            // more codes will be here
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgress(boolean state){
        if(state==true){
            bar.setVisibility(View.VISIBLE);
        }else{
            bar.setVisibility(View.GONE);
        }
    }

    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                myLabel.setText("No bluetooth adapter available");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("NP100SD486")) {
                        mmDevice = device;
                        break;
                    }
                }
            }

            myLabel.setText("Bluetooth device found.");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void openBT() throws IOException {
        try {
            TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String uuidId = tManager.getDeviceId();
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(UUID.fromString(uuidId));
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            myLabel.setText("Bluetooth Opened");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
 * after opening a connection to bluetooth printer device,
 * we have to listen and check if a data were sent to be printed.
 */
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // this will send text data to be printed by the bluetooth printer
    void sendData() throws IOException {
        try {


            String[] items={"one\n","two\n","three\n"};

           String timeOf=String.valueOf(new Date().toLocaleString());
            String Amounts;
            String msg;
            String order;
            String tryail = null;
            String str = null;
            StringBuilder sb = new StringBuilder();
            String line =null;

            for(int i=0;i<PrintData.formattedData.length;i++){

                sb.append(PrintData.formattedData[i] + "\n");


            }
            str = sb.toString();

            tryail=str;

            Log.e("items",tryail);

            String habitnumber = "<b>" + "Habit Number: " + "</b><br>i am great for<br> who i am<br>thank you lord<br><br><br><br>";
            //    myTextbox.setText(Html.fromHtml(habitnumber));

   //         myTextbox.setText(Html.fromHtml(habitnumber));
            // the text typed by the user
            //    String msg = myTextbox.getText().toString();

            if(PrintData.print_data_id==1){
                order=AppConfig.orderId;
            }else{
                order=AppConfigCashier.orderNumber;
            }





            if(PrintData.print_data_id==1){
                Amounts="\nWaiter's Name:"+PrintData.CashierName+"\n\n\n\n\n\n\n";

                msg = "\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0002Nagalas Chakula \n" +
                        "\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0002Branch code:\t\t"+PrintData.BranchName+"\t\n" +
                        "\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0002Order id:"+order+
                        "\n\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0002"+timeOf+"\n\n\n";
            }else{
                msg = "\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0002Nagalas Chakula \n" +
                        "\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0002Branch code:\t\t"+PrintData.BranchName+"\t\n" +
                        "\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0002Order id:"+order+
                        "\n\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0002"+PrintData.CashoutTime+"\n\n\n";
                Amounts="Tendered Amount:\u0020\u0020\u0020\u0020\u0020\u0002"+PrintData.TenderedAmount+".00/=\n"+
                        "Cash Sale:\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0002"+PrintData.printSale+".00/=\n"+
                        "Change:\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0002"+PrintData.ChangeGiven+
                        ".00/=\n\nServed by:\u0002"+PrintData.CashierName+"\n\nTHANK YOU\n\n\n\n\n";
            }



            String val=msg+header+str+"\n"+Amounts;

            mmOutputStream.write(val.getBytes());

            // tell the user data were sent
            myLabel.setText("Data sent.");

        } catch (Exception e) {
            e.printStackTrace();
        }catch (OutOfMemoryError error){
            error.printStackTrace();
        }
    }

    // close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class getReceiptItems extends AsyncTask<String,String,String> {

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";

        JSONArray receipt_items;
        JSONObject jsonObjectResponse;


        protected void onPreExecute(){
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser=new JSONParser();

            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();



            if(PrintData.print_data_id==2){
                jsonObjectData.add(new BasicNameValuePair("orderId",AppConfigCashier.orderNumber));
                jsonObjectData.add(new BasicNameValuePair("userId",AppConfig.userId));

                 jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+
                                AppConfig.hostname+ AppConfigCashier.print_bill_receipt,
                        "GET",jsonObjectData);

                Log.e("url",AppConfig.protocal+
                        AppConfig.hostname+ AppConfigCashier.print_bill_receipt);
                Log.e("orderId",AppConfigCashier.orderNumber);
                Log.d("data",jsonObjectResponse.toString());

            }

            else if(PrintData.print_data_id==3){
                jsonObjectData.add(new BasicNameValuePair("orderId",AppConfigCashier.orderNumber));

                 jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+
                                AppConfig.hostname+ AppConfigCashier.print_receipt,
                        "GET",jsonObjectData);

                Log.e("url",AppConfig.protocal+
                        AppConfig.hostname+ AppConfigCashier.print_receipt);
                Log.e("orderId",AppConfigCashier.orderNumber);
                Log.d("data",jsonObjectResponse.toString());

            }
            else if(PrintData.print_data_id==1){
                jsonObjectData.add(new BasicNameValuePair("orderId",AppConfig.orderId));

                jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+
                                AppConfig.hostname+ AppConfigCashier.print_order_receipt,
                        "GET",jsonObjectData);

                Log.e("url",AppConfig.protocal+
                        AppConfig.hostname+ AppConfigCashier.print_order_receipt);
                Log.e("orderId",AppConfig.orderId);
                Log.d("data",jsonObjectResponse.toString());


            }




            try{


                int success=jsonObjectResponse.getInt(TAG_SUCCESS);
                serverMessage=jsonObjectResponse.getString(TAG_MESSAGE);

                receipt_items=jsonObjectResponse.getJSONArray("receipt_items");

                Log.d("data",AppConfigCashier.orderNumber);

                if(PrintData.print_data_id==1){
                    PrintData.receipt_items=new String[receipt_items.length()];
                    PrintData.quantity=new String[receipt_items.length()];
                    PrintData.formattedData=new String[receipt_items.length()];
                    PrintData.receipt_items=new String[receipt_items.length()];

                    JSONObject js=receipt_items.getJSONObject(0);
                    PrintData.BranchName=js.getString("branch_name");
                    PrintData.CashierName=js.getString("name");

                    for(int i=0;i<receipt_items.length();i++){
                        // JSONObject jsonObject=MenuCategoryArray.getJSONObject(i);
                        JSONObject jsonObject=receipt_items.getJSONObject(i);

                        Log.d("receipts items",jsonObject.toString());

                        PrintData.quantity[i]=jsonObject.getString("quantinty");
                    //    PrintData.receipt_items[i]=jsonObject.getString("price");

                 //       PrintData.menu_item_value[i]=String.valueOf(Integer.parseInt(PrintData.quantity[i])*Integer.parseInt(PrintData.menu_price[i]));

                        PrintData.receipt_items[i]=jsonObject.getString("menu_item_name");



                        PrintData.formattedData[i]=PrintData.receipt_items[i]+"\n"+PrintData.quantity[i]+"\u0020\u0020\u0020\u0020\u0020" +
                                "\u0020\u0020\u0020\u0020\u0020";


                        Log.e("items", PrintData.receipt_items[i]);
                    }



                }else if(PrintData.print_data_id==2){

                    PrintData.receipt_items=new String[receipt_items.length()];
                    //   PrintData.receipt_amount=new String[receipt_items.length()];

                    PrintData.quantity=new String[receipt_items.length()];
                    PrintData.menu_price=new String[receipt_items.length()];
                    PrintData.menu_item_value=new String[receipt_items.length()];
                    PrintData.formattedData=new String[receipt_items.length()];
                    PrintData.receipt_items=new String[receipt_items.length()];


                    JSONObject js=receipt_items.getJSONObject(0);

                //    PrintData.TenderedAmount=js.getString("amount_given");
                    PrintData.printSale=js.getString("amount");
                    PrintData.BranchName=js.getString("branch_name");
                    PrintData.CashierName=js.getString("name");
                    PrintData.CashoutTime="TILL NO: 574986";


            //        PrintData.ChangeGiven=String.valueOf(Integer.parseInt(PrintData.TenderedAmount)-Integer.parseInt(PrintData.printSale));



                    for(int i=0;i<receipt_items.length();i++){
                        // JSONObject jsonObject=MenuCategoryArray.getJSONObject(i);
                        JSONObject jsonObject=receipt_items.getJSONObject(i);

                        Log.d("receipts items",jsonObject.toString());

                        PrintData.quantity[i]=jsonObject.getString("quantinty");
                        PrintData.menu_price[i]=jsonObject.getString("price");

                        PrintData.menu_item_value[i]=String.valueOf(Integer.parseInt(PrintData.quantity[i])*Integer.parseInt(PrintData.menu_price[i]));

                        PrintData.printSale=String.valueOf(Integer.parseInt(PrintData.menu_item_value[i])+Integer.parseInt(PrintData.printSale));

                        PrintData.receipt_items[i]=jsonObject.getString("menu_item_name");



                        PrintData.formattedData[i]=PrintData.receipt_items[i]+"\n"+PrintData.quantity[i]+"\u0020\u0020\u0020\u0020\u0020" +
                                "\u0020\u0020\u0020\u0020\u0020\u0002"+PrintData.menu_price[i]+
                                ".00/=\u0020\u0020"+PrintData.menu_item_value[i]+".00/=";


                        Log.e("items", PrintData.receipt_items[i]);
                    }


                }else if(PrintData.print_data_id==3){
                    PrintData.receipt_items=new String[receipt_items.length()];
                    //   PrintData.receipt_amount=new String[receipt_items.length()];

                    PrintData.quantity=new String[receipt_items.length()];
                    PrintData.menu_price=new String[receipt_items.length()];
                    PrintData.menu_item_value=new String[receipt_items.length()];
                    PrintData.formattedData=new String[receipt_items.length()];
                    PrintData.receipt_items=new String[receipt_items.length()];


                    JSONObject js=receipt_items.getJSONObject(0);

                    PrintData.TenderedAmount=js.getString("amount_given");
                    PrintData.printSale=js.getString("amount");
                    PrintData.BranchName=js.getString("branch_name");
                    PrintData.CashierName=js.getString("name");
                    PrintData.CashoutTime=js.getString("cashout_time");


                    PrintData.ChangeGiven=String.valueOf(Integer.parseInt(PrintData.TenderedAmount)-Integer.parseInt(PrintData.printSale));



                    for(int i=0;i<receipt_items.length();i++){
                        // JSONObject jsonObject=MenuCategoryArray.getJSONObject(i);
                        JSONObject jsonObject=receipt_items.getJSONObject(i);

                        Log.d("receipts items",jsonObject.toString());

                        PrintData.quantity[i]=jsonObject.getString("quantinty");
                        PrintData.menu_price[i]=jsonObject.getString("price");

                        PrintData.menu_item_value[i]=String.valueOf(Integer.parseInt(PrintData.quantity[i])*Integer.parseInt(PrintData.menu_price[i]));

                        PrintData.receipt_items[i]=jsonObject.getString("menu_item_name");



                        PrintData.formattedData[i]=PrintData.receipt_items[i]+"\n"+PrintData.quantity[i]+"\u0020\u0020\u0020\u0020\u0020" +
                                "\u0020\u0020\u0020\u0020\u0020"+PrintData.menu_price[i]+
                                ".00/=\u0020\u0020"+PrintData.menu_item_value[i]+".00/=";


                        Log.e("items", PrintData.receipt_items[i]);
                }







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
          //      listView.setAdapter(new MenuItemSelectedAdapter(MenuItemState.this,AppConfigKitchen.menuitemname,AppConfigKitchen.quantity));
              Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
            showProgress(false);
        }
    }
}
