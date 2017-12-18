package com.example.roywati.ncs.waiter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.roywati.ncs.R;

public class MenuItemsAdapter extends Adapter<MenuItemsAdapter.ListViewHolder> {
    Context context;
    LayoutInflater inflater = LayoutInflater.from(this.context);
    String[] itemDescr;
    String[] itemId;
    String[] itemName;
    String[] itemPrice;

    public class ListViewHolder extends ViewHolder {
        ImageView cart_item;
        TextView item_descr;
        TextView item_price;
        TextView menuItemId;
        TextView menuItemName;

        public ListViewHolder(View itemView) {
            super(itemView);
            this.menuItemName = (TextView) itemView.findViewById(R.id.menu_cart_item_name);
            this.menuItemId = (TextView) itemView.findViewById(R.id.menuitemcart_id);
            this.item_descr = (TextView) itemView.findViewById(R.id.item_Descr_info);
            this.item_price = (TextView) itemView.findViewById(R.id.item_price);
            this.cart_item = (ImageView) itemView.findViewById(R.id.add_menu_item_cart);
        }
    }

    public MenuItemsAdapter(Activity activity, String[] itemName, String[] itemId, String[] itemPrice, String[] itemDescr) {
        this.itemId = itemId;
        this.itemPrice = itemPrice;
        this.itemName = itemName;
        this.itemDescr = itemDescr;
        this.context = activity;
    }

    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(this.inflater.inflate(R.layout.custom_menu_items, parent, false));
    }

    public void onBindViewHolder(final ListViewHolder holder, int position) {
        holder.menuItemId.setText(this.itemId[position]);
        holder.item_descr.setText(this.itemDescr[position]);
        holder.menuItemName.setText(this.itemName[position]);
        holder.item_price.setText(this.itemPrice[position]);
        holder.cart_item.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AppConfig.menuCatId = holder.menuItemId.getText().toString();
                Toast.makeText(MenuItemsAdapter.this.context, AppConfig.menuCatId, 0).show();
            }
        });
    }

    public int getItemCount() {
        return this.itemId.length;
    }
}
