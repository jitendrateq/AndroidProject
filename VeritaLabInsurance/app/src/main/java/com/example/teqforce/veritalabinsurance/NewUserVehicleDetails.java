package com.example.teqforce.veritalabinsurance;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.teqforce.veritalabinsurance.app.Config;
import com.example.teqforce.veritalabinsurance.app.MyApplication;
import com.example.teqforce.veritalabinsurance.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static com.example.teqforce.veritalabinsurance.ExistingUserLogin.isValidPhoneNumber;

public class NewUserVehicleDetails extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = NewUserVehicleDetails.class.getSimpleName();
    private String selectedFilePath;
    private String SERVER_URL = "http://10.2.0.129:8080/AndroidImageUpload/uploadfile.php";
    private PrefManager pref;
    ImageView bUploadRC;
    ImageView bUploadPN;
    TextView tvFileName;
    Button BackButton;
    ProgressDialog dialog;
    PowerManager.WakeLock wakeLock;
    String filePath;
    Boolean IsFileUpload;
    Button btn_proceed;
    EditText edittxtreqno;
    EditText edittextprevpol;
    TextView Uploadmsg;
    TextView UploadPolicymsg;
    String UploadType;
    JSONObject jsonobject;
    JSONArray jsonarray;
    ArrayList<String> manufatureslist;
    ArrayList<Manufatures> manufatures;

    ArrayList<String> modelvarlist;
    ArrayList<ModelVar> modelvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_vehicle_details);
        bUploadRC = (ImageView) findViewById(R.id.imageButton_RC);
        bUploadPN = (ImageView) findViewById(R.id.imageButton_PN);
        tvFileName = (TextView) findViewById(R.id.tv_file_name);
        BackButton = (Button) findViewById(R.id.btn_back);
        btn_proceed = (Button) findViewById(R.id.btn_proceed);
        edittxtreqno = (EditText) findViewById(R.id.edittxtreqno);
        edittextprevpol = (EditText) findViewById(R.id.edittextprevpol);

        new DownloadJSON().execute();
        new DownloadModelVarJSON().execute();
        pref = new PrefManager(getApplicationContext());



        bUploadRC.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UploadType="RC";showFileChooser();
            }
        });

        bUploadPN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UploadType="Policy";showFileChooser();
            }
        });

        BackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //finish();
                onBackPressed();
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /*if (!validate()) {
                    onNotUploadingFile();
                    return;
                }*/
                addListenerOnButton();
            }
        });

    }

    public void addListenerOnButton() {
        try{
            HashMap<String, String> user = pref.getUserDetails();
            String regNo = edittxtreqno.getText().toString();
            String prevPolicyNo = edittextprevpol.getText().toString();
            //String modelVar = user.get("mobile");
            //String prevPolicyNo = user.get("mobile");



            String mobile = user.get("mobile");
            Spinner manufacturSpin=(Spinner) findViewById(R.id.manu_spinner);
            String manufacture = manufacturSpin.getSelectedItem().toString();

            Spinner modelVarSpin =(Spinner) findViewById(R.id.profile_spinner);
            String modelVar = modelVarSpin.getSelectedItem().toString();
            if(regNo != null && !regNo.isEmpty() && prevPolicyNo != null && !prevPolicyNo.isEmpty()  && mobile != null && !mobile.isEmpty() && manufacture != null && !manufacture.isEmpty()){
                InsertRegDetailsNewUser(regNo,manufacture,modelVar,prevPolicyNo,mobile);
            }
            else{
                Toast.makeText(getBaseContext(), "Please fill the details!!!", Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void onNotUploadingFile() {
        String RC = tvFileName.getText().toString();
        final Context context = this;
        if (RC.isEmpty()) {
            Toast.makeText(getBaseContext(), "Please upload RC document", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(context, UploadRegnNewUser.class);
        startActivity(intent);
    }

    public boolean validate() {
        boolean valid = true;

        String RC_upload = tvFileName.getText().toString();
        String RC_no = edittxtreqno.getText().toString();
        String Prv_Policy_No = edittextprevpol.getText().toString();

        if (RC_upload.isEmpty()) {
            //tvFileName.setError("enter a valid OTP");
            valid = false;
        } else {
            //tvFileName.setError(null);
        }

        if (RC_no.isEmpty()) {
            edittxtreqno.setError("Enter RC no");
            valid = false;
        } else {
            edittxtreqno.setError(null);
        }
        if (Prv_Policy_No.isEmpty()) {
            edittextprevpol.setError("Enter previous policy no");
            valid = false;
        } else {
            edittextprevpol.setError(null);
        }
        return valid;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    //no data present
                    return;
                }

                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
                wakeLock.acquire();

                Uri selectedFileUri = data.getData();
                //String filePath = Utils.getActualPath(this, selectedFileUri);
                selectedFilePath = getPathfromURI(this, data.getData());
                filePath = getPathfromURI(this, data.getData());
                //ContentResolver.openInputStream();
                Log.i(TAG, "Selected File Path:" + selectedFilePath);
                Uploadmsg = (TextView) findViewById(R.id.txtUploadmsg);
                UploadPolicymsg=(TextView) findViewById(R.id.txtUploadPolicymsg);
                if (filePath != null && !filePath.equals("")) {
                    tvFileName.setText(filePath);
                    if (filePath != null) {
                        dialog = ProgressDialog.show(NewUserVehicleDetails.this, "", "Uploading File...", true);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    //creating new thread to handle Http Operations
                                    uploadFile(filePath);
                                    if(IsFileUpload) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(UploadType=="RC")
                                                    Uploadmsg.setText("RC document uploaded successfully");
                                                else
                                                {UploadPolicymsg.setText("Prev. Policy document uploaded successfully");}
                                            }
                                        });
                                    }
                                } catch (OutOfMemoryError e) {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(NewUserVehicleDetails.this, "Insufficient Memory!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    dialog.dismiss();
                                }

                            }
                        }).start();
                    } else {
                        Toast.makeText(NewUserVehicleDetails.this, "Please choose a File First", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @SuppressLint("NewApi")
    public static String getPathfromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                // if ("primary".equalsIgnoreCase(type)) {
                String dflh=Environment.getExternalStorageDirectory() + "/" + split[1];
                return Environment.getExternalStorageDirectory() + "/" + split[1];


                // }


                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public int uploadFile(final String selectedFilePath) {

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String inspname="1234_RC";
        String crlf = "\r\n";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);
                connection.setRequestProperty("name",inspname);
                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);


                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {

                    try {

                        //write the bytes read from inputstream
                        dataOutputStream.write(buffer, 0, bufferSize);
                    } catch (OutOfMemoryError e) {
                        Toast.makeText(NewUserVehicleDetails.this, "Insufficient Memory!", Toast.LENGTH_SHORT).show();
                    }
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);

                String paramName = "name";
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + paramName + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(inspname + lineEnd);

                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                try{
                    serverResponseCode = connection.getResponseCode();
                }catch (OutOfMemoryError e){
                    Toast.makeText(NewUserVehicleDetails.this, "Memory Insufficient!", Toast.LENGTH_SHORT).show();
                }
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    IsFileUpload = true;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvFileName.setText("File Upload completed." + fileName);
                            IsFileUpload = true;
                        }
                    });
                }
                else
                {
                    IsFileUpload = false;
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

                if (wakeLock.isHeld()) {

                    wakeLock.release();
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewUserVehicleDetails.this, "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewUserVehicleDetails.this, "URL Error!", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewUserVehicleDetails.this, "Cannot Read/Write File", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            dialog.dismiss();
            return serverResponseCode;
        }

    }
    public void InsertRegDetailsNewUser(final String regNo, final String manufacture, final String modelVar, final String prevPolicyNo,final String mobile) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.URL_INSERT_NEW_USER_REG_DETAILS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

                try {
                    JSONObject responseObj = new JSONObject(response);

                    // Parsing json object response
                    // response will be a json object
                    boolean error = responseObj.getBoolean("error");
                    String message = responseObj.getString("message");

                    // checking for error, if not error SMS is initiated
                    // device should receive it shortly
                    if (!error) {
                        // boolean flag saying device is waiting for sms

                        Intent intent = new Intent(NewUserVehicleDetails.this, UploadRegnNewUser.class);
                        startActivity(intent);
                        String inspecid = pref.getInspectioID();

                        Toast.makeText(getApplicationContext(),
                                "InspectionID: " + inspecid,
                                Toast.LENGTH_LONG).show();


                    } else {

                        Toast.makeText(getApplicationContext(),
                                "Error: " + message,
                                Toast.LENGTH_LONG).show();
                    }

                    // hiding the progress bar
                    // progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                    // progressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                //  progressBar.setVisibility(View.GONE);
            }
        }) {

            /**
             * Passing user parameters to our server
             * @return
             */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reg_No", regNo);
                params.put("manufacturer", manufacture);
                params.put("model_Variant", modelVar);
                params.put("PrevPolicyNo", prevPolicyNo);
                params.put("mobile", mobile);
                params.put("inspection_id",  pref.getInspectioID());

                Log.e(TAG, "Posting params: " + params.toString());

                return params;
            }

        };

        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }




    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the WorldPopulation Class
            manufatures = new ArrayList<Manufatures>();
            // Create an array to populate the spinner
            manufatureslist = new ArrayList<String>();
            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(Config.URL_GET_MANUFACTURE_LIST);

            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("manufacture");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);

                    Manufatures manufaturesOpt = new Manufatures();

                    manufaturesOpt.setId(jsonobject.optInt("id"));
                    manufaturesOpt.setName(jsonobject.optString("name"));

                    manufatures.add(manufaturesOpt);

                    // Populate spinner with country names
                    manufatureslist.add(jsonobject.optString("name"));

                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml
            Spinner mySpinner = (Spinner) findViewById(R.id.manu_spinner);

            // Spinner adapter
            mySpinner
                    .setAdapter(new ArrayAdapter<String>(NewUserVehicleDetails.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            manufatureslist));

            // Spinner on item click listener
            mySpinner
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            //  new DownloadModelVarJSON().execute();
                            Spinner manufacturSpin=(Spinner) findViewById(R.id.manu_spinner);
                            String name = manufacturSpin.getSelectedItem().toString();

                            GetModelVarDropdon(name);




                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
        }
    }



    private class DownloadModelVarJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the WorldPopulation Class
            modelvar = new ArrayList<ModelVar>();
            // Create an array to populate the spinner
            modelvarlist  = new ArrayList<String>();
            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(Config.URL_GET_MODEL_LIST);

            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("model");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);

                    ModelVar modelvarOpt = new ModelVar();

                    modelvarOpt.setId(jsonobject.optInt("id"));
                    modelvarOpt.setName(jsonobject.optString("name"));

                    modelvar.add(modelvarOpt);

                    // Populate spinner with country names
                    modelvarlist.add(jsonobject.optString("name"));

                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml
            Spinner mySpinner = (Spinner) findViewById(R.id.profile_spinner);

            // Spinner adapter
            mySpinner
                    .setAdapter(new ArrayAdapter<String>(NewUserVehicleDetails.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            modelvarlist));

            // Spinner on item click listener
            mySpinner
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
        }
    }


    public void GetModelVarDropdon(final String name) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.URL_GET_MODEL_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

                try {
                    JSONObject responseObj = new JSONObject(response);
                    modelvar = new ArrayList<ModelVar>();
                    // Create an array to populate the spinner
                    modelvarlist  = new ArrayList<String>();


                    try {
                        // Locate the NodeList name
                        jsonarray = responseObj.getJSONArray("model");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            responseObj = jsonarray.getJSONObject(i);

                            ModelVar modelvarOpt = new ModelVar();

                            modelvarOpt.setId(responseObj.optInt("id"));
                            modelvarOpt.setName(responseObj.optString("name"));

                            modelvar.add(modelvarOpt);

                            // Populate spinner with Manufacture names
                            modelvarlist.add(responseObj.optString("name"));


                            Spinner mySpinner = (Spinner) findViewById(R.id.profile_spinner);
                            mySpinner
                                    .setAdapter(new ArrayAdapter<String>(NewUserVehicleDetails.this,
                                            android.R.layout.simple_spinner_dropdown_item,
                                            modelvarlist));

                        }
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }


                   /* } else {

                        Toast.makeText(getApplicationContext(),
                                "Error: " + "helloErrorishere",
                                Toast.LENGTH_LONG).show();
                    }*/

                    // hiding the progress bar
                    // progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + "helloErrorishere",
                            Toast.LENGTH_LONG).show();

                    // progressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                //  progressBar.setVisibility(View.GONE);
            }
        }) {

            /**
             * Passing user parameters to our server
             * @return
             */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);


                Log.e(TAG, "Posting params: " + params.toString());

                return params;
            }

        };

        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }



}
