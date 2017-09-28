package com.ananyagupta.northcrest;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.R.attr.data;
import static java.lang.Double.parseDouble;

public class SettingsActivity extends AppCompatActivity {

    private Bitmap photo;
    private ImageButton mDpIb;
    private MyHelper mMyHelper;
    private SQLiteDatabase mdB;
    private EditText mBalanceEt;
    private EditText mPhoneEt;
    private EditText mAddressEt;
    private EditText mNameEt;
    private EditText mDobEt;
    private byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        mDpIb = (ImageButton) findViewById(R.id.dp_settings_ib);
        mDpIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        mMyHelper = new MyHelper(SettingsActivity.this, "USERSDB", null, 1);
        mdB = mMyHelper.getWritableDatabase();

        mBalanceEt = (EditText) findViewById(R.id.balance_et);
        mPhoneEt = (EditText) findViewById(R.id.phone_et);
        mAddressEt = (EditText) findViewById(R.id.address_et);
        mNameEt = (EditText) findViewById(R.id.name_et);
        mDobEt = (EditText) findViewById(R.id.dob_et);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavUtils.navigateUpFromSameTask(SettingsActivity.this);
                    }
                };
                backPressDialogBox(dialogClickListener);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };
        backPressDialogBox(dialogClickListener);
    }

    private void backPressDialogBox(DialogInterface.OnClickListener dialogClickListener) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(SettingsActivity.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        } else {
            builder = new AlertDialog.Builder(SettingsActivity.this);
        }
        builder.setTitle("Leave Page")
                .setMessage("Are you sure you want to leave this page? All unsaved changes will be lost.")
                .setPositiveButton(android.R.string.yes, dialogClickListener)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.cancel();
                    }
                })
                .setIcon(R.drawable.ic_warning_black)
                .show();
    }

    public void capture() {
        if (ActivityCompat.checkSelfPermission(SettingsActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{android.Manifest.permission.CAMERA}, 8);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, 6);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 8) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 6);
                }
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void gallery() {
        Intent intent2 = new Intent(Intent.ACTION_PICK);
        intent2.setType("image/*");
        startActivityForResult(intent2, 8);
    }

    private void saveImage(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 0, stream);
        try {
            image = stream.toByteArray();
        }
        catch(Exception e)
        {
            Toast.makeText(this, "Unable to insert image. Try again later.", Toast.LENGTH_LONG).show();
        }
        String[] args = {FirebaseAuth.getInstance().getCurrentUser().getEmail()};
        Cursor c = mdB.query("users", null, "email = ?", args, null, null, null);
        if (c.moveToNext()) {
            ContentValues cv = new ContentValues();
            cv.put("dp",image);
            mdB.update("users", cv, "email = ?", args);
            Toast.makeText(SettingsActivity.this, "Profile photo successfully updated!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setEmptyDp(){
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.user_icon);
        photo = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        image = stream.toByteArray();
        mDpIb.setImageResource(R.drawable.user_icon);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library","Remove profile picture",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("Add Profile Photo!").setIcon(R.drawable.ic_camera);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    capture();
                } else if (items[item].equals("Choose from Library")) {
                    gallery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if(items[item].equals("Remove profile picture")){
                    setEmptyDp();
                    saveImage();
                    finish();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                photo = (Bitmap) extras.get("data");
                mDpIb.setImageBitmap(photo);
                saveImage();
                finish();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Result cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 8) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    mDpIb.setImageBitmap(selectedImage);
                    saveImage();
                    finish();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "You haven't selected an image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateBalance(View view) {
        String[] args = {FirebaseAuth.getInstance().getCurrentUser().getEmail()};
        Cursor c = mdB.query("users", null, "email = ?", args, null, null, null);
        try {
            double d = Double.parseDouble(mBalanceEt.getText().toString());
            if (c.moveToNext()) {
                ContentValues cv = new ContentValues();
                cv.put("balance", d);
                mdB.update("users", cv, "email = ?", args);
                Toast.makeText(SettingsActivity.this, "Balance successfully updated!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(SettingsActivity.this, "Invalid balance amount.", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveChanges(View view) {
        String[] args = {FirebaseAuth.getInstance().getCurrentUser().getEmail()};
        Cursor c = mdB.query("users", null, "email = ?", args, null, null, null);
        if (c.moveToNext()) {
            ContentValues cv = new ContentValues();
            cv.put("name",mNameEt.getText().toString());
            cv.put("phone",mPhoneEt.getText().toString());
            cv.put("address",mAddressEt.getText().toString());
            cv.put("dob",mDobEt.getText().toString());
            mdB.update("users", cv, "email = ?", args);
            Toast.makeText(SettingsActivity.this, "Details successfully updated!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] args = {FirebaseAuth.getInstance().getCurrentUser().getEmail()};
        Cursor c = mdB.query("users", null, "email = ?", args, null, null, null);
        super.onStart();
        if(c.moveToNext()) {
            mNameEt.setText(c.getString(2));
            mPhoneEt.setText(c.getString(5));
            mDobEt.setText(c.getString(4));
            mAddressEt.setText(c.getString(6));
            mBalanceEt.setText(String.valueOf(c.getDouble(7)));
            byte[] image = c.getBlob(9);
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                photo=bitmap;
                mDpIb.setImageBitmap(bitmap);
            }
            catch(Exception e)
            {
                Toast.makeText(SettingsActivity.this, "Unable to show profile picture.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

