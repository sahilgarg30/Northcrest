package com.ananyagupta.northcrest;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class RewardsFragment extends Fragment {


    private HomeActivity mHomePageActivity;
    private Button mRedeemPointsButton;
    private MyHelper mMyHelper;
    private SQLiteDatabase mdB;
    private TextView mRewardsTv;
    private double balance;
    private double rewards;


    public RewardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_rewards, container, false);
        mHomePageActivity = (HomeActivity) getActivity();
        mRedeemPointsButton = (Button) view.findViewById(R.id.custom_redeem_points_button);
        mMyHelper = new MyHelper(mHomePageActivity,"USERSDB",null,1);
        mdB = mMyHelper.getWritableDatabase();
        mRewardsTv = (TextView)view.findViewById(R.id.textView12);
        Cursor c = mdB.query("users",null,"email=?",new String[]{FirebaseAuth.getInstance().getCurrentUser().getEmail()},null,null,null);

        if(c.moveToNext()){
            rewards = c.getDouble(8);
        mRewardsTv.setText(String.valueOf(c.getDouble(8)));
        balance  = c.getDouble(7);}
        mRedeemPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balance+=rewards;
                rewards=0;
                ContentValues cv= new ContentValues();
                cv.put("balance",balance);
                cv.put("rewards",rewards);
                String[] args = {FirebaseAuth.getInstance().getCurrentUser().getEmail()};
                mdB.update("users",cv,"email = ?",args);
                mRewardsTv.setText(String.valueOf(rewards));
                Toast.makeText(mHomePageActivity, "Your balance is updated!", Toast.LENGTH_LONG).show();
            }
        });
        mHomePageActivity.getSupportActionBar().setTitle("Rewards");
        return view;
    }

}
