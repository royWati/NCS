package com.example.roywati.ncs.cashier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.roywati.ncs.R;

/**
 * Created by root on 2/23/18.
 */

public class DiscountsAdapter extends BaseAdapter{

    String[] name,value;
    Context context;

    public static LayoutInflater inflater= null ;



    public DiscountsAdapter(Context context,String[] name,String[] value) {
        this.name=name;
        this.value=value;
        this.context=context;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {

        return name.length;
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

        view=inflater.inflate(R.layout.discount_items,null);
        TextView br_name=(TextView)view.findViewById(R.id.disc_name);
        TextView br_value=(TextView)view.findViewById(R.id.disc_value);

        br_name.setText(name[i]);
        br_value.setText(value[i]+"%");

        return view;
    }
}
