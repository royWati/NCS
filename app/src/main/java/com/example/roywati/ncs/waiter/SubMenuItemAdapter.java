package com.example.roywati.ncs.waiter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.roywati.ncs.R;

public class SubMenuItemAdapter extends BaseAdapter {
    public static LayoutInflater inflater = null;
    Context context;
    String[] sub_item_Id;
    String[] sub_item_Name;

    public SubMenuItemAdapter(Activity activity, String[] sub_item_Name, String[] sub_item_Id) {
        this.sub_item_Id = sub_item_Id;
        this.sub_item_Name = sub_item_Name;
        this.context = activity;
        inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.sub_item_Id.length;
    }

    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_sub_menu_category, null);
        TextView name = (TextView) view.findViewById(R.id.subCategoryMenuName);
        ((TextView) view.findViewById(R.id.subCategoryMenuId)).setText(this.sub_item_Id[i]);
        name.setText(this.sub_item_Name[i]);
        return view;
    }
}
