package com.example.roywati.ncs.waiter;

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
 * Created by Chris on 5/8/2017.
 */
public class MenuCategoryAdapter extends BaseAdapter {
    String[] menuCatName,menuCatId;

    Context context;
    public static LayoutInflater inflater=null;

    public MenuCategoryAdapter(Activity activity,String[]menuCatName,String[]menuCatId){
        this.menuCatName=menuCatName;
        this.menuCatId=menuCatId;
        context=activity;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return menuCatId.length;
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

        view=inflater.inflate(R.layout.custom_menu_category,null);
        TextView id=(TextView)view.findViewById(R.id.menuCategoryId);
        TextView name=(TextView)view.findViewById(R.id.menuCategoryName);
        LinearLayout layout=(LinearLayout)view.findViewById(R.id.lau);

        id.setText(menuCatId[i]);
        name.setText(menuCatName[i]);

        if(i%2==0){
            layout.setBackgroundColor(Color.parseColor("#c69577"));
        //    price.setTextColor(Color.parseColor("#542400"));
        }

        return view;
    }
}
