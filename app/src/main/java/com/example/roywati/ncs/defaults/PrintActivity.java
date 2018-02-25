package com.example.roywati.ncs.defaults;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class PrintActivity extends AppCompatActivity {
    ProgressBar bar;
    String header = "QTY     PRICE     AMOUNT\n";
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mmDevice;
    InputStream mmInputStream;
    OutputStream mmOutputStream;
    BluetoothSocket mmSocket;
    TextView myLabel;
    EditText myTextbox;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    Thread workerThread;

    public class getReceiptItems extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        JSONObject jsonObjectResponse;
        JSONArray receipt_items;
        String serverMessage = "request not sent";
        int successState = 0;

        protected void onPreExecute() {
            super.onPreExecute();
            PrintActivity.this.showProgress(true);
        }

        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser();
            List<NameValuePair> jsonObjectData = new ArrayList();
            if (PrintData.print_data_id == 2) {
                jsonObjectData.add(new BasicNameValuePair("orderId", AppConfigCashier.orderNumber));
                jsonObjectData.add(new BasicNameValuePair("userId", AppConfig.userId));
                this.jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfigCashier.print_bill_receipt, HttpGet.METHOD_NAME, jsonObjectData);
                Log.e("url", AppConfig.protocal + AppConfig.hostname + AppConfigCashier.print_bill_receipt);
                Log.e("orderId", AppConfigCashier.orderNumber);
                Log.d("data", this.jsonObjectResponse.toString());
            } else if (PrintData.print_data_id == 3) {
                jsonObjectData.add(new BasicNameValuePair("orderId", AppConfigCashier.orderNumber));
                this.jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfigCashier.print_receipt, HttpGet.METHOD_NAME, jsonObjectData);
                Log.e("url", AppConfig.protocal + AppConfig.hostname + AppConfigCashier.print_receipt);
                Log.e("orderId", AppConfigCashier.orderNumber);
                Log.d("data", this.jsonObjectResponse.toString());
            } else if (PrintData.print_data_id == 1) {
                jsonObjectData.add(new BasicNameValuePair("orderId", AppConfig.orderId));
                this.jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfigCashier.print_order_receipt, HttpGet.METHOD_NAME, jsonObjectData);
                Log.e("url", AppConfig.protocal + AppConfig.hostname + AppConfigCashier.print_order_receipt);
                Log.e("orderId", AppConfig.orderId);
                Log.d("data", this.jsonObjectResponse.toString());
            }
            try {
                int success = this.jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = this.jsonObjectResponse.getString(this.TAG_MESSAGE);
                this.receipt_items = this.jsonObjectResponse.getJSONArray("receipt_items");
                Log.d("data", AppConfigCashier.orderNumber);
                JSONObject js;
                int i;
                JSONObject jsonObject;
                if (PrintData.print_data_id == 1) {
                    PrintData.receipt_items = new String[this.receipt_items.length()];
                    PrintData.quantity = new String[this.receipt_items.length()];
                    PrintData.formattedData = new String[this.receipt_items.length()];
                    PrintData.receipt_items = new String[this.receipt_items.length()];
                    js = this.receipt_items.getJSONObject(0);
                    PrintData.BranchName = js.getString("branch_name");
                    PrintData.CashierName = js.getString("name");
                    for (i = 0; i < this.receipt_items.length(); i++) {
                        jsonObject = this.receipt_items.getJSONObject(i);
                        Log.d("receipts items", jsonObject.toString());
                        PrintData.quantity[i] = jsonObject.getString("quantinty");
                        PrintData.receipt_items[i] = jsonObject.getString("menu_item_name");
                        PrintData.formattedData[i] = PrintData.receipt_items[i] + "\n" + PrintData.quantity[i] + "     " + "     ";
                        Log.e("items", PrintData.receipt_items[i]);
                    }
                } else if (PrintData.print_data_id == 2) {
                    PrintData.receipt_items = new String[this.receipt_items.length()];
                    PrintData.quantity = new String[this.receipt_items.length()];
                    PrintData.menu_price = new String[this.receipt_items.length()];
                    PrintData.menu_item_value = new String[this.receipt_items.length()];
                    PrintData.formattedData = new String[this.receipt_items.length()];
                    PrintData.receipt_items = new String[this.receipt_items.length()];
                    js = this.receipt_items.getJSONObject(0);
                    PrintData.printSale = js.getString("amount");
                    PrintData.BranchName = js.getString("branch_name");
                    PrintData.CashierName = js.getString("name");
                    PrintData.CashoutTime = " ";
                    for (i = 0; i < this.receipt_items.length(); i++) {
                        jsonObject = this.receipt_items.getJSONObject(i);
                        Log.d("receipts items", jsonObject.toString());
                        PrintData.quantity[i] = jsonObject.getString("quantinty");
                        PrintData.menu_price[i] = jsonObject.getString("price");
                        PrintData.menu_item_value[i] = String.valueOf(Integer.parseInt(PrintData.quantity[i]) * Integer.parseInt(PrintData.menu_price[i]));
                        PrintData.printSale = String.valueOf(Integer.parseInt(PrintData.menu_item_value[i]) + Integer.parseInt(PrintData.printSale));
                        PrintData.receipt_items[i] = jsonObject.getString("menu_item_name");
                        PrintData.formattedData[i] = PrintData.receipt_items[i] + "\n" + PrintData.quantity[i] + "     " + "     \u0002" + PrintData.menu_price[i] + ".00/=  " + PrintData.menu_item_value[i] + ".00/=";
                        Log.e("items", PrintData.receipt_items[i]);
                    }
                } else if (PrintData.print_data_id == 3) {
                    PrintData.receipt_items = new String[this.receipt_items.length()];
                    PrintData.quantity = new String[this.receipt_items.length()];
                    PrintData.menu_price = new String[this.receipt_items.length()];
                    PrintData.menu_item_value = new String[this.receipt_items.length()];
                    PrintData.formattedData = new String[this.receipt_items.length()];
                    PrintData.receipt_items = new String[this.receipt_items.length()];
                    js = this.receipt_items.getJSONObject(0);
                    PrintData.TenderedAmount = js.getString("amount_given");
                    PrintData.printSale = js.getString("amount");
                    PrintData.BranchName = js.getString("branch_name");
                    PrintData.CashierName = js.getString("name");
                    PrintData.CashoutTime = js.getString("cashout_time");
                    PrintData.ChangeGiven = String.valueOf(Integer.parseInt(PrintData.TenderedAmount) -
                            Integer.parseInt(PrintData.printSale));


                    for (i = 0; i < this.receipt_items.length(); i++) {
                        jsonObject = this.receipt_items.getJSONObject(i);
                        Log.d("receipts items", jsonObject.toString());
                        PrintData.quantity[i] = jsonObject.getString("quantinty");
                        PrintData.menu_price[i] = jsonObject.getString("price");
                        PrintData.menu_item_value[i] = String.valueOf(Integer.parseInt(PrintData.quantity[i]) * Integer.parseInt(PrintData.menu_price[i]));
                        PrintData.receipt_items[i] = jsonObject.getString("menu_item_name");
                        PrintData.formattedData[i] = PrintData.receipt_items[i] + "\n" + PrintData.quantity[i] + "     " + "     " + PrintData.menu_price[i] + ".00/=  " + PrintData.menu_item_value[i] + ".00/=";
                        Log.e("items", PrintData.receipt_items[i]);
                    }
                }
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
            if (this.successState == 1) {
                Toast.makeText(PrintActivity.this.getApplicationContext(), this.serverMessage, 1).show();
            } else {
                Toast.makeText(PrintActivity.this.getApplicationContext(), this.serverMessage, 1).show();
            }
            PrintActivity.this.showProgress(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_print);
        Button openButton = (Button) findViewById(R.id.open);
        Button sendButton = (Button) findViewById(R.id.send);
        Button closeButton = (Button) findViewById(R.id.close);
        this.bar = (ProgressBar) findViewById(R.id.reprogress);
        new getReceiptItems().execute(new String[0]);
        this.myLabel = (TextView) findViewById(R.id.label);
        this.myTextbox = (EditText) findViewById(R.id.entry);
        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PrintData.TenderedAmount = "0";
                PrintData.printSale = "0";
                PrintData.ChangeGiven = "0";
                AppConfig.checkout_status = 0;
                Intent intent;
                if (PrintData.print_data_id == 1) {
                    intent = new Intent(PrintActivity.this, Homepage.class);
                    intent.setFlags(268468224);
                    PrintActivity.this.startActivity(intent);
                } else if (PrintData.print_data_id == 2) {
                    intent = new Intent(PrintActivity.this, CashierHomepage.class);
                    intent.setFlags(268468224);
                    PrintActivity.this.startActivity(intent);
                } else if (PrintData.print_data_id == 3) {
                    intent = new Intent(PrintActivity.this, CashierHomepage.class);
                    intent.setFlags(268468224);
                    PrintActivity.this.startActivity(intent);
                } else {
                    intent = new Intent(PrintActivity.this, LoginActivity.class);
                    intent.setFlags(268468224);
                    PrintActivity.this.startActivity(intent);
                }
            }
        });
        openButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    PrintActivity.this.findBT();
                    PrintActivity.this.openBT();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        sendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    PrintActivity.this.sendData();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        closeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    PrintActivity.this.closeBT();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void showProgress(boolean state) {
        if (state) {
            this.bar.setVisibility(0);
        } else {
            this.bar.setVisibility(8);
        }
    }

    void findBT() {
        try {
            this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (this.mBluetoothAdapter == null) {
                this.myLabel.setText("No bluetooth adapter available");
            }
            if (!this.mBluetoothAdapter.isEnabled()) {
                startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 0);
            }
            Set<BluetoothDevice> pairedDevices = this.mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals("Thermal Printer")) {
                        this.mmDevice = device;
                        break;
                    }
                }
            }
            this.myLabel.setText("Bluetooth device found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void openBT() throws IOException {
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            this.mmDevice = this.mBluetoothAdapter.getRemoteDevice(AppConfig.printMac);
            this.mmSocket = this.mmDevice.createRfcommSocketToServiceRecord(uuid);
            this.mmSocket.connect();
            this.mmOutputStream = this.mmSocket.getOutputStream();
            this.mmInputStream = this.mmSocket.getInputStream();
            beginListenForData();
            this.myLabel.setText("Bluetooth Opened");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();
            this.stopWorker = false;
            this.readBufferPosition = 0;
            this.readBuffer = new byte[1024];
            this.workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !PrintActivity.this.stopWorker) {
                        try {
                            int bytesAvailable = PrintActivity.this.mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                PrintActivity.this.mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == (byte) 10) {
                                        byte[] encodedBytes = new byte[PrintActivity.this.readBufferPosition];
                                        System.arraycopy(PrintActivity.this.readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        PrintActivity.this.readBufferPosition = 0;
                                        handler.post(new Runnable() {
                                            public void run() {
                                                PrintActivity.this.myLabel.setText(data);
                                            }
                                        });
                                    } else {
                                        byte[] bArr = PrintActivity.this.readBuffer;
                                        PrintActivity printActivity = PrintActivity.this;
                                        int i2 = printActivity.readBufferPosition;
                                        printActivity.readBufferPosition = i2 + 1;
                                        bArr[i2] = b;
                                    }
                                }
                            }
                        } catch (IOException e) {
                            PrintActivity.this.stopWorker = true;
                        }
                    }
                }
            });
            this.workerThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendData() throws IOException {
        try {
            String order;
            String Amounts;
            String msg;
            String[] items = new String[]{"one\n", "two\n", "three\n"};
            String timeOf = String.valueOf(new Date().toLocaleString());
            StringBuilder sb = new StringBuilder();
            for (String str : PrintData.formattedData) {
                sb.append(str + "\n");
            }
            String str2 = sb.toString();
            Log.e("items", str2);
            String habitnumber = "<b>Habit Number: </b><br>i am great for<br> who i am<br>thank you lord<br><br><br><br>";
            if (PrintData.print_data_id == 1) {
                order = AppConfig.orderId;
            } else {
                order = AppConfigCashier.orderNumber;
            }
            if (PrintData.print_data_id == 1) {
                Amounts = "\nWaiter's Name:" + PrintData.CashierName + "\n\n\n\n\n\n\n";
                msg = "       \u0002Nagalas Chakula \n       \u0002Branch :\t\t" + PrintData.BranchName + "\t\n" + "       \u0002Order id:" + order + "\n       \u0002" + timeOf + "\n\n\n";
            } else {
                msg = "       \u0002Nagalas Chakula \n       \u0002Branch :\t\t" + PrintData.BranchName + "\t\n" + "       \u0002Order id:" + order + "\n       \u0002" + PrintData.CashoutTime + "\n\n\n";
                Amounts = "Tendered Amount:     \u0002" + PrintData.TenderedAmount + ".00/=\n" + "Cash Sale:            \u0002" + String.valueOf(Integer.parseInt(PrintData.printSale)+Integer.parseInt(AppConfigCashier.order_discount_am)) + ".00/=\n"+
                        "Discount("+String.valueOf(AppConfigCashier.order_discount_perce)+"%):            \u0002" + AppConfigCashier.order_discount_am + ".00/=\n" +
                        "Grand Total:            \u0002" + PrintData.printSale + ".00/=\n" +
                        "Change:               \u0002" + PrintData.ChangeGiven + ".00/=\n\nServed by:\u0002" + PrintData.CashierName + "\n\nTHANK YOU\n\nTILL NO: 263664\n\n\n";
            }
            this.mmOutputStream.write((msg + this.header + str2 + "\n" + Amounts).getBytes());
            this.myLabel.setText("Data sent.");
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
    }

    void closeBT() throws IOException {
        try {
            this.stopWorker = true;
            this.mmOutputStream.close();
            this.mmInputStream.close();
            this.mmSocket.close();
            this.myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
