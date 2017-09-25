package com.ananyagupta.northcrest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SettingsActivity extends AppCompatActivity {

    private Bitmap photo;
    private ImageButton mDpIb;

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                DialogInterface.OnClickListener dialogClickListener =  new DialogInterface.OnClickListener() {
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
        DialogInterface.OnClickListener dialogClickListener =  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };
        backPressDialogBox(dialogClickListener);
    }

    private void backPressDialogBox(DialogInterface.OnClickListener dialogClickListener){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(SettingsActivity.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        } else {
            builder = new AlertDialog.Builder(SettingsActivity.this);
        }
        builder.setTitle("Leave Page")
                .setMessage("Are you sure you want to leave this page? All unsaved changes will be lost.")
                .setPositiveButton(android.R.string.yes,dialogClickListener)
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
        if(ActivityCompat.checkSelfPermission(SettingsActivity.this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(SettingsActivity.this,new String[]{android.Manifest.permission.CAMERA},8);
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

