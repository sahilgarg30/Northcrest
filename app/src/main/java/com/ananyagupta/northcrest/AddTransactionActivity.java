package com.ananyagupta.northcrest;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AddTransactionActivity extends AppCompatActivity {

    private EditText mCustomerEt;
    private EditText mAmountEt;
    private double amount;
    private double rewards;
    private final String Deal = "NorthCrest Stores";
    private MyHelper mMyHelper;
    private SQLiteDatabase mdB;
    private RadioButton mDebitBtn;
    private RadioButton mCreditBtn;
    private double bal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        mCustomerEt = (EditText) findViewById(R.id.editText7);
        mAmountEt = (EditText) findViewById(R.id.editText2);
        mMyHelper = new MyHelper(AddTransactionActivity.this, "USERSDB", null, 1);
        mdB = mMyHelper.getWritableDatabase();
        mDebitBtn = (RadioButton) findViewById(R.id.debit_rb);
        mCreditBtn = (RadioButton) findViewById(R.id.credit_rb);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(AddTransactionActivity.this);
                break;
        }
        return true;
    }

    public void makeTransaction() {
        try {
            amount = Double.parseDouble(mAmountEt.getText().toString());
        }catch (Exception e){
            Toast.makeText(AddTransactionActivity.this, "Invalid Amount.", Toast.LENGTH_SHORT).show();
            return;
        }
        String customer = mCustomerEt.getText().toString();
        String[] args = {FirebaseAuth.getInstance().getCurrentUser().getEmail()};
        Cursor c = mdB.query("users", null, "email = ?", args, null, null, null);
        if (c.moveToNext()) {
            rewards = c.getDouble(8);
            bal = c.getDouble(7);
            if (mDebitBtn.isChecked()) {
                bal = bal - amount;
                if (bal < 0) {
                    Toast.makeText(AddTransactionActivity.this, "Not enough money to make transaction.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (customer.equals(Deal)) {
                    rewards += 1.25 * amount / 100;
                } else {
                    rewards += amount / 100;
                }
            } else {
                bal = bal + amount;
                rewards += 0;
            }
        }

        ContentValues cv = new ContentValues();
        cv.put("balance", bal);
        cv.put("rewards", rewards);
        mdB.update("users", cv, "email = ?", args);

        ContentValues cv1 = new ContentValues();
        cv1.put("email",FirebaseAuth.getInstance().getCurrentUser().getEmail());
        cv1.put("customer",customer);
        cv1.put("amount",mAmountEt.getText().toString());
        if(mDebitBtn.isChecked())
            cv1.put("type","debit");
        else
            cv1.put("type","credit");
        mdB.insert("trans",null,cv1);

        Toast.makeText(AddTransactionActivity.this, "Transaction Successfully made!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void handleTransaction(View view) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(AddTransactionActivity.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        } else {
            builder = new AlertDialog.Builder(AddTransactionActivity.this);
        }
        builder.setTitle("Confirm Transaction")
                .setMessage("Are you sure you want to make this transaction?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        makeTransaction();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.cancel();
                    }
                })
                .setIcon(R.drawable.ic_welcome_money)
                .show();
    }
}
