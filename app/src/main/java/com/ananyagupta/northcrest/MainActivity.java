package com.ananyagupta.northcrest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 9001;
    private Button mGoogleLoginBtn;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mEmailLoginBtn;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        mGoogleLoginBtn = (Button) findViewById(R.id.google_login_button);
        mGoogleLoginBtn.setOnClickListener(MainActivity.this);
        mEmailField = (EditText)findViewById(R.id.email_edittext);
        mEmailLoginBtn = (Button) findViewById(R.id.custom_signin_button);
        mPasswordField = (EditText)findViewById(R.id.password_edittext);
        mEmailLoginBtn.setOnClickListener(MainActivity.this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mAuth = FirebaseAuth.getInstance();

        mSp = getSharedPreferences("current_state",MODE_PRIVATE);
        mEdit = mSp.edit();
        int state = mSp.getInt("state",0);
        Intent intent;
        switch(state){
            case 0 : break;
            case 1 : intent = new Intent(MainActivity.this,UserDetailsActivity.class);
                     startActivity(intent);
                     finish();
                     break;
            case 2 :
                     intent = new Intent(MainActivity.this, HomeActivity.class);
                     startActivity(intent);
                     finish();
                     break;
        }
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user!=null){
                }
            }
        };
    }

    public void startSignUpActivity(View view) {
        Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.google_login_button:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.custom_signin_button:
                signInWithEmail(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
        }else {
            Toast.makeText(this, "Login Unsuccessful.", Toast.LENGTH_LONG).show();
            // Signed out, show unauthenticated UI.
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this,UserDetailsActivity.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "Signed in with google!", Toast.LENGTH_SHORT).show();
                            mEdit.putInt("state",1);
                            mEdit.apply();
                            finish();
                        }
                    }
                });
    }

        @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Google play services error.", Toast.LENGTH_LONG).show();

    }
    private void signInWithEmail(String email, String password) {
        if (!validateForm()) {
            Toast.makeText(MainActivity.this, "Email and password required.", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            mEdit.putInt("state",2);
                            mEdit.apply();
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "Signed in with email!", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Login Unsuccessful.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private boolean validateForm() {
        boolean valid = true;
        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }
        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }
        return valid;
    }
}
