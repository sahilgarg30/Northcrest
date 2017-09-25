package com.ananyagupta.northcrest;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class SupportFragment extends Fragment {


    private HomeActivity mHomePageActivity;
    private TextView mPhoneTv;
    private TextView mEmailTv;
    private TextView mWebsiteTv;

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
        mPhoneTv = (TextView) view.findViewById(R.id.phone_tv);
        mEmailTv=(TextView) view.findViewById(R.id.email_tv);
        mWebsiteTv=(TextView)view.findViewById(R.id.website_tv);
        mPhoneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:080-246-2453"));
                startActivity(intent);
            }
        });
        mEmailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,"Text in the body of mail");
                intent.putExtra(Intent.EXTRA_EMAIL,new String []{"support@northcrest.in"});
                intent.setType("text/plain");
                startActivity(intent);
            }
        });
        mWebsiteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.ncbanks.com"));
                startActivity(intent);
            }
        });
        return view;
    }

}
