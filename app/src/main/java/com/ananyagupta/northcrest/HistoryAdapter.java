package com.ananyagupta.northcrest;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Sahil on 24-09-2017.
 */

public class HistoryAdapter extends CursorAdapter {

    public HistoryAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).
                inflate(R.layout.history_listitem, parent, false);
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {
        TextView customerNameTv = (TextView)
                convertView.findViewById(R.id.textView2);
        TextView typeTv = (TextView)
                convertView.findViewById(R.id.textView13);
        TextView costTv = (TextView)
                convertView.findViewById(R.id.textView15);
        String name = cursor.getString(2);
        String cost = cursor.getString(3);
        String type = cursor.getString(4);

        if(name.isEmpty())
            customerNameTv.setText("Unknown customer");
        else
            customerNameTv.setText(name);

        if(cost.isEmpty())
            costTv.setText("Rs. 0");
        else
            costTv.setText(""+cost);

        if(type.isEmpty())
            typeTv.setText("Unknown type");
        else
            typeTv.setText(type);
    }
}
