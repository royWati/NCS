package com.example.roywati.ncs.waiter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.roywati.ncs.R;

/**
 * Created by Chris on 5/16/2017.
 */
public class viewOrderAdapter extends BaseAdapter {
    String[] order,table,status,checkout;
    Context context;
    public static LayoutInflater inflater=null;
    public viewOrderAdapter(Activity activity, String[] order, String[] table, String[] status, String[] checkout){

        this.order=order;
        this.table=table;
        this.status=status;
        this.checkout=checkout;
        context=activity;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return order.length;
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
        view=inflater.inflate(R.layout.custom_view_waiter_order,null);
        TextView id=(TextView)view.findViewById(R.id.order_no);
        TextView tbl=(TextView)view.findViewById(R.id.table_view_orders);
        TextView sts=(TextView)view.findViewById(R.id.view_order_status);
        TextView check=(TextView)view.findViewById(R.id.checkout_time);

        id.setText(order[i]);
        tbl.setText(table[i]);
        sts.setText(status[i]);
        check.setText(checkout[i]);

        return view;
    }
}
