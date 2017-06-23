package com.example.teqforce.veritalabinsurance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class NewUserVehicleInteriorCondition extends AppCompatActivity {

    Button btnOdometer;
    Button btnFront;
    Button btnBack;
    String imgPath;
    String imgType;
    private int PICK_IMAGE_REQUEST = 1;
    final private int CAPTURE_IMAGE = 2;
    private String selectedImagePath = "";
    private static final int STORAGE_PERMISSION_CODE = 123;
    TextView txtOdometer;
    TextView txtFront;
    TextView txtBack;
    Boolean Error=false;
    ImageButton ImgBtn_Odometer;
    ImageButton ImgBtn_Front;
    ImageButton ImgBtn_Back;
    Button BtnProceed;

    Boolean Odometer_Img = false;
    Boolean Front_Img = false;
    Boolean Back_Img = false;

    private Context mContext;
    private Activity mActivity;
    private RelativeLayout mRelativeLayout;
    private Button mButton;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_vehicle_interior_condition);
        btnOdometer = (Button) findViewById(R.id.btnOdometer);
        btnFront = (Button) findViewById(R.id.btnFront);
        btnBack = (Button) findViewById(R.id.btnBack);

        txtOdometer = (TextView)  findViewById(R.id.txtOdometer);
        txtFront = (TextView)  findViewById(R.id.txtFront);
        txtBack = (TextView)  findViewById(R.id.txtBack);

        ImgBtn_Odometer = (ImageButton)  findViewById(R.id.imageButton_Odometer);
        ImgBtn_Front = (ImageButton)  findViewById(R.id.imageButton_Front);
        ImgBtn_Back = (ImageButton)  findViewById(R.id.imageButton_Back);

        BtnProceed = (Button) findViewById(R.id.btn_proceed);

        btnOdometer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                imgType="Odometer"; ClickPhotoChooser();

            }
        });
        btnFront.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                imgType="Front"; ClickPhotoChooser();

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                imgType="Back"; ClickPhotoChooser();

            }
        });


        ImgBtn_Odometer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                uploadMultipart(txtOdometer.getText().toString(),"Odometer");
                Odometer_Img = true;
            }
        });
        ImgBtn_Front.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                uploadMultipart(txtFront.getText().toString(),"Front");
                Front_Img = true;
            }
        });
        ImgBtn_Back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                uploadMultipart(txtBack.getText().toString(),"Back");
                Back_Img = true;
            }
        });


        BtnProceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MoveNextActivity();
            }
        });
    }

    private void MoveNextActivity()
    {
        TextView CustomPopupMsg = (TextView) findViewById(R.id.tvCustomPopup);
        if(!Odometer_Img || !Front_Img || !Back_Img) {
            CustomPopupMsg.setText("Please upload images.");
            AddPupUp();
        }
        else {
            final Context context = this;
            Intent intent = new Intent(context, NewUserSummaryDetails.class);
            startActivity(intent);
        }
    }

    private void ClickPhotoChooser() {

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
        startActivityForResult(intent, CAPTURE_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_CANCELED) {
             if (requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK ) {
                selectedImagePath = getImagePath();
                if(imgType=="Odometer")
                {
                    txtOdometer.setText(selectedImagePath);
                }
                 else if(imgType=="Front")
                {
                    txtFront.setText(selectedImagePath);
                }
                 else if(imgType=="Back")
                {
                    txtBack.setText(selectedImagePath);
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }



    public String getImagePath() {
        return imgPath;
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void uploadMultipart(String selectedImagePath, String ImgName) {

        String name = ImgName;
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

    public void AddPupUp()
    {
        mContext = getApplicationContext();
        mActivity = com.example.teqforce.veritalabinsurance.NewUserVehicleInteriorCondition.this;
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.custom_popup,null);
        mPopupWindow = new PopupWindow(
                customView,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }
        Button closeButton = (Button) customView.findViewById(R.id.ib_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);
    }
}
