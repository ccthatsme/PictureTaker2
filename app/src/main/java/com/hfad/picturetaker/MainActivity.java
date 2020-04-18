package com.hfad.picturetaker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.Manifest.permission.*;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {


    private ImageView iv;
    private Button button;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private Camera camera;
    private ImageView ivDisplay;

    public static final int IMAGE_REQUEST = 2;

    String currentImagePath = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = findViewById(R.id.image);
        button = findViewById(R.id.photo_button);



    }

    public void takePhoto(View view) throws IOException {
        //our intent is to capture an image
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //check if there is any application capable of handling the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            //startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            File imageFile = null;
            imageFile = getImageFile();

            if (imageFile != null) {
                Uri imageUri = FileProvider.getUriForFile(this, "com.hfad.picturetaker.fileprovider", imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, IMAGE_REQUEST);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");

            iv.setImageBitmap(bitmap);


        }


    }


    public void displayPhoto(View view) {
        ivDisplay = findViewById(R.id.imageDisplay);

        Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("image_path"));


        ivDisplay.setImageBitmap(bitmap);

    }

    //this method will return a valid file path for the image

    private File getImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageName = ".jpg_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);


           File imageFile = File.createTempFile(imageName, ".jpg", storageDir);


        return imageFile;
    }

}
