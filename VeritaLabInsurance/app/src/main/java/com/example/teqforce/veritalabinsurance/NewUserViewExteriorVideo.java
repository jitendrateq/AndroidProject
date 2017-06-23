package com.example.teqforce.veritalabinsurance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

public class NewUserViewExteriorVideo extends AppCompatActivity {

    private String filePath = null;
    private VideoView vidPreview;
    Button btnproceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_view_exterior_video);

        vidPreview = (VideoView) findViewById(R.id.videoView);
        Intent i = getIntent();

        // image or video path that is captured in previous activity
        filePath = i.getStringExtra("filePath");

        if (filePath != null) {
            // Displaying the video on the screen
            previewMedia();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, video path is missing!", Toast.LENGTH_LONG).show();
        }
        ProceedButtonClick();
    }

    private void previewMedia() {

        vidPreview.setVisibility(View.VISIBLE);
        vidPreview.setVideoPath(filePath);
        // start playing
        vidPreview.start();

    }

    private void ProceedButtonClick()
    {
        btnproceed = (Button) findViewById(R.id.btn_ok);

        btnproceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(NewUserViewExteriorVideo.this, NewUserRetakeSubmitVideo.class);
                startActivity(i);
            }
        });
    }
}
