package com.ananyagupta.northcrest;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {


    private HomeActivity mHomePageActivity;
    private ListView mListView;
    private LinearLayout mHistoryEmpty;
    private HistoryAdapter mHistoryAdapter;
    private MyHelper mMyHelper;
    private SQLiteDatabase mdB;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        mHomePageActivity = (HomeActivity) getActivity();
        mHomePageActivity.getSupportActionBar().setTitle("Transaction History");
        mListView = (ListView) view.findViewById(R.id.history_lv);
        mMyHelper = new MyHelper(mHomePageActivity,"USERSDB",null,1);
        mdB = mMyHelper.getReadableDatabase();
        Cursor c = mdB.query("trans",null,"email=?",new String[]{FirebaseAuth.getInstance().getCurrentUser().getEmail()},null,null,"_id"+" DESC");
        mHistoryAdapter =  new HistoryAdapter(mHomePageActivity,c,0);
        mListView.setAdapter(mHistoryAdapter);
        mHistoryEmpty = (LinearLayout) view.findViewById(R.id.history_empty);
        mListView.setEmptyView(mHistoryEmpty);
        return view;
    }

    public void changeData() {
        mListView.setAdapter(null);
        Cursor c = mdB.query("trans",null,"email=?",new String[]{FirebaseAuth.getInstance().getCurrentUser().getEmail()},null,null,"_id"+" DESC");
        mHistoryAdapter =  new HistoryAdapter(mHomePageActivity,c,0);
        mListView.setAdapter(mHistoryAdapter);
    }
}
