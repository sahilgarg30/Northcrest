package com.ananyagupta.northcrest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.ActionMode;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GoogleApiClient mGoogleApiClient;
    private NavigationView navigationView;
    private android.app.FragmentManager mFragManager;
    private android.app.FragmentTransaction mFragTransaction;
    private BalanceFragment mBalanceFrag;
    private HistoryFragment mHistoryFrag;
    private MyProfileFragment mMyProfileFrag;
    private RewardsFragment mRewardsFrag;
    private SupportFragment mSupportFrag;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEdit;
    private TextView navUsername;
    private MyHelper mMyHelper;
    private SQLiteDatabase mdB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mMyHelper = new MyHelper(HomeActivity.this, "USERSDB", null, 1);
        mdB = mMyHelper.getReadableDatabase();
        String[] args = {FirebaseAuth.getInstance().getCurrentUser().getEmail()};
        Cursor c = mdB.query("users", null, "email = ?", args, null, null, null);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        navUsername = (TextView) headerView.findViewById(R.id.username_nav_tv);
        if(c.moveToNext())
        navUsername.setText(c.getString(1));

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        mBalanceFrag = new BalanceFragment();
        mHistoryFrag =  new HistoryFragment();
        mMyProfileFrag = new MyProfileFragment();
        mRewardsFrag = new RewardsFragment();
        mSupportFrag = new SupportFragment();
        mFragManager = getFragmentManager();
        mFragTransaction = mFragManager.beginTransaction();
        mFragTransaction.add(R.id.homepage_frame,mMyProfileFrag);
        mFragTransaction.commit();

        mSp = getSharedPreferences("current_state",MODE_PRIVATE);
        mEdit = mSp.edit();
    }
    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent =  new Intent(HomeActivity.this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_delete_transaction) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(HomeActivity.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
            } else {
                builder = new AlertDialog.Builder(HomeActivity.this);
            }
            builder.setTitle("Delete all transactions ?")
                    .setMessage("Are you sure you want to delete all transactions ? This will not affect your balance or rewards.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String args[] = {FirebaseAuth.getInstance().getCurrentUser().getEmail()};
                            mdB.delete("trans","email = ?",args);
                            if(mHistoryFrag.isResumed())
                                mHistoryFrag.changeData();
                            Toast.makeText(HomeActivity.this, "All transactions successfully deleted.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.cancel();
                        }
                    })
                    .setIcon(R.drawable.ic_warning_black)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            mFragTransaction = mFragManager.beginTransaction();
            mFragTransaction.replace(R.id.homepage_frame,mMyProfileFrag);
            mFragTransaction.commit();
        } else if (id == R.id.nav_balance) {
            mFragTransaction = mFragManager.beginTransaction();
            mFragTransaction.replace(R.id.homepage_frame,mBalanceFrag);
            mFragTransaction.commit();

        } else if (id == R.id.nav_history) {
            mFragTransaction = mFragManager.beginTransaction();
            mFragTransaction.replace(R.id.homepage_frame,mHistoryFrag);
            mFragTransaction.commit();
        } else if (id == R.id.nav_rewards) {
            mFragTransaction = mFragManager.beginTransaction();
            mFragTransaction.replace(R.id.homepage_frame,mRewardsFrag);
            mFragTransaction.commit();

        } else if (id == R.id.nav_support) {
            mFragTransaction = mFragManager.beginTransaction();
            mFragTransaction.replace(R.id.homepage_frame,mSupportFrag);
            mFragTransaction.commit();
        } else if (id == R.id.nav_signOut) {
            mEdit.putInt("state",0);
            mEdit.apply();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // ...
                            Intent i=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                        }
                    });
            Toast.makeText(HomeActivity.this,"Signed Out",Toast.LENGTH_LONG).show();
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
