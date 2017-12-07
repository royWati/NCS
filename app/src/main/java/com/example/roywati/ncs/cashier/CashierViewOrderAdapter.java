package com.example.roywati.ncs.cashier;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.roywati.ncs.R;

/**
 * Created by Roy Wati on 5/17/2017.
 */

public class CashierViewOrderAdapter extends BaseAdapter {

    String[] itemName,quantity;
    Context context;
    public static LayoutInflater inflater=null;
    LinearLayout viewColor;

    public CashierViewOrderAdapter(Activity activity, String[]itemName, String[]quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
        context = activity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return itemName.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=inflater.inflate(R.layout.custom_unpaid_orders_layout,null);
        TextView id=(TextView)view.findViewById(R.id.order_num_cash_unpaid);
        TextView name=(TextView)view.findViewById(R.id.item_price_cash_unpaid);
        viewColor=(LinearLayout) view.findViewById(R.id.viewColor);
        id.setText(itemName[i]);
        name.setText(quantity[i]);

        if(i%2==0)
        {
            viewColor.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        return view;
    }
}
