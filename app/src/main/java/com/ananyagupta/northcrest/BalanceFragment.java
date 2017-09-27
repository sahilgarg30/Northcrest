package com.ananyagupta.northcrest;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceFragment extends Fragment {


    private HomeActivity mHomePageActivity;
    private MyHelper mMyHelper;
    private SQLiteDatabase mdB;
    private TextView mBalanceTv;

    public BalanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.fragment_balance, container, false);
        mHomePageActivity = (HomeActivity) getActivity();
        mHomePageActivity.getSupportActionBar().setTitle("Balance");
        mMyHelper = new MyHelper(mHomePageActivity,"USERSDB",null,1);
        mdB = mMyHelper.getReadableDatabase();
        mBalanceTv = (TextView)view.findViewById(R.id.balance_tv);
        String args[]={FirebaseAuth.getInstance().getCurrentUser().getEmail()};
        Cursor c = mdB.query("users",null,"email=?",args,null,null,null);
        if(c.moveToNext())
        mBalanceTv.setText(String.valueOf(c.getDouble(7)));
        return view;
    }
}
