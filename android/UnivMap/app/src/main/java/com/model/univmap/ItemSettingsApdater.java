package com.model.univmap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cherrierlaoussing.univmap.R;

import java.util.List;

public class ItemSettingsApdater extends BaseAdapter {

    private Context context;
    private List<Object> list;
    private static final int HEADER = 0;
    private static final int ITEM_SETTINGS = 1;
    private LayoutInflater inflater;

    public ItemSettingsApdater(Context context, List<Object> list){
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position) instanceof ItemSettings){
            return ITEM_SETTINGS;
        }else return HEADER;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            switch (getItemViewType(position)){
                case ITEM_SETTINGS:
                    convertView = inflater.inflate(R.layout.item_setting_liste, null);
                    break;
                case HEADER:
                    convertView = inflater.inflate(R.layout.header_list_settings, null);
                    break;

            }
        }

        switch (getItemViewType(position)){
            case ITEM_SETTINGS:
                ItemSettings currentItem = (ItemSettings) getItem(position);
                String nameItem = currentItem.getNameItem();
                TextView itemView = convertView.findViewById(R.id.textView);
                itemView.setText(nameItem);
                break;
            case HEADER:
                String headerName = (String) getItem(position);
                TextView headerView = convertView.findViewById(R.id.headerList);
                headerView.setText(headerName);
                break;
        }



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return convertView;
    }
}
