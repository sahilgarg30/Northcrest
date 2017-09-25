package com.ananyagupta.northcrest;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {


    private HomeActivity mHomePageActivity;
    private ListView mListView;
    private LinearLayout mHistoryEmpty;
    private HistoryAdapter mHistoryAdapter;

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
        mHistoryAdapter =  new HistoryAdapter(mHomePageActivity);
        mListView.setAdapter(mHistoryAdapter);
        mHistoryEmpty = (LinearLayout) view.findViewById(R.id.history_empty);
        mListView.setEmptyView(mHistoryEmpty);
        return view;
    }
}
