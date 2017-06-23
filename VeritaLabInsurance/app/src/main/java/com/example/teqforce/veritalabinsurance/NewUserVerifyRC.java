package com.example.teqforce.veritalabinsurance;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class NewUserVerifyRC extends AppCompatActivity {

    ImageView imageViewRC;
    String imgpath;
    Button Btn_verify;
    Boolean Error=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_verify_rc);


        imageViewRC = (ImageView) findViewById(R.id.imageViewRC);
        Bundle bundle = getIntent().getExtras();
        imgpath = bundle.getString("ImgFilePath_RC");

        File imgFile = new File(imgpath);
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //imageViewRC.setImageBitmap(myBitmap);
            imageViewRC.setImageBitmap(decodeFile(imgpath));
        }

        ChangeRCPhoto();
        MoveNextActivity();

    }

    private void ChangeRCPhoto()
    {
        Button Btn_ChangePhotoRC = (Button) findViewById(R.id.btn_ChangePhotoRC);
        final Context context = this;
        Btn_ChangePhotoRC.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            Intent intent = new Intent(context, UploadRegnNewUser.class);
            startActivity(intent);

            }
        });
    }

    private void MoveNextActivity()
    {
        Btn_verify = (Button) findViewById(R.id.btn_verify);
        final Context context = this;
        Btn_verify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadMultipart(imgpath);
                if(!Error)
                {
                    Intent intent = new Intent(context, NewUserUploadPolicy.class);
                    startActivity(intent);
                }
            }
        });

    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    public void uploadMultipart(String selectedImagePath) {
        //getting name for the image
        //String name = editText.getText().toString().trim();
        String name="jeet";
        //getting the actual path of the image
        // String path = getPath(filePath);
        String path = selectedImagePath;

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, Constants.UPLOAD_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Error = true;
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
