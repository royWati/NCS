package com.example.roywati.ncs.kitchen;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roywati.ncs.R;

/**
 * Created by Chris on 5/14/2017.
 */
public class ProcessedAdapter extends BaseAdapter {

    String[] orderId,tableName;
    Context context;
    LayoutInflater inflater;

    public ProcessedAdapter(Activity activity, String[] orderId, String[] tableName){
        this.orderId=orderId;
        this.tableName=tableName;
        context=activity;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return orderId.length;
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
        view=inflater.inflate(R.layout.custom_processed_item,null);
        TextView order=(TextView)view.findViewById(R.id.order_item);
        TextView table=(TextView)view.findViewById(R.id.table_name);
        ImageView imageView=(ImageView)view.findViewById(R.id.processed);


        order.setText(orderId[i]);
        table.setText(tableName[i]);
        return view;
    }
}
