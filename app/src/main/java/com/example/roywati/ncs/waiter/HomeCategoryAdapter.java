package com.example.roywati.ncs.waiter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.roywati.ncs.R;

public class HomeCategoryAdapter extends BaseAdapter {
    public static LayoutInflater inflater = null;
    Context context;
    int[] images;
    String[] itemId;
    String[] itemName;

    public HomeCategoryAdapter(Homepage menuActivity, String[] itemId, String[] itemName, int[] images) {
        this.itemId = itemId;
        this.images = images;
        this.itemName = itemName;
        this.context = menuActivity;
        inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.itemId.length;
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_gridview, null);
        TextView id = (TextView) view.findViewById(R.id.menu_category_id);
        TextView name = (TextView) view.findViewById(R.id.menu_name);
        ((ImageView) view.findViewById(R.id.menu_action_image)).setImageResource(this.images[position]);
        id.setText(this.itemId[position]);
        name.setText(this.itemName[position]);
        return view;
    }
}
