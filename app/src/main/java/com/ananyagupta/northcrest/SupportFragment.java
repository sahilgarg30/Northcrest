package com.ananyagupta.northcrest;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class SupportFragment extends Fragment {


    private HomeActivity mHomePageActivity;

    public SupportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_support, container, false);
        mHomePageActivity = (HomeActivity) getActivity();
        mHomePageActivity.getSupportActionBar().setTitle("Support");
        return view;
    }

}
