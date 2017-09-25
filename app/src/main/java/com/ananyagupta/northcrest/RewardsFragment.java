package com.ananyagupta.northcrest;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class RewardsFragment extends Fragment {


    private HomeActivity mHomePageActivity;
    private Button mRedeemPointsButton;

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
        mRedeemPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mHomePageActivity, "Your balance is updated!", Toast.LENGTH_LONG).show();
            }
        });
        mHomePageActivity.getSupportActionBar().setTitle("Rewards");
        return view;
    }

}
