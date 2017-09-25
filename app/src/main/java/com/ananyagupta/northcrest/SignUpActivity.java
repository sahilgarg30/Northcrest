package com.ananyagupta.northcrest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEmailEt;
    private EditText mPasswordEt;
    private EditText mConfirmEt;
    private FirebaseAuth mAuth;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        mEmailEt = (EditText)findViewById(R.id.signup_email_edittext);
        mPasswordEt = (EditText)findViewById(R.id.signup_password_edittext);
        mConfirmEt = (EditText)findViewById(R.id.signup_confirmpassword_edittext);
        mAuth = FirebaseAuth.getInstance();

        mSp = getSharedPreferences("current_state",MODE_PRIVATE);
        mEdit = mSp.edit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case android.R.id.home :
                startMainActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startMainActivity() {
        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        startMainActivity();
    }


    public void signupUser(View view) {
        startSignUpMethod(mEmailEt.getText().toString(),mPasswordEt.getText().toString());
    }


    private void startSignUpMethod(String email, String password) {
        if (!validateForm()) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            mEdit.putInt("state",1);
                            mEdit.apply();
                            Intent intent = new Intent(SignUpActivity.this,UserDetailsActivity.class);
                            startActivity(intent);
                            Toast.makeText(SignUpActivity.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Sign up failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private boolean validateForm() {
        boolean valid = true;
        String email = mEmailEt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailEt.setError("Required.");
            valid = false;
        } else {
            mEmailEt.setError(null);
        }
        String password = mPasswordEt.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordEt.setError("Required.");
            valid = false;
        } else {
            mPasswordEt.setError(null);
        }
        String confirm_password = mConfirmEt.getText().toString();
        if (TextUtils.isEmpty(confirm_password)) {
            mConfirmEt.setError("Required.");
            valid = false;
        } else {
            mConfirmEt.setError(null);
        }
        if(!password.equals(confirm_password)) {
            mConfirmEt.setError("Doesn't match.");
            valid=false;
        }  else {
            mConfirmEt.setError(null);
        }
        return valid;
    }
}
