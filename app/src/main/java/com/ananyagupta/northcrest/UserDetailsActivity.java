package com.ananyagupta.northcrest;

import android.*;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class UserDetailsActivity extends AppCompatActivity {

    private SharedPreferences mSp;
    private SharedPreferences.Editor mEdit;
    private MyHelper mMyHelper;
    private SQLiteDatabase mdB;
    private ImageButton mDpIb;
    private Bitmap photo;
    private EditText mInBalanceEt;
    private EditText mNameEt;
    private EditText mAccNoEt;
    private EditText mDobEt;
    private EditText mAddressEt;
    private EditText mPhoneEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        mSp = getSharedPreferences("current_state",MODE_PRIVATE);
        mEdit = mSp.edit();

        mMyHelper = new MyHelper(UserDetailsActivity.this,"USERSDB",null,1);
        mdB = mMyHelper.getWritableDatabase();
        mDpIb = (ImageButton) findViewById(R.id.dp_ib);
        mDpIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        mNameEt = (EditText)findViewById(R.id.signup_name);
        mAccNoEt = (EditText)findViewById(R.id.signup_account_number);
        mDobEt = (EditText)findViewById(R.id.dob_et);
        mPhoneEt = (EditText)findViewById(R.id.user_details_phone);
        mAddressEt = (EditText)findViewById(R.id.address_tv);
        mInBalanceEt = (EditText)findViewById(R.id.initial_balance);
    }

    public void startHomeActivity(View view) {
      try {
           ContentValues cv = new ContentValues();
           cv.put("name", mNameEt.getText().toString());
           cv.put("accno", mAccNoEt.getText().toString());
           cv.put("phone", mPhoneEt.getText().toString());
           cv.put("dob", mDobEt.getText().toString());
           cv.put("address", mAddressEt.getText().toString());
           cv.put("balance", Double.parseDouble(mInBalanceEt.getText().toString()));
           cv.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
           cv.put("rewards",0.0);
           long id = mdB.insert("users",null,cv);
           if(id==-1) ;//throw new Exception();
           mEdit.putInt("state", 2);
           mEdit.apply();
           Intent intent = new Intent(UserDetailsActivity.this, HomeActivity.class);
           startActivity(intent);
           finish();
       }catch (Exception e){
           Toast.makeText(UserDetailsActivity.this, "Invalid Input.", Toast.LENGTH_SHORT).show();
       }
    }

    public void capture() {
        if(ActivityCompat.checkSelfPermission(UserDetailsActivity.this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(UserDetailsActivity.this,new String[]{android.Manifest.permission.CAMERA},8);
        }
        else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, 6);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==8)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 6);
                }
            }
            else
            {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void gallery() {
        Intent intent2 = new Intent(Intent.ACTION_PICK);
        intent2.setType("image/*");
        startActivityForResult(intent2, 8);
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailsActivity.this);
        builder.setTitle("Add Profile Photo!").setIcon(R.drawable.ic_camera);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    capture();
                } else if (items[item].equals("Choose from Library")) {
                    gallery();
                } else if (items[item].equals("Cancel")) {
                    mDpIb.setImageResource(R.drawable.user_icon);
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==6)
        {
            if(resultCode==RESULT_OK)
            {
                Bundle extras = data.getExtras();
                photo = (Bitmap) extras.get("data");
                mDpIb.setImageBitmap(photo);
            }
            else if(resultCode==RESULT_CANCELED)
            {
                Toast.makeText(this, "Result cancelled", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode==8)
        {
            if(resultCode==RESULT_OK)
            {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    mDpIb.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "You haven't selected an image", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
