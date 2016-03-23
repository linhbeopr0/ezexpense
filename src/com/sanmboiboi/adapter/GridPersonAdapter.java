package com.sanmboiboi.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mcexpense.R;
import com.sanmboiboi.activity.AddExpenseActivity;
import com.sanmboiboi.activity.AddPersonActivity;

public class GridPersonAdapter extends ArrayAdapter<String> {
    private final Context mContext;
    private final int layout;
    private final ArrayList<String> data;
    private Holder holder;

    public GridPersonAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        mContext = context;
        layout = resource;
        data = objects;
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        Log.d("LINH", "GridPersonAdapter|data.size = " + data.size());
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = convertView;

        if (row == null) {
            holder = new Holder();
            row = inflater.inflate(layout, null);
            holder.txtView = (TextView) row.findViewById(R.id.addExp_grid_txtName);
            holder.imgView = (ImageView) row.findViewById(R.id.addExp_deleteBtn);
            row.setTag(holder);
        } else {
            holder = (Holder) row.getTag();
        }
        Log.d("LINH", "GridPersonAdapter|name = " + data.get(position));
        holder.txtView.setText(data.get(position));

        return row;
    }

    private static class Holder {
        TextView txtView;
        ImageView imgView;
    }

}
