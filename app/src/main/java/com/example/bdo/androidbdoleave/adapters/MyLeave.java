package com.example.bdo.androidbdoleave.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.bdo.androidbdoleave.R;
import java.util.ArrayList;

/**
 * Created by suhe on 17/02/16.
 */
public class MyLeave extends BaseAdapter {
    private ArrayList<Leav> listData;
    private LayoutInflater layoutInflater;

    public MyLeave(Context context, ArrayList listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null) {
            view = layoutInflater.inflate(R.layout.myleave_rows,null);
            holder = new ViewHolder();
            holder.date_from = (TextView) view.findViewById(R.id.date_from);
            holder.description = (TextView) view.findViewById(R.id.description);
            holder.status =  (TextView) view.findViewById(R.id.status);
            holder.imageView = (ImageView) view.findViewById(R.id.thumbImage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        MyLeaveListItem listItem = listData.get(i);
        holder.date_from.setText(listItem.getDateFrom());
        holder.status.setText(listItem.getStatus());
        holder.status.setText(listItem.getDescription());
        return view;
    }

    static class ViewHolder {
        TextView date_from;
        TextView description;
        TextView status;
        ImageView imageView;
    }
}
