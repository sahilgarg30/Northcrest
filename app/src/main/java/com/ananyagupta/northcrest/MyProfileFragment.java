package com.ananyagupta.northcrest;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {


    private HomeActivity mHomePageActivity;
    private MyHelper mMyHelper;
    private SQLiteDatabase mdB;
    private TextView mNameTv;
    private TextView mUserIdTv;
    private TextView mAccNoTv;
    private TextView mEmailTv;
    private TextView mPhoneTv;
    private TextView mDobTv;
    private TextView mAddressTv;
    private ImageView mDpIv;

    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        mHomePageActivity = (HomeActivity) getActivity();
        mHomePageActivity.getSupportActionBar().setTitle("My Profile");
        mMyHelper = new MyHelper(mHomePageActivity, "USERSDB", null, 1);
        mdB = mMyHelper.getReadableDatabase();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mHomePageActivity, AddTransactionActivity.class);
                startActivity(intent);
            }
        });

        String[] args = {FirebaseAuth.getInstance().getCurrentUser().getEmail()};
        Cursor c = mdB.query("users", null, "email = ?", args, null, null, null);
        mNameTv = (TextView) view.findViewById(R.id.textView7);
        mUserIdTv = (TextView) view.findViewById(R.id.textView8);
        mAccNoTv = (TextView) view.findViewById(R.id.textView5);
        mEmailTv = (TextView) view.findViewById(R.id.textView10);
        mPhoneTv = (TextView) view.findViewById(R.id.textView12);
        mDobTv = (TextView) view.findViewById(R.id.textView14);
        mAddressTv = (TextView) view.findViewById(R.id.textView16);
        mDpIv = (ImageView) view.findViewById(R.id.imageView2);
        if(c.moveToNext()) {
            mUserIdTv.setText("NCBID0000" + c.getString(0));
            mNameTv.setText(c.getString(2));
            mAccNoTv.setText(c.getString(3));
            mPhoneTv.setText(c.getString(5));
            mDobTv.setText(c.getString(4));
            mAddressTv.setText(c.getString(6));
            mEmailTv.setText(c.getString(1));
            byte[] image = c.getBlob(9);
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                mDpIv.setImageBitmap(bitmap);
            }
            catch(Exception e)
            {
                Toast.makeText(mHomePageActivity, "Unable to put image", Toast.LENGTH_SHORT).show();
            }

        }
        return view;
    }
}
