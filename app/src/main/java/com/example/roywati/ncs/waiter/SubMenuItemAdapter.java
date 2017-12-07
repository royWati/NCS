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
 * Created by Chris on 5/10/2017.
 */
public class SubMenuItemAdapter extends BaseAdapter {

    String[] sub_item_Name,sub_item_Id;

    Context context;
    public static LayoutInflater inflater=null;
    public SubMenuItemAdapter(Activity activity, String[]sub_item_Name, String[]sub_item_Id){
        this.sub_item_Id=sub_item_Id;
        this.sub_item_Name=sub_item_Name;
        context=activity;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return sub_item_Id.length;
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
        view=inflater.inflate(R.layout.custom_sub_menu_category,null);
        TextView id=(TextView)view.findViewById(R.id.subCategoryMenuId);
        TextView name=(TextView)view.findViewById(R.id.subCategoryMenuName);

        id.setText(sub_item_Id[i]);
        name.setText(sub_item_Name[i]);

        return view;
    }
}
