package com.example.teqforce.veritalabinsurance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.teqforce.veritalabinsurance.service.AndroidMultiPartEntity.ProgressListener;
import com.example.teqforce.veritalabinsurance.app.Config;
import com.example.teqforce.veritalabinsurance.service.AndroidMultiPartEntity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

public class NewUserRetakeSubmitVideo extends AppCompatActivity {

    private String videofilePath = null;
    Button btnViewVideo;
    Button btnRetakeVideo;
    Button btnSubmit;
    Button btnProceed;
    boolean isImage;
    long totalSize = 0;
    private static final String TAG = NewUserRetakeSubmitVideo.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_retake_submit_video);

        // Receiving the data from previous activity
        Intent i = getIntent();
        // image or video path that is captured in previous activity
        videofilePath = i.getStringExtra("filePath");
        onButtonClick();
    }

    private void launchUploadActivity(){
        Intent i = new Intent(NewUserRetakeSubmitVideo.this, NewUserViewExteriorVideo.class);
        i.putExtra("filePath", videofilePath);
        startActivity(i);
    }

    private void onButtonClick()
    {

        btnViewVideo = (Button) findViewById(R.id.btnViewVideo);
        btnRetakeVideo = (Button) findViewById(R.id.btnRetake);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnProceed = (Button) findViewById(R.id.btn_proceed);

        btnViewVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                launchUploadActivity();
            }
        });

        btnRetakeVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            Intent i = new Intent(NewUserRetakeSubmitVideo.this, NewUserStartExteriorVideo.class);
            startActivity(i);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new UploadFileToServer().execute();
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                OnProceedButton();
            }
        });
    }

    private void OnProceedButton()
    {
        btnProceed = (Button) findViewById(R.id.btn_proceed);
        btnProceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewUserRetakeSubmitVideo.this, NewUserVehicleInteriorCondition.class);
                startActivity(i);
            }
        });
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
           // progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
           // progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
           // progressBar.setProgress(progress[0]);

            // updating percentage value
            //txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(videofilePath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("app", new StringBody("VeritaLab"));
                entity.addPart("email", new StringBody("abc@gmail.com"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            //showAlert(result);

            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
