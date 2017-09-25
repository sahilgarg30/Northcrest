package com.ananyagupta.northcrest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Sahil on 24-09-2017.
 */

public class HistoryAdapter extends BaseAdapter {
    private Context context;

    public HistoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.history_listitem, parent, false);
        }
        TextView customerNameTv = (TextView)
                convertView.findViewById(R.id.textView2);
        TextView typeTv = (TextView)
                convertView.findViewById(R.id.textView13);
        TextView costTv = (TextView)
                convertView.findViewById(R.id.textView15);
        return convertView;
    }
}
