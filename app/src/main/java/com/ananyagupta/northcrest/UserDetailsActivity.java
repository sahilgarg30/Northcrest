package com.ananyagupta.northcrest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class UserDetailsActivity extends AppCompatActivity {

    private SharedPreferences mSp;
    private SharedPreferences.Editor mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        mSp = getSharedPreferences("current_state",MODE_PRIVATE);
        mEdit = mSp.edit();
    }

    public void startHomeActivity(View view) {
        mEdit.putInt("state",2);
        mEdit.apply();
        Intent intent = new Intent(UserDetailsActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
