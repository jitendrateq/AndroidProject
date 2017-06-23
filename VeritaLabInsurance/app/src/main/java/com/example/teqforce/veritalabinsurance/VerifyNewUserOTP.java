package com.example.teqforce.veritalabinsurance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teqforce.veritalabinsurance.service.HttpService;

public class VerifyNewUserOTP extends AppCompatActivity {

    Button btn_back;
    private Button  btnVerifyOtp;
    private EditText  inputOtp;
    Boolean error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_new_user_otp);
        btnVerifyOtp = (Button) findViewById(R.id.btn_Verify);
        inputOtp = (EditText) findViewById(R.id.input_OTP);
        final Context context = this;

        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                switch (view.getId()) {


                    case R.id.btn_Verify:
                        verifyOtp();
                        // Intent intent = new Intent(context, NewUserVehicleDetails.class);
                        //  startActivity(intent);
                        break;


                }
            }
        });
    }

    private void verifyOtp() {
        String otp = inputOtp.getText().toString().trim();

        if (!otp.isEmpty()) {
            Intent grapprIntent = new Intent(getApplicationContext(), HttpService.class);
            grapprIntent.putExtra("otp", otp);
            startService(grapprIntent);

        } else {
            Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
        }

    }
}
